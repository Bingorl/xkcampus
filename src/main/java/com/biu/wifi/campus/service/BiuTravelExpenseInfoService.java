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

        //??????????????????
        AuditInfo auditInfo = new AuditInfo();
        auditInfo.setType(AuditBusinessType.TRAVEL_EXPENSE.getCode().shortValue());
        auditInfo.setBizId(travelExpenseAudit.getId());
        auditInfo.setUserId(req.getCurrentAuditUserId());
        boolean addAuditInfo = auditInfoService.add(auditInfo) > 0;

        // ?????????????????????????????????????????????
        User user = userMapper.selectByPrimaryKey(req.getCurrentAuditUserId());
        String deviceToken = user.getDevToken();
        Short deviceType = user.getDevType();
        addPush(req.getId(), travelExpenseAudit.getId(), 1, "???????????????????????????", req.getCurrentAuditUserId(), deviceType, deviceToken);

        if (!(result && addLeaveAudit && addAuditInfo&& result1)) {
            throw new BizException(Result.CUSTOM_MESSAGE, "????????????");
        }

    }

    public void addPush(Integer expenseId, Integer expenseNoticeId, Integer needToAudit,
                        String title, Integer receiverId, Short deviceType, String deviceToken) {
        // ??????
        Boolean flag = false;
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("id", "");
        hm.put("title", title);
        hm.put("type", PushMsgType.TRAVEL_EXPENSE_NOTICE.getCode());//??????????????????
        hm.put("expenseId", expenseId == null ? "" : expenseId);
        hm.put("expenseNoticeId", expenseNoticeId == null ? "" : expenseNoticeId);
        hm.put("needToAudit", needToAudit);

        try {
            flag = PushTool.pushToUser(receiverId, "", title, hm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // ????????????
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
            // ???????????????????????????????????????
            // 3?????????,?????????????????????
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
            throw new BizException(Result.CUSTOM_MESSAGE, "????????????");
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
        // ???????????????id
        int currentAuditUserId = expenseInfo.getCurrentAuditUserId();
        boolean currentAuditUserIsTheLast = false;
        // ??????????????????????????????????????????,????????????????????????id
        setCurrentAuditUserId(expenseInfo);
        int nextAuditUserId = currentAuditUserId;
        if (expenseInfo.getCurrentAuditUserId() == null) {
            // ??????????????????????????????
            currentAuditUserIsTheLast = true;
        } else {
            nextAuditUserId = expenseInfo.getCurrentAuditUserId();
        }

        BiuTravelExpenseAudit travelExpenseAudit = biuTravelExpenseAuditService.selectByExpenseId(expenseInfo.getId(), currentAuditUserId);
        Assert.notNull(travelExpenseAudit, "????????????????????????");
        travelExpenseAudit.setIsPass(status.intValue() == 2 ? (short) 1 : (short) 2);
        travelExpenseAudit.setRemark(remark);
        travelExpenseAudit.setAuditTime(new Date());
        boolean updateLeaveAudit = biuTravelExpenseAuditService.update(travelExpenseAudit) > 0;
        boolean updateAuditInfo = auditInfoService.update(travelExpenseAudit.getId(), currentAuditUserId,
                AuditBusinessType.TRAVEL_EXPENSE.getCode().shortValue(), travelExpenseAudit.getIsPass()) > 0;

        if (!(updateLeaveAudit && updateAuditInfo)) {
            throw new BizException(Result.CUSTOM_MESSAGE, "????????????");
        }

        // ????????????
        BiuTravelExpenseNotice travelExpenseNotice;
        Integer receiverId, expenseNoticeId, needToAudit = 0;
        String title;
        User user;
        if (status.intValue() == 3) {
            // ??????????????????
            expenseInfo.setStatus(status);
            expenseInfo.setCurrentAuditUserId(currentAuditUserId);
            expenseInfo.setUpdateTime(new Date());
            biuTravelExpenseInfoMapper.updateByPrimaryKeySelective(expenseInfo);

            // ????????????,???????????????????????????
            travelExpenseNotice = biuTravelExpenseNoticeService.addNotice(expenseInfo.getId(), "?????????????????????????????????", remark, expenseInfo.getApplyUserId());
            Assert.notNull(travelExpenseNotice.getId(), "????????????");
            // ??????????????????????????????????????????
            receiverId = expenseInfo.getApplyUserId();
            title = "?????????????????????????????????";
            user = userMapper.selectByPrimaryKey(receiverId);
            expenseNoticeId = travelExpenseNotice.getId();
        } else {
            if (currentAuditUserIsTheLast) {
                // ??????????????????
                expenseInfo.setStatus(status);
                expenseInfo.setCurrentAuditUserId(currentAuditUserId);
                expenseInfo.setUpdateTime(new Date());
                biuTravelExpenseInfoMapper.updateByPrimaryKeySelective(expenseInfo);
                // ???????????????????????????
                travelExpenseNotice = biuTravelExpenseNoticeService.addNotice(expenseInfo.getId(), "?????????????????????????????????",  remark, expenseInfo.getApplyUserId());
                Assert.notNull(travelExpenseNotice.getId(), "????????????");
                // ??????????????????????????????????????????
                receiverId = expenseInfo.getApplyUserId();
                title = "?????????????????????????????????";
                user = userMapper.selectByPrimaryKey(receiverId);
                expenseNoticeId = travelExpenseNotice.getId();
            } else {
                // ?????????????????????
                expenseInfo.setCurrentAuditUserId(nextAuditUserId);
                expenseInfo.setUpdateTime(new Date());
                biuTravelExpenseInfoMapper.updateByPrimaryKeySelective(expenseInfo);

                // ???????????????????????????????????????????????????
                BiuTravelExpenseAudit nextAudit = new BiuTravelExpenseAudit();
                nextAudit.setExpenseId(expenseInfo.getId());
                nextAudit.setUserId(nextAuditUserId);
                nextAudit.setCreateTime(new Date());
                boolean addAudit = biuTravelExpenseAuditService.add(nextAudit) > 0;
                // ????????????????????????
                AuditInfo auditInfo = new AuditInfo();
                auditInfo.setType(AuditBusinessType.TRAVEL_EXPENSE.getCode().shortValue());
                auditInfo.setBizId(nextAudit.getId());
                auditInfo.setUserId(nextAuditUserId);
                boolean addAuditInfo = auditInfoService.add(auditInfo) > 0;
                if (!(addAudit && addAuditInfo)) {
                    throw new BizException(Result.CUSTOM_MESSAGE, "????????????");
                }

                // ?????????????????????????????????????????????
                receiverId = nextAuditUserId;
                title = "?????????????????????????????????";
                user = userMapper.selectByPrimaryKey(receiverId);
                expenseNoticeId = null;
                needToAudit = 1;
            }
        }

        addPush(expenseInfo.getId(), expenseNoticeId, needToAudit, title, receiverId, user.getDevType(), user.getDevToken());

    }
}
