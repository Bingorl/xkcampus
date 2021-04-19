package com.biu.wifi.campus.dao.model;

import java.io.Serializable;
import java.util.Date;

/**
 * biu_classroom_book
 * @author 
 */
public class ClassroomBook implements Serializable {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 状态（1审核中，2通过，3驳回）
     */
    private Integer status;

    /**
     * 时间戳
     */
    private String version;

    /**
     * 审批人ID
     */
    private Integer auditUserId;

    /**
     * 审批人名字
     */
    private String auditUserName;

    /**
     * 审批时间
     */
    private Date auditTime;

    /**
     * 审批意见
     */
    private String auditRemark;

    /**
     * 教学楼ID
     */
    private Integer classroomBuildingId;

    /**
     * 人数范围（0-50）
     */
    private String personCount;

    /**
     * 使用类型ID
     */
    private Integer useTypeId;

    /**
     * 组织ID
     */
    private Integer organizationId;

    /**
     * 标题
     */
    private String title;

    /**
     * 详细说明内容
     */
    private String content;

    /**
     * 预约的教室号字符串，逗号分隔
     */
    private String classroomNo;

    /**
     * 调整的教室号字符串，逗号分隔
     */
    private String adjustClassroomNo;

    /**
     * 是否使用了多媒体(1是，2否)
     */
    private Short isUseMedia;

    /**
     * 联系人
     */
    private String linkMan;

    /**
     * 联系电话
     */
    private String linkPhone;

    /**
     * 工号或学号
     */
    private String linkManNo;

    /**
     * 申请人ID
     */
    private Integer userId;

    /**
     * 预约使用日期
     */
    private String bookDate;

    /**
     * 预约使用时间
     */
    private String bookPeriod;

    /**
     * 预定使用的节次
     */
    private String bookSection;

    /**
     * 查询条件（教务调整教室时需要根据原条件进行筛选）
     */
    private String bookCondition;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 是否删除（1是，2否）
     */
    private Short isDelete;

    /**
     * 删除时间
     */
    private Date deleteTime;

    /**
     * 备注
     */
    private String remark;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getAuditUserId() {
        return auditUserId;
    }

    public void setAuditUserId(Integer auditUserId) {
        this.auditUserId = auditUserId;
    }

    public String getAuditUserName() {
        return auditUserName;
    }

    public void setAuditUserName(String auditUserName) {
        this.auditUserName = auditUserName;
    }

    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    public String getAuditRemark() {
        return auditRemark;
    }

    public void setAuditRemark(String auditRemark) {
        this.auditRemark = auditRemark;
    }

    public Integer getClassroomBuildingId() {
        return classroomBuildingId;
    }

    public void setClassroomBuildingId(Integer classroomBuildingId) {
        this.classroomBuildingId = classroomBuildingId;
    }

    public String getPersonCount() {
        return personCount;
    }

    public void setPersonCount(String personCount) {
        this.personCount = personCount;
    }

    public Integer getUseTypeId() {
        return useTypeId;
    }

    public void setUseTypeId(Integer useTypeId) {
        this.useTypeId = useTypeId;
    }

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getClassroomNo() {
        return classroomNo;
    }

    public void setClassroomNo(String classroomNo) {
        this.classroomNo = classroomNo;
    }

    public String getAdjustClassroomNo() {
        return adjustClassroomNo;
    }

    public void setAdjustClassroomNo(String adjustClassroomNo) {
        this.adjustClassroomNo = adjustClassroomNo;
    }

    public Short getIsUseMedia() {
        return isUseMedia;
    }

    public void setIsUseMedia(Short isUseMedia) {
        this.isUseMedia = isUseMedia;
    }

    public String getLinkMan() {
        return linkMan;
    }

    public void setLinkMan(String linkMan) {
        this.linkMan = linkMan;
    }

    public String getLinkPhone() {
        return linkPhone;
    }

    public void setLinkPhone(String linkPhone) {
        this.linkPhone = linkPhone;
    }

    public String getLinkManNo() {
        return linkManNo;
    }

