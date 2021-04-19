package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.Classroom;
import com.biu.wifi.campus.dao.model.ClassroomCriteria;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassroomMapper {
    long countByExample(ClassroomCriteria example);

    int deleteByExample(ClassroomCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(Classroom record);

    int insertSelective(Classroom record);

    List<Classroom> selectByExampleWithBLOBs(ClassroomCriteria example);

    List<Classroom> selectByExample(ClassroomCriteria example);

    Classroom selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Classroom record, @Param("example") ClassroomCriteria example);

    int updateByExampleWithBLOBs(@Param("record") Classroom record, @Param("example") ClassroomCriteria example);

    int updateByExample(@Param("record") Classroom record, @Param("example") ClassroomCriteria example);

    int updateByPrimaryKeySelective(Classroom record);

    int updateByPrimaryKeyWithBLOBs(Classroom record);

    int updateByPrimaryKey(Classroom record);
}