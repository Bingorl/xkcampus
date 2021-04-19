package com.biu.wifi.campus.service;

import com.biu.wifi.campus.constant.NoticeType;
import com.biu.wifi.campus.dao.ExamArrangeNoticeMapper;
import com.biu.wifi.campus.dao.NoticeInfoMapper;
import com.biu.wifi.campus.dao.model.ExamArrangeNotice;
import com.biu.wifi.campus.dao.model.ExamArrangeNoticeCriteria;
import com.biu.wifi.campus.dao.model.NoticeInfo;
import com.biu.wifi.campus.dao.model.User;
import com.biu.wifi.campus.exception.BizException;
import com.biu.wifi.campus.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangbin.
 * @date 2019/2/13.
 */
@Service
public class ExamArrangeNoticeService {

    @Autowired
    private NoticeInfoMapper noticeInfoMapper;
    @Autowired
    private ExamArrangeNoticeMapper examArrangeNoticeMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private PushSerivce pushService;

    /**
     * 创建排考通知记录
     *
     * @param examArrangeId
     * @param toUser
     * @param title
     * @param content
     * @param syncPush      是否为同步推送
     */
    public void addExamArrangeNotice(int examArrangeId, int toUser, String title, String content, boolean syncPush) {
        // 插入排考通知
        ExamArrangeNotice examArrangeNotice = new ExamArrangeNotice();
        examArrangeNotice.setExamArrangeId(examArrangeId);
        examArrangeNotice.setToUser(toUser);
        examArrangeNotice.setTitle(title);
        examArrangeNotice.setContent(content);
        examArrangeNotice.setCreateTime(new Date());
        if (examArrangeNoticeMapper.insertSelective(examArrangeNotice) < 0)
            throw new BizException(Result.SQL_EXECUTE_FAILURE, "数据库操作失败");

        //插入通知汇总表
        NoticeInfo noticeInfo = new NoticeInfo();
        noticeInfo.setBizId(examArrangeNotice.getId());
        noticeInfo.setUserId(toUser);
        noticeInfo.setType(NoticeType.EXAM_PLAN_NOTICE.getCode().shortValue());
        noticeInfo.setIsDelete((short) 2);
        if (noticeInfoMapper.insertSelective(noticeInfo) < 0)
            throw new BizException(Result.SQL_EXECUTE_FAILURE, "数据库操作失败");

        if (syncPush) {
            // 同步推送
            pushExamArrangeNotice(examArrangeNotice, toUser, title);
        }
    }

    /**
     * 更新排考通知的推送状态并推送给用户
     *
     * @param examArrangeNotice
     * @param toUser
     * @param title
     */
    public void pushExamArrangeNotice(ExamArrangeNotice examArrangeNotice, int toUser, String title) {
        examArrangeNotice.setIsPushed(1);
        examArrangeNotice.setPushedTime(new Date());
        examArrangeNoticeMapper.updateByPrimaryKeySelective(examArrangeNotice);
        User user = userService.getById(toUser);
        if (user != null)
            pushService.pushNotice(title, examArrangeNotice.getExamArrangeId(), toUser, user.getDevToken(), user.getDevType(), (short) 14);
    }

    /**
     * 根据id获取排考通知的map对象
     *
     * @param examArrangeNoticeId
     * @return
     */
    public Map<String, Object> findExamArrangeNoticeMapById(Integer examArrangeNoticeId) {
        ExamArrangeNotice examArrangeNotice = examArrangeNoticeMapper.selectByPrimaryKey(examArrangeNoticeId);
        if (examArrangeNotice == null)
            throw new BizException(Result.CUSTOM_MESSAGE, "id为" + examArrangeNoticeId + "的排考通知不存在");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Map<String, Object> map = new HashMap<>();
        map.put("id", examArrangeNotice.getId());
        map.put("noticeType", NoticeType.EXAM_PLAN_NOTICE.getCode().shortValue());// 智能排考审批通知
        map.put("create_time", sdf.format(examArrangeNotice.getCreateTime()));
        map.put("name", "");
        map.put("title", examArrangeNotice.getTitle());
        map.put("content", examArrangeNotice.getContent());
        map.put("examArrangeId", examArrangeNotice.getExamArrangeId());
        // 是否已读、是否已收到
        map.put("is_received", examArrangeNotice.getIsReceived() == 1 ? 1 : 2);

        map.put("question_number", 0);
        map.put("received_all", 0);
        map.put("received_number", 0);
        map.put("is_calendar", "2");
        map.put("remind_date", "");
        map.put("remind_time", "");
        map.put("event_id", "");
        return map;
    }

    public List<ExamArrangeNotice> findUnPushedExamArrangeNoticeList() {
        ExamArrangeNoticeCriteria example = new ExamArrangeNoticeCriteria();
        example.setOrderByClause("create_time desc");
        example.createCriteria()
                .andIsDeleteEqualTo(2)
                .andIsPushedEqualTo(2);
        return examArrangeNoticeMapper.selectByExample(example);
    }

    /**
     * 删除我的排考通知
     *
     * @param userId
     * @param examArrangeNoticeId
     */
    public void deleteMyExamArrangeNotice(Integer userId, Integer examArrangeNoticeId) {
        ExamArrangeNotice examArrangeNotice = examArrangeNoticeMapper.selectByPrimaryKey(examArrangeNoticeId);
        if (examArrangeNotice == null) {
            throw new BizException(Result.CUSTOM_MESSAGE, "该记录已经被删除");
        }
        if (examArrangeNotice.getToUser() != userId) {
            throw new BizException(Result.CUSTOM_MESSAGE, "没有删除的权限");
        }
        if (examArrangeNotice.getIsReceived() != 1) {
            examArrangeNotice.setIsReceived(1);
            examArrangeNotice.setReceivedTime(new Date());
        }

        examArrangeNotice.setIsDelete(1);
        examArrangeNotice.setDeleteTime(new Date());
        examArrangeNoticeMapper.updateByPrimaryKeySelective(examArrangeNotice);
    }
}
