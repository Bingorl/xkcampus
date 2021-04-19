package com.biu.wifi.campus.dao.model;

import java.io.Serializable;
import java.util.Date;

/**
 * biu_activity
 *
 * @author
 */
public class Activity implements Serializable {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 学校ID
     */
    private Integer schoolId;

    /**
     * 发起人ID
     */
    private Integer userId;

    /**
     * 活动名称
     */
    private String name;

    /**
     * 活动logo
     */
    private String logo;

    /**
     * 活动简介
     */
    private String profile;

    /**
     * 状态(NULL未发布，1进行中，2暂停)
     */
    private Short status;

    /**
     * 可参与投票的人ID(指定人可投)
     */
    private String votePermission;

    /**
     * 单人最大可投票数
     */
    private Integer voteMaxCount;

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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public String getVotePermission() {
        return votePermission;
    }

    public void setVotePermission(String votePermission) {
        this.votePermission = votePermission;
    }

    public Integer getVoteMaxCount() {
        return voteMaxCount;
    }

    public void setVoteMaxCount(Integer voteMaxCount) {
        this.voteMaxCount = voteMaxCount;
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
        Activity other = (Activity) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getSchoolId() == null ? other.getSchoolId() == null : this.getSchoolId().equals(other.getSchoolId()))
                && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
                && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
                && (this.getLogo() == null ? other.getLogo() == null : this.getLogo().equals(other.getLogo()))
                && (this.getProfile() == null ? other.getProfile() == null : this.getProfile().equals(other.getProfile()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
                && (this.getVotePermission() == null ? other.getVotePermission() == null : this.getVotePermission().equals(other.getVotePermission()))
                && (this.getVoteMaxCount() == null ? other.getVoteMaxCount() == null : this.getVoteMaxCount().equals(other.getVoteMaxCount()))
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
        result = prime * result + ((getSchoolId() == null) ? 0 : getSchoolId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getLogo() == null) ? 0 : getLogo().hashCode());
        result = prime * result + ((getProfile() == null) ? 0 : getProfile().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getVotePermission() == null) ? 0 : getVotePermission().hashCode());
        result = prime * result + ((getVoteMaxCount() == null) ? 0 : getVoteMaxCount().hashCode());
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
        sb.append(", schoolId=").append(schoolId);
        sb.append(", userId=").append(userId);
        sb.append(", name=").append(name);
        sb.append(", logo=").append(logo);
        sb.append(", profile=").append(profile);
        sb.append(", status=").append(status);
        sb.append(", votePermission=").append(votePermission);
        sb.append(", voteMaxCount=").append(voteMaxCount);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", deleteTime=").append(deleteTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}