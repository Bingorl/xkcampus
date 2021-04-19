package com.biu.wifi.campus.dao.model;

import java.io.Serializable;
import java.util.Date;

/**
 * biu_dorm_building
 *
 * @author
 */
public class DormBuilding implements Serializable {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 学校ID
     */
    private Integer schoolId;

    /**
     * 学院ID
     */
    private Integer instituteId;

    /**
     * 区域位置
     */
    private String areaPosition;

    /**
     * 宿舍楼编号或名称
     */
    private String no;

    /**
     * 单元编号
     */
    private String unitNo;

    /**
     * 楼层数
     */
    private Integer floorCount;

    /**
     * 宿舍数
     */
    private String roomCount;

    /**
     * 宿管电话
     */
    private String phone;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除(1是2否)
     */
    private Short isDelete;

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

    public String getAreaPosition() {
        return areaPosition;
    }

    public void setAreaPosition(String areaPosition) {
        this.areaPosition = areaPosition;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getUnitNo() {
        return unitNo;
    }

    public void setUnitNo(String unitNo) {
        this.unitNo = unitNo;
    }

    public Integer getFloorCount() {
        return floorCount;
    }

    public void setFloorCount(Integer floorCount) {
        this.floorCount = floorCount;
    }

    public String getRoomCount() {
        return roomCount;
    }

    public void setRoomCount(String roomCount) {
        this.roomCount = roomCount;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
        DormBuilding other = (DormBuilding) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getSchoolId() == null ? other.getSchoolId() == null : this.getSchoolId().equals(other.getSchoolId()))
                && (this.getInstituteId() == null ? other.getInstituteId() == null : this.getInstituteId().equals(other.getInstituteId()))
                && (this.getAreaPosition() == null ? other.getAreaPosition() == null : this.getAreaPosition().equals(other.getAreaPosition()))
                && (this.getNo() == null ? other.getNo() == null : this.getNo().equals(other.getNo()))
                && (this.getUnitNo() == null ? other.getUnitNo() == null : this.getUnitNo().equals(other.getUnitNo()))
                && (this.getFloorCount() == null ? other.getFloorCount() == null : this.getFloorCount().equals(other.getFloorCount()))
                && (this.getRoomCount() == null ? other.getRoomCount() == null : this.getRoomCount().equals(other.getRoomCount()))
                && (this.getPhone() == null ? other.getPhone() == null : this.getPhone().equals(other.getPhone()))
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
        result = prime * result + ((getSchoolId() == null) ? 0 : getSchoolId().hashCode());
        result = prime * result + ((getInstituteId() == null) ? 0 : getInstituteId().hashCode());
        result = prime * result + ((getAreaPosition() == null) ? 0 : getAreaPosition().hashCode());
        result = prime * result + ((getNo() == null) ? 0 : getNo().hashCode());
        result = prime * result + ((getUnitNo() == null) ? 0 : getUnitNo().hashCode());
        result = prime * result + ((getFloorCount() == null) ? 0 : getFloorCount().hashCode());
        result = prime * result + ((getRoomCount() == null) ? 0 : getRoomCount().hashCode());
        result = prime * result + ((getPhone() == null) ? 0 : getPhone().hashCode());
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
        sb.append(", schoolId=").append(schoolId);
        sb.append(", instituteId=").append(instituteId);
        sb.append(", areaPosition=").append(areaPosition);
        sb.append(", no=").append(no);
        sb.append(", unitNo=").append(unitNo);
        sb.append(", floorCount=").append(floorCount);
        sb.append(", roomCount=").append(roomCount);
        sb.append(", phone=").append(phone);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", deleteTime=").append(deleteTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}