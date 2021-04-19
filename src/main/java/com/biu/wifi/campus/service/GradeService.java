package com.biu.wifi.campus.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biu.wifi.campus.dao.GradeMapper;
import com.biu.wifi.campus.dao.model.Grade;
import com.biu.wifi.core.support.orm.mybatis3.util.IbatisServiceUtils;

@Service
public class GradeService {

    @Autowired
    private GradeMapper gradeMapper;

    public List<Grade> findList(Grade entity) {
        return IbatisServiceUtils.find(entity, gradeMapper);
    }

    public Grade find(Integer id) {
        return gradeMapper.selectByPrimaryKey(id);
    }

}
