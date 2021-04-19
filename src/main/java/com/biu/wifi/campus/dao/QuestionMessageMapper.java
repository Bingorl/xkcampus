package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.QuestionMessage;
import com.biu.wifi.campus.dao.model.QuestionMessageCriteria;
import com.biu.wifi.core.base.CoreDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionMessageMapper extends CoreDao {
    long countByExample(QuestionMessageCriteria example);

    int deleteByExample(QuestionMessageCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(QuestionMessage record);

    int insertSelective(QuestionMessage record);

    List<QuestionMessage> selectByExample(QuestionMessageCriteria example);

    QuestionMessage selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") QuestionMessage record, @Param("example") QuestionMessageCriteria example);

    int updateByExample(@Param("record") QuestionMessage record, @Param("example") QuestionMessageCriteria example);

    int updateByPrimaryKeySelective(QuestionMessage record);

    int updateByPrimaryKey(QuestionMessage record);
}