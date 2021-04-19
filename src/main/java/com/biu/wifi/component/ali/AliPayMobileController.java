package com.biu.wifi.component.ali;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alipay.api.internal.util.AlipaySignature;
import com.biu.wifi.core.CoreConstants;
import com.biu.wifi.core.base.CoreController;
import com.biu.wifi.core.util.ServletUtilsEx;

/**
 * 支付宝
 *
 * @author TYOTANN
 */
@Controller
public class AliPayMobileController extends CoreController {

    /**
     * 获取支付宝Notify过来反馈信息
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/alipayNotify")
    public void payNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.fatal("$$$$收到支付宝notify信息,进入notify流程!");
        try {
            payAfter(request, response);
            logger.fatal("$$$$收到支付宝notify信息,进入notify流程!返回的response");
            ServletUtilsEx.renderText(response, "success");
        } catch (Exception e) {
            logger.fatal("$$$$支付后业务逻辑异常:" + e.getMessage(), e);
            ServletUtilsEx.renderText(response, "fail");
        }
    }

    /**
     * 支付后的业务操作
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    private void payAfter(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            // 获取支付宝POST过来反馈信息
            Map<String, String> params = new HashMap<String, String>();
            Map<String, String[]> requestParams = request.getParameterMap();
            for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
                String name = iter.next();
                String[] values = (String[]) requestParams.get(name);
                String valueStr = "";
                for (int i = 0; i < values.length; i++) {
                    valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
                }
                // 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
                // valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
                params.put(name, valueStr);
            }
            //切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
            //boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String publicKey, String charset, String sign_type)
            boolean flag = AlipaySignature.rsaCheckV1(params, CoreConstants.getProperty("pay.ali.AlipayPublicKey"), "utf-8", "RSA2");

            logger.fatal("$$$$支付宝验签结果:" + flag);

            // 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)
            // 商户订单号
            String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
            // 支付宝交易号
            String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");
            // 交易状态
            String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");
            logger.fatal("$$$$支付信息——商户订单号:" + out_trade_no + ";支付宝交易号:" + trade_no + ",交易状态:" + trade_status);

            if (flag) {
                // 请在这里加上商户的业务逻辑程序代码
                if (trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS")) {
                    // ——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
                    synchronized (this) {
                        //我能订单
                        if (out_trade_no.indexOf("JN") != -1) {
                        } else if (out_trade_no.indexOf("QH") != -1) {//抢活订单
                        } else if (out_trade_no.indexOf("LY") != -1) {//旅游订单
                        } else if (out_trade_no.indexOf("EC") != -1) {//商品订单
                        }
                    }
                } else {
                    logger.fatal("$$$$支付交易状态未知——商户订单号:" + out_trade_no + ";支付宝交易号:" + trade_no + ",交易状态:" + trade_status);
                }
            } else {
                logger.fatal("$$$$支付验证失败——商户订单号:" + out_trade_no + ";支付宝交易号:" + trade_no + ",交易状态:" + trade_status);
            }
        } catch (Exception e) {
            logger.fatal("$$$$支付后业务逻辑异常:" + e.getMessage(), e);
            ServletUtilsEx.renderText(response, "fail");
        }
    }
}
