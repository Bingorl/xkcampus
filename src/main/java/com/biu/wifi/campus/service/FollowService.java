package com.biu.wifi.campus.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biu.wifi.campus.dao.FollowMapper;
import com.biu.wifi.campus.dao.model.Follow;
import com.biu.wifi.campus.daoEx.XiaoXinMapperEx;
import com.biu.wifi.core.support.orm.mybatis3.util.IbatisServiceUtils;

@Service
public class FollowService {

    @Autowired
    private FollowMapper followMapper;

    @Autowired
    private XiaoXinMapperEx xiaoXinMapperEx;

    //选择@的人----获取粉丝列表
    public List<Map<String, Object>> user_selectAtUserList(Integer user_id) {
        return xiaoXinMapperEx.user_selectAtUserList(user_id);
    }

    public Follow getFollow(Follow entity) {
        return IbatisServiceUtils.get(entity, followMapper);
    }

    public int addFollow(Follow entity) {
        try {
            followMapper.insertSelective(entity);
            return entity.getId();
        } catch (Exception e) {

            e.printStackTrace();
            return 0;
        }
    }

    public int updateFollow(Follow entity) {
        return IbatisServiceUtils.updateByPk(entity, followMapper);
    }
}
