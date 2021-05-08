package com.biu.wifi.campus.service;

import com.biu.wifi.campus.constant.NoticeType;
import com.biu.wifi.campus.dao.FileReceiveNoticeMapper;
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
public class FileReceiveNoticeService {
    @Autowired
    private FileReceiveNoticeMapper fileReceiveNoticeMapper;
    @Autowired
    private NoticeInfoMapper noticeInfoMapper;

    public  FileReceiveNotice addfileNotice(Integer receiveId, String title, String content, String remark, Integer toUserId) {
        FileReceiveNotice leaveNotice = new FileReceiveNotice();
        leaveNotice.setReceiveId(receiveId);
        leaveNotice.setTitle(title);
        leaveNotice.setContent(content);
        leaveNotice.setRemark(remark);
        leaveNotice.setToUserId(toUserId);
        leaveNotice.setCreateTime(new Date());
        fileReceiveNoticeMapper.insertSelective(leaveNotice);
        //插入通知汇总表
        NoticeInfo noticeInfo = new NoticeInfo();
        noticeInfo.setBizId(leaveNotice.getId());
        noticeInfo.setUserId(toUserId);
        noticeInfo.setType(NoticeType.FILE_RECEIVE_NOTICE.getCode().shortValue());
        noticeInfo.setIsDelete((short) 2);
        noticeInfoMapper.insertSelective(noticeInfo);
        return leaveNotice;
    }

    public void confirmNotice(Integer id, Integer userId) {
        FileReceiveNoticeExample example = new FileReceiveNoticeExample();
            example.createCriteria()
                    .andReceiveIdEqualTo(id)
                    .andToUserIdEqualTo(userId)
                    .andIsDeleteEqualTo((short) 2);
            List<FileReceiveNotice> list = fileReceiveNoticeMapper.selectByExample(example);
            if (!list.isEmpty()) {
                FileReceiveNotice notice = list.get(0);
                notice.setIsConfirm((short) 1);
                notice.setConfirmTime(new Date());
                fileReceiveNoticeMapper.updateByPrimaryKeySelective(notice);
        }
    }

}
