package com.biu.wifi.campus.service;

import java.util.Date;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biu.wifi.campus.dao.AtMessageMapper;
import com.biu.wifi.campus.dao.CommentReplyMessageMapper;
import com.biu.wifi.campus.dao.FeedBackMapper;
import com.biu.wifi.campus.dao.FollowMapper;
import com.biu.wifi.campus.dao.LikeMessageMapper;
import com.biu.wifi.campus.dao.PostLikeMapper;
import com.biu.wifi.campus.dao.QuestionMessageMapper;
import com.biu.wifi.campus.dao.SayingLikeMapper;
import com.biu.wifi.campus.dao.SayingMapper;
import com.biu.wifi.campus.dao.UserPageRelationshipMapper;
import com.biu.wifi.campus.dao.model.AtMessage;
import com.biu.wifi.campus.dao.model.CommentReplyMessage;
import com.biu.wifi.campus.dao.model.FeedBack;
import com.biu.wifi.campus.dao.model.Follow;
import com.biu.wifi.campus.dao.model.LikeMessage;
import com.biu.wifi.campus.dao.model.PostLike;
import com.biu.wifi.campus.dao.model.QuestionMessage;
import com.biu.wifi.campus.dao.model.Saying;
import com.biu.wifi.campus.dao.model.SayingLike;
import com.biu.wifi.campus.dao.model.User;
import com.biu.wifi.campus.dao.model.UserPageRelationship;
import com.biu.wifi.campus.daoEx.AppPersonalityMapperEz;
import com.biu.wifi.core.support.orm.mybatis3.util.IbatisServiceUtils;

@Service
public class AppPersonalityService {

    @Autowired
    private AppPersonalityMapperEz appPersonalityMapperEz;

    @Autowired
    private UserPageRelationshipMapper userPageRelationshipMapper;
    @Autowired
    private AtMessageMapper atMessageMapper;
    @Autowired
    private LikeMessageMapper likeMessageMapper;
    @Autowired
    private FeedBackMapper feedBackMapper;
    @Autowired
    private FollowMapper followMapper;
    @Autowired
    private PostLikeMapper postLikeMapper;
    @Autowired
    private QuestionMessageMapper questionMessageMapper;
    @Autowired
    private SayingLikeMapper sayingLikeMapper;
    @Autowired
    private CommentReplyMessageMapper commentReplyMessageMapper;
    @Autowired
    private SayingMapper sayingMapper;

    public List<Saying> getSayingList(Saying s) {
        return IbatisServiceUtils.find(s, sayingMapper);
    }


    public List<CommentReplyMessage> selectMessageReplyList(CommentReplyMessage crm) {
        return IbatisServiceUtils.find(crm, commentReplyMessageMapper);
    }

    public List<SayingLike> findSayingList(SayingLike entity) {
        return IbatisServiceUtils.find(entity, sayingLikeMapper, "create_time DESC");
    }

    public List<QuestionMessage> getQuestion(QuestionMessage questionMessage) {
        return IbatisServiceUtils.find(questionMessage, questionMessageMapper);
    }

    public List<PostLike> getPostLike(PostLike postLike) {
        return IbatisServiceUtils.find(postLike, postLikeMapper);
    }

    public List<Follow> getFollow(Follow follow) {
        return IbatisServiceUtils.find(follow, followMapper);
    }

    public int addFeedback(FeedBack feedBack) {
        return feedBackMapper.insert(feedBack);
    }

    public List<LikeMessage> getLikeMessageList(LikeMessage likeMessage) {
        return IbatisServiceUtils.find(likeMessage, likeMessageMapper);
    }

    public List<AtMessage> getAtMessageList(AtMessage atMessage) {
        return IbatisServiceUtils.find(atMessage, atMessageMapper);
    }

    public UserPageRelationship getUserPageRelation(UserPageRelationship userPageRelationship) {
        return IbatisServiceUtils.get(userPageRelationship, userPageRelationshipMapper);
    }

    public Map<String, Object> getMyOwnOrPublicPage(Integer userId) {
        return appPersonalityMapperEz.getMyOwnOrPublicPage(userId);
    }

    public Map<String, Object> getMyOwnOrPublicPagee(Integer userId) {
        return appPersonalityMapperEz.getMyOwnOrPublicPagee(userId);
    }


    public void editInfo(Integer userId, String headimg, String area, String signature) {
        appPersonalityMapperEz.editInfo(userId, headimg, area, signature);
    }

    public Map<String, Object> getMyOwnInfo(Integer userId) {
        return appPersonalityMapperEz.getMyOwnInfo(userId);
    }

    public Map<String, Object> getMyOwnInfoo(Integer userId) {
        return appPersonalityMapperEz.getMyOwnInfoo(userId);
    }


    public List<Map<String, Object>> getMyOwnOrPublicDetails(Integer userId) {
        return appPersonalityMapperEz.getMyOwnOrPublicDetails(userId);
    }

    public List<Map<String, Object>> getOtherPublicDetails(Integer userId, String time, Short is_friend) {
        return appPersonalityMapperEz.getOtherPublicDetails(userId, time, is_friend);
    }


    public List<Map<String, Object>> likeSayingMap(Integer userId) {

        return appPersonalityMapperEz.likeSayingMap(userId);
    }

    public List<Map<String, Object>> commentSayingMap(Integer userId) {

        return appPersonalityMapperEz.commentSayingMap(userId);
    }

    public List<Map<String, Object>> replySayingMap(Integer userId, Integer commentId) {

        return appPersonalityMapperEz.replySayingMap(userId, commentId);
    }

