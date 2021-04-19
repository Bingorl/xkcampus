package com.biu.wifi.campus.daoEx;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JwCjcxMapperEx {

    int insert(@Param("arrayList") String[] arrayList);
}
