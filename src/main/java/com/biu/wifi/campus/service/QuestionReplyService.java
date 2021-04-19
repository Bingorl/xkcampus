package com.biu.wifi.campus.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biu.wifi.campus.dao.QuestionReplyMapper;
import com.biu.wifi.campus.dao.model.QuestionReply;
import com.biu.wifi.core.support.orm.mybatis3.util.IbatisServiceUtils;

@Service
public class QuestionReplyService {

    @Autowired
    private QuestionReplyMapper questionReplyMapper;

    public int addQuestionReply(QuestionReply entity) {
        try {
            IbatisServiceUtils.insert(entity, questionReplyMapper);
            return entity.getId();
        } catch (Exception e) {

            e.printStackTrace();
            return 0;
        }
    }

    public QuestionReply getQuestionReply(QuestionReply entity) {
        return IbatisServiceUtils.get(entity, questionReplyMapper);
    }

    public void updateQuestionReply(QuestionReply entity) {
        IbatisServiceUtils.updateByPk(entity, questionReplyMapper);
    }

    public List<QuestionReply> findQuestionReplyList(QuestionReply entity) {
        return IbatisServiceUtils.find(entity, questionReplyMapper);
    }
}
