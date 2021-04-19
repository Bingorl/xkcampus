package com.biu.wifi.campus.controller.common;

import com.biu.wifi.campus.Tool.JsonUtilEx;
import com.biu.wifi.campus.exception.BizException;
import com.biu.wifi.campus.exception.NeedLoginException;
import com.biu.wifi.campus.exception.SignatureNoSuccessException;
import com.biu.wifi.campus.result.Result;
import com.biu.wifi.core.util.ServletUtilsEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.SocketTimeoutException;

@EnableWebMvc
@ControllerAdvice
public class GlobalExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler
    public void exp(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        ex.printStackTrace();
        logger.error("进入spring全局异常处理：{}", ex.getMessage());

        if (ex instanceof NeedLoginException) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.NO_LOGIN, "用户未登录", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
        } else if (ex instanceof SignatureNoSuccessException) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.SIGNATURE_ERROR, "验签失败", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
        } else if (ex instanceof BizException) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(((BizException) ex).getKey(), ex.getMessage(), null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
        } else if (ex instanceof SocketTimeoutException) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "请求超时"));
            ServletUtilsEx.renderText(response, strToMoblieJson);
        } else if (ex instanceof IllegalArgumentException) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, ex.getMessage(), null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
        } else {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.FAILURE, "服务器异常（失败）", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
        }
    }
}
