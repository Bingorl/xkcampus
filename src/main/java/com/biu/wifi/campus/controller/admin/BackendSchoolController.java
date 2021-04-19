package com.biu.wifi.campus.controller.admin;

import com.biu.wifi.campus.Tool.StringUtil;
import com.biu.wifi.campus.dao.model.*;
import com.biu.wifi.campus.dao.model.Class;
import com.biu.wifi.campus.result.Result;
import com.biu.wifi.campus.service.BackendSchoolService;
import com.biu.wifi.campus.service.GroupTmpCreatePermissionService;
import com.biu.wifi.campus.service.UserService;
import com.biu.wifi.core.support.pageLimit.PageLimitHolderFilter;
import com.biu.wifi.core.util.ServletUtilsEx;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class BackendSchoolController {

    @Autowired
    private BackendSchoolService schoolService;

    @Autowired
    private UserService userService;

    @Autowired
    private GroupTmpCreatePermissionService groupTmpCreatePermissionService;

    @RequestMapping("/back_api_addInstitute_s")
    public void back_api_addInstitute_s(Integer schoolId, String name, HttpServletResponse response) {
        if (schoolId != null && StringUtils.isNotBlank(name)) {

            if (schoolService.getInstituteCount(schoolId, name) > 0) {
                ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "学院已经存在", null));
                return;
            }

            Institute institute = new Institute();
            institute.setCreateTime(new Date());
            institute.setIsDelete((short) 2);
            institute.setName(name);
            institute.setSchoolId(schoolId);
            schoolService.addInstitute(institute);

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", null));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_addMajor_s")
    public void back_api_addMajor_s(Integer instituteId, String name, HttpServletResponse response) {
        if (instituteId != null && StringUtils.isNotBlank(name)) {

            if (schoolService.getMajorCount(instituteId, name) > 0) {
                ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "专业已经存在", null));
                return;
            }

            Major major = new Major();
            major.setCreateTime(new Date());
            major.setIsDelete((short) 2);
            major.setName(name);
            major.setInstituteId(instituteId);
            schoolService.addMajor(major);

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", null));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_addGrade_s")
    public void back_api_addGrade_s(String name, HttpServletResponse response) {
        if (StringUtils.isNotBlank(name)) {

            if (schoolService.getGradeCount(name) > 0) {
                ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "年级已经存在", null));
                return;
            }

            Grade grade = new Grade();
            grade.setCreateTime(new Date());
            grade.setIsDelete((short) 2);
            grade.setName(name);
            schoolService.addGrade(grade);

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", null));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_addClass_s")
    public void back_api_addClass_s(String name, Integer majorId, Integer gradeId, HttpServletResponse response) {
        if (StringUtils.isNotBlank(name) && majorId != null) {

            if (schoolService.getClassCount(name, majorId, gradeId) > 0) {
                ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "班级已经存在", null));
                return;
            }

            Class clazz = new Class();
            clazz.setCreateTime(new Date());
            clazz.setIsDelete((short) 2);
            clazz.setName(name);
            clazz.setMajorId(majorId);
            clazz.setGradeId(gradeId);
            Grade grade = schoolService.getGradeById(gradeId);
            clazz.setGrade(grade == null ? "" : grade.getName());
            schoolService.addClass(clazz);

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", null));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_findStudentNumberList_s")
    public void back_api_findStudentNumberList_s(Integer page, Integer pageSize, Integer schoolId, String stuNumber,
                                                 Short status, HttpServletResponse response) {
        if (page != null && pageSize != null && schoolId != null && status != null) {
            PageLimitHolderFilter.setContext(page, pageSize, null);

            List<Map<String, Object>> list = schoolService.findStudentNumbers(schoolId, stuNumber, status);

            Map<String, Object> map = new HashMap<>();
            map.put("list", list);
            map.put("totalCount", PageLimitHolderFilter.getContext().getTotalCount());

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", map));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_addStudentNumber_s")
    public void back_api_addStudentNumber_s(Integer schoolId, String stuNumber, Integer instituteId, Integer majorId,
                                            Integer classId, Integer gradeId, String institute, String major, String clazz, String grade,
                                            HttpServletResponse response) {
        if (schoolId != null && instituteId != null && majorId != null && classId != null && gradeId != null
                && StringUtils.isNotBlank(stuNumber) && StringUtils.isNotBlank(institute)
                && StringUtils.isNotBlank(major) && StringUtils.isNotBlank(clazz) && StringUtils.isNotBlank(grade)) {

            if (schoolService.getStudentNumberCount(schoolId, instituteId, majorId, gradeId, classId, stuNumber) > 0) {
                ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "学号已经存在", null));
                return;
            }

            StudentInfo studentInfo = new StudentInfo();
            studentInfo.setClassId(classId);
            studentInfo.setGradeId(gradeId);
            studentInfo.setInstituteId(instituteId);
            studentInfo.setMajorId(majorId);
            studentInfo.setSchoolId(schoolId);
            studentInfo.setStuNum(stuNumber);
            studentInfo.setIsDelete((short) 2);
            studentInfo.setInstitute(institute);
            studentInfo.setMajor(major);
            studentInfo.setGrade(grade);
            studentInfo.setClazz(clazz);
            studentInfo.setCreateTime(new Date());
            School school = schoolService.getSchoolById(schoolId);
            studentInfo.setSchool(school == null ? "" : school.getName());
            schoolService.addStudentNumber(studentInfo);

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", null));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_findInstituteList")
    public void back_api_findInstituteList(Integer schoolId, HttpServletResponse response) {
        if (schoolId != null) {
            Institute institute = new Institute();
            institute.setIsDelete((short) 2);
            institute.setSchoolId(schoolId);
            List<Institute> list = schoolService.findInstituteBySchool(institute);

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", list));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    /**
     * 根据用户ID查询所在学校的学院列表
     *
     * @param userId
     * @param response
     */
    @RequestMapping("/back_api_findInstituteListByUserId")
    public void back_api_findInstituteListByUserId(Integer userId, HttpServletResponse response) {
        if (userId == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
            return;
        }

        User user = userService.getById(userId);
        Institute institute = new Institute();
        institute.setIsDelete((short) 2);
        institute.setSchoolId(user.getSchoolId());
        List<Institute> list = schoolService.findInstituteBySchool(institute);
        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", list));
    }

    @RequestMapping("/back_api_findMajorList")
    public void back_api_findMajorList(Integer instituteId, HttpServletResponse response) {
        if (instituteId != null) {
            Major major = new Major();
            major.setInstituteId(instituteId);
            major.setIsDelete((short) 2);
            List<Major> list = schoolService.findMajorByInstitute(major);

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", list));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_findClassList")
    public void back_api_findClassList(Integer majorId, Integer gradeId, HttpServletResponse response) {
        if (majorId != null && gradeId != null) {
            Class clazz = new Class();
            if (gradeId != 0) {
                clazz.setGradeId(gradeId);
            }
            clazz.setMajorId(majorId);
            clazz.setIsDelete((short) 2);
            List<Class> list = schoolService.findClassByMajorAndGrade(clazz);

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", list));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_findSchoolList")
    public void back_api_findSchoolList(HttpServletResponse response) {
        School school = new School();
        school.setIsDelete((short) 2);
        List<School> list = schoolService.findSchoolList(school);

        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", list));
    }

    @RequestMapping("/back_api_findGradeList")
    public void back_api_findGradeList(HttpServletResponse response) {
        Grade grade = new Grade();
        grade.setIsDelete((short) 2);
        List<Grade> list = schoolService.findGradeList(grade);

        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", list));
    }

    @RequestMapping("/back_api_deleteInstitute")
    public void back_api_deleteInstitute(Integer id, HttpServletResponse response) {
        if (id != null) {

            if (schoolService.getUserCountBySchoolInfo(null, id, null, null, null) > 0) {
                ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "该级别下存在学生， 无法删除", null));
                return;
            }

            Institute institute = new Institute();
            institute.setId(id);
            institute.setDeleteTime(new Date());
            institute.setIsDelete((short) 1);
            schoolService.updateInstitue(institute);

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", null));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_deleteMajor")
    public void back_api_deleteMajor(Integer id, HttpServletResponse response) {
        if (id != null) {

            if (schoolService.getUserCountBySchoolInfo(null, null, id, null, null) > 0) {
                ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "该级别下存在学生， 无法删除", null));
                return;
            }

            Major major = new Major();
            major.setId(id);
            major.setDeleteTime(new Date());
            major.setIsDelete((short) 1);
            schoolService.updateMajor(major);

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", null));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_deleteClass")
    public void back_api_deleteClass(Integer id, HttpServletResponse response) {
        if (id != null) {

            if (schoolService.getUserCountBySchoolInfo(null, null, null, null, id) > 0) {
                ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "该级别下存在学生， 无法删除", null));
                return;
            }

            Class clazz = new Class();
            clazz.setId(id);
            clazz.setDeleteTime(new Date());
            clazz.setIsDelete((short) 1);
            schoolService.updateClass(clazz);

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", null));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_updateInstitute")
    public void back_api_updateInstitute(Integer id, String name, HttpServletResponse response) {
        if (id != null && StringUtils.isNotBlank(name)) {

            Institute institute = new Institute();
            institute.setId(id);
            institute.setName(name);
            schoolService.updateInstitue(institute);

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", null));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_updateMajor")
    public void back_api_updateMajor(Integer id, String name, HttpServletResponse response) {
        if (id != null && StringUtils.isNotBlank(name)) {

            Major major = new Major();
            major.setId(id);
            major.setName(name);
            schoolService.updateMajor(major);

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", null));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_updateClass")
    public void back_api_updateClass(Integer id, String name, HttpServletResponse response) {
        if (id != null && StringUtils.isNotBlank(name)) {

            Class clazz = new Class();
            clazz.setId(id);
            clazz.setName(name);
            schoolService.updateClass(clazz);

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", null));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_updateStudentNumber")
    public void back_api_updateStudentNumber(Integer id, String stuNumber, Integer instituteId, Integer majorId,
                                             Integer classId, Integer gradeId, String institute, String major, String clazz, String grade,
                                             HttpServletResponse response) {
        if (instituteId != null && majorId != null && classId != null && gradeId != null
                && StringUtils.isNotBlank(stuNumber) && StringUtils.isNotBlank(institute)
                && StringUtils.isNotBlank(major) && StringUtils.isNotBlank(clazz) && StringUtils.isNotBlank(grade)) {

            StudentInfo studentInfo = schoolService.getStudentInfoById(id);

            if (studentInfo == null) {
                ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "原学号记录不存在", null));
                return;
            }

            if (schoolService.getStudentNumberCount(studentInfo.getSchoolId(), instituteId, majorId, gradeId, classId,
                    stuNumber) > 0) {
                ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "学号已经存在", null));
                return;
            }

            studentInfo.setClassId(classId);
            studentInfo.setClazz(clazz);
            studentInfo.setGrade(grade);
            studentInfo.setGradeId(gradeId);
            studentInfo.setInstitute(institute);
            studentInfo.setInstituteId(instituteId);
            studentInfo.setMajor(major);
            studentInfo.setMajorId(majorId);
            studentInfo.setStuNum(stuNumber);
            studentInfo.setUpdateTime(new Date());
            schoolService.updateStudentInfo(studentInfo);

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", null));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_getStuNumberById")
    public void back_api_getStuNumberById(Integer id, HttpServletResponse response) {
        if (id != null) {

            StudentInfo studentInfo = schoolService.getStudentInfoById(id);

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", studentInfo));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_deleteStudentNumber")
    public void back_api_deleteStudentNumber(Integer id, HttpServletResponse response) {
        if (id != null) {

            StudentInfo studentInfo = new StudentInfo();
            studentInfo.setId(id);
            studentInfo.setIsDelete((short) 1);
            studentInfo.setDeleteTime(new Date());
            schoolService.updateStudentInfo(studentInfo);

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", studentInfo));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_findSchoolList_a")
    public void back_api_findSchoolList_a(Integer page, Integer pageSize, String name, HttpServletResponse response) {
        if (page != null && pageSize != null) {
            PageLimitHolderFilter.setContext(page, pageSize, null);

            List<Map<String, Object>> list = schoolService.findSchoolInfoList(name);

            Map<String, Object> map = new HashMap<>();
            map.put("list", list);
            map.put("totalCount", PageLimitHolderFilter.getContext().getTotalCount());

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", map));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_addSchool_a")
    public void back_api_addSchool_a(String name, String username, String password, HttpServletResponse response) {
        if (StringUtils.isNotBlank(name) && StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)) {

            if (schoolService.getSchoolCount(name) > 0) {
                ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "学校已经存在", null));
                return;
            }

            if (schoolService.getSchoolAccountCount(username) > 0) {
                ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "账号已经存在", null));
                return;
            }

            School school = new School();
            school.setCreateTime(new Date());
            school.setIsDelete((short) 2);
            school.setName(name);
            schoolService.addSchool(school);

            String random = StringUtil.getStringRandom(6);
            Account account = new Account();
            account.setCreateTime(new Date());
            account.setIsDetele((short) 2);
            account.setPassword(StringUtil.MD5Encode(password + random));
            account.setSalt(random);
            account.setSchoolId(school.getId());
            account.setStatus((short) 1);
            account.setType((short) 2);
            account.setUsername(username);
            schoolService.addAccount(account);

            // 学校用户拥有创建临时群组的权限(发送一次性通知)
            GroupTmpCreatePermission permission = new GroupTmpCreatePermission();
            permission.setSchoolId(school.getId());
            permission.setUserId(account.getId());
            permission.setUserType(1);// 学校用户
            permission.setIsDelete((short) 2);
            groupTmpCreatePermissionService.addGroupTmpCreatePermission(permission);

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", null));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_updateSchool_a")
    public void back_api_updateSchool_a(Integer schoolId, Integer accountId, String name, String username,
                                        String password, HttpServletResponse response) {
        if (schoolId != null && accountId != null && StringUtils.isNotBlank(name) && StringUtils.isNotBlank(username)) {

            School originSchool = schoolService.getSchoolById(schoolId);

            if (originSchool != null) {
                if (!originSchool.getName().equals(name)) {
                    if (schoolService.getSchoolCount(name) > 0) {
                        ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "学校已经存在", null));
                        return;
                    }
                }
            } else {
                ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "学校不存在", null));
                return;
            }

            School school = new School();
            school.setId(schoolId);
            school.setName(name);
            schoolService.updateSchool(school);

            if (accountId == 0) {
                if (StringUtils.isBlank(password)) {
                    ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
                    return;
                }
                String random = StringUtil.getStringRandom(6);
                Account account = new Account();
                account.setCreateTime(new Date());
                account.setIsDetele((short) 2);
                account.setPassword(StringUtil.MD5Encode(password + random));
                account.setSalt(random);
                account.setSchoolId(schoolId);
                account.setStatus((short) 1);
                account.setType((short) 2);
                account.setUsername(username);
                schoolService.addAccount(account);
            } else {
                String random = StringUtil.getStringRandom(6);
                Account account = new Account();
                account.setId(accountId);
                account.setPassword(StringUtil.MD5Encode(password + random));
                account.setSalt(random);
                account.setUsername(username);
                schoolService.updateAccount(account);
            }

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", null));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_importSchoolInstituteInfo_s")
    public void back_api_importSchoolInstituteInfo_s(String fileId, HttpServletResponse response) {
        if (StringUtils.isNotBlank(fileId)) {

            boolean result = schoolService.importSchoolInstituteInfoList(fileId);

            if (result) {
                ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "导入成功", null));
            } else {
                ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "导入失败", null));
            }
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_importInstituteInfo_s")
    public void back_api_importInstituteInfo_s(Integer schoolId, String fileId, HttpServletResponse response) {
        if (StringUtils.isNotBlank(fileId) && schoolId != null) {

            boolean result = schoolService.importInstituteInfoList(schoolId, fileId);

            if (result) {
                ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "导入成功", null));
            } else {
                ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "导入失败", null));
            }
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_importStudentNumbber_s")
    public void back_api_importStudentNumbber_s(Integer schoolId, String fileId, HttpServletResponse response) {
        if (StringUtils.isNotBlank(fileId) && schoolId != null) {

            String result = schoolService.importStudentNumberList(fileId, schoolId);

            if ("success".equals(result)) {
                ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "导入成功", null));
            } else {
                ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, result, null));
            }
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    /**
     * 导入学校通讯录
     *
     * @param schoolId
     * @param fileId
     * @param response
     */
    @RequestMapping("back_api_importSchoolAddressBookList")
    public void back_api_importSchoolAddressBookList(Integer schoolId, String fileId, HttpServletResponse response) {
        if (StringUtils.isNotBlank(fileId) && schoolId != null) {

            boolean result = schoolService.importSchoolAddressBookList(fileId, schoolId);

            if (result) {
                ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "导入成功", null));
            } else {
                ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "导入失败", null));
            }
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    /**
     * 上传学号excel文件搜索学生
     */
    @RequestMapping("back_api_importStuNumberExcel")
    public void back_api_importStuNumberExcel(String fileId, HttpServletResponse response) {
        if (StringUtils.isNotBlank(fileId)) {
            String stuNumberStr = schoolService.importStuNumberExcel(fileId);
            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "导入成功", stuNumberStr));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_importClassroomExcel")
    public void back_api_importClassroomExcel(Integer schoolId, String fileId, HttpServletResponse response) {
        if (StringUtils.isNotBlank(fileId)) {
            schoolService.importClassroomExcel(schoolId, fileId);
            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "导入成功"));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }
}
