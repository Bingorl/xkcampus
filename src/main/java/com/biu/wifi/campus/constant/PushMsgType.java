package com.biu.wifi.campus.constant;

/**
 * @author 张彬.
 * @date 2021/4/8 13:13.
 */
public enum PushMsgType {
    SYSTEM_MSG(1,"官方消息"),
    GROUP_NOTICE(2,"群通知"),
    CALENDAR_NOTICE(3,"通知日历"),
    GROUP_USER_CHANGE_NOTICE(4,"群成员变动"),
    TOPIC_PUBLISH_NOTICE(5,"帖子评论、发布帖子@、点赞"),
    TOPIC_REPLY_NOTICE(6,"楼层评论、回复、楼层@，回复楼层，点赞"),
    SAYING_LIKE_NOTICE(7,"新鲜事评论、发布新鲜事@、点赞"),
    LEAVE_MESSAGE_NOTICE(8,"留言"),
    PUBLIC_PAGE_AUDIT_FAIL_NOTICE(9,"主页审核失败"),
    PUBLIC_PAGE_AUDIT_SUCCESS_NOTICE(10,"主页审核成功"),
    CHAT_NOTICE(11,"私信"),
    STUDENT_LEAVE_NOTICE(12,"学生请假通知"),
    CLASSROOM_BOOK_NOTICE(13,"教室预约通知"),
    EXAM_PLAN_NOTICE(14,"智能排考"),
    TEACHER_LEAVE_NOTICE(15,"教师请假通知"),
    DISCUSSION_TOPIC_APPLY_NOTICE(16,"会议议题申请通知"),
    STAMP_TO_APPLY_NOTICE(17,"用章申请通知"),
    FILE_RECEIVE_NOTICE(18,"文件签发通知"),
    SUPPLIES_PURCHASE_NOTICE(19,"采购申请通知"),
    CONTRACT_APPROVE_NOTICE(20,"合同申请通知"),
    ASSERTS_USE_NOTICE(21,"资产使用申请通知"),
    TRAVEL_EXPENSE_NOTICE(22,"差旅费用申请通知"),
    OFFICIAL_WEBSITE_NOTICE(23,"官网专栏申请通知"),
    ;

    private Integer code;
    private String description;

    PushMsgType(int code, String description) {
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
