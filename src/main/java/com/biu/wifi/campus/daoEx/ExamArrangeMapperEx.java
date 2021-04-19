package com.biu.wifi.campus.daoEx;

import com.biu.wifi.campus.dao.model.ExamArrange;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author zhangbin.
 * @date 2019/2/15.
 */
@Repository
public interface ExamArrangeMapperEx {

    List<ExamArrange> findListByCourseNo(@Param("schoolId") int schoolId,
                                         @Param("courseNo") String courseNo,
                                         @Param("courseSerialNo") String courseSerialNo,
                                         @Param("examTime") Date examTime,
                                         @Param("startTimePeriodList") List<String> startTimePeriodList);

    List<ExamArrange> findListByClassroomNo(@Param("schoolId") int schoolId,
                                            @Param("classroomNo") String classroomNo,
                                            @Param("examTime") Date examTime,
                                            @Param("startTimePeriodList") List<String> startTimePeriodList);
}
