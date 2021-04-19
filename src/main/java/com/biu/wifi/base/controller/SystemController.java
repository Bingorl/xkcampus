package com.biu.wifi.base.controller;

import com.biu.wifi.campus.Tool.NginxFileUtils;
import com.biu.wifi.campus.Tool.PushTool;
import com.biu.wifi.campus.dao.PushMapper;
import com.biu.wifi.campus.dao.model.Push;
import com.biu.wifi.campus.dao.model.User;
import com.biu.wifi.campus.dao.model.UserCriteria;
import com.biu.wifi.campus.result.Result;
import com.biu.wifi.campus.service.UserService;
import com.biu.wifi.component.datastore.FileSupportService;
import com.biu.wifi.core.CoreConstants;
import com.biu.wifi.core.base.CoreController;
import com.biu.wifi.core.support.cache.CacheSupport;
import com.biu.wifi.core.support.servlet.ServletHolderFilter;
import com.biu.wifi.core.util.CaptchaUtils;
import com.biu.wifi.core.util.FileUtilsEx;
import com.biu.wifi.core.util.ServletUtilsEx;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SystemController extends CoreController {

    @Autowired
    private FileSupportService fileSupportService;
    @Autowired
    private UserService userService;
    @Autowired
    private PushMapper pushMapper;

    /**
     * @throws Exception
     * @版本号 V 1.0
     * @description-创造验证码
     */
    @RequestMapping("/jcaptcha.do")
    public void jcaptcha(String deviceid, HttpServletRequest request, HttpServletResponse response) throws Exception {
        CaptchaUtils captchaUtils = null;
        boolean isMobileCaptcha = StringUtils.isNotBlank(deviceid);
        if (isMobileCaptcha) {
            captchaUtils = new CaptchaUtils(130, 58, 40);
        } else {
            captchaUtils = new CaptchaUtils(86, 30, 25);
        }
        // 如果是手机登录,则保存缓存
        if (isMobileCaptcha) {
            CacheSupport.put("mobileJcaptchaCache", deviceid, captchaUtils.getImageCode());
        } else {
            request.getSession().setAttribute("biu.jcaptcha", captchaUtils.getImageCode());
        }
        // 输出图象到页面
        ImageIO.write(captchaUtils.getImage(), "PNG", response.getOutputStream());
    }

    @RequestMapping("/back_api_getCode.do")
    public void getCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", request.getSession().getAttribute("biu.jcaptcha")));
    }

    @RequestMapping("/upFile.do")
    public void uploadFile(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> rs = new HashMap<String, Object>();

        try {
            Map param = ServletHolderFilter.getContext().getParamMap();
            List<DiskFileItem> fileList = null;
            fileList = (List<DiskFileItem>) param.get("file");
            if (fileList != null) {
//			    List fileIdList = new ArrayList();
                String purl = "";
                String fileName = "";
                for (DiskFileItem file : fileList) {
                    fileName = FileUtilsEx.getFileNameByPath(file.getName());

                    byte[] fileContent = file.get();
//			      fileIdList.add("http://nwj.assistworld.vip:85/nwjpic/" + fileSupportService.add(fileName, fileContent, "ds_upload"));
//			      purl = "http://localhost:85/nwjpic/" + fileSupportService.add(fileName, fileContent, "ds_upload");
                    purl = CoreConstants.getProperty("picurl") + fileSupportService.add(fileName, fileContent, "ds_upload");
                }
                rs.put("code", 0);
                rs.put("msg", "");
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("src", purl);
                data.put("filename", fileName);
                rs.put("data", data);
            } else {
                this.logger.error("上传文件没有接受到文件!");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            rs.put("errorMsg", e.getMessage());
        }
        ServletUtilsEx.renderJson(response, rs);
    }


    @SuppressWarnings("unchecked")
    @RequestMapping("/uploadFileToNginx.do")
    public void uploadFileToNginx(HttpServletRequest request, HttpServletResponse response) {

        Map<String, Object> rs = new HashMap<String, Object>();

        try {
            Map<String, Object> param = ServletHolderFilter.getContext().getParamMap();

            List<DiskFileItem> fileList = null;

            fileList = (List<DiskFileItem>) param.get("file");

            if (fileList != null) {
                List<String> fileIdList = new ArrayList<>();
                List<String> fileNameList = new ArrayList<>();
                String id = "";
                String fileName = "";

                for (DiskFileItem file : fileList) {
                    String tmpFileName = FileUtilsEx.getFileNameByPath(file.getName());
                    fileIdList.add(NginxFileUtils.add(tmpFileName, file.get(), "ds_upload", null));
                    fileNameList.add(tmpFileName);
                }

                if (fileIdList.size() > 0) {
                    for (String tmpId : fileIdList) {
                        id = id + tmpId + ",";
                    }
                    for (String tmpName : fileNameList) {
                        fileName = fileName + tmpName + ",";
                    }
//			    	id = fileIdList.get(0);
//			    	fileName = fileNameList.get(0);
                    rs.put("pic", id.substring(0, id.length() - 1));
                    rs.put("filename", fileName.substring(0, fileName.length() - 1));
                } else {
                    rs.put("pic", id);
                    rs.put("filename", fileName);
                }
            } else {
                logger.error("上传文件没有接受到文件!");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            rs.put("errorMsg", e.getMessage());
        }

        ServletUtilsEx.renderJson(response, rs);
    }

    /**
     * 平台推送消息
     *
     * @param type     推送类型
     *                 <p>
     *                 1官方消息 2群通知 3通知日历 4群成员变动 5帖子评论、发布帖子@、点赞
     *                 6楼层评论、回复、楼层@，回复楼层，点赞 7新鲜事评论、发布新鲜事@、点赞
     *                 8留言9主页审核失败 10主页审核成功 11私信  12请假通知  13教室预约通知  14系统通知
     * @param title    标题
     * @param content  内容
     * @param schoolId 学校ID
     * @param userIds  用户ID
     * @param response
     */
    @RequestMapping("pushMessageToUser")
    public void pushMessageToUser(Short type, String title, String content,
                                  @RequestParam(required = false) Integer schoolId,
                                  @RequestParam(required = false) String userIds, HttpServletResponse response) {
        if (type == null || title == null || content == null || (schoolId == null && userIds == null)) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "缺少必要参数"));
            return;
        }

        HashMap<String, Object> hm = new HashMap<>();
        hm.put("type", type);
        hm.put("title", title);


        UserCriteria ex = new UserCriteria();
        UserCriteria.Criteria criteria = ex.createCriteria()
                .andIsDeleteEqualTo((short) 2)
                .andDevTokenIsNotNull();
        if (schoolId != null) {
            criteria.andSchoolIdEqualTo(schoolId);
        } else {
            if (StringUtils.isNotBlank(userIds)) {
                List<Integer> list = new ArrayList<>();
                for (String userId : userIds.split(",")) {
                    list.add(Integer.valueOf(userId));
                }
                criteria.andIdIn(list);
            }
        }


        List<User> userList = userService.findList(ex);
        if (userList.isEmpty()) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "至少要设置一个推送目标对象"));
            return;
        }

        boolean flag = false;
        int count = 0;

        for (User user : userList) {
            // 入推送表
            Push puEntity = new Push();
            puEntity.setToken(user.getDevToken());
            puEntity.setContent(content);
            puEntity.setUserId(user.getId());
            puEntity.setMessageType(type);
            puEntity.setDeviceType(user.getDevType());
            puEntity.setTitle(title);
            count = pushMapper.insertSelective(puEntity);

            hm.put("id", puEntity.getId());
            try {
                flag = PushTool.pushToUser(user.getId(), content, title, hm);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (flag) {
                puEntity.setType((short) 10);
            } else {
                puEntity.setType((short) 50);
            }
            count = pushMapper.updateByPrimaryKeySelective(puEntity);
        }

        if (flag && count > 0) {
            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功"));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "失败"));
        }
    }
}
