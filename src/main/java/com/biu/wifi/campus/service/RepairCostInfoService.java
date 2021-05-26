package com.biu.wifi.campus.service;

import com.biu.wifi.campus.Tool.PushTool;
import com.biu.wifi.campus.constant.AuditBusinessType;
import com.biu.wifi.campus.constant.PushMsgType;
import com.biu.wifi.campus.dao.PushMapper;
import com.biu.wifi.campus.dao.RepairCostInfoMapper;
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
public class RepairCostInfoService extends AbstractAuditUserService {
    @Autowired
    private RepairCostInfoMapper repairCostInfoMapper;
    @Autowired
    private RepairCostAuditService repairCostAuditService;
    @Autowired
    private AuditInfoService auditInfoService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PushMapper pushMapper;
    @Autowired
    private RepairCostNoticeService repairCostNoticeService;
    @Autowired
    private RepairCostAuditUserService repairCostAuditUserService;

    public void addRepairCostInfo(RepairCostInfo req){
        req.setCreateTime(new Date());
        req.setIsDelete((short)2);
        req.setStatus((short) 1);
        setCurrentAuditUserId(req);
        boolean result = repairCostInfoMapper.insertSelective(req) > 0;

        RepairCostAudit audit = new RepairCostAudit();
        audit.setRepairId(req.getId());
        audit.setUserId(req.getCurrentAuditUserId());
        boolean addLeaveAudit = repairCostAuditService.add(audit) > 0;

        //插入汇总记录
        AuditInfo auditInfo = new AuditInfo();
        auditInfo.setType(AuditBusinessType.REPAIR_COST.getCode().shortValue());
        auditInfo.setBizId(audit.getId());
        auditInfo.setUserId(req.getCurrentAuditUserId());
        boolean addAuditInfo = auditInfoService.add(auditInfo) > 0;
        // 推送请假审批通知给第一个审批人
        User user = userMapper.selectByPrimaryKey(req.getCurrentAuditUserId());
        String deviceToken = user.getDevToken();
        Short deviceType = user.getDevType();
        addPush(req.getId(), audit.getId(), 1, "您有新的报修申请审批待处理", req.getCurrentAuditUserId(), deviceType, deviceToken);

        if (!(result && addLeaveAudit && addAuditInfo)) {
            throw new BizException(Result.CUSTOM_MESSAGE, "申请失败");
        }
    }

    private void setCurrentAuditUserId(RepairCostInfo req) {
        List<Integer> auditUserIds = repairCostAuditUserService.getAuditUserIds(req.getApplyUserId());
        HashMap hashMap = setAuditUser(req.getCurrentAuditUserId(), auditUserIds);
        req.setAuditUser(MapUtils.getString(hashMap, "auditUser"));
        req.setCurrentAuditUserId(MapUtils.getInteger(hashMap, "currentAuditUserId"));
    }

    public void addPush(Integer repairId, Integer repairNoticeId, Integer needToAudit,
                        String title, Integer receiverId, Short deviceType, String deviceToken) {
        // 推送
        Boolean flag = false;
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("id", "");
        hm.put("title", title);
        hm.put("type", PushMsgType.REPAIR_COST_NOTICE.getCode());//请假审批类型
        hm.put("repairId", repairId == null ? "" : repairId);
        hm.put("repairNoticeId", repairNoticeId == null ? "" : repairNoticeId);
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
        puEntity.setMessageType(PushMsgType.REPAIR_COST_NOTICE.getCode().shortValue());
        puEntity.setDeviceType(deviceType);
        puEntity.setTitle(title);
        if (flag) {
            puEntity.setType((short) 10);
        } else {
            puEntity.setType((short) 50);
        }

        pushMapper.insertSelective(puEntity);
    }

    public RepairCostInfo selectByPrimaryKey(Integer repairId) {
        return repairCostInfoMapper.selectByPrimaryKey(repairId);
    }

    public void cancel(RepairCostInfo info) {
        RepairCostInfo update=new RepairCostInfo();
        update.setStatus((short) 4);
        update.setUpdateTime(new Date());
        RepairCostInfoExample example = new RepairCostInfoExample();
        example.createCriteria()
                .andIdEqualTo(info.getId());
        boolean result = repairCostInfoMapper.updateByExampleSelective(update, example) > 0;
        if (!result) {
            throw new BizException(Result.CUSTOM_MESSAGE, "取消失败");
        }
    }

