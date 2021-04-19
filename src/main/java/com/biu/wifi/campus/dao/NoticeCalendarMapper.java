package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.NoticeCalendar;
import com.biu.wifi.campus.dao.model.NoticeCalendarCriteria;
import com.biu.wifi.core.base.CoreDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeCalendarMapper extends CoreDao {
    int countByExample(NoticeCalendarCriteria example);

    int deleteByExample(NoticeCalendarCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(NoticeCalendar record);

    int insertSelective(NoticeCalendar record);

    List<NoticeCalendar> selectByExample(NoticeCalendarCriteria example);

    NoticeCalendar selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") NoticeCalendar record, @Param("example") NoticeCalendarCriteria example);

    int updateByExample(@Param("record") NoticeCalendar record, @Param("example") NoticeCalendarCriteria example);

    int updateByPrimaryKeySelective(NoticeCalendar record);

    int updateByPrimaryKey(NoticeCalendar record);
}