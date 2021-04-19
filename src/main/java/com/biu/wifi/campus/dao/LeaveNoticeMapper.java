package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.LeaveNotice;
import com.biu.wifi.campus.dao.model.LeaveNoticeCriteria;
import com.biu.wifi.core.base.CoreDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveNoticeMapper extends CoreDao {
    long countByExample(LeaveNoticeCriteria example);

    int deleteByExample(LeaveNoticeCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(LeaveNotice record);

    int insertSelective(LeaveNotice record);

    List<LeaveNotice> selectByExample(LeaveNoticeCriteria example);

    LeaveNotice selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") LeaveNotice record, @Param("example") LeaveNoticeCriteria example);

    int updateByExample(@Param("record") LeaveNotice record, @Param("example") LeaveNoticeCriteria example);

    int updateByPrimaryKeySelective(LeaveNotice record);

    int updateByPrimaryKey(LeaveNotice record);
}