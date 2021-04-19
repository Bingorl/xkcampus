package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.ServiceMenu;
import com.biu.wifi.campus.dao.model.ServiceMenuCriteria;
import com.biu.wifi.core.base.CoreDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceMenuMapper extends CoreDao {
    long countByExample(ServiceMenuCriteria example);

    int deleteByExample(ServiceMenuCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(ServiceMenu record);

    int insertSelective(ServiceMenu record);

    List<ServiceMenu> selectByExample(ServiceMenuCriteria example);

    ServiceMenu selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ServiceMenu record, @Param("example") ServiceMenuCriteria example);

    int updateByExample(@Param("record") ServiceMenu record, @Param("example") ServiceMenuCriteria example);

    int updateByPrimaryKeySelective(ServiceMenu record);

    int updateByPrimaryKey(ServiceMenu record);
}