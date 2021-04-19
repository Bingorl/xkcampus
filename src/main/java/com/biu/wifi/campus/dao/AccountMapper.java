package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.Account;
import com.biu.wifi.campus.dao.model.AccountCriteria;
import com.biu.wifi.core.base.CoreDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountMapper extends CoreDao {
    int countByExample(AccountCriteria example);

    int deleteByExample(AccountCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(Account record);

    int insertSelective(Account record);

    List<Account> selectByExample(AccountCriteria example);

    Account selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Account record, @Param("example") AccountCriteria example);

    int updateByExample(@Param("record") Account record, @Param("example") AccountCriteria example);

    int updateByPrimaryKeySelective(Account record);

    int updateByPrimaryKey(Account record);
}