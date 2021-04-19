package com.biu.wifi.component.wechat;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.biu.wifi.core.CoreConstants;

@Controller
public class WeiXinPayController {

    private Log logger = LogFactory.getLog(WeiXinPayController.class);

    @SuppressWarnings("rawtypes")
    @RequestMapping("/weixinotify.do")
    @ResponseBody
    public void weixinotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.info("==============微信异步通知开始");
        Map<String, String> requestMap = null;
        try {
            //微信回复
            requestMap = MessageUtil.parseXml(request);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("获取微信异步通知失败");
            String stwxml = sendFAILToWx();
            printHandle(response, new StringBuffer(stwxml));
            return;
        }
        System.out.println("微信回复===============:" + requestMap);

        //过滤空 设置 TreeMap
        SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
        Iterator it = requestMap.keySet().iterator();
        while (it.hasNext()) {
            String parameter = (String) it.next();
            String parameterValue = requestMap.get(parameter);

            String v = "";
            if (null != parameterValue) {
                v = parameterValue.trim();
            }
            packageParams.put(parameter, v);
        }
        // 账号信息
        String key = CoreConstants.getProperty("wx_appkey");
        logger.info("微信支付成功异步回调参数:" + packageParams);
        //判断签名是否正确
        if (PayCommonUtil.isTenpaySign("UTF-8", packageParams, key)) {
            //------------------------------
            //处理业务开始
            //------------------------------
            logger.info("验证签名成功");
            if ("SUCCESS".equals(packageParams.get("return_code")) && "SUCCESS".equals(packageParams.get("result_code"))) {
                //微信支付成功
                String orderNo = (String) packageParams.get("out_trade_no");//商户订单号
                String transaction_id = (String) packageParams.get("transaction_id");//微信支付订单号
                logger.info(orderNo + "微信支付成功");

                synchronized (this) {
                    //我能订单
                    if (orderNo.indexOf("JN") != -1) {
                    } else if (orderNo.indexOf("QH") != -1) {//抢活订单
                    } else if (orderNo.indexOf("LY") != -1) {//旅游订单
                    } else if (orderNo.indexOf("EC") != -1) {//商品订单
                    }
                }

                String stwxml = sendSUCCESSToWx();
                printHandle(response, new StringBuffer(stwxml));
            }
        } else {
            logger.info("验证签名失败");
            String stwxml = sendFAILToWx();
            printHandle(response, new StringBuffer(stwxml));
        }
    }

    public String sendSUCCESSToWx() {
        String xml = "<xml>"
                + "<return_code><![CDATA[SUCCESS]]></return_code>"
                + "<return_msg><![CDATA[OK]]></return_msg>"
                + "</xml>";
        return xml;
    }

    public String sendFAILToWx() {
        String xml = "<xml>"
                + "<return_code><![CDATA[FAIL]]></return_code>"
                + "<return_msg><![CDATA[FAIL]]></return_msg>"
                + "</xml>";
        return xml;
    }

    private void printHandle(HttpServletResponse response, StringBuffer sb) {
        PrintWriter out = null;
        try {
            response.setContentType("text/xml");
            out = response.getWriter();
            out.println(sb.toString());
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
