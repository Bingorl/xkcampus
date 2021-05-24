package com.biu.wifi.campus.service;

import com.biu.wifi.campus.Tool.PushTool;
import com.biu.wifi.campus.constant.AuditBusinessType;
import com.biu.wifi.campus.constant.PushMsgType;
import com.biu.wifi.campus.dao.FileReceiveAuditInfoMapper;
import com.biu.wifi.campus.dao.PushMapper;
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
public class FileReceiveAuditInfoService extends AbstractAuditUserService{
    @Autowired
    private FileReceiveAuditInfoMapper fileReceiveAuditInfoMapper;
    @Autowired
    private FileReceiveAuditService fileReceiveAuditService;
    @Autowired
    private AuditInfoService auditInfoService;
    @Autowired
    private UserMapper userMapper;
   @Autowired
    private PushMapper pushMapper;
   @Autowired
    private FileReceiveNoticeService fileReceiveNoticeService;

    public void add(FileReceiveAuditInfo req) {
        setCurrentAuditUserId(req);
        req.setCreateTime(new Date());
        boolean fileInfoAdd= fileReceiveAuditInfoMapper.insertSelective(req)>0;
        FileReceiveAudit fileReceiveAudit=new FileReceiveAudit();
        fileReceiveAudit.setReceiveId(req.getId());
        fileReceiveAudit.setUserId(req.getCurrentAuditUserId());
        boolean fileAuditAdd=fileReceiveAuditService.add(fileReceiveAudit)>0;
        AuditInfo auditInfo = new AuditInfo();
        auditInfo.setType(AuditBusinessType.FILE_RECEIVE.getCode().shortValue());
        auditInfo.setBizId(fileReceiveAudit.getId());
        auditInfo.setUserId(req.getCurrentAuditUserId());
        boolean addAuditInfo = auditInfoService.add(auditInfo) > 0;
        // 推送请假审批通知给第一个审批人
        User user = userMapper.selectByPrimaryKey(req.getCurrentAuditUserId());
        String deviceToken = user.getDevToken();
        Short deviceType = user.getDevType();
        addPush(req.getId(), fileReceiveAudit.getId(), 1, "您有新的文件签发待处理", req.getCurrentAuditUserId(), deviceType, deviceToken);

        if (!(fileInfoAdd && fileAuditAdd && addAuditInfo)) {
            throw new BizException(Result.CUSTOM_MESSAGE, "申请失败");
        }
    }

    private void setCurrentAuditUserId(FileReceiveAuditInfo req) {
        List<Integer> auditUserIds = new ArrayList<>();
        for (String userId : req.getAuditUser().split(",")) {
            auditUserIds.add(Integer.valueOf(userId));
        }

        HashMap hashMap = setAuditUser(req.getCurrentAuditUserId(), auditUserIds);
        req.setAuditUser(MapUtils.getString(hashMap, "auditUser"));
        req.setCurrentAuditUserId(MapUtils.getInteger(hashMap, "currentAuditUserId"));
    }
    public void addPush(Integer applyId, Integer fileNoticeId, Integer needToAudit,
                        String title, Integer receiverId, Short deviceType, String deviceToken) {
        // 推送
        Boolean flag = false;
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("id", "");
        hm.put("title", title);
        hm.put("type", PushMsgType.FILE_RECEIVE_NOTICE.getCode());//请假审批类型
        hm.put("applyId", applyId == null ? "" : applyId);
        hm.put("fileNoticeId", fileNoticeId == null ? "" : fileNoticeId);
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
        puEntity.setMessageType(PushMsgType.FILE_RECEIVE_NOTICE.getCode().shortValue());
        puEntity.setDeviceType(deviceType);
        puEntity.setTitle(title);
        if (flag) {
            puEntity.setType((short) 10);
        } else {
            puEntity.setType((short) 50);
        }
        pushMapper.insertSelective(puEntity);
    }

    public FileReceiveAuditInfo selectByPrimaryKey(Integer receiveId) {

        return  fileReceiveAuditInfoMapper.selectByPrimaryKey(receiveId);
    }

    public void cancel(FileReceiveAuditInfo fileReceiveAuditInfo) {
        FileReceiveAuditInfo update=new FileReceiveAuditInfo();
        update.setStatus((short) 4);
        update.setUpdateTime(new Date());
        FileReceiveAuditInfoExample example = new FileReceiveAuditInfoExample();
        example.createCriteria()
                .andIdEqualTo(fileReceiveAuditInfo.getId());
        boolean result = fileReceiveAuditInfoMapper.updateByExampleSelective(update, example) > 0;
        if (!result) {
            throw new BizException(Result.CUSTOM_MESSAGE, "取消失败");
        }
    }

    public List<HashMap> myInfoList(Integer userId, String referenceNum, String title, Short status) {
        return fileReceiveAuditInfoMapper.myStampApplyInfoList(userId, referenceNum, title, getStatusList(status, true));

    }

    public List<HashMap> myAuditfileInfoList(Integer userId, String referenceNum, String title, Short status) {
        return fileReceiveAuditInfoMapper.myAuditLeaveInfoList(userId, referenceNum, title, getStatusList(status, false));

    }

