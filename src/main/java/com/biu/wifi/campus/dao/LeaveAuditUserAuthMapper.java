package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.LeaveAuditUserAuth;
import com.biu.wifi.campus.dao.model.LeaveAuditUserAuthCriteria;
import com.biu.wifi.core.base.CoreDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveAuditUserAuthMapper extends CoreDao {
    long countByExample(LeaveAuditUserAuthCriteria example);

    int deleteByExample(LeaveAuditUserAuthCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(LeaveAuditUserAuth record);

    int insertSelective(LeaveAuditUserAuth record);

    List<LeaveAuditUserAuth> selectByExample(LeaveAuditUserAuthCriteria example);

    LeaveAuditUserAuth selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") LeaveAuditUserAuth record, @Param("example") LeaveAuditUserAuthCriteria example);

    int updateByExample(@Param("record") LeaveAuditUserAuth record, @Param("example") LeaveAuditUserAuthCriteria example);

    int updateByPrimaryKeySelective(LeaveAuditUserAuth record);

    int updateByPrimaryKey(LeaveAuditUserAuth record);
}