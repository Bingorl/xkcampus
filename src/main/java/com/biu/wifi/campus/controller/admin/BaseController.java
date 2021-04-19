package com.biu.wifi.campus.controller.admin;

import com.biu.wifi.campus.Tool.JsonUtilEx;
import com.biu.wifi.campus.exception.BizException;
import com.biu.wifi.campus.exception.NeedLoginException;
import com.biu.wifi.campus.exception.SignatureNoSuccessException;
import com.biu.wifi.campus.result.Result;
import com.biu.wifi.core.util.ServletUtilsEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class BaseController {

    public Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 基于@ExceptionHandler异常处理
     */
    @ExceptionHandler
    public void exp(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        logger.info("进入手机接口异常处理");
        ex.printStackTrace();
        // 根据不同异常返回不同结果
        if (ex instanceof NeedLoginException) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.NO_LOGIN, "用户未登录", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
        } else if (ex instanceof SignatureNoSuccessException) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.SIGNATURE_ERROR, "验签失败", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
        } else if (ex instanceof BizException) {
            Result result;
            BizException exception = (BizException) ex;
            if (exception.getKey() != Result.SQL_EXECUTE_FAILURE && exception.getKey() != Result.FAILURE)
                result = new Result(exception.getKey(), ex.getMessage());
            else
                result = new Result(Result.FAILURE, "服务器异常（失败）");
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(result));
        } else if (ex instanceof IllegalArgumentException) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.FAILURE, ex.getMessage(), null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
        } else {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.FAILURE, "服务器异常（失败）", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
        }
    }
}
