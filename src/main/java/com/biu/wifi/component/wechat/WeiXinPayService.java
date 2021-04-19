package com.biu.wifi.component.wechat;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import com.biu.wifi.core.CoreConstants;
import com.biu.wifi.core.util.ServletUtilsEx;
import com.biu.wifi.campus.Tool.Md5;

public class WeiXinPayService {
    /**
     * @param osn
     * @param money
     * @param ip
     * @return String
     * @throws
     * @方法名称:sendWxPayRequest
     * @内容摘要: ＜发送统一下单请求＞
     */
    public static String sendWxPayRequest(HttpServletRequest request, String osn, Integer money, String ip, String openid) {
        // 构造HTTP请求
        HttpClient httpclient = new HttpClient();
        PostMethod postMethod = new PostMethod("https://api.mch.weixin.qq.com/pay/unifiedorder");
        String nonce_str = create_nonce_str();
        String sign = null;
        try {
            sign = getSign(request, nonce_str, osn, ip, money, openid);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        StringBuffer requestStr = new StringBuffer("<xml>");
        requestStr.append("<appid><![CDATA[");
        requestStr.append(CoreConstants.getProperty("wx_appid"));//应用ID
        requestStr.append("]]></appid>");
        requestStr.append("<body><![CDATA[");
        requestStr.append(CoreConstants.getProperty("wx_body"));//商品描述
        requestStr.append("]]></body>");
        requestStr.append("<mch_id><![CDATA[");
        requestStr.append(CoreConstants.getProperty("wx_mch_id"));//商户号
        requestStr.append("]]></mch_id>");
        requestStr.append("<nonce_str><![CDATA[");
        requestStr.append(nonce_str);//随机字符串
        requestStr.append("]]></nonce_str>");
        requestStr.append("<notify_url><![CDATA[");
        requestStr.append(ServletUtilsEx.getHostURL(request) + "mag/" + CoreConstants.getProperty("notify_url"));//通知地址
        requestStr.append("]]></notify_url>");
        requestStr.append("<openid><![CDATA[");
        requestStr.append(openid);//用户openid
        requestStr.append("]]></openid>");
        requestStr.append("<out_trade_no><![CDATA[");
        requestStr.append(osn);//商户订单号
        requestStr.append("]]></out_trade_no>");
        requestStr.append("<spbill_create_ip><![CDATA[");
        requestStr.append(ip);//终端IP
        requestStr.append("]]></spbill_create_ip>");
        requestStr.append("<total_fee><![CDATA[");
        requestStr.append(money);//总金额
        requestStr.append("]]></total_fee>");
        requestStr.append("<trade_type><![CDATA[");
        requestStr.append(CoreConstants.getProperty("wx_trade_type"));//交易类型
        requestStr.append("]]></trade_type>");
        requestStr.append("<sign><![CDATA[");
        requestStr.append(sign);//签名
        requestStr.append("]]></sign>");
        requestStr.append("</xml>");
        // 发送请求
        String strResponse = null;
        try {
            RequestEntity entity = new StringRequestEntity(requestStr.toString(), "text/xml", "UTF-8");
            postMethod.setRequestEntity(entity);
            httpclient.executeMethod(postMethod);
            strResponse = new String(postMethod.getResponseBody(), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            postMethod.releaseConnection();
        }
        System.out.println("微信回复:" + strResponse);
        return strResponse;
    }

    public static String getIpAddr(HttpServletRequest request) {
        if (request.getHeader("x-forwarded-for") == null) {
            return request.getRemoteAddr();
        }
        return request.getHeader("x-forwarded-for");
    }

    //随机数
    public static String create_nonce_str() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 获取签名
     */
    public static String getSign(HttpServletRequest request, String nonce_str, String osn, String ip, Integer money, String openid) throws Exception {
        String signTemp = "appid=" + CoreConstants.getProperty("wx_appid")
                + "&body=" + CoreConstants.getProperty("wx_body")
                + "&mch_id=" + CoreConstants.getProperty("wx_mch_id")
                + "&nonce_str=" + nonce_str
                + "&notify_url=" + (ServletUtilsEx.getHostURL(request) + "mag/" + CoreConstants.getProperty("wx_notify_url"))
                + "&openid=" + openid
                + "&out_trade_no=" + osn
                + "&spbill_create_ip=" + ip
                + "&total_fee=" + money
                + "&trade_type=" + CoreConstants.getProperty("wx_trade_type")
                + "&key=" + CoreConstants.getProperty("wx_appkey"); //这个key注意
        String sign = Md5.mD5(signTemp).toUpperCase();
        return sign;
    }

    public static String getPaySign(String prepayid, String noncestr, String timestamp) throws Exception {
        String signTemp = "appid=" + CoreConstants.getProperty("wx_appid")
                + "&noncestr=" + noncestr
                + "&package=Sign=WXPay"
                + "&partnerid=" + CoreConstants.getProperty("wx_mch_id")
                + "&prepayid=" + prepayid
                + "&timestamp=" + timestamp
                + "&key=" + CoreConstants.getProperty("wx_appkey"); //这个key注意
        String sign = Md5.mD5(signTemp).toUpperCase();
        return sign;
    }
}
