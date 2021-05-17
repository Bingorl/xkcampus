package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.BiuTravelExpenseAuditUser;
import com.biu.wifi.campus.dao.model.BiuTravelExpenseAuditUserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BiuTravelExpenseAuditUserMapper {
    long countByExample(BiuTravelExpenseAuditUserExample example);

    int deleteByExample(BiuTravelExpenseAuditUserExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BiuTravelExpenseAuditUser record);

    int insertSelective(BiuTravelExpenseAuditUser record);

    List<BiuTravelExpenseAuditUser> selectByExample(BiuTravelExpenseAuditUserExample example);

    BiuTravelExpenseAuditUser selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") BiuTravelExpenseAuditUser record, @Param("example") BiuTravelExpenseAuditUserExample example);

    int updateByExample(@Param("record") BiuTravelExpenseAuditUser record, @Param("example") BiuTravelExpenseAuditUserExample example);

    int updateByPrimaryKeySelective(BiuTravelExpenseAuditUser record);

    int updateByPrimaryKey(BiuTravelExpenseAuditUser record);
}