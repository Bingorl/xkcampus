package com.biu.wifi.campus.constant;

/**
 * @author 张彬.
 * @date 2021/4/9 16:33.
 */
public enum SignInType {
    DAILY_ATTENDANCE(1, "考勤签到"),
    MEETING_ATTENDANCE(2, "会议签到"),
    ;

    private Integer code;
    private String description;

    SignInType(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static String getDescription(Integer code) {
        for (SignInType signInType : SignInType.values()) {
            if (signInType.getCode().intValue() == code) {
                return signInType.getDescription();
            }
        }
        return "";
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
