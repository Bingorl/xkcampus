package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.ClassroomType;
import com.biu.wifi.campus.dao.model.ClassroomTypeCriteria;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassroomTypeMapper {
    long countByExample(ClassroomTypeCriteria example);

    int deleteByExample(ClassroomTypeCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(ClassroomType record);

    int insertSelective(ClassroomType record);

    List<ClassroomType> selectByExample(ClassroomTypeCriteria example);

    ClassroomType selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ClassroomType record, @Param("example") ClassroomTypeCriteria example);

    int updateByExample(@Param("record") ClassroomType record, @Param("example") ClassroomTypeCriteria example);

    int updateByPrimaryKeySelective(ClassroomType record);

    int updateByPrimaryKey(ClassroomType record);
}