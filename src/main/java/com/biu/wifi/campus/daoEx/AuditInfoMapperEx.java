package com.biu.wifi.campus.daoEx;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangbin.
 * @date 2018/10/29.
 */
@Repository
public interface AuditInfoMapperEx {

    List<Map> findList(@Param("userId") Integer userId);

    List<HashMap> findMapList(@Param("type") Short type,@Param("bizId") Integer bizId);
}
