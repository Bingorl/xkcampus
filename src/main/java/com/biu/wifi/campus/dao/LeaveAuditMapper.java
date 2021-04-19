package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.LeaveAudit;
import com.biu.wifi.campus.dao.model.LeaveAuditCriteria;
import com.biu.wifi.core.base.CoreDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveAuditMapper extends CoreDao {
    long countByExample(LeaveAuditCriteria example);

    int deleteByExample(LeaveAuditCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(LeaveAudit record);

    int insertSelective(LeaveAudit record);

    List<LeaveAudit> selectByExample(LeaveAuditCriteria example);

    LeaveAudit selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") LeaveAudit record, @Param("example") LeaveAuditCriteria example);

    int updateByExample(@Param("record") LeaveAudit record, @Param("example") LeaveAuditCriteria example);

    int updateByPrimaryKeySelective(LeaveAudit record);

    int updateByPrimaryKey(LeaveAudit record);
}