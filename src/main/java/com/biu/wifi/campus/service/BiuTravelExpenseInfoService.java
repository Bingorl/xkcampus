package com.biu.wifi.campus.service;

import com.biu.wifi.campus.Tool.CurrencyUtil;
import com.biu.wifi.campus.Tool.PushTool;
import com.biu.wifi.campus.constant.AuditBusinessType;
import com.biu.wifi.campus.constant.PushMsgType;
import com.biu.wifi.campus.dao.BiuTravelExpenseDetailMapper;
import com.biu.wifi.campus.dao.BiuTravelExpenseInfoMapper;
import com.biu.wifi.campus.dao.PushMapper;
import com.biu.wifi.campus.dao.UserMapper;
import com.biu.wifi.campus.dao.model.*;
import com.biu.wifi.campus.exception.BizException;
import com.biu.wifi.campus.result.Result;
import com.biu.wifi.core.util.DateUtilsEx;
import net.sf.json.JSONArray;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

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
public class BiuTravelExpenseInfoService extends AbstractAuditUserService{
    @Autowired
    private BiuTravelExpenseInfoMapper biuTravelExpenseInfoMapper;
    @Autowired
    private BiuTravelExpenseAuditService biuTravelExpenseAuditService;
    @Autowired
    private BiuTravelExpenseDetailMapper biuTravelExpenseDetailMapper;
    @Autowired
    private BiuTravelExpenseAuditUserService biuTravelExpenseAuditUserService;
    @Autowired
    private AuditInfoService auditInfoService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PushMapper pushMapper;
    @Autowired
    private BiuTravelExpenseNoticeService biuTravelExpenseNoticeService;

    public void add(BiuTravelExpenseInfo req) {
//        JSONArray jsonArray = JSONArray.fromObject(req.getDetailList());
//        List<BiuTravelExpenseDetail> list = (List<BiuTravelExpenseDetail>) JSONArray.toCollection(jsonArray,BiuTravelExpenseDetail.class);
        boolean result1=false;
        BigDecimal money=new BigDecimal(0);
        int planDays = DateUtilsEx.getDayBetween(req.getStartDate(), req.getEndDate());
        req.setPlanDays(planDays);
        req.setIsDelete((short) 2);
        req.setStatus((short) 1);
        req.setCreateTime(new Date());
        setCurrentAuditUserId(req);

        for (BiuTravelExpenseDetail detail1 : req.getDetailList()) {
            money = money.add(detail1.getCostMoney());
        }
        req.setCostMoney(money);
        req.setAmountInWords(CurrencyUtil.bigDecimalToLocalStr(money));
        boolean result= biuTravelExpenseInfoMapper.insertSelective(req)>0;

        for (BiuTravelExpenseDetail detail : req.getDetailList()) {
            detail.setExpenseId(req.getId());
            result1 = biuTravelExpenseDetailMapper.insertSelective(detail)>0;
        }

        BiuTravelExpenseAudit travelExpenseAudit = new BiuTravelExpenseAudit();
        travelExpenseAudit.setExpenseId(req.getId());
        travelExpenseAudit.setUserId(req.getCurrentAuditUserId());
        boolean addLeaveAudit = biuTravelExpenseAuditService.add(travelExpenseAudit) > 0;

        //插入汇总记录
        AuditInfo auditInfo = new AuditInfo();
        auditInfo.setType(AuditBusinessType.TRAVEL_EXPENSE.getCode().shortValue());
        auditInfo.setBizId(travelExpenseAudit.getId());
        auditInfo.setUserId(req.getCurrentAuditUserId());
        boolean addAuditInfo = auditInfoService.add(auditInfo) > 0;

        // 推送请假审批通知给第一个审批人
        User user = userMapper.selectByPrimaryKey(req.getCurrentAuditUserId());
        String deviceToken = user.getDevToken();
        Short deviceType = user.getDevType();
        addPush(req.getId(), travelExpenseAudit.getId(), 1, "您有新的请假审批待处理", req.getCurrentAuditUserId(), deviceType, deviceToken);

        if (!(result && addLeaveAudit && addAuditInfo&& result1)) {
            throw new BizException(Result.CUSTOM_MESSAGE, "申请失败");
        }

    }

