package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.GroupMessage;
import com.biu.wifi.campus.dao.model.GroupMessageCriteria;
import com.biu.wifi.core.base.CoreDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupMessageMapper extends CoreDao {
    long countByExample(GroupMessageCriteria example);

    int deleteByExample(GroupMessageCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(GroupMessage record);

    int insertSelective(GroupMessage record);

    List<GroupMessage> selectByExample(GroupMessageCriteria example);

    GroupMessage selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") GroupMessage record, @Param("example") GroupMessageCriteria example);

    int updateByExample(@Param("record") GroupMessage record, @Param("example") GroupMessageCriteria example);

    int updateByPrimaryKeySelective(GroupMessage record);

    int updateByPrimaryKey(GroupMessage record);
}