package com.biu.wifi.campus.service;

import com.biu.wifi.campus.constant.AuditBusinessType;
import com.biu.wifi.campus.dao.FileReceiveAuditMapper;
import com.biu.wifi.campus.dao.FileReceiveAuditUserMapper;
import com.biu.wifi.campus.dao.InstituteMapper;
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
public class FileReceiveAuditUserSerive {

    @Autowired
    private FileReceiveAuditUserMapper fileReceiveAuditUserMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private InstituteMapper instituteMapper;
    @Autowired
    private FileReceiveAuditMapper fileReceiveAuditMapper;

    public List<HashMap> getAuditUserList(Integer userId, Integer type) {
        List<HashMap> list=new ArrayList<>();
        List<Integer> auditUserList = getAuditUser(userId, type);
        for (Integer id : auditUserList) {
            list.add( userMapper.selectMap(id));
        }

        return list;
    }

    private List<Integer> getAuditUser(Integer userId, Integer type) {
        List<Integer> auditUserList=new ArrayList<>();
        User user = userMapper.selectByPrimaryKey(userId);
        Integer instituteId = user.getInstituteId();
        FileReceiveAuditUserExample example=new FileReceiveAuditUserExample();
        example.createCriteria().andInstituteIdEqualTo(instituteId)
                                .andTypeEqualTo(type.shortValue());
        List<FileReceiveAuditUser> fileReceiveAuditUsers = fileReceiveAuditUserMapper.selectByExample(example);
        String[] strings = fileReceiveAuditUsers.get(0).getAuditUser().split(",");
        for (String s : strings) {
            auditUserList.add(Integer.valueOf(s));
        }
        return  auditUserList;
    }

    public FileReceiveAuditUser find(Integer schoolId, Integer instituteId, Integer fileType) {
        FileReceiveAuditUserExample example=new FileReceiveAuditUserExample();
        example.createCriteria().andInstituteIdEqualTo(instituteId)
                                .andTypeEqualTo(fileType.shortValue())
                                .andSchoolIdEqualTo(schoolId);
        List<FileReceiveAuditUser> fileReceiveAuditUsers = fileReceiveAuditUserMapper.selectByExample(example);
        return fileReceiveAuditUsers.size()!=0?fileReceiveAuditUsers.get(0):null;
    }

    public List<HashMap> findMapList(FileReceiveAuditInfo fileReceiveAuditInfo) {
        List<String> auditUserIds = Arrays.asList(fileReceiveAuditInfo.getAuditUser().split(","));

        List<HashMap> list = new ArrayList<>();
        for (String userId : auditUserIds) {
            HashMap hashMap = fileReceiveAuditMapper.selectMap(fileReceiveAuditInfo.getId(), userId);
            if (hashMap == null) {
                hashMap = new HashMap<>();
                hashMap.put("id", null);
                hashMap.put("type", AuditBusinessType.TEACHER_LEAVE.getCode());
                hashMap.put("bizId", fileReceiveAuditInfo.getId());
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