    public void addPush(Integer expenseId, Integer expenseNoticeId, Integer needToAudit,
                        String title, Integer receiverId, Short deviceType, String deviceToken) {
        // 推送
        Boolean flag = false;
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("id", "");
        hm.put("title", title);
        hm.put("type", PushMsgType.TRAVEL_EXPENSE_NOTICE.getCode());//请假审批类型
        hm.put("expenseId", expenseId == null ? "" : expenseId);
        hm.put("expenseNoticeId", expenseNoticeId == null ? "" : expenseNoticeId);
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
        puEntity.setMessageType(PushMsgType.TRAVEL_EXPENSE_NOTICE.getCode().shortValue());
        puEntity.setDeviceType(deviceType);
        puEntity.setTitle(title);
        if (flag) {
            puEntity.setType((short) 10);
        } else {
            puEntity.setType((short) 50);
        }

        pushMapper.insertSelective(puEntity);
    }

    private void setCurrentAuditUserId(BiuTravelExpenseInfo req) {
        List<Integer> auditUserIds = new ArrayList<>();
        if (StringUtils.isEmpty(req.getAuditUser())) {
            // 新增时读取审批人列表的数据
            // 3天以下,部门负责人审批
            auditUserIds = biuTravelExpenseAuditUserService.getAuditUserIds(req.getApplyUserId(), req.getPlanDays(), req.getCostMoney() );
        } else {
            for (String userId : req.getAuditUser().split(",")) {
                auditUserIds.add(Integer.valueOf(userId));
            }
        }
        HashMap hashMap = setAuditUser(req.getCurrentAuditUserId(), auditUserIds);
        req.setAuditUser(MapUtils.getString(hashMap, "auditUser"));
        req.setCurrentAuditUserId(MapUtils.getInteger(hashMap, "currentAuditUserId"));
    }

    public BiuTravelExpenseInfo selectByPrimaryKey(Integer expenseId) {
        BiuTravelExpenseInfo expenseInfo = biuTravelExpenseInfoMapper.selectByPrimaryKey(expenseId);
        BiuTravelExpenseDetailExample example = new BiuTravelExpenseDetailExample();
        example.createCriteria().andExpenseIdEqualTo(expenseInfo.getId());
        List<BiuTravelExpenseDetail> detailList = biuTravelExpenseDetailMapper.selectByExample(example);
        expenseInfo.setDetailList(detailList);
        return expenseInfo;

    }


    @Transactional(rollbackFor = {BizException.class, IllegalArgumentException.class})
    public void cancel(BiuTravelExpenseInfo expenseInfo) {
        BiuTravelExpenseInfo update = new BiuTravelExpenseInfo();
        update.setStatus((short) 4);
        update.setUpdateTime(new Date());

        BiuTravelExpenseInfoExample example = new BiuTravelExpenseInfoExample();
        example.createCriteria()
                .andIdEqualTo(expenseInfo.getId());
        boolean result = biuTravelExpenseInfoMapper.updateByExampleSelective(update, example) > 0;
        if (!result) {
            throw new BizException(Result.CUSTOM_MESSAGE, "取消失败");
        }
    }

    public List<HashMap> myExpenseInfoList(Integer userId, String startDate, String endDate, Short status) {
        List<HashMap> list = biuTravelExpenseInfoMapper.myExpenseInfoList(userId, startDate, endDate, getStatusList(status, true));
        setDetailList(list);
        return list;

    }

    public List<HashMap> myAuditExpenseInfoList(Integer userId, String startDate, String endDate, Short status) {
        List<HashMap> list=biuTravelExpenseInfoMapper.myAuditLeaveInfoList(userId, startDate, endDate, getStatusList(status, false));
        setDetailList(list);
        return list;
    }

    private void setDetailList(List<HashMap> list) {
        for (HashMap map : list) {
            Integer id = (Integer) map.get("id");
            BiuTravelExpenseDetailExample example = new BiuTravelExpenseDetailExample();
            example.createCriteria().andExpenseIdEqualTo(id);
            List<BiuTravelExpenseDetail> detailList = biuTravelExpenseDetailMapper.selectByExample(example);
            map.put("detailList", detailList);
        }
    }

