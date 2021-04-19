package com.biu.wifi.campus.dao.model;

import com.biu.wifi.core.base.CoreEntity;

import java.util.Date;

public class Saying extends CoreEntity {
    private Integer id;

    private Integer creatorId;

    private String atUser;

    private String img;

    private Integer watchCount;

    private Short privacy;

    private Short isForward;

    private Short originType;

    private Integer originId;

    private Integer originSayingId;

    private Integer forwardNumber;

    private Date createTime;

    private Integer likeNumber;

    private Integer commentNumber;

    private Short isDelete;

    private Date deleteTime;

    private String content;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public String getAtUser() {
        return atUser;
    }

    public void setAtUser(String atUser) {
        this.atUser = atUser == null ? null : atUser.trim();
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img == null ? null : img.trim();
    }

    public Integer getWatchCount() {
        return watchCount;
    }

    public void setWatchCount(Integer watchCount) {
        this.watchCount = watchCount;
    }

    public Short getPrivacy() {
        return privacy;
    }

    public void setPrivacy(Short privacy) {
        this.privacy = privacy;
    }

    public Short getIsForward() {
        return isForward;
    }

    public void setIsForward(Short isForward) {
        this.isForward = isForward;
    }

    public Short getOriginType() {
        return originType;
    }

    public void setOriginType(Short originType) {
        this.originType = originType;
    }

    public Integer getOriginId() {
        return originId;
    }

    public void setOriginId(Integer originId) {
        this.originId = originId;
    }

    public Integer getOriginSayingId() {
        return originSayingId;
    }

    public void setOriginSayingId(Integer originSayingId) {
        this.originSayingId = originSayingId;
    }

    public Integer getForwardNumber() {
        return forwardNumber;
    }

    public void setForwardNumber(Integer forwardNumber) {
        this.forwardNumber = forwardNumber;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getLikeNumber() {
        return likeNumber;
    }

    public void setLikeNumber(Integer likeNumber) {
        this.likeNumber = likeNumber;
    }

    public Integer getCommentNumber() {
        return commentNumber;
    }

    public void setCommentNumber(Integer commentNumber) {
        this.commentNumber = commentNumber;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
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
        Saying other = (Saying) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getCreatorId() == null ? other.getCreatorId() == null : this.getCreatorId().equals(other.getCreatorId()))
                && (this.getAtUser() == null ? other.getAtUser() == null : this.getAtUser().equals(other.getAtUser()))
                && (this.getImg() == null ? other.getImg() == null : this.getImg().equals(other.getImg()))
                && (this.getWatchCount() == null ? other.getWatchCount() == null : this.getWatchCount().equals(other.getWatchCount()))
                && (this.getPrivacy() == null ? other.getPrivacy() == null : this.getPrivacy().equals(other.getPrivacy()))
                && (this.getIsForward() == null ? other.getIsForward() == null : this.getIsForward().equals(other.getIsForward()))
                && (this.getOriginType() == null ? other.getOriginType() == null : this.getOriginType().equals(other.getOriginType()))
                && (this.getOriginId() == null ? other.getOriginId() == null : this.getOriginId().equals(other.getOriginId()))
                && (this.getOriginSayingId() == null ? other.getOriginSayingId() == null : this.getOriginSayingId().equals(other.getOriginSayingId()))
                && (this.getForwardNumber() == null ? other.getForwardNumber() == null : this.getForwardNumber().equals(other.getForwardNumber()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
                && (this.getLikeNumber() == null ? other.getLikeNumber() == null : this.getLikeNumber().equals(other.getLikeNumber()))
                && (this.getCommentNumber() == null ? other.getCommentNumber() == null : this.getCommentNumber().equals(other.getCommentNumber()))
                && (this.getIsDelete() == null ? other.getIsDelete() == null : this.getIsDelete().equals(other.getIsDelete()))
                && (this.getDeleteTime() == null ? other.getDeleteTime() == null : this.getDeleteTime().equals(other.getDeleteTime()))
                && (this.getContent() == null ? other.getContent() == null : this.getContent().equals(other.getContent()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getCreatorId() == null) ? 0 : getCreatorId().hashCode());
        result = prime * result + ((getAtUser() == null) ? 0 : getAtUser().hashCode());
        result = prime * result + ((getImg() == null) ? 0 : getImg().hashCode());
        result = prime * result + ((getWatchCount() == null) ? 0 : getWatchCount().hashCode());
        result = prime * result + ((getPrivacy() == null) ? 0 : getPrivacy().hashCode());
        result = prime * result + ((getIsForward() == null) ? 0 : getIsForward().hashCode());
        result = prime * result + ((getOriginType() == null) ? 0 : getOriginType().hashCode());
        result = prime * result + ((getOriginId() == null) ? 0 : getOriginId().hashCode());
        result = prime * result + ((getOriginSayingId() == null) ? 0 : getOriginSayingId().hashCode());
        result = prime * result + ((getForwardNumber() == null) ? 0 : getForwardNumber().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getLikeNumber() == null) ? 0 : getLikeNumber().hashCode());
        result = prime * result + ((getCommentNumber() == null) ? 0 : getCommentNumber().hashCode());
        result = prime * result + ((getIsDelete() == null) ? 0 : getIsDelete().hashCode());
        result = prime * result + ((getDeleteTime() == null) ? 0 : getDeleteTime().hashCode());
        result = prime * result + ((getContent() == null) ? 0 : getContent().hashCode());
        return result;
    }
}