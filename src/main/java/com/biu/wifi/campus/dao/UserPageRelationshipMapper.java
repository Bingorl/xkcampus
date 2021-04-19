package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.UserPageRelationship;
import com.biu.wifi.campus.dao.model.UserPageRelationshipCriteria;
import com.biu.wifi.core.base.CoreDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPageRelationshipMapper extends CoreDao {
    long countByExample(UserPageRelationshipCriteria example);

    int deleteByExample(UserPageRelationshipCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(UserPageRelationship record);

    int insertSelective(UserPageRelationship record);

    List<UserPageRelationship> selectByExample(UserPageRelationshipCriteria example);

    UserPageRelationship selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") UserPageRelationship record, @Param("example") UserPageRelationshipCriteria example);

    int updateByExample(@Param("record") UserPageRelationship record, @Param("example") UserPageRelationshipCriteria example);

    int updateByPrimaryKeySelective(UserPageRelationship record);

    int updateByPrimaryKey(UserPageRelationship record);


}