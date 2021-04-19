package com.biu.wifi.campus.dao.model;

import java.io.Serializable;
import java.util.Date;

/**
 * biu_user_stu_no_invalid
 *
 * @author
 */
public class UserStuNoInvalid implements Serializable {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 学校ID
     */
    private Integer schoolId;

    /**
     * 学校名称
     */
    private String schoolName;

    /**
     * 学号
     */
    private String stuNo;

    /**
     * 学生姓名
     */
    private String username;

    /**
     * 是否是教职工(1是2否)
     */
    private Short isTeacher;

    /**
     * 推送设备类型 1 ios  2 android
     */
    private Short devType;

    /**
     * 推送设备token
     */
    private String devToken;

    /**
     * 创建时间
     */
    private Date createTime;

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

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getStuNo() {
        return stuNo;
    }

    public void setStuNo(String stuNo) {
        this.stuNo = stuNo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Short getIsTeacher() {
        return isTeacher;
    }

    public void setIsTeacher(Short isTeacher) {
        this.isTeacher = isTeacher;
    }

    public Short getDevType() {
        return devType;
    }

    public void setDevType(Short devType) {
        this.devType = devType;
    }

    public String getDevToken() {
        return devToken;
    }

    public void setDevToken(String devToken) {
        this.devToken = devToken;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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
        UserStuNoInvalid other = (UserStuNoInvalid) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getSchoolId() == null ? other.getSchoolId() == null : this.getSchoolId().equals(other.getSchoolId()))
                && (this.getSchoolName() == null ? other.getSchoolName() == null : this.getSchoolName().equals(other.getSchoolName()))
                && (this.getStuNo() == null ? other.getStuNo() == null : this.getStuNo().equals(other.getStuNo()))
                && (this.getUsername() == null ? other.getUsername() == null : this.getUsername().equals(other.getUsername()))
                && (this.getIsTeacher() == null ? other.getIsTeacher() == null : this.getIsTeacher().equals(other.getIsTeacher()))
                && (this.getDevType() == null ? other.getDevType() == null : this.getDevType().equals(other.getDevType()))
                && (this.getDevToken() == null ? other.getDevToken() == null : this.getDevToken().equals(other.getDevToken()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getSchoolId() == null) ? 0 : getSchoolId().hashCode());
        result = prime * result + ((getSchoolName() == null) ? 0 : getSchoolName().hashCode());
        result = prime * result + ((getStuNo() == null) ? 0 : getStuNo().hashCode());
        result = prime * result + ((getUsername() == null) ? 0 : getUsername().hashCode());
        result = prime * result + ((getIsTeacher() == null) ? 0 : getIsTeacher().hashCode());
        result = prime * result + ((getDevType() == null) ? 0 : getDevType().hashCode());
        result = prime * result + ((getDevToken() == null) ? 0 : getDevToken().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
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
        sb.append(", schoolName=").append(schoolName);
        sb.append(", stuNo=").append(stuNo);
        sb.append(", username=").append(username);
        sb.append(", isTeacher=").append(isTeacher);
        sb.append(", devType=").append(devType);
        sb.append(", devToken=").append(devToken);
        sb.append(", createTime=").append(createTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}