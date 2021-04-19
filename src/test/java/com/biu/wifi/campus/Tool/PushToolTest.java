package com.biu.wifi.campus.Tool;

import com.biu.wifi.campus.BaseJunit;
import com.biu.wifi.campus.dao.model.User;
import com.biu.wifi.campus.dao.model.UserCriteria;
import com.biu.wifi.campus.service.UserService;
import com.biu.wifi.component.push.deviceBean.AndroidPushInfo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;

/**
 * @author zhangbin.
 * @date 2018/12/11.
 */
public class PushToolTest extends BaseJunit {

    @Autowired
    private UserService userService;
    private final String ios_appkey = "5b63c8bdb27b0a1d78000029";
    private final String ios_master_secret = "qii61ldusgxic7h6t7o6zp99vnc8gjgo";
    private final String android_appkey = "5b7feb2eb27b0a216a00000e";
    private final String android_master_secret = "ddcervzebzm2tze9jhhoy7finjoszaan";

    @Test
    public void groupPush() throws Exception {
        UserCriteria userCriteria = new UserCriteria();
        userCriteria.createCriteria()
                .andIsDeleteEqualTo((short) 2)
                .andSchoolIdEqualTo(26)
                .andDevTypeEqualTo((short) 1)
                .andDevTokenIsNotNull();

        List<User> userList = userService.findList(userCriteria);

        String title = "关于推送学号不正确问题的通知";
        String info = "关于推送学号不正确问题：目前教务数据仅开放17级通信专业，用户已经可以使用成绩查询、我的课表等功能。其他年级专业的数据我们会逐步开放，开放之后将不会提醒学号不正确。空教室预约、请假审批近期会在河海大学上线，敬请期待。";

        User user = userService.getById(171);
        AndroidPushInfo androidPushInfo = new AndroidPushInfo(android_appkey, android_master_secret, user.getDevToken(), info, title);
        androidPushInfo.setIsTest(false);
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("id", "");
        hm.put("title", title);
        hm.put("type", 1);//官方消息
        androidPushInfo.setParameters(hm);
        PushTool.to_Android_User(androidPushInfo);

        int i = 0, j = 0;
//        for (User u : userList) {
//            if (u.getDevToken() != null) {
//                //推送设备类型 1 ios  2 android
//                IosPushInfo iosPushInfo = new IosPushInfo(ios_appkey, ios_master_secret, u.getDevToken(), info, title);
//                iosPushInfo.setIsTest(false);//success
//                iosPushInfo.setBadge(1);
//                PushTool.to_Ios_User(iosPushInfo);
//                i++;
//            }/*else{
//        AndroidPushInfo androidPushInfo = new AndroidPushInfo(android_appkey, android_master_secret, u.getDevToken(), info, title);
//                androidPushInfo.setIsTest(false);
//                PushTool.to_Android_User(androidPushInfo);
//               j++;
//        }*/
//        }
        logger.info("安卓推送了{}台，ios推送了{}台", i, j);
    }

}