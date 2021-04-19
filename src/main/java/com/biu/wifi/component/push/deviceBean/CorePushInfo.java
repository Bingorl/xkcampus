package com.biu.wifi.component.push.deviceBean;
/**
 * 安卓和ios 共有属性
 *
 * @author Administrator
 */

import java.util.HashMap;

public class CorePushInfo {
    /**
     *  appkey 友盟
     */
    private String appkey;
    /**
     *  master_secret 友盟
     */
    private String master_secret;
    /**
     *  deviceToken 设备token
     */
    private String deviceToken;

    /**
     *  title 推送_内容
     */
    private String info;

    /**
     *  推送标题
     */
    private String title;

    /**
     * parameters 自定义参数
     */
    private HashMap<String, Object> parameters;

    /**
     *  isTest 是否测试  默认  false
     */
    private Boolean isTest = false;


    public CorePushInfo(String appkey, String master_secret, String deviceToken, String info, String title) {
        super();
        this.appkey = appkey;
        this.master_secret = master_secret;
        this.deviceToken = deviceToken;
        this.info = info;
        this.title = title;
    }


    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }


    public Boolean getIsTest() {
        return isTest;
    }

    public void setIsTest(Boolean isTest) {
        this.isTest = isTest;
    }


    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getMaster_secret() {
        return master_secret;
    }

    public void setMaster_secret(String master_secret) {
        this.master_secret = master_secret;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public HashMap<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(HashMap<String, Object> parameters) {
        this.parameters = parameters;
    }
}
