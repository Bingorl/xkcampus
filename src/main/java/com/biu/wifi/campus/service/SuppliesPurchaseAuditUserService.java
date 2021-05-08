package com.biu.wifi.campus.service;

import com.biu.wifi.campus.constant.AuditBusinessType;
import com.biu.wifi.campus.dao.InstituteMapper;
import com.biu.wifi.campus.dao.SuppliesPurchaseAuditMapper;
import com.biu.wifi.campus.dao.SuppliesPurchaseAuditUserMapper;
import com.biu.wifi.campus.dao.UserMapper;
import com.biu.wifi.campus.dao.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @Author MCX
 * @Version 1.0
 **/
@Service
public class SuppliesPurchaseAuditUserService  {
    @Autowired
    private SuppliesPurchaseAuditUserMapper suppliesPurchaseAuditUserMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private InstituteMapper instituteMapper;
    @Autowired
    private SuppliesPurchaseAuditMapper suppliesPurchaseAuditMapper;

    public List<HashMap> findMapList(SuppliesPurchaseInfo purchaseInfo) {
        List<String> auditUserIds = Arrays.asList(purchaseInfo.getAuditUser().split(","));

        List<HashMap> list = new ArrayList<>();
        for (String userId : auditUserIds) {
            HashMap hashMap = suppliesPurchaseAuditUserMapper.selectMap(purchaseInfo.getId(), userId);
            if (hashMap == null) {
                hashMap = new HashMap<>();
                hashMap.put("id", null);
                hashMap.put("type", AuditBusinessType.SUPPLIES_PURCHASE.getCode());
                hashMap.put("bizId", purchaseInfo.getId());
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
