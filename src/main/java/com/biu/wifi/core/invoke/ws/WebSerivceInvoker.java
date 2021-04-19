package com.biu.wifi.core.invoke.ws;

import javax.jws.WebService;

@WebService
public interface WebSerivceInvoker {

    public String invoke(String functionCode, String param);
}
