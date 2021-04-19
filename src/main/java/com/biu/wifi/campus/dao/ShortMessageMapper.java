package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.ShortMessage;
import com.biu.wifi.campus.dao.model.ShortMessageCriteria;
import com.biu.wifi.core.base.CoreDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShortMessageMapper extends CoreDao {
    long countByExample(ShortMessageCriteria example);

    int deleteByExample(ShortMessageCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(ShortMessage record);

    int insertSelective(ShortMessage record);

    List<ShortMessage> selectByExample(ShortMessageCriteria example);

    ShortMessage selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ShortMessage record, @Param("example") ShortMessageCriteria example);

    int updateByExample(@Param("record") ShortMessage record, @Param("example") ShortMessageCriteria example);

    int updateByPrimaryKeySelective(ShortMessage record);

    int updateByPrimaryKey(ShortMessage record);
}