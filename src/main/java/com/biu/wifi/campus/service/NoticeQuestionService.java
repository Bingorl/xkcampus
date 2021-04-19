package com.biu.wifi.campus.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biu.wifi.campus.dao.NoticeQuestionMapper;
import com.biu.wifi.campus.dao.model.NoticeQuestion;
import com.biu.wifi.core.support.orm.mybatis3.util.IbatisServiceUtils;

@Service
public class NoticeQuestionService {

    @Autowired
    private NoticeQuestionMapper noticeQuestionMapper;

    public int addNoticeQuestion(NoticeQuestion entity) {
        try {
            IbatisServiceUtils.insert(entity, noticeQuestionMapper);
            return entity.getId();
        } catch (Exception e) {

            e.printStackTrace();
            return 0;
        }
    }

    public NoticeQuestion getNoticeQuestion(NoticeQuestion entity) {
        return IbatisServiceUtils.get(entity, noticeQuestionMapper);
    }

    public int updateNoticeQuestion(NoticeQuestion entity) {
        return IbatisServiceUtils.updateByPk(entity, noticeQuestionMapper);
    }
}
