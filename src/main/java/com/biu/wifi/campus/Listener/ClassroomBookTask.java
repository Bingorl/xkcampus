package com.biu.wifi.campus.Listener;

import com.biu.wifi.campus.dao.ClassroomOccupyMapper;
import com.biu.wifi.campus.dao.model.Classroom;
import com.biu.wifi.campus.dao.model.ClassroomOccupy;
import com.biu.wifi.campus.dao.model.ClassroomOccupyCriteria;
import com.biu.wifi.campus.result.Result;
import com.biu.wifi.campus.service.ClassroomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author zhangbin.
 * @date 2019/1/8.
 */
// @Component
public class ClassroomBookTask {

    private static Logger logger = LoggerFactory.getLogger(ClassroomBookTask.class);
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    @Autowired
    private ClassroomOccupyMapper classroomOccupyMapper;
    @Autowired
    private ClassroomService classroomService;

    /**
     * 释放占用的教室
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    public void releaseClassroom() {
        Date now = new Date();
        ClassroomOccupyCriteria classroomOccupyEx = new ClassroomOccupyCriteria();
        classroomOccupyEx.createCriteria()
                .andEndTimeEqualTo(sdf.format(now));
        List<ClassroomOccupy> classroomOccupyList = classroomOccupyMapper.selectByExample(classroomOccupyEx);
        if (classroomOccupyList.isEmpty()) {
            logger.info("暂无需要释放的教室");
            return;
        } else {
            logger.info("开始执行释放教室的调度任务");
        }
        for (ClassroomOccupy classroomOccupy : classroomOccupyList) {
            Result result = classroomService.release(classroomOccupy.getClassroomId());
            if (result.getKey() != Result.SUCCESS) {
                Classroom classroom = classroomService.findById(classroomOccupy.getClassroomId());
                logger.error("释放教室【{}】失败", classroom.getNo());
            }
        }
        logger.info("释放教室的调度任务执行完成");
    }
}
