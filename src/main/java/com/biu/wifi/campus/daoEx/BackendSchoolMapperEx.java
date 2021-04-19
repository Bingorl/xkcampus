package com.biu.wifi.campus.daoEx;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface BackendSchoolMapperEx {

    public List<Map<String, Object>> findStudentNumbers(@Param("schoolId") Integer schoolId, @Param("stuNumber") String stuNumber, @Param("status") Short status);

    public List<Map<String, Object>> findSchoolInfoList(@Param("name") String name);

}
