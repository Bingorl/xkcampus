package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.StudentChoosedCourse;
import com.biu.wifi.campus.dao.model.StudentChoosedCourseCriteria;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentChoosedCourseMapper {
    long countByExample(StudentChoosedCourseCriteria example);

    int deleteByExample(StudentChoosedCourseCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(StudentChoosedCourse record);

    int insertSelective(StudentChoosedCourse record);

    List<StudentChoosedCourse> selectByExample(StudentChoosedCourseCriteria example);

    StudentChoosedCourse selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") StudentChoosedCourse record, @Param("example") StudentChoosedCourseCriteria example);

    int updateByExample(@Param("record") StudentChoosedCourse record, @Param("example") StudentChoosedCourseCriteria example);

    int updateByPrimaryKeySelective(StudentChoosedCourse record);

    int updateByPrimaryKey(StudentChoosedCourse record);
}