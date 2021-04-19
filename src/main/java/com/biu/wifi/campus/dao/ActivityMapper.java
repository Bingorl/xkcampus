package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.Activity;
import com.biu.wifi.campus.dao.model.ActivityCriteria;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityMapper {
    long countByExample(ActivityCriteria example);

    int deleteByExample(ActivityCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(Activity record);

    int insertSelective(Activity record);

    List<Activity> selectByExample(ActivityCriteria example);

    Activity selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Activity record, @Param("example") ActivityCriteria example);

    int updateByExample(@Param("record") Activity record, @Param("example") ActivityCriteria example);

    int updateByPrimaryKeySelective(Activity record);

    int updateByPrimaryKey(Activity record);
}