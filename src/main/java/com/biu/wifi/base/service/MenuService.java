package com.biu.wifi.base.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biu.wifi.base.dao.MenuMapper;
import com.biu.wifi.base.dao.model.Menu;
import com.biu.wifi.core.support.orm.mybatis3.util.IbatisServiceUtils;

@Service
public class MenuService {
    @Autowired
    private MenuMapper dao;

    public List<Menu> find(Menu entity) throws Exception {
        return IbatisServiceUtils.find(entity, dao, "id asc");
    }
}
