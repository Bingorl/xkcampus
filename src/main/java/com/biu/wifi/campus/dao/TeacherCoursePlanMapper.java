package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.TeacherCoursePlan;
import com.biu.wifi.campus.dao.model.TeacherCoursePlanCriteria;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherCoursePlanMapper {
    long countByExample(TeacherCoursePlanCriteria example);

    int deleteByExample(TeacherCoursePlanCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(TeacherCoursePlan record);

    int insertSelective(TeacherCoursePlan record);

    List<TeacherCoursePlan> selectByExampleWithBLOBs(TeacherCoursePlanCriteria example);

    List<TeacherCoursePlan> selectByExample(TeacherCoursePlanCriteria example);

    TeacherCoursePlan selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TeacherCoursePlan record, @Param("example") TeacherCoursePlanCriteria example);

    int updateByExampleWithBLOBs(@Param("record") TeacherCoursePlan record, @Param("example") TeacherCoursePlanCriteria example);

    int updateByExample(@Param("record") TeacherCoursePlan record, @Param("example") TeacherCoursePlanCriteria example);

    int updateByPrimaryKeySelective(TeacherCoursePlan record);

    int updateByPrimaryKeyWithBLOBs(TeacherCoursePlan record);

    int updateByPrimaryKey(TeacherCoursePlan record);
}