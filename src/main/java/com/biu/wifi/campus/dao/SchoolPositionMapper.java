package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.SchoolPosition;
import com.biu.wifi.campus.dao.model.SchoolPositionCriteria;
import com.biu.wifi.core.base.CoreDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SchoolPositionMapper extends CoreDao {
    long countByExample(SchoolPositionCriteria example);

    int deleteByExample(SchoolPositionCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(SchoolPosition record);

    int insertSelective(SchoolPosition record);

    List<SchoolPosition> selectByExample(SchoolPositionCriteria example);

    SchoolPosition selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SchoolPosition record, @Param("example") SchoolPositionCriteria example);

    int updateByExample(@Param("record") SchoolPosition record, @Param("example") SchoolPositionCriteria example);

    int updateByPrimaryKeySelective(SchoolPosition record);

    int updateByPrimaryKey(SchoolPosition record);
}