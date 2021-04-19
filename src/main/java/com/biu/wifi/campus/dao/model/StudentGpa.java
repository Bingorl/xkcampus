package com.biu.wifi.campus.dao.model;

import java.io.Serializable;
import java.util.Date;

/**
 * biu_student_gpa
 *
 * @author
 */
public class StudentGpa implements Serializable {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 学校ID
     */
    private Integer schoolId;

    /**
     * 学号
     */
    private String stuNo;

    /**
     * 推优绩点
     */
    private Double gpa;

    /**
     * 推优专业排名
     */
    private Integer majorRanking;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
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

    public String getStuNo() {
        return stuNo;
    }

    public void setStuNo(String stuNo) {
        this.stuNo = stuNo;
    }

    public Double getGpa() {
        return gpa;
    }

    public void setGpa(Double gpa) {
        this.gpa = gpa;
    }

    public Integer getMajorRanking() {
        return majorRanking;
    }

    public void setMajorRanking(Integer majorRanking) {
        this.majorRanking = majorRanking;
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
        StudentGpa other = (StudentGpa) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getSchoolId() == null ? other.getSchoolId() == null : this.getSchoolId().equals(other.getSchoolId()))
                && (this.getStuNo() == null ? other.getStuNo() == null : this.getStuNo().equals(other.getStuNo()))
                && (this.getGpa() == null ? other.getGpa() == null : this.getGpa().equals(other.getGpa()))
                && (this.getMajorRanking() == null ? other.getMajorRanking() == null : this.getMajorRanking().equals(other.getMajorRanking()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
                && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getSchoolId() == null) ? 0 : getSchoolId().hashCode());
        result = prime * result + ((getStuNo() == null) ? 0 : getStuNo().hashCode());
        result = prime * result + ((getGpa() == null) ? 0 : getGpa().hashCode());
        result = prime * result + ((getMajorRanking() == null) ? 0 : getMajorRanking().hashCode());
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
        sb.append(", stuNo=").append(stuNo);
        sb.append(", gpa=").append(gpa);
        sb.append(", majorRanking=").append(majorRanking);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}