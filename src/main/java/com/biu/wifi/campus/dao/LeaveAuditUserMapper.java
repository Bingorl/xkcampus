package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.LeaveAuditUser;
import com.biu.wifi.campus.dao.model.LeaveAuditUserCriteria;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveAuditUserMapper {
    long countByExample(LeaveAuditUserCriteria example);

    int deleteByExample(LeaveAuditUserCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(LeaveAuditUser record);

    int insertSelective(LeaveAuditUser record);

    List<LeaveAuditUser> selectByExample(LeaveAuditUserCriteria example);

    LeaveAuditUser selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") LeaveAuditUser record, @Param("example") LeaveAuditUserCriteria example);

    int updateByExample(@Param("record") LeaveAuditUser record, @Param("example") LeaveAuditUserCriteria example);

    int updateByPrimaryKeySelective(LeaveAuditUser record);

    int updateByPrimaryKey(LeaveAuditUser record);
}