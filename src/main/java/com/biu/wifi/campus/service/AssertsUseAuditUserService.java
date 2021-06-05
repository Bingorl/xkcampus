package com.biu.wifi.campus.service;

import com.biu.wifi.campus.constant.AuditBusinessType;
import com.biu.wifi.campus.dao.AssertsUseAuditMapper;
import com.biu.wifi.campus.dao.AssertsUseAuditUserMapper;
import com.biu.wifi.campus.dao.InstituteMapper;
import com.biu.wifi.campus.dao.UserMapper;
import com.biu.wifi.campus.dao.model.*;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @Author MCX
 * @Version 1.0
 **/
@Service
public class AssertsUseAuditUserService {
    @Autowired
    private AssertsUseAuditUserMapper assertsUseAuditUserMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private InstituteMapper instituteMapper;
   @Autowired
    private AssertsUseAuditMapper assertsUseAuditMapper;


    public List<HashMap> getAuditUserList(Integer applyUserId) {
        List<Integer> auditUserIds = getAuditUserIds(applyUserId);
        Assert.notEmpty(auditUserIds, "暂未设置审核人,请联系管理员");
        List<HashMap> list = new ArrayList<>();
        for (Integer userId : auditUserIds) {
            list.add(userMapper.selectMap(userId));
        }
        return list;
    }

    private List<Integer> getAuditUserIds(Integer applyUserId) {
        List<Integer> list=new ArrayList<>();
        User user = userMapper.selectByPrimaryKey(applyUserId);
        AssertsUseAuditUserExample example=new AssertsUseAuditUserExample();
        example.createCriteria().andSchoolIdEqualTo(user.getSchoolId())
                .andInstituteIdEqualTo(user.getInstituteId());
        List<AssertsUseAuditUser> assertsUseAuditUsers = assertsUseAuditUserMapper.selectByExample(example);
        if(CollectionUtils.isNotEmpty(assertsUseAuditUsers)){
            for (String s : assertsUseAuditUsers.get(0).getAuditUser().split(",")) {
                list.add(Integer.valueOf(s));
            }
        }
        return list;
    }

    public List<HashMap> findMapList(AssertsUseInfo assertsUseInfo) {
        List<String> auditUserIds = Arrays.asList(assertsUseInfo.getAuditUser().split(","));

        List<HashMap> list = new ArrayList<>();
        for (String userId : auditUserIds) {
            HashMap hashMap = assertsUseAuditMapper.selectMap(assertsUseInfo.getId(), userId);
            if (hashMap == null) {
                hashMap = new HashMap<>();
                hashMap.put("id", null);
                hashMap.put("type", AuditBusinessType.ASSERTS_USE.getCode());
                hashMap.put("bizId", assertsUseInfo.getId());
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
