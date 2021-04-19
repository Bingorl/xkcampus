package com.biu.wifi.campus.daoEx;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageMapperEx {

    //提问未读数
    int getQuestionNotReadNum(@Param("user_id") Integer user_id);

    //提问回复未读数
    int getQuestionReplyNotReadNum(@Param("user_id") Integer user_id);

    //发布帖子@未读数
    int getPostNotReadNum(@Param("user_id") Integer user_id);

    //帖子盖楼@未读数
    int getPostCommentNotReadNum(@Param("user_id") Integer user_id);

    //发布新鲜事@未读数
    int getSayingNotReadNum(@Param("user_id") Integer user_id);

    //帖子点赞未读数
    int getPostLikeNotReadNum(@Param("user_id") Integer user_id);

    //楼层点赞未读数
    int getPostComemntLikeNotReadNum(@Param("user_id") Integer user_id);

    //新鲜事点赞和新鲜事评论点赞未读数
    int getSayingLikeNotReadNum(@Param("user_id") Integer user_id);

    //帖子评论回复未读数
    int getPostCommetReplyNotReadNum(@Param("user_id") Integer user_id);

    //新鲜事评论回复未读数
    int getSayingComementNotReadNum(@Param("user_id") Integer user_id);
}
