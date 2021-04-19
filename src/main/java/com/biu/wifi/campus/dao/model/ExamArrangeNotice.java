package com.biu.wifi.campus.dao.model;

import java.io.Serializable;
import java.util.Date;

/**
 * biu_exam_arrange_notice
 *
 * @author
 */
public class ExamArrangeNotice implements Serializable {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 排考id
     */
    private Integer examArrangeId;

    /**
     * 是否收到（1是，0否）
     */
    private Integer isReceived;

    /**
     * 确认收到时间
     */
    private Date receivedTime;

    /**
     * 通知标题
     */
    private String title;

    /**
     * 通知内容
     */
    private String content;

    /**
     * 接收人id
     */
    private Integer toUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除（1是2否）
     */
    private Integer isDelete;

    /**
     * 删除时间
     */
    private Date deleteTime;

    /**
     * 是否推送,默认为未推送(1是2否)
     */
    private Integer isPushed;

    /**
     * 推送时间
     */
    private Date pushedTime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getExamArrangeId() {
        return examArrangeId;
    }

    public void setExamArrangeId(Integer examArrangeId) {
        this.examArrangeId = examArrangeId;
    }

    public Integer getIsReceived() {
        return isReceived;
    }

    public void setIsReceived(Integer isReceived) {
        this.isReceived = isReceived;
    }

    public Date getReceivedTime() {
        return receivedTime;
    }

    public void setReceivedTime(Date receivedTime) {
        this.receivedTime = receivedTime;
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

    public Integer getToUser() {
        return toUser;
    }

    public void setToUser(Integer toUser) {
        this.toUser = toUser;
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

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public Date getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Date deleteTime) {
        this.deleteTime = deleteTime;
    }

    public Integer getIsPushed() {
        return isPushed;
    }

    public void setIsPushed(Integer isPushed) {
        this.isPushed = isPushed;
    }

    public Date getPushedTime() {
        return pushedTime;
    }

    public void setPushedTime(Date pushedTime) {
        this.pushedTime = pushedTime;
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
        ExamArrangeNotice other = (ExamArrangeNotice) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getExamArrangeId() == null ? other.getExamArrangeId() == null : this.getExamArrangeId().equals(other.getExamArrangeId()))
                && (this.getIsReceived() == null ? other.getIsReceived() == null : this.getIsReceived().equals(other.getIsReceived()))
                && (this.getReceivedTime() == null ? other.getReceivedTime() == null : this.getReceivedTime().equals(other.getReceivedTime()))
                && (this.getTitle() == null ? other.getTitle() == null : this.getTitle().equals(other.getTitle()))
                && (this.getContent() == null ? other.getContent() == null : this.getContent().equals(other.getContent()))
                && (this.getToUser() == null ? other.getToUser() == null : this.getToUser().equals(other.getToUser()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
                && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
                && (this.getIsDelete() == null ? other.getIsDelete() == null : this.getIsDelete().equals(other.getIsDelete()))
                && (this.getDeleteTime() == null ? other.getDeleteTime() == null : this.getDeleteTime().equals(other.getDeleteTime()))
                && (this.getIsPushed() == null ? other.getIsPushed() == null : this.getIsPushed().equals(other.getIsPushed()))
                && (this.getPushedTime() == null ? other.getPushedTime() == null : this.getPushedTime().equals(other.getPushedTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getExamArrangeId() == null) ? 0 : getExamArrangeId().hashCode());
        result = prime * result + ((getIsReceived() == null) ? 0 : getIsReceived().hashCode());
        result = prime * result + ((getReceivedTime() == null) ? 0 : getReceivedTime().hashCode());
        result = prime * result + ((getTitle() == null) ? 0 : getTitle().hashCode());
        result = prime * result + ((getContent() == null) ? 0 : getContent().hashCode());
        result = prime * result + ((getToUser() == null) ? 0 : getToUser().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getIsDelete() == null) ? 0 : getIsDelete().hashCode());
        result = prime * result + ((getDeleteTime() == null) ? 0 : getDeleteTime().hashCode());
        result = prime * result + ((getIsPushed() == null) ? 0 : getIsPushed().hashCode());
        result = prime * result + ((getPushedTime() == null) ? 0 : getPushedTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", examArrangeId=").append(examArrangeId);
        sb.append(", isReceived=").append(isReceived);
        sb.append(", receivedTime=").append(receivedTime);
        sb.append(", title=").append(title);
        sb.append(", content=").append(content);
        sb.append(", toUser=").append(toUser);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", deleteTime=").append(deleteTime);
        sb.append(", isPushed=").append(isPushed);
        sb.append(", pushedTime=").append(pushedTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}