package com.biu.wifi.campus.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biu.wifi.campus.dao.SayingCommentMapper;
import com.biu.wifi.campus.dao.SayingMapper;
import com.biu.wifi.campus.dao.SayingReplyMapper;
import com.biu.wifi.campus.dao.model.Saying;
import com.biu.wifi.campus.dao.model.SayingComment;
import com.biu.wifi.campus.dao.model.SayingReply;
import com.biu.wifi.campus.daoEx.BackendSayingMapperEx;
import com.biu.wifi.core.support.orm.mybatis3.util.IbatisServiceUtils;

@Service
public class BackendSayingService {

    @Autowired
    private BackendSayingMapperEx sayingMapperEx;

    @Autowired
    private SayingCommentMapper sayingCommentMapper;

    @Autowired
    private SayingReplyMapper sayingReplyMapper;

    @Autowired
    private SayingMapper sayingMapper;

    public List<Map<String, Object>> findSayingList(Integer creatorId, String content, String startTime, String endTime, Long time, String creatorName) {
        return sayingMapperEx.findSayingList(creatorId, content, startTime, endTime, time, creatorName);
    }

    public List<Map<String, Object>> findSayingComments(Integer sayingId, Long time) {
        return sayingMapperEx.findSayingComments(sayingId, time);
    }

    public List<Map<String, Object>> findSayingReplys(Integer commentId) {
        return sayingMapperEx.findSayingReplys(commentId);
    }

    public void deleteSayingComment(SayingComment entity) {
        sayingCommentMapper.updateByPrimaryKeySelective(entity);
    }

    public void deleteSayingCommentReply(SayingReply entity) {
        sayingReplyMapper.updateByPrimaryKeySelective(entity);
    }

    public void updateSaying(Saying entity) {
        sayingMapper.updateByPrimaryKeySelective(entity);
    }

    public Saying getSaying(Saying entity) {
        return IbatisServiceUtils.get(entity, sayingMapper);
    }

    public SayingReply getSayingReply(SayingReply entity) {
        return IbatisServiceUtils.get(entity, sayingReplyMapper);
    }
}
