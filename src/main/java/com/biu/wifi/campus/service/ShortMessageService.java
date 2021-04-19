package com.biu.wifi.campus.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biu.wifi.campus.dao.ShortMessageMapper;
import com.biu.wifi.campus.dao.model.ShortMessage;
import com.biu.wifi.core.support.orm.mybatis3.util.IbatisServiceUtils;

@Service
public class ShortMessageService {

    @Autowired
    private ShortMessageMapper shortMessageMapper;

    //时间具体到天查,每个号码每天发送的短信
    public List<ShortMessage> findPhoneMessagesByPhone(String mobile, String createTime) {
        ShortMessage entity = new ShortMessage();
        entity.setPhone(mobile);
        entity.setCreateTime(createTime);
//		entity.setStatus((short)2);
        return IbatisServiceUtils.find(entity, shortMessageMapper);
    }

    //时间具体到天查,每个IP每天发送的短信
    public List<ShortMessage> findPhoneMessagesByIp(String ip, String createTime) {
        ShortMessage entity = new ShortMessage();
        entity.setIp(ip);
        entity.setCreateTime(createTime);
//		entity.setStatus((short)2);
        return IbatisServiceUtils.find(entity, shortMessageMapper);
    }

    public int addPhoneMessages(ShortMessage entity) {
        try {
            return IbatisServiceUtils.insert(entity, shortMessageMapper);
        } catch (Exception e) {

            e.printStackTrace();
        }
        return 0;
    }

    public List<ShortMessage> findList(ShortMessage entity) {
        return IbatisServiceUtils.find(entity, shortMessageMapper);
    }

    public void updateShortMessage(ShortMessage entity) {
        IbatisServiceUtils.updateByPk(entity, shortMessageMapper);
    }

}
