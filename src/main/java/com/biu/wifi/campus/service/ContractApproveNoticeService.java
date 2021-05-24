package com.biu.wifi.campus.service;

import com.biu.wifi.campus.constant.NoticeType;
import com.biu.wifi.campus.dao.ContractApproveNoticeMapper;
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
public class ContractApproveNoticeService {
    @Autowired
    private ContractApproveNoticeMapper contractApproveNoticeMapper;
    @Autowired
    private NoticeInfoMapper noticeInfoMapper;

    public void confirmNotice(Integer approveId, Integer userId) {
        ContractApproveNoticeExample example = new ContractApproveNoticeExample();
        example.createCriteria()
                .andApproveIdEqualTo(approveId)
                .andToUserIdEqualTo(userId)
                .andIsDeleteEqualTo((short) 2);
        List<ContractApproveNotice> list = contractApproveNoticeMapper.selectByExample(example);
        if (!list.isEmpty()) {
            ContractApproveNotice notice = list.get(0);
            notice.setIsConfirm((short) 1);
            notice.setConfirmTime(new Date());
            contractApproveNoticeMapper.updateByPrimaryKeySelective(notice);
        }
    }

    public ContractApproveNotice addLeaveNotice(Integer receiveId, String title, String content, String remark, Integer toUserId) {
        ContractApproveNotice contractApproveNotice = new ContractApproveNotice();
        contractApproveNotice.setApproveId(receiveId);
        contractApproveNotice.setTitle(title);
        contractApproveNotice.setContent(content);
        contractApproveNotice.setRemark(remark);
        contractApproveNotice.setToUserId(toUserId);
        contractApproveNotice.setCreateTime(new Date());
        contractApproveNoticeMapper.insertSelective(contractApproveNotice);
        //插入通知汇总表
        NoticeInfo noticeInfo = new NoticeInfo();
        noticeInfo.setBizId(contractApproveNotice.getId());
        noticeInfo.setUserId(toUserId);
        noticeInfo.setType(NoticeType.CONTRACT_APPROVE_NOTICE.getCode().shortValue());
        noticeInfo.setIsDelete((short) 2);
        noticeInfoMapper.insertSelective(noticeInfo);
        return contractApproveNotice;
    }
}
