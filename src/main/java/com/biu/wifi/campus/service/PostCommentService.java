package com.biu.wifi.campus.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biu.wifi.campus.dao.PostCommentMapper;
import com.biu.wifi.campus.dao.model.PostComment;
import com.biu.wifi.core.support.orm.mybatis3.util.IbatisServiceUtils;

@Service
public class PostCommentService {

    @Autowired
    private PostCommentMapper postCommentMapper;

    public int addPostComment(PostComment entity) {
        try {
            IbatisServiceUtils.insert(entity, postCommentMapper);
            return entity.getId();
        } catch (Exception e) {

            e.printStackTrace();
            return 0;
        }
    }

    public PostComment getPostComment(PostComment entity) {
        return IbatisServiceUtils.get(entity, postCommentMapper);
    }

    public void updatePostComment(PostComment entity) {
        IbatisServiceUtils.updateByPk(entity, postCommentMapper);
    }
}
