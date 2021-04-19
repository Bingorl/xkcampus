package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.ServiceMenuUserSchool;
import com.biu.wifi.campus.dao.model.ServiceMenuUserSchoolCriteria;
import com.biu.wifi.core.base.CoreDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceMenuUserSchoolMapper extends CoreDao {
    long countByExample(ServiceMenuUserSchoolCriteria example);

    int deleteByExample(ServiceMenuUserSchoolCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(ServiceMenuUserSchool record);

    int insertSelective(ServiceMenuUserSchool record);

    List<ServiceMenuUserSchool> selectByExample(ServiceMenuUserSchoolCriteria example);

    ServiceMenuUserSchool selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ServiceMenuUserSchool record,
                                 @Param("example") ServiceMenuUserSchoolCriteria example);

    int updateByExample(@Param("record") ServiceMenuUserSchool record,
                        @Param("example") ServiceMenuUserSchoolCriteria example);

    int updateByPrimaryKeySelective(ServiceMenuUserSchool record);

    int updateByPrimaryKey(ServiceMenuUserSchool record);
}