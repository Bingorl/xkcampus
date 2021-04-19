package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.ActivityItem;
import com.biu.wifi.campus.dao.model.ActivityItemCriteria;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityItemMapper {
    long countByExample(ActivityItemCriteria example);

    int deleteByExample(ActivityItemCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(ActivityItem record);

    int insertSelective(ActivityItem record);

    List<ActivityItem> selectByExample(ActivityItemCriteria example);

    ActivityItem selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ActivityItem record, @Param("example") ActivityItemCriteria example);

    int updateByExample(@Param("record") ActivityItem record, @Param("example") ActivityItemCriteria example);

    int updateByPrimaryKeySelective(ActivityItem record);

    int updateByPrimaryKey(ActivityItem record);
}