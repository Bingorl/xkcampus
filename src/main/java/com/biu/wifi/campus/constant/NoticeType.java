package com.biu.wifi.campus.constant;

/**
 * @author 张彬.
 * @date 2021/4/16 14:26.
 */
public enum NoticeType {

    GROUP_NOTICE(1, "群组通知"),
    CHAT_NOTICE(2, "私信通知"),
    STUDENT_LEAVE_NOTICE(3, "请假通知"),
    CLASSROOM_BOOK_NOTICE(4, "教室预约"),
    EXAM_PLAN_NOTICE(5, "智能排考"),
    TEACHER_LEAVE_NOTICE(6, "教师请假通知"),
    DISCUSSION_TOPIC_APPLY_NOTICE(7, "会议议题申请通知"),
    ;

    private Integer code;
    private String description;

    NoticeType(int code, String description) {
        this.code = code;
        this.description = description;
    }


    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
