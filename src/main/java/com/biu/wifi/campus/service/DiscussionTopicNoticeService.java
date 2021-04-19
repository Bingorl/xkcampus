package com.biu.wifi.campus.service;

import com.biu.wifi.campus.constant.NoticeType;
import com.biu.wifi.campus.dao.DiscussionTopicNoticeMapper;
import com.biu.wifi.campus.dao.NoticeInfoMapper;
import com.biu.wifi.campus.dao.model.DiscussionTopicNotice;
import com.biu.wifi.campus.dao.model.DiscussionTopicNoticeExample;
import com.biu.wifi.campus.dao.model.NoticeInfo;
import com.biu.wifi.campus.exception.BizException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 张彬.
 * @date 2021/4/4 22:33.
 */
@Service
public class DiscussionTopicNoticeService {

    @Autowired
    private DiscussionTopicNoticeMapper discussionTopicNoticeMapper;
    @Autowired
    private NoticeInfoMapper noticeInfoMapper;

    @Transactional(rollbackFor = BizException.class)
    public DiscussionTopicNotice add(DiscussionTopicNotice req) {
        req.setIsConfirm((short) 2);
        req.setIsRead((short) 2);
        req.setIsDelete((short) 2);
        req.setCreateTime(new Date());
        boolean addDiscussionNotice = discussionTopicNoticeMapper.insertSelective(req) > 0;

        NoticeInfo noticeInfo = new NoticeInfo();
        noticeInfo.setBizId(req.getApplyId());
        noticeInfo.setType(NoticeType.DISCUSSION_TOPIC_APPLY_NOTICE.getCode().shortValue());
        noticeInfo.setUserId(req.getToUserId());
        noticeInfo.setIsDelete((short) 2);
        boolean addNotice = noticeInfoMapper.insertSelective(noticeInfo) > 0;

        return req;
    }

    public Map<String, Object> findNoticeMapById(Integer bizId) {
        DiscussionTopicNotice notice = discussionTopicNoticeMapper.selectByPrimaryKey(bizId);
        Map<String, Object> map = new HashMap();
        map.put("id", notice.getId());
        map.put("noticeType", NoticeType.DISCUSSION_TOPIC_APPLY_NOTICE.getCode());//请假审批通知
        map.put("name", "会议议题审核");
        map.put("title", notice.getTitle());
        map.put("content", notice.getContent());
        map.put("is_received", notice.getIsConfirm() == 1 ? 1 : 2);
        map.put("create_time", notice.getCreateTime());
        map.put("question_number", 0);
        map.put("received_all", 0);
        map.put("received_number", 0);
        map.put("is_calendar", "2");
        map.put("remind_date", "");
        map.put("remind_time", "");
        map.put("event_id", "");
        map.put("isRepeatApply", null);
        map.put("applyId", notice.getApplyId());
        return map;
    }

    public void confirmNotice(Integer applyId, Integer userId) {
        DiscussionTopicNoticeExample example = new DiscussionTopicNoticeExample();
        example.createCriteria()
                .andApplyIdEqualTo(applyId)
                .andToUserIdEqualTo(userId)
                .andIsDeleteEqualTo((short) 2);
        List<DiscussionTopicNotice> list = discussionTopicNoticeMapper.selectByExample(example);
        if (!list.isEmpty()) {
            DiscussionTopicNotice notice = list.get(0);
            notice.setIsConfirm((short) 1);
            notice.setConfirmTime(new Date());
            discussionTopicNoticeMapper.updateByPrimaryKeySelective(notice);
        }
    }
}
