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
import java.math.BigDecimal;
import java.rmi.activation.ActivationID;
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
    @Autowired
    private SuppliesPurchaseNoticeService suppliesPurchaseNoticeService;


    public List<HashMap> getAuditUserList(Integer applyUserId, BigDecimal money) {
        List<Integer> auditUserIds = getAuditUserIds(applyUserId, money);
        Assert.notEmpty(auditUserIds, "暂未设置审核人,请联系管理员");
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
        if (money.compareTo(new BigDecimal(10000))==-1||money.compareTo(new BigDecimal(10000))==0) {
        List<SuppliesPurchaseAuditUser> suppliesPurchaseAuditUsers = suppliesPurchaseAuditUserMapper.selectOneByTypeAndInstituteId(1, instituteId);
        if (CollectionUtils.isNotEmpty(suppliesPurchaseAuditUsers)) {
            for (String s : suppliesPurchaseAuditUsers.get(0).getAuditUser().split(",")) {
                auditUserIds.add(Integer.valueOf(s));
            }
            return auditUserIds;
        }
        }
        // 3-7天,主管领导审批
        if (money.compareTo(new BigDecimal(10000))==1&&money.compareTo(new BigDecimal(50000))==-1) {
            List<SuppliesPurchaseAuditUser> suppliesPurchaseAuditUsers = suppliesPurchaseAuditUserMapper.selectOneByTypeAndInstituteId(2, instituteId);
            if (CollectionUtils.isNotEmpty(suppliesPurchaseAuditUsers)) {
                if (CollectionUtils.isNotEmpty(suppliesPurchaseAuditUsers)) {
                    for (String s : suppliesPurchaseAuditUsers.get(0).getAuditUser().split(",")) {
                        auditUserIds.add(Integer.valueOf(s));
                    }
                    return auditUserIds;
                }
            }
        }
        // 7天以上,院长审批
        if (money.compareTo(new BigDecimal(50000))==1||money.compareTo(new BigDecimal(50000))==0) {
            List<SuppliesPurchaseAuditUser> suppliesPurchaseAuditUsers = suppliesPurchaseAuditUserMapper.selectOneByTypeAndInstituteId(3, instituteId);
            if (CollectionUtils.isNotEmpty(suppliesPurchaseAuditUsers)) {
                if (CollectionUtils.isNotEmpty(suppliesPurchaseAuditUsers)) {
                    for (String s : suppliesPurchaseAuditUsers.get(0).getAuditUser().split(",")) {
                        auditUserIds.add(Integer.valueOf(s));
                    }
                    return auditUserIds;
                }
            }
        }
        return auditUserIds;
    }

    public void add(SuppliesPurchaseInfo req) {
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
        SuppliesPurchaseInfo update=new SuppliesPurchaseInfo();
        update.setStatus((short) 4);
        update.setUpdateTime(new Date());
        SuppliesPurchaseInfoExample example = new SuppliesPurchaseInfoExample();
        example.createCriteria()
                .andIdEqualTo(suppliesPurchaseInfo.getId());
        boolean result = suppliesPurchaseInfoMapper.updateByExampleSelective(update, example) > 0;
        if (!result) {
            throw new BizException(Result.CUSTOM_MESSAGE, "取消失败");
        }
    }


    public List<HashMap> mySuppliesPurchaseInfoList(Integer userId, String startDate, String endDate, Short status) {
        return suppliesPurchaseInfoMapper.mySuppliesPurchaseInfoList(userId, startDate, endDate, getStatusList(status, true));

    }

    public List<HashMap> myAuditpurchaseInfoList(Integer userId, String startDate, String endDate, Short status) {
        return suppliesPurchaseInfoMapper.myAuditpurchaseInfoList(userId, startDate, endDate, getStatusList(status, false));
    }

    public void audit(SuppliesPurchaseInfo purchaseInfo, Short status, String remark) {
        int currentAuditUserId = purchaseInfo.getCurrentAuditUserId();
        boolean currentAuditUserIsTheLast = false;
        // 是否需要进入到下一个审核节点,设置下一个审批人id
        setCurrentAuditUserId(purchaseInfo);
        int nextAuditUserId = currentAuditUserId;
        if (purchaseInfo.getCurrentAuditUserId() == null) {
            // 当前审核人为最后一个
            currentAuditUserIsTheLast = true;
        } else {
            nextAuditUserId = purchaseInfo.getCurrentAuditUserId();
        }

        SuppliesPurchaseAudit suppliesPurchaseAudit = suppliesPurchaseAuditService.selectBypurchaseId(purchaseInfo.getId(), currentAuditUserId);
        Assert.notNull(suppliesPurchaseAudit, "该采购申请记录不存在");
        suppliesPurchaseAudit.setIsPass(status.intValue() == 2 ? (short) 1 : (short) 2);
        suppliesPurchaseAudit.setRemark(remark);
        suppliesPurchaseAudit.setAuditTime(new Date());
        boolean updateLeaveAudit = suppliesPurchaseAuditService.update(suppliesPurchaseAudit) > 0;
        boolean updateAuditInfo = auditInfoService.update(suppliesPurchaseAudit.getId(), currentAuditUserId,
                AuditBusinessType.SUPPLIES_PURCHASE.getCode().shortValue(), suppliesPurchaseAudit.getIsPass()) > 0;

        if (!(updateLeaveAudit && updateAuditInfo)) {
            throw new BizException(Result.CUSTOM_MESSAGE, "审核失败");
        }

        // 推送通知
        SuppliesPurchaseNotice suppliesPurchaseNotice;
        Integer purchaseId, purchaseNoticeId, needToAudit = 0;
        String title;
        User user;
        if (status.intValue() == 3) {
            // 修改审核状态
            purchaseInfo.setStatus(status);
            purchaseInfo.setCurrentAuditUserId(currentAuditUserId);
            purchaseInfo.setUpdateTime(new Date());
            suppliesPurchaseInfoMapper.updateByPrimaryKeySelective(purchaseInfo);

            // 审核驳回,通知请假人请假结果
            suppliesPurchaseNotice = suppliesPurchaseNoticeService.addfileNotice(purchaseInfo.getId(), "您的采购申请未通过审批", purchaseInfo.getReason(), remark, purchaseInfo.getApplyUserId());
            Assert.notNull(purchaseInfo.getId(), "审核失败");
            // 推送请假审批结果通知给请假人
            purchaseId = purchaseInfo.getApplyUserId();
            title = "您提交的文件未通过审批";
            user = userMapper.selectByPrimaryKey(purchaseId);
            purchaseNoticeId = suppliesPurchaseNotice.getId();
        } else {
            if (currentAuditUserIsTheLast) {
                // 修改审核状态
                purchaseInfo.setStatus(status);
                purchaseInfo.setCurrentAuditUserId(currentAuditUserId);
                purchaseInfo.setUpdateTime(new Date());
                suppliesPurchaseInfoMapper.updateByPrimaryKeySelective(purchaseInfo);
                // 通知请假人请假结果
                suppliesPurchaseNotice = suppliesPurchaseNoticeService.addfileNotice(purchaseInfo.getId(), "您的申请已通过审批", purchaseInfo.getReason(), remark, purchaseInfo.getApplyUserId());
                Assert.notNull(suppliesPurchaseNotice.getId(), "审核失败");
                // 推送请假审批结果通知给请假人
                purchaseId = purchaseInfo.getApplyUserId();
                title = "您提交的文件已通过审批";
                user = userMapper.selectByPrimaryKey(purchaseId);
                purchaseNoticeId = suppliesPurchaseNotice.getId();
            } else {
                // 修改当前审核人
                purchaseInfo.setCurrentAuditUserId(nextAuditUserId);
                purchaseInfo.setUpdateTime(new Date());
                suppliesPurchaseInfoMapper.updateByPrimaryKeySelective(purchaseInfo);

                // 新增下一个审批人审批记录，状态为空
                SuppliesPurchaseAudit nextLeaveAudit = new SuppliesPurchaseAudit();
                nextLeaveAudit.setPurchaseId(purchaseInfo.getId());
                nextLeaveAudit.setUserId(nextAuditUserId);
                nextLeaveAudit.setCreateTime(new Date());
                boolean addLeaveAudit = suppliesPurchaseAuditService.add(nextLeaveAudit) > 0;
                // 新增审核操作记录
                AuditInfo auditInfo = new AuditInfo();
                auditInfo.setType(AuditBusinessType.SUPPLIES_PURCHASE.getCode().shortValue());
                auditInfo.setBizId(nextLeaveAudit.getId());
                auditInfo.setUserId(nextAuditUserId);
                boolean addAuditInfo = auditInfoService.add(auditInfo) > 0;
                if (!(addLeaveAudit && addAuditInfo)) {
                    throw new BizException(Result.CUSTOM_MESSAGE, "审核失败");
                }

                // 推送请假审批通知给下一个审批人
                purchaseId = nextAuditUserId;
                title = "您有新的文件请求待处理";
                user = userMapper.selectByPrimaryKey(purchaseId);
                purchaseNoticeId = null;
                needToAudit = 1;
            }

        }
        addPush(purchaseInfo.getId(), purchaseNoticeId, needToAudit, title, purchaseId, user.getDevType(), user.getDevToken());

    }
}
