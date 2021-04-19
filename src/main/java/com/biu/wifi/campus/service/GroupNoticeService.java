package com.biu.wifi.campus.service;

import com.biu.wifi.campus.constant.NoticeType;
import com.biu.wifi.campus.dao.GroupNoticeMapper;
import com.biu.wifi.campus.dao.NoticeCalendarMapper;
import com.biu.wifi.campus.dao.NoticeReceiveMapper;
import com.biu.wifi.campus.dao.model.*;
import com.biu.wifi.campus.daoEx.GroupNoticeMapperEx;
import com.biu.wifi.core.support.orm.mybatis3.util.IbatisServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GroupNoticeService {

    @Autowired
    private GroupNoticeMapper groupNoticeMapper;

    @Autowired
    private GroupNoticeMapperEx groupNoticeMapperEx;
    @Autowired
    private NoticeReceiveMapper noticeReceiveMapper;
    @Autowired
    private NoticeCalendarMapper noticeCalendarMapper;

    public int addGroupNotice(GroupNotice entity) {
        try {
            IbatisServiceUtils.insert(entity, groupNoticeMapper);
            return entity.getId();
        } catch (Exception e) {

            e.printStackTrace();
            return 0;
        }
    }

    public GroupNotice getGroupNotice(GroupNotice entity) {
        return IbatisServiceUtils.get(entity, groupNoticeMapper);
    }

    public void updateGroupNotice(GroupNotice entity) {
        IbatisServiceUtils.updateByPk(entity, groupNoticeMapper);
    }

    public void deleteGroupNotice(GroupNotice entity) {
        try {
            IbatisServiceUtils.delete(entity, groupNoticeMapper);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public List<GroupNotice> findGroupNoticeList(GroupNotice entity) {
        return IbatisServiceUtils.find(entity, groupNoticeMapper);
    }

    //通知列表
    public List<Map<String, Object>> app_noticeList(String search_word, Integer type, String date, Integer group_id, String time, Integer user_id) {
        return groupNoticeMapperEx.noticeList(search_word, type, date, group_id, time, user_id);
    }

    //通知提问列表
    public List<Map<String, Object>> findNoticeQuestionList(Integer id, String time) {
        return groupNoticeMapperEx.findNoticeQuestionList(id, time);
    }

    //提问的回复列表
    public List<Map<String, Object>> findQuestionReplyList(Integer qid, String time) {
        return groupNoticeMapperEx.findQuestionReplyList(qid, time);
    }

    //通知收到详情,收到列表,未收到列表
    public List<Map<String, Object>> findReceivedList(Integer id, Short type) {
        return groupNoticeMapperEx.findReceivedList(id, type);
    }

    //群组搜索附件,根据附件名搜索
    public List<Map<String, Object>> findNoticeAttachList(String search_word, Integer group_id, String time, Integer user_id) {
        return groupNoticeMapperEx.findNoticeAttachList(search_word, group_id, time, user_id);
    }

    //获取日历事件列表
    public List<Map<String, Object>> findCalderNoticeList(Integer user_id, Integer type, String search_date, Integer order_type) {
        return groupNoticeMapperEx.findCalderNoticeList(user_id, type, search_date, order_type);
    }

    //获取本月日历是否添加
    public List<Map<String, Object>> findIsCalderList(Integer user_id, String search_date) {
        return groupNoticeMapperEx.findIsCalderList(user_id, search_date);
    }

    //未读通知数
    public int getNotReadNotice(Integer user_id) {
        return groupNoticeMapperEx.getNotReadNotice(user_id);
    }

    public Map<String, Object> findGroupNoticeMapById(Integer groupNoticeId, Integer userId) {
        GroupNotice groupNotice = groupNoticeMapper.selectByPrimaryKey(groupNoticeId);
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");

        Map map = new HashMap();
        map.put("noticeType", NoticeType.GROUP_NOTICE.getCode().shortValue());//消息通知
        map.put("id", groupNotice.getId());
        map.put("create_time", groupNotice.getCreateTime());
        map.put("title", groupNotice.getTitle());
        map.put("content", groupNotice.getContent());
        map.put("name", groupNotice.getUser());
        map.put("question_number", groupNotice.getQuestionCount());
        map.put("received_number", groupNotice.getReceiveCount());

        NoticeReceiveCriteria noticeReceiveEx = new NoticeReceiveCriteria();
        noticeReceiveEx.setOrderByClause("create_time desc");
        NoticeReceiveCriteria.Criteria criteria = noticeReceiveEx.createCriteria()
                .andNoticeIdEqualTo(groupNoticeId);
        long received_all = noticeReceiveMapper.countByExample(noticeReceiveEx);
        map.put("received_all", received_all);
        criteria.andIsDeleteEqualTo((short) 2)
                .andUserIdEqualTo(userId);
        List<NoticeReceive> noticeReceiveList = noticeReceiveMapper.selectByExample(noticeReceiveEx);
        if (userId != null) {
            // 1:已收到 2:未收到
            if (noticeReceiveList.size() > 0 && noticeReceiveList.get(0).getIsReceived().shortValue() == 1) {
                map.put("is_received", 1);
            } else {
                map.put("is_received", 2);
            }
            NoticeCalendarCriteria noticeCalendarEx = new NoticeCalendarCriteria();
            noticeCalendarEx.setOrderByClause("create_time desc");
            noticeCalendarEx.createCriteria()
                    .andIsDeleteEqualTo((short) 2)
                    .andUserIdEqualTo(userId)
                    .andNoticeIdEqualTo(groupNoticeId);
            List<NoticeCalendar> noticeCalendarList = noticeCalendarMapper.selectByExample(noticeCalendarEx);
            if (0 != noticeCalendarList.size()) {
                NoticeCalendar noticeCalendar = noticeCalendarList.get(0);
                map.put("is_calendar", 1);
                if (null != noticeCalendar.getRemindDate()) {
                    map.put("remind_date", sdf1.format(noticeCalendar.getRemindDate()));
                } else {
                    map.put("remind_date", "");
                }
                if (null != noticeCalendar.getRemindTime()) {
                    map.put("remind_time", sdf2.format(noticeCalendar.getRemindTime()));
                } else {
                    map.put("remind_time", "");
                }
                map.put("event_id", noticeCalendar.getId());
            } else {
                map.put("is_calendar", 2);
                map.put("remind_date", "");
                map.put("remind_time", "");
                map.put("event_id", "");
            }
        } else {
            //未登录
            map.put("is_received", 2);
            map.put("is_calendar", 2);
            map.put("remind_date", "");
            map.put("remind_time", "");
            map.put("event_id", "");
        }
        return map;
    }
}
