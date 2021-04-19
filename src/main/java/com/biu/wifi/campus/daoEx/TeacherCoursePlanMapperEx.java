package com.biu.wifi.campus.daoEx;

import com.biu.wifi.campus.dao.model.TeacherCoursePlan;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zhangbin.
 * @date 2019/2/16.
 */
@Repository
public interface TeacherCoursePlanMapperEx {

    List<TeacherCoursePlan> findList(@Param("schoolId") int schoolId,
                                     @Param("courseWeek") int courseWeek,
                                     @Param("courseWeekDay") String courseWeekDay);
}
