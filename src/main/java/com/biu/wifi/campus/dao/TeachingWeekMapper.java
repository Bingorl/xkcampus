package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.TeachingWeek;
import com.biu.wifi.campus.dao.model.TeachingWeekCriteria;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeachingWeekMapper {
    long countByExample(TeachingWeekCriteria example);

    int deleteByExample(TeachingWeekCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(TeachingWeek record);

    int insertSelective(TeachingWeek record);

    List<TeachingWeek> selectByExample(TeachingWeekCriteria example);

    TeachingWeek selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TeachingWeek record, @Param("example") TeachingWeekCriteria example);

    int updateByExample(@Param("record") TeachingWeek record, @Param("example") TeachingWeekCriteria example);

    int updateByPrimaryKeySelective(TeachingWeek record);

    int updateByPrimaryKey(TeachingWeek record);
}