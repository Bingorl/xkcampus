package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.LeaveType;
import com.biu.wifi.campus.dao.model.LeaveTypeCriteria;
import com.biu.wifi.core.base.CoreDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveTypeMapper extends CoreDao {
    long countByExample(LeaveTypeCriteria example);

    int deleteByExample(LeaveTypeCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(LeaveType record);

    int insertSelective(LeaveType record);

    List<LeaveType> selectByExample(LeaveTypeCriteria example);

    LeaveType selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") LeaveType record, @Param("example") LeaveTypeCriteria example);

    int updateByExample(@Param("record") LeaveType record, @Param("example") LeaveTypeCriteria example);

    int updateByPrimaryKeySelective(LeaveType record);

    int updateByPrimaryKey(LeaveType record);
}