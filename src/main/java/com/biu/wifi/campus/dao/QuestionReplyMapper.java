package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.QuestionReply;
import com.biu.wifi.campus.dao.model.QuestionReplyCriteria;
import com.biu.wifi.core.base.CoreDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionReplyMapper extends CoreDao {
    long countByExample(QuestionReplyCriteria example);

    int deleteByExample(QuestionReplyCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(QuestionReply record);

    int insertSelective(QuestionReply record);

    List<QuestionReply> selectByExample(QuestionReplyCriteria example);

    QuestionReply selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") QuestionReply record, @Param("example") QuestionReplyCriteria example);

    int updateByExample(@Param("record") QuestionReply record, @Param("example") QuestionReplyCriteria example);

    int updateByPrimaryKeySelective(QuestionReply record);

    int updateByPrimaryKey(QuestionReply record);
}