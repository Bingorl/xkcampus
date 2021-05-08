package com.biu.wifi.campus.service;

import com.biu.wifi.campus.constant.NoticeType;
import com.biu.wifi.campus.dao.NoticeInfoMapper;
import com.biu.wifi.campus.dao.SuppliesPurchaseNoticeMapper;
import com.biu.wifi.campus.dao.model.*;
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
    @Autowired
    private NoticeInfoMapper noticeInfoMapper;


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

    public SuppliesPurchaseNotice addfileNotice(Integer receiveId, String title, String content, String remark, Integer toUserId) {
        SuppliesPurchaseNotice leaveNotice = new SuppliesPurchaseNotice();
        leaveNotice.setPurchaseId(receiveId);
        leaveNotice.setTitle(title);
        leaveNotice.setContent(content);
        leaveNotice.setRemark(remark);
        leaveNotice.setToUserId(toUserId);
        leaveNotice.setCreateTime(new Date());
        suppliesPurchaseNoticeMapper.insertSelective(leaveNotice);
        //插入通知汇总表
        NoticeInfo noticeInfo = new NoticeInfo();
        noticeInfo.setBizId(leaveNotice.getId());
        noticeInfo.setUserId(toUserId);
        noticeInfo.setType(NoticeType.SUPPLIES_PURCHASE_NOTICE.getCode().shortValue());
        noticeInfo.setIsDelete((short) 2);
        noticeInfoMapper.insertSelective(noticeInfo);
        return leaveNotice;
    }
}

