package com.biu.wifi.campus.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biu.wifi.campus.dao.NoticeReceiveMapper;
import com.biu.wifi.campus.dao.model.NoticeReceive;
import com.biu.wifi.campus.dao.model.NoticeReceiveCriteria;
import com.biu.wifi.campus.dao.model.NoticeReceiveCriteria.Criteria;
import com.biu.wifi.core.support.orm.mybatis3.util.IbatisServiceUtils;

@Service
public class NoticeReceiveService {

    @Autowired
    private NoticeReceiveMapper noticeReceiveMapper;

    public void addNoticeReceive(NoticeReceive entity) {
        try {
            IbatisServiceUtils.insert(entity, noticeReceiveMapper);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public NoticeReceive getNoticeReceive(NoticeReceive entity) {
        return IbatisServiceUtils.get(entity, noticeReceiveMapper);
    }

    public void updateNoticeReceive(NoticeReceive entity) {
        IbatisServiceUtils.updateByPk(entity, noticeReceiveMapper);
    }

    public void deleteNoticeReceive(NoticeReceive entity) {
        try {
            IbatisServiceUtils.delete(entity, noticeReceiveMapper);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public List<NoticeReceive> findNoticeReceiveList(NoticeReceive entity) {
        return IbatisServiceUtils.find(entity, noticeReceiveMapper);
    }

    //删除用户通知收到表相关群组信息
    public void deleteAllByUser(Integer userId, Integer groupId) {
        NoticeReceive noticeReceive = new NoticeReceive();
        noticeReceive.setIsDelete((short) 1);
        noticeReceive.setDeleteTime(new Date());

        NoticeReceiveCriteria nc = new NoticeReceiveCriteria();
        Criteria cc = nc.createCriteria();
        if (groupId != null) {
            cc.andGroupIdEqualTo(groupId);
        }
        if (userId != null) {
            cc.andUserIdEqualTo(userId);
        }

        noticeReceiveMapper.updateByExampleSelective(noticeReceive, nc);
    }
}
