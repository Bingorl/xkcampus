package com.biu.wifi.campus.controller;

import com.biu.wifi.campus.BaseJunit;
import org.junit.Test;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * @author zhangbin.
 * @date 2018/12/8.
 */
public class BackendTeachingWeekControllerTest extends BaseJunit {
    @Test
    public void back_api_addTeachingCourseTimePlan() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/back_api_addTeachingCourseTimePlan.do")
                .param("schoolId", "26")
                .param("periodType", "4")
                .param("courseCount", "3")
                .param("startTime", "18:30")
                .param("continueTime", "45")
                .param("restTime", "10"))
                .andReturn();
        if (result.getResponse().getStatus() == 200) {
            logger.info("服务器返回：{}", result.getResponse().getContentAsString());
        } else {
            logger.info("接口调用错误");
        }
    }

    @Test
    public void back_api_addTeachingCourseTimePlanList() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/back_api_addTeachingCourseTimePlanList.do")
                .param("schoolId", "26"))
                .andReturn();
        if (result.getResponse().getStatus() == 200) {
            logger.info("服务器返回：{}", result.getResponse().getContentAsString());
        } else {
            logger.info("接口调用错误");
        }
    }
}