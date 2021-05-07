package com.biu.wifi.campus.service;

import com.biu.wifi.campus.Tool.PushTool;
import com.biu.wifi.campus.constant.AuditBusinessType;
import com.biu.wifi.campus.constant.PushMsgType;
import com.biu.wifi.campus.dao.*;
import com.biu.wifi.campus.dao.model.*;
import com.biu.wifi.campus.exception.BizException;
import com.biu.wifi.campus.result.Result;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import weblogic.wtc.jatmi.TdomTcb;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @Author MCX
 * @Version 1.0
 **/
@Service
public class SuppliesPurchaseInfoService extends AbstractAuditUserService {
    @Autowired
    private SuppliesPurchaseInfoMapper suppliesPurchaseInfoMapper;
    @Autowired
    private SuppliesPurchaseAuditUserMapper suppliesPurchaseAuditUserMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SuppliesPurchaseAuditService suppliesPurchaseAuditService;
      @Autowired
    private AuditInfoService auditInfoService;
     @Autowired
    private PushMapper pushMapper;


    public List<HashMap> getAuditUserList(Integer applyUserId, BigDecimal money) {
        List<Integer> auditUserIds = getAuditUserIds(applyUserId, money);
        List<HashMap> list = new ArrayList<>();
        for (Integer userId : auditUserIds) {
            list.add(userMapper.selectMap(userId));
        }
        return list;
    }

    public List<Integer> getAuditUserIds(Integer applyUserId, BigDecimal money) {
        List<Integer> auditUserIds = new ArrayList<>();
        User user = userMapper.selectByPrimaryKey(applyUserId);
        int instituteId = user.getInstituteId();
        List<SuppliesPurchaseAuditUser> suppliesPurchaseAuditUsers = suppliesPurchaseAuditUserMapper.selectOneByTypeAndInstituteId(1, instituteId);
        if (CollectionUtils.isNotEmpty(suppliesPurchaseAuditUsers)) {
            Integer userId = Integer.valueOf(suppliesPurchaseAuditUsers.get(0).getAuditUser().split(",")[0]);
            auditUserIds.add(userId);
        }
        // 3-7天,主管领导审批
        if (money.compareTo(new BigDecimal(10000))==1) {
            suppliesPurchaseAuditUsers = suppliesPurchaseAuditUserMapper.selectOneByTypeAndInstituteId(2, instituteId);
            if (CollectionUtils.isNotEmpty(suppliesPurchaseAuditUsers)) {
                Integer userId = Integer.valueOf(suppliesPurchaseAuditUsers.get(0).getAuditUser().split(",")[1]);
                auditUserIds.add(userId);
            }
        }
        // 7天以上,院长审批
        if (money.compareTo(new BigDecimal(50000))==1) {
            suppliesPurchaseAuditUsers = suppliesPurchaseAuditUserMapper.selectOneByTypeAndInstituteId(3, instituteId);
            if (CollectionUtils.isNotEmpty(suppliesPurchaseAuditUsers)) {
                Integer userId = Integer.valueOf(suppliesPurchaseAuditUsers.get(0).getAuditUser().split(",")[2]);
                auditUserIds.add(userId);
            }
        }
        return auditUserIds;
    }

    public Integer add(SuppliesPurchaseInfo req) {
        req.setIsDelete((short) 2);
        req.setStatus((short) 1);
        req.setCreateTime(new Date());
        setCurrentAuditUserId(req);

        boolean result = suppliesPurchaseInfoMapper.insertSelective(req) > 0;

        SuppliesPurchaseAudit suppliesPurchaseAudit = new SuppliesPurchaseAudit();
        suppliesPurchaseAudit.setPurchaseId(req.getId());
        suppliesPurchaseAudit.setUserId(req.getCurrentAuditUserId());
        boolean addLeaveAudit = suppliesPurchaseAuditService.add(suppliesPurchaseAudit) > 0;

        //插入汇总记录
        AuditInfo auditInfo = new AuditInfo();
        auditInfo.setType(AuditBusinessType.SUPPLIES_PURCHASE.getCode().shortValue());
        auditInfo.setBizId(suppliesPurchaseAudit.getId());
        auditInfo.setUserId(req.getCurrentAuditUserId());
        boolean addAuditInfo = auditInfoService.add(auditInfo) > 0;

        // 推送请假审批通知给第一个审批人
        User user = userMapper.selectByPrimaryKey(req.getCurrentAuditUserId());
        String deviceToken = user.getDevToken();
        Short deviceType = user.getDevType();
        addPush(req.getId(), suppliesPurchaseAudit.getId(), 1, "您有新的采购审批待处理", req.getCurrentAuditUserId(), deviceType, deviceToken);

        if (!(result && addLeaveAudit && addAuditInfo)) {
            throw new BizException(Result.CUSTOM_MESSAGE, "申请失败");
        }
    }

