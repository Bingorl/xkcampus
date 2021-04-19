package com.biu.wifi.campus.daoEx;

import com.biu.wifi.campus.dao.model.ClassroomBookItem;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zhangbin.
 * @date 2018/12/7.
 */
@Repository
public interface ClassroomBookItemMapperEx {

    /**
     * 查询指定教室在指定的时间段待审核或者已审批通过的预约记录数
     *
     * @param classroomBookId     预约ID
     * @param classroomBuildingId 教学楼ID
     * @param classroomNo         教室号
     * @param startTime           开始使用时间
     * @return
     */
    long findBookedCount(@Param("classroomBookId") Integer classroomBookId,
                         @Param("classroomBuildingId") Integer classroomBuildingId,
                         @Param("classroomNo") String classroomNo,
                         @Param("startTime") String startTime);

    /**
     * 查询指定教学楼，在指定的时间段内的待审核或者已审批通过的预约记录
     *
     * @param classroomBuildingId 教学楼ID
     * @param startTimeList       使用开始时间集合
     * @return
     */
    List<ClassroomBookItem> findClassroomBookList(@Param("classroomBuildingId") int classroomBuildingId,
                                                  @Param("startTimeList") List<String> startTimeList);

}
