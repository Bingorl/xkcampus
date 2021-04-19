package com.biu.wifi.component.ali;

import javax.servlet.http.HttpServletRequest;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.biu.wifi.core.CoreConstants;
import com.biu.wifi.core.util.ServletUtilsEx;

public class AlipayService {

    public static String APP_ID = CoreConstants.getProperty("pay.ali.appId");
    public static String APP_PRIVATE_KEY = CoreConstants.getProperty("pay.ali.rsaPrivate");
    public static String CHARSET = "utf-8";
    public static String ALIPAY_PUBLIC_KEY = CoreConstants.getProperty("pay.ali.AlipayPublicKey");
    public static String SellerId = CoreConstants.getProperty("pay.ali.partnerId");
    public static String NotifyUrl = CoreConstants.getProperty("pay.ali.notifyUrl");

    //获取直接给客户端的参数
    public static String getOrderString(HttpServletRequest request, Double money, String OutTradeNo) {
        //实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", APP_ID, APP_PRIVATE_KEY, "json", CHARSET, ALIPAY_PUBLIC_KEY, "RSA2");
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest appPayRequest = new AlipayTradeAppPayRequest();
        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody("mag支付");
        model.setSubject("mag支付");
        model.setOutTradeNo(OutTradeNo);
        //订单总金额，单位为元
        model.setTotalAmount(money.toString());
        //收款支付宝用户ID
        model.setSellerId(SellerId);
        model.setProductCode("QUICK_MSECURITY_PAY");
        appPayRequest.setBizModel(model);
        appPayRequest.setNotifyUrl(ServletUtilsEx.getHostURL(request) + "mag/" + NotifyUrl);
        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(appPayRequest);
            System.out.println(response.getBody());//就是orderString 可以直接给客户端请求，无需再做处理。
            return response.getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return "fail";
        }
    }

}
