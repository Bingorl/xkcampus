package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.School;
import com.biu.wifi.campus.dao.model.SchoolCriteria;
import com.biu.wifi.core.base.CoreDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SchoolMapper extends CoreDao {
    int countByExample(SchoolCriteria example);

    int deleteByExample(SchoolCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(School record);

    int insertSelective(School record);

    List<School> selectByExample(SchoolCriteria example);

    School selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") School record, @Param("example") SchoolCriteria example);

    int updateByExample(@Param("record") School record, @Param("example") SchoolCriteria example);

    int updateByPrimaryKeySelective(School record);

    int updateByPrimaryKey(School record);
}