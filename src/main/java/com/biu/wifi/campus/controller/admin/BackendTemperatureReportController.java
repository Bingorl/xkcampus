package com.biu.wifi.campus.controller.admin;

import com.biu.wifi.campus.result.Result;
import com.biu.wifi.campus.service.TemperatureReportService;
import com.biu.wifi.core.support.pageLimit.PageLimitHolderFilter;
import com.biu.wifi.core.util.ServletUtilsEx;
import org.apache.commons.collections.MapUtils;
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
 * @date 2021/4/19 22:58.
 */
@Controller
public class BackendTemperatureReportController {

    @Autowired
    private TemperatureReportService temperatureReportService;

    /**
     * 体温上报条件列表
     *
     * @param pageNum
     * @param pageSize
     * @param schoolId
     * @param type      0全部1正常2异常
     * @param startTime
     * @param endTime
     * @param keyword
     * @param response
     * @return
     * @author 张彬.
     * @date 2021/4/19 22:59.
     */
    @RequestMapping("back_api_findTemperatureReportList")
    public void back_api_findTemperatureReportList(Integer pageNum, Integer pageSize, Integer schoolId, Integer type,
                                                   String startTime, String endTime, String keyword, HttpServletResponse response) {
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
        List<HashMap> list = temperatureReportService.search(schoolId, type, startTime, endTime, keyword);
        for (HashMap hashMap : list) {
            if (MapUtils.getDouble(hashMap, "temperature").compareTo(Double.valueOf(37)) == 1) {
                hashMap.put("waring", "异常");
            } else {
                hashMap.put("waring", "正常");
            }
        }
        HashMap hashMap = new HashMap();
        hashMap.put("list", list);
        hashMap.put("totalCount", PageLimitHolderFilter.getContext().getTotalCount());
        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "请求成功", hashMap));
    }
}
