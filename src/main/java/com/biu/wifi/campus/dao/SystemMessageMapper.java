package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.SystemMessage;
import com.biu.wifi.campus.dao.model.SystemMessageCriteria;
import com.biu.wifi.core.base.CoreDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SystemMessageMapper extends CoreDao {
    long countByExample(SystemMessageCriteria example);

    int deleteByExample(SystemMessageCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(SystemMessage record);

    int insertSelective(SystemMessage record);

    List<SystemMessage> selectByExample(SystemMessageCriteria example);

    SystemMessage selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SystemMessage record, @Param("example") SystemMessageCriteria example);

    int updateByExample(@Param("record") SystemMessage record, @Param("example") SystemMessageCriteria example);

    int updateByPrimaryKeySelective(SystemMessage record);

    int updateByPrimaryKey(SystemMessage record);
}