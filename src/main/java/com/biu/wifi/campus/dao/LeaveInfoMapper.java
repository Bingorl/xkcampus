package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.LeaveInfo;
import com.biu.wifi.campus.dao.model.LeaveInfoCriteria;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveInfoMapper {
    long countByExample(LeaveInfoCriteria example);

    int deleteByExample(LeaveInfoCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(LeaveInfo record);

    int insertSelective(LeaveInfo record);

    List<LeaveInfo> selectByExample(LeaveInfoCriteria example);

    LeaveInfo selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") LeaveInfo record, @Param("example") LeaveInfoCriteria example);

    int updateByExample(@Param("record") LeaveInfo record, @Param("example") LeaveInfoCriteria example);

    int updateByPrimaryKeySelective(LeaveInfo record);

    int updateByPrimaryKey(LeaveInfo record);
}