package com.biu.wifi.campus.dao;

import java.util.List;

import com.biu.wifi.campus.dao.model.AuditRolePermission;
import com.biu.wifi.campus.dao.model.AuditRolePermissionCriteria;
import org.apache.ibatis.annotations.Param;

public interface AuditRolePermissionMapper {
    long countByExample(AuditRolePermissionCriteria example);

    int deleteByExample(AuditRolePermissionCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(AuditRolePermission record);

    int insertSelective(AuditRolePermission record);

    List<AuditRolePermission> selectByExample(AuditRolePermissionCriteria example);

    AuditRolePermission selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") AuditRolePermission record, @Param("example") AuditRolePermissionCriteria example);

    int updateByExample(@Param("record") AuditRolePermission record, @Param("example") AuditRolePermissionCriteria example);

    int updateByPrimaryKeySelective(AuditRolePermission record);

    int updateByPrimaryKey(AuditRolePermission record);
}