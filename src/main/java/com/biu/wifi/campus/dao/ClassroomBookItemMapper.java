package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.ClassroomBookItem;
import com.biu.wifi.campus.dao.model.ClassroomBookItemCriteria;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassroomBookItemMapper {
    long countByExample(ClassroomBookItemCriteria example);

    int deleteByExample(ClassroomBookItemCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(ClassroomBookItem record);

    int insertSelective(ClassroomBookItem record);

    List<ClassroomBookItem> selectByExample(ClassroomBookItemCriteria example);

    ClassroomBookItem selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ClassroomBookItem record, @Param("example") ClassroomBookItemCriteria example);

    int updateByExample(@Param("record") ClassroomBookItem record, @Param("example") ClassroomBookItemCriteria example);

    int updateByPrimaryKeySelective(ClassroomBookItem record);

    int updateByPrimaryKey(ClassroomBookItem record);
}