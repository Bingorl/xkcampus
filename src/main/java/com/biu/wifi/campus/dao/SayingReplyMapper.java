package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.SayingReply;
import com.biu.wifi.campus.dao.model.SayingReplyCriteria;
import com.biu.wifi.core.base.CoreDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SayingReplyMapper extends CoreDao {
    long countByExample(SayingReplyCriteria example);

    int deleteByExample(SayingReplyCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(SayingReply record);

    int insertSelective(SayingReply record);

    List<SayingReply> selectByExample(SayingReplyCriteria example);

    SayingReply selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SayingReply record, @Param("example") SayingReplyCriteria example);

    int updateByExample(@Param("record") SayingReply record, @Param("example") SayingReplyCriteria example);

    int updateByPrimaryKeySelective(SayingReply record);

    int updateByPrimaryKey(SayingReply record);
}