    public void addPush(Integer purchaseId, Integer purchaseNoticeId, Integer needToAudit,
                        String title, Integer receiverId, Short deviceType, String deviceToken) {
        // 推送
        Boolean flag = false;
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("id", "");
        hm.put("title", title);
        hm.put("type", PushMsgType.STUDENT_LEAVE_NOTICE.getCode());//请假审批类型
        hm.put("purchaseId", purchaseId == null ? "" : purchaseId);
        hm.put("purchaseNoticeId", purchaseNoticeId == null ? "" : purchaseNoticeId);
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
        puEntity.setMessageType(PushMsgType.SUPPLIES_PURCHASE_NOTICE.getCode().shortValue());
        puEntity.setDeviceType(deviceType);
        puEntity.setTitle(title);
        if (flag) {
            puEntity.setType((short) 10);
        } else {
            puEntity.setType((short) 50);
        }

        pushMapper.insertSelective(puEntity);
    }

    private void setCurrentAuditUserId(SuppliesPurchaseInfo req) {
        List<Integer> auditUserIds = new ArrayList<>();
        if (StringUtils.isEmpty(req.getAuditUser())) {
            // 新增时读取审批人列表的数据
            // 3天以下,部门负责人审批
            auditUserIds = getAuditUserIds(req.getApplyUserId(), req.getMoney());
        } else {
            for (String userId : req.getAuditUser().split(",")) {
                auditUserIds.add(Integer.valueOf(userId));
            }
        }
        HashMap hashMap = setAuditUser(req.getCurrentAuditUserId(), auditUserIds);
        req.setAuditUser(MapUtils.getString(hashMap, "auditUser"));
        req.setCurrentAuditUserId(MapUtils.getInteger(hashMap, "currentAuditUserId"));
    }

    public SuppliesPurchaseInfo selectByPrimaryKey(Integer purchaseId) {
        return suppliesPurchaseInfoMapper.selectByPrimaryKey(purchaseId);
    }

    public void cancel(SuppliesPurchaseInfo suppliesPurchaseInfo) {
        suppliesPurchaseInfo.setStatus((short) 4);
        suppliesPurchaseInfo.setUpdateTime(new Date());
        int i = suppliesPurchaseInfoMapper.updateByPrimaryKeySelective(suppliesPurchaseInfo);
        if (i!=1) {
            throw new BizException(Result.CUSTOM_MESSAGE, "取消失败");
        }
    }


    public List<HashMap> mySuppliesPurchaseInfoList(Integer userId, String startDate, String endDate, Short status) {
        return suppliesPurchaseInfoMapper.mySuppliesPurchaseInfoList(userId, startDate, endDate, getStatusList(status, true));

    }

