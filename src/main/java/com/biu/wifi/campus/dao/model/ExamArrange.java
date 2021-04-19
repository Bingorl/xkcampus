package com.biu.wifi.campus.dao.model;

import java.io.Serializable;
import java.util.Date;

/**
 * biu_exam_arrange
 * @author 
 */
public class ExamArrange implements Serializable {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 学校id
     */
    private Integer schoolId;

    /**
     * 学年学期代码
     */
    private String termCode;

    /**
     * 课程号
     */
    private String courseNo;

    /**
     * 课序号
     */
    private String courseSerialNo;

    /**
     * 课程名
     */
    private String courseName;

    /**
     * 授课老师名称
     */
    private String teacherName;

    /**
     * 授课老师工号
     */
    private String teacherNo;

    /**
     * 班级号，逗号分隔
     */
    private String classNo;

    /**
     * 选课学生id，逗号分隔
     */
    private String studentId;

    /**
     * 授课教室号，逗号分隔
     */
    private String classroomNo;

    /**
     * 调整前的授课教室号，逗号分隔
     */
    private String oldClassroomNo;

    /**
     * 结课时间
     */
    private Date courseEndTime;

    /**
     * 结课时间所在周次
     */
    private Integer courseEndTimeWeekIndex;

    /**
     * 结课时间所在星期名
     */
    private String courseEndTimeWeekName;

    /**
     * 结课时间所在节次
     */
    private String courseEndTimeSection;

    /**
     * 监考人员id，逗号分隔
     */
    private String invigilateTeacherId;

    /**
     * 巡考人员id，逗号分隔
     */
    private String patrolTeacherId;

    /**
     * 考试时间
     */
    private Date examTime;

    /**
     * 考试时间所在周次
     */
    private Integer examTimeWeekIndex;

    /**
     * 考试时间的星期名
     */
    private String examTimeWeekName;

    /**
     * 考试时间所在的节次，逗号分隔
     */
    private String examTimeSection;

    /**
     * 考试时间的开始时间，逗号分隔
     */
    private String examTimePeriod;

    /**
     * 调整前的考试时间
     */
    private Date oldExamTime;

    /**
     * 调整前的考试时间所在周次
     */
    private Integer oldExamTimeWeekIndex;

    /**
     * 调整前的考试时间所在星期名
     */
    private String oldExamTimeWeekName;

    /**
     * 调整前的考试时间所在的节次，逗号分隔
     */
    private String oldExamTimeSection;

    /**
     * 状态(1待审核，2驳回，3添加监考完成，4添加巡考完成，5通过)
     */
    private Integer status;

    /**
     * 附件，逗号分隔
     */
    private String attachment;

    /**
     * 最终审核人id
     */
    private Integer auditUserId;

    /**
     * 审核时间
     */
    private Date auditTime;

    /**
     * 审核意见
     */
    private String auditRemark;

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

    public String getTermCode() {
        return termCode;
    }

