package com.biu.wifi.campus.service;

import com.biu.wifi.campus.dao.TeacherLeaveAuditMapper;
import com.biu.wifi.campus.dao.model.TeacherLeaveAudit;
import com.biu.wifi.campus.dao.model.TeacherLeaveAuditExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author 张彬.
 * @date 2021/4/4 0:49.
 */
@Service
public class TeacherLeaveAuditService {
    @Autowired
    private TeacherLeaveAuditMapper teacherLeaveAuditMapper;

    public int add(TeacherLeaveAudit req) {
        req.setCreateTime(new Date());
        req.setIsDelete((short) 2);
        return teacherLeaveAuditMapper.insertSelective(req);
    }

    public TeacherLeaveAudit selectByLeaveId(Integer leaveId, Integer currentAuditUserId) {
        TeacherLeaveAuditExample example = new TeacherLeaveAuditExample();
        example.createCriteria()
                .andLeaveIdEqualTo(leaveId)
                .andUserIdEqualTo(currentAuditUserId)
                .andIsDeleteEqualTo((short) 2);
        List<TeacherLeaveAudit> list = teacherLeaveAuditMapper.selectByExample(example);
        return list == null ? null : list.get(0);
    }

    public int update(TeacherLeaveAudit teacherLeaveAudit) {
        teacherLeaveAudit.setUpdateTime(new Date());
        return teacherLeaveAuditMapper.updateByPrimaryKeySelective(teacherLeaveAudit);
    }
}
