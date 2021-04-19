package com.biu.wifi.component.goeasy;

import com.google.gson.Gson;
import io.goeasy.GoEasy;

/**
 * @author zhangbin.
 * @date 2018/11/23.
 */
public class GoEasyUtil {

    private static final String cdnHost = "cdn-hangzhou.goeasy.io";
    private static final String restHost = "rest-hangzhou.goeasy.io";
    private static final String subscribeKey = "BS-aef230331de84ac4b98fceacd05e2499";
    private static final String commonKey = "BC-46973f3e3e11494c999cc05e1f0b7ae6";

    public static GoEasy goEasy = null;

    public static GoEasy getInstance() {
        if (goEasy == null) {
            goEasy = new GoEasy(restHost, commonKey);
        }
        return goEasy;
    }

    public static void publish(String channel, Object object) {
        Gson gson = new Gson();
        String content = gson.toJson(object);
        GoEasyUtil.getInstance().publish(channel, content);
    }

    public static void main(String[] args) {
        String content = "[{\"isDelete\":2,\"bottom\":\"38.0%\",\"actId\":31,\"updateTime\":\"Nov 30, 2018 9:25:51 AM\",\"itemNo\":\"1号\",\"version\":\"1543541151326\",\"diffCount\":11,\"itemName\":\"节目1\",\"rankingSort\":\"2名\",\"createTime\":\"Nov 30, 2018 9:23:09 AM\",\"logo\":\"2018\\\\11\\\\30\\\\5d7ff473-ceff-4f9c-a7b7-eb142b4420f6.JPG\",\"id\":137,\"voteCount\":11,\"username\":\"\",\"height\":\"33.0%\"},{\"isDelete\":2,\"bottom\":\"85.0%\",\"actId\":31,\"updateTime\":\"Nov 30, 2018 9:26:32 AM\",\"itemNo\":\"2号\",\"version\":\"1543541192501\",\"diffCount\":9,\"itemName\":\"节目2\",\"rankingSort\":\"3名\",\"createTime\":\"Nov 30, 2018 9:23:09 AM\",\"logo\":\"2018\\\\11\\\\30\\\\610af559-a3ed-4f81-a44b-c046ce8f383a.JPG\",\"id\":138,\"voteCount\":9,\"username\":\"\",\"height\":\"80.0%\"},{\"isDelete\":2,\"bottom\":\"44.0%\",\"actId\":31,\"updateTime\":\"Nov 30, 2018 9:24:11 AM\",\"itemNo\":\"3号\",\"version\":\"1543541051004\",\"diffCount\":13,\"itemName\":\"节目3\",\"rankingSort\":\"1名\",\"createTime\":\"Nov 30, 2018 9:23:09 AM\",\"logo\":\"2018\\\\11\\\\30\\\\e09e5481-9186-4980-bcf4-7338173dc3bd.JPG\",\"id\":139,\"voteCount\":13,\"username\":\"\",\"height\":\"39.0%\"}]";
        GoEasyUtil.getInstance().publish("the_result_of_activity_31", content);
    }
}
