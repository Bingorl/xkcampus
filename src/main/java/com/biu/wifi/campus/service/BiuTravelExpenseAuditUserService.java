package com.biu.wifi.campus.service;

import com.biu.wifi.campus.constant.AuditBusinessType;
import com.biu.wifi.campus.dao.BiuTravelExpenseAuditMapper;
import com.biu.wifi.campus.dao.BiuTravelExpenseAuditUserMapper;
import com.biu.wifi.campus.dao.InstituteMapper;
import com.biu.wifi.campus.dao.UserMapper;
import com.biu.wifi.campus.dao.model.BiuTravelExpenseAuditUser;
import com.biu.wifi.campus.dao.model.BiuTravelExpenseInfo;
import com.biu.wifi.campus.dao.model.Institute;
import com.biu.wifi.campus.dao.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @Author MCX
 * @Version 1.0
 **/
@Service
public class BiuTravelExpenseAuditUserService {
    @Autowired
    private BiuTravelExpenseAuditUserMapper biuTravelExpenseAuditUserMapper;
    @Autowired
    private BiuTravelExpenseAuditMapper biuTravelExpenseAuditMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private InstituteMapper instituteMapper;

    public List<HashMap> getAuditUserList(Integer expenseUserId, Integer planDays, BigDecimal costMoney) {
        List<Integer> auditUserIds = getAuditUserIds(expenseUserId, planDays,costMoney);
        Assert.notEmpty(auditUserIds, "暂未设置审核人,请联系管理员");
        List<HashMap> list = new ArrayList<>();
        for (Integer userId : auditUserIds) {
            list.add(userMapper.selectMap(userId));
        }
        return list;
    }

    public List<Integer> getAuditUserIds(Integer expenseUserId, Integer planDays, BigDecimal costMoney) {
        List<Integer> auditUserIds = new ArrayList<>();
        User user = userMapper.selectByPrimaryKey(expenseUserId);
        int instituteId = user.getInstituteId();
        if(planDays>7||costMoney.compareTo(new BigDecimal(10000))==1){
            BiuTravelExpenseAuditUser one = biuTravelExpenseAuditUserMapper.findOne(1, instituteId);
            for (String s : one.getAuditUser().split(",")) {
                auditUserIds.add(Integer.valueOf(s));
            }
            return auditUserIds;
        }else{
            BiuTravelExpenseAuditUser one = biuTravelExpenseAuditUserMapper.findOne(0, instituteId);
            for (String s : one.getAuditUser().split(",")) {
                auditUserIds.add(Integer.valueOf(s));
            }
            return auditUserIds;
        }
    }

    public List<HashMap> findMapList(BiuTravelExpenseInfo expenseInfo) {
        List<String> auditUserIds = Arrays.asList(expenseInfo.getAuditUser().split(","));

        List<HashMap> list = new ArrayList<>();
        for (String userId : auditUserIds) {
            HashMap hashMap = biuTravelExpenseAuditMapper.selectMap(expenseInfo.getId(), userId);
            if (hashMap == null) {
                hashMap = new HashMap<>();
                hashMap.put("id", null);
                hashMap.put("type", AuditBusinessType.TRAVEL_EXPENSE.getCode());
                hashMap.put("bizId", expenseInfo.getId());
                hashMap.put("userId", Integer.valueOf(userId));
                hashMap.put("isPass", null);
            }
            User auditUser = userMapper.selectByPrimaryKey(Integer.valueOf(userId));
            Institute institute = instituteMapper.selectByPrimaryKey(auditUser.getInstituteId());
            hashMap.put("userName", auditUser.getName());
            hashMap.put("instituteName", institute.getName());
            list.add(hashMap);
        }

        return list;
    }
}
