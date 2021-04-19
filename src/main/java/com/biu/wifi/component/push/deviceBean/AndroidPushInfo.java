package com.biu.wifi.component.push.deviceBean;

import com.biu.wifi.component.push.AndroidNotification;
import com.biu.wifi.component.push.AndroidNotification.DisplayType;

public class AndroidPushInfo extends CorePushInfo {


    public AndroidPushInfo(String appkey, String master_secret, String deviceToken, String info, String title) {
        super(appkey, master_secret, deviceToken, info, title);
    }

    /**
     * 通知栏提示文字 ticker
     */
    private String ticker = "通知";
    /**
     * 通知栏提示文字 ticker
     */
    private String title = "54校园";
    /**
     * 默认 享去
     */
    private String customAfterOpen = "54校园";

    /**
     * 展示方式
     */
    private DisplayType displayType = AndroidNotification.DisplayType.NOTIFICATION;

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCustomAfterOpen() {
        return customAfterOpen;
    }

    public void setCustomAfterOpen(String customAfterOpen) {
        this.customAfterOpen = customAfterOpen;
    }

    public DisplayType getDisplayType() {
        return displayType;
    }

    public void setDisplayType(DisplayType displayType) {
        this.displayType = displayType;
    }
}
