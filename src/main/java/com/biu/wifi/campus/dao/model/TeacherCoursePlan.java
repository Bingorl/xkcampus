package com.biu.wifi.campus.dao.model;

import java.io.Serializable;
import java.util.Date;

/**
 * biu_teacher_course_plan
 *
 * @author
 */
public class TeacherCoursePlan implements Serializable {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 学年学期编码
     */
    private String termCode;

    /**
     * 学校ID
     */
    private Integer schoolId;

    /**
     * 教学楼ID
     */
    private Integer buildingId;

    /**
     * 教室号
     */
    private String classroomNo;

    /**
     * 课程名
     */
    private String courseName;

    /**
     * 课程号
     */
    private String courseNo;

    /**
     * 课序号
     */
    private String courseSerialNo;

    /**
     * 上课周次
     */
    private String courseWeek;

    /**
     * 上课星期
     */
    private String courseWeekDay;

    /**
     * 上课节次
     */
    private String courseSection;

    /**
     * 持续节次
     */
    private String courseContinueSection;

    /**
     * 教师号
     */
    private String teacherNo;

    /**
     * 教师名
     */
    private String teacherName;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除（1是，2否）
     */
    private Short isDelete;

    /**
     * 删除时间
     */
    private Date deleteTime;

    /**
     * 备注
     */
    private String remark;

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

    public Integer getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Integer buildingId) {
        this.buildingId = buildingId;
    }

    public String getClassroomNo() {
        return classroomNo;
    }

    public void setClassroomNo(String classroomNo) {
        this.classroomNo = classroomNo;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
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

    public String getCourseWeek() {
        return courseWeek;
    }

    public void setCourseWeek(String courseWeek) {
        this.courseWeek = courseWeek;
    }

    public String getCourseWeekDay() {
        return courseWeekDay;
    }

    public void setCourseWeekDay(String courseWeekDay) {
        this.courseWeekDay = courseWeekDay;
    }

    public String getCourseSection() {
        return courseSection;
    }

    public void setCourseSection(String courseSection) {
        this.courseSection = courseSection;
    }

    public String getCourseContinueSection() {
        return courseContinueSection;
    }

    public void setCourseContinueSection(String courseContinueSection) {
        this.courseContinueSection = courseContinueSection;
    }

    public String getTeacherNo() {
        return teacherNo;
    }

    public void setTeacherNo(String teacherNo) {
        this.teacherNo = teacherNo;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
        TeacherCoursePlan other = (TeacherCoursePlan) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getTermCode() == null ? other.getTermCode() == null : this.getTermCode().equals(other.getTermCode()))
                && (this.getSchoolId() == null ? other.getSchoolId() == null : this.getSchoolId().equals(other.getSchoolId()))
                && (this.getBuildingId() == null ? other.getBuildingId() == null : this.getBuildingId().equals(other.getBuildingId()))
                && (this.getClassroomNo() == null ? other.getClassroomNo() == null : this.getClassroomNo().equals(other.getClassroomNo()))
                && (this.getCourseName() == null ? other.getCourseName() == null : this.getCourseName().equals(other.getCourseName()))
                && (this.getCourseNo() == null ? other.getCourseNo() == null : this.getCourseNo().equals(other.getCourseNo()))
                && (this.getCourseSerialNo() == null ? other.getCourseSerialNo() == null : this.getCourseSerialNo().equals(other.getCourseSerialNo()))
                && (this.getCourseWeek() == null ? other.getCourseWeek() == null : this.getCourseWeek().equals(other.getCourseWeek()))
                && (this.getCourseWeekDay() == null ? other.getCourseWeekDay() == null : this.getCourseWeekDay().equals(other.getCourseWeekDay()))
                && (this.getCourseSection() == null ? other.getCourseSection() == null : this.getCourseSection().equals(other.getCourseSection()))
                && (this.getCourseContinueSection() == null ? other.getCourseContinueSection() == null : this.getCourseContinueSection().equals(other.getCourseContinueSection()))
                && (this.getTeacherNo() == null ? other.getTeacherNo() == null : this.getTeacherNo().equals(other.getTeacherNo()))
                && (this.getTeacherName() == null ? other.getTeacherName() == null : this.getTeacherName().equals(other.getTeacherName()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
                && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
                && (this.getIsDelete() == null ? other.getIsDelete() == null : this.getIsDelete().equals(other.getIsDelete()))
                && (this.getDeleteTime() == null ? other.getDeleteTime() == null : this.getDeleteTime().equals(other.getDeleteTime()))
                && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getTermCode() == null) ? 0 : getTermCode().hashCode());
        result = prime * result + ((getSchoolId() == null) ? 0 : getSchoolId().hashCode());
        result = prime * result + ((getBuildingId() == null) ? 0 : getBuildingId().hashCode());
        result = prime * result + ((getClassroomNo() == null) ? 0 : getClassroomNo().hashCode());
        result = prime * result + ((getCourseName() == null) ? 0 : getCourseName().hashCode());
        result = prime * result + ((getCourseNo() == null) ? 0 : getCourseNo().hashCode());
        result = prime * result + ((getCourseSerialNo() == null) ? 0 : getCourseSerialNo().hashCode());
        result = prime * result + ((getCourseWeek() == null) ? 0 : getCourseWeek().hashCode());
        result = prime * result + ((getCourseWeekDay() == null) ? 0 : getCourseWeekDay().hashCode());
        result = prime * result + ((getCourseSection() == null) ? 0 : getCourseSection().hashCode());
        result = prime * result + ((getCourseContinueSection() == null) ? 0 : getCourseContinueSection().hashCode());
        result = prime * result + ((getTeacherNo() == null) ? 0 : getTeacherNo().hashCode());
        result = prime * result + ((getTeacherName() == null) ? 0 : getTeacherName().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getIsDelete() == null) ? 0 : getIsDelete().hashCode());
        result = prime * result + ((getDeleteTime() == null) ? 0 : getDeleteTime().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
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
        sb.append(", buildingId=").append(buildingId);
        sb.append(", classroomNo=").append(classroomNo);
        sb.append(", courseName=").append(courseName);
        sb.append(", courseNo=").append(courseNo);
        sb.append(", courseSerialNo=").append(courseSerialNo);
        sb.append(", courseWeek=").append(courseWeek);
        sb.append(", courseWeekDay=").append(courseWeekDay);
        sb.append(", courseSection=").append(courseSection);
        sb.append(", courseContinueSection=").append(courseContinueSection);
        sb.append(", teacherNo=").append(teacherNo);
        sb.append(", teacherName=").append(teacherName);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", deleteTime=").append(deleteTime);
        sb.append(", remark=").append(remark);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}