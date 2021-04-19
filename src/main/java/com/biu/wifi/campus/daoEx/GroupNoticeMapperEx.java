package com.biu.wifi.campus.daoEx;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface GroupNoticeMapperEx {

    //通知列表,(根据标题内容搜索,日历查询,群组查询)
    List<Map<String, Object>> noticeList(@Param("search_word") String search_word, @Param("type") Integer type, @Param("date") String date, @Param("group_id") Integer group_id, @Param("time") String time, @Param("user_id") Integer user_id);

    //通知提问列表
    List<Map<String, Object>> findNoticeQuestionList(@Param("id") Integer id, @Param("time") String time);

    //通知提问的回复列表
    List<Map<String, Object>> findQuestionReplyList(@Param("qid") Integer qid, @Param("time") String time);

    //通知收到,未收到列表
    List<Map<String, Object>> findReceivedList(@Param("id") Integer id, @Param("type") Short type);

    //群组搜索附件
    List<Map<String, Object>> findNoticeAttachList(@Param("search_word") String search_word, @Param("group_id") Integer group_id, @Param("time") String time, @Param("user_id") Integer user_id);

    //获取日历事件列表
    List<Map<String, Object>> findCalderNoticeList(@Param("user_id") Integer user_id, @Param("type") Integer type, @Param("search_date") String search_date, @Param("order_type") Integer order_type);

    //获取日历是否添加事件
    List<Map<String, Object>> findIsCalderList(@Param("user_id") Integer user_id, @Param("search_date") String search_date);

    //获取未读通知数
    int getNotReadNotice(@Param("user_id") Integer user_id);
}
