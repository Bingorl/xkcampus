package com.biu.wifi.core.invoke.ws;

import java.util.HashMap;

import javax.jws.WebService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biu.wifi.core.invoke.controller.call.CallController;
import com.biu.wifi.core.invoke.controller.call.CallServiceFactory;
import com.biu.wifi.core.util.XMLUtilsEx;

@WebService(endpointInterface = "com.biu.wifi.core.invoke.ws.WebSerivceInvoker", serviceName = "WebSerivceInvoker")
public class WebSerivceInvokerImpl implements WebSerivceInvoker {

    private static Log logger = LogFactory.getLog(WebSerivceInvokerImpl.class);

    @SuppressWarnings("unchecked")
    @Override
    public String invoke(String functionCode, String param) {

        if (logger.isDebugEnabled()) {
            logger.debug("webserivce请求开始:" + functionCode + ";参数:" + param);
        }

        // 取得服务解析器
        CallController serviceHandle = CallServiceFactory.getInstance(functionCode);

        // 服务调用
        try {
            return XMLUtilsEx.serialize(serviceHandle.call(functionCode, XMLUtilsEx.deserialize(param, HashMap.class)).getData(), "root");
        } catch (Exception e) {
            logger.error("webserivce请求异常", e);
        }

        return "";
    }

}
