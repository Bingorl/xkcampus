package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.Grade;
import com.biu.wifi.campus.dao.model.GradeCriteria;
import com.biu.wifi.core.base.CoreDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradeMapper extends CoreDao {
    long countByExample(GradeCriteria example);

    int deleteByExample(GradeCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(Grade record);

    int insertSelective(Grade record);

    List<Grade> selectByExample(GradeCriteria example);

    Grade selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Grade record, @Param("example") GradeCriteria example);

    int updateByExample(@Param("record") Grade record, @Param("example") GradeCriteria example);

    int updateByPrimaryKeySelective(Grade record);

    int updateByPrimaryKey(Grade record);
}