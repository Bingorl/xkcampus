package com.biu.wifi.campus.service;

import java.io.IOException;

//接口类型：互亿无线触发短信接口，支持发送验证码短信、订单通知短信等。
//账户注册：请通过该地址开通账户http://sms.ihuyi.com/register.html
//注意事项：
//（1）调试期间，请用默认的模板进行测试，默认模板详见接口文档；
//（2）请使用 用户名(例如：cf_demo123)及 APIkey来调用接口，APIkey在会员中心可以获取；
//（3）该代码仅供接入互亿无线短信接口参考使用，客户可根据实际需要自行编写；

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biu.wifi.campus.dao.model.ShortMessage;
import com.biu.wifi.core.support.cache.CacheSupport;

@Service
public class SMSToolService {

    @Autowired
    private ShortMessageService shortMessageService;

    private String Url = "http://106.ihuyi.cn/webservice/sms.php?method=Submit";

    public Integer sendSMS(String phone, Integer mobile_code, short type, String ip) throws Exception {
        CacheSupport.put("mobileyanzheng", phone, mobile_code);
        // 发送内容
        String content = new String("您的验证码是：" + mobile_code + "。请不要把验证码泄露给其他人。");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 每个手机号码每天只能发送3条
        List<ShortMessage> phoneMessages = shortMessageService.findPhoneMessagesByPhone(phone, sdf.format(new Date()));
        // 每个IP每天只能发送20条
        List<ShortMessage> ipMessages = shortMessageService.findPhoneMessagesByIp(ip, sdf.format(new Date()));
        int ret = -1;
        if (phoneMessages.size() > 0 || ipMessages.size() > 0) {
            if (phoneMessages.size() >= 3) {
                //超过每个号码每日发送最大条数
                ret = -1;
            } else if (ipMessages.size() >= 20) {
                //超过每个号IP每日发送最大条数
                ret = -2;
            } else {
                // 少于3条可以发送
                int result = sendMessage(content, phone);
                // 发送完之后入库
                if (result == 2) {
                    ShortMessage message = new ShortMessage();
                    message.setPhone(phone);
                    message.setIp(ip);
                    message.setContent(content);
                    message.setCreateTime(sdf.format(new Date()));
                    message.setVerificationCode(String.valueOf(mobile_code));
                    message.setStatus((short) 1);
                    message.setType(type);
                    ret = shortMessageService.addPhoneMessages(message);
                }
            }
        } else {
            //第一次发送,直接发送短信,入库
            int result = sendMessage(content, phone);
            // 发送完之后入库
            if (result == 2) {
                ShortMessage message = new ShortMessage();
                message.setPhone(phone);
                message.setIp(ip);
                message.setContent(content);
                message.setCreateTime(sdf.format(new Date()));
                message.setVerificationCode(String.valueOf(mobile_code));
                message.setStatus((short) 1);
                message.setType(type);
                ret = shortMessageService.addPhoneMessages(message);
            }
        }
        return ret;
    }

    // 发送短信
    public int sendMessage(String content, String phone) {
        HttpClient client = new HttpClient();
        PostMethod method = new PostMethod(Url);

        client.getParams().setContentCharset("GBK");
        method.setRequestHeader("ContentType",
                "application/x-www-form-urlencoded;charset=GBK");

        NameValuePair[] data = {// 提交短信
                new NameValuePair("account", "C89066115"),
                //查看密码请登录用户中心->验证码、通知短信->帐户及签名设置->APIKEY
                new NameValuePair("password", "1e370804b4a79c998d6790b0beef54af"),
                new NameValuePair("mobile", phone),
                new NameValuePair("content", content),};
        method.setRequestBody(data);

        int result = 0;
        try {
            client.executeMethod(method);

            String SubmitResult = method.getResponseBodyAsString();

            // System.out.println(SubmitResult);

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

            result = Integer.parseInt(code);

        } catch (HttpException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        } catch (DocumentException e) {

            e.printStackTrace();
        }
        return result;
    }

}
