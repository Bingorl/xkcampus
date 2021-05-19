package com.biu.wifi.campus.service;

import com.biu.wifi.campus.constant.NoticeType;
import com.biu.wifi.campus.dao.BiuTravelExpenseNoticeMapper;
import com.biu.wifi.campus.dao.NoticeInfoMapper;
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
public class BiuTravelExpenseNoticeService {
    @Autowired
    private BiuTravelExpenseNoticeMapper biuTravelExpenseNoticeMapper;
    @Autowired
    private NoticeInfoMapper noticeInfoMapper;

    public void confirmNotice(Integer id, Integer userId) {
        BiuTravelExpenseNoticeExample example = new BiuTravelExpenseNoticeExample();
        example.createCriteria()
                .andExpenseIdEqualTo(id)
                .andToUserIdEqualTo(userId)
                .andIsDeleteEqualTo((short) 2);
        List<BiuTravelExpenseNotice> list = biuTravelExpenseNoticeMapper.selectByExample(example);
        if (!list.isEmpty()) {
            BiuTravelExpenseNotice notice = list.get(0);
            notice.setIsConfirm((short) 1);
            notice.setConfirmTime(new Date());
            biuTravelExpenseNoticeMapper.updateByPrimaryKeySelective(notice);
        }
    }

    public BiuTravelExpenseNotice addNotice(Integer id, String title, String remark, Integer toUserId) {
        BiuTravelExpenseNotice expenseNotice = new BiuTravelExpenseNotice();
        expenseNotice.setExpenseId(id);
        expenseNotice.setTitle(title);
//        expenseNotice.setContent(content);
        expenseNotice.setRemark(remark);
        expenseNotice.setToUserId(toUserId);
        expenseNotice.setCreateTime(new Date());
        biuTravelExpenseNoticeMapper.insertSelective(expenseNotice);
        //插入通知汇总表
        NoticeInfo noticeInfo = new NoticeInfo();
        noticeInfo.setBizId(expenseNotice.getId());
        noticeInfo.setUserId(toUserId);
        noticeInfo.setType(NoticeType.TEACHER_LEAVE_NOTICE.getCode().shortValue());
        noticeInfo.setIsDelete((short) 2);
        noticeInfoMapper.insertSelective(noticeInfo);
        return expenseNotice;
    }
}
