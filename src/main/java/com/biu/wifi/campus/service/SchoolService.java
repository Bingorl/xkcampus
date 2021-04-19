package com.biu.wifi.campus.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biu.wifi.campus.dao.model.SchoolCriteria;
import com.biu.wifi.campus.dao.model.SchoolCriteria.Criteria;
import com.biu.wifi.campus.dao.SchoolMapper;
import com.biu.wifi.campus.dao.model.School;
import com.biu.wifi.core.support.orm.mybatis3.util.IbatisServiceUtils;

@Service
public class SchoolService {

    @Autowired
    private SchoolMapper schoolMapper;

    public void addSchool(School entity) {
        try {
            IbatisServiceUtils.insert(entity, schoolMapper);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public School getSchool(School entity) {
        return IbatisServiceUtils.get(entity, schoolMapper);
    }

    public School getById(Integer id) {
        return schoolMapper.selectByPrimaryKey(id);
    }

    public void updateSchool(School entity) {
        IbatisServiceUtils.updateByPk(entity, schoolMapper);
    }

    public void deleteSchool(School entity) {
        try {
            IbatisServiceUtils.delete(entity, schoolMapper);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public List<School> findSchoolList(School entity) {
        return IbatisServiceUtils.find(entity, schoolMapper);
    }

    public List<School> findList(School entity) {
        SchoolCriteria criteria = new SchoolCriteria();
        Criteria cc = criteria.createCriteria();
        if (StringUtils.isNotBlank(entity.getName())) {
            cc.andNameLike("%" + entity.getName() + "%");
        }
        return schoolMapper.selectByExample(criteria);
    }
}
