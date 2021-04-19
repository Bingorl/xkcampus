package com.biu.wifi.campus.daoEx;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface BackendUserMapperEx {

    public List<Map<String, Object>> findUserLists(@Param("schoolId") Integer schoolId,
                                                   @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("name") String name, @Param("instituteId") Integer instituteId, @Param("majorId") Integer majorId,
                                                   @Param("classId") Integer classId, @Param("gradeId") Integer gradeId, @Param("id") Integer id,
                                                   @Param("studentNumber") String studentNumber, @Param("status") Short status, @Param("isTeacher") Short isTeacher);

    public List<Map<String, Object>> findUserTeacherLists(@Param("schoolId") Integer schoolId,
                                                          @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("name") String name,
                                                          @Param("id") Integer id, @Param("studentNumber") String studentNumber, @Param("status") Short status);

    public List<Map<String, Object>> findUserListByCondition(@Param("schoolId") Integer schoolId,
                                                             @Param("name") String name, @Param("stuNumber") String stuNumber,
                                                             @Param("instituteId") Integer instituteId, @Param("majorId") Integer majorId,
                                                             @Param("classId") Integer classId, @Param("gradeId") Integer gradeId);

    public Map<String, Object> querySchoolIndexPageData(@Param("schoolId") Integer schoolId);


    public List<Map<String, Object>> findStudentAuthLists(@Param("schoolId") Integer schoolId,
                                                          @Param("name") String name, @Param("instituteId") Integer instituteId, @Param("majorId") Integer majorId,
                                                          @Param("classId") Integer classId, @Param("gradeId") Integer gradeId, @Param("studentNumber") String studentNumber);

    public List<Map<String, Object>> findTeacherAuthLists(@Param("schoolId") Integer schoolId,
                                                          @Param("name") String name, @Param("studentNumber") String studentNumber);

    public List<Map<String, Object>> findPublicPages(@Param("schoolId") Integer schoolId,
                                                     @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("name") String name);

    public List<Map<String, Object>> findPublicPageManagers(@Param("pageId") Integer pageId);

    public List<Map<String, Object>> findPublicPageAuthLists(@Param("schoolId") Integer schoolId);

    public Map<String, Object> findUserById(@Param("id") Integer id);
}
