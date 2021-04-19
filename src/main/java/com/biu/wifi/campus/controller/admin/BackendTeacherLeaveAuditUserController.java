package com.biu.wifi.campus.controller.admin;

import com.biu.wifi.campus.exception.BizException;
import com.biu.wifi.campus.result.Result;
import com.biu.wifi.campus.service.BackendSchoolService;
import com.biu.wifi.campus.service.TeacherLeaveAuditUserService;
import com.biu.wifi.core.util.ServletUtilsEx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

/**
 * @author 张彬.
 * @date 2021/4/18 15:52.
 */
@Controller
public class BackendTeacherLeaveAuditUserController {

    // 3天以下
    private static final String LESS_THEN_THREE_DAYS = "LESS_THEN_THREE_DAYS";
    // 3-7天
    private static final String BETWEEN_THREE_AND_SEVEN_DAYS = "BETWEEN_THREE_AND_SEVEN_DAYS";
    // 7天以上
    private static final String GREAT_THEN_SEVEN_DAYS = "GREAT_THEN_SEVEN_DAYS";

    @Autowired
    private BackendSchoolService schoolService;
    @Autowired
    private TeacherLeaveAuditUserService teacherLeaveAuditUserService;

    private Short getType(String code) {
        Short type;
        switch (code) {
            case LESS_THEN_THREE_DAYS:
                type = 1;
                break;
            case BETWEEN_THREE_AND_SEVEN_DAYS:
                type = 2;
                break;
            case GREAT_THEN_SEVEN_DAYS:
                type = 3;
                break;
            default:
                throw new BizException(Result.CUSTOM_MESSAGE, "请假类型不能为空");
        }
        return type;
    }

    /**
     * 查询请假审核人员列表
     *
     * @param instituteId 学院id
     * @param code        请假类型
     * @param response
     * @return
     * @author 张彬.
     * @date 2021/4/18 15:53.
     */
    @RequestMapping("/back_api_findTeacherLeaveAuditUserList")
    public void back_api_findTeacherLeaveAuditUserList(Integer instituteId, String code, HttpServletResponse response) {
        Assert.notNull(instituteId, "学院id不能为空");
        Assert.notNull(code, "请假类型不能为空");

        List<HashMap> list = teacherLeaveAuditUserService.findTeacherLeaveAuditUserList(instituteId, getType(code));
        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", list));
    }

    /**
     * 编辑审核人
     *
     * @param instituteId
     * @param code
     * @param userIds
     * @param response
     * @return
     * @author 张彬.
     * @date 2021/4/18 20:44.
     */
    @RequestMapping("/back_api_updateTeacherLeaveAuditUserList")
    public void back_api_updateTeacherLeaveAuditUserList(Integer instituteId, String code, String userIds, HttpServletResponse response) {
        Assert.notNull(userIds, "请选择审核人");
        Assert.notNull(code, "请假类型不能为空");
        Assert.notNull(instituteId, "学院id不能为空");

        teacherLeaveAuditUserService.add(instituteId, getType(code), userIds);
        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功"));
    }
}
