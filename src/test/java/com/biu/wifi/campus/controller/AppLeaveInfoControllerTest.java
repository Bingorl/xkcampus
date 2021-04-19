package com.biu.wifi.campus.controller;

import com.biu.wifi.campus.BaseJunit;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Map;

/**
 * @author zhangbin.
 * @date 2018/11/16.
 */
public class AppLeaveInfoControllerTest extends BaseJunit {

    @Test
    public void app_findLeaveInfoListByUserId() throws Exception {
        String url = "/app_findLeaveInfoListByUserId.do";
        RequestBuilder request = MockMvcRequestBuilders.get(url)
                .param("page", "1")
                .param("pageSize", "10")
                .param("user_id", "119");
        MvcResult result = mockMvc.perform(request).andReturn();
        MockHttpServletResponse response = result.getResponse();
        int status = response.getStatus();
        if (status == 200) {
            String data = response.getContentAsString();
            Map map = new ObjectMapper().readValue(data, Map.class);
            System.out.println(map);
        } else {
            logger.error("服务器异常");
            response.getWriter().println();
        }
    }

}