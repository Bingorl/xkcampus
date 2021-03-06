package com.biu.wifi.campus.constant;

/**
 * 基础字典类型
 *
 * @author 张彬.
 * @date 2021/4/6 9:34.
 * @return
 */
public enum BaseDictType {

    TEACHER_LEAVE_TYPE("教师请假类型"),
    STAMP_TO_APPLY_TYPE("用章申请类型"),
    STAMP_TYPE("印章类型"),
    FILE_TYPE("文件类型"),
    PURCHASE_TYPE("采购类型"),
    SCAN_TYPE("资产使用规模"),
    PAYMENT_TYPE("付款方式"),
    VEHICLE_TYPE("出差方式"),
    ;
    private String description;

    BaseDictType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
