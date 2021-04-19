package com.biu.wifi.campus.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biu.wifi.campus.dao.AccountMapper;
import com.biu.wifi.campus.dao.model.Account;
import com.biu.wifi.core.support.orm.mybatis3.util.IbatisServiceUtils;

@Service
public class BackendAccountService {

    @Autowired
    private AccountMapper accountMapper;

    public List<Account> getAccountListForLogin(Account entity) {
        return IbatisServiceUtils.find(entity, accountMapper);
    }

    public void addAccount(Account entity) {
        accountMapper.insertSelective(entity);
    }

    public Account getById(Integer id) {
        return accountMapper.selectByPrimaryKey(id);
    }
}
