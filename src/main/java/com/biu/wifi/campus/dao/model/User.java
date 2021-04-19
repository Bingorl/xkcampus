package com.biu.wifi.campus.dao.model;

import com.biu.wifi.core.base.CoreEntity;

import java.util.Date;

public class User extends CoreEntity {

    private Integer id;

    private Short type;

    private String name;

    private String headimg;

    private String introduction;

    private String phone;

    private String salt;

    private String password;

    private Short sex;

    private String signature;

    private String stuNumber;

    private Integer gradeId;

    private String grade;

    private Integer schoolId;

    private Integer instituteId;

    private Integer majorId;

    private Integer classId;

    private String area;

    private Integer focusNum;

    private Integer fansNum;

    private Integer postNum;

    private Integer newsNum;

    private Short isAuth;

    private Short status;

    private Date disableTime;

    private Integer creatorId;

    private String devId;

    private String devToken;

    private Short devType;

    private String huanxinId;

    private Short isPushAt;

    private Short isPushCmt;

    private Short isPushLike;

    private Short isPushQuestion;

    private Date createTime;

    private Date updateTime;

    private Short isDelete;

    private Date deleteTime;

    private Short isTeacher;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getHeadimg() {
        return headimg;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg == null ? null : headimg.trim();
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction == null ? null : introduction.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt == null ? null : salt.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public Short getSex() {
        return sex;
    }

    public void setSex(Short sex) {
        this.sex = sex;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature == null ? null : signature.trim();
    }

    public String getStuNumber() {
        return stuNumber;
    }

    public void setStuNumber(String stuNumber) {
        this.stuNumber = stuNumber == null ? null : stuNumber.trim();
    }

    public Integer getGradeId() {
        return gradeId;
    }

    public void setGradeId(Integer gradeId) {
        this.gradeId = gradeId;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade == null ? null : grade.trim();
    }

    public Integer getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
    }

    public Integer getInstituteId() {
        return instituteId;
    }

    public void setInstituteId(Integer instituteId) {
        this.instituteId = instituteId;
    }

    public Integer getMajorId() {
        return majorId;
    }

