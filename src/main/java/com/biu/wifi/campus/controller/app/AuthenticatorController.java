package com.biu.wifi.campus.controller.app;

import com.biu.wifi.campus.Tool.AccessTokenUtils;
import com.biu.wifi.campus.Tool.PasswordUtil;
import com.biu.wifi.campus.Tool.RedisUtils;
import com.biu.wifi.campus.Tool.SpringContextUtils;
import com.biu.wifi.campus.controller.admin.BaseController;
import com.biu.wifi.campus.dao.model.User;
import com.biu.wifi.campus.exception.NeedLoginException;
import com.biu.wifi.campus.exception.SignatureNoSuccessException;
import com.biu.wifi.campus.service.UserService;
import com.biu.wifi.core.support.servlet.ServletHolderFilter;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@Controller
public class AuthenticatorController extends BaseController {

    @ModelAttribute
    public void getUserId(HttpServletRequest request, HttpServletResponse response, Model model)
            throws NoSuchAlgorithmException {
        logger.info("进入到AuthenticatorController");
        RedisUtils.addNumIncrement("test_redis_config");

        Map<String, Object> param = ServletHolderFilter.getContext().getParamMap();
        // 验证签名
        String device_id = param.get("device_id").toString();
        String channel = param.get("channel").toString();
        String version = param.get("version").toString();
        String signature = param.get("signature").toString();
        String temp;
        // 获取签名规则
        String type = signature.substring(signature.length() - 1);
        if ("1".equals(channel)) {
            // IOS
            if ("1".equals(type)) {
                temp = PasswordUtil.md5(
                        device_id + channel + version + device_id.substring(device_id.length() - 5))
                        + "1";
            } else if ("2".equals(type)) {
                temp = PasswordUtil.md5(
                        device_id.substring(device_id.length() - 5) + device_id + channel + version)
                        + "2";
            } else {
                temp = PasswordUtil.md5(
                        device_id + device_id.substring(device_id.length() - 5) + channel + version)
                        + "3";
            }
        } else {
            // android
            if ("1".equals(type)) {
                temp = PasswordUtil.md5(device_id.substring(0, 5) + device_id + channel + version) + "1";
            } else if ("2".equals(type)) {
                temp = PasswordUtil.md5(device_id + channel + version + device_id.substring(0, 5)) + "2";
            } else {
                temp = PasswordUtil.md5(device_id + channel + device_id.substring(0, 5) + version) + "3";
            }
        }
        if (!temp.equals(signature)) {
            throw new SignatureNoSuccessException("验签失败");
        }

        String url = request.getRequestURI();
        //传递版本号
        model.addAttribute("version", version);

        // // TODO: 张彬 2021/6/3 与泊头一站式服务大厅共享token
        String token = param.get("token").toString();
        logger.info("获取到的token为：{}", token);
        // 从token中获取用户账号信息
        String username = null;
        try {
            username = AccessTokenUtils.getValueFromAccessToken(token, "username");

        } catch (Exception e) {
            logger.error("解析token异常：{}", e);
            throw new NeedLoginException("用户未登录");
        }
        User user = getUserByUsername(username);
        if (checkNeedAuthority(url)) {
            if (StringUtils.isBlank(username)) {
                logger.error("username is null");
                throw new NeedLoginException("用户未登录");
            } else {
                if (user == null) {
                    logger.error("账号{}不存在", username);
                    throw new NeedLoginException("用户未登录");
                } else {
                    // 往model里面添加用户ID
                    model.addAttribute("user_id", user.getId());
                }
            }
        } else {
            if (user == null) {
                model.addAttribute("user_id", null);
            } else {
                model.addAttribute("user_id", user.getId());
            }
        }

        // TODO: 张彬 2021/6/4 9:11 通过本系统的登录实现的token验证
        /*if (checkNeedAuthority(url)) {
            try {
                String token = param.get("token").toString();
                if (StringUtils.isNotBlank(token)) {
                    System.out.println("存在token");
                    // 从redis中获取信息
                    String user_id = RedisUtils.getValueByKey(token);
                    if (StringUtils.isBlank(user_id)) {
                        throw new NeedLoginException("用户未登录");
                    } else {
                        // 往model里面添加用户ID
                        model.addAttribute("user_id", Integer.valueOf(user_id));
                    }
                } else {
                    throw new NeedLoginException("用户未登录");
                }
            } catch (Exception e) {
                throw new NeedLoginException("用户未登录");
            }
        } else {
            try {
                String token = param.get("token").toString();
                if (StringUtils.isNotBlank(token)) {
                    // 从redis中获取信息
                    String user_id = RedisUtils.getValueByKey(token);
                    if (StringUtils.isBlank(user_id)) {
                        model.addAttribute("user_id", null);
                    } else {
                        model.addAttribute("user_id", Integer.valueOf(user_id));
                    }
                } else {
                    model.addAttribute("user_id", null);
                }
            } catch (Exception e) {
                model.addAttribute("user_id", null);
            }
        }*/
    }

    private User getUserByUsername(String username) {
        UserService userService = (UserService) SpringContextUtils.getBean("userService");
        User uEntity = new User();
        uEntity.setPhone(username);
        uEntity.setIsDelete((short) 2);
        User user = userService.getUser(uEntity);
        return user;
    }

    public boolean checkNeedAuthority(String url) {
        System.out.println(url);
        return url.indexOf("user") != -1;
    }

    /**
     * 将字符串版本号转成int
     * <p>
     * 如：V1.3 -> 1.3
     *
     * @param version
     * @return
     */
    public double convertVersionToDouble(String version) {
        version = version.substring(1);
        Double d = Double.valueOf(version);
        return d.doubleValue();
    }
}
