package com.biu.wifi.campus.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biu.wifi.campus.dao.SayingCommentLikeMapper;
import com.biu.wifi.campus.dao.model.SayingCommentLike;
import com.biu.wifi.core.support.orm.mybatis3.util.IbatisServiceUtils;

@Service
public class SayingCommentLikeService {

    @Autowired
    private SayingCommentLikeMapper commentLikeMapper;

    public SayingCommentLike get(SayingCommentLike entity) {
        return IbatisServiceUtils.get(entity, commentLikeMapper);
    }

    public int insert(SayingCommentLike entity) {
        return commentLikeMapper.insertSelective(entity);
    }

    public int update(SayingCommentLike entity) {
        return IbatisServiceUtils.updateByPk(entity, commentLikeMapper);
    }

    public int delete(SayingCommentLike entity) {
        try {
            return IbatisServiceUtils.delete(entity, commentLikeMapper);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

}
