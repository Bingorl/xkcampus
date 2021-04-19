package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.AuditPermission;
import com.biu.wifi.campus.dao.model.AuditPermissionCriteria;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditPermissionMapper {
    long countByExample(AuditPermissionCriteria example);

    int deleteByExample(AuditPermissionCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(AuditPermission record);

    int insertSelective(AuditPermission record);

    List<AuditPermission> selectByExample(AuditPermissionCriteria example);

    AuditPermission selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") AuditPermission record, @Param("example") AuditPermissionCriteria example);

    int updateByExample(@Param("record") AuditPermission record, @Param("example") AuditPermissionCriteria example);

    int updateByPrimaryKeySelective(AuditPermission record);

    int updateByPrimaryKey(AuditPermission record);
}