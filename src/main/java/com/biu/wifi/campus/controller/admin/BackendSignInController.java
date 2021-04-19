package com.biu.wifi.campus.controller.admin;

import com.biu.wifi.campus.constant.SignInType;
import com.biu.wifi.campus.result.Result;
import com.biu.wifi.campus.service.SignInService;
import com.biu.wifi.core.support.pageLimit.PageLimitHolderFilter;
import com.biu.wifi.core.util.ServletUtilsEx;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author 张彬.
 * @date 2021/4/19 22:10.
 */
@Controller
public class BackendSignInController {

    @Autowired
    private SignInService signInService;

    /**
     * 获取签到类型列表
     *
     * @param
     * @return
     * @author 张彬.
     * @date 2021/4/19 22:25.
     */
    @RequestMapping("back_api_findSignTypeList")
    public void back_api_findSignTypeList(HttpServletResponse response) {
        List<HashMap> list = new ArrayList<>();
        for (SignInType signInType : SignInType.values()) {
            HashMap hashMap = new HashMap();
            hashMap.put("name", signInType.getDescription());
            hashMap.put("value", signInType.getCode());
            list.add(hashMap);
        }
        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "请求成功", list));
    }


    /**
     * 签到条件列表
     *
     * @param pageNum
     * @param pageSize
     * @param type
     * @param startTime
     * @param endTime
     * @param keyword
     * @param response
     * @return
     * @author 张彬.
     * @date 2021/4/19 22:11.
     */
    @RequestMapping("back_api_searchSignInList")
    public void back_api_searchSignInList(Integer pageNum, Integer pageSize, Integer schoolId, Integer type,
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
        List<HashMap> list = signInService.search(schoolId, type, startTime, endTime, keyword);
        for (HashMap map : list) {
            map.put("typeName", SignInType.getDescription(MapUtils.getInteger(map, "type")));
        }
        HashMap hashMap = new HashMap();
        hashMap.put("list", list);
        hashMap.put("totalCount", PageLimitHolderFilter.getContext().getTotalCount());
        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "请求成功", hashMap));
    }
}
