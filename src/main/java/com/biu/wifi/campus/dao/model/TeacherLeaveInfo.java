package com.biu.wifi.campus.dao.model;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author 张彬.
 * @date 2021/4/3 19:08.
 */

/**
 * 教师请假表
 */
public class TeacherLeaveInfo implements Serializable {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 请假类型(1年假2事假3病假4调休5产假6陪产假7婚假8产检假9丧假10哺乳假)
     */
    private Integer leaveType;

    /**
     * 请假原因
     */
    private String reason;

    /**
     * 请假材料(附件、证明)
     */
    private String attachment;

    /**
     * 活动组织名称或重大活动名称
     */
    private String organization;

    /**
     * 开始日期
     */
    private Date startDate;

    /**
     * 结束日期
     */
    private Date endDate;

    /**
     * 销假日期
     */
    private Date backDate;

    /**
     * 计划请假天数
     */
    private Integer planDays;

    /**
     * 实际请假天数
     */
    private Integer actDays;

    /**
     * 申请人ID
     */
    private Integer applyUserId;

    /**
     * 申请人姓名
     */
    private String applyUserName;

    /**
     * 申请人工号
     */
    private String applyUserNo;

    /**
     * 申请人手机号
     */
    private String applyUserTel;

    /**
     * 申请人所在部门id
     */
    private String applyUserDeptId;

    /**
     * 状态(1审批中,2通过,3驳回,4取消,5已完成)
     */
    private Short status;

    /**
     * 审批人ID字符串，按照审批顺序排列，逗号分隔
     */
    private String auditUser;

    /**
     * 当前审核人id
     */
    private Integer currentAuditUserId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 是否删除(1是，2否)
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

    public Integer getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(Integer leaveType) {
        this.leaveType = leaveType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getBackDate() {
        return backDate;
    }

    public void setBackDate(Date backDate) {
        this.backDate = backDate;
    }

    public Integer getPlanDays() {
        return planDays;
    }

    public void setPlanDays(Integer planDays) {
        this.planDays = planDays;
    }

    public Integer getActDays() {
        return actDays;
    }

    public void setActDays(Integer actDays) {
        this.actDays = actDays;
    }

    public Integer getApplyUserId() {
        return applyUserId;
    }

    public void setApplyUserId(Integer applyUserId) {
        this.applyUserId = applyUserId;
    }

    public String getApplyUserName() {
        return applyUserName;
    }

    public void setApplyUserName(String applyUserName) {
        this.applyUserName = applyUserName;
    }

    public String getApplyUserNo() {
        return applyUserNo;
    }

    public void setApplyUserNo(String applyUserNo) {
        this.applyUserNo = applyUserNo;
    }

    public String getApplyUserTel() {
        return applyUserTel;
    }

    public void setApplyUserTel(String applyUserTel) {
        this.applyUserTel = applyUserTel;
    }

    public String getApplyUserDeptId() {
        return applyUserDeptId;
    }

    public void setApplyUserDeptId(String applyUserDeptId) {
        this.applyUserDeptId = applyUserDeptId;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public String getAuditUser() {
        return auditUser;
    }

    public void setAuditUser(String auditUser) {
        this.auditUser = auditUser;
    }

    public Integer getCurrentAuditUserId() {
        return currentAuditUserId;
    }

    public void setCurrentAuditUserId(Integer currentAuditUserId) {
        this.currentAuditUserId = currentAuditUserId;
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