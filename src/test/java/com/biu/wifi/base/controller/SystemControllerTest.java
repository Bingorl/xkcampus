package com.biu.wifi.base.controller;

import com.biu.wifi.campus.BaseJunit;
import org.junit.Test;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.Assert.*;

/**
 * @author zhangbin.
 * @date 2018/12/20.
 */
public class SystemControllerTest extends BaseJunit {

    @Test
    public void pushMessageToUser() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/pushMessageToUser.do")
                .param("type", "14")
                .param("title", "测试推送系统通知")
                .param("content", "这是测试推送系统通知")
                .param("userIds", "119,121,190,196"))
                .andReturn();
        logger.info("服务器返回数据：{}", result.getResponse().getContentAsString());
    }
}