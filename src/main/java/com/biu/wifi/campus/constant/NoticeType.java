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
    STAMP_TO_APPLY_NOTICE(8, "用章申请通知"),
    FILE_RECEIVE_NOTICE(9, "文件签发通知"),
    SUPPLIES_PURCHASE_NOTICE(10, "采购申请通知"),
    CONTRACT_APPROVE_NOTICE(11, "合同申请通知"),
    ASSERTS_USE_NOTICE(12, "资产申请通知"),
    OFFICIAL_WEBSITE_NOTICE(13, "官网专栏申请通知"),
    REPAIR_COST_NOTICE(14, "报修费用申请通知"),
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
