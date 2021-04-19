package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.PostCommentLike;
import com.biu.wifi.campus.dao.model.PostCommentLikeCriteria;
import com.biu.wifi.core.base.CoreDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostCommentLikeMapper extends CoreDao {
    int countByExample(PostCommentLikeCriteria example);

    int deleteByExample(PostCommentLikeCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(PostCommentLike record);

    int insertSelective(PostCommentLike record);

    List<PostCommentLike> selectByExample(PostCommentLikeCriteria example);

    PostCommentLike selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") PostCommentLike record, @Param("example") PostCommentLikeCriteria example);

    int updateByExample(@Param("record") PostCommentLike record, @Param("example") PostCommentLikeCriteria example);

    int updateByPrimaryKeySelective(PostCommentLike record);

    int updateByPrimaryKey(PostCommentLike record);
}