package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.Group;
import com.biu.wifi.campus.dao.model.GroupCriteria;
import com.biu.wifi.core.base.CoreDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupMapper extends CoreDao {
    long countByExample(GroupCriteria example);

    int deleteByExample(GroupCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(Group record);

    int insertSelective(Group record);

    List<Group> selectByExample(GroupCriteria example);

    Group selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Group record, @Param("example") GroupCriteria example);

    int updateByExample(@Param("record") Group record, @Param("example") GroupCriteria example);

    int updateByPrimaryKeySelective(Group record);

    int updateByPrimaryKey(Group record);
}