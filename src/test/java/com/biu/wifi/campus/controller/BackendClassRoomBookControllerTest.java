package com.biu.wifi.campus.controller;

import com.biu.wifi.campus.BaseJunit;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * @author zhangbin.
 * @date 2018/12/5.
 */
public class BackendClassRoomBookControllerTest extends BaseJunit {

    @Test
    public void back_api_addOrUpdateClassroomBuilding() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/back_api_addOrUpdateClassroomBuilding.do")
//                .param("id", "")
                .param("schoolId", "26")
                .param("areaPosition", "")
                .param("no", "A03")
                .param("name", "勤学楼")
//                .param("floor", "")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
        if (response.getStatus() == 200) {
            String object = response.getContentAsString();
            logger.info("服务器返回：{}", object);
        } else {
            throw new RuntimeException("接口调用异常");
        }
    }

    @Test
    public void back_api_findClassroomBuildingList() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/back_api_findClassroomBuildingList.do")
                .param("page", "1")
                .param("keyword", "")
                .param("schoolId", "26"))
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
        if (response.getStatus() == 200) {
            String object = response.getContentAsString();
            logger.info("服务器返回：{}", object);
        } else {
            throw new RuntimeException("接口调用异常");
        }
    }

    @Test
    public void back_api_deleteClassroomBuilding() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/back_api_deleteClassroomBuilding.do")
                .param("id", "1"))
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
        if (response.getStatus() == 200) {
            String object = response.getContentAsString();
            logger.info("服务器返回：{}", object);
        } else {
            throw new RuntimeException("接口调用异常");
        }
    }

    @Test
    public void back_api_addOrUpdateClassroomBookUseType() throws Exception {
        String names = "学生活动,补课,宣传会,考务";
        for (String name : names.split(",")) {
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/back_api_addOrUpdateClassroomBookUseType.do")
                    .param("schoolId", "26")
                    .param("name", name))
                    .andReturn();
            MockHttpServletResponse response = result.getResponse();
            if (response.getStatus() == 200) {
                String object = response.getContentAsString();
                logger.info("服务器返回：{}", object);
            } else {
                throw new RuntimeException("接口调用异常");
            }
        }
    }

    @Test
    public void back_api_findClassroomBookUseTypeList() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/back_api_findClassroomBookUseTypeList.do")
                .param("page", "1")
                .param("schoolId", "26")
                .param("keyword", ""))
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
        if (response.getStatus() == 200) {
            String object = response.getContentAsString();
            logger.info("服务器返回：{}", object);
        } else {
            throw new RuntimeException("接口调用异常");
        }
    }

    @Test
    public void back_api_deleteClassroomBookUseType() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/back_api_deleteClassroomBookUseType.do")
                .param("id", "5"))
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
        if (response.getStatus() == 200) {
            String object = response.getContentAsString();
            logger.info("服务器返回：{}", object);
        } else {
            throw new RuntimeException("接口调用异常");
        }
    }

    @Test
    public void back_api_addOrUpdateClassroomBookUseTypeOrganization() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/back_api_addOrUpdateClassroomBookUseTypeOrganization.do")
                .param("useTypeId", "1")
                .param("type", "1")
                .param("instituteId", "33")
                .param("name", "学院")
                .param("schoolId", "26"))
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
        if (response.getStatus() == 200) {
            String object = response.getContentAsString();
            logger.info("服务器返回：{}", object);
        } else {
            throw new RuntimeException("接口调用异常");
        }
    }

    @Test
    public void back_api_findClassroomBookUseTypeOrganizationList() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/back_api_findClassroomBookUseTypeOrganizationList.do")
                .param("page", "1")
                .param("schoolId", "26"))
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
        if (response.getStatus() == 200) {
            String object = response.getContentAsString();
            logger.info("服务器返回：{}", object);
        } else {
            throw new RuntimeException("接口调用异常");
        }
    }

    @Test
    public void back_api_deleteClassroomBookUseTypeOrganization() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/back_api_deleteClassroomBookUseTypeOrganization.do")
                .param("id", "1"))
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
        if (response.getStatus() == 200) {
            String object = response.getContentAsString();
            logger.info("服务器返回：{}", object);
        } else {
            throw new RuntimeException("接口调用异常");
        }
    }

}