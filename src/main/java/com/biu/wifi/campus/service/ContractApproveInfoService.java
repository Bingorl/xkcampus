package com.biu.wifi.campus.service;

import com.biu.wifi.campus.Tool.PushTool;
import com.biu.wifi.campus.constant.AuditBusinessType;
import com.biu.wifi.campus.constant.PushMsgType;
import com.biu.wifi.campus.dao.ContractApproveAuditUserMapper;
import com.biu.wifi.campus.dao.ContractApproveInfoMapper;
import com.biu.wifi.campus.dao.PushMapper;
import com.biu.wifi.campus.dao.UserMapper;
import com.biu.wifi.campus.dao.model.*;
import com.biu.wifi.campus.exception.BizException;
import com.biu.wifi.campus.result.Result;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
public class ContractApproveInfoService extends AbstractAuditUserService{
    @Autowired
    private ContractApproveInfoMapper contractApproveInfoMapper;
    @Autowired
    private ContractApproveAuditServcie contractApproveAuditService;
    @Autowired
    private AuditInfoService auditInfoService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PushMapper pushMapper;
    @Autowired
    private ContractApproveNoticeService contractApproveNoticeService;
    @Autowired
    private ContractApproveAuditUserMapper contractApproveAuditUserMapper;


    @Transactional(rollbackFor = {BizException.class, IllegalArgumentException.class})
    public void addContractApproveInfo(ContractApproveInfo req) {
        req.setCreateTime(new Date());
        req.setIsDelete((short)2);
        req.setStatus((short) 1);
        setCurrentAuditUserId(req);
        boolean result = contractApproveInfoMapper.insertSelective(req) > 0;

        ContractApproveAudit contractApproveAudit = new ContractApproveAudit();
        contractApproveAudit.setApproveId(req.getId());
        contractApproveAudit.setUserId(req.getCurrentAuditUserId());
        boolean addLeaveAudit = contractApproveAuditService.add(contractApproveAudit) > 0;

        //插入汇总记录
        AuditInfo auditInfo = new AuditInfo();
        auditInfo.setType(AuditBusinessType.CONTRACT_APPROVE.getCode().shortValue());
        auditInfo.setBizId(contractApproveAudit.getId());
        auditInfo.setUserId(req.getCurrentAuditUserId());
        boolean addAuditInfo = auditInfoService.add(auditInfo) > 0;
        // 推送请假审批通知给第一个审批人
        User user = userMapper.selectByPrimaryKey(req.getCurrentAuditUserId());
        String deviceToken = user.getDevToken();
        Short deviceType = user.getDevType();
        addPush(req.getId(), contractApproveAudit.getId(), 1, "您有新的合同审批待处理", req.getCurrentAuditUserId(), deviceType, deviceToken);

        if (!(result && addLeaveAudit && addAuditInfo)) {
            throw new BizException(Result.CUSTOM_MESSAGE, "申请失败");
        }
    }

    private void setCurrentAuditUserId(ContractApproveInfo req) {
        List<Integer> auditUserIds=new ArrayList<>();
        User user = userMapper.selectByPrimaryKey(req.getApplyUserId());
        ContractApproveAuditUserExample example=new ContractApproveAuditUserExample();
        example.createCriteria().andSchoolIdEqualTo(user.getSchoolId())
                .andInstituteIdEqualTo(user.getInstituteId());
        List<ContractApproveAuditUser> contractApproveAuditUsers = contractApproveAuditUserMapper.selectByExample(example);
        for (String s : contractApproveAuditUsers.get(0).getAuditUser().split(",")) {
            auditUserIds.add(Integer.valueOf(s));
        }
        HashMap hashMap = setAuditUser(req.getCurrentAuditUserId(), auditUserIds);
        req.setAuditUser(MapUtils.getString(hashMap, "auditUser"));
        req.setCurrentAuditUserId(MapUtils.getInteger(hashMap, "currentAuditUserId"));
    }

    /**
     * 推送
     * @param approveId
     * @param approveNoticeId
     * @param needToAudit
     * @param title
     * @param receiverId
     * @param deviceType
     * @param deviceToken
     */
    public void addPush(Integer approveId, Integer approveNoticeId, Integer needToAudit,
                        String title, Integer receiverId, Short deviceType, String deviceToken) {
        // 推送
        Boolean flag = false;
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("id", "");
        hm.put("title", title);
        hm.put("type", PushMsgType.CONTRACT_APPROVE_NOTICE.getCode());//请假审批类型
        hm.put("approveId", approveId == null ? "" : approveId);
        hm.put("approveNoticeId", approveNoticeId == null ? "" : approveNoticeId);
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
        puEntity.setMessageType(PushMsgType.CONTRACT_APPROVE_NOTICE.getCode().shortValue());
        puEntity.setDeviceType(deviceType);
        puEntity.setTitle(title);
        if (flag) {
            puEntity.setType((short) 10);
        } else {
            puEntity.setType((short) 50);
        }

        pushMapper.insertSelective(puEntity);
    }

    public ContractApproveInfo selectByPrimaryKey(Integer approveId) {
        return contractApproveInfoMapper.selectByPrimaryKey(approveId);
    }

