package com.biu.wifi.campus.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biu.wifi.campus.dao.SayingLikeMapper;
import com.biu.wifi.campus.dao.model.SayingLike;
import com.biu.wifi.campus.daoEx.SayingMapperEx;
import com.biu.wifi.core.support.orm.mybatis3.util.IbatisServiceUtils;

@Service
public class SayingLikeService {

    @Autowired
    private SayingLikeMapper sayingLikeMapper;

    @Autowired
    private SayingMapperEx sayingMapperEx;

    public List<SayingLike> findList(SayingLike entity) {
        return IbatisServiceUtils.find(entity, sayingLikeMapper, "create_time DESC");
    }

    public SayingLike get(SayingLike entity) {
        return IbatisServiceUtils.get(entity, sayingLikeMapper);
    }

    public int insert(SayingLike entity) {
        return sayingLikeMapper.insertSelective(entity);
    }

    public int update(SayingLike entity) {
        return IbatisServiceUtils.updateByPk(entity, sayingLikeMapper);
    }

    public int delete(SayingLike entity) {
        try {
            return IbatisServiceUtils.delete(entity, sayingLikeMapper);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    //获取新鲜事点赞的人列表
    public List<Map<String, Object>> user_findSayindLikeList(String time, Integer sayingId) {
        return sayingMapperEx.user_findSayindLikeList(time, sayingId);
    }

}
