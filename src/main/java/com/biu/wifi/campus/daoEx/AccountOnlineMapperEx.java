package com.biu.wifi.campus.daoEx;

import org.springframework.stereotype.Repository;

@Repository
public interface AccountOnlineMapperEx {

    //删除过期token
    int deleteToken();

}
