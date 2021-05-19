package com.biu.wifi.campus.service;

import com.biu.wifi.campus.constant.NoticeType;
import com.biu.wifi.campus.dao.NoticeInfoMapper;
import com.biu.wifi.campus.dao.OfficialWebsiteNoticeMapper;
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
public class OfficialWebsiteNoticeService {
    @Autowired
    private OfficialWebsiteNoticeMapper officialWebsiteNoticeMapper;
    @Autowired
    private NoticeInfoMapper noticeInfoMapper;

    public void confirmNotice(Integer id, Integer userId) {
        OfficialWebsiteNoticeExample example = new OfficialWebsiteNoticeExample();
        example.createCriteria()
                .andWebsiteIdEqualTo(id)
                .andToUserIdEqualTo(userId)
                .andIsDeleteEqualTo((short) 2);
        List<OfficialWebsiteNotice> list = officialWebsiteNoticeMapper.selectByExample(example);
        if (!list.isEmpty()) {
            OfficialWebsiteNotice notice = list.get(0);
            notice.setIsConfirm((short) 1);
            notice.setConfirmTime(new Date());
            officialWebsiteNoticeMapper.updateByPrimaryKeySelective(notice);
        }
    }


    public OfficialWebsiteNotice addNotice(Integer websiteId, String title, String content, String remark, Integer toUserId) {
        OfficialWebsiteNotice notice = new OfficialWebsiteNotice();
        notice.setWebsiteId(websiteId);
        notice.setTitle(title);
        notice.setContent(content);
        notice.setRemark(remark);
        notice.setToUserId(toUserId);
        notice.setCreateTime(new Date());
        officialWebsiteNoticeMapper.insertSelective(notice);
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
