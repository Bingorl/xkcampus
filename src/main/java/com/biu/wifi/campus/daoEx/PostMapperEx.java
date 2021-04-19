package com.biu.wifi.campus.daoEx;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface PostMapperEx {

    //首页搜索,帖子搜索,通过帖子标题,名称或者楼搜索
    public List<Map<String, Object>> findPostListByName(@Param("search_word") String search_word, @Param("time") String time);

    //首页搜索,新鲜事根据内容搜索
    public List<Map<String, Object>> findSayingListByName(@Param("search_word") String search_word, @Param("time") String time, @Param("user_id") Integer user_id);

    //首页搜索,群组根据群名称群号搜索
    public List<Map<String, Object>> findGroupListByname(@Param("search_word") String search_word, @Param("time") String time);

    //论坛首页置顶帖和公告贴列表,最多六条
    public List<Map<String, Object>> findTopPostList(@Param("school_id") Integer school_id, @Param("time") String time);

    //论坛帖子列表
    public List<Map<String, Object>> findPostList(@Param("type") Integer type, @Param("school_id") Integer school_id, @Param("search_type") Integer search_type, @Param("essence_type") Integer essence_type, @Param("time") String time);

    //帖子楼层列表
    public List<Map<String, Object>> findPostFloorList(@Param("type") Integer type, @Param("post_id") Integer post_id, @Param("search_type") Integer search_type, @Param("time") String time, @Param("user_id") Integer user_id);

    //楼层回复列表
    public List<Map<String, Object>> findPostReplyList(@Param("id") Integer id, @Param("time") String time);

    public List<Map<String, Object>> selectList();
}
