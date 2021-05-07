package com.biu.wifi.campus.service;

import cn.hutool.core.collection.CollectionUtil;
import com.biu.wifi.campus.dao.InstituteMapper;
import com.biu.wifi.campus.dao.model.Institute;
import com.biu.wifi.core.support.orm.mybatis3.util.IbatisServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InstituteService {

    @Autowired
    private InstituteMapper instituteMapper;

    public List<Institute> findList(Institute entity) {
        return IbatisServiceUtils.find(entity, instituteMapper);
    }

    public Institute getById(Integer id) {
        return instituteMapper.selectByPrimaryKey(id);
    }

    public String getInstituteNamesByIds(String applyDeptId) {
        List<String> list = new ArrayList<>();
        for (String id : applyDeptId.split(",")) {
            Institute institute = instituteMapper.selectByPrimaryKey(Integer.valueOf(id));
            if (institute != null) {
                list.add(institute.getName());
            }
        }
        return CollectionUtil.join(list, ",");
    }

    public List<Institute> findAll() {

        return instituteMapper.selectByExample(null);
    }
}
