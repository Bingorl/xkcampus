package com.biu.wifi.campus.dao.model;

import java.io.Serializable;
import java.util.Date;

/**
 * biu_leave_info
 *
 * @author
 */
public class LeaveInfo implements Serializable {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 请假类型(按请假天数和性质来分)
     */
    private Integer leaveType;

    /**
     * 类型(1事假，2病假)
     */
    private Short reasonType;

    /**
     * 请假原因
     */
    private String reason;

    /**
     * 请假材料(照片、证明)
     */
    private String attachment;

    /**
     * 去向(1外出，2回家，3留校)
     */
    private Short goTo;

    /**
     * 实习单位名称或重大活动名称
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
     * 请假人ID
     */
    private Integer userId;

    /**
     * 请假人姓名
     */
    private String realName;

    /**
     * 请假人手机号
     */
    private String tel;

    /**
     * 学号
     */
    private String stuNo;

    /**
     * 班级ID
     */
    private Integer classId;

    /**
     * 公寓信息
     */
    private String apartment;

    /**
     * 宿舍楼信息
     */
    private String apartmentBuilding;

    /**
     * 紧急联系人姓名
     */
    private String linkMan;

    /**
     * 紧急联系人电话
     */
    private String linkTel;

    /**
     * 紧急联系人是否为父母(1是，2否)
     */
    private String isParentLink;

    /**
     * 请假相关说明文件ID（比如安全承诺书）
     */
    private Integer paperId;

    /**
     * 状态(1审批中,2通过,3驳回,4取消,5已完成)
     */
    private Short status;

    /**
     * 审批人ID字符串，按照审批顺序排列，逗号分隔
     */
    private String auditUser;

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

    public Short getReasonType() {
        return reasonType;
    }

