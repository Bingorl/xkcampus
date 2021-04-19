package com.biu.wifi.campus.controller;

import com.biu.wifi.campus.BaseJunit;
import com.biu.wifi.campus.dao.NoticeInfoMapper;
import com.biu.wifi.campus.dao.UserMapper;
import com.biu.wifi.campus.dao.model.NoticeInfo;
import com.biu.wifi.campus.dao.model.User;
import com.biu.wifi.campus.dao.model.UserCriteria;
import com.biu.wifi.campus.service.AuditInfoService;
import com.biu.wifi.campus.service.GroupNoticeService;
import com.biu.wifi.campus.service.LeaveInfoService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author zhangbin.
 * @date 2018/12/15.
 */
public class DataTest extends BaseJunit {

    @Autowired
    private LeaveInfoService leaveInfoService;
    @Autowired
    private NoticeInfoMapper noticeInfoMapper;
    @Autowired
    private AuditInfoService auditInfoService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private GroupNoticeService groupNoticeService;

    @Test
    public void test() {
        // TODO 同步通知和审核数据到总表
        UserCriteria example = new UserCriteria();
        example.createCriteria()
                .andIsDeleteEqualTo((short) 2)
                .andIsAuthEqualTo((short) 1);
        List<User> userList = userMapper.selectByExample(example);

        List<Map<String, Object>> noticeList = new ArrayList<>();

        for (User user : userList) {
            List<Map<String, Object>> leaveNoticeLit = leaveInfoService.findLeaveNoticeMapList(user.getId());
            List<Map<String, Object>> groupNoticeList = groupNoticeService.app_noticeList(null, null,
                    null, null, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), user.getId());
            for (Map<String, Object> map : leaveNoticeLit) {
                map.put("user_id", user.getId());
                map.put("type","3");
                noticeList.add(map);
            }

            for (Map<String, Object> map : groupNoticeList) {
                map.put("user_id", user.getId());
                map.put("type","1");
                noticeList.add(map);
            }

            Collections.sort(noticeList, new Comparator<Map<String, Object>>() {
                @Override
                public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                    return o1.get("create_time").toString().compareTo(o2.get("create_time").toString());
                }
            });
        }

        for (Map<String, Object> map : noticeList) {
            NoticeInfo noticeInfo = new NoticeInfo();
            noticeInfo.setBizId(Integer.valueOf(map.get("id").toString()));
            noticeInfo.setUserId(Integer.valueOf(map.get("user_id").toString()));
            noticeInfo.setType(Short.valueOf(map.get("type").toString()));
            noticeInfoMapper.insertSelective(noticeInfo);
        }
    }
}