    public List<HashMap> myAuditpurchaseInfoList(Integer userId, String startDate, String endDate, Short status) {
        return suppliesPurchaseInfoMapper.myAuditpurchaseInfoList(userId, startDate, endDate, getStatusList(status, false));
    }
//TODO audit
//    public void audit(SuppliesPurchaseInfo purchaseInfo, Short status, String remark) {
//        int currentAuditUserId = purchaseInfo.getCurrentAuditUserId();
//        boolean currentAuditUserIsTheLast = false;
//        // 是否需要进入到下一个审核节点,设置下一个审批人id
//        setCurrentAuditUserId(purchaseInfo);
//        int nextAuditUserId = currentAuditUserId;
//        if (purchaseInfo.getCurrentAuditUserId() == null) {
//            // 当前审核人为最后一个
//            currentAuditUserIsTheLast = true;
//        } else {
//            nextAuditUserId = purchaseInfo.getCurrentAuditUserId();
//        }
//
//        FileReceiveAudit fileReceiveAudit = fileReceiveAuditService.selectByLeaveId(info.getId(), currentAuditUserId);
//        Assert.notNull(fileReceiveAudit, "该文件签发记录不存在");
//        fileReceiveAudit.setIsPass(status.intValue() == 2 ? (short) 1 : (short) 2);
//        fileReceiveAudit.setRemark(remark);
//        fileReceiveAudit.setAuditTime(new Date());
//        boolean updateLeaveAudit = fileReceiveAuditService.update(fileReceiveAudit) > 0;
//        boolean updateAuditInfo = auditInfoService.update(fileReceiveAudit.getId(), currentAuditUserId,
//                AuditBusinessType.FILE_RECEIVE.getCode().shortValue(), fileReceiveAudit.getIsPass()) > 0;
//
//        if (!(updateLeaveAudit && updateAuditInfo)) {
//            throw new BizException(Result.CUSTOM_MESSAGE, "审核失败");
//        }
//
//        // 推送通知
//        FileReceiveNotice fileReceiveNotice;
//        Integer receiverId, receiveNoticeId, needToAudit = 0;
//        String title;
//        User user;
//        if (status.intValue() == 3) {
//            // 修改审核状态
//            info.setStatus(status);
//            info.setCurrentAuditUserId(currentAuditUserId);
//            info.setUpdateTime(new Date());
//            fileReceiveAuditInfoMapper.updateByPrimaryKeySelective(info);
//
//            // 审核驳回,通知请假人请假结果
//            fileReceiveNotice = fileReceiveNoticeService.addfileNotice(info.getId(), "您的文件未通过审批", info.getContent(), remark, info.getApplyUserId());
//            Assert.notNull(fileReceiveNotice.getId(), "审核失败");
//            // 推送请假审批结果通知给请假人
//            receiverId = info.getApplyUserId();
//            title = "您提交的文件未通过审批";
//            user = userMapper.selectByPrimaryKey(receiverId);
//            receiveNoticeId = fileReceiveNotice.getId();
//        } else {
//            if (currentAuditUserIsTheLast) {
//                // 修改审核状态
//                info.setStatus(status);
//                info.setCurrentAuditUserId(currentAuditUserId);
//                info.setUpdateTime(new Date());
//                fileReceiveAuditInfoMapper.updateByPrimaryKeySelective(info);
//                // 通知请假人请假结果
//                fileReceiveNotice = fileReceiveNoticeService.addfileNotice(info.getId(), "您的申请已通过审批", info.getContent(), remark, info.getApplyUserId());
//                Assert.notNull(fileReceiveNotice.getId(), "审核失败");
//                // 推送请假审批结果通知给请假人
//                receiverId = info.getApplyUserId();
//                title = "您提交的文件已通过审批";
//                user = userMapper.selectByPrimaryKey(receiverId);
//                receiveNoticeId = fileReceiveNotice.getId();
//            } else {
//                // 修改当前审核人
//                info.setCurrentAuditUserId(nextAuditUserId);
//                info.setUpdateTime(new Date());
//                fileReceiveAuditInfoMapper.updateByPrimaryKeySelective(info);
//
//                // 新增下一个审批人审批记录，状态为空
//                FileReceiveAudit nextLeaveAudit = new FileReceiveAudit();
//                nextLeaveAudit.setReceiveId(info.getId());
//                nextLeaveAudit.setUserId(nextAuditUserId);
//                nextLeaveAudit.setCreateTime(new Date());
//                boolean addLeaveAudit = fileReceiveAuditService.add(nextLeaveAudit) > 0;
//                // 新增审核操作记录
//                AuditInfo auditInfo = new AuditInfo();
//                auditInfo.setType(AuditBusinessType.FILE_RECEIVE.getCode().shortValue());
//                auditInfo.setBizId(nextLeaveAudit.getId());
//                auditInfo.setUserId(nextAuditUserId);
//                boolean addAuditInfo = auditInfoService.add(auditInfo) > 0;
//                if (!(addLeaveAudit && addAuditInfo)) {
//                    throw new BizException(Result.CUSTOM_MESSAGE, "审核失败");
//                }
//
//                // 推送请假审批通知给下一个审批人
//                receiverId = nextAuditUserId;
//                title = "您有新的文件请求待处理";
//                user = userMapper.selectByPrimaryKey(receiverId);
//                receiveNoticeId = null;
//                needToAudit = 1;
//            }
//
//        }
//        addPush(info.getId(), receiveNoticeId, needToAudit, title, receiverId, user.getDevType(), user.getDevToken());
//
//    }
}