    public void audit(FileReceiveAuditInfo info, Short status, String remark) {
        int currentAuditUserId = info.getCurrentAuditUserId();
        boolean currentAuditUserIsTheLast = false;
        // 是否需要进入到下一个审核节点,设置下一个审批人id
        setCurrentAuditUserId(info);
        int nextAuditUserId = currentAuditUserId;
        if (info.getCurrentAuditUserId() == null) {
            // 当前审核人为最后一个
            currentAuditUserIsTheLast = true;
        } else {
            nextAuditUserId = info.getCurrentAuditUserId();
        }

        FileReceiveAudit fileReceiveAudit = fileReceiveAuditService.selectByLeaveId(info.getId(), currentAuditUserId);
        Assert.notNull(fileReceiveAudit, "该文件签发记录不存在");
        fileReceiveAudit.setIsPass(status.intValue() == 2 ? (short) 1 : (short) 2);
        fileReceiveAudit.setRemark(remark);
        fileReceiveAudit.setAuditTime(new Date());
        boolean updateLeaveAudit = fileReceiveAuditService.update(fileReceiveAudit) > 0;
        boolean updateAuditInfo = auditInfoService.update(fileReceiveAudit.getId(), currentAuditUserId,
                AuditBusinessType.FILE_RECEIVE.getCode().shortValue(), fileReceiveAudit.getIsPass()) > 0;

        if (!(updateLeaveAudit && updateAuditInfo)) {
            throw new BizException(Result.CUSTOM_MESSAGE, "审核失败");
        }

        // 推送通知
        FileReceiveNotice fileReceiveNotice;
        Integer receiverId, receiveNoticeId, needToAudit = 0;
        String title;
        User user;
        if (status.intValue() == 3) {
            // 修改审核状态
            info.setStatus(status);
            info.setCurrentAuditUserId(currentAuditUserId);
            info.setUpdateTime(new Date());
            fileReceiveAuditInfoMapper.updateByPrimaryKeySelective(info);

            // 审核驳回,通知请假人请假结果
            fileReceiveNotice = fileReceiveNoticeService.addfileNotice(info.getId(), "您的文件未通过审批", info.getContent(), remark, info.getApplyUserId());
            Assert.notNull(fileReceiveNotice.getId(), "审核失败");
            // 推送请假审批结果通知给请假人
            receiverId = info.getApplyUserId();
            title = "您提交的文件未通过审批";
            user = userMapper.selectByPrimaryKey(receiverId);
            receiveNoticeId = fileReceiveNotice.getId();
        } else {
            if (currentAuditUserIsTheLast) {
                // 修改审核状态
                info.setStatus(status);
                info.setCurrentAuditUserId(currentAuditUserId);
                info.setUpdateTime(new Date());
                fileReceiveAuditInfoMapper.updateByPrimaryKeySelective(info);
                // 通知请假人请假结果
                fileReceiveNotice = fileReceiveNoticeService.addfileNotice(info.getId(), "您的申请已通过审批", info.getContent(), remark, info.getApplyUserId());
                Assert.notNull(fileReceiveNotice.getId(), "审核失败");
                // 推送请假审批结果通知给请假人
                receiverId = info.getApplyUserId();
                title = "您提交的文件已通过审批";
                user = userMapper.selectByPrimaryKey(receiverId);
                receiveNoticeId = fileReceiveNotice.getId();
            } else {
                // 修改当前审核人
                info.setCurrentAuditUserId(nextAuditUserId);
                info.setUpdateTime(new Date());
                fileReceiveAuditInfoMapper.updateByPrimaryKeySelective(info);

                // 新增下一个审批人审批记录，状态为空
                FileReceiveAudit nextLeaveAudit = new FileReceiveAudit();
                nextLeaveAudit.setReceiveId(info.getId());
                nextLeaveAudit.setUserId(nextAuditUserId);
                nextLeaveAudit.setCreateTime(new Date());
                boolean addLeaveAudit = fileReceiveAuditService.add(nextLeaveAudit) > 0;
                // 新增审核操作记录
                AuditInfo auditInfo = new AuditInfo();
                auditInfo.setType(AuditBusinessType.FILE_RECEIVE.getCode().shortValue());
                auditInfo.setBizId(nextLeaveAudit.getId());
                auditInfo.setUserId(nextAuditUserId);
                boolean addAuditInfo = auditInfoService.add(auditInfo) > 0;
                if (!(addLeaveAudit && addAuditInfo)) {
                    throw new BizException(Result.CUSTOM_MESSAGE, "审核失败");
                }

                // 推送请假审批通知给下一个审批人
                receiverId = nextAuditUserId;
                title = "您有新的文件请求待处理";
                user = userMapper.selectByPrimaryKey(receiverId);
                receiveNoticeId = null;
                needToAudit = 1;
            }

        }
        addPush(info.getId(), receiveNoticeId, needToAudit, title, receiverId, user.getDevType(), user.getDevToken());

    }
}
