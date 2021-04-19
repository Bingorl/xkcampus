package com.biu.wifi.core.support.session;

import com.biu.wifi.core.CoreConstants;
import com.biu.wifi.core.invoke.controller.ResultEntity;
import com.biu.wifi.core.util.ServletUtilsEx;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 把session信息放入threadLocal
 *
 * @author TYOTANN
 */
public class SessionHolderFilter implements Filter {

    protected Log logger = LogFactory.getLog(getClass());

    private final static ThreadLocal<SessionInfo> threadLocal = new ThreadLocal<SessionInfo>();

    private final static Set<String> sessionUncheckSet = new HashSet<String>();

    static {

        for (String key : CoreConstants.SESSION_UNCHECK) {

            if (StringUtils.isNotBlank(key)) {
                sessionUncheckSet.add(key.trim());
            }
        }

        // 默认框架使用，不检查session的方法
        {
            sessionUncheckSet.add("/healthCheck.do");
            sessionUncheckSet.add("/uploadFile.do");
            sessionUncheckSet.add("/uploadFileText.do");
            sessionUncheckSet.add("/downloadFile.do");
            sessionUncheckSet.add("/oldDownloadFile.do");
            sessionUncheckSet.add("/jcaptcha.do");
            sessionUncheckSet.add("/session_out.do");
            sessionUncheckSet.add("/login.do");
            sessionUncheckSet.add("/weixin.do");
            sessionUncheckSet.add("/upFile.do");
            sessionUncheckSet.add("/apk_download.do");
            sessionUncheckSet.add("/pushMessageToUser.do");
            sessionUncheckSet.add("/importNews.do");
            sessionUncheckSet.add("/findNewsList.do");
            sessionUncheckSet.add("/findNewsDetails.do");
            sessionUncheckSet.add("/test.do");
            sessionUncheckSet.add("/app_toLogin.do");
            sessionUncheckSet.add("/app_toTemperatureReport.do");
            sessionUncheckSet.add("/app_toSignIn.do");
        }

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//		((HttpServletResponse) response).setHeader("Access-Control-Allow-Origin", "*");
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = ((HttpServletResponse) response);
        resp.setHeader("Access-Control-Allow-Origin", req.getHeader("Origin"));
        resp.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        resp.setHeader("Access-Control-Max-Age", "0");
        resp.setHeader("Access-Control-Allow-Headers", "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token,Access-Control-Allow-Headers");
        resp.setHeader("Access-Control-Allow-Credentials", "true");
        resp.setHeader("XDomainRequestAllowed", "1");


        try {

            setContext(SessionContext.getSessionInfoByReq(req));

            if (getContext() == null) {

                if (sessionCheck(req)) {

                    String sessionoutPage = CoreConstants.SESSION_OUTPAGE.indexOf("http") == 0 ? CoreConstants.SESSION_OUTPAGE : ServletUtilsEx.getHostURLWithContextPath((HttpServletRequest) request) + "/" + CoreConstants.SESSION_OUTPAGE;

                    logger.debug("用户session失效，跳转到页面：" + sessionoutPage);

                    // 如果是请求的service
                    if (((HttpServletRequest) request).getServletPath().indexOf("invoke.do") > -1) {

                        Map<String, String> data = new HashMap<String, String>();
                        data.put("sessionoutPage", sessionoutPage);
                        ServletUtilsEx.renderJson((HttpServletResponse) response, new ResultEntity("-98", "session失效", data));
                    } else {
                        ((HttpServletResponse) response).sendRedirect(sessionoutPage);
                    }

                    return;
                }
            }

            chain.doFilter(request, response);
        } finally {
            setContext(null);
        }
    }

    /**
     * <pre>
     * 首页面与首页面调用的服务(LoginService)不进行session检查
     * </pre>
     *
     * @param request
     * @return
     */
    private boolean sessionCheck(HttpServletRequest request) {

        String reqPath = ((HttpServletRequest) request).getServletPath();

        if (sessionUncheckSet.contains(reqPath)) {
            return false;
        }

        if (reqPath.indexOf("user") > -1) {
            //手机接口不检测session
            return false;
        }

        if (reqPath.indexOf("app") > -1) {
            //手机接口不检测session
            return false;
        }

        if (reqPath.indexOf("back") > -1) {
            //后台接口不检测session
            return false;
        }

        if (reqPath.indexOf("invoke.do") > -1) {
            // 下载模式不检查session
            if ("2".equals(request.getParameter("FRAMEactionMode"))) {
                return false;
            }
            // 显式设置不检查session的话,不做session检查,否则都做检查
            if ("false".equals(request.getParameter("FRAMEsessionCheck"))) {
                return false;
            }
        }

        return true;
    }

    private static void setContext(SessionInfo sc) {
        threadLocal.set(sc);
    }

    public static SessionInfo getContext() {
        return threadLocal.get();
    }

    public void destroy() {
    }

    public void init(FilterConfig arg0) throws ServletException {
    }

}
