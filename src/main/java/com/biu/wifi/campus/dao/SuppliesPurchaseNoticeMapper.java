package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.SuppliesPurchaseNotice;
import com.biu.wifi.campus.dao.model.SuppliesPurchaseNoticeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SuppliesPurchaseNoticeMapper {
    long countByExample(SuppliesPurchaseNoticeExample example);

    int deleteByExample(SuppliesPurchaseNoticeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SuppliesPurchaseNotice record);

    int insertSelective(SuppliesPurchaseNotice record);

    List<SuppliesPurchaseNotice> selectByExample(SuppliesPurchaseNoticeExample example);

    SuppliesPurchaseNotice selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SuppliesPurchaseNotice record, @Param("example") SuppliesPurchaseNoticeExample example);

    int updateByExample(@Param("record") SuppliesPurchaseNotice record, @Param("example") SuppliesPurchaseNoticeExample example);

    int updateByPrimaryKeySelective(SuppliesPurchaseNotice record);

    int updateByPrimaryKey(SuppliesPurchaseNotice record);
}