package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.SayingCommentLike;
import com.biu.wifi.campus.dao.model.SayingCommentLikeCriteria;
import com.biu.wifi.core.base.CoreDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SayingCommentLikeMapper extends CoreDao {
    int countByExample(SayingCommentLikeCriteria example);

    int deleteByExample(SayingCommentLikeCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(SayingCommentLike record);

    int insertSelective(SayingCommentLike record);

    List<SayingCommentLike> selectByExample(SayingCommentLikeCriteria example);

    SayingCommentLike selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SayingCommentLike record, @Param("example") SayingCommentLikeCriteria example);

    int updateByExample(@Param("record") SayingCommentLike record, @Param("example") SayingCommentLikeCriteria example);

    int updateByPrimaryKeySelective(SayingCommentLike record);

    int updateByPrimaryKey(SayingCommentLike record);
}