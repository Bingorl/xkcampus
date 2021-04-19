package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.LeaveMessage;
import com.biu.wifi.campus.dao.model.LeaveMessageCriteria;
import com.biu.wifi.core.base.CoreDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveMessageMapper extends CoreDao {
    long countByExample(LeaveMessageCriteria example);

    int deleteByExample(LeaveMessageCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(LeaveMessage record);

    int insertSelective(LeaveMessage record);

    List<LeaveMessage> selectByExample(LeaveMessageCriteria example);

    LeaveMessage selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") LeaveMessage record, @Param("example") LeaveMessageCriteria example);

    int updateByExample(@Param("record") LeaveMessage record, @Param("example") LeaveMessageCriteria example);

    int updateByPrimaryKeySelective(LeaveMessage record);

    int updateByPrimaryKey(LeaveMessage record);
}