    public void audit(BiuTravelExpenseInfo expenseInfo, Short status, String remark) {
        // 当前审批人id
        int currentAuditUserId = expenseInfo.getCurrentAuditUserId();
        boolean currentAuditUserIsTheLast = false;
        // 是否需要进入到下一个审核节点,设置下一个审批人id
        setCurrentAuditUserId(expenseInfo);
        int nextAuditUserId = currentAuditUserId;
        if (expenseInfo.getCurrentAuditUserId() == null) {
            // 当前审核人为最后一个
            currentAuditUserIsTheLast = true;
        } else {
            nextAuditUserId = expenseInfo.getCurrentAuditUserId();
        }

        BiuTravelExpenseAudit travelExpenseAudit = biuTravelExpenseAuditService.selectByExpenseId(expenseInfo.getId(), currentAuditUserId);
        Assert.notNull(travelExpenseAudit, "该审批记录不存在");
        travelExpenseAudit.setIsPass(status.intValue() == 2 ? (short) 1 : (short) 2);
        travelExpenseAudit.setRemark(remark);
        travelExpenseAudit.setAuditTime(new Date());
        boolean updateLeaveAudit = biuTravelExpenseAuditService.update(travelExpenseAudit) > 0;
        boolean updateAuditInfo = auditInfoService.update(travelExpenseAudit.getId(), currentAuditUserId,
                AuditBusinessType.TRAVEL_EXPENSE.getCode().shortValue(), travelExpenseAudit.getIsPass()) > 0;

        if (!(updateLeaveAudit && updateAuditInfo)) {
            throw new BizException(Result.CUSTOM_MESSAGE, "审核失败");
        }

        // 推送通知
        BiuTravelExpenseNotice travelExpenseNotice;
        Integer receiverId, expenseNoticeId, needToAudit = 0;
        String title;
        User user;
        if (status.intValue() == 3) {
            // 修改审核状态
            expenseInfo.setStatus(status);
            expenseInfo.setCurrentAuditUserId(currentAuditUserId);
            expenseInfo.setUpdateTime(new Date());
            biuTravelExpenseInfoMapper.updateByPrimaryKeySelective(expenseInfo);

            // 审核驳回,通知请假人请假结果
            travelExpenseNotice = biuTravelExpenseNoticeService.addNotice(expenseInfo.getId(), "您的差旅申请未通过审批", remark, expenseInfo.getApplyUserId());
            Assert.notNull(travelExpenseNotice.getId(), "审核失败");
            // 推送请假审批结果通知给请假人
            receiverId = expenseInfo.getApplyUserId();
            title = "您提交的申请未通过审批";
            user = userMapper.selectByPrimaryKey(receiverId);
            expenseNoticeId = travelExpenseNotice.getId();
        } else {
            if (currentAuditUserIsTheLast) {
                // 修改审核状态
                expenseInfo.setStatus(status);
                expenseInfo.setCurrentAuditUserId(currentAuditUserId);
                expenseInfo.setUpdateTime(new Date());
                biuTravelExpenseInfoMapper.updateByPrimaryKeySelective(expenseInfo);
                // 通知请假人请假结果
                travelExpenseNotice = biuTravelExpenseNoticeService.addNotice(expenseInfo.getId(), "您的差旅申请已通过审批",  remark, expenseInfo.getApplyUserId());
                Assert.notNull(travelExpenseNotice.getId(), "审核失败");
                // 推送请假审批结果通知给请假人
                receiverId = expenseInfo.getApplyUserId();
                title = "您提交的申请已通过审批";
                user = userMapper.selectByPrimaryKey(receiverId);
                expenseNoticeId = travelExpenseNotice.getId();
            } else {
                // 修改当前审核人
                expenseInfo.setCurrentAuditUserId(nextAuditUserId);
                expenseInfo.setUpdateTime(new Date());
                biuTravelExpenseInfoMapper.updateByPrimaryKeySelective(expenseInfo);

                // 新增下一个审批人审批记录，状态为空
                BiuTravelExpenseAudit nextAudit = new BiuTravelExpenseAudit();
                nextAudit.setExpenseId(expenseInfo.getId());
                nextAudit.setUserId(nextAuditUserId);
                nextAudit.setCreateTime(new Date());
                boolean addAudit = biuTravelExpenseAuditService.add(nextAudit) > 0;
                // 新增审核操作记录
                AuditInfo auditInfo = new AuditInfo();
                auditInfo.setType(AuditBusinessType.TRAVEL_EXPENSE.getCode().shortValue());
                auditInfo.setBizId(nextAudit.getId());
                auditInfo.setUserId(nextAuditUserId);
                boolean addAuditInfo = auditInfoService.add(auditInfo) > 0;
                if (!(addAudit && addAuditInfo)) {
                    throw new BizException(Result.CUSTOM_MESSAGE, "审核失败");
                }

                // 推送请假审批通知给下一个审批人
                receiverId = nextAuditUserId;
                title = "您有新的请假审批待处理";
                user = userMapper.selectByPrimaryKey(receiverId);
                expenseNoticeId = null;
                needToAudit = 1;
            }
        }

        addPush(expenseInfo.getId(), expenseNoticeId, needToAudit, title, receiverId, user.getDevType(), user.getDevToken());

    }
}
