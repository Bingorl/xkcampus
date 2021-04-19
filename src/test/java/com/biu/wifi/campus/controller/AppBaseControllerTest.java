package com.biu.wifi.campus.controller;

import com.biu.wifi.campus.BaseJunit;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * @author zhangbin.
 * @date 2018/12/17.
 */
public class AppBaseControllerTest extends BaseJunit {

    @Test
    public void app_findAuditTypeList() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/app_findAuditTypeList.do")
                .param("user_id", "119"))
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
        logger.info("服务器返回：{}", response.getContentAsString());
    }
}