package com.biu.wifi.campus.daoEx;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface BackendGroupMapperEx {

    public List<Map<String, Object>> findManageGroupList(@Param("userId") Integer userId, @Param("groupName") String groupName);

    public List<Map<String, Object>> findGroupMembers(@Param("groupId") Integer groupId);

    public List<Map<String, Object>> findGroupNotices(@Param("groupId") Integer groupId, @Param("title") String title, @Param("content") String content);

    public List<Map<String, Object>> findNoticeQuestions(@Param("noticeId") Integer noticeId, @Param("time") Long time);

    public List<Map<String, Object>> findQuestionReplys(@Param("questionId") Integer questionId);

    public List<Map<String, Object>> findNoticeReceives(@Param("noticeId") Integer noticeId, @Param("type") Short type);

    public List<Map<String, Object>> findGroupList(@Param("schoolId") Integer schoolId, @Param("groupName") String groupName, @Param("time") Long time);
}
