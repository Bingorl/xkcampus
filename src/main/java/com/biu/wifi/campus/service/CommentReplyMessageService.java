package com.biu.wifi.campus.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biu.wifi.campus.dao.CommentReplyMessageMapper;
import com.biu.wifi.campus.dao.model.CommentReplyMessage;

@Service
public class CommentReplyMessageService {

    @Autowired
    private CommentReplyMessageMapper commentReplyMessageMapper;

    public int addCommentReplyMessage(CommentReplyMessage entity) {
        try {
            commentReplyMessageMapper.insertSelective(entity);
            return entity.getId();
        } catch (Exception e) {

            e.printStackTrace();
            return 0;
        }
    }
}
