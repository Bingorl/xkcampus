package com.biu.wifi.campus.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biu.wifi.campus.dao.UserAuthMapper;
import com.biu.wifi.campus.dao.model.UserAuth;
import com.biu.wifi.core.support.orm.mybatis3.util.IbatisServiceUtils;

@Service
public class UserAuthService {

    @Autowired
    private UserAuthMapper userAuthMapper;

    public void addUserAuth(UserAuth entity) {
        try {
            IbatisServiceUtils.insert(entity, userAuthMapper);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public UserAuth getUserAuth(UserAuth entity) {
        return IbatisServiceUtils.get(entity, userAuthMapper);
    }

    public void updateUserAuth(UserAuth entity) {
        IbatisServiceUtils.updateByPk(entity, userAuthMapper);
    }

    public void deleteUserAuth(UserAuth entity) {
        try {
            IbatisServiceUtils.delete(entity, userAuthMapper);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public List<UserAuth> findUserAuthList(UserAuth entity) {
        return IbatisServiceUtils.find(entity, userAuthMapper, "create_time DESC");
    }
}
