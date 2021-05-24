package com.biu.wifi.campus.service;

import com.biu.wifi.campus.constant.NoticeType;
import com.biu.wifi.campus.dao.NoticeInfoMapper;
import com.biu.wifi.campus.dao.RepairCostNoticeMapper;
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
public class RepairCostNoticeService {
    @Autowired
    private RepairCostNoticeMapper repairCostNoticeMapper;
    @Autowired
    private NoticeInfoMapper noticeInfoMapper;

    public void confirmNotice(Integer id, Integer userId) {
        RepairCostNoticeExample example = new RepairCostNoticeExample();
        example.createCriteria()
                .andRepairIdEqualTo(id)
                .andToUserIdEqualTo(userId)
                .andIsDeleteEqualTo((short) 2);
        List<RepairCostNotice> list = repairCostNoticeMapper.selectByExample(example);
        if (!list.isEmpty()) {
            RepairCostNotice notice = list.get(0);
            notice.setIsConfirm((short) 1);
            notice.setConfirmTime(new Date());
            repairCostNoticeMapper.updateByPrimaryKeySelective(notice);
        }
    }
    public RepairCostNotice addNotice(Integer websiteId, String title, String content, String remark, Integer toUserId) {
        RepairCostNotice notice = new RepairCostNotice();
        notice.setRepairId(websiteId);
        notice.setTitle(title);
        notice.setContent(content);
        notice.setRemark(remark);
        notice.setToUserId(toUserId);
        notice.setCreateTime(new Date());
        repairCostNoticeMapper.insertSelective(notice);
        //插入通知汇总表
        NoticeInfo noticeInfo = new NoticeInfo();
        noticeInfo.setBizId(notice.getId());
        noticeInfo.setUserId(toUserId);
        noticeInfo.setType(NoticeType.OFFICIAL_WEBSITE_NOTICE.getCode().shortValue());
        noticeInfo.setIsDelete((short) 2);
        noticeInfoMapper.insertSelective(noticeInfo);
        return notice;
    }

}
