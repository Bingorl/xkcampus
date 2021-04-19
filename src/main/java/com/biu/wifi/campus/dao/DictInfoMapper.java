package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.DictInfo;
import com.biu.wifi.campus.dao.model.DictInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author 张彬.
 * @date 2021/4/6 9:45.
 */
@Repository
public interface DictInfoMapper {
    long countByExample(DictInfoExample example);

    int deleteByExample(DictInfoExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(DictInfo record);

    int insertSelective(DictInfo record);

    List<DictInfo> selectByExample(DictInfoExample example);

    DictInfo selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") DictInfo record, @Param("example") DictInfoExample example);

    int updateByExample(@Param("record") DictInfo record, @Param("example") DictInfoExample example);

    int updateByPrimaryKeySelective(DictInfo record);

    int updateByPrimaryKey(DictInfo record);
}