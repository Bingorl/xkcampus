package com.biu.wifi.campus.dao.model;

import com.biu.wifi.core.base.CoreEntity;

public class JwCjcx extends CoreEntity {
    private Integer id;

    private String schoolId;

    private String realName;

    private String sex;

    private String stuNo;

    private String idType;

    private String idNo;

    private String eduBg;

    private String eduSys;

    private String enterYear;

    private String grade;

    private String instituteName;

    private String majorName;

    private String className;

    private String lanType;

    private String isApply;

    private String classCode;

    private String classNo;

    private String majorNo;

    private String instituteNo;

    private String examDate;

    private String examScore;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId == null ? null : schoolId.trim();
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName == null ? null : realName.trim();
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    public String getStuNo() {
        return stuNo;
    }

    public void setStuNo(String stuNo) {
        this.stuNo = stuNo == null ? null : stuNo.trim();
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType == null ? null : idType.trim();
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo == null ? null : idNo.trim();
    }

    public String getEduBg() {
        return eduBg;
    }

    public void setEduBg(String eduBg) {
        this.eduBg = eduBg == null ? null : eduBg.trim();
    }

    public String getEduSys() {
        return eduSys;
    }

    public void setEduSys(String eduSys) {
        this.eduSys = eduSys == null ? null : eduSys.trim();
    }

    public String getEnterYear() {
        return enterYear;
    }

    public void setEnterYear(String enterYear) {
        this.enterYear = enterYear == null ? null : enterYear.trim();
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade == null ? null : grade.trim();
    }

    public String getInstituteName() {
        return instituteName;
    }

    public void setInstituteName(String instituteName) {
        this.instituteName = instituteName == null ? null : instituteName.trim();
    }

    public String getMajorName() {
        return majorName;
    }

    public void setMajorName(String majorName) {
        this.majorName = majorName == null ? null : majorName.trim();
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className == null ? null : className.trim();
    }

    public String getLanType() {
        return lanType;
    }

    public void setLanType(String lanType) {
        this.lanType = lanType == null ? null : lanType.trim();
    }

    public String getIsApply() {
        return isApply;
    }

    public void setIsApply(String isApply) {
        this.isApply = isApply == null ? null : isApply.trim();
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode == null ? null : classCode.trim();
    }

    public String getClassNo() {
        return classNo;
    }

    public void setClassNo(String classNo) {
        this.classNo = classNo == null ? null : classNo.trim();
    }

    public String getMajorNo() {
        return majorNo;
    }

    public void setMajorNo(String majorNo) {
        this.majorNo = majorNo == null ? null : majorNo.trim();
    }

    public String getInstituteNo() {
        return instituteNo;
    }

    public void setInstituteNo(String instituteNo) {
        this.instituteNo = instituteNo == null ? null : instituteNo.trim();
    }

    public String getExamDate() {
        return examDate;
    }

    public void setExamDate(String examDate) {
        this.examDate = examDate == null ? null : examDate.trim();
    }

    public String getExamScore() {
        return examScore;
    }

    public void setExamScore(String examScore) {
        this.examScore = examScore == null ? null : examScore.trim();
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
        JwCjcx other = (JwCjcx) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getSchoolId() == null ? other.getSchoolId() == null
                : this.getSchoolId().equals(other.getSchoolId()))
                && (this.getRealName() == null ? other.getRealName() == null
                : this.getRealName().equals(other.getRealName()))
                && (this.getSex() == null ? other.getSex() == null : this.getSex().equals(other.getSex()))
                && (this.getStuNo() == null ? other.getStuNo() == null : this.getStuNo().equals(other.getStuNo()))
                && (this.getIdType() == null ? other.getIdType() == null : this.getIdType().equals(other.getIdType()))
                && (this.getIdNo() == null ? other.getIdNo() == null : this.getIdNo().equals(other.getIdNo()))
                && (this.getEduBg() == null ? other.getEduBg() == null : this.getEduBg().equals(other.getEduBg()))
                && (this.getEduSys() == null ? other.getEduSys() == null : this.getEduSys().equals(other.getEduSys()))
                && (this.getEnterYear() == null ? other.getEnterYear() == null
                : this.getEnterYear().equals(other.getEnterYear()))
                && (this.getGrade() == null ? other.getGrade() == null : this.getGrade().equals(other.getGrade()))
                && (this.getInstituteName() == null ? other.getInstituteName() == null
                : this.getInstituteName().equals(other.getInstituteName()))
                && (this.getMajorName() == null ? other.getMajorName() == null
                : this.getMajorName().equals(other.getMajorName()))
                && (this.getClassName() == null ? other.getClassName() == null
                : this.getClassName().equals(other.getClassName()))
                && (this.getLanType() == null ? other.getLanType() == null
                : this.getLanType().equals(other.getLanType()))
                && (this.getIsApply() == null ? other.getIsApply() == null
                : this.getIsApply().equals(other.getIsApply()))
                && (this.getClassCode() == null ? other.getClassCode() == null
                : this.getClassCode().equals(other.getClassCode()))
                && (this.getClassNo() == null ? other.getClassNo() == null
                : this.getClassNo().equals(other.getClassNo()))
                && (this.getMajorNo() == null ? other.getMajorNo() == null
                : this.getMajorNo().equals(other.getMajorNo()))
                && (this.getInstituteNo() == null ? other.getInstituteNo() == null
                : this.getInstituteNo().equals(other.getInstituteNo()))
                && (this.getExamDate() == null ? other.getExamDate() == null
                : this.getExamDate().equals(other.getExamDate()))
                && (this.getExamScore() == null ? other.getExamScore() == null
                : this.getExamScore().equals(other.getExamScore()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getSchoolId() == null) ? 0 : getSchoolId().hashCode());
        result = prime * result + ((getRealName() == null) ? 0 : getRealName().hashCode());
        result = prime * result + ((getSex() == null) ? 0 : getSex().hashCode());
        result = prime * result + ((getStuNo() == null) ? 0 : getStuNo().hashCode());
        result = prime * result + ((getIdType() == null) ? 0 : getIdType().hashCode());
        result = prime * result + ((getIdNo() == null) ? 0 : getIdNo().hashCode());
        result = prime * result + ((getEduBg() == null) ? 0 : getEduBg().hashCode());
        result = prime * result + ((getEduSys() == null) ? 0 : getEduSys().hashCode());
        result = prime * result + ((getEnterYear() == null) ? 0 : getEnterYear().hashCode());
        result = prime * result + ((getGrade() == null) ? 0 : getGrade().hashCode());
        result = prime * result + ((getInstituteName() == null) ? 0 : getInstituteName().hashCode());
        result = prime * result + ((getMajorName() == null) ? 0 : getMajorName().hashCode());
        result = prime * result + ((getClassName() == null) ? 0 : getClassName().hashCode());
        result = prime * result + ((getLanType() == null) ? 0 : getLanType().hashCode());
        result = prime * result + ((getIsApply() == null) ? 0 : getIsApply().hashCode());
        result = prime * result + ((getClassCode() == null) ? 0 : getClassCode().hashCode());
        result = prime * result + ((getClassNo() == null) ? 0 : getClassNo().hashCode());
        result = prime * result + ((getMajorNo() == null) ? 0 : getMajorNo().hashCode());
        result = prime * result + ((getInstituteNo() == null) ? 0 : getInstituteNo().hashCode());
        result = prime * result + ((getExamDate() == null) ? 0 : getExamDate().hashCode());
        result = prime * result + ((getExamScore() == null) ? 0 : getExamScore().hashCode());
        return result;
    }
}