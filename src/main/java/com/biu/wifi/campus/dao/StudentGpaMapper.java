package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.StudentGpa;
import com.biu.wifi.campus.dao.model.StudentGpaCriteria;
import com.biu.wifi.core.base.CoreDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentGpaMapper extends CoreDao {
    long countByExample(StudentGpaCriteria example);

    int deleteByExample(StudentGpaCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(StudentGpa record);

    int insertSelective(StudentGpa record);

    List<StudentGpa> selectByExample(StudentGpaCriteria example);

    StudentGpa selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") StudentGpa record, @Param("example") StudentGpaCriteria example);

    int updateByExample(@Param("record") StudentGpa record, @Param("example") StudentGpaCriteria example);

    int updateByPrimaryKeySelective(StudentGpa record);

    int updateByPrimaryKey(StudentGpa record);
}