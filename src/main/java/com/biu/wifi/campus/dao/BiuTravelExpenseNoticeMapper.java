package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.BiuTravelExpenseNotice;
import com.biu.wifi.campus.dao.model.BiuTravelExpenseNoticeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BiuTravelExpenseNoticeMapper {
    long countByExample(BiuTravelExpenseNoticeExample example);

    int deleteByExample(BiuTravelExpenseNoticeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BiuTravelExpenseNotice record);

    int insertSelective(BiuTravelExpenseNotice record);

    List<BiuTravelExpenseNotice> selectByExample(BiuTravelExpenseNoticeExample example);

    BiuTravelExpenseNotice selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") BiuTravelExpenseNotice record, @Param("example") BiuTravelExpenseNoticeExample example);

    int updateByExample(@Param("record") BiuTravelExpenseNotice record, @Param("example") BiuTravelExpenseNoticeExample example);

    int updateByPrimaryKeySelective(BiuTravelExpenseNotice record);

    int updateByPrimaryKey(BiuTravelExpenseNotice record);
}