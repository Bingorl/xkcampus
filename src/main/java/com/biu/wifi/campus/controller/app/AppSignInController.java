package com.biu.wifi.campus.controller.app;

import com.biu.wifi.campus.Tool.RedisUtils;
import com.biu.wifi.campus.dao.model.SignIn;
import com.biu.wifi.campus.exception.BizException;
import com.biu.wifi.campus.service.SignInService;
import com.biu.wifi.core.support.pageLimit.PageLimitHolderFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

/**
 * @author 张彬.
 * @date 2021/4/9 16:36.
 */
@Controller
public class AppSignInController {

    @Autowired
    private SignInService signInService;

    /**
     * 签到
     *
     * @param token
     * @param type
     * @param model
     * @return
     * @author 张彬.
     * @date 2021/4/9 16:42.
     */
    @RequestMapping("app_addSignIn")
    public ModelAndView addSignIn(String token, Integer type, Model model) {
        model.addAttribute("pageType", "signIn");
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

        if (type == null) {
            model.addAttribute("msg", "请选择签到类型");
            return new ModelAndView("oa/signIn");
        }

        SignIn signIn = new SignIn();
        signIn.setUserId(Integer.valueOf(userId));
        signIn.setType(type);
        try {
            signInService.add(signIn);
            model.addAttribute("msg", "签到成功");
            model.addAttribute("signTypeList", signInService.getSignTypeList());
            model.addAttribute("signIns", signInService.mySignInList(Integer.valueOf(userId)));
            return new ModelAndView("oa/signIn");
        } catch (BizException e) {
            model.addAttribute("msg", "签到失败");
        }
        return new ModelAndView("oa/signIn");
    }
}
