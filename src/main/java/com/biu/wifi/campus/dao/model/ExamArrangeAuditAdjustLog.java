package com.biu.wifi.campus.dao.model;

import java.io.Serializable;
import java.util.Date;

/**
 * biu_exam_arrange_audit_adjust_log
 * @author 
 */
public class ExamArrangeAuditAdjustLog implements Serializable {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 排考id
     */
    private Integer examArrangeId;

    /**
     * 类型(1-调整教室，2-调整考试时间，3-调整监考人员，4调整巡考人员)
     */
    private Integer type;

    /**
     * 调整前的业务内容
     */
    private String oldContent;

    /**
     * 业务内容
     */
    private String content;

    /**
     * 调整用户id
     */
    private Integer adjustUserId;

    /**
     * 调整时间
     */
    private Date adjustTime;

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getOldContent() {
        return oldContent;
    }

    public void setOldContent(String oldContent) {
        this.oldContent = oldContent;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getAdjustUserId() {
        return adjustUserId;
    }

    public void setAdjustUserId(Integer adjustUserId) {
        this.adjustUserId = adjustUserId;
    }

    public Date getAdjustTime() {
        return adjustTime;
    }

    public void setAdjustTime(Date adjustTime) {
        this.adjustTime = adjustTime;
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
        ExamArrangeAuditAdjustLog other = (ExamArrangeAuditAdjustLog) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getExamArrangeId() == null ? other.getExamArrangeId() == null : this.getExamArrangeId().equals(other.getExamArrangeId()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getOldContent() == null ? other.getOldContent() == null : this.getOldContent().equals(other.getOldContent()))
            && (this.getContent() == null ? other.getContent() == null : this.getContent().equals(other.getContent()))
            && (this.getAdjustUserId() == null ? other.getAdjustUserId() == null : this.getAdjustUserId().equals(other.getAdjustUserId()))
            && (this.getAdjustTime() == null ? other.getAdjustTime() == null : this.getAdjustTime().equals(other.getAdjustTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getExamArrangeId() == null) ? 0 : getExamArrangeId().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getOldContent() == null) ? 0 : getOldContent().hashCode());
        result = prime * result + ((getContent() == null) ? 0 : getContent().hashCode());
        result = prime * result + ((getAdjustUserId() == null) ? 0 : getAdjustUserId().hashCode());
        result = prime * result + ((getAdjustTime() == null) ? 0 : getAdjustTime().hashCode());
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
        sb.append(", type=").append(type);
        sb.append(", oldContent=").append(oldContent);
        sb.append(", content=").append(content);
        sb.append(", adjustUserId=").append(adjustUserId);
        sb.append(", adjustTime=").append(adjustTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}