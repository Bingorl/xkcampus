package com.biu.wifi.campus.Listener;

import com.biu.wifi.campus.Tool.PushTool;
import com.biu.wifi.campus.dao.PushMapper;
import com.biu.wifi.campus.dao.UserStuNoInvalidMapper;
import com.biu.wifi.campus.dao.model.*;
import com.biu.wifi.campus.service.JwService;
import com.biu.wifi.campus.service.SchoolService;
import com.biu.wifi.campus.service.UserService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 校验学生学号是否正确的调度任务
 *
 * @author zhangbin.
 * @date 2018/11/9.
 */
// @Component
public class ValidTask {

    @Autowired
    private UserService userService;
    @Autowired
    private JwService jwService;
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private PushMapper pushMapper;
    @Autowired
    private UserStuNoInvalidMapper userStuNoInvalidMapper;

    /**
     * 通过比对教务系统的学籍信息
     * 校验数据库已录入的学号是否合法
     * 将不合法的数据存入一张数据库中
     * <p>
     * 凌晨1：00执行
     */
    //@Scheduled(cron = "0 0 1 * * ?")
    public void valid() {
        School schoolQuery = new School();
        schoolQuery.setIsDelete((short) 2);
        List<School> schoolList = schoolService.findSchoolList(schoolQuery);

        List<Map<String, Object>> list;
        //遍历学校
        for (School school : schoolList) {
            UserCriteria userEx = new UserCriteria();
            userEx.createCriteria()
                    .andIsDeleteEqualTo((short) 2)
                    .andSchoolIdEqualTo(school.getId())
                    .andIsTeacherEqualTo((short) 2);
            List<User> userList = userService.findList(userEx);
            //遍历学生
            for (User user : userList) {
                list = jwService.findXSXJList("", user.getStuNumber());
                if (CollectionUtils.isEmpty(list)) {
                    UserStuNoInvalidCriteria userStuNoInvalidEx = new UserStuNoInvalidCriteria();
                    userStuNoInvalidEx.createCriteria()
                            .andSchoolIdEqualTo(user.getSchoolId())
                            .andStuNoEqualTo(user.getStuNumber());
                    long count = userStuNoInvalidMapper.countByExample(userStuNoInvalidEx);
                    if (count == 0) {
                        //学号不存在，记录在表中，统一进行推送
                        UserStuNoInvalid userStuNoInvalid = new UserStuNoInvalid();
                        userStuNoInvalid.setSchoolId(user.getSchoolId());
                        userStuNoInvalid.setSchoolName(school.getName());
                        userStuNoInvalid.setUsername(user.getName());
                        userStuNoInvalid.setStuNo(user.getStuNumber());
                        userStuNoInvalid.setCreateTime(new Date());
                        userStuNoInvalid.setIsTeacher(user.getIsTeacher());
                        userStuNoInvalid.setDevType(user.getDevType());
                        userStuNoInvalid.setDevToken(user.getDevToken());
                        userStuNoInvalidMapper.insertSelective(userStuNoInvalid);
                    }
                }
            }
        }
    }

    /**
     * 推送学号修改提醒通知
     * <p>
     * 早上8点执行
     */
//    @Scheduled(cron = "0 0 8 * * ?")
    public void push() {
        List<UserStuNoInvalid> list = userStuNoInvalidMapper.selectByExample(null);
        String title;
        for (UserStuNoInvalid user : list) {
            boolean flag = false;
            title = "您的学号不正确，请尽快修改！";

            HashMap<String, Object> hm = new HashMap<>();
            hm.put("id", "");
            hm.put("title", title);
            hm.put("type", 1);//官方消息

            try {
                flag = PushTool.pushToUser(user.getId(), "", title, hm);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 入推送表
            Push puEntity = new Push();
            puEntity.setToken(user.getDevToken());
            puEntity.setContent("");
            puEntity.setUserId(user.getId());
            puEntity.setMessageType((short) 1);
            puEntity.setDeviceType(user.getDevType());
            puEntity.setTitle(title);
            if (flag) {
                puEntity.setType((short) 10);
            } else {
                puEntity.setType((short) 50);
            }

            pushMapper.insertSelective(puEntity);
        }
    }
}
