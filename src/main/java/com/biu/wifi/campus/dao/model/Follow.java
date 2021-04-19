package com.biu.wifi.campus.dao.model;

import com.biu.wifi.core.base.CoreEntity;

import java.util.Date;

public class Follow extends CoreEntity {
    private Integer id;

    private Integer followerId;

    private Integer beFollowedId;

    private Date createTime;

    private Short isTwoWay;

    private Short isCancel;

    private Date cancelTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFollowerId() {
        return followerId;
    }

    public void setFollowerId(Integer followerId) {
        this.followerId = followerId;
    }

    public Integer getBeFollowedId() {
        return beFollowedId;
    }

    public void setBeFollowedId(Integer beFollowedId) {
        this.beFollowedId = beFollowedId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Short getIsTwoWay() {
        return isTwoWay;
    }

    public void setIsTwoWay(Short isTwoWay) {
        this.isTwoWay = isTwoWay;
    }

    public Short getIsCancel() {
        return isCancel;
    }

    public void setIsCancel(Short isCancel) {
        this.isCancel = isCancel;
    }

    public Date getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(Date cancelTime) {
        this.cancelTime = cancelTime;
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
        Follow other = (Follow) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getFollowerId() == null ? other.getFollowerId() == null : this.getFollowerId().equals(other.getFollowerId()))
                && (this.getBeFollowedId() == null ? other.getBeFollowedId() == null : this.getBeFollowedId().equals(other.getBeFollowedId()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
                && (this.getIsTwoWay() == null ? other.getIsTwoWay() == null : this.getIsTwoWay().equals(other.getIsTwoWay()))
                && (this.getIsCancel() == null ? other.getIsCancel() == null : this.getIsCancel().equals(other.getIsCancel()))
                && (this.getCancelTime() == null ? other.getCancelTime() == null : this.getCancelTime().equals(other.getCancelTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getFollowerId() == null) ? 0 : getFollowerId().hashCode());
        result = prime * result + ((getBeFollowedId() == null) ? 0 : getBeFollowedId().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getIsTwoWay() == null) ? 0 : getIsTwoWay().hashCode());
        result = prime * result + ((getIsCancel() == null) ? 0 : getIsCancel().hashCode());
        result = prime * result + ((getCancelTime() == null) ? 0 : getCancelTime().hashCode());
        return result;
    }
}