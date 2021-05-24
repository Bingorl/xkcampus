package com.biu.wifi.campus.service;

import com.biu.wifi.campus.constant.NoticeType;
import com.biu.wifi.campus.dao.AssertsUseAuditMapper;
import com.biu.wifi.campus.dao.AssertsUseNoticeMapper;
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
public class AssertsUseNoticeService {
    @Autowired
    private AssertsUseNoticeMapper assertsUseNoticeMapper;
    @Autowired
    private NoticeInfoMapper noticeInfoMapper;

    public void confirmNotice(Integer id, Integer userId) {
        AssertsUseNoticeExample example = new AssertsUseNoticeExample();
        example.createCriteria()
                .andUseIdEqualTo(id)
                .andToUserIdEqualTo(userId)
                .andIsDeleteEqualTo((short) 2);
        List<AssertsUseNotice> list = assertsUseNoticeMapper.selectByExample(example);
        if (!list.isEmpty()) {
            AssertsUseNotice notice = list.get(0);
            notice.setIsConfirm((short) 1);
            notice.setConfirmTime(new Date());
            assertsUseNoticeMapper.updateByPrimaryKeySelective(notice);
        }
    }

    public AssertsUseNotice addUseNotice(Integer useId, String title, String content, String remark, Integer toUserId) {
        AssertsUseNotice useNotice = new AssertsUseNotice();
        useNotice.setUseId(useId);
        useNotice.setTitle(title);
        useNotice.setContent(content);
        useNotice.setRemark(remark);
        useNotice.setToUserId(toUserId);
        useNotice.setCreateTime(new Date());
        assertsUseNoticeMapper.insertSelective(useNotice);
        //插入通知汇总表
        NoticeInfo noticeInfo = new NoticeInfo();
        noticeInfo.setBizId(useNotice.getId());
        noticeInfo.setUserId(toUserId);
        noticeInfo.setType(NoticeType.ASSERTS_USE_NOTICE.getCode().shortValue());
        noticeInfo.setIsDelete((short) 2);
        noticeInfoMapper.insertSelective(noticeInfo);
        return useNotice;
    }
}
