package com.biu.wifi.campus.service;

import com.biu.wifi.campus.Tool.PushTool;
import com.biu.wifi.campus.constant.AuditBusinessType;
import com.biu.wifi.campus.constant.PushMsgType;
import com.biu.wifi.campus.dao.AssertsUseAuditMapper;
import com.biu.wifi.campus.dao.AssertsUseInfoMapper;
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
public class AssertsUseInfoService extends AbstractAuditUserService {
    @Autowired
    private AssertsUseInfoMapper assertsUseInfoMapper;
    @Autowired
    private AssertsUseAuditService assertsUseAuditService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AuditInfoService auditInfoService;
    @Autowired
    private PushMapper pushMapper;
    @Autowired
    private AssertsUseNoticeService assertsUseNoticeService;
    @Autowired
    private ContractApproveAuditUserService contractApproveAuditUserService;


    public void addAssertsUseInfo(AssertsUseInfo req) {
        req.setCreateTime(new Date());
        req.setIsDelete((short)2);
        req.setStatus((short) 1);
        setCurrentAuditUserId(req);
        boolean result = assertsUseInfoMapper.insertSelective(req) > 0;

        AssertsUseAudit assertsUseAudit = new AssertsUseAudit();
        assertsUseAudit.setUseId(req.getId());
        assertsUseAudit.setUserId(req.getCurrentAuditUserId());
        boolean addLeaveAudit = assertsUseAuditService.add(assertsUseAudit) > 0;

        //插入汇总记录
        AuditInfo auditInfo = new AuditInfo();
        auditInfo.setType(AuditBusinessType.CONTRACT_APPROVE.getCode().shortValue());
        auditInfo.setBizId(assertsUseAudit.getId());
        auditInfo.setUserId(req.getCurrentAuditUserId());
        boolean addAuditInfo = auditInfoService.add(auditInfo) > 0;
        // 推送请假审批通知给第一个审批人
        User user = userMapper.selectByPrimaryKey(req.getCurrentAuditUserId());
        String deviceToken = user.getDevToken();
        Short deviceType = user.getDevType();
        addPush(req.getId(), assertsUseAudit.getId(), 1, "您有新的资产使用审批待处理", req.getCurrentAuditUserId(), deviceType, deviceToken);

        if (!(result && addLeaveAudit && addAuditInfo)) {
            throw new BizException(Result.CUSTOM_MESSAGE, "申请失败");
        }
    }

    private void setCurrentAuditUserId(AssertsUseInfo req) {
        List<Integer> auditUserIds= contractApproveAuditUserService.getAuditUserIds(req.getApplyUserId());
        HashMap hashMap = setAuditUser(req.getCurrentAuditUserId(), auditUserIds);
        req.setAuditUser(MapUtils.getString(hashMap, "auditUser"));
        req.setCurrentAuditUserId(MapUtils.getInteger(hashMap, "currentAuditUserId"));
    }

    public void addPush(Integer useId, Integer useNoticeId, Integer needToAudit,
                        String title, Integer receiverId, Short deviceType, String deviceToken) {
        // 推送
        Boolean flag = false;
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("id", "");
        hm.put("title", title);
        hm.put("type", PushMsgType.ASSERTS_USE_NOTICE.getCode());//请假审批类型
        hm.put("useId", useId == null ? "" : useId);
        hm.put("useNoticeId", useNoticeId == null ? "" : useNoticeId);
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
        puEntity.setMessageType(PushMsgType.ASSERTS_USE_NOTICE.getCode().shortValue());
        puEntity.setDeviceType(deviceType);
        puEntity.setTitle(title);
        if (flag) {
            puEntity.setType((short) 10);
        } else {
            puEntity.setType((short) 50);
        }

        pushMapper.insertSelective(puEntity);
    }

    public AssertsUseInfo selectByPrimaryKey(Integer useId) {
        AssertsUseInfo assertsUseInfo = assertsUseInfoMapper.selectByPrimaryKey(useId);
        return assertsUseInfo;
    }

    public void cancel(AssertsUseInfo info) {
        AssertsUseInfo update=new AssertsUseInfo();
        update.setStatus((short) 4);
        update.setUpdateTime(new Date());
        AssertsUseInfoExample example = new AssertsUseInfoExample();
        example.createCriteria()
                .andIdEqualTo(info.getId());
        boolean result = assertsUseInfoMapper.updateByExampleSelective(update, example) > 0;
        if (!result) {
            throw new BizException(Result.CUSTOM_MESSAGE, "取消失败");
        }
    }

    public List<HashMap> myUseInfoList(Integer userId, String startDate, String endDate, Short status) {
        return assertsUseInfoMapper.myUseInfoList( userId,  startDate,  endDate,  getStatusList(status,true));
    }

