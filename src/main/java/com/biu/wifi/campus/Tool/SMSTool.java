package com.biu.wifi.campus.Tool;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.biu.wifi.core.CoreConstants;
import com.biu.wifi.core.support.cache.CacheSupport;

public class SMSTool {
    /**
     * APIID：C89066115
     * 1e370804b4a79c998d6790b0beef54af
     */
    public static void sendSMS(String phone, Integer mobile_code) {
        if (Boolean.parseBoolean(CoreConstants.getProperty("is_really_model"))) {
            CacheSupport.put("mobileyanzheng", phone, mobile_code);
        } else {
            CacheSupport.put("mobileyanzheng", phone, 123456);
        }
        //发送内容
        String content = "您的短信验证码为:" + mobile_code + ",请妥善保管!";
        if (Boolean.parseBoolean(CoreConstants.getProperty("is_really_model"))) {
            sendMessage(content, phone);
        }
    }

    //发送短信
    public static void sendMessage(String content, String phone) {
        HttpClient client = new HttpClient();
        PostMethod method = new PostMethod("http://106.ihuyi.cn/webservice/sms.php?method=Submit");
        client.getParams().setContentCharset("GBK");
        method.setRequestHeader("ContentType", "application/x-www-form-urlencoded;charset=GBK");

        NameValuePair[] data = {//提交短信
                new NameValuePair("account", "C89066115"), //查看用户名请登录用户中心->验证码、通知短信->帐户及签名设置->APIID
                new NameValuePair("password", "1e370804b4a79c998d6790b0beef54af"),  //查看密码请登录用户中心->验证码、通知短信->帐户及签名设置->APIKEY
                //new NameValuePair("password", util.StringUtil.MD5Encode("密码")),
                new NameValuePair("mobile", phone),
                new NameValuePair("content", content),
        };
        method.setRequestBody(data);

        try {
            client.executeMethod(method);

            String SubmitResult = method.getResponseBodyAsString();

            //System.out.println(SubmitResult);

            Document doc = DocumentHelper.parseText(SubmitResult);
            Element root = doc.getRootElement();

            String code = root.elementText("code");
            String msg = root.elementText("msg");
            String smsid = root.elementText("smsid");

            System.out.println(code);
            System.out.println(msg);
            System.out.println(smsid);

            if ("2".equals(code)) {
                System.out.println("短信提交成功");
            }

        } catch (HttpException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
