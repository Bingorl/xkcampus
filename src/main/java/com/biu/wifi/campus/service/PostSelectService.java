package com.biu.wifi.campus.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biu.wifi.campus.dao.PostSelectMapper;
import com.biu.wifi.campus.dao.model.PostSelect;
import com.biu.wifi.core.support.orm.mybatis3.util.IbatisServiceUtils;

@Service
public class PostSelectService {

    @Autowired
    private PostSelectMapper postSelectMapper;

    public List<PostSelect> findPostSelectList(PostSelect entity) {
        return IbatisServiceUtils.find(entity, postSelectMapper, "weight DESC");
    }
}
