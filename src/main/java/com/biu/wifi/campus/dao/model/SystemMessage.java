package com.biu.wifi.campus.dao.model;

import com.biu.wifi.core.base.CoreEntity;

import java.util.Date;

public class SystemMessage extends CoreEntity {
    private Integer id;

    private String title;

    private String content;

    private Short sendType;

    private String receiveUnitIds;

    private String receiverIds;

    private Date sendTime;

    private Short isDetele;

    private Date deleteTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Short getSendType() {
        return sendType;
    }

    public void setSendType(Short sendType) {
        this.sendType = sendType;
    }

    public String getReceiveUnitIds() {
        return receiveUnitIds;
    }

    public void setReceiveUnitIds(String receiveUnitIds) {
        this.receiveUnitIds = receiveUnitIds == null ? null : receiveUnitIds.trim();
    }

    public String getReceiverIds() {
        return receiverIds;
    }

    public void setReceiverIds(String receiverIds) {
        this.receiverIds = receiverIds == null ? null : receiverIds.trim();
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public Short getIsDetele() {
        return isDetele;
    }

    public void setIsDetele(Short isDetele) {
        this.isDetele = isDetele;
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
        SystemMessage other = (SystemMessage) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getTitle() == null ? other.getTitle() == null : this.getTitle().equals(other.getTitle()))
                && (this.getContent() == null ? other.getContent() == null : this.getContent().equals(other.getContent()))
                && (this.getSendType() == null ? other.getSendType() == null : this.getSendType().equals(other.getSendType()))
                && (this.getReceiveUnitIds() == null ? other.getReceiveUnitIds() == null : this.getReceiveUnitIds().equals(other.getReceiveUnitIds()))
                && (this.getReceiverIds() == null ? other.getReceiverIds() == null : this.getReceiverIds().equals(other.getReceiverIds()))
                && (this.getSendTime() == null ? other.getSendTime() == null : this.getSendTime().equals(other.getSendTime()))
                && (this.getIsDetele() == null ? other.getIsDetele() == null : this.getIsDetele().equals(other.getIsDetele()))
                && (this.getDeleteTime() == null ? other.getDeleteTime() == null : this.getDeleteTime().equals(other.getDeleteTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getTitle() == null) ? 0 : getTitle().hashCode());
        result = prime * result + ((getContent() == null) ? 0 : getContent().hashCode());
        result = prime * result + ((getSendType() == null) ? 0 : getSendType().hashCode());
        result = prime * result + ((getReceiveUnitIds() == null) ? 0 : getReceiveUnitIds().hashCode());
        result = prime * result + ((getReceiverIds() == null) ? 0 : getReceiverIds().hashCode());
        result = prime * result + ((getSendTime() == null) ? 0 : getSendTime().hashCode());
        result = prime * result + ((getIsDetele() == null) ? 0 : getIsDetele().hashCode());
        result = prime * result + ((getDeleteTime() == null) ? 0 : getDeleteTime().hashCode());
        return result;
    }
}