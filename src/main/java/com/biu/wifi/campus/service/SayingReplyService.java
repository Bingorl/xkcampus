package com.biu.wifi.campus.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biu.wifi.campus.dao.SayingReplyMapper;
import com.biu.wifi.campus.dao.model.SayingReply;
import com.biu.wifi.campus.daoEx.SayingMapperEx;
import com.biu.wifi.core.support.orm.mybatis3.util.IbatisServiceUtils;

@Service
public class SayingReplyService {

    @Autowired
    private SayingReplyMapper replyMapper;

    @Autowired
    private SayingMapperEx sayingMapperEx;

    public SayingReply getById(Integer id) {
        return replyMapper.selectByPrimaryKey(id);
    }

    public int insert(SayingReply entity) {
        return replyMapper.insertSelective(entity);
    }

    public int update(SayingReply entity) {
        return IbatisServiceUtils.updateByPk(entity, replyMapper);
    }

    //根据评论id获取回复列表
    public List<Map<String, Object>> findReplyListFromCommentId(Integer commentId) {
        return sayingMapperEx.findReplyListFromCommentId(commentId);
    }

}
