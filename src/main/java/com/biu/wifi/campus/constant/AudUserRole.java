package com.biu.wifi.campus.constant;

/**
 * @author zhangbin.
 * @date 2018/12/18.
 */
public enum AudUserRole {

    JDY("0001", "教导员"),
    JFRY("0002", "教辅人员"),
    TEACHER("0003", "教师"),
    SGY("0004", "宿管人员"),
    ABRY("0005", "安保人员"),
    WAITER("0006", "餐厅人员"),
    JWRY("0007", "教务处人员"),
    JXMS("0008", "教学秘书");

    public String code;
    public String name;

    AudUserRole(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}

