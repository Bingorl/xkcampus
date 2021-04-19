package com.biu.wifi.campus.service;

import java.util.List;

import com.biu.wifi.campus.dao.model.ClassCriteria;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biu.wifi.campus.dao.ClassMapper;
import com.biu.wifi.campus.dao.model.Class;
import com.biu.wifi.core.support.orm.mybatis3.util.IbatisServiceUtils;

@Service
public class ClassService {

    @Autowired
    private ClassMapper classMapper;

    public List<Class> findList(Class entity) {
        return IbatisServiceUtils.find(entity, classMapper);
    }

    public Class findByCode(String code) {
        ClassCriteria example = new ClassCriteria();
        example.createCriteria()
                .andCodeEqualTo(code)
                .andIsDeleteEqualTo((short) 2);
        List<Class> classes = classMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(classes)) {
            return null;
        } else {
            return classes.get(0);
        }
    }
}
