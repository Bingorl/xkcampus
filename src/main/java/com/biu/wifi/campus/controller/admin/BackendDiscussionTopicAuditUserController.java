package com.biu.wifi.campus.controller.admin;

import com.biu.wifi.campus.result.Result;
import com.biu.wifi.campus.service.BackendSchoolService;
import com.biu.wifi.campus.service.DiscussionTopicAuditService;
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
public class BackendDiscussionTopicAuditUserController {

    @Autowired
    private DiscussionTopicAuditService discussionTopicAuditService;

    /**
     * 查询会议议题审核人员列表
     *
     * @param instituteId 学院id
     * @param response
     * @return
     * @author 张彬.
     * @date 2021/4/18 15:53.
     */
    @RequestMapping("/back_api_findDiscussionTopicAuditUserList")
    public void back_api_findDiscussionTopicAuditUserList(Integer instituteId, HttpServletResponse response) {
        Assert.notNull(instituteId, "学院id不能为空");

        List<HashMap> list = discussionTopicAuditService.findAuditUserList(instituteId);
        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", list));
    }

    /**
     * 编辑审核人
     *
     * @param instituteId
     * @param userIds
     * @param response
     * @return
     * @author 张彬.
     * @date 2021/4/18 20:44.
     */
    @RequestMapping("/back_api_updateDiscussionTopicAuditUserList")
    public void back_api_updateDiscussionTopicAuditUserList(Integer instituteId, String userIds, HttpServletResponse response) {
        Assert.notNull(userIds, "请选择审核人");
        Assert.notNull(instituteId, "学院id不能为空");

        discussionTopicAuditService.updateDiscussionTopicAuditUserList(instituteId, userIds);
        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功"));
    }
}
