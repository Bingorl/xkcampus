package com.biu.wifi.campus.controller.app;

import com.biu.wifi.campus.Tool.*;
import com.biu.wifi.campus.dao.model.AccountOnline;
import com.biu.wifi.campus.dao.model.User;
import com.biu.wifi.campus.dao.model.UserAuth;
import com.biu.wifi.campus.result.Result;
import com.biu.wifi.campus.service.*;
import com.biu.wifi.component.datastore.FileSupportService;
import com.biu.wifi.component.huanxin.HuanXinService;
import com.biu.wifi.core.CoreConstants;
import com.biu.wifi.core.support.cache.CacheSupport;
import com.biu.wifi.core.support.servlet.ServletHolderFilter;
import com.biu.wifi.core.util.FileUtilsEx;
import com.biu.wifi.core.util.ServletUtilsEx;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AppUserController extends AuthenticatorController {

    @Autowired
    private UserService userService;
    @Autowired
    private AccountOnlineService accountOnlineService;
    @Autowired
    private SMSToolService smsToolService;
    @Autowired
    private HuanXinService huanXinService;
    @Autowired
    private FileSupportService fileService;
    @Autowired
    private UserAuthService userAuthService;
    @Autowired
    private LeaveInfoService leaveInfoService;
    @Autowired
    private JwCjcxService jwCjcxService;

    /**
     * 登录
     *
     * @param response
     * @param mobile
     * @param password
     */
    @RequestMapping("/app_login")
    public void app_login(@ModelAttribute("version") String version, HttpServletResponse response, String mobile, String password) {
        if (StringUtils.isBlank(mobile) || StringUtils.isBlank(password)) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
            return;
        }

        User uEntity = new User();
        uEntity.setPhone(mobile);
        uEntity.setIsDelete((short) 2);
        User user = userService.getUser(uEntity);
        if (null == user) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "账号不存在", null)));
            return;
        }
        // 解密移动端Base64传过来的密码,进来比对
        password = BaseUtil.getFromBase64(password);
        if (!user.getPassword().equals(StringUtil.MD5Encode(password + user.getSalt()))) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "账号或者密码错误", null)));
            return;
        }
        if (user.getStatus().shortValue() == 2) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "账号被禁用,暂不能登录!", null)));
            return;
        }
        // 用户在线信息表
        AccountOnline aoEntity = new AccountOnline();
        aoEntity.setAccountId(user.getId());
        // 获取信息
        List<AccountOnline> list = accountOnlineService.findList(aoEntity);
        if (list != null && list.size() > 0) {
            for (AccountOnline online : list) {
                // 删除redis
                RedisUtils.delKey(online.getOnlineKey());
            }
        }
        // 删除存在的用户登录token记录
        accountOnlineService.delete(aoEntity);
        // 生成token
        Date now = new Date();
        String loginkey = StringUtilEx.produceToken(now);
        long saveTime = StringUtilEx.save_time;
        // 用户在线信息表
        AccountOnline accountOnline = new AccountOnline();
        accountOnline.setAccountId(user.getId());
        accountOnline.setOnlineKey(loginkey);
        accountOnline.setEdatetime(now.getTime() + saveTime * 1000);
        accountOnline.setSdatetime(new BigDecimal(now.getTime()).longValue());
        accountOnline.setType((short) 1);
        accountOnline.setLengthen((int) saveTime);
        accountOnlineService.insert(accountOnline);

        // 放入redis
        RedisUtils.addValue(loginkey, String.valueOf(user.getId()), 30 * 24 * 60 * 60);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("token", loginkey);
        map.put("userId", user.getId());
        UserAuth uaEntity = new UserAuth();
        uaEntity.setUserId(user.getId());
        List<UserAuth> userAuthList = userAuthService.findUserAuthList(uaEntity);
        int status = 0;
        if (userAuthList.size() == 0) {
            status = 0;
        } else {
            status = userAuthList.get(0).getStatus();
        }
        map.put("is_auth", status);

        if (status == 3) {
            map.put("reason", userAuthList.get(0).getReason());
        }

        if (convertVersionToDouble(version) >= 1.3) {
            //判断用户是否有待办审核
            boolean toDoAudit = user.getIsTeacher() == 1 ? true : false;
            map.put("toDoAudit", toDoAudit);
        }

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", map)));
    }

    /**
     * 验证码验证
     *
     * @param response
     * @param type
     * @param user_id
     * @param mobile
     * @param mobile_verify
     */
    @RequestMapping("/app_checkSendCode")
    public void app_checkSendCode(HttpServletResponse response, Integer type,
                                  @ModelAttribute("user_id") Integer user_id, String mobile, String mobile_verify) {
        if (type == null || StringUtils.isBlank(mobile_verify)) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
            return;
        }
        if (type.intValue() == 1) {
            // 忘记密码,校验手机号是否传了
            if (StringUtils.isBlank(mobile)) {
                ServletUtilsEx.renderText(response,
                        JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
                return;
            }
            if (!CheckPhone.isPhone(mobile)) {
                ServletUtilsEx.renderText(response,
                        JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "手机号码不合法", null)));
                return;
            }
            User uEntity = new User();
            uEntity.setPhone(mobile);
            uEntity.setIsDelete((short) 2);
            User user = userService.getUser(uEntity);
            if (null == user) {
                ServletUtilsEx.renderText(response,
                        JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "账号不存在", null)));
                return;
            }
            if (user.getStatus().shortValue() == 2) {
                ServletUtilsEx.renderText(response,
                        JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "账号被禁用,暂不能找回密码!", null)));
                return;
            }
            // 获取验证码
            Integer mobileCode = CacheSupport.get("code", mobile, Integer.class);
            if (mobileCode == null || mobileCode.intValue() != Integer.parseInt(mobile_verify)) {
                ServletUtilsEx.renderText(response,
                        JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "验证码错误", null)));
            } else {
                ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", null)));
            }
        } else {
            // 修改密码
            if (null == user_id) {
                ServletUtilsEx.renderText(response,
                        JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
                return;
            }
            User uEntity = new User();
            uEntity.setId(user_id);
            uEntity.setIsDelete((short) 2);
            User user = userService.getUser(uEntity);
            if (null == user) {
                ServletUtilsEx.renderText(response,
                        JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "账号不存在", null)));
                return;
            }
            if (user.getStatus().shortValue() == 2) {
                ServletUtilsEx.renderText(response,
                        JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "账号被禁用,暂不能找回密码!", null)));
                return;
            }
            // 获取验证码
            Integer mobileCode = CacheSupport.get("code", user.getPhone(), Integer.class);
            if (mobileCode == null || mobileCode.intValue() != Integer.parseInt(mobile_verify)) {
                ServletUtilsEx.renderText(response,
                        JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "验证码错误", null)));
            } else {
                ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", null)));
            }
        }
    }

    /**
     * 忘记密码/修改密码
     *
     * @param response
     * @param mobile
     * @param password
     * @param mobile_verify
     */
    @RequestMapping("/app_findPassword")
    public void app_findPassword(HttpServletResponse response, String mobile, String password, Integer type,
                                 @ModelAttribute("user_id") Integer user_id) {
        if (type == null || StringUtils.isBlank(password)) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
            return;
        }

        if (type.intValue() == 1) {
            // 忘记密码
            if (StringUtils.isBlank(mobile)) {
                ServletUtilsEx.renderText(response,
                        JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
                return;
            }
            if (!CheckPhone.isPhone(mobile)) {
                ServletUtilsEx.renderText(response,
                        JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "手机号码不合法", null)));
                return;
            }
            User uEntity = new User();
            uEntity.setPhone(mobile);
            uEntity.setIsDelete((short) 2);
            User user = userService.getUser(uEntity);
            if (null == user) {
                ServletUtilsEx.renderText(response,
                        JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "账号不存在", null)));
                return;
            }
            if (user.getStatus().shortValue() == 2) {
                ServletUtilsEx.renderText(response,
                        JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "账号被禁用,暂不能找回密码!", null)));
                return;
            }
            // 解密移动端Base64传过来的密码,进来比对
            password = BaseUtil.getFromBase64(password);
            User usEntity = new User();
            usEntity.setId(user.getId());
            usEntity.setPassword(StringUtil.MD5Encode(password + user.getSalt()));
            userService.updateUser(usEntity);
        } else {
            // 修改密码
            if (null == user_id) {
                ServletUtilsEx.renderText(response,
                        JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
                return;
            }
            User uEntity = new User();
            uEntity.setId(user_id);
            uEntity.setIsDelete((short) 2);
            User user = userService.getUser(uEntity);
            if (null == user) {
                ServletUtilsEx.renderText(response,
                        JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "账号不存在", null)));
                return;
            }
            if (user.getStatus().shortValue() == 2) {
                ServletUtilsEx.renderText(response,
                        JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "账号被禁用,暂不能找回密码!", null)));
                return;
            }
            // 解密移动端Base64传过来的密码,进来比对
            password = BaseUtil.getFromBase64(password);
            User usEntity = new User();
            usEntity.setId(user.getId());
            usEntity.setPassword(StringUtil.MD5Encode(password + user.getSalt()));
            userService.updateUser(usEntity);
        }
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", null)));
    }

    /**
     * 发送验证码
     *
     * @param request
     * @param response
     * @param phone
     * @param type
     */
    @RequestMapping("/app_sendmobile")
    public void app_sendmobile(HttpServletRequest request, HttpServletResponse response, String phone, Short type) {
        if (StringUtils.isBlank(phone) || type == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
            return;
        }
        if (!CheckPhone.isPhone(phone)) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "手机号码不合法", null)));
            return;
        }
        User uEntity = new User();
        uEntity.setPhone(phone);
        uEntity.setIsDelete((short) 2);
        User user = userService.getUser(uEntity);
        if (null == user) {
            // 忘记密码
            if (type.shortValue() == 2) {
                ServletUtilsEx.renderText(response,
                        JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "账号不存在", null)));
                return;
            }
        } else {
            // 注册
            if (type.shortValue() == 1) {
                ServletUtilsEx.renderText(response,
                        JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "账号已存在", null)));
                return;
            }
        }
        int ret = 0;
        int mobile_code = (int) ((Math.random() * 9 + 1) * 100000);
        String ip = StringUtilEx.getIpAddr(request);
        System.out.println("验证码：" + mobile_code);
        // 防止手机连续发送,先把这个号码及其时间放到 cache里面
        String string = CacheSupport.get("phone", phone, String.class);
        if (StringUtils.isNotBlank(string)) {
            CacheSupport.put("phone", phone, String.valueOf(new Date().getTime()));
            long oldtime = Long.parseLong(string);
            long newtime = new Date().getTime();
            if ((int) (newtime - oldtime) / 1000 < 60) {
                // 手机号60秒之后再发
                ret = -3;
            } else {
                CacheSupport.put("phone", phone, String.valueOf(new Date().getTime()));
                if (Boolean.parseBoolean(CoreConstants.getProperty("is_really_model"))) {
                    CacheSupport.put("code", phone, mobile_code);
                    // 发送验证码
                    try {
                        // 返回-1,每天发送数量大于3条;返回1,短信发送成功并已入库
                        ret = smsToolService.sendSMS(phone, mobile_code, type, ip);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    mobile_code = 123456;
                    CacheSupport.put("code", phone, mobile_code);
                    ret = 1;
                }

            }
        } else {
            CacheSupport.put("phone", phone, String.valueOf(new Date().getTime()));
            if (Boolean.parseBoolean(CoreConstants.getProperty("is_really_model"))) {
                CacheSupport.put("code", phone, mobile_code);
                // 发送验证码
                try {
                    ret = smsToolService.sendSMS(phone, mobile_code, type, ip);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                mobile_code = 123456;
                CacheSupport.put("code", phone, mobile_code);
                ret = 1;
            }

        }

        if (ret == -3) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "号码一分钟发送次数超过限制", null)));
        } else if (ret == -1) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "号码当天发送短信超过限制", null)));
        } else if (ret == -2) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "同一ip地址发送短信超过限制", null)));
        } else {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功")));
        }
    }

    /**
     * 注册
     *
     * @param response
     * @param mobile
     * @param password
     * @param mobile_verify
     */
    @RequestMapping("/app_register")
    public void app_register(HttpServletResponse response, String mobile, String password, String mobile_verify) {
        if (StringUtils.isBlank(mobile) || StringUtils.isBlank(password) /*|| StringUtils.isBlank(mobile_verify)*/) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
            return;
        }
        /*if (!CheckPhone.isPhone(mobile)) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "手机号码不合法", null)));
            return;
        }*/
        User uEntity = new User();
        uEntity.setPhone(mobile);
        uEntity.setIsDelete((short) 2);
        User user = userService.getUser(uEntity);
        if (null != user) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "账号已存在", null)));
            return;
        }
        // 获取验证码
        /*Integer mobileCode = CacheSupport.get("code", mobile, Integer.class);
        if (mobileCode == null || mobileCode.intValue() != Integer.parseInt(mobile_verify)) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "验证码错误", null)));
            return;
        }*/
        // 获取加密盐
        String random = StringUtil.getStringRandom(6);
        // 解密移动端Base64传过来的密码,进来比对
        password = BaseUtil.getFromBase64(password);
        User usEntity = new User();
        usEntity.setFansNum(0);
        usEntity.setFocusNum(0);
        usEntity.setIsAuth((short) 2);
        usEntity.setIsDelete((short) 2);
        usEntity.setIsPushAt((short) 1);
        usEntity.setIsPushCmt((short) 1);
        usEntity.setIsPushLike((short) 1);
        usEntity.setIsPushQuestion((short) 1);
        usEntity.setNewsNum(0);
        usEntity.setPassword(StringUtil.MD5Encode(password + random));
        usEntity.setPhone(mobile);
        usEntity.setPostNum(0);
        usEntity.setSalt(random);
        usEntity.setStatus((short) 1);
        usEntity.setType((short) 1);
        usEntity.setIsTeacher((short) 2);// 注册时用户默认为学生
        usEntity.setSex((short) 1);
        usEntity.setHeadimg("/default/boy_head_img.png");// 性别默认为男

        Integer user_id = userService.addUser(usEntity);
        // String huanxin = "";
        // //注册环信
        // try {
        // huanxin = huanXinService.addImMembers(mobile,"123456", mobile);
        // } catch (Exception e) {
        //
        // e.printStackTrace();
        // }
        // //修改环信id
        // User entity1 = new User();
        // entity1.setId(user_id);
        // entity1.setHuanxinId(huanxin);
        // userService.updateUser(entity1);

        // 生成token
        Date now = new Date();
        String loginkey = StringUtilEx.produceToken(now);
        long saveTime = StringUtilEx.save_time;
        // 用户在线信息表
        AccountOnline accountOnline = new AccountOnline();
        accountOnline.setAccountId(usEntity.getId());
        accountOnline.setOnlineKey(loginkey);
        accountOnline.setEdatetime(now.getTime() + saveTime * 1000);
        accountOnline.setSdatetime(new BigDecimal(now.getTime()).longValue());
        accountOnline.setType((short) 1);
        accountOnline.setLengthen((int) saveTime);
        accountOnlineService.insert(accountOnline);
        // 注册完之后更新redis中的用户数量
        Integer user_num = null;
        if (null != RedisUtils.getValueByKey("user_num")) {
            user_num = Integer.valueOf(RedisUtils.getValueByKey("user_num").toString());
            RedisUtils.addValue("user_num", String.valueOf(user_num.intValue() + 1), null);
        } else {
            User entity = new User();
            entity.setIsDelete((short) 2);
            List<User> list = userService.findList(entity);
            RedisUtils.addValue("user_num", String.valueOf(list.size()), null);
        }

        // 放入redis
        RedisUtils.addValue(loginkey, String.valueOf(usEntity.getId()), 30 * 24 * 60 * 60);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("token", loginkey);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", map)));
    }

    /**
     * 上传推送token
     *
     * @param response
     * @param user_id
     * @param send_token
     * @param type
     * @param dev_id
     */
    @RequestMapping("/user_up_umeng")
    public void user_up_umeng(HttpServletResponse response, @ModelAttribute("user_id") Integer user_id,
                              String send_token, Short type, String dev_id) {
        if (StringUtils.isBlank(dev_id) || StringUtils.isBlank(send_token) || type == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
            return;
        }
        User uEntity = new User();
        uEntity.setId(user_id);
        uEntity.setDevId(dev_id);
        uEntity.setDevToken(send_token);
        uEntity.setDevType(type);
        uEntity.setUpdateTime(new Date());
        userService.updateUser(uEntity);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", null)));
    }

    /**
     * 退出登录
     *
     * @param response
     * @param user_id
     */
    @RequestMapping("/user_app_logout")
    public void user_app_logout(HttpServletResponse response, @ModelAttribute("user_id") Integer user_id) {
        AccountOnline accountOnline = new AccountOnline();
        accountOnline.setAccountId(user_id);
        // 获取信息
        List<AccountOnline> list = accountOnlineService.findList(accountOnline);
        if (list != null && list.size() > 0) {
            for (AccountOnline online : list) {
                // 删除redis
                RedisUtils.delKey(online.getOnlineKey());
            }
        }

        accountOnlineService.delete(accountOnline);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", null)));
    }

    /**
     * 身份认证
     *
     * @param response
     * @param user_id
     * @param version
     */
    @RequestMapping("/user_identity_auth")
    public void user_identity_auth(HttpServletResponse response, @ModelAttribute("user_id") Integer user_id,
                                   @ModelAttribute("version") String version) {
        Map<String, Object> param = ServletHolderFilter.getContext().getParamMap();
        // 用户类型标识(1-学生，2-教职工)
        Object tObject = param.get("type");
        boolean teacher = false;
        if (tObject != null) {
            // 兼容老版本的配置，老版本中只有学生注册，没有教师注册
            int type = Integer.valueOf(String.valueOf(tObject));
            if (type == 2) {
                teacher = true;
            }
        }

        Object name = param.get("name");
        Object stu_number = param.get("stu_number");
        Object sex = param.get("sex");
        Object grade_id = param.get("grade_id");
        Object school_id = param.get("school_id");
        Object institute_id = param.get("institute_id");
        Object major_id = param.get("major_id");
        Object class_id = param.get("class_id");
        List<DiskFileItem> imgList = null;
        try {
            imgList = (List<DiskFileItem>) param.get("image");
        } catch (Exception e) {
            imgList = null;
        }

        if (name == null || stu_number == null || sex == null || school_id == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
            return;
        }

        /*if (checkVersion(version)) {
            //todo 1.3版本后需要校验学号的真实性
            boolean flag = jwCjcxService.validStuNo(Integer.valueOf(school_id.toString()), stu_number.toString());
            if (!flag) {
                ServletUtilsEx.renderText(response,
                        JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "无法查询到学籍信息，请检查学号是否正确", null)));
                return;
            }
        }*/

        // 身份认证为学生时,校验学院.专业.年级.班级非空
        if (!teacher) {
            if (grade_id == null || institute_id == null || major_id == null || class_id == null || imgList == null) {
                ServletUtilsEx.renderText(response,
                        JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
                return;
            }
        }

        UserAuth uaEntity = new UserAuth();
        uaEntity.setUserId(user_id);
        List<UserAuth> userAuthList = userAuthService.findUserAuthList(uaEntity);
        if (userAuthList.size() > 0 && userAuthList.get(0).getStatus().shortValue() == 1) {
            // 原本有认证的申请,不能再次申请
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "您有认证中的申请,等待审核结果再次申请", null)));
            return;
        }

        // 获取用户信息
        User uEntity = new User();
        uEntity.setId(user_id);
        User user = userService.getUser(uEntity);
        if (user == null || user.getIsDelete().shortValue() == 1) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "账号不存在", null)));
            return;
        }

        if (user.getStatus().shortValue() == 2) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "账号被禁用", null)));
            return;
        }

        String fileid = null;
        if (imgList != null && imgList.size() > 0) {
            DiskFileItem fileBean = imgList.get(0);
            byte[] fileContent = fileBean.get();
            String fileName = FileUtilsEx.getFileNameByPath(fileBean.getName());
            fileid = NginxFileUtils.add(fileName, fileContent, "ds_upload", null);
        }

        UserAuth aEntity = new UserAuth();
        aEntity.setName(name.toString());
        short sexVal = Short.valueOf(sex.toString());
        aEntity.setSex(sexVal);
        aEntity.setStatus((short) 1);
        aEntity.setPhone(user.getPhone());
        aEntity.setSchoolId(Integer.valueOf(school_id.toString()));
        aEntity.setCreateTime(new Date());
        aEntity.setImage(fileid);
        aEntity.setStuNumber(stu_number.toString());
        aEntity.setUserId(user_id);
        aEntity.setInstituteId(Integer.valueOf(institute_id.toString()));

        // 身份认证为学生时,设置学院.专业.年级.班级
        if (!teacher) {
            aEntity.setClassId(Integer.valueOf(class_id.toString()));
            aEntity.setGradeId(Integer.valueOf(grade_id.toString()));
            aEntity.setMajorId(Integer.valueOf(major_id.toString()));
        }

        userAuthService.addUserAuth(aEntity);

        // 身份认证为教职工时,更新用户信息
        if (teacher) {
            user.setIsTeacher((short) 1);
            user.setSchoolId(Integer.valueOf(school_id.toString()));
            user.setInstituteId(Integer.valueOf(institute_id.toString()));
            userService.updateUser(user);
        }

        // 更新女生头像 默认头像为男生
        if (sexVal == (short) 2) {
            // 女孩
            user.setSex(sexVal);
            user.setHeadimg("/default/girl_head_img.png");
            userService.updateUser(user);
        }

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", null)));
    }

    /**
     * 用户是否认证
     *
     * @param response
     * @param user_id
     */
    @RequestMapping("/user_getAuthStatus")
    public void user_getAuthStatus(HttpServletResponse response, @ModelAttribute("user_id") Integer user_id) {
        // 获取用户信息
        User uEntity = new User();
        uEntity.setId(user_id);
        User user = userService.getUser(uEntity);
        if (user == null || user.getIsDelete().shortValue() == 1) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "账号不存在", null)));
            return;
        }

        if (user.getStatus().shortValue() == 2) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "账号被禁用", null)));
            return;
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("is_auth", user.getIsAuth());
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", map)));
    }
}
