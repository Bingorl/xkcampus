package com.biu.wifi.campus.daoEx;

import com.biu.wifi.campus.dao.model.ServiceMenuUserSchool;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ServiceMenuUserSchoolMapperEx {

    List<ServiceMenuUserSchool> selectListByExample(@Param("schoolId") Integer schoolId);

    List<Map<String, Object>> selectMapByExample(@Param("schoolId") Integer schoolId,
                                                 @Param("categoryId") Integer categoryId, @Param("keyword") String keyword);

    long selectMapCountByExample(@Param("schoolId") Integer schoolId);
}