    public List<Map<String, Object>> getReleasePostList(Integer userId) {
        return appPersonalityMapperEz.getReleasePostList(userId);
    }

    public List<Map<String, Object>> getMyFocusOn(Integer userId) {
        return appPersonalityMapperEz.getMyFocusOn(userId);
    }

    public List<Map<String, Object>> getMyFans(Integer userId) {
        return appPersonalityMapperEz.getMyFans(userId);
    }

    public List<Map<String, Object>> getMyCollect(Integer userId) {
        return appPersonalityMapperEz.getMyCollect(userId);
    }

    public void cancelMyCollect(Integer userId, Integer collectId, Date time) {
        appPersonalityMapperEz.cancelMyCollect(userId, collectId, time);

    }

    public List<Map<String, Object>> getMessageList(Integer user_id, String time) {
        return appPersonalityMapperEz.getMessageList(user_id, time);
    }

    public List<Map<String, Object>> getAtMessageList(Integer user_id, String time) {
        return appPersonalityMapperEz.getAtMessageList(user_id, time);
    }

    public List<Map<String, Object>> getZanMessageList(Integer user_id, String time) {
        return appPersonalityMapperEz.getZanMessageList(user_id, time);
    }

    public List<Map<String, Object>> getQuestionMessList(Integer user_id, String time) {
        return appPersonalityMapperEz.getQuestionMessList(user_id, time);
    }

    public List<Map<String, Object>> getHelpList() {
        return appPersonalityMapperEz.getHelpList();
    }

    public Map<String, Object> getHelpDetail(Integer helpId) {
        return appPersonalityMapperEz.getHelpDetail(helpId);
    }

    public User getPublicPage(Integer user_id) {
        return appPersonalityMapperEz.getPublicPage(user_id);
    }

    public Map<String, Object> selectPush(Integer user_id) {
        return appPersonalityMapperEz.selectPush(user_id);
    }

    public List<Map<String, Object>> findCommentAndReplyList(Integer id) {
        return appPersonalityMapperEz.findCommentAndReplyList(id);
    }

    public Map<String, Object> getPostInfo(Integer post_id) {
        return appPersonalityMapperEz.getPostInfo(post_id);
    }

    public Map<String, Object> getSayingInfo(Integer post_id) {
        return appPersonalityMapperEz.getSayingInfo(post_id);
    }

    public int updateIsRead(Integer id) {
        return appPersonalityMapperEz.updateIsRead(id);
    }

    public int updateIsReadAT(Integer id) {
        return appPersonalityMapperEz.updateIsReadAT(id);
    }

    public int updateIsReadLike(Integer id) {
        return appPersonalityMapperEz.updateIsReadLike(id);
    }

    public int updateIsReadquestion(Integer id) {
        return appPersonalityMapperEz.updateIsReadquestion(id);
    }

    public Short selectPostComm(Integer id) {
        return appPersonalityMapperEz.selectPostComm(id);
    }

    public Short selectPostReply(Integer id) {
        return appPersonalityMapperEz.selectPostReply(id);
    }

    public Short selectSayingComm(Integer id) {
        return appPersonalityMapperEz.selectSayingComm(id);
    }

    public Short selectSayingReply(Integer id) {
        return appPersonalityMapperEz.selectSayingReply(id);
    }

    public Short selectLeaveMessage(Integer id) {
        return appPersonalityMapperEz.selectLeaveMessage(id);
    }

    public Short getPostStatus(Integer objectId) {
        return appPersonalityMapperEz.getPostStatus(objectId);
    }

    public Short getCommentStatus(Integer objectId) {
        return appPersonalityMapperEz.getCommentStatus(objectId);
    }

    public Short getCommentReplyStatus(Integer objectId) {
        return appPersonalityMapperEz.getCommentReplyStatus(objectId);
    }

    public Short getSayingStatus(Integer objectId) {
        return appPersonalityMapperEz.getSayingStatus(objectId);
    }

    public Short getNoticeStatus(Integer objectId) {
        return appPersonalityMapperEz.getNoticeStatus(objectId);
    }

    public List<Integer> getNoticeId(Integer objectId) {
        return appPersonalityMapperEz.getNoticeId(objectId);
    }

    public Map<String, Object> selectPostCommen(Integer id) {
        return appPersonalityMapperEz.selectPostCommen(id);
    }

    public Map<String, Object> selectPostRe(Integer id) {
        return appPersonalityMapperEz.selectPostRe(id);
    }

    public Map<String, Object> selectSayingCommen(Integer id) {
        return appPersonalityMapperEz.selectSayingCommen(id);
    }

    public Map<String, Object> selectSayingRe(Integer id) {
        return appPersonalityMapperEz.selectSayingRe(id);
    }

    public Map<String, Object> selectLeaveMess(Integer id) {
        return appPersonalityMapperEz.selectLeaveMess(id);
    }

    public Map<String, Object> getPostSta(Integer objectId) {
        return appPersonalityMapperEz.getPostSta(objectId);
    }

    public Map<String, Object> getCommentSta(Integer objectId) {
        return appPersonalityMapperEz.getCommentSta(objectId);
    }

    public Map<String, Object> getCommentReplySta(Integer objectId) {
        return appPersonalityMapperEz.getCommentReplySta(objectId);
    }

    public Map<String, Object> getSayingSta(Integer objectId) {
        return appPersonalityMapperEz.getSayingSta(objectId);
    }

    public Integer getPostComment(int parseInt) {
        return appPersonalityMapperEz.getPostComment(parseInt);
    }

    public List<Map<String, Object>> getMyPublicDetails(Integer userId, String time) {
        return appPersonalityMapperEz.getMyPublicDetails(userId, time);
    }


}
