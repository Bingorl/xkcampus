package com.biu.wifi.campus.Listener;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.biu.wifi.campus.dao.model.User;
import com.biu.wifi.campus.result.Result;
import com.biu.wifi.campus.service.BackendUserService;
import com.biu.wifi.core.support.SpringContextLoader;
import com.biu.wifi.core.util.ServletUtilsEx;

public class PermissionInterceptor implements HandlerInterceptor {

    private final static Set<String> permissionUncheckSet = new HashSet<String>();

    static {
        permissionUncheckSet.add("/back_api_operateUserAccountStatus_s.do");
    }

    @Override
    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
            throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
            throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {

        String path = request.getServletPath();

        if (StringUtils.isNotBlank(path) && path.indexOf("/back_api") > -1) {
            if (permissionUncheckSet.contains(path)) {
                return true;
            } else {
                String userId = request.getParameter("userId");

                if (StringUtils.isNotBlank(userId)) {
                    BackendUserService backendUserService = (BackendUserService) SpringContextLoader.getBean("backendUserService");

                    User user = null;

                    try {
                        user = backendUserService.getUserById(Integer.parseInt(userId));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (user != null && user.getStatus() == 2) {
                        ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "账号被禁用", null));
                        return false;
                    } else {
                        return true;
                    }
                } else {
                    return true;
                }
            }
        } else {
            return true;
        }
    }

}
