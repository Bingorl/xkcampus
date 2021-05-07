package com.biu.wifi.campus.service;

import com.biu.wifi.campus.dao.SuppliesPurchaseNoticeMapper;
import com.biu.wifi.campus.dao.model.SuppliesPurchaseNotice;
import com.biu.wifi.campus.dao.model.SuppliesPurchaseNoticeExample;
import com.biu.wifi.campus.dao.model.TeacherLeaveNotice;
import com.biu.wifi.campus.dao.model.TeacherLeaveNoticeExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Author MCX
 * @Version 1.0
 **/
@Service
public class SuppliesPurchaseNoticeService {
    @Autowired
    private SuppliesPurchaseNoticeMapper suppliesPurchaseNoticeMapper;


    public void confirmNotice(Integer id, Integer userId) {
        SuppliesPurchaseNoticeExample example = new SuppliesPurchaseNoticeExample();
        example.createCriteria()
                .andPurchaseIdEqualTo(id)
                .andToUserIdEqualTo(userId)
                .andIsDeleteEqualTo((short) 2);
        List<SuppliesPurchaseNotice> list = suppliesPurchaseNoticeMapper.selectByExample(example);
        if (!list.isEmpty()) {
            SuppliesPurchaseNotice notice = list.get(0);
            notice.setIsConfirm((short) 1);
            notice.setConfirmTime(new Date());
            suppliesPurchaseNoticeMapper.updateByPrimaryKeySelective(notice);
        }
    }
}

