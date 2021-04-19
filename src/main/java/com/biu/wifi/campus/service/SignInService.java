package com.biu.wifi.campus.service;

import com.biu.wifi.campus.constant.SignInType;
import com.biu.wifi.campus.dao.SignInMapper;
import com.biu.wifi.campus.dao.model.SignIn;
import com.biu.wifi.campus.exception.BizException;
import com.biu.wifi.campus.result.Result;
import com.biu.wifi.core.util.DateUtilsEx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author 张彬.
 * @date 2021/4/9 16:37.
 */
@Service
public class SignInService {

    @Autowired
    private SignInMapper signInMapper;

    public void add(SignIn signIn) {
        signIn.setCreateTime(new Date());
        signIn.setIsDelete((short) 2);
        boolean json = signInMapper.insertSelective(signIn) > 0;
        if (!json) {
            throw new BizException(Result.CUSTOM_MESSAGE, "签到失败");
        }
    }

    public List<SignIn> mySignInList(Integer userId, Integer type) {
        return signInMapper.search(userId, type);
    }

    public List<HashMap> getSignTypeList() {
        List<HashMap> list = new ArrayList<>();
        for (SignInType signInType : SignInType.values()) {
            HashMap hashMap = new HashMap();
            hashMap.put("name", signInType.getDescription());
            hashMap.put("value", signInType.getCode());
            list.add(hashMap);
        }
        return list;
    }

    public List<HashMap> mySignInList(Integer userId) {
        List<SignIn> signIns = mySignInList(userId, null);
        List<HashMap> list = new ArrayList<>();
        for (SignIn signIn : signIns) {
            HashMap hashMap = new HashMap();
            hashMap.put("id", signIn.getId());
            hashMap.put("type", SignInType.getDescription(signIn.getType()));
            hashMap.put("createTime", DateUtilsEx.formatToString(signIn.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            list.add(hashMap);
        }
        return list;
    }
}
