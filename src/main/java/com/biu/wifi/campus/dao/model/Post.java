package com.biu.wifi.campus.dao.model;

import com.biu.wifi.core.base.CoreEntity;

import java.util.Date;

public class Post extends CoreEntity {
    private Integer id;

    private Integer schoolId;

    private String title;

    private String atUser;

    private String img;

    private Integer creatorId;

    private Date createTime;

    private Short postType;

    private Short isSelect;

    private Integer weight;

    private Integer selectTypeId;

    private Date selectTime;

    private Date modifyTime;

    private Integer likeNumber;

    private Date noticeTopTime;

    private Integer hot;

    private Integer commentNumber;

    private Date newestCmtTime;

    private Short isDelete;

    private Date deleteTime;

    private String content;

    private Integer watchCount;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
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

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Short getPostType() {
        return postType;
    }

    public void setPostType(Short postType) {
        this.postType = postType;
    }

    public Short getIsSelect() {
        return isSelect;
    }

    public void setIsSelect(Short isSelect) {
        this.isSelect = isSelect;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getSelectTypeId() {
        return selectTypeId;
    }

    public void setSelectTypeId(Integer selectTypeId) {
        this.selectTypeId = selectTypeId;
    }

    public Date getSelectTime() {
        return selectTime;
    }

    public void setSelectTime(Date selectTime) {
        this.selectTime = selectTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Integer getLikeNumber() {
        return likeNumber;
    }

    public void setLikeNumber(Integer likeNumber) {
        this.likeNumber = likeNumber;
    }

    public Date getNoticeTopTime() {
        return noticeTopTime;
    }

    public void setNoticeTopTime(Date noticeTopTime) {
        this.noticeTopTime = noticeTopTime;
    }

    public Integer getHot() {
        return hot;
    }

    public void setHot(Integer hot) {
        this.hot = hot;
    }

    public Integer getCommentNumber() {
        return commentNumber;
    }

    public void setCommentNumber(Integer commentNumber) {
        this.commentNumber = commentNumber;
    }

    public Date getNewestCmtTime() {
        return newestCmtTime;
    }

    public void setNewestCmtTime(Date newestCmtTime) {
        this.newestCmtTime = newestCmtTime;
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

    public Integer getWatchCount() {
        return watchCount;
    }

    public void setWatchCount(Integer watchCount) {
        this.watchCount = watchCount;
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
        Post other = (Post) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getSchoolId() == null ? other.getSchoolId() == null : this.getSchoolId().equals(other.getSchoolId()))
                && (this.getTitle() == null ? other.getTitle() == null : this.getTitle().equals(other.getTitle()))
                && (this.getAtUser() == null ? other.getAtUser() == null : this.getAtUser().equals(other.getAtUser()))
                && (this.getImg() == null ? other.getImg() == null : this.getImg().equals(other.getImg()))
                && (this.getCreatorId() == null ? other.getCreatorId() == null : this.getCreatorId().equals(other.getCreatorId()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
                && (this.getPostType() == null ? other.getPostType() == null : this.getPostType().equals(other.getPostType()))
                && (this.getIsSelect() == null ? other.getIsSelect() == null : this.getIsSelect().equals(other.getIsSelect()))
                && (this.getWeight() == null ? other.getWeight() == null : this.getWeight().equals(other.getWeight()))
                && (this.getSelectTypeId() == null ? other.getSelectTypeId() == null : this.getSelectTypeId().equals(other.getSelectTypeId()))
                && (this.getSelectTime() == null ? other.getSelectTime() == null : this.getSelectTime().equals(other.getSelectTime()))
                && (this.getModifyTime() == null ? other.getModifyTime() == null : this.getModifyTime().equals(other.getModifyTime()))
                && (this.getLikeNumber() == null ? other.getLikeNumber() == null : this.getLikeNumber().equals(other.getLikeNumber()))
                && (this.getNoticeTopTime() == null ? other.getNoticeTopTime() == null : this.getNoticeTopTime().equals(other.getNoticeTopTime()))
                && (this.getHot() == null ? other.getHot() == null : this.getHot().equals(other.getHot()))
                && (this.getCommentNumber() == null ? other.getCommentNumber() == null : this.getCommentNumber().equals(other.getCommentNumber()))
                && (this.getNewestCmtTime() == null ? other.getNewestCmtTime() == null : this.getNewestCmtTime().equals(other.getNewestCmtTime()))
                && (this.getIsDelete() == null ? other.getIsDelete() == null : this.getIsDelete().equals(other.getIsDelete()))
                && (this.getDeleteTime() == null ? other.getDeleteTime() == null : this.getDeleteTime().equals(other.getDeleteTime()))
                && (this.getContent() == null ? other.getContent() == null : this.getContent().equals(other.getContent()))
                && (this.getWatchCount() == null ? other.getWatchCount() == null : this.getWatchCount().equals(other.getWatchCount()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getSchoolId() == null) ? 0 : getSchoolId().hashCode());
        result = prime * result + ((getTitle() == null) ? 0 : getTitle().hashCode());
        result = prime * result + ((getAtUser() == null) ? 0 : getAtUser().hashCode());
        result = prime * result + ((getImg() == null) ? 0 : getImg().hashCode());
        result = prime * result + ((getCreatorId() == null) ? 0 : getCreatorId().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getPostType() == null) ? 0 : getPostType().hashCode());
        result = prime * result + ((getIsSelect() == null) ? 0 : getIsSelect().hashCode());
        result = prime * result + ((getWeight() == null) ? 0 : getWeight().hashCode());
        result = prime * result + ((getSelectTypeId() == null) ? 0 : getSelectTypeId().hashCode());
        result = prime * result + ((getSelectTime() == null) ? 0 : getSelectTime().hashCode());
        result = prime * result + ((getModifyTime() == null) ? 0 : getModifyTime().hashCode());
        result = prime * result + ((getLikeNumber() == null) ? 0 : getLikeNumber().hashCode());
        result = prime * result + ((getNoticeTopTime() == null) ? 0 : getNoticeTopTime().hashCode());
        result = prime * result + ((getHot() == null) ? 0 : getHot().hashCode());
        result = prime * result + ((getCommentNumber() == null) ? 0 : getCommentNumber().hashCode());
        result = prime * result + ((getNewestCmtTime() == null) ? 0 : getNewestCmtTime().hashCode());
        result = prime * result + ((getIsDelete() == null) ? 0 : getIsDelete().hashCode());
        result = prime * result + ((getDeleteTime() == null) ? 0 : getDeleteTime().hashCode());
        result = prime * result + ((getContent() == null) ? 0 : getContent().hashCode());
        result = prime * result + ((getWatchCount() == null) ? 0 : getWatchCount().hashCode());
        return result;
    }
}