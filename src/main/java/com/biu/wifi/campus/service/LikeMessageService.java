package com.biu.wifi.campus.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biu.wifi.campus.dao.LikeMessageMapper;
import com.biu.wifi.campus.dao.model.LikeMessage;

@Service
public class LikeMessageService {

    @Autowired
    private LikeMessageMapper likeMessageMapper;

    public int addLikeMessage(LikeMessage entity) {
        return likeMessageMapper.insertSelective(entity);
    }
}
