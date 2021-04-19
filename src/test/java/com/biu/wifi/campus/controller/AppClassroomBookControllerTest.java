package com.biu.wifi.campus.controller;

import com.biu.wifi.campus.BaseJunit;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * @author zhangbin.
 * @date 2018/12/10.
 */
public class AppClassroomBookControllerTest extends BaseJunit {

    @Test
    public void app_findClassroomBuildingList() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/app_findClassroomBuildingList.do")
                .param("schoolId", "26"))
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
        logger.info("服务器返回：{}", response.getContentAsString());
    }

    @Test
    public void app_findClassroomList() throws Exception {
        /*MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/app_findClassroomList.do")
                .param("user_id", "119")
                .param("buildingId", "4")
                .param("bookDateStr", "2018-12-12")
                .param("bookPeriod", "10:45"))
                .andReturn();*/
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/app_findClassroomList.do")
                .param("classroomBookId", "21")
                .param("user_id", "119")
                .param("buildingId", "4")
                .param("bookDateStr", "2018-12-12")
                .param("bookPeriod", "10:45"))
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
        logger.info("服务器返回：{}", response.getContentAsString());
    }

    @Test
    public void app_addClassroomBook() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/app_addOrUpdateClassroomBook.do")
                .param("classroomBuildingId", "4")
                .param("bookDate", "2018-12-12")
                .param("bookPeriod", "10:45")
                .param("classroomNo", "A104")
                .param("adjustClassroomNo", "")
                .param("useTypeId", "1")
                .param("organizationId", "1")
                .param("title", "学生会活动")
                .param("content", "学生会活动")
                .param("isUseMedia", "2")
                .param("userId", "119")
                .param("linkMan", "张彬")
                .param("linkPhone", "15861626212")
                .param("linkManNo", "15861626212")
                .param("remark", ""))
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
        logger.info("服务器返回：{}", response.getContentAsString());
    }

    @Test
    public void app_updateClassroomBook() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/app_updateClassroomBook.do")
                .param("schoolId", "26")
                .param("classroomBookId", "1")
                .param("adjustClassroomNo", "A105,A106")
                .param("remark", ""))
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
        logger.info("服务器返回：{}", response.getContentAsString());
    }

    @Test
    public void app_app_findClassroomBookList() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/app_app_findClassroomBookList.do")
                .param("user_id", "119")
                .param("page", "1"))
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
        logger.info("服务器返回：{}", response.getContentAsString());
    }

    @Test
    public void app_classroomBookDetail() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/app_classroomBookDetail.do")
                .param("schoolId", "26")
                .param("classroomBookId", "1"))
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
        logger.info("服务器返回：{}", response.getContentAsString());
    }

    @Test
    public void app_findClassroomBookUseTypeOrganizationList() throws Exception {

    }

    @Test
    public void app_findClassroomBookUseTypeOrganizationAuditUserList() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/app_findClassroomBookUseTypeOrganizationAuditUserList.do")
                .param("schoolId", "26")
                .param("useTypeId", "1"))
                .andReturn();
        logger.info(result.getResponse().getContentAsString());
    }

    @Test
    public void app_addOrUpdateClassroomBookUseType() throws Exception {

    }

    @Test
    public void app_findClassroomBookUseTypeList() throws Exception {

    }

    @Test
    public void app_findClassroomBookUseTypeListByOrganizationId() throws Exception {

    }

    @Test
    public void app_addTeachingCourseTimePlanList() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/app_addTeachingCourseTimePlanList.do")
                .param("schoolId", "26"))
                .andReturn();
        logger.info(result.getResponse().getContentAsString());
    }

}