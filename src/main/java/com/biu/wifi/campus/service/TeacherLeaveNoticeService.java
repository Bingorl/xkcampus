package com.biu.wifi.campus.service;

import com.biu.wifi.campus.constant.NoticeType;
import com.biu.wifi.campus.dao.NoticeInfoMapper;
import com.biu.wifi.campus.dao.TeacherLeaveNoticeMapper;
import com.biu.wifi.campus.dao.model.NoticeInfo;
import com.biu.wifi.campus.dao.model.TeacherLeaveNotice;
import com.biu.wifi.campus.dao.model.TeacherLeaveNoticeExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 张彬.
 * @date 2021/4/4 1:15.
 */
@Service
public class TeacherLeaveNoticeService {

    @Autowired
    private TeacherLeaveNoticeMapper teacherLeaveNoticeMapper;
    @Autowired
    private NoticeInfoMapper noticeInfoMapper;

    /**
     * 添加通知
     *
     * @param leaveId
     * @param title
     * @param content
     * @param remark
     * @param toUserId
     */
    public TeacherLeaveNotice addLeaveNotice(Integer leaveId, String title, String content, String remark, Integer toUserId) {
        TeacherLeaveNotice leaveNotice = new TeacherLeaveNotice();
        leaveNotice.setLeaveId(leaveId);
        leaveNotice.setTitle(title);
        leaveNotice.setContent(content);
        leaveNotice.setRemark(remark);
        leaveNotice.setToUserId(toUserId);
        leaveNotice.setCreateTime(new Date());
        teacherLeaveNoticeMapper.insertSelective(leaveNotice);
        //插入通知汇总表
        NoticeInfo noticeInfo = new NoticeInfo();
        noticeInfo.setBizId(leaveNotice.getId());
        noticeInfo.setUserId(toUserId);
        noticeInfo.setType(NoticeType.TEACHER_LEAVE_NOTICE.getCode().shortValue());
        noticeInfo.setIsDelete((short) 2);
        noticeInfoMapper.insertSelective(noticeInfo);
        return leaveNotice;
    }

    /**
     * 根据业务id查询通知
     *
     * @param bizId
     * @return
     * @author 张彬.
     * @date 2021/4/16 15:17.
     */
    public Map<String, Object> findLeaveNoticeMapById(Integer bizId) {
        TeacherLeaveNotice leaveNotice = teacherLeaveNoticeMapper.selectByPrimaryKey(bizId);
        Map<String, Object> map = new HashMap();
        map.put("id", leaveNotice.getId());
        map.put("noticeType", NoticeType.TEACHER_LEAVE_NOTICE.getCode());//请假审批通知
        map.put("name", "请假");
        map.put("title", leaveNotice.getTitle());
        map.put("content", leaveNotice.getContent());
        map.put("is_received", leaveNotice.getIsConfirm() == 1 ? 1 : 2);
        map.put("create_time", leaveNotice.getCreateTime());
        map.put("question_number", 0);
        map.put("received_all", 0);
        map.put("received_number", 0);
        map.put("is_calendar", "2");
        map.put("remind_date", "");
        map.put("remind_time", "");
        map.put("event_id", "");
        map.put("isRepeatApply", null);
        map.put("leaveId", leaveNotice.getLeaveId());
        return map;
    }

    public void confirmNotice(Integer leaveId, Integer userId) {
        TeacherLeaveNoticeExample example = new TeacherLeaveNoticeExample();
        example.createCriteria()
                .andLeaveIdEqualTo(leaveId)
                .andToUserIdEqualTo(userId)
                .andIsDeleteEqualTo((short) 2);
        List<TeacherLeaveNotice> list = teacherLeaveNoticeMapper.selectByExample(example);
        if (!list.isEmpty()) {
            TeacherLeaveNotice notice = list.get(0);
            notice.setIsConfirm((short) 1);
            notice.setConfirmTime(new Date());
            teacherLeaveNoticeMapper.updateByPrimaryKeySelective(notice);
        }
    }
}
