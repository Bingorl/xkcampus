package com.biu.wifi.campus.service;

import com.biu.wifi.campus.Tool.PushTool;
import com.biu.wifi.campus.constant.AuditBusinessType;
import com.biu.wifi.campus.constant.PushMsgType;
import com.biu.wifi.campus.dao.FileReceiveAuditInfoMapper;
import com.biu.wifi.campus.dao.PushMapper;
import com.biu.wifi.campus.dao.UserMapper;
import com.biu.wifi.campus.dao.model.*;
import com.biu.wifi.campus.exception.BizException;
import com.biu.wifi.campus.result.Result;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @Author MCX
 * @Version 1.0
 **/
@Service
public class FileReceiveAuditInfoService extends AbstractAuditUserService{
    @Autowired
    private FileReceiveAuditInfoMapper fileReceiveAuditInfoMapper;
    @Autowired
    private FileReceiveAuditService fileReceiveAuditService;
    @Autowired
    private AuditInfoService auditInfoService;
    @Autowired
    private UserMapper userMapper;
   @Autowired
    private PushMapper pushMapper;
   @Autowired
    private FileReceiveNoticeService fileReceiveNoticeService;

    public void add(FileReceiveAuditInfo req) {
        setCurrentAuditUserId(req);
        req.setCreateTime(new Date());
        boolean fileInfoAdd= fileReceiveAuditInfoMapper.insertSelective(req)>0;
        FileReceiveAudit fileReceiveAudit=new FileReceiveAudit();
        fileReceiveAudit.setReceiveId(req.getId());
        fileReceiveAudit.setUserId(req.getCurrentAuditUserId());
        boolean fileAuditAdd=fileReceiveAuditService.add(fileReceiveAudit)>0;
        AuditInfo auditInfo = new AuditInfo();
        auditInfo.setType(AuditBusinessType.FILE_RECEIVE.getCode().shortValue());
        auditInfo.setBizId(fileReceiveAudit.getId());
        auditInfo.setUserId(req.getCurrentAuditUserId());
        boolean addAuditInfo = auditInfoService.add(auditInfo) > 0;
        // ?????????????????????????????????????????????
        User user = userMapper.selectByPrimaryKey(req.getCurrentAuditUserId());
        String deviceToken = user.getDevToken();
        Short deviceType = user.getDevType();
        addPush(req.getId(), fileReceiveAudit.getId(), 1, "?????????????????????????????????", req.getCurrentAuditUserId(), deviceType, deviceToken);

        if (!(fileInfoAdd && fileAuditAdd && addAuditInfo)) {
            throw new BizException(Result.CUSTOM_MESSAGE, "????????????");
        }
    }

