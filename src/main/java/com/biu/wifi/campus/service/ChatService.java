package com.biu.wifi.campus.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biu.wifi.campus.dao.ChatMapper;
import com.biu.wifi.campus.dao.model.Chat;
import com.biu.wifi.campus.daoEx.XiaoXinMapperEx;
import com.biu.wifi.core.support.orm.mybatis3.util.IbatisServiceUtils;

@Service
public class ChatService {

    @Autowired
    private ChatMapper chatMapper;

    @Autowired
    private XiaoXinMapperEx xiaoXinMapperEx;

    public Chat get(Chat entity) {
        return IbatisServiceUtils.get(entity, chatMapper);
    }

    public void insert(Chat entity) {
        chatMapper.insertSelective(entity);
    }

    public void update(Chat entity) {
        IbatisServiceUtils.updateByPk(entity, chatMapper);
    }

    //校信搜索联系人列表(私信过的人)
    public List<Map<String, Object>> findChatUserFromXiaoXin(Integer userId, String search) {
        return xiaoXinMapperEx.findChatUserFromXiaoXin(userId, search);
    }

    //获取私信列表  isFollow 是否关注  1是2否
    public List<Map<String, Object>> user_findChatList(Integer userId, Short isFollow, String time) {
        return xiaoXinMapperEx.user_findChatList(userId, isFollow, time);
    }

}
