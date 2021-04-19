package com.biu.wifi.campus.Tool;

import com.biu.wifi.campus.dao.model.User;
import com.biu.wifi.campus.service.UserService;
import com.biu.wifi.component.push.PushClient;
import com.biu.wifi.component.push.android.AndroidBroadcast;
import com.biu.wifi.component.push.android.AndroidUnicast;
import com.biu.wifi.component.push.deviceBean.AndroidPushInfo;
import com.biu.wifi.component.push.deviceBean.IosPushInfo;
import com.biu.wifi.component.push.ios.IOSBroadcast;
import com.biu.wifi.component.push.ios.IOSUnicast;
import com.biu.wifi.core.CoreConstants;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class PushTool {

    private final static String ios_appkey = "5b63c8bdb27b0a1d78000029";
    private final static String ios_master_secret = "qii61ldusgxic7h6t7o6zp99vnc8gjgo";
    private final static String android_appkey = "5b7feb2eb27b0a216a00000e";
    //private final static String android_umeng_secret = "3dbcb893a0fe448d13d7a477c04d8f92";
    private final static String android_master_secret = "ddcervzebzm2tze9jhhoy7finjoszaan";
    private static PushClient client = new PushClient();

    /**
     * 推个人
     *
     * @param userId
     * @param info
     * @param parameters
     * @throws Exception
     */
    public static Boolean pushToUser(Integer userId, String info, String title, HashMap<String, Object> parameters) throws Exception {
        Boolean flag = false;
        try {
            UserService userService = (UserService) SpringContextUtils.getBean("UserService");
            User uEntity = new User();
            uEntity.setId(userId);
            User user = userService.getUser(uEntity);

            if (null != user) {
                if (user.getDevType().shortValue() == 1) {
                    // IOS
                    IosPushInfo iosPushInfo = new IosPushInfo(ios_appkey, ios_master_secret, user.getDevToken(), info, title);
                    iosPushInfo.setParameters(parameters);
//					if(!Boolean.parseBoolean(CoreConstants.getProperty("is_really"))) {
                    if (!Boolean.parseBoolean(CoreConstants.getProperty("is_really_model"))) {
                        iosPushInfo.setIsTest(true);
                    }
                    iosPushInfo.setBadge(1);

                    flag = to_Ios_User(iosPushInfo);
                } else if (user.getDevType().shortValue() == 2) {
                    // Android
                    AndroidPushInfo androidPushInfo = new AndroidPushInfo(android_appkey, android_master_secret, user.getDevToken(), info, null);
                    androidPushInfo.setParameters(parameters);
                    androidPushInfo.setTitle(title);

//					if(!Boolean.parseBoolean(CoreConstants.getProperty("is_really"))) {
                    if (!Boolean.parseBoolean(CoreConstants.getProperty("is_really_model"))) {
                        androidPushInfo.setIsTest(true);
                    }

                    flag = to_Android_User(androidPushInfo);
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return flag;
    }

    /**
     * 推一组用户
     *
     * @param userId
     * @param info
     * @param parameters
     * @throws Exception
     */
    public static Boolean pushToUserS(Integer[] userId, String info, String title, HashMap<String, Object> parameters) throws Exception {
        Boolean flag = true;
        for (Integer id : userId) {
            Thread.sleep(100);
            if (!pushToUser(id, info, title, parameters)) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    /**
     * 群推
     *
     * @param info
     * @param parameters
     * @throws Exception
     */
    public static Boolean pushAllUser(String info, HashMap<String, Object> parameters) throws Exception {
        try {
            IosPushInfo iosPushInfo = new IosPushInfo(ios_appkey, ios_master_secret, null, info, null);
            iosPushInfo.setParameters(parameters);
            iosPushInfo.setBadge(1);

//			if(!Boolean.parseBoolean(CoreConstants.getProperty("is_really"))) {
            if (!Boolean.parseBoolean(CoreConstants.getProperty("is_really_model"))) {
                iosPushInfo.setIsTest(true);
            }

            boolean ios_flag = to_Ios_AllUser(iosPushInfo);

            AndroidPushInfo androidPushInfo = new AndroidPushInfo(android_appkey, android_master_secret, null, info, null);
            androidPushInfo.setParameters(parameters);

//			if(!Boolean.parseBoolean(CoreConstants.getProperty("is_really"))) {
            if (!Boolean.parseBoolean(CoreConstants.getProperty("is_really_model"))) {
                androidPushInfo.setIsTest(true);
            }

            boolean android_flag = to_Android_AllUser(androidPushInfo);
            if (ios_flag && android_flag) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }
    }

    protected static boolean to_Android_User(AndroidPushInfo androidPushInfo) throws Exception {
        AndroidUnicast unicast = new AndroidUnicast(androidPushInfo.getAppkey(), androidPushInfo.getMaster_secret());
        unicast.setDeviceToken(androidPushInfo.getDeviceToken());
        unicast.setTicker(androidPushInfo.getTicker());
        unicast.setTitle(androidPushInfo.getTitle());
        unicast.setText(androidPushInfo.getInfo());
        unicast.setDisplayType(androidPushInfo.getDisplayType());
        unicast.setPlaySound(true);
        if (androidPushInfo.getIsTest()) {
            unicast.setTestMode();
        } else {
            unicast.setProductionMode();
        }
        JSONObject json = new JSONObject();
        Map<String, Object> extras = androidPushInfo.getParameters();
        if (extras != null) {
            for (String key : extras.keySet()) {
                unicast.setExtraField(key, extras.get(key).toString());
                json.put(key, extras.get(key).toString());
            }
        }
        unicast.goCustomAfterOpen(json);
        return client.send(unicast);
    }

    protected static boolean to_Ios_User(IosPushInfo iosPushInfo) throws Exception {
        IOSUnicast unicast = new IOSUnicast(iosPushInfo.getAppkey(), iosPushInfo.getMaster_secret());
        unicast.setDeviceToken(iosPushInfo.getDeviceToken());
        JSONObject json = new JSONObject();
        json.put("title", iosPushInfo.getTitle());
        json.put("body", iosPushInfo.getInfo());
        unicast.setAlert(json);
        unicast.setBadge(iosPushInfo.getBadge());
        unicast.setSound(iosPushInfo.getSound());
        if (iosPushInfo.getIsTest()) {
            unicast.setTestMode();
        } else {
            unicast.setProductionMode();
        }
        Map<String, Object> extras = iosPushInfo.getParameters();
        if (extras != null) {
            for (String key : extras.keySet()) {
                unicast.setCustomizedField(key, extras.get(key).toString());
            }
        }
        return client.send(unicast);
    }

    protected static boolean to_Ios_AllUser(IosPushInfo iosPushInfo) throws Exception {
        IOSBroadcast broadcast = new IOSBroadcast(iosPushInfo.getAppkey(), iosPushInfo.getMaster_secret());
        JSONObject json = new JSONObject();
        json.put("title", iosPushInfo.getTitle());
        json.put("body", iosPushInfo.getInfo());
        broadcast.setAlert(json);
        broadcast.setBadge(iosPushInfo.getBadge());
        broadcast.setSound(iosPushInfo.getSound());
        if (iosPushInfo.getIsTest()) {
            broadcast.setTestMode();
        } else {
            broadcast.setProductionMode();
        }
        Map<String, Object> extras = iosPushInfo.getParameters();
        if (extras != null) {
            for (String key : extras.keySet()) {
                broadcast.setCustomizedField(key, extras.get(key).toString());
            }
        }
        return client.send(broadcast);
    }

    protected static boolean to_Android_AllUser(AndroidPushInfo androidPushInfo) throws Exception {
        AndroidBroadcast androidBroadcast = new AndroidBroadcast(androidPushInfo.getAppkey(), androidPushInfo.getMaster_secret());
        androidBroadcast.setTicker(androidPushInfo.getTicker());
        androidBroadcast.setTitle(androidPushInfo.getTitle());
        androidBroadcast.setText(androidPushInfo.getInfo());
        androidBroadcast.goAppAfterOpen();
        androidBroadcast.setDisplayType(androidPushInfo.getDisplayType());

        if (androidPushInfo.getIsTest()) {
            androidBroadcast.setTestMode();
        } else {
            androidBroadcast.setProductionMode();
        }
        Map<String, Object> extras = androidPushInfo.getParameters();
        if (extras != null) {
            for (String key : extras.keySet()) {
                androidBroadcast.setExtraField(key, extras.get(key).toString());
            }
        }
        return client.send(androidBroadcast);
    }


    public static void main(String[] args) throws Exception {
//		IosPushInfo iosPushInfo = new IosPushInfo(ios_appkey, ios_master_secret, "00607ec358fd2bec03fff5f5180410c49d69ea3405ce050bb891dce36da7d8a0", "ios单测试推送角标","测试");
        IosPushInfo iosPushInfo = new IosPushInfo(ios_appkey, ios_master_secret, "c17d0f3cbafd07a6771a777adb0e5b83e522380176cb5741f36f6e2ff619fb7f", "正式环境，123456", "测试");
        iosPushInfo.setIsTest(false);//success
        //iosPushInfo.setBadge(1);
        to_Ios_User(iosPushInfo);

//		AndroidPushInfo androidPushInfo = new AndroidPushInfo(android_appkey, android_master_secret, "AiZMjIAk_VzW3LghqHREXNb8oI2urYtXnk7FFc1UgHkJ", "安卓单测试推送");
//		androidPushInfo.setIsTest(true);
//		to_Android_User(androidPushInfo);


//		pushAllUser("测试 ", null);
    }
}
