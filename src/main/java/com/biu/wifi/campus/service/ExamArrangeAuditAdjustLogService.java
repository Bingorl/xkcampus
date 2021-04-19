package com.biu.wifi.campus.service;

import com.biu.wifi.campus.dao.ExamArrangeAuditAdjustLogMapper;
import com.biu.wifi.campus.dao.model.ExamArrangeAuditAdjustLog;
import com.biu.wifi.campus.dao.model.ExamArrangeAuditAdjustLogCriteria;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author zhangbin.
 * @date 2019/3/12.
 */
@Service
public class ExamArrangeAuditAdjustLogService {

    //调整教室
    public final int UPDATE_EXAM_CLASSROOM = 1;
    //调整考试时间
    public final int UPDATE_EXAM_TIME = 2;
    //调整监考人员
    public final int UPDATE_INVIGILATE_TEACHER = 3;
    //调整巡考人员
    public final int UPDATE_PATROL_TEACHER = 4;

    @Autowired
    private ExamArrangeAuditAdjustLogMapper examArrangeAuditAdjustLogMapper;

    public void addLog(int examArrangeId, int userId, int type, String content, String oldContent) {
        ExamArrangeAuditAdjustLog log = new ExamArrangeAuditAdjustLog();
        log.setAdjustTime(new Date());
        log.setAdjustUserId(userId);
        log.setContent(content);
        log.setOldContent(oldContent);
        log.setExamArrangeId(examArrangeId);
        log.setType(type);
        if (!StringUtils.equals(content, oldContent))
            examArrangeAuditAdjustLogMapper.insertSelective(log);
    }

    public List<ExamArrangeAuditAdjustLog> findListByUserId(int userId) {
        ExamArrangeAuditAdjustLogCriteria example = new ExamArrangeAuditAdjustLogCriteria();
        example.createCriteria()
                .andAdjustUserIdEqualTo(userId);
        return examArrangeAuditAdjustLogMapper.selectByExample(example);
    }

    public List<ExamArrangeAuditAdjustLog> findListByUserId(int userId, int examArrangeId) {
        ExamArrangeAuditAdjustLogCriteria example = new ExamArrangeAuditAdjustLogCriteria();
        example.createCriteria()
                .andAdjustUserIdEqualTo(userId)
                .andExamArrangeIdEqualTo(examArrangeId);
        return examArrangeAuditAdjustLogMapper.selectByExample(example);
    }
}