    public void setReasonType(Short reasonType) {
        this.reasonType = reasonType;
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

    public Short getGoTo() {
        return goTo;
    }

    public void setGoTo(Short goTo) {
        this.goTo = goTo;
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getStuNo() {
        return stuNo;
    }

    public void setStuNo(String stuNo) {
        this.stuNo = stuNo;
    }

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }

    public String getApartmentBuilding() {
        return apartmentBuilding;
    }

    public void setApartmentBuilding(String apartmentBuilding) {
        this.apartmentBuilding = apartmentBuilding;
    }

    public String getLinkMan() {
        return linkMan;
    }

    public void setLinkMan(String linkMan) {
        this.linkMan = linkMan;
    }

    public String getLinkTel() {
        return linkTel;
    }

    public void setLinkTel(String linkTel) {
        this.linkTel = linkTel;
    }

    public String getIsParentLink() {
        return isParentLink;
    }

    public void setIsParentLink(String isParentLink) {
        this.isParentLink = isParentLink;
    }

    public Integer getPaperId() {
        return paperId;
    }

    public void setPaperId(Integer paperId) {
        this.paperId = paperId;
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

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        LeaveInfo other = (LeaveInfo) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getLeaveType() == null ? other.getLeaveType() == null : this.getLeaveType().equals(other.getLeaveType()))
                && (this.getReasonType() == null ? other.getReasonType() == null : this.getReasonType().equals(other.getReasonType()))
                && (this.getReason() == null ? other.getReason() == null : this.getReason().equals(other.getReason()))
                && (this.getAttachment() == null ? other.getAttachment() == null : this.getAttachment().equals(other.getAttachment()))
                && (this.getGoTo() == null ? other.getGoTo() == null : this.getGoTo().equals(other.getGoTo()))
                && (this.getOrganization() == null ? other.getOrganization() == null : this.getOrganization().equals(other.getOrganization()))
                && (this.getStartDate() == null ? other.getStartDate() == null : this.getStartDate().equals(other.getStartDate()))
                && (this.getEndDate() == null ? other.getEndDate() == null : this.getEndDate().equals(other.getEndDate()))
                && (this.getBackDate() == null ? other.getBackDate() == null : this.getBackDate().equals(other.getBackDate()))
                && (this.getPlanDays() == null ? other.getPlanDays() == null : this.getPlanDays().equals(other.getPlanDays()))
                && (this.getActDays() == null ? other.getActDays() == null : this.getActDays().equals(other.getActDays()))
                && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
                && (this.getRealName() == null ? other.getRealName() == null : this.getRealName().equals(other.getRealName()))
                && (this.getTel() == null ? other.getTel() == null : this.getTel().equals(other.getTel()))
                && (this.getStuNo() == null ? other.getStuNo() == null : this.getStuNo().equals(other.getStuNo()))
                && (this.getClassId() == null ? other.getClassId() == null : this.getClassId().equals(other.getClassId()))
                && (this.getApartment() == null ? other.getApartment() == null : this.getApartment().equals(other.getApartment()))
                && (this.getApartmentBuilding() == null ? other.getApartmentBuilding() == null : this.getApartmentBuilding().equals(other.getApartmentBuilding()))
                && (this.getLinkMan() == null ? other.getLinkMan() == null : this.getLinkMan().equals(other.getLinkMan()))
                && (this.getLinkTel() == null ? other.getLinkTel() == null : this.getLinkTel().equals(other.getLinkTel()))
                && (this.getIsParentLink() == null ? other.getIsParentLink() == null : this.getIsParentLink().equals(other.getIsParentLink()))
                && (this.getPaperId() == null ? other.getPaperId() == null : this.getPaperId().equals(other.getPaperId()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
                && (this.getAuditUser() == null ? other.getAuditUser() == null : this.getAuditUser().equals(other.getAuditUser()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
                && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
                && (this.getIsDelete() == null ? other.getIsDelete() == null : this.getIsDelete().equals(other.getIsDelete()))
                && (this.getDeleteTime() == null ? other.getDeleteTime() == null : this.getDeleteTime().equals(other.getDeleteTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getLeaveType() == null) ? 0 : getLeaveType().hashCode());
        result = prime * result + ((getReasonType() == null) ? 0 : getReasonType().hashCode());
        result = prime * result + ((getReason() == null) ? 0 : getReason().hashCode());
        result = prime * result + ((getAttachment() == null) ? 0 : getAttachment().hashCode());
        result = prime * result + ((getGoTo() == null) ? 0 : getGoTo().hashCode());
        result = prime * result + ((getOrganization() == null) ? 0 : getOrganization().hashCode());
        result = prime * result + ((getStartDate() == null) ? 0 : getStartDate().hashCode());
        result = prime * result + ((getEndDate() == null) ? 0 : getEndDate().hashCode());
        result = prime * result + ((getBackDate() == null) ? 0 : getBackDate().hashCode());
        result = prime * result + ((getPlanDays() == null) ? 0 : getPlanDays().hashCode());
        result = prime * result + ((getActDays() == null) ? 0 : getActDays().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getRealName() == null) ? 0 : getRealName().hashCode());
        result = prime * result + ((getTel() == null) ? 0 : getTel().hashCode());
        result = prime * result + ((getStuNo() == null) ? 0 : getStuNo().hashCode());
        result = prime * result + ((getClassId() == null) ? 0 : getClassId().hashCode());
        result = prime * result + ((getApartment() == null) ? 0 : getApartment().hashCode());
        result = prime * result + ((getApartmentBuilding() == null) ? 0 : getApartmentBuilding().hashCode());
        result = prime * result + ((getLinkMan() == null) ? 0 : getLinkMan().hashCode());
        result = prime * result + ((getLinkTel() == null) ? 0 : getLinkTel().hashCode());
        result = prime * result + ((getIsParentLink() == null) ? 0 : getIsParentLink().hashCode());
        result = prime * result + ((getPaperId() == null) ? 0 : getPaperId().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getAuditUser() == null) ? 0 : getAuditUser().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getIsDelete() == null) ? 0 : getIsDelete().hashCode());
        result = prime * result + ((getDeleteTime() == null) ? 0 : getDeleteTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", leaveType=").append(leaveType);
        sb.append(", reasonType=").append(reasonType);
        sb.append(", reason=").append(reason);
        sb.append(", attachment=").append(attachment);
        sb.append(", goTo=").append(goTo);
        sb.append(", organization=").append(organization);
        sb.append(", startDate=").append(startDate);
        sb.append(", endDate=").append(endDate);
        sb.append(", backDate=").append(backDate);
        sb.append(", planDays=").append(planDays);
        sb.append(", actDays=").append(actDays);
        sb.append(", userId=").append(userId);
        sb.append(", realName=").append(realName);
        sb.append(", tel=").append(tel);
        sb.append(", stuNo=").append(stuNo);
        sb.append(", classId=").append(classId);
        sb.append(", apartment=").append(apartment);
        sb.append(", apartmentBuilding=").append(apartmentBuilding);
        sb.append(", linkMan=").append(linkMan);
        sb.append(", linkTel=").append(linkTel);
        sb.append(", isParentLink=").append(isParentLink);
        sb.append(", paperId=").append(paperId);
        sb.append(", status=").append(status);
        sb.append(", auditUser=").append(auditUser);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", deleteTime=").append(deleteTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}