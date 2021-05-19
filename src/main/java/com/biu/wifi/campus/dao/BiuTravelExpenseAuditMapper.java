package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.BiuTravelExpenseAudit;
import com.biu.wifi.campus.dao.model.BiuTravelExpenseAuditExample;

import java.util.HashMap;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BiuTravelExpenseAuditMapper {
    long countByExample(BiuTravelExpenseAuditExample example);

    int deleteByExample(BiuTravelExpenseAuditExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BiuTravelExpenseAudit record);

    int insertSelective(BiuTravelExpenseAudit record);

    List<BiuTravelExpenseAudit> selectByExample(BiuTravelExpenseAuditExample example);

    BiuTravelExpenseAudit selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") BiuTravelExpenseAudit record, @Param("example") BiuTravelExpenseAuditExample example);

    int updateByExample(@Param("record") BiuTravelExpenseAudit record, @Param("example") BiuTravelExpenseAuditExample example);

    int updateByPrimaryKeySelective(BiuTravelExpenseAudit record);

    int updateByPrimaryKey(BiuTravelExpenseAudit record);

    HashMap selectMap(@Param("expenseId") Integer id,@Param("userId") String userId);
}