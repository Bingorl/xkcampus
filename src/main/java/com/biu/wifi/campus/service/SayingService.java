package com.biu.wifi.campus.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biu.wifi.campus.dao.SayingMapper;
import com.biu.wifi.campus.dao.model.Saying;
import com.biu.wifi.campus.daoEx.SayingMapperEx;
import com.biu.wifi.core.support.orm.mybatis3.util.IbatisServiceUtils;

@Service
public class SayingService {

    @Autowired
    private SayingMapper sayingMapper;

    @Autowired
    private SayingMapperEx sayingMapperEx;

    public List<Saying> findList(Saying entity) {
        return IbatisServiceUtils.find(entity, sayingMapper);
    }

    public Saying getById(Integer id) {
        return sayingMapper.selectByPrimaryKey(id);
    }

    public void update(Saying entity) {
        IbatisServiceUtils.updateByPk(entity, sayingMapper);
    }

    //获取新鲜事列表
    public List<Map<String, Object>> user_findSayingList(Integer userId, String time) {
        return sayingMapperEx.user_findSayingList(userId, time);
    }

    //新鲜事详情
    public Map<String, Object> user_getSayingInfo(Integer userId, Integer sayingId) {
        return sayingMapperEx.user_getSayingInfo(userId, sayingId);
    }

    public int addSaying(Saying entity) {
        sayingMapper.insertSelective(entity);
        return entity.getId();
    }
}
