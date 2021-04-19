package com.biu.wifi.campus.daoEx;

import com.biu.wifi.campus.dao.model.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface SayingMapperEx {

    //获取新鲜事列表
    List<Map<String, Object>> user_findSayingList(@Param("userId") Integer userId, @Param("time") String time);

    //获取新鲜事列表页评论回复列表(15条)
    List<Map<String, Object>> findCommentAndReplyList(@Param("sayingId") Integer sayingId);

    //新鲜事详情
    Map<String, Object> user_getSayingInfo(@Param("userId") Integer userId, @Param("sayingId") Integer sayingId);

    //获取新鲜事评论列表
    List<Map<String, Object>> user_findSayingCommentList(@Param("userId") Integer userId, @Param("time") String time, @Param("sayingId") Integer sayingId);

    //根据评论id获取回复列表
    List<Map<String, Object>> findReplyListFromCommentId(@Param("commentId") Integer commentId);

    //批量新增@信息
    int insertAllAtMsg(@Param("type") Short type, @Param("userId") Integer userId, @Param("user") String user,
                       @Param("toerList") List<User> toerList, @Param("objectId") Integer objectId, @Param("content") String content,
                       @Param("originContent") String originContent);

    //获取新鲜事点赞的人列表
    List<Map<String, Object>> user_findSayindLikeList(@Param("time") String time, @Param("sayingId") Integer sayingId);

}
