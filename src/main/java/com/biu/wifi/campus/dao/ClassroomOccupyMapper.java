package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.ClassroomOccupy;
import com.biu.wifi.campus.dao.model.ClassroomOccupyCriteria;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassroomOccupyMapper {
    long countByExample(ClassroomOccupyCriteria example);

    int deleteByExample(ClassroomOccupyCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(ClassroomOccupy record);

    int insertSelective(ClassroomOccupy record);

    List<ClassroomOccupy> selectByExample(ClassroomOccupyCriteria example);

    ClassroomOccupy selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ClassroomOccupy record, @Param("example") ClassroomOccupyCriteria example);

    int updateByExample(@Param("record") ClassroomOccupy record, @Param("example") ClassroomOccupyCriteria example);

    int updateByPrimaryKeySelective(ClassroomOccupy record);

    int updateByPrimaryKey(ClassroomOccupy record);
}