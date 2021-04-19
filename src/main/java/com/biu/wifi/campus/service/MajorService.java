package com.biu.wifi.campus.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biu.wifi.campus.dao.MajorMapper;
import com.biu.wifi.campus.dao.model.Major;
import com.biu.wifi.core.support.orm.mybatis3.util.IbatisServiceUtils;

@Service
public class MajorService {

    @Autowired
    private MajorMapper majorMapper;

    public List<Major> findList(Major entity) {
        return IbatisServiceUtils.find(entity, majorMapper);
    }

}
