package com.biu.wifi.campus.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biu.wifi.campus.dao.GroupMapper;
import com.biu.wifi.campus.dao.model.Group;
import com.biu.wifi.campus.daoEx.XiaoXinMapperEx;
import com.biu.wifi.core.support.orm.mybatis3.util.IbatisServiceUtils;

@Service
public class GroupService {

    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private XiaoXinMapperEx xiaoXinMapperEx;

    public Group getById(Integer id) {
        return groupMapper.selectByPrimaryKey(id);
    }

    public Group getGroup(Group entity) {
        return IbatisServiceUtils.get(entity, groupMapper);
    }

    public void insert(Group entity) {
        groupMapper.insertSelective(entity);
    }

    public void update(Group entity) {
        IbatisServiceUtils.updateByPk(entity, groupMapper);
    }

    //校信搜索群组列表
    public List<Map<String, Object>> findGroupUserFromXiaoXin(Integer userId, String search) {
        return xiaoXinMapperEx.findGroupUserFromXiaoXin(userId, search);
    }

    //获取群组列表
    public List<Map<String, Object>> user_findGroupList(Integer userId, String time) {
        return xiaoXinMapperEx.user_findGroupList(userId, time);
    }
}