    public List<HashMap> myRepairCostInfoList(Integer userId, String startDate, String endDate, Short status) {
        return repairCostInfoMapper.myRepairCostInfoList( userId,startDate,endDate,  getStatusList(status,true));

    }
    public List<HashMap> myAuditRepairCostInfoList(Integer userId, String startDate, String endDate, Short status) {
        return repairCostInfoMapper.myAuditRepairCostInfoList( userId,startDate,endDate,  getStatusList(status,false));

    }

    public void audit(RepairCostInfo info, Short status, String remark) {
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

        RepairCostAudit audit = repairCostAuditService.selectByUseId(info.getId(), currentAuditUserId);
        Assert.notNull(audit, "该申请审批记录不存在");
        audit.setIsPass(status.intValue() == 2 ? (short) 1 : (short) 2);
        audit.setRemark(remark);
        audit.setAuditTime(new Date());
        boolean updateLeaveAudit = repairCostAuditService.update(audit) > 0;
        boolean updateAuditInfo = auditInfoService.update(audit.getId(), currentAuditUserId,
                AuditBusinessType.REPAIR_COST.getCode().shortValue(), audit.getIsPass()) > 0;

        if (!(updateLeaveAudit && updateAuditInfo)) {
            throw new BizException(Result.CUSTOM_MESSAGE, "审核失败");
        }

        // 推送通知
        RepairCostNotice notice;
        Integer receiverId, repairNoticeId, needToAudit = 0;
        String title;
        User user;
        if (status.intValue() == 3) {
            // 修改审核状态
            info.setStatus(status);
            info.setCurrentAuditUserId(currentAuditUserId);
            info.setUpdateTime(new Date());
            repairCostInfoMapper.updateByPrimaryKeySelective(info);

            // 审核驳回,通知请假人请假结果
            notice =  repairCostNoticeService.addNotice(info.getId(), "您的申请未通过审批", info.getReason(), remark, info.getApplyUserId());
            Assert.notNull(notice.getId(), "审核失败");
            // 推送请假审批结果通知给请假人
            receiverId = info.getApplyUserId();
            title = "您提交的申请未通过审批";
            user = userMapper.selectByPrimaryKey(receiverId);
            repairNoticeId = notice.getId();
        } else {
            if (currentAuditUserIsTheLast) {
                // 修改审核状态
                info.setStatus(status);
                info.setCurrentAuditUserId(currentAuditUserId);
                info.setUpdateTime(new Date());
                repairCostInfoMapper.updateByPrimaryKeySelective(info);
                // 通知请假人请假结果
                notice =repairCostNoticeService.addNotice(info.getId(), "您的申请已通过审批", info.getReason(), remark, info.getApplyUserId());
                Assert.notNull(notice.getId(), "审核失败");
                // 推送请假审批结果通知给请假人
                receiverId = info.getApplyUserId();
                title = "您提交的申请已通过审批";
                user = userMapper.selectByPrimaryKey(receiverId);
                repairNoticeId = notice.getId();
            } else {
                // 修改当前审核人
                info.setCurrentAuditUserId(nextAuditUserId);
                info.setUpdateTime(new Date());
                repairCostInfoMapper.updateByPrimaryKeySelective(info);

                // 新增下一个审批人审批记录，状态为空
                RepairCostAudit nextAudit = new RepairCostAudit();
                nextAudit.setRepairId(info.getId());
                nextAudit.setUserId(nextAuditUserId);
                nextAudit.setCreateTime(new Date());
                boolean addLeaveAudit = repairCostAuditService.add(nextAudit) > 0;
                // 新增审核操作记录
                AuditInfo auditInfo = new AuditInfo();
                auditInfo.setType(AuditBusinessType.REPAIR_COST.getCode().shortValue());
                auditInfo.setBizId(nextAudit.getId());
                auditInfo.setUserId(nextAuditUserId);
                boolean addAuditInfo = auditInfoService.add(auditInfo) > 0;
                if (!(addLeaveAudit && addAuditInfo)) {
                    throw new BizException(Result.CUSTOM_MESSAGE, "审核失败");
                }

                // 推送请假审批通知给下一个审批人
                receiverId = nextAuditUserId;
                title = "您有新的申请审批待处理";
                user = userMapper.selectByPrimaryKey(receiverId);
                repairNoticeId = null;
                needToAudit = 1;
            }
        }

        addPush(info.getId(), repairNoticeId, needToAudit, title, receiverId, user.getDevType(), user.getDevToken());
    }
}