    private void setCurrentAuditUserId(FileReceiveAuditInfo req) {
        List<Integer> auditUserIds = new ArrayList<>();
        for (String userId : req.getAuditUser().split(",")) {
            auditUserIds.add(Integer.valueOf(userId));
        }

        HashMap hashMap = setAuditUser(req.getCurrentAuditUserId(), auditUserIds);
        req.setAuditUser(MapUtils.getString(hashMap, "auditUser"));
        req.setCurrentAuditUserId(MapUtils.getInteger(hashMap, "currentAuditUserId"));
    }
    public void addPush(Integer applyId, Integer fileNoticeId, Integer needToAudit,
                        String title, Integer receiverId, Short deviceType, String deviceToken) {
        // ??????
        Boolean flag = false;
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("id", "");
        hm.put("title", title);
        hm.put("type", PushMsgType.FILE_RECEIVE_NOTICE.getCode());//??????????????????
        hm.put("applyId", applyId == null ? "" : applyId);
        hm.put("fileNoticeId", fileNoticeId == null ? "" : fileNoticeId);
        hm.put("needToAudit", needToAudit);

        try {
            flag = PushTool.pushToUser(receiverId, "", title, hm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // ????????????
        Push puEntity = new Push();
        puEntity.setToken(deviceToken);
        puEntity.setContent("");
        puEntity.setUserId(receiverId);
        puEntity.setMessageType(PushMsgType.FILE_RECEIVE_NOTICE.getCode().shortValue());
        puEntity.setDeviceType(deviceType);
        puEntity.setTitle(title);
        if (flag) {
            puEntity.setType((short) 10);
        } else {
            puEntity.setType((short) 50);
        }
        pushMapper.insertSelective(puEntity);
    }

    public FileReceiveAuditInfo selectByPrimaryKey(Integer receiveId) {

        return  fileReceiveAuditInfoMapper.selectByPrimaryKey(receiveId);
    }

    public void cancel(FileReceiveAuditInfo fileReceiveAuditInfo) {
        FileReceiveAuditInfo update=new FileReceiveAuditInfo();
        update.setStatus((short) 4);
        update.setUpdateTime(new Date());
        FileReceiveAuditInfoExample example = new FileReceiveAuditInfoExample();
        example.createCriteria()
                .andIdEqualTo(fileReceiveAuditInfo.getId());
        boolean result = fileReceiveAuditInfoMapper.updateByExampleSelective(update, example) > 0;
        if (!result) {
            throw new BizException(Result.CUSTOM_MESSAGE, "????????????");
        }
    }

    public List<HashMap> myInfoList(Integer userId, String referenceNum, String title, Short status) {
        return fileReceiveAuditInfoMapper.myStampApplyInfoList(userId, referenceNum, title, getStatusList(status, true));

    }

    public List<HashMap> myAuditfileInfoList(Integer userId, String referenceNum, String title, Short status) {
        return fileReceiveAuditInfoMapper.myAuditLeaveInfoList(userId, referenceNum, title, getStatusList(status, false));

    }

    public void audit(FileReceiveAuditInfo info, Short status, String remark) {
        int currentAuditUserId = info.getCurrentAuditUserId();
        boolean currentAuditUserIsTheLast = false;
        // ??????????????????????????????????????????,????????????????????????id
        setCurrentAuditUserId(info);
        int nextAuditUserId = currentAuditUserId;
        if (info.getCurrentAuditUserId() == null) {
            // ??????????????????????????????
            currentAuditUserIsTheLast = true;
        } else {
            nextAuditUserId = info.getCurrentAuditUserId();
        }

        FileReceiveAudit fileReceiveAudit = fileReceiveAuditService.selectByLeaveId(info.getId(), currentAuditUserId);
        Assert.notNull(fileReceiveAudit, "??????????????????????????????");
        fileReceiveAudit.setIsPass(status.intValue() == 2 ? (short) 1 : (short) 2);
        fileReceiveAudit.setRemark(remark);
        fileReceiveAudit.setAuditTime(new Date());
        boolean updateLeaveAudit = fileReceiveAuditService.update(fileReceiveAudit) > 0;
        boolean updateAuditInfo = auditInfoService.update(fileReceiveAudit.getId(), currentAuditUserId,
                AuditBusinessType.FILE_RECEIVE.getCode().shortValue(), fileReceiveAudit.getIsPass()) > 0;

        if (!(updateLeaveAudit && updateAuditInfo)) {
            throw new BizException(Result.CUSTOM_MESSAGE, "????????????");
        }

        // ????????????
        FileReceiveNotice fileReceiveNotice;
        Integer receiverId, receiveNoticeId, needToAudit = 0;
        String title;
        User user;
        if (status.intValue() == 3) {
            // ??????????????????
            info.setStatus(status);
            info.setCurrentAuditUserId(currentAuditUserId);
            info.setUpdateTime(new Date());
            fileReceiveAuditInfoMapper.updateByPrimaryKeySelective(info);

            // ????????????,???????????????????????????
            fileReceiveNotice = fileReceiveNoticeService.addfileNotice(info.getId(), "???????????????????????????", info.getContent(), remark, info.getApplyUserId());
            Assert.notNull(fileReceiveNotice.getId(), "????????????");
            // ??????????????????????????????????????????
            receiverId = info.getApplyUserId();
            title = "?????????????????????????????????";
            user = userMapper.selectByPrimaryKey(receiverId);
            receiveNoticeId = fileReceiveNotice.getId();
        } else {
            if (currentAuditUserIsTheLast) {
                // ??????????????????
                info.setStatus(status);
                info.setCurrentAuditUserId(currentAuditUserId);
                info.setUpdateTime(new Date());
                fileReceiveAuditInfoMapper.updateByPrimaryKeySelective(info);
                // ???????????????????????????
                fileReceiveNotice = fileReceiveNoticeService.addfileNotice(info.getId(), "???????????????????????????", info.getContent(), remark, info.getApplyUserId());
                Assert.notNull(fileReceiveNotice.getId(), "????????????");
                // ??????????????????????????????????????????
                receiverId = info.getApplyUserId();
                title = "?????????????????????????????????";
                user = userMapper.selectByPrimaryKey(receiverId);
                receiveNoticeId = fileReceiveNotice.getId();
            } else {
                // ?????????????????????
                info.setCurrentAuditUserId(nextAuditUserId);
                info.setUpdateTime(new Date());
                fileReceiveAuditInfoMapper.updateByPrimaryKeySelective(info);

                // ???????????????????????????????????????????????????
                FileReceiveAudit nextLeaveAudit = new FileReceiveAudit();
                nextLeaveAudit.setReceiveId(info.getId());
                nextLeaveAudit.setUserId(nextAuditUserId);
                nextLeaveAudit.setCreateTime(new Date());
                boolean addLeaveAudit = fileReceiveAuditService.add(nextLeaveAudit) > 0;
                // ????????????????????????
                AuditInfo auditInfo = new AuditInfo();
                auditInfo.setType(AuditBusinessType.FILE_RECEIVE.getCode().shortValue());
                auditInfo.setBizId(nextLeaveAudit.getId());
                auditInfo.setUserId(nextAuditUserId);
                boolean addAuditInfo = auditInfoService.add(auditInfo) > 0;
                if (!(addLeaveAudit && addAuditInfo)) {
                    throw new BizException(Result.CUSTOM_MESSAGE, "????????????");
                }

                // ?????????????????????????????????????????????
                receiverId = nextAuditUserId;
                title = "?????????????????????????????????";
                user = userMapper.selectByPrimaryKey(receiverId);
                receiveNoticeId = null;
                needToAudit = 1;
            }

        }
        addPush(info.getId(), receiveNoticeId, needToAudit, title, receiverId, user.getDevType(), user.getDevToken());

    }
}
