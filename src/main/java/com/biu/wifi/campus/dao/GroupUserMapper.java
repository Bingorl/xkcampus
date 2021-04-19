package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.GroupUser;
import com.biu.wifi.campus.dao.model.GroupUserCriteria;
import com.biu.wifi.core.base.CoreDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupUserMapper extends CoreDao {
    int countByExample(GroupUserCriteria example);

    int deleteByExample(GroupUserCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(GroupUser record);

    int insertSelective(GroupUser record);

    List<GroupUser> selectByExample(GroupUserCriteria example);

    GroupUser selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") GroupUser record, @Param("example") GroupUserCriteria example);

    int updateByExample(@Param("record") GroupUser record, @Param("example") GroupUserCriteria example);

    int updateByPrimaryKeySelective(GroupUser record);

    int updateByPrimaryKey(GroupUser record);
}