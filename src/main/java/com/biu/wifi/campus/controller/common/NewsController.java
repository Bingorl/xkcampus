package com.biu.wifi.campus.controller.common;

import com.biu.wifi.campus.Tool.JsonUtilEx;
import com.biu.wifi.campus.Tool.TimeUtil;
import com.biu.wifi.campus.dao.model.News;
import com.biu.wifi.campus.dto.NewsDto;
import com.biu.wifi.campus.result.Result;
import com.biu.wifi.campus.service.NewsService2;
import com.biu.wifi.core.support.pageLimit.PageLimitHolderFilter;
import com.biu.wifi.core.util.ServletUtilsEx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangbin.
 * @date 2019/3/12.
 */
@Controller
public class NewsController {

    @Autowired
    private NewsService2 newsService;

    /**
     * 导入新闻
     *
     * @param schoolId
     * @param response
     */
    @RequestMapping("/importNews")
    public void importNews(Integer schoolId, HttpServletResponse response) throws IOException {
        if (schoolId == null) {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数")));
            return;
        }

        newsService.importNews(schoolId);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功")));
    }

    /**
     * 新闻列表
     *
     * @param pageNo    页码(非必填)
     * @param pageSize  每页记录数(非必填)
     * @param schoolId  学校id
     * @param keyword   新闻标题关键字(非必填)
     * @param startTime 发布起始时间(非必填)
     * @param endTime   发布截止时间(非必填)
     * @param response
     */
    @RequestMapping("/findNewsList")
    public void findNewsList(@RequestParam(defaultValue = "1", required = false) Integer pageNo,
                             @RequestParam(defaultValue = "15", required = false) Integer pageSize,
                             @RequestParam Integer schoolId,
                             @RequestParam(required = false) String keyword,
                             @RequestParam(required = false) String startTime,
                             @RequestParam(required = false) String endTime, HttpServletResponse response) {
        if (schoolId == null) {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数")));
            return;
        }

        PageLimitHolderFilter.setContext(pageNo, pageSize, null);
        List<News> newsList = newsService.newsList(schoolId, keyword, startTime, endTime);
        List<Map> mapList = new ArrayList<>();
        for (News news : newsList) {
            Map map = new HashMap(BeanMap.create(news));
            map.put("publishTime", TimeUtil.format(news.getPublishTime(), "yyyy-MM-dd"));
            mapList.add(map);
        }

        Map data = new HashMap();
        data.put("list", mapList);
        data.put("totalCount", PageLimitHolderFilter.getContext().getTotalCount());
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", data)));
    }

    /**
     * 新闻详情
     *
     * @param id       新闻id
     * @param schoolId 学校id
     * @param request
     * @param response
     */
    @RequestMapping("/findNewsDetails")
    public void findNewsDetails(Integer id, Integer schoolId, HttpServletRequest request, HttpServletResponse response) {
        if (id == null || schoolId == null) {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数")));
            return;
        }

        News current = newsService.findById(id, schoolId);
        newsService.addClickCount(current, request);
        News next = newsService.findNextByPreNews(current);
        NewsDto newsDto = new NewsDto();
        newsDto.setCurrent(current);
        newsDto.setNext(next);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", newsDto)));
    }
}
