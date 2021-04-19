package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.ClassroomBookUseType;
import com.biu.wifi.campus.dao.model.ClassroomBookUseTypeCriteria;
import com.biu.wifi.core.base.CoreDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassroomBookUseTypeMapper extends CoreDao {
    long countByExample(ClassroomBookUseTypeCriteria example);

    int deleteByExample(ClassroomBookUseTypeCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(ClassroomBookUseType record);

    int insertSelective(ClassroomBookUseType record);

    List<ClassroomBookUseType> selectByExample(ClassroomBookUseTypeCriteria example);

    ClassroomBookUseType selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ClassroomBookUseType record, @Param("example") ClassroomBookUseTypeCriteria example);

    int updateByExample(@Param("record") ClassroomBookUseType record, @Param("example") ClassroomBookUseTypeCriteria example);

    int updateByPrimaryKeySelective(ClassroomBookUseType record);

    int updateByPrimaryKey(ClassroomBookUseType record);
}