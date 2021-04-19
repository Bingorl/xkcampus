package com.biu.wifi.campus.Tool;

import java.util.HashMap;

import com.biu.wifi.campus.dao.model.Push;
import com.biu.wifi.campus.service.BackendUserService;
import com.biu.wifi.core.support.SpringContextLoader;

public class PushUtils {

    public static void addToPushTable(String title, String content, Short devType, Short messageType, String token, Integer receiverId, Integer objectId) {
        HashMap<String, Object> extras = new HashMap<>();
        extras.put("id", objectId);
        extras.put("title", title);
        extras.put("content", content);

        boolean flag = false;

        try {
            flag = PushTool.pushToUser(receiverId, content, title, extras);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        BackendUserService backendUserService = (BackendUserService) SpringContextLoader.getBean("backendUserService");
        Push push = new Push();
        push.setTitle(title);
        push.setContent(content);
        push.setDeviceType(devType);
        push.setMessageType(messageType);
        push.setToken(token);
        if (flag) {
            push.setType((short) 10);
        } else {
            push.setType((short) 50);
        }
        push.setUserId(receiverId);
        push.setObjectId(objectId);
        backendUserService.addPush(push);
    }
}
