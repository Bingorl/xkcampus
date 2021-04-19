package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.ServiceMenuCategory;
import com.biu.wifi.campus.dao.model.ServiceMenuCategoryCriteria;
import com.biu.wifi.core.base.CoreDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceMenuCategoryMapper extends CoreDao {
    long countByExample(ServiceMenuCategoryCriteria example);

    int deleteByExample(ServiceMenuCategoryCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(ServiceMenuCategory record);

    int insertSelective(ServiceMenuCategory record);

    List<ServiceMenuCategory> selectByExample(ServiceMenuCategoryCriteria example);

    ServiceMenuCategory selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ServiceMenuCategory record, @Param("example") ServiceMenuCategoryCriteria example);

    int updateByExample(@Param("record") ServiceMenuCategory record, @Param("example") ServiceMenuCategoryCriteria example);

    int updateByPrimaryKeySelective(ServiceMenuCategory record);

    int updateByPrimaryKey(ServiceMenuCategory record);
}