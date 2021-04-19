package com.biu.wifi.campus.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biu.wifi.campus.dao.PostLikeMapper;
import com.biu.wifi.campus.dao.model.PostLike;
import com.biu.wifi.core.support.orm.mybatis3.util.IbatisServiceUtils;

@Service
public class PostLikeService {

    @Autowired
    private PostLikeMapper postLikeMapper;

    public int addPostLike(PostLike entity) {
        try {
            IbatisServiceUtils.insert(entity, postLikeMapper);
            return entity.getId();
        } catch (Exception e) {

            e.printStackTrace();
            return 0;
        }
    }

    public PostLike getPostLike(PostLike entity) {
        return IbatisServiceUtils.get(entity, postLikeMapper);
    }

    public void deletePostLike(PostLike entity) {
        try {
            IbatisServiceUtils.delete(entity, postLikeMapper);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
