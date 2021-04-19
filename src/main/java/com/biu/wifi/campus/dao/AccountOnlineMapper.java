package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.AccountOnline;
import com.biu.wifi.campus.dao.model.AccountOnlineCriteria;
import com.biu.wifi.core.base.CoreDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountOnlineMapper extends CoreDao {
    long countByExample(AccountOnlineCriteria example);

    int deleteByExample(AccountOnlineCriteria example);

    int deleteByPrimaryKey(Long accountOnlineId);

    int insert(AccountOnline record);

    int insertSelective(AccountOnline record);

    List<AccountOnline> selectByExample(AccountOnlineCriteria example);

    AccountOnline selectByPrimaryKey(Long accountOnlineId);

    int updateByExampleSelective(@Param("record") AccountOnline record, @Param("example") AccountOnlineCriteria example);

    int updateByExample(@Param("record") AccountOnline record, @Param("example") AccountOnlineCriteria example);

    int updateByPrimaryKeySelective(AccountOnline record);

    int updateByPrimaryKey(AccountOnline record);
}