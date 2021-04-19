package com.biu.wifi.campus.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biu.wifi.campus.dao.PostCommentLikeMapper;
import com.biu.wifi.campus.dao.model.PostCommentLike;
import com.biu.wifi.core.support.orm.mybatis3.util.IbatisServiceUtils;

@Service
public class PostCommentLikeService {

    @Autowired
    private PostCommentLikeMapper postCommentLikeMapper;

    public int addPostCommentLike(PostCommentLike entity) {
        try {
            IbatisServiceUtils.insert(entity, postCommentLikeMapper);
            return entity.getId();
        } catch (Exception e) {

            e.printStackTrace();
        }
        return 0;
    }

    public PostCommentLike getPostCommentLike(PostCommentLike entity) {
        return IbatisServiceUtils.get(entity, postCommentLikeMapper);
    }

    public void deletePostCommentLike(PostCommentLike entity) {
        try {
            IbatisServiceUtils.delete(entity, postCommentLikeMapper);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