    public void cancel(ContractApproveInfo info) {
        ContractApproveInfo update=new ContractApproveInfo();
        update.setStatus((short) 4);
        update.setUpdateTime(new Date());
        ContractApproveInfoExample example = new ContractApproveInfoExample();
        example.createCriteria()
                .andIdEqualTo(info.getId());
        boolean result = contractApproveInfoMapper.updateByExampleSelective(update, example) > 0;
        if (!result) {
            throw new BizException(Result.CUSTOM_MESSAGE, "取消失败");
        }
    }

    public List<HashMap> myApproveInfoList(Integer userId, String title, Short status) {
        return contractApproveInfoMapper. myApproveInfoList(userId,title,getStatusList(status, true));
    }

    public List<HashMap> myAuditapproveInfoList(Integer userId, String title, Short status) {
        return contractApproveInfoMapper. myAuditapproveInfoList(userId,title,getStatusList(status, false));
    }

    public void audit(ContractApproveInfo info, Short status, String remark) {
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

        ContractApproveAudit contractApproveAudit =contractApproveAuditService.selectByApproveId(info.getId(), currentAuditUserId);
        Assert.notNull(contractApproveAudit, "该合同申请审批记录不存在");
        contractApproveAudit.setIsPass(status.intValue() == 2 ? (short) 1 : (short) 2);
        contractApproveAudit.setRemark(remark);
        contractApproveAudit.setAuditTime(new Date());
        boolean updateLeaveAudit = contractApproveAuditService.update(contractApproveAudit) > 0;
        boolean updateAuditInfo = auditInfoService.update(contractApproveAudit.getId(), currentAuditUserId,
                AuditBusinessType.CONTRACT_APPROVE.getCode().shortValue(), contractApproveAudit.getIsPass()) > 0;

        if (!(updateLeaveAudit && updateAuditInfo)) {
            throw new BizException(Result.CUSTOM_MESSAGE, "审核失败");
        }

        // 推送通知
        ContractApproveNotice approveNotice;
        Integer receiverId, approveNoticeId, needToAudit = 0;
        String title;
        User user;
        if (status.intValue() == 3) {
            // 修改审核状态
            info.setStatus(status);
            info.setCurrentAuditUserId(currentAuditUserId);
            info.setUpdateTime(new Date());
            contractApproveInfoMapper.updateByPrimaryKeySelective(info);

            // 审核驳回,通知请假人请假结果
            approveNotice = contractApproveNoticeService.addLeaveNotice(info.getId(), "您的合同申请未通过审批", info.getTitle(), remark, info.getApplyUserId());
            Assert.notNull(approveNotice.getId(), "审核失败");
            // 推送请假审批结果通知给请假人
            receiverId = info.getApplyUserId();
            title = "您提交的合同未通过审批";
            user = userMapper.selectByPrimaryKey(receiverId);
            approveNoticeId = approveNotice.getId();
        } else {
            if (currentAuditUserIsTheLast) {
                // 修改审核状态
                info.setStatus(status);
                info.setCurrentAuditUserId(currentAuditUserId);
                info.setUpdateTime(new Date());
                contractApproveInfoMapper.updateByPrimaryKeySelective(info);
                // 通知请假人请假结果
                approveNotice = contractApproveNoticeService.addLeaveNotice(info.getId(), "您的合同申请已通过审批", info.getTitle(), remark, info.getApplyUserId());
                Assert.notNull(approveNotice.getId(), "审核失败");
                // 推送请假审批结果通知给请假人
                receiverId = info.getApplyUserId();
                title = "您提交的合同已通过审批";
                user = userMapper.selectByPrimaryKey(receiverId);
                approveNoticeId = approveNotice.getId();
            } else {
                // 修改当前审核人
                info.setCurrentAuditUserId(nextAuditUserId);
                info.setUpdateTime(new Date());
                contractApproveInfoMapper.updateByPrimaryKeySelective(info);

                // 新增下一个审批人审批记录，状态为空
                ContractApproveAudit nextLeaveAudit = new ContractApproveAudit();
                nextLeaveAudit.setApproveId(info.getId());
                nextLeaveAudit.setUserId(nextAuditUserId);
                nextLeaveAudit.setCreateTime(new Date());
                boolean addLeaveAudit = contractApproveAuditService.add(nextLeaveAudit) > 0;
                // 新增审核操作记录
                AuditInfo auditInfo = new AuditInfo();
                auditInfo.setType(AuditBusinessType.CONTRACT_APPROVE.getCode().shortValue());
                auditInfo.setBizId(nextLeaveAudit.getId());
                auditInfo.setUserId(nextAuditUserId);
                boolean addAuditInfo = auditInfoService.add(auditInfo) > 0;
                if (!(addLeaveAudit && addAuditInfo)) {
                    throw new BizException(Result.CUSTOM_MESSAGE, "审核失败");
                }

                // 推送请假审批通知给下一个审批人
                receiverId = nextAuditUserId;
                title = "您有新的合同审批待处理";
                user = userMapper.selectByPrimaryKey(receiverId);
                approveNoticeId = null;
                needToAudit = 1;
            }
        }

        addPush(info.getId(), approveNoticeId, needToAudit, title, receiverId, user.getDevType(), user.getDevToken());
    }
}
