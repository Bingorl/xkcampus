package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.AtMessage;
import com.biu.wifi.campus.dao.model.AtMessageCriteria;
import com.biu.wifi.core.base.CoreDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AtMessageMapper extends CoreDao {
    long countByExample(AtMessageCriteria example);

    int deleteByExample(AtMessageCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(AtMessage record);

    int insertSelective(AtMessage record);

    List<AtMessage> selectByExample(AtMessageCriteria example);

    AtMessage selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") AtMessage record, @Param("example") AtMessageCriteria example);

    int updateByExample(@Param("record") AtMessage record, @Param("example") AtMessageCriteria example);

    int updateByPrimaryKeySelective(AtMessage record);

    int updateByPrimaryKey(AtMessage record);
}