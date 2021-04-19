package com.biu.wifi.campus.service;

import com.biu.wifi.campus.Tool.PushTool;
import com.biu.wifi.campus.dao.PushMapper;
import com.biu.wifi.campus.dao.model.Push;
import com.biu.wifi.campus.dao.model.PushCriteria;
import com.biu.wifi.campus.exception.BizException;
import com.biu.wifi.campus.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class PushSerivce {

    @Autowired
    private PushMapper pushMapper;

    public int addPush(Push entity) {
        try {
            pushMapper.insertSelective(entity);
            return entity.getId();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public List<Push> findList(PushCriteria example) {
        return pushMapper.selectByExample(example);
    }

    /**
     * 推送通知
     *
     * @param title       通知标题
     * @param bizId       业务id
     * @param toUserId    通知接收人ID
     * @param devToken    设备token
     * @param devType     设备类型
     * @param messageType 消息类型
     * @return
     */
    @Async
    public void pushNotice(String title, Integer bizId, Integer toUserId, String devToken, Short devType, Short messageType) {
        // 推送
        boolean flag = false;
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("id", bizId);
        hm.put("title", title);
        hm.put("type", messageType);

        try {
            flag = PushTool.pushToUser(toUserId, "", title, hm);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 入推送表
        Push puEntity = new Push();
        puEntity.setToken(devToken);
        puEntity.setContent("");
        puEntity.setUserId(toUserId);
        puEntity.setMessageType(messageType);
        puEntity.setDeviceType(devType);
        puEntity.setTitle(title);
        if (flag) {
            puEntity.setType((short) 10);
        } else {
            puEntity.setType((short) 50);
        }

        if (pushMapper.insertSelective(puEntity) < 0)
            throw new BizException(Result.SQL_EXECUTE_FAILURE, "数据库操作失败");
    }
}
