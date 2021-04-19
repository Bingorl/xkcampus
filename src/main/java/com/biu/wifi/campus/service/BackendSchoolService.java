package com.biu.wifi.campus.service;

import com.biu.wifi.campus.Tool.ExcelUtils;
import com.biu.wifi.campus.dao.*;
import com.biu.wifi.campus.dao.model.Class;
import com.biu.wifi.campus.dao.model.*;
import com.biu.wifi.campus.dao.model.UserCriteria.Criteria;
import com.biu.wifi.campus.daoEx.BackendSchoolMapperEx;
import com.biu.wifi.campus.exception.ImportStudentInfoException;
import com.biu.wifi.component.datastore.FileSupportService;
import com.biu.wifi.component.datastore.fileio.FileIoEntity;
import com.biu.wifi.core.support.orm.mybatis3.util.IbatisServiceUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class BackendSchoolService {

    private Log logger = LogFactory.getLog(BackendSchoolService.class);

    @Autowired
    private GradeMapper gradeMapper;

    @Autowired
    private InstituteMapper instituteMapper;

    @Autowired
    private MajorMapper majorMapper;

    @Autowired
    private ClassMapper classMapper;

    @Autowired
    private BackendSchoolMapperEx schoolMapperEx;

    @Autowired
    private StudentInfoMapper studentInfoMapper;

    @Autowired
    private SchoolMapper schoolMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private FileSupportService fileSupportService;

    @Autowired
    private SchoolAddressBookMapper schoolAddressBookMapper;

    @Autowired
    private SchoolDepartMapper schoolDepartMapper;
    @Autowired
    private ClassroomBuildingMapper classroomBuildingMapper;
    @Autowired
    private ClassroomMapper classroomMapper;
    @Autowired
    private ClassroomTypeMapper classroomTypeMapper;

    public Grade getGrade(Grade entity) {
        return IbatisServiceUtils.get(entity, gradeMapper);
    }

    public Grade getGradeById(Integer id) {
        return gradeMapper.selectByPrimaryKey(id);
    }

    public School getSchoolById(Integer id) {
        return schoolMapper.selectByPrimaryKey(id);
    }

    public Institute getInstituteById(Integer id) {
        return instituteMapper.selectByPrimaryKey(id);
    }

    public Major getMajorById(Integer id) {
        return majorMapper.selectByPrimaryKey(id);
    }

    public Class getClassById(Integer id) {
        return classMapper.selectByPrimaryKey(id);
    }

    public void addInstitute(Institute entity) {
        instituteMapper.insertSelective(entity);
    }

    public void addMajor(Major entity) {
        majorMapper.insertSelective(entity);
    }

    public void addGrade(Grade entity) {
        gradeMapper.insertSelective(entity);
    }

    public void addClass(Class entity) {
        classMapper.insertSelective(entity);
    }

    public void addStudentNumber(StudentInfo entity) {
        studentInfoMapper.insertSelective(entity);
    }

    public List<Map<String, Object>> findStudentNumbers(Integer schoolId, String stuNumber, Short status) {
        return schoolMapperEx.findStudentNumbers(schoolId, stuNumber, status);
    }

    public List<Institute> findInstituteBySchool(Institute entity) {
        return IbatisServiceUtils.find(entity, instituteMapper, "id asc");
    }

    public List<Major> findMajorByInstitute(Major entity) {
        return IbatisServiceUtils.find(entity, majorMapper, "id asc");
    }

    public List<Class> findClassByMajorAndGrade(Class entity) {
        return IbatisServiceUtils.find(entity, classMapper, "id asc");
    }

    public List<School> findSchoolList(School entity) {
        return IbatisServiceUtils.find(entity, schoolMapper, "id asc");
    }

    public List<Grade> findGradeList(Grade entity) {
        return IbatisServiceUtils.find(entity, gradeMapper, "id asc");
    }

    public int getUserCountBySchoolInfo(Integer schoolId, Integer InstituteId, Integer majorId, Integer gradeId,
                                        Integer classId) {
        UserCriteria userCriteria = new UserCriteria();
        Criteria criteria = userCriteria.createCriteria();
        criteria.andIsDeleteEqualTo((short) 2);
        criteria.andIsAuthEqualTo((short) 1);

        if (schoolId != null) {
            criteria.andSchoolIdEqualTo(schoolId);
        }

        if (InstituteId != null) {
            criteria.andInstituteIdEqualTo(InstituteId);
        }

        if (majorId != null) {
            criteria.andMajorIdEqualTo(majorId);
        }

        if (gradeId != null) {
            criteria.andGradeIdEqualTo(gradeId);
        }

        if (classId != null) {
            criteria.andClassIdEqualTo(classId);
        }

        return new Long(userMapper.countByExample(userCriteria)).intValue();
    }

    public void updateSchool(School entity) {
        schoolMapper.updateByPrimaryKeySelective(entity);
    }

    public void updateAccount(Account entity) {
        accountMapper.updateByPrimaryKeySelective(entity);
    }

    public void updateInstitue(Institute entity) {
        instituteMapper.updateByPrimaryKeySelective(entity);
    }

    public void updateMajor(Major entity) {
        majorMapper.updateByPrimaryKeySelective(entity);
    }

    public void updateGrade(Grade entity) {
        gradeMapper.updateByPrimaryKeySelective(entity);
    }

    public void updateClass(Class entity) {
        classMapper.updateByPrimaryKeySelective(entity);
    }

    public int getStudentNumberCount(Integer schoolId, Integer instituteId, Integer majorId, Integer gradeId,
                                     Integer classId, String studentNumber) {
        StudentInfoCriteria studentInfoCriteria = new StudentInfoCriteria();
        StudentInfoCriteria.Criteria criteria = studentInfoCriteria.createCriteria();
        criteria.andSchoolIdEqualTo(schoolId);
        criteria.andInstituteIdEqualTo(instituteId);
        criteria.andMajorIdEqualTo(majorId);
        criteria.andGradeIdEqualTo(gradeId);
        criteria.andClassIdEqualTo(classId);
        criteria.andStuNumEqualTo(studentNumber);
        criteria.andIsDeleteEqualTo((short) 2);

        return new Long(studentInfoMapper.countByExample(studentInfoCriteria)).intValue();
    }

    public StudentInfo getStudentInfoById(Integer id) {
        return studentInfoMapper.selectByPrimaryKey(id);
    }

    public void updateStudentInfo(StudentInfo entity) {
        studentInfoMapper.updateByPrimaryKeySelective(entity);
    }

    public int getSchoolCount(String name) {
        SchoolCriteria schoolCriteria = new SchoolCriteria();
        SchoolCriteria.Criteria criteria = schoolCriteria.createCriteria();
        criteria.andNameEqualTo(name);
        criteria.andIsDeleteEqualTo((short) 2);

        return new Long(schoolMapper.countByExample(schoolCriteria)).intValue();
    }

    public List<School> getSchool(String name) {
        SchoolCriteria schoolCriteria = new SchoolCriteria();
        SchoolCriteria.Criteria criteria = schoolCriteria.createCriteria();
        criteria.andNameEqualTo(name);
        criteria.andIsDeleteEqualTo((short) 2);

        return schoolMapper.selectByExample(schoolCriteria);
    }

    public int getSchoolAccountCount(String username) {
        AccountCriteria accountCriteria = new AccountCriteria();
        AccountCriteria.Criteria criteria = accountCriteria.createCriteria();
        criteria.andUsernameEqualTo(username);
        criteria.andIsDeteleEqualTo((short) 2);

        return new Long(accountMapper.countByExample(accountCriteria)).intValue();
    }

    public int getInstituteCount(Integer schoolId, String name) {
        InstituteCriteria instituteCriteria = new InstituteCriteria();
        InstituteCriteria.Criteria criteria = instituteCriteria.createCriteria();
        criteria.andSchoolIdEqualTo(schoolId);
        criteria.andNameEqualTo(name);
        criteria.andIsDeleteEqualTo((short) 2);

        return new Long(instituteMapper.countByExample(instituteCriteria)).intValue();
    }

    public List<Institute> getInstitute(Integer schoolId, String name) {
        InstituteCriteria instituteCriteria = new InstituteCriteria();
        InstituteCriteria.Criteria criteria = instituteCriteria.createCriteria();
        criteria.andSchoolIdEqualTo(schoolId);
        criteria.andNameEqualTo(name);
        criteria.andIsDeleteEqualTo((short) 2);

        return instituteMapper.selectByExample(instituteCriteria);
    }

    public int getMajorCount(Integer instituteId, String name) {
        MajorCriteria majorCriteria = new MajorCriteria();
        MajorCriteria.Criteria criteria = majorCriteria.createCriteria();
        criteria.andInstituteIdEqualTo(instituteId);
        criteria.andNameEqualTo(name);
        criteria.andIsDeleteEqualTo((short) 2);

        return new Long(majorMapper.countByExample(majorCriteria)).intValue();
    }

    public List<Major> getMajor(Integer instituteId, String name) {
        MajorCriteria majorCriteria = new MajorCriteria();
        MajorCriteria.Criteria criteria = majorCriteria.createCriteria();
        criteria.andInstituteIdEqualTo(instituteId);
        criteria.andNameEqualTo(name);
        criteria.andIsDeleteEqualTo((short) 2);

        return majorMapper.selectByExample(majorCriteria);
    }

    public int getGradeCount(String name) {
        GradeCriteria gradeCriteria = new GradeCriteria();
        GradeCriteria.Criteria criteria = gradeCriteria.createCriteria();
        criteria.andNameEqualTo(name);
        criteria.andIsDeleteEqualTo((short) 2);

        return new Long(gradeMapper.countByExample(gradeCriteria)).intValue();
    }

    public List<Grade> getGrade(String name) {
        GradeCriteria gradeCriteria = new GradeCriteria();
        GradeCriteria.Criteria criteria = gradeCriteria.createCriteria();
        criteria.andNameEqualTo(name);
        criteria.andIsDeleteEqualTo((short) 2);

        return gradeMapper.selectByExample(gradeCriteria);
    }

    public int getClassCount(String name, Integer majorId, Integer gradeId) {
        ClassCriteria classCriteria = new ClassCriteria();
        ClassCriteria.Criteria criteria = classCriteria.createCriteria();
        criteria.andNameEqualTo(name);
        criteria.andMajorIdEqualTo(majorId);
        criteria.andGradeIdEqualTo(gradeId);
        criteria.andIsDeleteEqualTo((short) 2);

        return new Long(classMapper.countByExample(classCriteria)).intValue();
    }

    public List<Class> getClass(String name, Integer majorId, Integer gradeId) {
        ClassCriteria classCriteria = new ClassCriteria();
        ClassCriteria.Criteria criteria = classCriteria.createCriteria();
        criteria.andNameEqualTo(name);
        criteria.andMajorIdEqualTo(majorId);
        criteria.andGradeIdEqualTo(gradeId);
        criteria.andIsDeleteEqualTo((short) 2);

        return classMapper.selectByExample(classCriteria);
    }

    public List<Map<String, Object>> findSchoolInfoList(String name) {
        return schoolMapperEx.findSchoolInfoList(name);
    }

    public void addSchool(School entity) {
        schoolMapper.insertSelective(entity);
    }

    public void addAccount(Account entity) {
        accountMapper.insertSelective(entity);
    }

    public boolean importInstituteInfoList(Integer schoolId, String fileId) {

        FileIoEntity fileIoEntity = fileSupportService.get(fileId);

        if (fileIoEntity == null) {
            return false;
        }

        String result = parseInstituteExcelFile(fileIoEntity.getContent(), schoolId, fileId);

        fileSupportService.remove(fileId);

        if ("success".equals(result)) {
            return true;
        } else {
            return false;
        }
    }

    private String parseInstituteExcelFile(byte[] upload, Integer schoolId, String fileId) {
        try {
            School school = this.getSchoolById(schoolId);

            if (school == null) {
                return "failure";
            }

            ExcelUtils excel = ExcelUtils.getInstance(upload, fileId);

            int rowCount = excel.getSheetRow(0);

            for (int i = 0; i < rowCount; i++) {
//					String schoolName = excel.read(0, 0, i);
//					School school = null;
//					
//					if (StringUtils.isNotBlank(schoolName)) {
//						if (this.getSchoolCount(schoolName) > 0) {
//							school = this.getSchool(schoolName).get(0);
//						} else {
//							school = new School();
//							school.setCreateTime(new Date());
//							school.setIsDelete((short) 2);
//							school.setName(schoolName);
//							this.addSchool(school);
//						}
//					} else {
//						continue;
//					}

                String instituteName = excel.read(0, 0, i);
                Institute institute = null;

                if (StringUtils.isNotBlank(instituteName)) {
                    if (this.getInstituteCount(school.getId(), instituteName) > 0) {
                        institute = this.getInstitute(school.getId(), instituteName).get(0);
                    } else {
                        institute = new Institute();
                        institute.setCreateTime(new Date());
                        institute.setIsDelete((short) 2);
                        institute.setName(instituteName);
                        institute.setSchoolId(school.getId());
                        this.addInstitute(institute);
                    }
                } else {
                    continue;
                }

                String majorName = excel.read(0, 1, i);
                Major major = null;

                if (StringUtils.isNotBlank(majorName)) {
                    if (this.getMajorCount(institute.getId(), majorName) > 0) {
                        major = this.getMajor(institute.getId(), majorName).get(0);
                    } else {
                        major = new Major();
                        major.setCreateTime(new Date());
                        major.setIsDelete((short) 2);
                        major.setName(majorName);
                        major.setInstituteId(institute.getId());
                        this.addMajor(major);
                    }
                } else {
                    continue;
                }

                String gradeName = excel.read(0, 2, i);
                Grade grade = null;

                if (StringUtils.isNotBlank(gradeName)) {

                    int index = gradeName.indexOf(".");
                    if (index != -1) {
                        gradeName = gradeName.substring(0, index);
                    }

                    if (this.getGradeCount(gradeName) > 0) {
                        grade = this.getGrade(gradeName).get(0);
                    } else {
                        grade = new Grade();
                        grade.setCreateTime(new Date());
                        grade.setIsDelete((short) 2);
                        grade.setName(gradeName);
                        this.addGrade(grade);
                    }
                } else {
                    continue;
                }

                String clazzName = excel.read(0, 3, i);
                Class clazz = null;

                if (StringUtils.isNotBlank(clazzName)) {
                    if (this.getClassCount(clazzName, major.getId(), grade.getId()) > 0) {
                        clazz = this.getClass(clazzName, major.getId(), grade.getId()).get(0);
                    } else {
                        clazz = new Class();
                        clazz.setCreateTime(new Date());
                        clazz.setIsDelete((short) 2);
                        clazz.setName(clazzName);
                        clazz.setMajorId(major.getId());
                        clazz.setGradeId(grade.getId());
                        clazz.setGrade(gradeName);
                        this.addClass(clazz);
                    }
                } else {
                    continue;
                }
            }

            return "success";
        } catch (Exception e) {
            logger.error(e);
            // roll back transaction manually
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return "failure";
        }
    }

    public String importStudentNumberList(String fileId, Integer schoolId) {

        FileIoEntity fileIoEntity = fileSupportService.get(fileId);

        if (fileIoEntity == null) {
            return "文件不存在";
        }

        String result = parseStudentNumberExcelFile(fileIoEntity.getContent(), schoolId, fileId);

        fileSupportService.remove(fileId);

        return result;
    }

    private String parseStudentNumberExcelFile(byte[] upload, Integer schoolId, String field) {

        School school = this.getSchoolById(schoolId);

        if (school == null) {
            return "failure";
        }

        try {
            ExcelUtils excel = ExcelUtils.getInstance(upload, field);

            int rowCount = excel.getSheetRow(0);

            for (int i = 0; i < rowCount; i++) {
//				String schoolName = excel.read(0, 0, i);
//				School school = null;
//				
//				if (StringUtils.isNotBlank(schoolName)) {
//					if (this.getSchoolCount(schoolName) > 0) {
//						school = this.getSchool(schoolName).get(0);
//					} else {
//						throw new ImportStudentInfoException("第" + (i + 1) + "行，第1列学校不存在");
//					}
//				} else {
//					continue;
//				}

                String instituteName = excel.read(0, 0, i);
                Institute institute = null;

                if (StringUtils.isNotBlank(instituteName)) {
                    if (this.getInstituteCount(school.getId(), instituteName) > 0) {
                        institute = this.getInstitute(school.getId(), instituteName).get(0);
                    } else {
                        throw new ImportStudentInfoException("第" + (i + 1) + "行，第1列学院不存在");
                    }
                } else {
                    continue;
                }

                String majorName = excel.read(0, 1, i);
                Major major = null;

                if (StringUtils.isNotBlank(majorName)) {
                    if (this.getMajorCount(institute.getId(), majorName) > 0) {
                        major = this.getMajor(institute.getId(), majorName).get(0);
                    } else {
                        throw new ImportStudentInfoException("第" + (i + 1) + "行，第2列专业不存在");
                    }
                } else {
                    continue;
                }

                String gradeName = excel.read(0, 2, i);
                Grade grade = null;

                if (StringUtils.isNotBlank(gradeName)) {

                    int index = gradeName.indexOf(".");
                    if (index != -1) {
                        gradeName = gradeName.substring(0, index);
                    }

                    if (this.getGradeCount(gradeName) > 0) {
                        grade = this.getGrade(gradeName).get(0);
                    } else {
                        throw new ImportStudentInfoException("第" + (i + 1) + "行，第3列年级不存在");
                    }
                } else {
                    continue;
                }

                String clazzName = excel.read(0, 3, i);
                Class clazz = null;

                if (StringUtils.isNotBlank(clazzName)) {
                    if (this.getClassCount(clazzName, major.getId(), grade.getId()) > 0) {
                        clazz = this.getClass(clazzName, major.getId(), grade.getId()).get(0);
                    } else {
                        throw new ImportStudentInfoException("第" + (i + 1) + "行，第4列班级不存在");
                    }
                } else {
                    continue;
                }

                String stuNumber = excel.read(0, 4, i);

                if (stuNumber.indexOf("E") != -1 || stuNumber.indexOf("e") != -1) {
                    throw new ImportStudentInfoException("请设置学号单元格为文本格式并重新填入学号");
                }

                if (this.getStudentNumberCount(school.getId(), institute.getId(), major.getId(), grade.getId(),
                        clazz.getId(), stuNumber) == 0) {
                    StudentInfo studentInfo = new StudentInfo();
                    studentInfo.setClassId(clazz.getId());
                    studentInfo.setGradeId(grade.getId());
                    studentInfo.setInstituteId(institute.getId());
                    studentInfo.setMajorId(major.getId());
                    studentInfo.setSchoolId(school.getId());
                    studentInfo.setStuNum(stuNumber);
                    studentInfo.setIsDelete((short) 2);
                    studentInfo.setInstitute(instituteName);
                    studentInfo.setMajor(majorName);
                    studentInfo.setGrade(gradeName);
                    studentInfo.setClazz(clazzName);
                    studentInfo.setCreateTime(new Date());
                    studentInfo.setSchool(school.getName());
                    this.addStudentNumber(studentInfo);
                }
            }

            return "success";
        } catch (ImportStudentInfoException e) {
            logger.error(e);
            // roll back transaction manually
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return e.getMessage();
        } catch (Exception e) {
            logger.error(e);
            // roll back transaction manually
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return "failure";
        }
    }

    public boolean importSchoolInstituteInfoList(String fileId) {

        FileIoEntity fileIoEntity = fileSupportService.get(fileId);

        if (fileIoEntity == null) {
            return false;
        }

        String result = parseSchoolInstituteExcelFile(fileIoEntity.getContent(), fileId);

        fileSupportService.remove(fileId);

        if ("success".equals(result)) {
            return true;
        } else {
            return false;
        }
    }

    private String parseSchoolInstituteExcelFile(byte[] upload, String field) {
        try {
            ExcelUtils excel = ExcelUtils.getInstance(upload, field);

            int rowCount = excel.getSheetRow(0);

            for (int i = 0; i < rowCount; i++) {
                String schoolName = excel.read(0, 0, i);
                School school = null;

                if (StringUtils.isNotBlank(schoolName)) {
                    if (this.getSchoolCount(schoolName) > 0) {
                        school = this.getSchool(schoolName).get(0);
                    } else {
                        school = new School();
                        school.setCreateTime(new Date());
                        school.setIsDelete((short) 2);
                        school.setName(schoolName);
                        this.addSchool(school);
                    }
                } else {
                    continue;
                }

                String instituteName = excel.read(0, 1, i);
                Institute institute = null;

                if (StringUtils.isNotBlank(instituteName)) {
                    if (this.getInstituteCount(school.getId(), instituteName) > 0) {
                        institute = this.getInstitute(school.getId(), instituteName).get(0);
                    } else {
                        institute = new Institute();
                        institute.setCreateTime(new Date());
                        institute.setIsDelete((short) 2);
                        institute.setName(instituteName);
                        institute.setSchoolId(school.getId());
                        this.addInstitute(institute);
                    }
                } else {
                    continue;
                }

                String majorName = excel.read(0, 2, i);
                Major major = null;

                if (StringUtils.isNotBlank(majorName)) {
                    if (this.getMajorCount(institute.getId(), majorName) > 0) {
                        major = this.getMajor(institute.getId(), majorName).get(0);
                    } else {
                        major = new Major();
                        major.setCreateTime(new Date());
                        major.setIsDelete((short) 2);
                        major.setName(majorName);
                        major.setInstituteId(institute.getId());
                        this.addMajor(major);
                    }
                } else {
                    continue;
                }

                String gradeName = excel.read(0, 3, i);
                Grade grade = null;

                if (StringUtils.isNotBlank(gradeName)) {

                    int index = gradeName.indexOf(".");
                    if (index != -1) {
                        gradeName = gradeName.substring(0, index);
                    }

                    if (this.getGradeCount(gradeName) > 0) {
                        grade = this.getGrade(gradeName).get(0);
                    } else {
                        grade = new Grade();
                        grade.setCreateTime(new Date());
                        grade.setIsDelete((short) 2);
                        grade.setName(gradeName);
                        this.addGrade(grade);
                    }
                } else {
                    continue;
                }

                String clazzName = excel.read(0, 4, i);
                Class clazz = null;

                if (StringUtils.isNotBlank(clazzName)) {
                    if (this.getClassCount(clazzName, major.getId(), grade.getId()) > 0) {
                        clazz = this.getClass(clazzName, major.getId(), grade.getId()).get(0);
                    } else {
                        clazz = new Class();
                        clazz.setCreateTime(new Date());
                        clazz.setIsDelete((short) 2);
                        clazz.setName(clazzName);
                        clazz.setMajorId(major.getId());
                        clazz.setGradeId(grade.getId());
                        clazz.setGrade(gradeName);
                        this.addClass(clazz);
                    }
                } else {
                    continue;
                }
            }

            return "success";
        } catch (Exception e) {
            logger.error(e);
            // roll back transaction manually
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return "failure";
        }
    }

    /**
     * 导入学校通讯录
     *
     * @param fileId
     * @param schoolId
     * @return
     */
    public boolean importSchoolAddressBookList(String fileId, Integer schoolId) {

        FileIoEntity fileIoEntity = fileSupportService.get(fileId);

        if (fileIoEntity == null) {
            return false;
        }

        String result = parseSchoolAddressBookExcelFile(fileIoEntity.getContent(), fileId, schoolId);

        fileSupportService.remove(fileId);

        if ("success".equals(result)) {
            return true;
        } else {
            return false;
        }
    }

    private String parseSchoolAddressBookExcelFile(byte[] upload, String fileId, Integer schoolId) {
        try {
            ExcelUtils excel = ExcelUtils.getInstance(upload, fileId);

            int rowCount = 0, sheetIndex = 0;

            {
                // 此代码块是为了避免用户创建表格时不时选用的第一个sheet来进行操作的
                int sheetCount = excel.getSheetCout();
                if (sheetCount > 0) {
                    for (int i = 0; i < sheetCount; i++) {
                        rowCount = excel.getSheetRow(i);
                        if (rowCount > 0) {
                            sheetIndex = i;
                            break;
                        }
                    }
                }
            }

            final String departDefaultName = "默认部门";

            // 去掉第一行的标题，从第二行开始才是通讯录的正文
            for (int i = 2; i < rowCount; i++) {
                // 姓名
                String realName = excel.readByCheck(sheetIndex, 0, i);
                // 办公地址
                String officePosition = excel.readByCheck(sheetIndex, 1, i);
                // 手机号码
                String tel = excel.readByCheck(sheetIndex, 2, i);
                // 办公室电话
                String phone = excel.readByCheck(sheetIndex, 3, i);
                // 专业
                String majorName = excel.readByCheck(sheetIndex, 4, i);
                // 部门名称
                String departName = excel.readByCheck(sheetIndex, 5, i);
                // 职位名称
                String positionName = excel.readByCheck(sheetIndex, 6, i);

                // 去掉空格
                realName = StringUtils.deleteWhitespace(realName);
                if ("姓名".equals(realName)) {
                    // 防止表头被插入进数据库
                    continue;
                }

                // 没有明确部门分类就归属到默认部门中（一级分类）
                SchoolDepart depart = null;
                SchoolDepartCriteria departEx = new SchoolDepartCriteria();
                SchoolDepartCriteria.Criteria departCria = departEx.createCriteria();
                departCria.andSchoolIdEqualTo(schoolId).andIsDeleteEqualTo((short) 2);
                List<SchoolDepart> departs = null;
                if (StringUtils.isBlank(departName) || "null".equals(departName)) {
                    departCria.andNameEqualTo(departDefaultName);
                    departs = schoolDepartMapper.selectByExample(departEx);
                    if (CollectionUtils.isEmpty(departs)) {
                        // 新建默认部门
                        depart = new SchoolDepart();
                        depart.setName(departDefaultName);
                        depart.setSchoolId(schoolId);
                        depart.setLevel((short) 1);
                        depart.setCreateTime(new Date());
                        schoolDepartMapper.insertSelective(depart);
                    } else {
                        depart = departs.get(0);
                    }

                } else {
                    departCria.andNameEqualTo(departName);
                    departs = schoolDepartMapper.selectByExample(departEx);
                    if (CollectionUtils.isEmpty(departs)) {
                        // 新建部门
                        depart = new SchoolDepart();
                        depart.setName(departName);
                        depart.setSchoolId(schoolId);
                        depart.setCreateTime(new Date());
                        schoolDepartMapper.insertSelective(depart);
                    } else {
                        depart = departs.get(0);
                    }
                }

                // 二级分类（部门或专业）
                int pid = depart.getId();
                if (StringUtils.isNotBlank(majorName) && !"null".equals(majorName)) {
                    SchoolDepartCriteria childDepartEx = new SchoolDepartCriteria();
                    SchoolDepartCriteria.Criteria childCria = childDepartEx.createCriteria();
                    childCria.andPidEqualTo(pid);
                    childCria.andIsDeleteEqualTo((short) 2);
                    childCria.andNameEqualTo(majorName);
                    List<SchoolDepart> childDeparts = schoolDepartMapper.selectByExample(childDepartEx);
                    if (CollectionUtils.isEmpty(childDeparts)) {
                        // 根据专业名称新建子部门
                        depart = new SchoolDepart();
                        depart.setName(majorName);
                        depart.setSchoolId(schoolId);
                        depart.setPid(pid);
                        depart.setLevel((short) 2);
                        depart.setCreateTime(new Date());
                        schoolDepartMapper.insertSelective(depart);
                    } else {
                        depart = childDeparts.get(0);
                    }

                    departName += "-" + depart.getName();
                }

                if (StringUtils.isBlank(realName) && StringUtils.isBlank(tel) && StringUtils.isBlank(phone)) {
                    //如果是无效数据，直接跳过
                    continue;
                }

                SchoolAddressBookCriteria schoolAddressBookEx = new SchoolAddressBookCriteria();
                SchoolAddressBookCriteria.Criteria schoolAddressBookCria = schoolAddressBookEx.createCriteria();
                schoolAddressBookCria.andSchoolIdEqualTo(schoolId);
                schoolAddressBookCria.andNameEqualTo(realName);
                schoolAddressBookCria.andDepartIdEqualTo(depart.getId());
                schoolAddressBookCria.andIsDeleteEqualTo((short) 2);

                List<SchoolAddressBook> addressBooks = schoolAddressBookMapper.selectByExample(schoolAddressBookEx);
                SchoolAddressBook addressBook = null;
                if (CollectionUtils.isEmpty(addressBooks)) {
                    addressBook = new SchoolAddressBook();
                    addressBook.setName(realName);
                    addressBook.setPhone(phone);
                    addressBook.setTel(tel);
                    addressBook.setSchoolId(schoolId);
                    addressBook.setDepartId(depart.getId());
                    addressBook.setDepartName(departName);
                    addressBook.setPositionName(positionName);
                    addressBook.setOfficePosition(officePosition);
                    addressBook.setCreateTime(new Date());
                    schoolAddressBookMapper.insertSelective(addressBook);
                } else {
                    addressBook = addressBooks.get(0);
                    addressBook.setName(realName);
                    addressBook.setPhone(phone);
                    addressBook.setTel(tel);
                    addressBook.setSchoolId(schoolId);
                    addressBook.setDepartId(depart.getId());
                    addressBook.setDepartName(departName);
                    addressBook.setPositionName(positionName);
                    addressBook.setOfficePosition(officePosition);
                    addressBook.setUpdateTime(new Date());
                    schoolAddressBookMapper.updateByPrimaryKeySelective(addressBook);
                }
            }

            return "success";
        } catch (Exception e) {
            logger.error(e);
            // roll back transaction manually
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return "failure";
        }
    }

    /**
     * 导入学生学号excel
     *
     * @param fileId
     * @return
     */
    public String importStuNumberExcel(String fileId) {

        FileIoEntity fileIoEntity = fileSupportService.get(fileId);

        if (fileIoEntity == null) {
            return "";
        }

        StringBuffer sb = new StringBuffer();
        try {
            ExcelUtils excel = ExcelUtils.getInstance(fileIoEntity.getContent(), fileId);
            int rowCount = excel.getSheetRow(0), sheetIndex = 0;

            for (int i = 0; i < rowCount; i++) {
                String stuNumber = excel.readByCheck(sheetIndex, 0, i);
                sb.append(stuNumber);
                if (i != rowCount - 1) {
                    sb.append(",");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        fileSupportService.remove(fileId);

        return sb.toString();
    }

    /**
     * 导入excel格式的教室数据
     *
     * @param schoolId
     * @param fileId
     */
    public void importClassroomExcel(Integer schoolId, String fileId) {
        FileIoEntity fileIoEntity = fileSupportService.get(fileId);

        if (fileIoEntity == null) {
            return;
        }

        try {
            ExcelUtils excel = ExcelUtils.getInstance(fileIoEntity.getContent(), fileId);
            int rowCount = excel.getSheetRow(0), sheetIndex = 0;

            // 去掉第一行的标题，从第二行开始才是正文
            for (int i = 1; i < rowCount; i++) {
                // 教学楼号
                String classroomBuildingNo = excel.readByCheck(sheetIndex, 2, i);
                // 教学楼名
                String classroomBuildingName = excel.readByCheck(sheetIndex, 3, i);
                // 教室号
                String classroomNo = excel.readByCheck(sheetIndex, 4, i);
                // 教室名
                String classroomName = excel.readByCheck(sheetIndex, 5, i);
                // 英文教室名
                String classroomEnName = excel.readByCheck(sheetIndex, 6, i);
                // 教室类型
                String classroomTypeName = excel.readByCheck(sheetIndex, 7, i);
                // 是否可借
                String isBorrow = excel.readByCheck(sheetIndex, 8, i);
                // 所在楼层
                String floor = excel.readByCheck(sheetIndex, 9, i);
                // 相近教室
                String nearBy = excel.readByCheck(sheetIndex, 10, i);
                // 上课座位数
                String seatCount = excel.readByCheck(sheetIndex, 11, i);
                // 考试座位数
                String exSeatCount = excel.readByCheck(sheetIndex, 12, i);
                // 考场最大座位数
                String maxExSeatCount = excel.readByCheck(sheetIndex, 13, i);
                // 系所
                String instituteId = excel.readByCheck(sheetIndex, 14, i);
                // 排课属性
                String classArrange = excel.readByCheck(sheetIndex, 15, i);
                // 排课优先级
                String classArrangeOrder = excel.readByCheck(sheetIndex, 16, i);
                // 是否是真实教室
                String trueClassroom = excel.readByCheck(sheetIndex, 17, i);
                // 教室状态
                String status = excel.readByCheck(sheetIndex, 18, i);
                // 组号
                String classroomGroupNo = excel.readByCheck(sheetIndex, 19, i);
                // 备注
                String remark = excel.readByCheck(sheetIndex, 20, i);

                ClassroomBuildingCriteria classroomBuildingEx = new ClassroomBuildingCriteria();
                classroomBuildingEx.setOrderByClause("create_time");
                classroomBuildingEx.createCriteria()
                        .andIsDeleteEqualTo((short) 2)
                        .andSchoolIdEqualTo(schoolId)
                        .andNoEqualTo(classroomBuildingNo)
                        .andNameEqualTo(classroomBuildingName);
                List<ClassroomBuilding> classroomBuildingList = classroomBuildingMapper.selectByExample(classroomBuildingEx);
                Integer classroomBuildingId;
                if (classroomBuildingList.isEmpty()) {
                    ClassroomBuilding classroomBuilding = new ClassroomBuilding();
                    classroomBuilding.setSchoolId(schoolId);
                    classroomBuilding.setNo(classroomBuildingNo);
                    classroomBuilding.setName(classroomBuildingName);
                    classroomBuilding.setCreateTime(new Date());
                    classroomBuilding.setIsDelete((short) 2);
                    classroomBuildingMapper.insertSelective(classroomBuilding);
                    classroomBuildingId = classroomBuilding.getId();
                } else {
                    classroomBuildingId = classroomBuildingList.get(0).getId();
                }

                ClassroomCriteria classroomEx = new ClassroomCriteria();
                classroomEx.setOrderByClause("create_time desc");
                classroomEx.createCriteria()
                        .andBuildingIdEqualTo(classroomBuildingId)
                        .andNoEqualTo(classroomNo)
                        .andNameEqualTo(classroomName);
                List<Classroom> classroomList = classroomMapper.selectByExample(classroomEx);

                ClassroomTypeCriteria classroomTypeEx = new ClassroomTypeCriteria();
                classroomTypeEx.setOrderByClause("create_time desc");
                classroomTypeEx.createCriteria()
                        .andIsDeleteEqualTo((short) 2)
                        .andSchoolIdEqualTo(schoolId)
                        .andNameEqualTo(classroomTypeName);
                List<ClassroomType> classroomTypeList = classroomTypeMapper.selectByExample(classroomTypeEx);
                Integer classroomTypeId;
                if (classroomTypeList.isEmpty()) {
                    logger.error("名为[" + classroomTypeName + "]的教室类型不存在");
                    continue;
                } else {
                    classroomTypeId = classroomTypeList.get(0).getId();
                }

                Classroom classroom = new Classroom();
                classroom.setBuildingId(classroomBuildingId);
                classroom.setNo(classroomNo);
                classroom.setName(classroomName);
                classroom.setTypeId(classroomTypeId);
                classroom.setSeatCount(StringUtils.isNotBlank(seatCount) ? Integer.valueOf(seatCount) : 0);
                classroom.setExSeatCount(StringUtils.isNotBlank(exSeatCount) ? Integer.valueOf(exSeatCount) : 0);
                if (StringUtils.equals("可用", status)) {
                    classroom.setStatus((short) 1);
                } else if (StringUtils.equals("暂停", status)) {
                    classroom.setStatus((short) 2);
                }
                classroom.setRemark(remark);
                classroom.setIsDelete((short) 2);
                classroom.setCreateTime(new Date());
                classroom.setVersion(String.valueOf(new Date().getTime()));

                if (classroomList.isEmpty()) {
                    classroom.setCreateTime(new Date());
                    classroom.setIsDelete((short) 2);
                    classroomMapper.insertSelective(classroom);
                } else {
                    classroom.setId(classroomList.get(0).getId());
                    classroom.setCreateTime(new Date());
                    classroomMapper.updateByPrimaryKeySelective(classroom);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        fileSupportService.remove(fileId);
    }
}
