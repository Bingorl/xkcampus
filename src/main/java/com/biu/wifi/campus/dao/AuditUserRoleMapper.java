package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.AuditUserRole;
import com.biu.wifi.campus.dao.model.AuditUserRoleCriteria;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditUserRoleMapper {
    long countByExample(AuditUserRoleCriteria example);

    int deleteByExample(AuditUserRoleCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(AuditUserRole record);

    int insertSelective(AuditUserRole record);

    List<AuditUserRole> selectByExample(AuditUserRoleCriteria example);

    AuditUserRole selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") AuditUserRole record, @Param("example") AuditUserRoleCriteria example);

    int updateByExample(@Param("record") AuditUserRole record, @Param("example") AuditUserRoleCriteria example);

    int updateByPrimaryKeySelective(AuditUserRole record);

    int updateByPrimaryKey(AuditUserRole record);
}