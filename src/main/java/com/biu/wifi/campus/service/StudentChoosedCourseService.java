package com.biu.wifi.campus.service;

import com.biu.wifi.campus.dao.ClassMapper;
import com.biu.wifi.campus.dao.StudentChoosedCourseMapper;
import com.biu.wifi.campus.dao.UserMapper;
import com.biu.wifi.campus.dao.model.Class;
import com.biu.wifi.campus.dao.model.*;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author zhangbin.
 * @date 2019/3/11.
 */
@Service
public class StudentChoosedCourseService {

    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private StudentChoosedCourseMapper studentChoosedCourseMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ClassMapper classMapper;
    @Autowired
    private JdbcTemplate ojdbcTemplate;

    /**
     * 根据课程号和课序号查询学生选课列表
     *
     * @param courseNo
     * @param courseSerialNo
     * @return
     */
    public List<StudentChoosedCourse> findListByCourseNo(String courseNo, String courseSerialNo) {
        StudentChoosedCourseCriteria ex = new StudentChoosedCourseCriteria();
        ex.createCriteria()
                .andIsDeleteEqualTo(2)
                .andCourseNoEqualTo(courseNo)
                .andCourseSerialNoEqualTo(courseSerialNo);
        return studentChoosedCourseMapper.selectByExample(ex);
    }

    public List<User> getStudentListByByCourseNo(String courseNo, String courseSerialNo) {
        List<StudentChoosedCourse> studentChoosedCourseList = findListByCourseNo(courseNo, courseSerialNo);
        Set<User> userList = new HashSet<>();
        for (StudentChoosedCourse studentChoosedCourse : studentChoosedCourseList) {
            UserCriteria userEx = new UserCriteria();
            userEx.setOrderByClause("create_time desc");
            userEx.createCriteria()
                    .andIsDeleteEqualTo((short) 2)
                    .andIsTeacherEqualTo((short) 2)
                    .andStuNumberEqualTo(studentChoosedCourse.getStuNumber());
            List<User> list = userMapper.selectByExample(userEx);
            if (!list.isEmpty()) {
                userList.addAll(list);
            }
        }
        return new ArrayList<>(userList);
    }

    public String getStudentIdByByCourseNo(String courseNo, String courseSerialNo) {
        List<User> userList = getStudentListByByCourseNo(courseNo, courseSerialNo);
        List<Integer> studentIdList = new ArrayList<>();
        for (User user : userList) {
            studentIdList.add(user.getId());
        }
        if (studentIdList.isEmpty()) {
            return null;
        } else {
            return StringUtils.join(studentIdList, ",");
        }
    }

    public String getClassNoByCourseNo(String courseNo, String courseSerialNo) {
        List<User> userList = getStudentListByByCourseNo(courseNo, courseSerialNo);
        List<String> classNoList = new ArrayList<>();
        for (User user : userList) {
            Class c = classMapper.selectByPrimaryKey(user.getClassId());
            if (c == null) {
                logger.error("查询的班级[id={}]不存在", user.getClassId());
                continue;
            } else {
                classNoList.add(c.getName());
            }
        }
        if (classNoList.isEmpty()) {
            return null;
        } else {
            return StringUtils.join(classNoList, ",");
        }
    }

    /**
     * 导入教务的学生选课数据
     */
    public void importData() {
        List<Map<String, Object>> mapList = ojdbcTemplate.queryForList("select * from xk_xkb");
        for (Map map : mapList) {
            StudentChoosedCourse studentChoosedCourse = new StudentChoosedCourse();
            studentChoosedCourse.setTermCode(MapUtils.getString(map, "ZXJXJHH"));
            studentChoosedCourse.setCourseNo(MapUtils.getString(map, "KCH"));
            studentChoosedCourse.setCourseSerialNo(MapUtils.getString(map, "KXH"));
            studentChoosedCourse.setStuNumber(MapUtils.getString(map, "XH"));
            studentChoosedCourse.setPlanNo(MapUtils.getString(map, "FAJHH"));
            studentChoosedCourse.setStudyType(MapUtils.getString(map, "XDFSDM"));
            studentChoosedCourse.setCourseAttr(MapUtils.getString(map, "KCSXDM"));
            studentChoosedCourse.setExamType(MapUtils.getString(map, "KSLXDM"));
            studentChoosedCourse.setStatus(MapUtils.getString(map, "XKZTDM"));
            studentChoosedCourse.setRemark(MapUtils.getString(map, "BZ"));
            studentChoosedCourse.setCreateTime(new Date());
            studentChoosedCourseMapper.insertSelective(studentChoosedCourse);
        }
    }
}
