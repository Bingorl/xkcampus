package com.biu.wifi.campus.daoEx;

import com.biu.wifi.campus.dao.model.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface AppPersonalityMapperEz {

    public Map<String, Object> getMyOwnOrPublicPage(@Param("userId") Integer userId);

    public Map<String, Object> getMyOwnOrPublicPagee(@Param("userId") Integer userId);

    public void editInfo(@Param("userId") Integer userId, @Param("headimg") String headimg, @Param("area") String area, @Param("signature") String signature);

    public Map<String, Object> getMyOwnInfo(Integer userId);

    public Map<String, Object> getMyOwnInfoo(Integer userId);

    public List<Map<String, Object>> getMyOwnOrPublicDetails(@Param("userId") Integer userId);

    public List<Map<String, Object>> getOtherPublicDetails(@Param("userId") Integer userId, @Param("time") String time, @Param("is_friend") Short is_friend);

    public List<Map<String, Object>> likeSayingMap(Integer userId);

    public List<Map<String, Object>> commentSayingMap(Integer userId);

    public List<Map<String, Object>> replySayingMap(Integer userId, Integer commentId);

    public List<Map<String, Object>> getReleasePostList(Integer userId);

    public List<Map<String, Object>> getMyFocusOn(Integer userId);

    public List<Map<String, Object>> getMyFans(Integer userId);

    public List<Map<String, Object>> getMyCollect(Integer userId);

    public void cancelMyCollect(Integer userId, Integer collectId, Date time);

    public List<Map<String, Object>> getMessageList(Integer user_id, String time);

    public List<Map<String, Object>> getAtMessageList(Integer user_id, String time);

    public List<Map<String, Object>> getZanMessageList(Integer user_id, String time);

    public List<Map<String, Object>> getQuestionMessList(Integer user_id, String time);

    public List<Map<String, Object>> getHelpList();

    public Map<String, Object> getHelpDetail(Integer helpId);

    public User getPublicPage(Integer user_id);

    public Map<String, Object> selectPush(Integer user_id);

    public List<Map<String, Object>> findCommentAndReplyList(Integer id);

    public Map<String, Object> getPostInfo(Integer post_id);

    public Map<String, Object> getSayingInfo(Integer post_id);

    public int updateIsRead(Integer id);

    public int updateIsReadAT(Integer id);

    public int updateIsReadLike(Integer id);

    public int updateIsReadquestion(Integer id);

    public Short selectPostComm(Integer id);

    public Short selectPostReply(Integer id);

    public Short selectSayingComm(Integer id);

    public Short selectSayingReply(Integer id);

    public Short selectLeaveMessage(Integer id);

    public Short getPostStatus(Integer objectId);

    public Short getCommentStatus(Integer objectId);

    public Short getCommentReplyStatus(Integer objectId);

    public Short getSayingStatus(Integer objectId);

    public Short getNoticeStatus(Integer objectId);

    public List<Integer> getNoticeId(Integer objectId);

    public Map<String, Object> selectPostCommen(Integer id);

    public Map<String, Object> selectPostRe(Integer id);

    public Map<String, Object> selectSayingCommen(Integer id);

    public Map<String, Object> selectSayingRe(Integer id);

    public Map<String, Object> selectLeaveMess(Integer id);

    public Map<String, Object> getPostSta(Integer objectId);

    public Map<String, Object> getCommentSta(Integer objectId);

    public Map<String, Object> getCommentReplySta(Integer objectId);

    public Map<String, Object> getSayingSta(Integer objectId);

    public Integer getPostComment(int parseInt);

    public List<Map<String, Object>> getMyPublicDetails(@Param("userId") Integer userId, @Param("time") String time);


}
