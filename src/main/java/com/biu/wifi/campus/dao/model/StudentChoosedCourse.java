package com.biu.wifi.campus.dao.model;

import java.io.Serializable;
import java.util.Date;

/**
 * biu_student_choosed_course
 * @author 
 */
public class StudentChoosedCourse implements Serializable {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 学年学期
     */
    private String termCode;

    /**
     * 学校id
     */
    private Integer schoolId;

    /**
     * 课程号
     */
    private String courseNo;

    /**
     * 课序号
     */
    private String courseSerialNo;

    /**
     * 学号
     */
    private String stuNumber;

    /**
     * 方案计划号
     */
    private String planNo;

    /**
     * 修读方式代码(01-，02-，03-，04-，05-，06-，07-，08-，09-)
     */
    private String studyType;

    /**
     * 课程属性代码(001-，002-，003-)
     */
    private String courseAttr;

    /**
     * 考试类型代码(01-，02-，03-，04-，05-，06-)
     */
    private String examType;

    /**
     * 选课状态(001-，011-，016-)
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除(1-是，2-否)
     */
    private Integer isDelete;

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

    public String getTermCode() {
        return termCode;
    }

    public void setTermCode(String termCode) {
        this.termCode = termCode;
    }

    public Integer getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
    }

    public String getCourseNo() {
        return courseNo;
    }

    public void setCourseNo(String courseNo) {
        this.courseNo = courseNo;
    }

    public String getCourseSerialNo() {
        return courseSerialNo;
    }

    public void setCourseSerialNo(String courseSerialNo) {
        this.courseSerialNo = courseSerialNo;
    }

    public String getStuNumber() {
        return stuNumber;
    }

    public void setStuNumber(String stuNumber) {
        this.stuNumber = stuNumber;
    }

    public String getPlanNo() {
        return planNo;
    }

    public void setPlanNo(String planNo) {
        this.planNo = planNo;
    }

    public String getStudyType() {
        return studyType;
    }

    public void setStudyType(String studyType) {
        this.studyType = studyType;
    }

    public String getCourseAttr() {
        return courseAttr;
    }

    public void setCourseAttr(String courseAttr) {
        this.courseAttr = courseAttr;
    }

    public String getExamType() {
        return examType;
    }

    public void setExamType(String examType) {
        this.examType = examType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
        StudentChoosedCourse other = (StudentChoosedCourse) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getTermCode() == null ? other.getTermCode() == null : this.getTermCode().equals(other.getTermCode()))
            && (this.getSchoolId() == null ? other.getSchoolId() == null : this.getSchoolId().equals(other.getSchoolId()))
            && (this.getCourseNo() == null ? other.getCourseNo() == null : this.getCourseNo().equals(other.getCourseNo()))
            && (this.getCourseSerialNo() == null ? other.getCourseSerialNo() == null : this.getCourseSerialNo().equals(other.getCourseSerialNo()))
            && (this.getStuNumber() == null ? other.getStuNumber() == null : this.getStuNumber().equals(other.getStuNumber()))
            && (this.getPlanNo() == null ? other.getPlanNo() == null : this.getPlanNo().equals(other.getPlanNo()))
            && (this.getStudyType() == null ? other.getStudyType() == null : this.getStudyType().equals(other.getStudyType()))
            && (this.getCourseAttr() == null ? other.getCourseAttr() == null : this.getCourseAttr().equals(other.getCourseAttr()))
            && (this.getExamType() == null ? other.getExamType() == null : this.getExamType().equals(other.getExamType()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()))
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
        result = prime * result + ((getTermCode() == null) ? 0 : getTermCode().hashCode());
        result = prime * result + ((getSchoolId() == null) ? 0 : getSchoolId().hashCode());
        result = prime * result + ((getCourseNo() == null) ? 0 : getCourseNo().hashCode());
        result = prime * result + ((getCourseSerialNo() == null) ? 0 : getCourseSerialNo().hashCode());
        result = prime * result + ((getStuNumber() == null) ? 0 : getStuNumber().hashCode());
        result = prime * result + ((getPlanNo() == null) ? 0 : getPlanNo().hashCode());
        result = prime * result + ((getStudyType() == null) ? 0 : getStudyType().hashCode());
        result = prime * result + ((getCourseAttr() == null) ? 0 : getCourseAttr().hashCode());
        result = prime * result + ((getExamType() == null) ? 0 : getExamType().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
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
        sb.append(", termCode=").append(termCode);
        sb.append(", schoolId=").append(schoolId);
        sb.append(", courseNo=").append(courseNo);
        sb.append(", courseSerialNo=").append(courseSerialNo);
        sb.append(", stuNumber=").append(stuNumber);
        sb.append(", planNo=").append(planNo);
        sb.append(", studyType=").append(studyType);
        sb.append(", courseAttr=").append(courseAttr);
        sb.append(", examType=").append(examType);
        sb.append(", status=").append(status);
        sb.append(", remark=").append(remark);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", deleteTime=").append(deleteTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}