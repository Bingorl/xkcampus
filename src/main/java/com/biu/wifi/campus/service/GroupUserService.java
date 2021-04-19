package com.biu.wifi.campus.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biu.wifi.campus.dao.GroupUserMapper;
import com.biu.wifi.campus.dao.model.GroupUser;
import com.biu.wifi.campus.dao.model.GroupUserCriteria;
import com.biu.wifi.campus.daoEx.XiaoXinMapperEx;
import com.biu.wifi.core.support.orm.mybatis3.util.IbatisServiceUtils;

@Service
public class GroupUserService {

    @Autowired
    private GroupUserMapper groupUserMapper;

    @Autowired
    private XiaoXinMapperEx xiaoXinMapperEx;

    public GroupUser getGroupUser(GroupUser entity) {
        return IbatisServiceUtils.get(entity, groupUserMapper);
    }

    public List<GroupUser> findGroupUserList(GroupUser entity) {
        return IbatisServiceUtils.find(entity, groupUserMapper, "create_time DESC");
    }

    public int findCount(GroupUserCriteria entity) {
        return groupUserMapper.countByExample(entity);
    }

    public void insert(GroupUser entity) {
        groupUserMapper.insertSelective(entity);
    }

    public void update(GroupUser entity) {
        IbatisServiceUtils.updateByPk(entity, groupUserMapper);
    }

    //获取群分享界面管理员列表
    public List<Map<String, Object>> app_findShareGroupUserList(Integer group_id) {
        return xiaoXinMapperEx.app_findShareGroupUserList(group_id);
    }

    //获取前9位加入的成员列表
    public List<Map<String, Object>> findNineMemberList(Integer group_id) {
        return xiaoXinMapperEx.findNineMemberList(group_id);
    }

    //获取群成员列表
    public List<Map<String, Object>> user_findGroupMemberList(Integer groupId, String search) {
        return xiaoXinMapperEx.user_findGroupMemberList(groupId, search);
    }
}
