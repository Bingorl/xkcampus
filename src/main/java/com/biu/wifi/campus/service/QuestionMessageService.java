package com.biu.wifi.campus.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biu.wifi.campus.dao.QuestionMessageMapper;
import com.biu.wifi.campus.dao.model.QuestionMessage;
import com.biu.wifi.core.support.orm.mybatis3.util.IbatisServiceUtils;

@Service
public class QuestionMessageService {

    @Autowired
    private QuestionMessageMapper questionMessageMapper;

    public int addQuestionMessage(QuestionMessage entity) {
        try {
            IbatisServiceUtils.insert(entity, questionMessageMapper);
            return entity.getId();
        } catch (Exception e) {

            e.printStackTrace();
            return 0;
        }
    }
}