    public void setLinkManNo(String linkManNo) {
        this.linkManNo = linkManNo;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getBookDate() {
        return bookDate;
    }

    public void setBookDate(String bookDate) {
        this.bookDate = bookDate;
    }

    public String getBookPeriod() {
        return bookPeriod;
    }

    public void setBookPeriod(String bookPeriod) {
        this.bookPeriod = bookPeriod;
    }

    public String getBookSection() {
        return bookSection;
    }

    public void setBookSection(String bookSection) {
        this.bookSection = bookSection;
    }

    public String getBookCondition() {
        return bookCondition;
    }

    public void setBookCondition(String bookCondition) {
        this.bookCondition = bookCondition;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
        ClassroomBook other = (ClassroomBook) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getVersion() == null ? other.getVersion() == null : this.getVersion().equals(other.getVersion()))
                && (this.getAuditUserId() == null ? other.getAuditUserId() == null : this.getAuditUserId().equals(other.getAuditUserId()))
                && (this.getAuditUserName() == null ? other.getAuditUserName() == null : this.getAuditUserName().equals(other.getAuditUserName()))
            && (this.getAuditTime() == null ? other.getAuditTime() == null : this.getAuditTime().equals(other.getAuditTime()))
                && (this.getAuditRemark() == null ? other.getAuditRemark() == null : this.getAuditRemark().equals(other.getAuditRemark()))
            && (this.getClassroomBuildingId() == null ? other.getClassroomBuildingId() == null : this.getClassroomBuildingId().equals(other.getClassroomBuildingId()))
            && (this.getPersonCount() == null ? other.getPersonCount() == null : this.getPersonCount().equals(other.getPersonCount()))
            && (this.getUseTypeId() == null ? other.getUseTypeId() == null : this.getUseTypeId().equals(other.getUseTypeId()))
            && (this.getOrganizationId() == null ? other.getOrganizationId() == null : this.getOrganizationId().equals(other.getOrganizationId()))
            && (this.getTitle() == null ? other.getTitle() == null : this.getTitle().equals(other.getTitle()))
            && (this.getContent() == null ? other.getContent() == null : this.getContent().equals(other.getContent()))
            && (this.getClassroomNo() == null ? other.getClassroomNo() == null : this.getClassroomNo().equals(other.getClassroomNo()))
            && (this.getAdjustClassroomNo() == null ? other.getAdjustClassroomNo() == null : this.getAdjustClassroomNo().equals(other.getAdjustClassroomNo()))
            && (this.getIsUseMedia() == null ? other.getIsUseMedia() == null : this.getIsUseMedia().equals(other.getIsUseMedia()))
            && (this.getLinkMan() == null ? other.getLinkMan() == null : this.getLinkMan().equals(other.getLinkMan()))
            && (this.getLinkPhone() == null ? other.getLinkPhone() == null : this.getLinkPhone().equals(other.getLinkPhone()))
            && (this.getLinkManNo() == null ? other.getLinkManNo() == null : this.getLinkManNo().equals(other.getLinkManNo()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getBookDate() == null ? other.getBookDate() == null : this.getBookDate().equals(other.getBookDate()))
            && (this.getBookPeriod() == null ? other.getBookPeriod() == null : this.getBookPeriod().equals(other.getBookPeriod()))
            && (this.getBookSection() == null ? other.getBookSection() == null : this.getBookSection().equals(other.getBookSection()))
            && (this.getBookCondition() == null ? other.getBookCondition() == null : this.getBookCondition().equals(other.getBookCondition()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getIsDelete() == null ? other.getIsDelete() == null : this.getIsDelete().equals(other.getIsDelete()))
            && (this.getDeleteTime() == null ? other.getDeleteTime() == null : this.getDeleteTime().equals(other.getDeleteTime()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getVersion() == null) ? 0 : getVersion().hashCode());
        result = prime * result + ((getAuditUserId() == null) ? 0 : getAuditUserId().hashCode());
        result = prime * result + ((getAuditUserName() == null) ? 0 : getAuditUserName().hashCode());
        result = prime * result + ((getAuditTime() == null) ? 0 : getAuditTime().hashCode());
        result = prime * result + ((getAuditRemark() == null) ? 0 : getAuditRemark().hashCode());
        result = prime * result + ((getClassroomBuildingId() == null) ? 0 : getClassroomBuildingId().hashCode());
        result = prime * result + ((getPersonCount() == null) ? 0 : getPersonCount().hashCode());
        result = prime * result + ((getUseTypeId() == null) ? 0 : getUseTypeId().hashCode());
        result = prime * result + ((getOrganizationId() == null) ? 0 : getOrganizationId().hashCode());
        result = prime * result + ((getTitle() == null) ? 0 : getTitle().hashCode());
        result = prime * result + ((getContent() == null) ? 0 : getContent().hashCode());
        result = prime * result + ((getClassroomNo() == null) ? 0 : getClassroomNo().hashCode());
        result = prime * result + ((getAdjustClassroomNo() == null) ? 0 : getAdjustClassroomNo().hashCode());
        result = prime * result + ((getIsUseMedia() == null) ? 0 : getIsUseMedia().hashCode());
        result = prime * result + ((getLinkMan() == null) ? 0 : getLinkMan().hashCode());
        result = prime * result + ((getLinkPhone() == null) ? 0 : getLinkPhone().hashCode());
        result = prime * result + ((getLinkManNo() == null) ? 0 : getLinkManNo().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getBookDate() == null) ? 0 : getBookDate().hashCode());
        result = prime * result + ((getBookPeriod() == null) ? 0 : getBookPeriod().hashCode());
        result = prime * result + ((getBookSection() == null) ? 0 : getBookSection().hashCode());
        result = prime * result + ((getBookCondition() == null) ? 0 : getBookCondition().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getIsDelete() == null) ? 0 : getIsDelete().hashCode());
        result = prime * result + ((getDeleteTime() == null) ? 0 : getDeleteTime().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", status=").append(status);
        sb.append(", version=").append(version);
        sb.append(", auditUserId=").append(auditUserId);
        sb.append(", auditUserName=").append(auditUserName);
        sb.append(", auditTime=").append(auditTime);
        sb.append(", auditRemark=").append(auditRemark);
        sb.append(", classroomBuildingId=").append(classroomBuildingId);
        sb.append(", personCount=").append(personCount);
        sb.append(", useTypeId=").append(useTypeId);
        sb.append(", organizationId=").append(organizationId);
        sb.append(", title=").append(title);
        sb.append(", content=").append(content);
        sb.append(", classroomNo=").append(classroomNo);
        sb.append(", adjustClassroomNo=").append(adjustClassroomNo);
        sb.append(", isUseMedia=").append(isUseMedia);
        sb.append(", linkMan=").append(linkMan);
        sb.append(", linkPhone=").append(linkPhone);
        sb.append(", linkManNo=").append(linkManNo);
        sb.append(", userId=").append(userId);
        sb.append(", bookDate=").append(bookDate);
        sb.append(", bookPeriod=").append(bookPeriod);
        sb.append(", bookSection=").append(bookSection);
        sb.append(", bookCondition=").append(bookCondition);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", deleteTime=").append(deleteTime);
        sb.append(", remark=").append(remark);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}