package com.biu.wifi.campus.daoEx;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author zhangbin.
 * @date 2018/10/29.
 */
@Repository
public interface LeaveAuditMapperEx {

    List<Map<String, Object>> findLeaveAuditList(@Param("userId") Integer userId);

    Map<String, Object> findById(@Param("id") Integer id);
}
