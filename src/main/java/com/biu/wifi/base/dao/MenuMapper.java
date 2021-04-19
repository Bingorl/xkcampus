package com.biu.wifi.base.dao;

import com.biu.wifi.base.dao.model.Menu;
import com.biu.wifi.base.dao.model.MenuCriteria;
import com.biu.wifi.core.base.CoreDao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface MenuMapper extends CoreDao {
    int countByExample(MenuCriteria example);

    int deleteByExample(MenuCriteria example);

    int deleteByPrimaryKey(String id);

    int insert(Menu record);

    int insertSelective(Menu record);

    List<Menu> selectByExample(MenuCriteria example);

    Menu selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") Menu record, @Param("example") MenuCriteria example);

    int updateByExample(@Param("record") Menu record, @Param("example") MenuCriteria example);

    int updateByPrimaryKeySelective(Menu record);

    int updateByPrimaryKey(Menu record);
}