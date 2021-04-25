package com.biu.wifi.campus.service;

import com.biu.wifi.campus.constant.NoticeType;
import com.biu.wifi.campus.dao.NoticeInfoMapper;
import com.biu.wifi.campus.dao.StampToApplyNoticeMapper;
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
public class StampToApplyNoticeService {
    @Autowired
    private NoticeInfoMapper noticeInfoMapper;
    @Autowired
    private StampToApplyNoticeMapper stampToApplyNoticeMapper;

    public void confirmNotice(Integer id, Integer userId) {
        StampToApplyNoticeExample example=new StampToApplyNoticeExample();
        example.createCriteria()
                .andApplyIdEqualTo(id)
                .andToUserIdEqualTo(userId)
                .andIsDeleteEqualTo((short) 2);
        List<StampToApplyNotice> stampToApplyNotices = stampToApplyNoticeMapper.selectByExample(example);
        if (!stampToApplyNotices.isEmpty()) {
            StampToApplyNotice notice = stampToApplyNotices.get(0);
            notice.setIsConfirm((short) 1);
            notice.setConfirmTime(new Date());
            stampToApplyNoticeMapper.updateByPrimaryKeySelective(notice);
        }
    }


    /**
     * 添加通知
     *
     * @param applyId
     * @param title
     * @param content
     * @param remark
     * @param toUserId
     */
    public StampToApplyNotice addStampNotice(Integer applyId, String title, String content, String remark, Integer toUserId) {
        StampToApplyNotice leaveNotice = new StampToApplyNotice();
        leaveNotice.setApplyId(applyId);
        leaveNotice.setTitle(title);
        leaveNotice.setContent(content);
        leaveNotice.setRemark(remark);
        leaveNotice.setToUserId(toUserId);
        leaveNotice.setCreateTime(new Date());
        stampToApplyNoticeMapper.insertSelective(leaveNotice);
        //插入通知汇总表
        NoticeInfo noticeInfo = new NoticeInfo();
        noticeInfo.setBizId(leaveNotice.getId());
        noticeInfo.setUserId(toUserId);
        noticeInfo.setType(NoticeType.STAMP_TO_APPLY_NOTICE.getCode().shortValue());
        noticeInfo.setIsDelete((short) 2);
        noticeInfoMapper.insertSelective(noticeInfo);
        return leaveNotice;
    }

}
