package com.biu.wifi.campus.Listener;

import com.biu.wifi.campus.Tool.TimeUtil;
import com.biu.wifi.campus.dao.LeaveNoticeMapper;
import com.biu.wifi.campus.dao.model.*;
import com.biu.wifi.campus.service.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 请假审批通知任务调度器
 *
 * @author zhangbin.
 * @date 2018/11/7.
 */
// @Component
public class LeaveNoticeTask {

    private static Logger logger = LoggerFactory.getLogger(LeaveNoticeTask.class);
    @Autowired
    private LeaveInfoService leaveInfoService;
    @Autowired
    private DormBuildingService dormBuildingService;
    @Autowired
    private DormBuildingUserService dormBuildingUserService;
    @Autowired
    private UserService userService;
    @Autowired
    private TeachingWeekService teachingWeekService;
    @Autowired
    private JwCjcxService jwCjcxService;
    @Autowired
    private LeaveNoticeMapper leaveNoticeMapper;

    @Scheduled(cron = "0 0/3 * * * ?")
    public void pushNotice() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");

        //查询审批通过的通知
        List<LeaveInfo> leaveInfoList = leaveInfoService.findAuditPassLeaveInfoList();

        for (LeaveInfo leaveInfo : leaveInfoList) {
            StringBuffer title, content;
            LeaveNotice leaveNotice;
            String statusText = leaveInfoService.getLeaveInfoStatusText(leaveInfo.getStatus());
            User user = userService.getById(leaveInfo.getUserId());
            //外出或回家通知宿管
            if (leaveInfo.getGoTo() != 3 && StringUtils.isNotBlank(leaveInfo.getApartmentBuilding())) {
                //查询宿管人员列表
                DormBuilding dormBuilding = dormBuildingService.getById(Integer.valueOf(leaveInfo.getApartmentBuilding()));
                DormBuildingUserCriteria dormBuildingUserEx = new DormBuildingUserCriteria();
                dormBuildingUserEx.createCriteria()
                        .andIsDeleteEqualTo((short) 2)
                        .andBuildingIdEqualTo(dormBuilding.getId());
                List<DormBuildingUser> dormBuildingUserList = dormBuildingUserService.findList(dormBuildingUserEx);
                for (DormBuildingUser dormBuildingUser : dormBuildingUserList) {
                    leaveNotice = new LeaveNotice();
                    title = new StringBuffer("请假：");
                    if (StringUtils.isNotBlank(dormBuilding.getAreaPosition())) {
                        title.append(dormBuilding.getAreaPosition());
                    }
                    title.append(dormBuilding.getNo());
                    if (StringUtils.isNotBlank(dormBuilding.getUnitNo())) {
                        title.append(dormBuilding.getUnitNo());
                    }

                    title.append(leaveInfo.getApartment())
                            .append("宿舍")
                            .append(leaveInfo.getRealName())
                            .append("的请假").append(statusText);
                    content = new StringBuffer("假期：");
                    content.append(sdf.format(leaveInfo.getStartDate()))
                            .append(" 至 ")
                            .append(sdf.format(leaveInfo.getEndDate()));
                    leaveNotice.setTitle(title.toString());
                    leaveNotice.setContent(content.toString());
                    leaveNotice.setLeaveId(leaveInfo.getId());
                    leaveNotice.setToUserType((short) 3);//接收人为宿管
                    leaveNotice.setToUserId(dormBuildingUser.getUserId());
                    leaveNotice.setCreateTime(new Date());
                    //新增通知记录
                    leaveNoticeMapper.insertSelective(leaveNotice);
                    /**
                     * @param leaveId       请假单ID
                     * @param leaveNoticeId 请假审批通知ID
                     * @param title         标题
                     * @param receiverId    接收人ID
                     * @param deviceType    接收人设备类型
                     * @param deviceToken   接收人设备token
                     */
                    User buildingUser = userService.getById(dormBuildingUser.getUserId());
                    leaveInfoService.addPush(leaveInfo.getId(), leaveNotice.getId(), 0, title.toString(),
                            dormBuildingUser.getUserId(), buildingUser.getDevType(), buildingUser.getDevToken());
                }
            }

            //通知任课教师
            //查询当前所处的学期学年
            String zxjxjhh;
            String mondayDate;
            List<TeachingWeek> teachingWeekList = teachingWeekService.findList(user.getSchoolId(), null);
            if (CollectionUtils.isEmpty(teachingWeekList)) {
                logger.info("管理员未设置课程教学周期信息，请假通知执行任务终止");
                continue;
            }
            TeachingWeek teachingWeek = teachingWeekList.get(0);
            zxjxjhh = teachingWeek.getTermCode();
            mondayDate = teachingWeek.getMondayDate();

            //校验学号是否正确
            if (!jwCjcxService.validStuNo(user.getSchoolId(), user.getStuNumber())) {
                logger.error("学生的学号【{}】不正确，请假通知执行任务终止", user.getStuNumber());
                continue;
            }

            //根据请假的开始时间和结束时间
            //计算请假所在的星期
            String skxq = TimeUtil.getQXSet(leaveInfo.getStartDate(), leaveInfo.getEndDate(), leaveInfo.getPlanDays(), false);
            //计算请假时间所在的周次
            String[] mondays = mondayDate.split(",");
            List<Map<String, String>> skzcList = getSKZC(leaveInfo.getStartDate(), leaveInfo.getEndDate(), mondays, false);
            List<Integer> skzc = new ArrayList<>();
            for (Map<String, String> map : skzcList) {
                String key = new ArrayList<>(map.keySet()).get(0);
                int index = Integer.valueOf(key);
                skzc.add(index);
            }
            //假期内的上课课程列表
            List<Map<String, Object>> classPKList = jwCjcxService.findXSKCBList(zxjxjhh, user.getStuNumber(), skzc, skxq);

            if (CollectionUtils.isEmpty(classPKList)) {
                logger.info("学号为{}的学生，在请假期间没有查询到课程...");
                continue;
            }

            for (Map<String, Object> classz : classPKList) {
                content = new StringBuffer("假期：");
                for (int i = 0; i < skzcList.size(); i++) {
                    Map<String, String> map = skzcList.get(i);
                    String key = new ArrayList<>(map.keySet()).get(0);
                    int index = Integer.valueOf(key);
                    content.append("第" + index + "周").append(map.get(key).toString());
                    int skjc = Integer.valueOf(classz.get("SKJC").toString());
                    int cxjc = Integer.valueOf(classz.get("CXJC").toString());
                    for (int j = 0; j < cxjc; j++) {
                        content.append(skjc);
                        if (j != cxjc - 1) {
                            content.append(".");
                        }
                        skjc++;
                    }
                    content.append("节");
                    if (i != skzcList.size() - 1) {
                        content.append("、");
                    }
                }

                if ("假期：".equals(content.toString())) {
                    //该教师在该周次没有课程
                    continue;
                }

                String className = classz.get("KCM").toString();
                leaveNotice = new LeaveNotice();
                title = new StringBuffer("请假：");
                title.append(leaveInfo.getStuNo() + "  ")
                        .append(leaveInfo.getRealName())
                        .append("    ")
                        .append(leaveInfo.getStuNo())
                        .append("    ")
                        .append("《")
                        .append(className)
                        .append("》")
                        .append(statusText);
                leaveNotice.setTitle(title.toString());
                leaveNotice.setContent(content.toString());
                leaveNotice.setLeaveId(leaveInfo.getId());
                leaveNotice.setToUserType((short) 2);//接收人为老师
                //上课教师号
                String jobNo = classz.get("JSH").toString();
                //根据教师号查询教师
                User teacher = new User();
                teacher.setIsDelete((short) 2);
                teacher.setStuNumber(jobNo);
                teacher.setSchoolId(user.getSchoolId());
                List<User> teacherList = userService.findList(teacher);
                if (teacherList.size() == 0) {
                    logger.error("请假审批成功推送通知调度任务未找到工号为【{}】的教师!", jobNo);
                    continue;
                }
                teacher = teacherList.get(0);
                leaveNotice.setToUserId(teacher.getId());
                leaveNotice.setCreateTime(new Date());
                //新增通知记录
                leaveNoticeMapper.insertSelective(leaveNotice);
                /**
                 * @param leaveId       请假单ID
                 * @param leaveNoticeId 请假审批通知ID
                 * @param title         标题
                 * @param receiverId    接收人ID
                 * @param deviceType    接收人设备类型
                 * @param deviceToken   接收人设备token
                 */
                leaveInfoService.addPush(leaveInfo.getId(), leaveNotice.getId(), 0, title.toString(),
                        teacher.getId(), teacher.getDevType(), teacher.getDevToken());
            }

        }
    }

    /**
     * 获取给定日期在学期周期的第几周
     *
     * @param date
     * @param mondays
     * @return
     */
    public int getWeekIndex(Date date, String[] mondays, boolean excludeSatSun) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date m = null;
        Date f = null;
        int index = 0;//周次
        for (int i = 0; i < mondays.length; i++) {
            try {
                //周一
                m = sdf.parse(mondays[i]);
                if (!excludeSatSun) {
                    //周五
                    f = DateUtils.addDays(m, 4);
                } else {
                    //周日
                    f = DateUtils.addDays(m, 6);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (date.compareTo(m) >= 0 && date.compareTo(f) <= 0) {
                index = i + 1;
                break;
            }
        }
        return index;
    }

    /**
     * 获取一段时间所在的周次
     *
     * @param d1            开始时间
     * @param d2            结束时间
     * @param mondays       课程周期内的所有周一
     * @param excludeSatSun 是否排除周六周日
     * @return
     */
    public List<Map<String, String>> getSKZC(Date d1, Date d2, String[] mondays, boolean excludeSatSun) {
        List<Map<String, String>> list = new ArrayList<>();
        Map<String, String> map;
        boolean go = true;
        do {

            int m = TimeUtil.getDay(d1);
            if (excludeSatSun && (m == 6 && m == 7)) {
                //排除周六和周日
                continue;
            }
            int index = getWeekIndex(d1, mondays, excludeSatSun);
            String str = TimeUtil.getDayStr(d1);
            map = new HashedMap();
            map.put(String.valueOf(index), str);
            if (index != 0) {
                list.add(map);
                go = false;
            }

            d1 = DateUtils.addDays(d1, 1);
        } while (d1.compareTo(d2) <= 0 && go);
        return list;
    }
}
