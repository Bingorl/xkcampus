package com.biu.wifi.campus.dao.model;

import java.io.Serializable;
import java.util.Date;

/**
 * biu_teaching_week
 *
 * @author
 */
public class TeachingWeek implements Serializable {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 学校ID
     */
    private Integer schoolId;

    /**
     * 学期学年代码
     */
    private String termCode;

    /**
     * 学期学年名称
     */
    private String termName;

    /**
     * 课程教学周期(2018-09-03~2019-01-24)
     */
    private String termPeriod;

    /**
     * 周数
     */
    private Integer weekCount;

    /**
     * 每周周一的日期(2018-09-03,2018-09-10)
     */
    private String mondayDate;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

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

    public String getTermCode() {
        return termCode;
    }

    public void setTermCode(String termCode) {
        this.termCode = termCode;
    }

    public String getTermName() {
        return termName;
    }

    public void setTermName(String termName) {
        this.termName = termName;
    }

    public String getTermPeriod() {
        return termPeriod;
    }

    public void setTermPeriod(String termPeriod) {
        this.termPeriod = termPeriod;
    }

    public Integer getWeekCount() {
        return weekCount;
    }

    public void setWeekCount(Integer weekCount) {
        this.weekCount = weekCount;
    }

    public String getMondayDate() {
        return mondayDate;
    }

    public void setMondayDate(String mondayDate) {
        this.mondayDate = mondayDate;
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
        TeachingWeek other = (TeachingWeek) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getSchoolId() == null ? other.getSchoolId() == null : this.getSchoolId().equals(other.getSchoolId()))
                && (this.getTermCode() == null ? other.getTermCode() == null : this.getTermCode().equals(other.getTermCode()))
                && (this.getTermName() == null ? other.getTermName() == null : this.getTermName().equals(other.getTermName()))
                && (this.getTermPeriod() == null ? other.getTermPeriod() == null : this.getTermPeriod().equals(other.getTermPeriod()))
                && (this.getWeekCount() == null ? other.getWeekCount() == null : this.getWeekCount().equals(other.getWeekCount()))
                && (this.getMondayDate() == null ? other.getMondayDate() == null : this.getMondayDate().equals(other.getMondayDate()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
                && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getSchoolId() == null) ? 0 : getSchoolId().hashCode());
        result = prime * result + ((getTermCode() == null) ? 0 : getTermCode().hashCode());
        result = prime * result + ((getTermName() == null) ? 0 : getTermName().hashCode());
        result = prime * result + ((getTermPeriod() == null) ? 0 : getTermPeriod().hashCode());
        result = prime * result + ((getWeekCount() == null) ? 0 : getWeekCount().hashCode());
        result = prime * result + ((getMondayDate() == null) ? 0 : getMondayDate().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
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
        sb.append(", termCode=").append(termCode);
        sb.append(", termName=").append(termName);
        sb.append(", termPeriod=").append(termPeriod);
        sb.append(", weekCount=").append(weekCount);
        sb.append(", mondayDate=").append(mondayDate);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}