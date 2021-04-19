package com.biu.wifi.campus.daoEx;

import com.biu.wifi.campus.dao.model.LeaveMessage;
import com.biu.wifi.campus.dao.model.User;
import com.biu.wifi.campus.dao.model.UserPageRelationship;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface AppPublicPageMapperEz {

    Map<String, Object> getAudit(Integer user_id);

    User getUserPublic(Integer user_id);

    List<Map<String, Object>> getMessageBoardList(Integer pageId);

    List<Map<String, Object>> getPublicFansList(Integer id);

    List<Map<String, Object>> getPublicSameFansList(@Param("id") Integer id, @Param("schoolId") Integer schoolId, @Param("name") String name);

    List<Map<String, Object>> getPublicManage(Integer id);

    List<LeaveMessage> getReleaveMessage(Integer id, Integer user_id);

    User getUserInfo(Integer user_id);

    Integer getPageId(Integer user_id);

    List<Integer> getAdminId(Integer id);

    List<Map<String, Object>> selectPublicFansList(Integer user_id, Integer schoolId, String name);

    User getUserInfoo(Integer user_id);

    Map<String, Object> getAuditBack(Integer user_id);

    String getToReplyName(int parseInt);

    UserPageRelationship getUserPageRelationship(Integer user_id);


}
