package com.biu.wifi.component.push.ios;

import com.biu.wifi.component.push.IOSNotification;

public class IOSBroadcast extends IOSNotification {
    public IOSBroadcast(String appkey, String appMasterSecret) throws Exception {
        setAppMasterSecret(appMasterSecret);
        setPredefinedKeyValue("appkey", appkey);
        this.setPredefinedKeyValue("type", "broadcast");

    }
}
