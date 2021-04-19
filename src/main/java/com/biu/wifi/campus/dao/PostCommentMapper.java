package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.PostComment;
import com.biu.wifi.campus.dao.model.PostCommentCriteria;
import com.biu.wifi.core.base.CoreDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostCommentMapper extends CoreDao {
    int countByExample(PostCommentCriteria example);

    int deleteByExample(PostCommentCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(PostComment record);

    int insertSelective(PostComment record);

    List<PostComment> selectByExample(PostCommentCriteria example);

    PostComment selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") PostComment record, @Param("example") PostCommentCriteria example);

    int updateByExample(@Param("record") PostComment record, @Param("example") PostCommentCriteria example);

    int updateByPrimaryKeySelective(PostComment record);

    int updateByPrimaryKey(PostComment record);
}