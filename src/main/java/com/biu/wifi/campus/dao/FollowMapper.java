package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.Follow;
import com.biu.wifi.campus.dao.model.FollowCriteria;
import com.biu.wifi.core.base.CoreDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowMapper extends CoreDao {
    long countByExample(FollowCriteria example);

    int deleteByExample(FollowCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(Follow record);

    int insertSelective(Follow record);

    List<Follow> selectByExample(FollowCriteria example);

    Follow selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Follow record, @Param("example") FollowCriteria example);

    int updateByExample(@Param("record") Follow record, @Param("example") FollowCriteria example);

    int updateByPrimaryKeySelective(Follow record);

    int updateByPrimaryKey(Follow record);
}