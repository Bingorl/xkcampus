package com.biu.wifi.campus.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biu.wifi.campus.dao.AtMessageMapper;
import com.biu.wifi.campus.dao.model.User;
import com.biu.wifi.campus.daoEx.SayingMapperEx;

@Service
public class AtMessageService {

    @Autowired
    private AtMessageMapper atMessageMapper;

    @Autowired
    private SayingMapperEx sayingMapperEx;

    //批量新增@信息
    public void insertAll(Short type, Integer userId, String user, List<User> toerList, Integer objectId, String content, String originContent) {
        sayingMapperEx.insertAllAtMsg(type, userId, user, toerList, objectId, content, originContent);
    }

}
