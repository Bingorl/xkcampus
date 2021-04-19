package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.TeachingCourseTimePlan;
import com.biu.wifi.campus.dao.model.TeachingCourseTimePlanCriteria;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeachingCourseTimePlanMapper {
    long countByExample(TeachingCourseTimePlanCriteria example);

    int deleteByExample(TeachingCourseTimePlanCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(TeachingCourseTimePlan record);

    int insertSelective(TeachingCourseTimePlan record);

    List<TeachingCourseTimePlan> selectByExample(TeachingCourseTimePlanCriteria example);

    TeachingCourseTimePlan selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TeachingCourseTimePlan record, @Param("example") TeachingCourseTimePlanCriteria example);

    int updateByExample(@Param("record") TeachingCourseTimePlan record, @Param("example") TeachingCourseTimePlanCriteria example);

    int updateByPrimaryKeySelective(TeachingCourseTimePlan record);

    int updateByPrimaryKey(TeachingCourseTimePlan record);
}