package com.biu.wifi.campus.controller.admin;

import com.biu.wifi.campus.controller.app.AppTeacherLeaveInfoController;
import com.biu.wifi.campus.result.Result;
import com.biu.wifi.campus.service.TeacherLeaveInfoService;
import com.biu.wifi.core.support.pageLimit.PageLimitHolderFilter;
import com.biu.wifi.core.util.ServletUtilsEx;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

/**
 * @author 张彬.
 * @date 2021/4/19 16:41.
 */
@Controller
public class BackendTeacherLeaveInfoController {

    @Autowired
    private TeacherLeaveInfoService teacherLeaveInfoService;
    @Autowired
    private AppTeacherLeaveInfoController appTeacherLeaveInfoController;

    /**
     * 教师请假类型列表
     *
     * @param response
     * @return
     * @author 张彬.
     * @date 2021/4/7 0:24.
     */
    @RequestMapping("back_api_teacherLeaveInfoTypeList")
    public void teacherLeaveInfoTypeList(HttpServletResponse response) {
        appTeacherLeaveInfoController.teacherLeaveInfoTypeList(response);
    }

    /**
     * 查看请假单详情
     *
     * @param leaveId
     * @param response
     */
    @RequestMapping("back_api_teacherLeaveInfoDetail")
    public void detail(Integer leaveId, HttpServletResponse response) {
        appTeacherLeaveInfoController.detail(null, leaveId, response);
    }

    /**
     * 请假条件列表
     *
     * @param pageNum
     * @param pageSize
     * @param schoolId
     * @param leaveType
     * @param status
     * @param keyword
     * @param startTime
     * @param endTime
     * @return
     * @author 张彬.
     * @date 2021/4/19 16:45.
     */
    @RequestMapping("back_api_searchTeacherLeaveInfoList")
    public void back_api_searchTeacherLeaveInfoList(Integer pageNum, Integer pageSize, Integer schoolId, Integer leaveType,
                                                    Short status, String keyword, String startTime, String endTime, HttpServletResponse response) {
        Assert.notNull(schoolId, "学校id不能为空");
        Assert.notNull(pageNum, "页码不能为空");
        Assert.notNull(pageSize, "每页记录数不能为空");

        if (StringUtils.isNotBlank(startTime)) {
            startTime += " 00:00:00";
        }
        if (StringUtils.isNotBlank(endTime)) {
            endTime += " 23:59:59";
        }

        PageLimitHolderFilter.setContext(pageNum, pageSize, null);
        List<HashMap> list = teacherLeaveInfoService.search(schoolId, leaveType, status, keyword, startTime, endTime);
        HashMap hashMap = new HashMap();
        hashMap.put("list", list);
        hashMap.put("totalCount", PageLimitHolderFilter.getContext().getTotalCount());
        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "请求成功", hashMap));
    }
}
