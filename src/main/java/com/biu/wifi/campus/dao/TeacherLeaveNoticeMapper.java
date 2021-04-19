package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.TeacherLeaveNotice;
import com.biu.wifi.campus.dao.model.TeacherLeaveNoticeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author 张彬.
 * @date 2021/4/4 0:44.
 */
@Repository
public interface TeacherLeaveNoticeMapper {
    long countByExample(TeacherLeaveNoticeExample example);

    int deleteByExample(TeacherLeaveNoticeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TeacherLeaveNotice record);

    int insertSelective(TeacherLeaveNotice record);

    List<TeacherLeaveNotice> selectByExample(TeacherLeaveNoticeExample example);

    TeacherLeaveNotice selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TeacherLeaveNotice record, @Param("example") TeacherLeaveNoticeExample example);

    int updateByExample(@Param("record") TeacherLeaveNotice record, @Param("example") TeacherLeaveNoticeExample example);

    int updateByPrimaryKeySelective(TeacherLeaveNotice record);

    int updateByPrimaryKey(TeacherLeaveNotice record);
}