    public List<HashMap> myAuditUseInfoList(Integer userId, String startDate, String endDate, Short status) {
        return assertsUseInfoMapper.myAuditUseInfoList( userId,  startDate,  endDate,  getStatusList(status,false));

    }

    public void audit(AssertsUseInfo info, Short status, String remark) {
        // 当前审批人id
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

        AssertsUseAudit assertsUseAudit = assertsUseAuditService.selectByUseId(info.getId(), currentAuditUserId);
        Assert.notNull(assertsUseAudit, "该资产审批记录不存在");
        assertsUseAudit.setIsPass(status.intValue() == 2 ? (short) 1 : (short) 2);
        assertsUseAudit.setRemark(remark);
        assertsUseAudit.setAuditTime(new Date());
        boolean updateLeaveAudit = assertsUseAuditService.update(assertsUseAudit) > 0;
        boolean updateAuditInfo = auditInfoService.update(assertsUseAudit.getId(), currentAuditUserId,
                AuditBusinessType.ASSERTS_USE.getCode().shortValue(), assertsUseAudit.getIsPass()) > 0;

        if (!(updateLeaveAudit && updateAuditInfo)) {
            throw new BizException(Result.CUSTOM_MESSAGE, "审核失败");
        }

        // 推送通知
        AssertsUseNotice assertsUseNotice;
        Integer receiverId, useNoticeId, needToAudit = 0;
        String title;
        User user;
        if (status.intValue() == 3) {
            // 修改审核状态
            info.setStatus(status);
            info.setCurrentAuditUserId(currentAuditUserId);
            info.setUpdateTime(new Date());
            assertsUseInfoMapper.updateByPrimaryKeySelective(info);

            // 审核驳回,通知请假人请假结果
            assertsUseNotice =  assertsUseNoticeService.addUseNotice(info.getId(), "您的资产申请未通过审批", info.getReason(), remark, info.getApplyUserId());
            Assert.notNull(assertsUseNotice.getId(), "审核失败");
            // 推送请假审批结果通知给请假人
            receiverId = info.getApplyUserId();
            title = "您提交的申请未通过审批";
            user = userMapper.selectByPrimaryKey(receiverId);
            useNoticeId = assertsUseNotice.getId();
        } else {
            if (currentAuditUserIsTheLast) {
                // 修改审核状态
                info.setStatus(status);
                info.setCurrentAuditUserId(currentAuditUserId);
                info.setUpdateTime(new Date());
                assertsUseInfoMapper.updateByPrimaryKeySelective(info);
                // 通知请假人请假结果
                assertsUseNotice = assertsUseNoticeService.addUseNotice(info.getId(), "您的资产申请已通过审批", info.getReason(), remark, info.getApplyUserId());
                Assert.notNull(assertsUseNotice.getId(), "审核失败");
                // 推送请假审批结果通知给请假人
                receiverId = info.getApplyUserId();
                title = "您提交的申请已通过审批";
                user = userMapper.selectByPrimaryKey(receiverId);
                useNoticeId = assertsUseNotice.getId();
            } else {
                // 修改当前审核人
                info.setCurrentAuditUserId(nextAuditUserId);
                info.setUpdateTime(new Date());
                assertsUseInfoMapper.updateByPrimaryKeySelective(info);

                // 新增下一个审批人审批记录，状态为空
                AssertsUseAudit nextLeaveAudit = new AssertsUseAudit();
                nextLeaveAudit.setUseId(info.getId());
                nextLeaveAudit.setUserId(nextAuditUserId);
                nextLeaveAudit.setCreateTime(new Date());
                boolean addLeaveAudit = assertsUseAuditService.add(nextLeaveAudit) > 0;
                // 新增审核操作记录
                AuditInfo auditInfo = new AuditInfo();
                auditInfo.setType(AuditBusinessType.ASSERTS_USE.getCode().shortValue());
                auditInfo.setBizId(nextLeaveAudit.getId());
                auditInfo.setUserId(nextAuditUserId);
                boolean addAuditInfo = auditInfoService.add(auditInfo) > 0;
                if (!(addLeaveAudit && addAuditInfo)) {
                    throw new BizException(Result.CUSTOM_MESSAGE, "审核失败");
                }

                // 推送请假审批通知给下一个审批人
                receiverId = nextAuditUserId;
                title = "您有新的资产审批待处理";
                user = userMapper.selectByPrimaryKey(receiverId);
                useNoticeId = null;
                needToAudit = 1;
            }
        }

        addPush(info.getId(), useNoticeId, needToAudit, title, receiverId, user.getDevType(), user.getDevToken());
    }
}
