package com.biu.wifi.campus.service;

import com.biu.wifi.campus.dao.CollectionMapper;
import com.biu.wifi.campus.dao.model.Collection;
import com.biu.wifi.core.support.orm.mybatis3.util.IbatisServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollectionService {

    @Autowired
    private CollectionMapper collectionMapper;

    public List<Collection> findList(Collection entity) {
        return IbatisServiceUtils.find(entity, collectionMapper);
    }

    public void addCollection(Collection entity) {
        try {
            IbatisServiceUtils.insert(entity, collectionMapper);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
