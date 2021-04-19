package com.biu.wifi.campus.Listener;

import com.biu.wifi.campus.dao.StudentGpaMapper;
import com.biu.wifi.campus.dao.model.*;
import com.biu.wifi.campus.service.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 推优绩点定时任务
 *
 * @author zhangbin.
 * @date 2018/11/15.
 */
// @Component
public class StudentGpaTask {

    @Autowired
    private SchoolService schoolService;
    @Autowired
    private InstituteService instituteService;
    @Autowired
    private UserService userService;
    @Autowired
    private JwCjcxService jwCjcxService;
    @Autowired
    private TeachingWeekService teachingWeekService;
    @Autowired
    private StudentGpaMapper studentGpaMapper;
//    @Autowired
//    private StudentGpaMapperEx studentGpaMapperEx;

    @Scheduled(cron = "0 0/1 * * * ?")
    @Transactional
    public void execute() {

        //查询所有学校
        School schoolQuery = new School();
        schoolQuery.setIsDelete((short) 2);
        List<School> schoolList = schoolService.findList(schoolQuery);
        for (School school : schoolList) {
            //查询学校的所有专业
            Institute instituteQuery = new Institute();
            instituteQuery.setSchoolId(school.getId());
            instituteQuery.setIsDelete((short) 2);
            List<Institute> instituteList = instituteService.findList(instituteQuery);
            for (Institute institute : instituteList) {

                List<StudentGpa> studentGpaList = new ArrayList<>();

                //查询专业中所有的学生
                UserCriteria userEx = new UserCriteria();
                userEx.createCriteria()
                        .andSchoolIdEqualTo(school.getId())
                        .andInstituteIdEqualTo(institute.getId())
                        .andIsDeleteEqualTo((short) 2)
                        .andIsTeacherEqualTo((short) 2)
                        .andStatusEqualTo((short) 1)
                        .andIsAuthEqualTo((short) 1);
                List<User> userList = userService.findList(userEx);
                for (User user : userList) {
                    //更新学生的推优绩点
                    StudentGpaCriteria example = new StudentGpaCriteria();
                    if (StringUtils.isBlank(user.getStuNumber())) {
                        continue;
                    }
                    example.createCriteria().andStuNoEqualTo(user.getStuNumber());
                    List<StudentGpa> tempList = studentGpaMapper.selectByExample(example);
                    StudentGpa studentGpa;
                    if (CollectionUtils.isEmpty(tempList)) {
                        studentGpa = new StudentGpa();
                    } else {
                        studentGpa = tempList.get(0);
                    }

                    // 计算绩点
                    List<TeachingWeek> teachingWeekList = teachingWeekService.findList(user.getSchoolId(), null);
                    if (CollectionUtils.isEmpty(teachingWeekList)) {
                        continue;
                    }
                    TeachingWeek teachingWeek = teachingWeekList.get(0);
                    //当前学年学期
                    String zxjxjhh = teachingWeek.getTermCode();
                    //查询到课程成绩列表
                    List<Map<String, Object>> kccjList = jwCjcxService.findKCCJList(zxjxjhh, user.getStuNumber());
                    if (kccjList.size() > 0) {
                        double gpa = jwCjcxService.getTYJD(kccjList);
                        if (studentGpa.getId() == null) {
                            studentGpa.setGpa(gpa);
                        } else {
                            studentGpa.setGpa(studentGpa.getGpa() + gpa);
                        }
                    } else {
                        if (studentGpa.getId() == null) {
                            studentGpa.setSchoolId(school.getId());
                            studentGpa.setStuNo(user.getStuNumber());
                            studentGpa.setMajorRanking(0);
                            studentGpa.setGpa(0d);
                        }
                    }
                    studentGpaList.add(studentGpa);
                }

                if (CollectionUtils.isNotEmpty(studentGpaList)) {
                    //根据推优绩点排名
                    Collections.sort(studentGpaList, new Comparator<StudentGpa>() {
                        @Override
                        public int compare(StudentGpa o1, StudentGpa o2) {
                            if (o1.getGpa() == null)
                                o1.setGpa(0d);
                            if (o2.getGpa() == null)
                                o2.setGpa(0d);

                            return o1.getGpa().compareTo(o2.getGpa());
                        }
                    });

                    //计算排名
                    int i = 0;
                    Iterator<StudentGpa> iterator = studentGpaList.iterator();
                    while (iterator.hasNext()) {
                        i += 1;
                        StudentGpa studentGpa = iterator.next();
                        studentGpa.setMajorRanking(i);
                        if (studentGpa.getId() != null) {
                            studentGpa.setUpdateTime(new Date());
                        } else {
                            studentGpa.setCreateTime(new Date());
                        }
                    }

                    //更新
                    iterator = studentGpaList.iterator();
                    while (iterator.hasNext()) {
                        StudentGpa studentGpa = iterator.next();
                        if (studentGpa.getId() != null) {
                            studentGpaMapper.updateByPrimaryKeySelective(studentGpa);
                        } else {
                            studentGpa.setCreateTime(new Date());
                            studentGpaMapper.insertSelective(studentGpa);
                        }
                    }

                    //studentGpaMapperEx.batchInsert(studentGpaList);
                }
            }
        }
    }
}
