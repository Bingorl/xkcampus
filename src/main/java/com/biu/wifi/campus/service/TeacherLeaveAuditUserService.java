package com.biu.wifi.campus.service;

import com.biu.wifi.campus.constant.AuditBusinessType;
import com.biu.wifi.campus.dao.InstituteMapper;
import com.biu.wifi.campus.dao.TeacherLeaveAuditMapper;
import com.biu.wifi.campus.dao.TeacherLeaveAuditUserMapper;
import com.biu.wifi.campus.dao.UserMapper;
import com.biu.wifi.campus.dao.model.*;
import com.biu.wifi.campus.exception.BizException;
import com.biu.wifi.campus.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author 张彬.
 * @date 2021/4/16 16:08.
 */
@Service
public class TeacherLeaveAuditUserService {

    @Autowired
    private TeacherLeaveAuditUserMapper teacherLeaveAuditUserMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private TeacherLeaveAuditMapper teacherLeaveAuditMapper;
    @Autowired
    private InstituteMapper instituteMapper;

    public List<HashMap> findMapList(TeacherLeaveInfo teacherLeaveInfo) {
        List<String> auditUserIds = Arrays.asList(teacherLeaveInfo.getAuditUser().split(","));

        List<HashMap> list = new ArrayList<>();
        for (String userId : auditUserIds) {
            HashMap hashMap = teacherLeaveAuditMapper.selectMap(teacherLeaveInfo.getId(), userId);
            if (hashMap == null) {
                hashMap = new HashMap<>();
                hashMap.put("id", null);
                hashMap.put("type", AuditBusinessType.TEACHER_LEAVE.getCode());
                hashMap.put("bizId", teacherLeaveInfo.getId());
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

    /**
     * 查询请假审核人员列表
     *
     * @param instituteId
     * @param type
     * @return
     * @author 张彬.
     * @date 2021/4/18 15:56.
     */
    public List<HashMap> findTeacherLeaveAuditUserList(Integer instituteId, Short type) {
        TeacherLeaveAuditUserExample example = new TeacherLeaveAuditUserExample();
        example.setOrderByClause("id desc");
        example.createCriteria()
                .andInstituteIdEqualTo(instituteId)
                .andTypeEqualTo(type)
                .andIsDeleteEqualTo((short) 2);
        List<TeacherLeaveAuditUser> teacherLeaveAuditUsers = teacherLeaveAuditUserMapper.selectByExample(example);

        List<HashMap> list = new ArrayList<>();
        if (teacherLeaveAuditUsers.isEmpty()) {
            return list;
        } else {
            List<String> auditUserIds = Arrays.asList(teacherLeaveAuditUsers.get(0).getAuditUser().split(","));

            for (String userId : auditUserIds) {
                User user = userMapper.selectByPrimaryKey(Integer.valueOf(userId));
                Institute institute = instituteMapper.selectByPrimaryKey(user.getInstituteId());
                HashMap hashMap = new HashMap();
                hashMap.put("userId", user.getId());
                hashMap.put("username", user.getName());
                hashMap.put("instituteId", user.getInstituteId());
                hashMap.put("instituteName", institute == null ? "" : institute.getName());
                list.add(hashMap);
            }
            return list;
        }
    }

    @Transactional(rollbackFor = BizException.class)
    public void add(Integer instituteId, Short type, String userIds) {
        Institute institute = instituteMapper.selectByPrimaryKey(instituteId);

        TeacherLeaveAuditUserExample example = new TeacherLeaveAuditUserExample();
        example.createCriteria()
                .andInstituteIdEqualTo(instituteId)
                .andTypeEqualTo(type)
                .andIsDeleteEqualTo((short) 2);
        boolean delete = teacherLeaveAuditUserMapper.deleteByExample(example) >= 0;

        TeacherLeaveAuditUser auditUser = new TeacherLeaveAuditUser();
        auditUser.setAuditUser(userIds);
        auditUser.setInstituteId(instituteId);
        auditUser.setType(type);
        auditUser.setSchoolId(institute.getSchoolId());
        auditUser.setIsDelete((short) 2);
        auditUser.setCreateTime(new Date());
        boolean add = teacherLeaveAuditUserMapper.insertSelective(auditUser) > 0;

        if (!(add && delete)) {
            throw new BizException(Result.CUSTOM_MESSAGE, "添加审核人失败");
        }
    }
}
