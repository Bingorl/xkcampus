package com.biu.wifi.campus.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biu.wifi.campus.dao.PostReplyMapper;
import com.biu.wifi.campus.dao.model.PostReply;
import com.biu.wifi.core.support.orm.mybatis3.util.IbatisServiceUtils;

@Service
public class PostReplyService {

    @Autowired
    private PostReplyMapper postReplyMapper;

    public int addPostReply(PostReply entity) {
        try {
            IbatisServiceUtils.insert(entity, postReplyMapper);
            return entity.getId();
        } catch (Exception e) {

            e.printStackTrace();
            return 0;
        }
    }

    public PostReply getPostReply(PostReply entity) {
        return IbatisServiceUtils.get(entity, postReplyMapper);
    }

    public void updatePostReply(PostReply entity) {
        IbatisServiceUtils.updateByPk(entity, postReplyMapper);
    }
}
