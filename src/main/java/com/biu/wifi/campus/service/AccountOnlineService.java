package com.biu.wifi.campus.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biu.wifi.core.support.orm.mybatis3.util.IbatisServiceUtils;
import com.biu.wifi.campus.Tool.StringUtilEx;
import com.biu.wifi.campus.dao.AccountOnlineMapper;
import com.biu.wifi.campus.dao.model.AccountOnline;
import com.biu.wifi.campus.daoEx.AccountOnlineMapperEx;

@Service
public class AccountOnlineService {

    @Autowired
    private AccountOnlineMapper accountOnlineMapper;

    @Autowired
    private AccountOnlineMapperEx accountOnlineMapperEx;

    public List<AccountOnline> findList(AccountOnline entity) {
        return IbatisServiceUtils.find(entity, accountOnlineMapper, "account_online_id DESC");
    }

    public int insert(AccountOnline entity) {
        return accountOnlineMapper.insertSelective(entity);
    }

    public void delete(AccountOnline entity) {
        try {
            IbatisServiceUtils.delete(entity, accountOnlineMapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public AccountOnline getAccountOnline(AccountOnline entity) {
        return IbatisServiceUtils.get(entity, accountOnlineMapper);
    }

    public AccountOnline getToken(String token) {
        AccountOnline accountOnline = new AccountOnline();
        accountOnline.setOnlineKey(token);
        accountOnline = IbatisServiceUtils.get(accountOnline, accountOnlineMapper);
        return accountOnline;
    }

    public void updateToken(AccountOnline accountOnline) {
        //重新更新token有效期
        Date now = new Date();
        long saveTime = StringUtilEx.save_time;
        accountOnline.setEdatetime(now.getTime() + saveTime * 1000);
        accountOnline.setLengthen((int) saveTime);
        accountOnline.setSdatetime(new BigDecimal(now.getTime()).longValue());
        IbatisServiceUtils.updateByPk(accountOnline, accountOnlineMapper);
    }

    //删除过期token
    public int deleteToken() {
        return accountOnlineMapperEx.deleteToken();
    }

}
