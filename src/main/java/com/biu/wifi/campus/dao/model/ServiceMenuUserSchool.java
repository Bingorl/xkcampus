package com.biu.wifi.campus.dao.model;

import com.biu.wifi.core.base.CoreEntity;

import java.util.Date;

public class ServiceMenuUserSchool extends CoreEntity {
    private Integer id;

    private Integer userId;

    private Integer schoolId;

    private Integer serviceMenuId;

    private Integer sort;

    private Date sortTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
    }

    public Integer getServiceMenuId() {
        return serviceMenuId;
    }

    public void setServiceMenuId(Integer serviceMenuId) {
        this.serviceMenuId = serviceMenuId;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Date getSortTime() {
        return sortTime;
    }

    public void setSortTime(Date sortTime) {
        this.sortTime = sortTime;
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
        ServiceMenuUserSchool other = (ServiceMenuUserSchool) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
                && (this.getSchoolId() == null ? other.getSchoolId() == null
                : this.getSchoolId().equals(other.getSchoolId()))
                && (this.getServiceMenuId() == null ? other.getServiceMenuId() == null
                : this.getServiceMenuId().equals(other.getServiceMenuId()))
                && (this.getSort() == null ? other.getSort() == null : this.getSort().equals(other.getSort()))
                && (this.getSortTime() == null ? other.getSortTime() == null
                : this.getSortTime().equals(other.getSortTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getSchoolId() == null) ? 0 : getSchoolId().hashCode());
        result = prime * result + ((getServiceMenuId() == null) ? 0 : getServiceMenuId().hashCode());
        result = prime * result + ((getSort() == null) ? 0 : getSort().hashCode());
        result = prime * result + ((getSortTime() == null) ? 0 : getSortTime().hashCode());
        return result;
    }
}