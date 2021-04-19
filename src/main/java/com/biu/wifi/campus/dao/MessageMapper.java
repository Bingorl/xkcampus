package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.Message;
import com.biu.wifi.campus.dao.model.MessageCriteria;
import com.biu.wifi.core.base.CoreDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageMapper extends CoreDao {
    long countByExample(MessageCriteria example);

    int deleteByExample(MessageCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(Message record);

    int insertSelective(Message record);

    List<Message> selectByExample(MessageCriteria example);

    Message selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Message record, @Param("example") MessageCriteria example);

    int updateByExample(@Param("record") Message record, @Param("example") MessageCriteria example);

    int updateByPrimaryKeySelective(Message record);

    int updateByPrimaryKey(Message record);
}