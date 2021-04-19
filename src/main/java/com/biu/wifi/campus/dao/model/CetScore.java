package com.biu.wifi.campus.dao.model;

import java.io.Serializable;

/**
 * biu_cet_score
 *
 * @author
 */
public class CetScore implements Serializable {
    /**
     * 准考证号
     */
    private String exCardNum;

    /**
     * 学校ID
     */
    private Integer schoolId;

    /**
     * 学校名称
     */
    private String schoolName;

    /**
     * 考试级别
     */
    private String cet;

    /**
     * 总分
     */
    private String totalScore;

    /**
     * 听力
     */
    private String listenScore;

    /**
     * 阅读
     */
    private String readScore;

    /**
     * 写作和翻译
     */
    private String writingScore;

    /**
     * 口试等级
     */
    private String oralTestScore;

    /**
     * 考试时间
     */
    private String cetExamTime;

    private static final long serialVersionUID = 1L;

    public String getExCardNum() {
        return exCardNum;
    }

    public void setExCardNum(String exCardNum) {
        this.exCardNum = exCardNum;
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

    public String getCet() {
        return cet;
    }

    public void setCet(String cet) {
        this.cet = cet;
    }

    public String getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(String totalScore) {
        this.totalScore = totalScore;
    }

    public String getListenScore() {
        return listenScore;
    }

    public void setListenScore(String listenScore) {
        this.listenScore = listenScore;
    }

    public String getReadScore() {
        return readScore;
    }

    public void setReadScore(String readScore) {
        this.readScore = readScore;
    }

    public String getWritingScore() {
        return writingScore;
    }

    public void setWritingScore(String writingScore) {
        this.writingScore = writingScore;
    }

    public String getOralTestScore() {
        return oralTestScore;
    }

    public void setOralTestScore(String oralTestScore) {
        this.oralTestScore = oralTestScore;
    }

    public String getCetExamTime() {
        return cetExamTime;
    }

    public void setCetExamTime(String cetExamTime) {
        this.cetExamTime = cetExamTime;
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
        CetScore other = (CetScore) that;
        return (this.getExCardNum() == null ? other.getExCardNum() == null : this.getExCardNum().equals(other.getExCardNum()))
                && (this.getSchoolId() == null ? other.getSchoolId() == null : this.getSchoolId().equals(other.getSchoolId()))
                && (this.getSchoolName() == null ? other.getSchoolName() == null : this.getSchoolName().equals(other.getSchoolName()))
                && (this.getCet() == null ? other.getCet() == null : this.getCet().equals(other.getCet()))
                && (this.getTotalScore() == null ? other.getTotalScore() == null : this.getTotalScore().equals(other.getTotalScore()))
                && (this.getListenScore() == null ? other.getListenScore() == null : this.getListenScore().equals(other.getListenScore()))
                && (this.getReadScore() == null ? other.getReadScore() == null : this.getReadScore().equals(other.getReadScore()))
                && (this.getWritingScore() == null ? other.getWritingScore() == null : this.getWritingScore().equals(other.getWritingScore()))
                && (this.getOralTestScore() == null ? other.getOralTestScore() == null : this.getOralTestScore().equals(other.getOralTestScore()))
                && (this.getCetExamTime() == null ? other.getCetExamTime() == null : this.getCetExamTime().equals(other.getCetExamTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getExCardNum() == null) ? 0 : getExCardNum().hashCode());
        result = prime * result + ((getSchoolId() == null) ? 0 : getSchoolId().hashCode());
        result = prime * result + ((getSchoolName() == null) ? 0 : getSchoolName().hashCode());
        result = prime * result + ((getCet() == null) ? 0 : getCet().hashCode());
        result = prime * result + ((getTotalScore() == null) ? 0 : getTotalScore().hashCode());
        result = prime * result + ((getListenScore() == null) ? 0 : getListenScore().hashCode());
        result = prime * result + ((getReadScore() == null) ? 0 : getReadScore().hashCode());
        result = prime * result + ((getWritingScore() == null) ? 0 : getWritingScore().hashCode());
        result = prime * result + ((getOralTestScore() == null) ? 0 : getOralTestScore().hashCode());
        result = prime * result + ((getCetExamTime() == null) ? 0 : getCetExamTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", exCardNum=").append(exCardNum);
        sb.append(", schoolId=").append(schoolId);
        sb.append(", schoolName=").append(schoolName);
        sb.append(", cet=").append(cet);
        sb.append(", totalScore=").append(totalScore);
        sb.append(", listenScore=").append(listenScore);
        sb.append(", readScore=").append(readScore);
        sb.append(", writingScore=").append(writingScore);
        sb.append(", oralTestScore=").append(oralTestScore);
        sb.append(", cetExamTime=").append(cetExamTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}