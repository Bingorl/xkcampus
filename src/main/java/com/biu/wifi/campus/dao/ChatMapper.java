package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.Chat;
import com.biu.wifi.campus.dao.model.ChatCriteria;
import com.biu.wifi.core.base.CoreDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMapper extends CoreDao {
    int countByExample(ChatCriteria example);

    int deleteByExample(ChatCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(Chat record);

    int insertSelective(Chat record);

    List<Chat> selectByExample(ChatCriteria example);

    Chat selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Chat record, @Param("example") ChatCriteria example);

    int updateByExample(@Param("record") Chat record, @Param("example") ChatCriteria example);

    int updateByPrimaryKeySelective(Chat record);

    int updateByPrimaryKey(Chat record);
}