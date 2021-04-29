package com.biu.wifi.campus.service;

import com.biu.wifi.campus.Tool.PushTool;
import com.biu.wifi.campus.constant.AuditBusinessType;
import com.biu.wifi.campus.constant.PushMsgType;
import com.biu.wifi.campus.dao.PushMapper;
import com.biu.wifi.campus.dao.StampToApplyInfoMapper;
import com.biu.wifi.campus.dao.StampToApplyMapper;
import com.biu.wifi.campus.dao.UserMapper;
import com.biu.wifi.campus.dao.model.*;
import com.biu.wifi.campus.exception.BizException;
import com.biu.wifi.campus.result.Result;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @Author MCX
 * @Version 1.0
 **/
@Service
public class StampToApplyInfoService extends AbstractAuditUserService {

    @Autowired
    private AuditInfoService auditInfoService;
    @Autowired
    private StampToApplyNoticeService stampToApplyNoticeService;
    @Autowired
    private StampToApplyUserService stampToApplyUserService;
    @Autowired
    private PushMapper pushMapper;
    @Autowired
    private StampToApplyInfoMapper stampToApplyInfoMapper;
    @Autowired
    private StampToApplyService stampToApplyService;
    @Autowired
    private StampToApplyMapper stampToApplyMapper;
    @Autowired
    private UserMapper userMapper;

    public void add(StampToApplyInfo stampToApplyInfo){
        setCurrentAuditUserId(stampToApplyInfo);
        stampToApplyInfo.setCreateTime(new Date());
        boolean result = stampToApplyInfoMapper.insertSelective(stampToApplyInfo) > 0;

        StampToApply teacherLeaveAudit = new StampToApply();
        teacherLeaveAudit.setApplyId(stampToApplyInfo.getId());
        teacherLeaveAudit.setUserId(stampToApplyInfo.getCurrentAuditUserId());
        boolean addLeaveAudit = stampToApplyService.add(teacherLeaveAudit) > 0;

        //插入汇总记录
        AuditInfo auditInfo = new AuditInfo();
        auditInfo.setType(AuditBusinessType.TEACHER_LEAVE.getCode().shortValue());
        auditInfo.setBizId(teacherLeaveAudit.getId());
        auditInfo.setUserId(stampToApplyInfo.getCurrentAuditUserId());
        boolean addAuditInfo = auditInfoService.add(auditInfo) > 0;

        // 推送请假审批通知给第一个审批人
        User user = userMapper.selectByPrimaryKey(stampToApplyInfo.getCurrentAuditUserId());
        String deviceToken = user.getDevToken();
        Short deviceType = user.getDevType();
        addPush(stampToApplyInfo.getId(), teacherLeaveAudit.getId(), 1, "您有新的用章申请审批待处理", stampToApplyInfo.getCurrentAuditUserId(), deviceType, deviceToken);

        if (!(result && addLeaveAudit && addAuditInfo)) {
            throw new BizException(Result.CUSTOM_MESSAGE, "申请失败");
        }
    }
    public StampToApplyInfo selectByPrimaryKey(Integer applyId){
        StampToApplyInfo stampToApplyInfo = stampToApplyInfoMapper.selectByPrimaryKey(applyId);
        return stampToApplyInfo;
    }

    public void cancel(StampToApplyInfo applyInfo) {
        applyInfo.setIsDelete((short) 1);
        int i = stampToApplyInfoMapper.updateByPrimaryKeySelective(applyInfo);
        if (i!=1) {
            throw new BizException(Result.CUSTOM_MESSAGE, "取消失败");
        }
    }

    public List<HashMap> myStampInfoList(Integer userId, String startDate, String endDate, Short status) {
        return stampToApplyInfoMapper.myStampApplyInfoList(userId, startDate, endDate, getStatusList(status, true));
    }

    public List<HashMap> myAuditApplyInfoList(Integer userId, String startDate, String endDate, Short status) {
        return stampToApplyInfoMapper.myAuditStampApplyInfoList(userId,startDate,endDate,getStatusList(status, false));
    }

