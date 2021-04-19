package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.LikeMessage;
import com.biu.wifi.campus.dao.model.LikeMessageCriteria;
import com.biu.wifi.core.base.CoreDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeMessageMapper extends CoreDao {
    long countByExample(LikeMessageCriteria example);

    int deleteByExample(LikeMessageCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(LikeMessage record);

    int insertSelective(LikeMessage record);

    List<LikeMessage> selectByExample(LikeMessageCriteria example);

    LikeMessage selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") LikeMessage record, @Param("example") LikeMessageCriteria example);

    int updateByExample(@Param("record") LikeMessage record, @Param("example") LikeMessageCriteria example);

    int updateByPrimaryKeySelective(LikeMessage record);

    int updateByPrimaryKey(LikeMessage record);
}