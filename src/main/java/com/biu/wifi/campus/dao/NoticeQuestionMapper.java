package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.NoticeQuestion;
import com.biu.wifi.campus.dao.model.NoticeQuestionCriteria;
import com.biu.wifi.core.base.CoreDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeQuestionMapper extends CoreDao {
    int countByExample(NoticeQuestionCriteria example);

    int deleteByExample(NoticeQuestionCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(NoticeQuestion record);

    int insertSelective(NoticeQuestion record);

    List<NoticeQuestion> selectByExample(NoticeQuestionCriteria example);

    NoticeQuestion selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") NoticeQuestion record, @Param("example") NoticeQuestionCriteria example);

    int updateByExample(@Param("record") NoticeQuestion record, @Param("example") NoticeQuestionCriteria example);

    int updateByPrimaryKeySelective(NoticeQuestion record);

    int updateByPrimaryKey(NoticeQuestion record);
}