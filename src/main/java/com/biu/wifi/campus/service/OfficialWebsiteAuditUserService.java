package com.biu.wifi.campus.service;

import com.biu.wifi.campus.constant.AuditBusinessType;
import com.biu.wifi.campus.dao.InstituteMapper;
import com.biu.wifi.campus.dao.OfficialWebsiteAuditMapper;
import com.biu.wifi.campus.dao.OfficialWebsiteAuditUserMapper;
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
public class OfficialWebsiteAuditUserService {
    @Autowired
    private OfficialWebsiteAuditUserMapper officialWebsiteAuditUserMapper;
    @Autowired
    private OfficialWebsiteAuditMapper officialWebsiteAuditMapper;
    @Autowired
    private InstituteMapper instituteMapper;
    @Autowired
    private UserMapper userMapper;

    public List<HashMap> getAuditUserList(Integer applyUserId) {
        List<Integer> auditUserIds = getAuditUserIds(applyUserId);
        Assert.notEmpty(auditUserIds, "暂未设置审核人,请联系管理员");
        List<HashMap> list = new ArrayList<>();
        for (Integer userId : auditUserIds) {
            list.add(userMapper.selectMap(userId));
        }
        return list;
    }

    public List<Integer> getAuditUserIds(Integer applyUserId) {
        List<Integer> list=new ArrayList<>();
        User user = userMapper.selectByPrimaryKey(applyUserId);
        OfficialWebsiteAuditUserExample example=new OfficialWebsiteAuditUserExample();
        example.createCriteria().andSchoolIdEqualTo(user.getSchoolId())
                .andInstituteIdEqualTo(user.getInstituteId());
        List<OfficialWebsiteAuditUser> assertsUseAuditUsers = officialWebsiteAuditUserMapper.selectByExample(example);
        if(CollectionUtils.isNotEmpty(assertsUseAuditUsers)){
            for (String s : assertsUseAuditUsers.get(0).getAuditUser().split(",")) {
                list.add(Integer.valueOf(s));
            }
        }
        return list;
    }

    public List<HashMap> findMapList(OfficialWebsiteInfo info) {
        List<String> auditUserIds = Arrays.asList(info.getAuditUser().split(","));

        List<HashMap> list = new ArrayList<>();
        for (String userId : auditUserIds) {
            HashMap hashMap = officialWebsiteAuditMapper.selectMap(info.getId(), userId);
            if (hashMap == null) {
                hashMap = new HashMap<>();
                hashMap.put("id", null);
                hashMap.put("type", AuditBusinessType.OFFICIAL_WEBSITE.getCode());
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