    public void setMajorId(Integer majorId) {
        this.majorId = majorId;
    }

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area == null ? null : area.trim();
    }

    public Integer getFocusNum() {
        return focusNum;
    }

    public void setFocusNum(Integer focusNum) {
        this.focusNum = focusNum;
    }

    public Integer getFansNum() {
        return fansNum;
    }

    public void setFansNum(Integer fansNum) {
        this.fansNum = fansNum;
    }

    public Integer getPostNum() {
        return postNum;
    }

    public void setPostNum(Integer postNum) {
        this.postNum = postNum;
    }

    public Integer getNewsNum() {
        return newsNum;
    }

    public void setNewsNum(Integer newsNum) {
        this.newsNum = newsNum;
    }

    public Short getIsAuth() {
        return isAuth;
    }

    public void setIsAuth(Short isAuth) {
        this.isAuth = isAuth;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public Date getDisableTime() {
        return disableTime;
    }

    public void setDisableTime(Date disableTime) {
        this.disableTime = disableTime;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId == null ? null : devId.trim();
    }

    public String getDevToken() {
        return devToken;
    }

    public void setDevToken(String devToken) {
        this.devToken = devToken == null ? null : devToken.trim();
    }

    public Short getDevType() {
        return devType;
    }

    public void setDevType(Short devType) {
        this.devType = devType;
    }

    public String getHuanxinId() {
        return huanxinId;
    }

    public void setHuanxinId(String huanxinId) {
        this.huanxinId = huanxinId == null ? null : huanxinId.trim();
    }

    public Short getIsPushAt() {
        return isPushAt;
    }

    public void setIsPushAt(Short isPushAt) {
        this.isPushAt = isPushAt;
    }

    public Short getIsPushCmt() {
        return isPushCmt;
    }

    public void setIsPushCmt(Short isPushCmt) {
        this.isPushCmt = isPushCmt;
    }

    public Short getIsPushLike() {
        return isPushLike;
    }

    public void setIsPushLike(Short isPushLike) {
        this.isPushLike = isPushLike;
    }

    public Short getIsPushQuestion() {
        return isPushQuestion;
    }

    public void setIsPushQuestion(Short isPushQuestion) {
        this.isPushQuestion = isPushQuestion;
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

    public Short getIsTeacher() {
        return isTeacher;
    }

    public void setIsTeacher(Short isTeacher) {
        this.isTeacher = isTeacher;
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
        User other = (User) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
                && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
                && (this.getHeadimg() == null ? other.getHeadimg() == null
                : this.getHeadimg().equals(other.getHeadimg()))
                && (this.getIntroduction() == null ? other.getIntroduction() == null
                : this.getIntroduction().equals(other.getIntroduction()))
                && (this.getPhone() == null ? other.getPhone() == null : this.getPhone().equals(other.getPhone()))
                && (this.getSalt() == null ? other.getSalt() == null : this.getSalt().equals(other.getSalt()))
                && (this.getPassword() == null ? other.getPassword() == null
                : this.getPassword().equals(other.getPassword()))
                && (this.getSex() == null ? other.getSex() == null : this.getSex().equals(other.getSex()))
                && (this.getSignature() == null ? other.getSignature() == null
                : this.getSignature().equals(other.getSignature()))
                && (this.getStuNumber() == null ? other.getStuNumber() == null
                : this.getStuNumber().equals(other.getStuNumber()))
                && (this.getGradeId() == null ? other.getGradeId() == null
                : this.getGradeId().equals(other.getGradeId()))
                && (this.getGrade() == null ? other.getGrade() == null : this.getGrade().equals(other.getGrade()))
                && (this.getSchoolId() == null ? other.getSchoolId() == null
                : this.getSchoolId().equals(other.getSchoolId()))
                && (this.getInstituteId() == null ? other.getInstituteId() == null
                : this.getInstituteId().equals(other.getInstituteId()))
                && (this.getMajorId() == null ? other.getMajorId() == null
                : this.getMajorId().equals(other.getMajorId()))
                && (this.getClassId() == null ? other.getClassId() == null
                : this.getClassId().equals(other.getClassId()))
                && (this.getArea() == null ? other.getArea() == null : this.getArea().equals(other.getArea()))
                && (this.getFocusNum() == null ? other.getFocusNum() == null
                : this.getFocusNum().equals(other.getFocusNum()))
                && (this.getFansNum() == null ? other.getFansNum() == null
                : this.getFansNum().equals(other.getFansNum()))
                && (this.getPostNum() == null ? other.getPostNum() == null
                : this.getPostNum().equals(other.getPostNum()))
                && (this.getNewsNum() == null ? other.getNewsNum() == null
                : this.getNewsNum().equals(other.getNewsNum()))
                && (this.getIsAuth() == null ? other.getIsAuth() == null : this.getIsAuth().equals(other.getIsAuth()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
                && (this.getDisableTime() == null ? other.getDisableTime() == null
                : this.getDisableTime().equals(other.getDisableTime()))
                && (this.getCreatorId() == null ? other.getCreatorId() == null
                : this.getCreatorId().equals(other.getCreatorId()))
                && (this.getDevId() == null ? other.getDevId() == null : this.getDevId().equals(other.getDevId()))
                && (this.getDevToken() == null ? other.getDevToken() == null
                : this.getDevToken().equals(other.getDevToken()))
                && (this.getDevType() == null ? other.getDevType() == null
                : this.getDevType().equals(other.getDevType()))
                && (this.getHuanxinId() == null ? other.getHuanxinId() == null
                : this.getHuanxinId().equals(other.getHuanxinId()))
                && (this.getIsPushAt() == null ? other.getIsPushAt() == null
                : this.getIsPushAt().equals(other.getIsPushAt()))
                && (this.getIsPushCmt() == null ? other.getIsPushCmt() == null
                : this.getIsPushCmt().equals(other.getIsPushCmt()))
                && (this.getIsPushLike() == null ? other.getIsPushLike() == null
                : this.getIsPushLike().equals(other.getIsPushLike()))
                && (this.getIsPushQuestion() == null ? other.getIsPushQuestion() == null
                : this.getIsPushQuestion().equals(other.getIsPushQuestion()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null
                : this.getCreateTime().equals(other.getCreateTime()))
                && (this.getUpdateTime() == null ? other.getUpdateTime() == null
                : this.getUpdateTime().equals(other.getUpdateTime()))
                && (this.getIsDelete() == null ? other.getIsDelete() == null
                : this.getIsDelete().equals(other.getIsDelete()))
                && (this.getDeleteTime() == null ? other.getDeleteTime() == null
                : this.getDeleteTime().equals(other.getDeleteTime()))
                && (this.getIsTeacher() == null ? other.getIsTeacher() == null
                : this.getIsTeacher().equals(other.getIsTeacher()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getHeadimg() == null) ? 0 : getHeadimg().hashCode());
        result = prime * result + ((getIntroduction() == null) ? 0 : getIntroduction().hashCode());
        result = prime * result + ((getPhone() == null) ? 0 : getPhone().hashCode());
        result = prime * result + ((getSalt() == null) ? 0 : getSalt().hashCode());
        result = prime * result + ((getPassword() == null) ? 0 : getPassword().hashCode());
        result = prime * result + ((getSex() == null) ? 0 : getSex().hashCode());
        result = prime * result + ((getSignature() == null) ? 0 : getSignature().hashCode());
        result = prime * result + ((getStuNumber() == null) ? 0 : getStuNumber().hashCode());
        result = prime * result + ((getGradeId() == null) ? 0 : getGradeId().hashCode());
        result = prime * result + ((getGrade() == null) ? 0 : getGrade().hashCode());
        result = prime * result + ((getSchoolId() == null) ? 0 : getSchoolId().hashCode());
        result = prime * result + ((getInstituteId() == null) ? 0 : getInstituteId().hashCode());
        result = prime * result + ((getMajorId() == null) ? 0 : getMajorId().hashCode());
        result = prime * result + ((getClassId() == null) ? 0 : getClassId().hashCode());
        result = prime * result + ((getArea() == null) ? 0 : getArea().hashCode());
        result = prime * result + ((getFocusNum() == null) ? 0 : getFocusNum().hashCode());
        result = prime * result + ((getFansNum() == null) ? 0 : getFansNum().hashCode());
        result = prime * result + ((getPostNum() == null) ? 0 : getPostNum().hashCode());
        result = prime * result + ((getNewsNum() == null) ? 0 : getNewsNum().hashCode());
        result = prime * result + ((getIsAuth() == null) ? 0 : getIsAuth().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getDisableTime() == null) ? 0 : getDisableTime().hashCode());
        result = prime * result + ((getCreatorId() == null) ? 0 : getCreatorId().hashCode());
        result = prime * result + ((getDevId() == null) ? 0 : getDevId().hashCode());
        result = prime * result + ((getDevToken() == null) ? 0 : getDevToken().hashCode());
        result = prime * result + ((getDevType() == null) ? 0 : getDevType().hashCode());
        result = prime * result + ((getHuanxinId() == null) ? 0 : getHuanxinId().hashCode());
        result = prime * result + ((getIsPushAt() == null) ? 0 : getIsPushAt().hashCode());
        result = prime * result + ((getIsPushCmt() == null) ? 0 : getIsPushCmt().hashCode());
        result = prime * result + ((getIsPushLike() == null) ? 0 : getIsPushLike().hashCode());
        result = prime * result + ((getIsPushQuestion() == null) ? 0 : getIsPushQuestion().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getIsDelete() == null) ? 0 : getIsDelete().hashCode());
        result = prime * result + ((getDeleteTime() == null) ? 0 : getDeleteTime().hashCode());
        result = prime * result + ((getIsTeacher() == null) ? 0 : getIsTeacher().hashCode());
        return result;
    }
}