    public void setTermCode(String termCode) {
        this.termCode = termCode;
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

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTeacherNo() {
        return teacherNo;
    }

    public void setTeacherNo(String teacherNo) {
        this.teacherNo = teacherNo;
    }

    public String getClassNo() {
        return classNo;
    }

    public void setClassNo(String classNo) {
        this.classNo = classNo;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getClassroomNo() {
        return classroomNo;
    }

    public void setClassroomNo(String classroomNo) {
        this.classroomNo = classroomNo;
    }

    public String getOldClassroomNo() {
        return oldClassroomNo;
    }

    public void setOldClassroomNo(String oldClassroomNo) {
        this.oldClassroomNo = oldClassroomNo;
    }

    public Date getCourseEndTime() {
        return courseEndTime;
    }

    public void setCourseEndTime(Date courseEndTime) {
        this.courseEndTime = courseEndTime;
    }

    public Integer getCourseEndTimeWeekIndex() {
        return courseEndTimeWeekIndex;
    }

    public void setCourseEndTimeWeekIndex(Integer courseEndTimeWeekIndex) {
        this.courseEndTimeWeekIndex = courseEndTimeWeekIndex;
    }

    public String getCourseEndTimeWeekName() {
        return courseEndTimeWeekName;
    }

    public void setCourseEndTimeWeekName(String courseEndTimeWeekName) {
        this.courseEndTimeWeekName = courseEndTimeWeekName;
    }

    public String getCourseEndTimeSection() {
        return courseEndTimeSection;
    }

    public void setCourseEndTimeSection(String courseEndTimeSection) {
        this.courseEndTimeSection = courseEndTimeSection;
    }

    public String getInvigilateTeacherId() {
        return invigilateTeacherId;
    }

    public void setInvigilateTeacherId(String invigilateTeacherId) {
        this.invigilateTeacherId = invigilateTeacherId;
    }

    public String getPatrolTeacherId() {
        return patrolTeacherId;
    }

    public void setPatrolTeacherId(String patrolTeacherId) {
        this.patrolTeacherId = patrolTeacherId;
    }

    public Date getExamTime() {
        return examTime;
    }

    public void setExamTime(Date examTime) {
        this.examTime = examTime;
    }

    public Integer getExamTimeWeekIndex() {
        return examTimeWeekIndex;
    }

    public void setExamTimeWeekIndex(Integer examTimeWeekIndex) {
        this.examTimeWeekIndex = examTimeWeekIndex;
    }

    public String getExamTimeWeekName() {
        return examTimeWeekName;
    }

    public void setExamTimeWeekName(String examTimeWeekName) {
        this.examTimeWeekName = examTimeWeekName;
    }

    public String getExamTimeSection() {
        return examTimeSection;
    }

    public void setExamTimeSection(String examTimeSection) {
        this.examTimeSection = examTimeSection;
    }

    public String getExamTimePeriod() {
        return examTimePeriod;
    }

    public void setExamTimePeriod(String examTimePeriod) {
        this.examTimePeriod = examTimePeriod;
    }

    public Date getOldExamTime() {
        return oldExamTime;
    }

    public void setOldExamTime(Date oldExamTime) {
        this.oldExamTime = oldExamTime;
    }

    public Integer getOldExamTimeWeekIndex() {
        return oldExamTimeWeekIndex;
    }

    public void setOldExamTimeWeekIndex(Integer oldExamTimeWeekIndex) {
        this.oldExamTimeWeekIndex = oldExamTimeWeekIndex;
    }

    public String getOldExamTimeWeekName() {
        return oldExamTimeWeekName;
    }

    public void setOldExamTimeWeekName(String oldExamTimeWeekName) {
        this.oldExamTimeWeekName = oldExamTimeWeekName;
    }

    public String getOldExamTimeSection() {
        return oldExamTimeSection;
    }

    public void setOldExamTimeSection(String oldExamTimeSection) {
        this.oldExamTimeSection = oldExamTimeSection;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public Integer getAuditUserId() {
        return auditUserId;
    }

    public void setAuditUserId(Integer auditUserId) {
        this.auditUserId = auditUserId;
    }

    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    public String getAuditRemark() {
        return auditRemark;
    }

    public void setAuditRemark(String auditRemark) {
        this.auditRemark = auditRemark;
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
        ExamArrange other = (ExamArrange) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getSchoolId() == null ? other.getSchoolId() == null : this.getSchoolId().equals(other.getSchoolId()))
            && (this.getTermCode() == null ? other.getTermCode() == null : this.getTermCode().equals(other.getTermCode()))
            && (this.getCourseNo() == null ? other.getCourseNo() == null : this.getCourseNo().equals(other.getCourseNo()))
            && (this.getCourseSerialNo() == null ? other.getCourseSerialNo() == null : this.getCourseSerialNo().equals(other.getCourseSerialNo()))
            && (this.getCourseName() == null ? other.getCourseName() == null : this.getCourseName().equals(other.getCourseName()))
            && (this.getTeacherName() == null ? other.getTeacherName() == null : this.getTeacherName().equals(other.getTeacherName()))
            && (this.getTeacherNo() == null ? other.getTeacherNo() == null : this.getTeacherNo().equals(other.getTeacherNo()))
            && (this.getClassNo() == null ? other.getClassNo() == null : this.getClassNo().equals(other.getClassNo()))
            && (this.getStudentId() == null ? other.getStudentId() == null : this.getStudentId().equals(other.getStudentId()))
            && (this.getClassroomNo() == null ? other.getClassroomNo() == null : this.getClassroomNo().equals(other.getClassroomNo()))
            && (this.getOldClassroomNo() == null ? other.getOldClassroomNo() == null : this.getOldClassroomNo().equals(other.getOldClassroomNo()))
            && (this.getCourseEndTime() == null ? other.getCourseEndTime() == null : this.getCourseEndTime().equals(other.getCourseEndTime()))
            && (this.getCourseEndTimeWeekIndex() == null ? other.getCourseEndTimeWeekIndex() == null : this.getCourseEndTimeWeekIndex().equals(other.getCourseEndTimeWeekIndex()))
            && (this.getCourseEndTimeWeekName() == null ? other.getCourseEndTimeWeekName() == null : this.getCourseEndTimeWeekName().equals(other.getCourseEndTimeWeekName()))
            && (this.getCourseEndTimeSection() == null ? other.getCourseEndTimeSection() == null : this.getCourseEndTimeSection().equals(other.getCourseEndTimeSection()))
            && (this.getInvigilateTeacherId() == null ? other.getInvigilateTeacherId() == null : this.getInvigilateTeacherId().equals(other.getInvigilateTeacherId()))
            && (this.getPatrolTeacherId() == null ? other.getPatrolTeacherId() == null : this.getPatrolTeacherId().equals(other.getPatrolTeacherId()))
            && (this.getExamTime() == null ? other.getExamTime() == null : this.getExamTime().equals(other.getExamTime()))
            && (this.getExamTimeWeekIndex() == null ? other.getExamTimeWeekIndex() == null : this.getExamTimeWeekIndex().equals(other.getExamTimeWeekIndex()))
            && (this.getExamTimeWeekName() == null ? other.getExamTimeWeekName() == null : this.getExamTimeWeekName().equals(other.getExamTimeWeekName()))
            && (this.getExamTimeSection() == null ? other.getExamTimeSection() == null : this.getExamTimeSection().equals(other.getExamTimeSection()))
            && (this.getExamTimePeriod() == null ? other.getExamTimePeriod() == null : this.getExamTimePeriod().equals(other.getExamTimePeriod()))
            && (this.getOldExamTime() == null ? other.getOldExamTime() == null : this.getOldExamTime().equals(other.getOldExamTime()))
            && (this.getOldExamTimeWeekIndex() == null ? other.getOldExamTimeWeekIndex() == null : this.getOldExamTimeWeekIndex().equals(other.getOldExamTimeWeekIndex()))
            && (this.getOldExamTimeWeekName() == null ? other.getOldExamTimeWeekName() == null : this.getOldExamTimeWeekName().equals(other.getOldExamTimeWeekName()))
            && (this.getOldExamTimeSection() == null ? other.getOldExamTimeSection() == null : this.getOldExamTimeSection().equals(other.getOldExamTimeSection()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getAttachment() == null ? other.getAttachment() == null : this.getAttachment().equals(other.getAttachment()))
            && (this.getAuditUserId() == null ? other.getAuditUserId() == null : this.getAuditUserId().equals(other.getAuditUserId()))
            && (this.getAuditTime() == null ? other.getAuditTime() == null : this.getAuditTime().equals(other.getAuditTime()))
            && (this.getAuditRemark() == null ? other.getAuditRemark() == null : this.getAuditRemark().equals(other.getAuditRemark()))
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
        result = prime * result + ((getCourseNo() == null) ? 0 : getCourseNo().hashCode());
        result = prime * result + ((getCourseSerialNo() == null) ? 0 : getCourseSerialNo().hashCode());
        result = prime * result + ((getCourseName() == null) ? 0 : getCourseName().hashCode());
        result = prime * result + ((getTeacherName() == null) ? 0 : getTeacherName().hashCode());
        result = prime * result + ((getTeacherNo() == null) ? 0 : getTeacherNo().hashCode());
        result = prime * result + ((getClassNo() == null) ? 0 : getClassNo().hashCode());
        result = prime * result + ((getStudentId() == null) ? 0 : getStudentId().hashCode());
        result = prime * result + ((getClassroomNo() == null) ? 0 : getClassroomNo().hashCode());
        result = prime * result + ((getOldClassroomNo() == null) ? 0 : getOldClassroomNo().hashCode());
        result = prime * result + ((getCourseEndTime() == null) ? 0 : getCourseEndTime().hashCode());
        result = prime * result + ((getCourseEndTimeWeekIndex() == null) ? 0 : getCourseEndTimeWeekIndex().hashCode());
        result = prime * result + ((getCourseEndTimeWeekName() == null) ? 0 : getCourseEndTimeWeekName().hashCode());
        result = prime * result + ((getCourseEndTimeSection() == null) ? 0 : getCourseEndTimeSection().hashCode());
        result = prime * result + ((getInvigilateTeacherId() == null) ? 0 : getInvigilateTeacherId().hashCode());
        result = prime * result + ((getPatrolTeacherId() == null) ? 0 : getPatrolTeacherId().hashCode());
        result = prime * result + ((getExamTime() == null) ? 0 : getExamTime().hashCode());
        result = prime * result + ((getExamTimeWeekIndex() == null) ? 0 : getExamTimeWeekIndex().hashCode());
        result = prime * result + ((getExamTimeWeekName() == null) ? 0 : getExamTimeWeekName().hashCode());
        result = prime * result + ((getExamTimeSection() == null) ? 0 : getExamTimeSection().hashCode());
        result = prime * result + ((getExamTimePeriod() == null) ? 0 : getExamTimePeriod().hashCode());
        result = prime * result + ((getOldExamTime() == null) ? 0 : getOldExamTime().hashCode());
        result = prime * result + ((getOldExamTimeWeekIndex() == null) ? 0 : getOldExamTimeWeekIndex().hashCode());
        result = prime * result + ((getOldExamTimeWeekName() == null) ? 0 : getOldExamTimeWeekName().hashCode());
        result = prime * result + ((getOldExamTimeSection() == null) ? 0 : getOldExamTimeSection().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getAttachment() == null) ? 0 : getAttachment().hashCode());
        result = prime * result + ((getAuditUserId() == null) ? 0 : getAuditUserId().hashCode());
        result = prime * result + ((getAuditTime() == null) ? 0 : getAuditTime().hashCode());
        result = prime * result + ((getAuditRemark() == null) ? 0 : getAuditRemark().hashCode());
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
        sb.append(", courseNo=").append(courseNo);
        sb.append(", courseSerialNo=").append(courseSerialNo);
        sb.append(", courseName=").append(courseName);
        sb.append(", teacherName=").append(teacherName);
        sb.append(", teacherNo=").append(teacherNo);
        sb.append(", classNo=").append(classNo);
        sb.append(", studentId=").append(studentId);
        sb.append(", classroomNo=").append(classroomNo);
        sb.append(", oldClassroomNo=").append(oldClassroomNo);
        sb.append(", courseEndTime=").append(courseEndTime);
        sb.append(", courseEndTimeWeekIndex=").append(courseEndTimeWeekIndex);
        sb.append(", courseEndTimeWeekName=").append(courseEndTimeWeekName);
        sb.append(", courseEndTimeSection=").append(courseEndTimeSection);
        sb.append(", invigilateTeacherId=").append(invigilateTeacherId);
        sb.append(", patrolTeacherId=").append(patrolTeacherId);
        sb.append(", examTime=").append(examTime);
        sb.append(", examTimeWeekIndex=").append(examTimeWeekIndex);
        sb.append(", examTimeWeekName=").append(examTimeWeekName);
        sb.append(", examTimeSection=").append(examTimeSection);
        sb.append(", examTimePeriod=").append(examTimePeriod);
        sb.append(", oldExamTime=").append(oldExamTime);
        sb.append(", oldExamTimeWeekIndex=").append(oldExamTimeWeekIndex);
        sb.append(", oldExamTimeWeekName=").append(oldExamTimeWeekName);
        sb.append(", oldExamTimeSection=").append(oldExamTimeSection);
        sb.append(", status=").append(status);
        sb.append(", attachment=").append(attachment);
        sb.append(", auditUserId=").append(auditUserId);
        sb.append(", auditTime=").append(auditTime);
        sb.append(", auditRemark=").append(auditRemark);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}