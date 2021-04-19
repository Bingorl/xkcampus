package com.biu.wifi.campus.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biu.wifi.campus.dao.GroupMessageMapper;
import com.biu.wifi.campus.dao.model.GroupMessage;
import com.biu.wifi.campus.dao.model.GroupMessageCriteria;
import com.biu.wifi.campus.dao.model.GroupMessageCriteria.Criteria;
import com.biu.wifi.campus.daoEx.XiaoXinMapperEx;
import com.biu.wifi.core.support.orm.mybatis3.util.IbatisServiceUtils;

@Service
public class GroupMessageService {

    @Autowired
    private GroupMessageMapper groupMessageMapper;

    @Autowired
    private XiaoXinMapperEx xiaoXinMapperEx;

    public List<GroupMessage> findList(GroupMessage entity) {
        return IbatisServiceUtils.find(entity, groupMessageMapper, "create_time DESC");
    }

    public long findCount(GroupMessageCriteria entity) {
        return groupMessageMapper.countByExample(entity);
    }

    public GroupMessage getById(Integer id) {
        return groupMessageMapper.selectByPrimaryKey(id);
    }

    public void insert(GroupMessage entity) {
        groupMessageMapper.insertSelective(entity);
    }

    public void update(GroupMessage entity) {
        IbatisServiceUtils.updateByPk(entity, groupMessageMapper);
    }

    //批量新增消息------申请加群
    public void insertAllApplyAdd(Integer groupId, Integer userId, String user, List<Map<String, Object>> groupManagerList) {
        xiaoXinMapperEx.insertAllApplyAdd(groupId, userId, user, groupManagerList);
    }

    //批量新增消息------退出群组
    public void insertAllOutGroup(Integer groupId, Integer userId, String user, List<Map<String, Object>> groupManagerList) {
        xiaoXinMapperEx.insertAllOutGroup(groupId, userId, user, groupManagerList);
    }

    //获取群通知列表
    public List<Map<String, Object>> user_findGroupMsgList(Integer userId, String time) {
        return xiaoXinMapperEx.user_findGroupMsgList(userId, time);
    }

    //将未读消息设为已读
    public void updateIsRead(Integer userId) {
        GroupMessage entity = new GroupMessage();
        entity.setIsRead((short) 1);

        GroupMessageCriteria gmc = new GroupMessageCriteria();
        Criteria cc = gmc.createCriteria();
        cc.andReceiverIdEqualTo(userId);
        cc.andIsReadEqualTo((short) 2);
        cc.andIsDeteleEqualTo((short) 2);

        groupMessageMapper.updateByExampleSelective(entity, gmc);
    }

    //清空群通知
    public void user_cleanGroupMsg(Integer userId) {
        GroupMessage entity = new GroupMessage();
        entity.setIsDetele((short) 1);
        entity.setDeleteTime(new Date());

        GroupMessageCriteria gmc = new GroupMessageCriteria();
        Criteria cc = gmc.createCriteria();
        cc.andReceiverIdEqualTo(userId);
        cc.andIsDeteleEqualTo((short) 2);

        groupMessageMapper.updateByExampleSelective(entity, gmc);
    }

    //批量新增群通知消息------解散群组
    public void insertAllDeleteGroup(Integer groupId, Integer userId, String user, List<Map<String, Object>> groupUserList) {
        xiaoXinMapperEx.insertAllDeleteGroup(groupId, userId, user, groupUserList);
    }

    /**
     * 是否可以发送邀请通知
     *
     * @param receiverId 接收人ID
     * @param groupId    群ID
     * @return
     */
    public boolean allowedSendGroupInviteMsg(Integer receiverId, Integer groupId) {
        GroupMessageCriteria example = new GroupMessageCriteria();
        example.createCriteria()
                .andIsDeteleEqualTo((short) 2)
                .andTypeEqualTo((short) 2)//邀请加入
                .andGroupIdEqualTo(groupId)
                .andReceiverIdEqualTo(receiverId)
                .andStatusEqualTo((short) 1);//邀请中
        long count = groupMessageMapper.countByExample(example);

        if (count > 0) {
            return false;
        } else {
            return true;
        }
    }
}
