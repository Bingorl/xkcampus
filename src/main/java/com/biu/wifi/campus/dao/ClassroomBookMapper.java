package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.ClassroomBook;
import com.biu.wifi.campus.dao.model.ClassroomBookCriteria;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassroomBookMapper {
    long countByExample(ClassroomBookCriteria example);

    int deleteByExample(ClassroomBookCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(ClassroomBook record);

    int insertSelective(ClassroomBook record);

    List<ClassroomBook> selectByExampleWithBLOBs(ClassroomBookCriteria example);

    List<ClassroomBook> selectByExample(ClassroomBookCriteria example);

    ClassroomBook selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ClassroomBook record, @Param("example") ClassroomBookCriteria example);

    int updateByExampleWithBLOBs(@Param("record") ClassroomBook record, @Param("example") ClassroomBookCriteria example);

    int updateByExample(@Param("record") ClassroomBook record, @Param("example") ClassroomBookCriteria example);

    int updateByPrimaryKeySelective(ClassroomBook record);

    int updateByPrimaryKeyWithBLOBs(ClassroomBook record);

    int updateByPrimaryKey(ClassroomBook record);
}