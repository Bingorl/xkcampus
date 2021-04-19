package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.StudentInfo;
import com.biu.wifi.campus.dao.model.StudentInfoCriteria;
import com.biu.wifi.core.base.CoreDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentInfoMapper extends CoreDao {
    int countByExample(StudentInfoCriteria example);

    int deleteByExample(StudentInfoCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(StudentInfo record);

    int insertSelective(StudentInfo record);

    List<StudentInfo> selectByExample(StudentInfoCriteria example);

    StudentInfo selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") StudentInfo record, @Param("example") StudentInfoCriteria example);

    int updateByExample(@Param("record") StudentInfo record, @Param("example") StudentInfoCriteria example);

    int updateByPrimaryKeySelective(StudentInfo record);

    int updateByPrimaryKey(StudentInfo record);
}