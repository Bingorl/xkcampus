package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.BiuTravelExpenseDetail;
import com.biu.wifi.campus.dao.model.BiuTravelExpenseDetailExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BiuTravelExpenseDetailMapper {
    long countByExample(BiuTravelExpenseDetailExample example);

    int deleteByExample(BiuTravelExpenseDetailExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BiuTravelExpenseDetail record);

    int insertSelective(BiuTravelExpenseDetail record);

    List<BiuTravelExpenseDetail> selectByExample(BiuTravelExpenseDetailExample example);

    BiuTravelExpenseDetail selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") BiuTravelExpenseDetail record, @Param("example") BiuTravelExpenseDetailExample example);

    int updateByExample(@Param("record") BiuTravelExpenseDetail record, @Param("example") BiuTravelExpenseDetailExample example);

    int updateByPrimaryKeySelective(BiuTravelExpenseDetail record);

    int updateByPrimaryKey(BiuTravelExpenseDetail record);
}