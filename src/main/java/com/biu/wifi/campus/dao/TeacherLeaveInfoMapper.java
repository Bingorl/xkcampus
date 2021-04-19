package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.TeacherLeaveInfo;
import com.biu.wifi.campus.dao.model.TeacherLeaveInfoExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * @author 张彬.
 * @date 2021/4/3 19:08.
 */
@Repository
public interface TeacherLeaveInfoMapper {
    long countByExample(TeacherLeaveInfoExample example);

    int deleteByExample(TeacherLeaveInfoExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TeacherLeaveInfo record);

    int insertSelective(TeacherLeaveInfo record);

    List<TeacherLeaveInfo> selectByExample(TeacherLeaveInfoExample example);

    TeacherLeaveInfo selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TeacherLeaveInfo record, @Param("example") TeacherLeaveInfoExample example);

    int updateByExample(@Param("record") TeacherLeaveInfo record, @Param("example") TeacherLeaveInfoExample example);

    int updateByPrimaryKeySelective(TeacherLeaveInfo record);

    int updateByPrimaryKey(TeacherLeaveInfo record);

    List<HashMap> myLeaveInfoList(@Param("userId") Integer userId,
                                  @Param("startDate") String startDate,
                                  @Param("endDate") String endDate,
                                  @Param("statusList") List<Short> statusList);

    List<HashMap> myAuditLeaveInfoList(@Param("userId") Integer userId,
                                       @Param("startDate") String startDate,
                                       @Param("endDate") String endDate,
                                       @Param("statusList") List<Short> statusList);

    List<HashMap> search(@Param("schoolId") Integer schoolId,
                         @Param("leaveType") Integer leaveType,
                         @Param("status") Short status,
                         @Param("keyword") String keyword,
                         @Param("startTime") String startTime,
                         @Param("endTime") String endTime);
}