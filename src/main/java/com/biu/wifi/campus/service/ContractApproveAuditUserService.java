package com.biu.wifi.campus.service;

import com.biu.wifi.campus.constant.AuditBusinessType;
import com.biu.wifi.campus.dao.ContractApproveAuditMapper;
import com.biu.wifi.campus.dao.ContractApproveAuditUserMapper;
import com.biu.wifi.campus.dao.InstituteMapper;
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
public class ContractApproveAuditUserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ContractApproveAuditUserMapper contractApproveAuditUserMapper;
    @Autowired
    private ContractApproveAuditMapper contractApproveAuditMapper;
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
        ContractApproveAuditUserExample example=new ContractApproveAuditUserExample();
        example.createCriteria().andSchoolIdEqualTo(user.getSchoolId())
                                .andInstituteIdEqualTo(user.getInstituteId());
        List<ContractApproveAuditUser> contractApproveAuditUsers = contractApproveAuditUserMapper.selectByExample(example);
        if(CollectionUtils.isNotEmpty(contractApproveAuditUsers)){
            for (String s : contractApproveAuditUsers.get(0).getAuditUser().split(",")) {
                list.add(Integer.valueOf(s));
            }
        }
        return list;
    }

    public List<HashMap> findMapList(ContractApproveInfo approveInfo) {
        List<String> auditUserIds = Arrays.asList(approveInfo.getAuditUser().split(","));

        List<HashMap> list = new ArrayList<>();
        for (String userId : auditUserIds) {
            HashMap hashMap = contractApproveAuditMapper.selectMap(approveInfo.getId(), userId);
            if (hashMap == null) {
                hashMap = new HashMap<>();
                hashMap.put("id", null);
                hashMap.put("type", AuditBusinessType.CONTRACT_APPROVE.getCode());
                hashMap.put("bizId", approveInfo.getId());
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
