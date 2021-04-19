package com.biu.wifi.campus.daoEx;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface BackendOtherManageMapperEx {

    public List<Map<String, Object>> findReportList(@Param("schoolId") Integer schoolId);

    public List<Map<String, Object>> findFeedbackList();

    public List<Map<String, Object>> findUserListByName(@Param("name") String name);
}
