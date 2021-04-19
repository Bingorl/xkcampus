package com.biu.wifi.campus.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biu.wifi.campus.Tool.RandomUtil;
import com.biu.wifi.campus.dao.GroupMapper;
import com.biu.wifi.campus.dao.GroupMessageMapper;
import com.biu.wifi.campus.dao.GroupNoticeMapper;
import com.biu.wifi.campus.dao.GroupUserMapper;
import com.biu.wifi.campus.dao.NoticeAttachmentMapper;
import com.biu.wifi.campus.dao.QuestionReplyMapper;
import com.biu.wifi.campus.dao.model.Group;
import com.biu.wifi.campus.dao.model.GroupCriteria;
import com.biu.wifi.campus.dao.model.GroupMessage;
import com.biu.wifi.campus.dao.model.GroupNotice;
import com.biu.wifi.campus.dao.model.GroupUserCriteria;
import com.biu.wifi.campus.dao.model.QuestionReply;
import com.biu.wifi.campus.dao.model.GroupUserCriteria.Criteria;
import com.biu.wifi.campus.dao.model.NoticeAttachment;
import com.biu.wifi.campus.daoEx.BackendGroupMapperEx;
import com.biu.wifi.core.support.orm.mybatis3.util.IbatisServiceUtils;
import com.biu.wifi.dao.TCptDataInfoMapper;
import com.biu.wifi.model.TCptDataInfo;

@Service
public class BackendGroupService {

    @Autowired
    private GroupUserMapper groupUserMapper;

    @Autowired
    private BackendGroupMapperEx groupMapperEx;

    @Autowired
    private GroupNoticeMapper groupNoticeMapper;

    @Autowired
    private QuestionReplyMapper questionReplyMapper;

    @Autowired
    private TCptDataInfoMapper tCptDataInfoMapper;

    @Autowired
    private NoticeAttachmentMapper noticeAttachmentMapper;

    @Autowired
    private GroupMessageMapper groupMessageMapper;

    @Autowired
    private GroupMapper groupMapper;

    public int getManageGroupCount(Integer userId, List<Short> types) {
        GroupUserCriteria groupUserCriteria = new GroupUserCriteria();
        Criteria criteria = groupUserCriteria.createCriteria();
        criteria.andUserIdEqualTo(userId);
        criteria.andTypeIn(types);
        criteria.andIsDeleteEqualTo((short) 2);
        return groupUserMapper.countByExample(groupUserCriteria);
    }

    public List<Map<String, Object>> findManageGroupList(Integer userId, String groupName) {
        return groupMapperEx.findManageGroupList(userId, groupName);
    }

    public List<Map<String, Object>> findGroupList(Integer schoolId, String groupName, Long time) {
        return groupMapperEx.findGroupList(schoolId, groupName, time);
    }

    public List<Map<String, Object>> findGroupMembers(Integer groupId) {
        return groupMapperEx.findGroupMembers(groupId);
    }

    public List<Map<String, Object>> findGroupNotices(Integer groupId, String title, String content) {
        return groupMapperEx.findGroupNotices(groupId, title, content);
    }

    public List<Map<String, Object>> findNoticeQuestions(Integer noticeId, Long time) {
        return groupMapperEx.findNoticeQuestions(noticeId, time);
    }

    public List<Map<String, Object>> findQuestionReplys(Integer questionId) {
        return groupMapperEx.findQuestionReplys(questionId);
    }

    public List<Map<String, Object>> findNoticeReceives(Integer noticeId, Short type) {
        return groupMapperEx.findNoticeReceives(noticeId, type);
    }

    public void addGroupNotice(GroupNotice entity) {
        groupNoticeMapper.insertSelective(entity);
    }

    public void deleteGroupNotice(GroupNotice entity) {
        groupNoticeMapper.updateByPrimaryKeySelective(entity);
    }

    public void addQuestionReply(QuestionReply entity) {
        questionReplyMapper.insertSelective(entity);
    }

    public List<TCptDataInfo> findTcpList(TCptDataInfo entity) {
        return IbatisServiceUtils.find(entity, tCptDataInfoMapper);
    }

    public void addNoticeAttachment(NoticeAttachment entity) {
        noticeAttachmentMapper.insertSelective(entity);
    }

    public void addGroupMessage(GroupMessage entity) {
        groupMessageMapper.insertSelective(entity);
    }

    public Group getGroupById(Integer id) {
        return groupMapper.selectByPrimaryKey(id);
    }

    public int getGroupMessageCount(GroupMessage entity) {
        return IbatisServiceUtils.find(entity, groupMessageMapper).size();
    }

    public GroupNotice getGroupNotice(Integer id) {
        return groupNoticeMapper.selectByPrimaryKey(id);
    }

    public int addGroup(Group group) {
        return groupMapper.insertSelective(group);
    }

    /**
     * 校验一次性通知群组名
     *
     * @param groupName
     * @return
     */
    public String checkGroupName(String groupName) {
        GroupCriteria example = new GroupCriteria();
        example.createCriteria().andIsDeleteEqualTo((short) 2).andNameEqualTo(groupName);
        long count = groupMapper.countByExample(example);
        while (count > 0) {
            groupName = "临时群组[" + RandomUtil.randomNumber(0, 6) + "]";
            checkGroupName(groupName);
        }
        return groupName;
    }
}
