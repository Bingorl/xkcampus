package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.PostReply;
import com.biu.wifi.campus.dao.model.PostReplyCriteria;
import com.biu.wifi.core.base.CoreDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostReplyMapper extends CoreDao {
    int countByExample(PostReplyCriteria example);

    int deleteByExample(PostReplyCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(PostReply record);

    int insertSelective(PostReply record);

    List<PostReply> selectByExample(PostReplyCriteria example);

    PostReply selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") PostReply record, @Param("example") PostReplyCriteria example);

    int updateByExample(@Param("record") PostReply record, @Param("example") PostReplyCriteria example);

    int updateByPrimaryKeySelective(PostReply record);

    int updateByPrimaryKey(PostReply record);
}