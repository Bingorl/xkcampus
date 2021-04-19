package com.biu.wifi.component.push.deviceBean;

public class IosPushInfo extends CorePushInfo {

    public IosPushInfo(String appkey, String master_secret, String deviceToken, String info, String title) {
        super(appkey, master_secret, deviceToken, info, title);
    }

    /**
     * ios badge 默认 0
     */
    private Integer badge = 0;
    /**
     * ios sound 默认 default
     */
    private String sound = "default";

    public Integer getBadge() {
        return badge;
    }

    public void setBadge(Integer badge) {
        this.badge = badge;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }


}
