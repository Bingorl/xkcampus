package com.biu.wifi.campus.service;

import com.biu.wifi.campus.constant.AuditBusinessType;
import com.biu.wifi.campus.dao.InstituteMapper;
import com.biu.wifi.campus.dao.RepairCostAuditMapper;
import com.biu.wifi.campus.dao.RepairCostAuditUserMapper;
import com.biu.wifi.campus.dao.UserMapper;
import com.biu.wifi.campus.dao.model.*;
import org.apache.commons.collections.CollectionUtils;
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
public class RepairCostAuditUserService {
    @Autowired
    private RepairCostAuditUserMapper repairCostAuditUserMapper;
    @Autowired
    private RepairCostAuditMapper repairCostAuditMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private InstituteMapper instituteMapper;

    public List<HashMap> getAuditUserList(Integer applyUserId) {
        List<Integer> auditUserIds = getAuditUserIds(applyUserId);
        List<HashMap> list = new ArrayList<>();
        for (Integer userId : auditUserIds) {
            list.add(userMapper.selectMap(userId));
        }
        return list;
    }

    public List<Integer> getAuditUserIds(Integer applyUserId) {
        List<Integer> list=new ArrayList<>();
        User user = userMapper.selectByPrimaryKey(applyUserId);
        RepairCostAuditUserExample example=new RepairCostAuditUserExample();
        example.createCriteria().andSchoolIdEqualTo(user.getSchoolId())
                .andInstituteIdEqualTo(user.getInstituteId());
        List<RepairCostAuditUser> assertsUseAuditUsers = repairCostAuditUserMapper.selectByExample(example);
        if(CollectionUtils.isNotEmpty(assertsUseAuditUsers)){
            for (String s : assertsUseAuditUsers.get(0).getAuditUser().split(",")) {
                list.add(Integer.valueOf(s));
            }
        }
        return list;
    }

    public List<HashMap> findMapList(RepairCostInfo info) {
        List<String> auditUserIds = Arrays.asList(info.getAuditUser().split(","));

        List<HashMap> list = new ArrayList<>();
        for (String userId : auditUserIds) {
            HashMap hashMap =repairCostAuditMapper.selectMap(info.getId(), userId);
            if (hashMap == null) {
                hashMap = new HashMap<>();
                hashMap.put("id", null);
                hashMap.put("type", AuditBusinessType.REPAIR_COST.getCode());
                hashMap.put("bizId", info.getId());
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
