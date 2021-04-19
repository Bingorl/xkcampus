package com.biu.wifi.campus.controller.app;

import com.biu.wifi.campus.Tool.RedisUtils;
import com.biu.wifi.campus.Tool.StringUtil;
import com.biu.wifi.campus.Tool.StringUtilEx;
import com.biu.wifi.campus.dao.model.TemperatureReport;
import com.biu.wifi.campus.dao.model.User;
import com.biu.wifi.campus.exception.BizException;
import com.biu.wifi.campus.service.SignInService;
import com.biu.wifi.campus.service.TemperatureReportService;
import com.biu.wifi.campus.service.UserService;
import com.biu.wifi.core.support.pageLimit.PageLimitHolderFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;


/**
 * @author 张彬.
 * @date 2021/4/9 16:03.
 */
@Controller
public class AppTemperatureReportController {

    @Autowired
    private TemperatureReportService temperatureReportService;
    @Autowired
    private UserService userService;
    @Autowired
    private SignInService signInService;

    /**
     * 跳转登录页面
     *
     * @param pageType
     * @param model
     * @return
     * @author 张彬.
     * @date 2021/4/9 23:33.
     */
    @RequestMapping("app_toLogin")
    public ModelAndView toLogin(String pageType, Model model) {
        model.addAttribute("msg", "");
        model.addAttribute("pageType", pageType);
        return new ModelAndView("oa/login");
    }

    /**
     * 登录操作
     *
     * @param username
     * @param password
     * @param pageType
     * @param model
     * @param request
     * @param response
     * @return
     * @author 张彬.
     * @date 2021/4/9 23:12.
     */
    @RequestMapping("app_doLogin")
    public ModelAndView doLogin(String username, String password, String pageType, Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = userService.getUserByStuNumber(username);
        if (!user.getPassword().equals(StringUtil.MD5Encode(password + user.getSalt()))) {
            model.addAttribute("msg", "账号密码错误");
            return new ModelAndView("oa/login");
        }

        String loginKey = StringUtilEx.produceToken(new Date());
        // 放入redis
        RedisUtils.addValue(loginKey, String.valueOf(user.getId()), 30 * 24 * 60 * 60);
        model.addAttribute("token", loginKey);
        model.addAttribute("pageType", pageType);
        model.addAttribute("msg", "");
        if ("signIn".equals(pageType)) {
            model.addAttribute("signTypeList", signInService.getSignTypeList());
            model.addAttribute("signIns", signInService.mySignInList(user.getId()));
            return new ModelAndView("oa/signIn");
        } else {
            model.addAttribute("temperatureReports", temperatureReportService.myTemperatureReportMapList(user.getId()));
            return new ModelAndView("oa/temperatureReport");
        }
    }

    /**
     * 体温上报
     *
     * @param token
     * @param req
     * @param model
     * @param response
     * @return
     * @author 张彬.
     * @date 2021/4/9 16:05.
     */
    @RequestMapping("app_addTemperatureReport")
    public ModelAndView add(String token, TemperatureReport req, Model model, HttpServletResponse response) {
        model.addAttribute("pageType", "temperatureReport");
        model.addAttribute("token", token);
        if (token == null) {
            model.addAttribute("msg", "请先登录");
            return new ModelAndView("oa/login");
        }

        // 从redis中获取信息
        String userId = RedisUtils.getValueByKey(token);
        if (userId == null) {
            model.addAttribute("msg", "请先登录");
            return new ModelAndView("oa/login");
        }

        if (req.getTemperature() == null) {
            model.addAttribute("msg", "体温不能为空");
            return new ModelAndView("oa/temperatureReport");
        }

        req.setUserId(Integer.valueOf(userId));
        try {
            temperatureReportService.add(req);
            model.addAttribute("msg", "上报成功");
            model.addAttribute("temperatureReports", temperatureReportService.myTemperatureReportMapList(Integer.valueOf(userId)));
        } catch (BizException e) {
            model.addAttribute("msg", "体温上报失败");
        }
        return new ModelAndView("oa/temperatureReport");
    }
}
