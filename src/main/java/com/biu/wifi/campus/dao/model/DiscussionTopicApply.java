package com.biu.wifi.campus.dao.model;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author 张彬.
 * @date 2021/4/4 21:56.
 */

/**
 * 会议议题申请表
 */
public class DiscussionTopicApply implements Serializable {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 议题名称
     */
    private String topicName;

    /**
     * 议题类型1院长会议议题,2党委会议议题
     */
    private Short topicType;

    /**
     * 预计开始日期
     */
    private Date startDate;

    /**
     * 议题内容
     */
    private String topicContent;

    /**
     * 提报部门id
     */
    private Integer applyDeptId;

    /**
     * 列席部门id,多个逗号分隔
     */
    private String attendDeptId;

    /**
     * 状态(1审批中,2通过,3驳回,4取消,5已完成)
     */
    private Short status;

    /**
     * 创建用户id
     */
    private Integer userId;

    /**
     * 审核人id,多个逗号按审核顺序依次逗号隔开
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
     * 是否删除1是2否
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

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public Short getTopicType() {
        return topicType;
    }

    public void setTopicType(Short topicType) {
        this.topicType = topicType;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getTopicContent() {
        return topicContent;
    }

    public void setTopicContent(String topicContent) {
        this.topicContent = topicContent;
    }

    public Integer getApplyDeptId() {
        return applyDeptId;
    }

    public void setApplyDeptId(Integer applyDeptId) {
        this.applyDeptId = applyDeptId;
    }

    public String getAttendDeptId() {
        return attendDeptId;
    }

    public void setAttendDeptId(String attendDeptId) {
        this.attendDeptId = attendDeptId;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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