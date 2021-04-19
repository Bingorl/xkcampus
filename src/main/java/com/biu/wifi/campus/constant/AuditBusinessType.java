package com.biu.wifi.campus.constant;

/**
 * @author 张彬.
 * @date 2021/4/4 21:36.
 */
public enum AuditBusinessType {
    LEAVE(1, "学生请假"),
    CLASSROOM_BOOK(2, "教室预约"),
    EXAM_PLAN(3, "智能排考"),
    TEACHER_LEAVE(4, "教师请假"),
    DISCUSSION_TOPIC_APPLY(5, "会议议题申请"),
    ;

    private Integer code;
    private String description;

    AuditBusinessType(int code, String description) {
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
