package com.biu.wifi.campus.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biu.wifi.campus.dao.NoticeCalendarMapper;
import com.biu.wifi.campus.dao.model.NoticeCalendar;
import com.biu.wifi.core.support.orm.mybatis3.util.IbatisServiceUtils;

@Service
public class NoticeCalendarService {

    @Autowired
    private NoticeCalendarMapper noticeCalendarMapper;

    public void addNoticeCalendar(NoticeCalendar entity) {
        try {
            IbatisServiceUtils.insert(entity, noticeCalendarMapper);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public NoticeCalendar getNoticeCalendar(NoticeCalendar entity) {
        return IbatisServiceUtils.get(entity, noticeCalendarMapper);
    }

    public void updateNoticeCalendar(NoticeCalendar entity) {
        IbatisServiceUtils.updateByPk(entity, noticeCalendarMapper);
    }

    public void deleteNoticeCalendar(NoticeCalendar entity) {
        try {
            IbatisServiceUtils.delete(entity, noticeCalendarMapper);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public List<NoticeCalendar> findNoticeCalendarList(NoticeCalendar entity) {
        return IbatisServiceUtils.find(entity, noticeCalendarMapper);
    }
}
