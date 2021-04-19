package com.biu.wifi.campus.controller.app;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author zhangbin
 * @date 2021-03-31 11:56
 */
@Api(value = "", description = "swagger测试控制器")
@Controller
@RequestMapping("/app/test/swagger/")
public class AppTestSwaggerController {

    @ApiOperation(value = "测试接口1", notes = "测试接口1", httpMethod = "GET", tags = "swagger测试控制器")
    @RequestMapping(value = "test1", method = RequestMethod.GET)
    @ResponseBody
    public String test1() {
        return "test1";
    }

    @ApiOperation(value = "测试接口2", notes = "测试接口2", httpMethod = "GET", tags = "swagger测试控制器")
    @RequestMapping(value = "test2", method = RequestMethod.GET)
    @ResponseBody
    public String test2() {
        return "test2";
    }
}
