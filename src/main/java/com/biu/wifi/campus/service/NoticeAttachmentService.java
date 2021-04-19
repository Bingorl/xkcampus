package com.biu.wifi.campus.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biu.wifi.campus.dao.NoticeAttachmentMapper;
import com.biu.wifi.campus.dao.model.NoticeAttachment;
import com.biu.wifi.core.support.orm.mybatis3.util.IbatisServiceUtils;

@Service
public class NoticeAttachmentService {

    @Autowired
    private NoticeAttachmentMapper noticeAttachmentMapper;

    public void addNoticeAttachment(NoticeAttachment entity) {
        try {
            IbatisServiceUtils.insert(entity, noticeAttachmentMapper);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public NoticeAttachment getNoticeAttachment(NoticeAttachment entity) {
        return IbatisServiceUtils.get(entity, noticeAttachmentMapper);
    }

    public void updateNoticeAttachment(NoticeAttachment entity) {
        IbatisServiceUtils.updateByPk(entity, noticeAttachmentMapper);
    }

    public void deleteNoticeAttachment(NoticeAttachment entity) {
        try {
            IbatisServiceUtils.delete(entity, noticeAttachmentMapper);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public List<NoticeAttachment> findNoticeAttachmentList(NoticeAttachment entity) {
        return IbatisServiceUtils.find(entity, noticeAttachmentMapper, "create_time DESC");
    }
}
