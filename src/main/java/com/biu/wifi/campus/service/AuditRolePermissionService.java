package com.biu.wifi.campus.service;

import com.biu.wifi.campus.Tool.BeanUtil;
import com.biu.wifi.campus.dao.AuditPermissionMapper;
import com.biu.wifi.campus.dao.AuditUserRoleMapper;
import com.biu.wifi.campus.dao.model.*;
import com.biu.wifi.campus.result.Result;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zhangbin.
 * @date 2018/12/17.
 */
@Service
public class AuditRolePermissionService {

    @Autowired
    private AuditRolePermissionMapper auditRolePermissionMapper;
    @Autowired
    private AuditUserRoleMapper auditUserRoleMapper;
    @Autowired
    private AuditPermissionMapper auditPermissionMapper;

    public Result add(List<AuditRolePermission> auditRolePermissionList) {
        if (CollectionUtils.isEmpty(auditRolePermissionList)) {
            return new Result(Result.CUSTOM_MESSAGE, "请选择相应的角色权限");
        }

        AuditRolePermission auditRolePermission = auditRolePermissionList.get(0);
        //删除旧权限
        AuditRolePermissionCriteria example = new AuditRolePermissionCriteria();
        example.createCriteria()
                .andSchoolIdEqualTo(auditRolePermission.getSchoolId())
                .andRoleIdEqualTo(auditRolePermission.getRoleId());
        auditRolePermissionMapper.deleteByExample(example);
        //添加新权限
        for (AuditRolePermission rolePermission : auditRolePermissionList) {
            auditRolePermissionMapper.insertSelective(rolePermission);
        }

        return new Result(Result.SUCCESS, "成功");
    }

    public List<AuditRolePermission> findList(AuditRolePermissionCriteria example) {
        return auditRolePermissionMapper.selectByExample(example);
    }

    public List<Map> findMapList(AuditRolePermissionCriteria example) {
        List<AuditRolePermission> auditRolePermissionList = findList(example);
        List<Map> mapList = BeanUtil.beanListToMapList(auditRolePermissionList, "");
        for (Map map : mapList) {
            AuditUserRole auditUserRole = auditUserRoleMapper.selectByPrimaryKey(Integer.valueOf(map.get("roleId").toString()));
        }
        return mapList;
    }

    /**
     * 根据角色ID获取权限列表
     *
     * @param roleId
     * @return
     */
    public List<AuditPermission> findPermissionListByRoleId(Integer roleId) {
        AuditRolePermissionCriteria example = new AuditRolePermissionCriteria();
        example.createCriteria()
                .andRoleIdEqualTo(roleId);
        List<AuditRolePermission> rolePermissionList = auditRolePermissionMapper.selectByExample(example);

        List<AuditPermission> permissionList = new ArrayList<>();
        for (AuditRolePermission rolePermission : rolePermissionList) {
            AuditPermission permission = auditPermissionMapper.selectByPrimaryKey(rolePermission.getPermissionId());
            permissionList.add(permission);
        }
        return permissionList;
    }

    /**
     * 通过角色ID和权限code查询是否有对应的权限
     *
     * @param roleId
     * @param permissionCode
     * @return
     */
    public boolean findPermissionByCodeAndRoleId(Integer roleId, String permissionCode) {
        AuditRolePermissionCriteria auditRolePermissionEx = new AuditRolePermissionCriteria();
        auditRolePermissionEx.createCriteria()
                .andRoleIdEqualTo(roleId);
        List<AuditRolePermission> auditRolePermissionList = findList(auditRolePermissionEx);
        List<Integer> permissionIdList = new ArrayList<>();
        for (AuditRolePermission auditRolePermission : auditRolePermissionList) {
            permissionIdList.add(auditRolePermission.getPermissionId());
        }
        AuditPermissionCriteria auditPermissionEx = new AuditPermissionCriteria();
        auditPermissionEx.createCriteria()
                .andIsDeleteEqualTo((short) 2)
                .andIdIn(permissionIdList)
                .andCodeEqualTo(permissionCode);
        long count = auditPermissionMapper.countByExample(auditPermissionEx);
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }
}
