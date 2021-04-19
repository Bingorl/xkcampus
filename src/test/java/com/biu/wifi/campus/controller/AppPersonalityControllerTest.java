package com.biu.wifi.campus.controller;

import com.biu.wifi.campus.BaseJunit;
import org.junit.Test;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * @author zhangbin.
 * @date 2018/12/20.
 */
public class AppPersonalityControllerTest extends BaseJunit {

    @Test
    public void app_findPushList() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/app_findPushList.do")
                .param("page", "1")
                .param("user_id", "119"))
                .andReturn();
        logger.info("服务器返回：{}", result.getResponse().getContentAsString());
    }
}