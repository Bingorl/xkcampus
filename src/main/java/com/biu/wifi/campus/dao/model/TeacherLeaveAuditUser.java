package com.biu.wifi.campus.dao.model;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author 张彬.
 * @date 2021/4/4 0:24.
 */
/**
    * 请假审批人流向信息表(第一人审批人定义后续审批人队列信息)
    */
public class TeacherLeaveAuditUser implements Serializable {
    /**
    * 主键
    */
    private Integer id;

    /**
    * 学校ID
    */
    private Integer schoolId;

    /**
    * 学院/部门ID
    */
    private Integer instituteId;

    /**
    * 审批人ID字符串，按照审批顺序排列，逗号分隔
    */
    private String auditUser;

    /**
    * 请假类型(1-3天以下，2-3~7天，3-7天以上)
    */
    private Short type;

    /**
    * 创建时间
    */
    private Date createTime;

    /**
    * 更新时间
    */
    private Date updateTime;

    /**
    * 是否删除(1是2否)
    */
    private Short isDelete;

    /**
    * 删除时间
    */
    private Date deleteTime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
    }

    public Integer getInstituteId() {
        return instituteId;
    }

    public void setInstituteId(Integer instituteId) {
        this.instituteId = instituteId;
    }

    public String getAuditUser() {
        return auditUser;
    }

    public void setAuditUser(String auditUser) {
        this.auditUser = auditUser;
    }

    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Short getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Short isDelete) {
        this.isDelete = isDelete;
    }

    public Date getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Date deleteTime) {
        this.deleteTime = deleteTime;
    }
}