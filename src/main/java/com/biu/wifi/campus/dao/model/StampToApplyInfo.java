package com.biu.wifi.campus.dao.model;

import java.util.Date;

public class StampToApplyInfo {
    private Integer id;

    private Integer applyType;

    private String reason;

    private Integer stampType;

    private Date startDate;

    private Date endDate;

    private Date backDate;

    private Integer applyUserId;

    private String applyUserName;

    private String applyUserNo;

    private String applyUserTel;

    private String applyUserDeptId;

    private Short status;

    private String auditUser;

    private Integer currentAuditUserId;

    private Date createTime;

    private Date updateTime;

    private Short isDelete;

    private Date deleteTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getApplyType() {
        return applyType;
    }

    public void setApplyType(Integer applyType) {
        this.applyType = applyType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason == null ? null : reason.trim();
    }

    public Integer getStampType() {
        return stampType;
    }

    public void setStampType(Integer stampType) {
        this.stampType = stampType;
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
        this.applyUserName = applyUserName == null ? null : applyUserName.trim();
    }

    public String getApplyUserNo() {
        return applyUserNo;
    }

    public void setApplyUserNo(String applyUserNo) {
        this.applyUserNo = applyUserNo == null ? null : applyUserNo.trim();
    }

    public String getApplyUserTel() {
        return applyUserTel;
    }

    public void setApplyUserTel(String applyUserTel) {
        this.applyUserTel = applyUserTel == null ? null : applyUserTel.trim();
    }

    public String getApplyUserDeptId() {
        return applyUserDeptId;
    }

    public void setApplyUserDeptId(String applyUserDeptId) {
        this.applyUserDeptId = applyUserDeptId == null ? null : applyUserDeptId.trim();
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
        this.auditUser = auditUser == null ? null : auditUser.trim();
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