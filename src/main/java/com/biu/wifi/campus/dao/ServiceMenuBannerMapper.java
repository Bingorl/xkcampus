package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.ServiceMenuBanner;
import com.biu.wifi.campus.dao.model.ServiceMenuBannerCriteria;
import com.biu.wifi.core.base.CoreDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceMenuBannerMapper extends CoreDao {
    long countByExample(ServiceMenuBannerCriteria example);

    int deleteByExample(ServiceMenuBannerCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(ServiceMenuBanner record);

    int insertSelective(ServiceMenuBanner record);

    List<ServiceMenuBanner> selectByExample(ServiceMenuBannerCriteria example);

    ServiceMenuBanner selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ServiceMenuBanner record, @Param("example") ServiceMenuBannerCriteria example);

    int updateByExample(@Param("record") ServiceMenuBanner record, @Param("example") ServiceMenuBannerCriteria example);

    int updateByPrimaryKeySelective(ServiceMenuBanner record);

    int updateByPrimaryKey(ServiceMenuBanner record);
}