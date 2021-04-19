package com.biu.wifi.campus.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biu.wifi.campus.dao.SayingCommentMapper;
import com.biu.wifi.campus.dao.model.SayingComment;
import com.biu.wifi.campus.daoEx.SayingMapperEx;
import com.biu.wifi.core.support.orm.mybatis3.util.IbatisServiceUtils;

@Service
public class SayingCommentService {

    @Autowired
    private SayingCommentMapper commentMapper;

    @Autowired
    private SayingMapperEx sayingMapperEx;

    public List<SayingComment> findList(SayingComment entity) {
        return IbatisServiceUtils.find(entity, commentMapper);
    }

    public SayingComment getById(Integer id) {
        return commentMapper.selectByPrimaryKey(id);
    }

    public SayingComment get(SayingComment entity) {
        return IbatisServiceUtils.get(entity, commentMapper);
    }

    public int insert(SayingComment entity) {
        return commentMapper.insertSelective(entity);
    }

    public int update(SayingComment entity) {
        return IbatisServiceUtils.updateByPk(entity, commentMapper);
    }

    //获取新鲜事列表页评论回复列表(15条)
    public List<Map<String, Object>> findCommentAndReplyList(Integer sayingId) {
        return sayingMapperEx.findCommentAndReplyList(sayingId);
    }

    //获取新鲜事评论列表
    public List<Map<String, Object>> user_findSayingCommentList(Integer userId, String time, Integer sayingId) {
        return sayingMapperEx.user_findSayingCommentList(userId, time, sayingId);
    }

}
