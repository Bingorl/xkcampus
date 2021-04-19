package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.SayingComment;
import com.biu.wifi.campus.dao.model.SayingCommentCriteria;
import com.biu.wifi.core.base.CoreDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SayingCommentMapper extends CoreDao {
    long countByExample(SayingCommentCriteria example);

    int deleteByExample(SayingCommentCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(SayingComment record);

    int insertSelective(SayingComment record);

    List<SayingComment> selectByExample(SayingCommentCriteria example);

    SayingComment selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SayingComment record, @Param("example") SayingCommentCriteria example);

    int updateByExample(@Param("record") SayingComment record, @Param("example") SayingCommentCriteria example);

    int updateByPrimaryKeySelective(SayingComment record);

    int updateByPrimaryKey(SayingComment record);
}