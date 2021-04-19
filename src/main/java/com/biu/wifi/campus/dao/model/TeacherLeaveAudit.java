package com.biu.wifi.campus.dao.model;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author 张彬.
 * @date 2021/4/4 0:43.
 */
/**
    * 教师请假审核表
    */
public class TeacherLeaveAudit implements Serializable {
    /**
    * 主键
    */
    private Integer id;

    /**
    * 请假ID
    */
    private Integer leaveId;

    /**
    * 审核人ID
    */
    private Integer userId;

    /**
    * 是否通过(1通过,2驳回)
    */
    private Short isPass;

    /**
    * 审核时间
    */
    private Date auditTime;

    /**
    * 备注
    */
    private String remark;

    /**
    * 创建时间
    */
    private Date createTime;

    /**
    * 修改时间
    */
    private Date updateTime;

    /**
    * 是否删除(1是,2否）
    */
    private Short isDelete;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(Integer leaveId) {
        this.leaveId = leaveId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Short getIsPass() {
        return isPass;
    }

    public void setIsPass(Short isPass) {
        this.isPass = isPass;
    }

    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
}