    public void audit(StampToApplyInfo stampToApplyInfo, Short status, String remark) {
        int currentAuditUserId = stampToApplyInfo.getCurrentAuditUserId();
        boolean currentAuditUserIsTheLast = false;
        // 是否需要进入到下一个审核节点,设置下一个审批人id
        setCurrentAuditUserId(stampToApplyInfo);
        int nextAuditUserId = currentAuditUserId;
        if (stampToApplyInfo.getCurrentAuditUserId() == null) {
            // 当前审核人为最后一个
            currentAuditUserIsTheLast = true;
        } else {
            nextAuditUserId = stampToApplyInfo.getCurrentAuditUserId();
        }

        StampToApply stampToApply = stampToApplyService.selectByLeaveId(stampToApplyInfo.getId(), currentAuditUserId);
        Assert.notNull(stampToApply, "该用章申请审批记录不存在");
        stampToApply.setIsPass(status.intValue() == 2 ? (short) 1 : (short) 2);
        stampToApply.setRemark(remark);
        stampToApply.setAuditTime(new Date());
        boolean updateLeaveAudit = stampToApplyService.update(stampToApply) > 0;
        boolean updateAuditInfo = auditInfoService.update(stampToApply.getId(), currentAuditUserId,
                AuditBusinessType.STAMP_TO_APPLY.getCode().shortValue(), stampToApply.getIsPass()) > 0;

        if (!(updateLeaveAudit && updateAuditInfo)) {
            throw new BizException(Result.CUSTOM_MESSAGE, "审核失败");
        }

        // 推送通知
        StampToApplyNotice teacherLeaveNotice;
        Integer receiverId, leaveNoticeId, needToAudit = 0;
        String title;
        User user;
        if (status.intValue() == 3) {
            // 修改审核状态
            stampToApplyInfo.setStatus(status);
            stampToApplyInfo.setCurrentAuditUserId(currentAuditUserId);
            stampToApplyInfo.setUpdateTime(new Date());
            stampToApplyInfoMapper.updateByPrimaryKeySelective(stampToApplyInfo);

            // 审核驳回,通知请假人请假结果
            teacherLeaveNotice = stampToApplyNoticeService.addStampNotice(stampToApplyInfo.getId(), "您的申请未通过审批", stampToApplyInfo.getReason(), remark, stampToApplyInfo.getApplyUserId());
            Assert.notNull(teacherLeaveNotice.getId(), "审核失败");
            // 推送请假审批结果通知给请假人
            receiverId = stampToApplyInfo.getApplyUserId();
            title = "您提交的申请未通过审批";
            user = userMapper.selectByPrimaryKey(receiverId);
            leaveNoticeId = teacherLeaveNotice.getId();
        } else {
            if (currentAuditUserIsTheLast) {
                // 修改审核状态
                stampToApplyInfo.setStatus(status);
                stampToApplyInfo.setCurrentAuditUserId(currentAuditUserId);
                stampToApplyInfo.setUpdateTime(new Date());
                stampToApplyInfoMapper.updateByPrimaryKeySelective(stampToApplyInfo);
                // 通知请假人请假结果
                teacherLeaveNotice = stampToApplyNoticeService.addStampNotice(stampToApplyInfo.getId(), "您的申请已通过审批", stampToApplyInfo.getReason(), remark, stampToApplyInfo.getApplyUserId());
                Assert.notNull(teacherLeaveNotice.getId(), "审核失败");
                // 推送请假审批结果通知给请假人
                receiverId = stampToApplyInfo.getApplyUserId();
                title = "您提交的请假已通过审批";
                user = userMapper.selectByPrimaryKey(receiverId);
                leaveNoticeId = teacherLeaveNotice.getId();
            } else {
                // 修改当前审核人
                stampToApplyInfo.setCurrentAuditUserId(nextAuditUserId);
                stampToApplyInfo.setUpdateTime(new Date());
                stampToApplyInfoMapper.updateByPrimaryKeySelective(stampToApplyInfo);

                // 新增下一个审批人审批记录，状态为空
                StampToApply nextLeaveAudit = new StampToApply();
                nextLeaveAudit.setApplyId(stampToApplyInfo.getId());
                nextLeaveAudit.setUserId(nextAuditUserId);
                nextLeaveAudit.setCreateTime(new Date());
                boolean addLeaveAudit = stampToApplyService.add(nextLeaveAudit) > 0;
                // 新增审核操作记录
                AuditInfo auditInfo = new AuditInfo();
                auditInfo.setType(AuditBusinessType.STAMP_TO_APPLY.getCode().shortValue());
                auditInfo.setBizId(nextLeaveAudit.getId());
                auditInfo.setUserId(nextAuditUserId);
                boolean addAuditInfo = auditInfoService.add(auditInfo) > 0;
                if (!(addLeaveAudit && addAuditInfo)) {
                    throw new BizException(Result.CUSTOM_MESSAGE, "审核失败");
                }

                // 推送请假审批通知给下一个审批人
                receiverId = nextAuditUserId;
                title = "您有新的请假审批待处理";
                user = userMapper.selectByPrimaryKey(receiverId);
                leaveNoticeId = null;
                needToAudit = 1;
            }
        }


    }


    private void setCurrentAuditUserId(StampToApplyInfo req) {
        List<Integer> auditUserIds = new ArrayList<>();
        for (String userId : req.getAuditUser().split(",")) {
                auditUserIds.add(Integer.valueOf(userId));
        }

        HashMap hashMap = setAuditUser(req.getCurrentAuditUserId(), auditUserIds);
        req.setAuditUser(MapUtils.getString(hashMap, "auditUser"));
        req.setCurrentAuditUserId(MapUtils.getInteger(hashMap, "currentAuditUserId"));
    }
    public void addPush(Integer applyId, Integer stampNoticeId, Integer needToAudit,
                        String title, Integer receiverId, Short deviceType, String deviceToken) {
        // 推送
        Boolean flag = false;
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("id", "");
        hm.put("title", title);
        hm.put("type", PushMsgType.STAMP_TO_APPLY_NOTICE.getCode());//请假审批类型
        hm.put("applyId", applyId == null ? "" : applyId);
        hm.put("stampNoticeId", stampNoticeId == null ? "" : stampNoticeId);
        hm.put("needToAudit", needToAudit);

        try {
            flag = PushTool.pushToUser(receiverId, "", title, hm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 入推送表
        Push puEntity = new Push();
        puEntity.setToken(deviceToken);
        puEntity.setContent("");
        puEntity.setUserId(receiverId);
        puEntity.setMessageType(PushMsgType.STAMP_TO_APPLY_NOTICE.getCode().shortValue());
        puEntity.setDeviceType(deviceType);
        puEntity.setTitle(title);
        if (flag) {
            puEntity.setType((short) 10);
        } else {
            puEntity.setType((short) 50);
        }

        pushMapper.insertSelective(puEntity);
    }
}
