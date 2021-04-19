package com.biu.wifi.campus.service;

import com.biu.wifi.campus.dao.AuditPermissionMapper;
import com.biu.wifi.campus.dao.model.AuditPermission;
import com.biu.wifi.campus.dao.model.AuditPermissionCriteria;
import com.biu.wifi.campus.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author zhangbin.
 * @date 2018/12/17.
 */
@Service
public class AuditPermissionService {

    @Autowired
    private AuditPermissionMapper auditPermissionMapper;

    public Result add(AuditPermission auditPermission) {
        AuditPermissionCriteria example = new AuditPermissionCriteria();
        example.createCriteria()
                .andIsDeleteEqualTo((short) 2)
                .andPidEqualTo(auditPermission.getPid())
                .andTypeEqualTo(auditPermission.getType())
                .andSchoolIdEqualTo(auditPermission.getSchoolId())
                .andNameEqualTo(auditPermission.getName())
                .andValueEqualTo(auditPermission.getValue());

        long count = auditPermissionMapper.countByExample(example);
        if (count > 0) {
            return new Result(Result.CUSTOM_MESSAGE, "该权限已存在");
        }

        auditPermission.setCreateTime(new Date());
        int result = auditPermissionMapper.insertSelective(auditPermission);
        if (result > 0) {
            return new Result(Result.SUCCESS, "成功");
        } else {
            return new Result(Result.FAILURE, "失败");
        }
    }

    public Result delete(Integer id) {
        AuditPermissionCriteria example = new AuditPermissionCriteria();
        example.setOrderByClause("create_time desc");
        example.createCriteria()
                .andIsDeleteEqualTo((short) 2)
                .andIdEqualTo(id);

        List<AuditPermission> permissionList = auditPermissionMapper.selectByExample(example);
        if (permissionList.isEmpty()) {
            return new Result(Result.CUSTOM_MESSAGE, "该记录已被删除");
        }

        AuditPermission auditPermission = permissionList.get(0);
        auditPermission.setIsDelete((short) 1);
        auditPermission.setDeleteTime(new Date());
        int result = auditPermissionMapper.updateByPrimaryKeySelective(auditPermission);
        if (result > 0) {
            return new Result(Result.SUCCESS, "成功");
        } else {
            return new Result(Result.FAILURE, "失败");
        }
    }

    public List<AuditPermission> findList(AuditPermissionCriteria example) {
        return auditPermissionMapper.selectByExample(example);
    }

    public AuditPermission findById(Integer id) {
        return auditPermissionMapper.selectByPrimaryKey(id);
    }

}
