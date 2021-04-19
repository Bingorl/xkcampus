package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.LeaveAuditUserRole;
import com.biu.wifi.campus.dao.model.LeaveAuditUserRoleCriteria;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveAuditUserRoleMapper {
    long countByExample(LeaveAuditUserRoleCriteria example);

    int deleteByExample(LeaveAuditUserRoleCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(LeaveAuditUserRole record);

    int insertSelective(LeaveAuditUserRole record);

    List<LeaveAuditUserRole> selectByExample(LeaveAuditUserRoleCriteria example);

    LeaveAuditUserRole selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") LeaveAuditUserRole record, @Param("example") LeaveAuditUserRoleCriteria example);

    int updateByExample(@Param("record") LeaveAuditUserRole record, @Param("example") LeaveAuditUserRoleCriteria example);

    int updateByPrimaryKeySelective(LeaveAuditUserRole record);

    int updateByPrimaryKey(LeaveAuditUserRole record);
}