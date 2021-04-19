package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.NoticeReceive;
import com.biu.wifi.campus.dao.model.NoticeReceiveCriteria;
import com.biu.wifi.core.base.CoreDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeReceiveMapper extends CoreDao {
    long countByExample(NoticeReceiveCriteria example);

    int deleteByExample(NoticeReceiveCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(NoticeReceive record);

    int insertSelective(NoticeReceive record);

    List<NoticeReceive> selectByExample(NoticeReceiveCriteria example);

    NoticeReceive selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") NoticeReceive record, @Param("example") NoticeReceiveCriteria example);

    int updateByExample(@Param("record") NoticeReceive record, @Param("example") NoticeReceiveCriteria example);

    int updateByPrimaryKeySelective(NoticeReceive record);

    int updateByPrimaryKey(NoticeReceive record);
}