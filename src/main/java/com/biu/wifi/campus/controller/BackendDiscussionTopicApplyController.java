package com.biu.wifi.campus.controller;

import com.biu.wifi.campus.controller.app.AppDiscussionTopicApplyController;
import com.biu.wifi.campus.result.Result;
import com.biu.wifi.campus.service.DiscussionTopicApplyService;
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
 * @date 2021/4/19 20:55.
 */
@Controller
public class BackendDiscussionTopicApplyController {

    @Autowired
    private DiscussionTopicApplyService discussionTopicApplyService;
    @Autowired
    private AppDiscussionTopicApplyController appDiscussionTopicApplyController;

    /**
     * 查看会议议题申请详情
     *
     * @param applyId
     * @param response
     */
    @RequestMapping("back_api_discussionTopicApplyDetail")
    public void detail(Integer applyId, HttpServletResponse response) {
        appDiscussionTopicApplyController.detail(null, applyId, response);
    }

    /**
     * 会议议题申请条件列表
     *
     * @param pageNum
     * @param pageSize
     * @param schoolId
     * @param type
     * @param status
     * @param startTime
     * @param endTime
     * @param response
     * @return
     * @author 张彬.
     * @date 2021/4/19 20:58.
     */
    @RequestMapping("back_api_searchDiscussionTopicApplyList")
    public void back_api_searchDiscussionTopicApplyList(Integer pageNum, Integer pageSize, Integer schoolId, Integer type, Short status,
                                                        String startTime, String endTime, String keyword, HttpServletResponse response) {
        Assert.notNull(schoolId, "学校id不能为空");
        Assert.notNull(type, "类型不能为空");
        Assert.notNull(pageNum, "页码不能为空");
        Assert.notNull(pageSize, "每页记录数不能为空");

        if (StringUtils.isNotBlank(startTime)) {
            startTime += " 00:00:00";
        }
        if (StringUtils.isNotBlank(endTime)) {
            endTime += " 23:59:59";
        }

        PageLimitHolderFilter.setContext(pageNum, pageSize, null);
        List<HashMap> list = discussionTopicApplyService.search(schoolId, type, status, startTime, endTime, keyword);
        HashMap hashMap = new HashMap();
        hashMap.put("list", list);
        hashMap.put("totalCount", PageLimitHolderFilter.getContext().getTotalCount());
        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "请求成功", hashMap));
    }
}
