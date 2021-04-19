package com.biu.wifi.campus.service;


import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.biu.wifi.campus.dao.LeaveMessageMapper;

import com.biu.wifi.campus.dao.PageApplyMapper;
import com.biu.wifi.campus.dao.UserMapper;
import com.biu.wifi.campus.dao.UserPageRelationshipMapper;

import com.biu.wifi.campus.dao.model.LeaveMessage;

import com.biu.wifi.campus.dao.model.PageApply;
import com.biu.wifi.campus.dao.model.User;
import com.biu.wifi.campus.dao.model.UserPageRelationship;

import com.biu.wifi.campus.daoEx.AppPublicPageMapperEz;
import com.biu.wifi.core.support.orm.mybatis3.util.IbatisServiceUtils;

@Service
public class AppPublicPageService {

    @Autowired
    private AppPublicPageMapperEz appPublicPageMapperEz;
    @Autowired
    private PageApplyMapper pageApplyMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserPageRelationshipMapper userPageRelationshipMapper;
    @Autowired
    private LeaveMessageMapper leaveMessageMapper;

    public List<PageApply> getPageApply(PageApply pageApp) {
        return IbatisServiceUtils.find(pageApp, pageApplyMapper);
    }

    public List<User> getUserList(User user) {
        return IbatisServiceUtils.find(user, userMapper);
    }

    public User getUser(User user) {
        return IbatisServiceUtils.get(user, userMapper);
    }

    public void updateLeaveMessage(LeaveMessage leaveMessage) {
        leaveMessageMapper.updateByPrimaryKeySelective(leaveMessage);
    }

    public LeaveMessage getLeaveMessage(LeaveMessage leaveMessage) {
        return IbatisServiceUtils.get(leaveMessage, leaveMessageMapper);

    }

    public int AddLeaveMessage(LeaveMessage leaveMessage) throws Exception {
        return IbatisServiceUtils.insert(leaveMessage, leaveMessageMapper);
    }

    public int addPageApply(PageApply pageApply) {
        return pageApplyMapper.insert(pageApply);
    }

    public Map<String, Object> getAudit(Integer user_id) {
        return appPublicPageMapperEz.getAudit(user_id);
    }

    public User getUserPublic(Integer user_id) {
        return appPublicPageMapperEz.getUserPublic(user_id);
    }


    public int updateUserPage(User u) {
        return userMapper.updateByPrimaryKeySelective(u);
    }

    public List<Map<String, Object>> getMessageBoardList(Integer pageId) {
        return appPublicPageMapperEz.getMessageBoardList(pageId);
    }

    public List<Map<String, Object>> getPublicFansList(Integer id) {
        return appPublicPageMapperEz.getPublicFansList(id);
    }

    public List<Map<String, Object>> getPublicSameFansList(Integer id, Integer schoolId, String name) {
        return appPublicPageMapperEz.getPublicSameFansList(id, schoolId, name);
    }


    public List<UserPageRelationship> getNumOfManage(UserPageRelationship userPageRelationship) {
        return IbatisServiceUtils.find(userPageRelationship, userPageRelationshipMapper);
    }

    public int addUserPageRelationship(UserPageRelationship userPage) {
        return userPageRelationshipMapper.insert(userPage);
    }

    public List<Map<String, Object>> getPublicManage(Integer id) {
        return appPublicPageMapperEz.getPublicManage(id);
    }

    public int updateUserPageRelationship(UserPageRelationship userPage) {
        return userPageRelationshipMapper.updateByPrimaryKeySelective(userPage);
    }

    public List<LeaveMessage> getReleaveMessage(Integer id, Integer user_id) {
        return appPublicPageMapperEz.getReleaveMessage(id, user_id);
    }

    public User getUserInfo(Integer user_id) {
        return appPublicPageMapperEz.getUserInfo(user_id);
    }

    public Integer getPageId(Integer user_id) {
        return appPublicPageMapperEz.getPageId(user_id);
    }

    public List<Integer> getAdminId(Integer id) {
        return appPublicPageMapperEz.getAdminId(id);
    }

    public List<Map<String, Object>> selectPublicFansList(Integer user_id, Integer schoolId, String name) {
        return appPublicPageMapperEz.selectPublicFansList(user_id, schoolId, name);
    }

    public User getUserInfoo(Integer user_id) {
        return appPublicPageMapperEz.getUserInfoo(user_id);
    }

    public Map<String, Object> getAuditBack(Integer user_id) {
        return appPublicPageMapperEz.getAuditBack(user_id);
    }

    public String getToReplyName(int parseInt) {
        return appPublicPageMapperEz.getToReplyName(parseInt);
    }

    public UserPageRelationship getUserPageRelationship(Integer user_id) {
        return appPublicPageMapperEz.getUserPageRelationship(user_id);
    }


}
