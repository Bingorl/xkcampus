package com.biu.wifi.campus.dao.model;

import com.biu.wifi.core.base.CoreEntity;

public class AccountOnline extends CoreEntity {
    private Long accountOnlineId;

    private Integer accountId;

    private String onlineKey;

    private Long edatetime;

    private Long sdatetime;

    private Short type;

    private Integer lengthen;

    public Long getAccountOnlineId() {
        return accountOnlineId;
    }

    public void setAccountOnlineId(Long accountOnlineId) {
        this.accountOnlineId = accountOnlineId;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getOnlineKey() {
        return onlineKey;
    }

    public void setOnlineKey(String onlineKey) {
        this.onlineKey = onlineKey == null ? null : onlineKey.trim();
    }

    public Long getEdatetime() {
        return edatetime;
    }

    public void setEdatetime(Long edatetime) {
        this.edatetime = edatetime;
    }

    public Long getSdatetime() {
        return sdatetime;
    }

    public void setSdatetime(Long sdatetime) {
        this.sdatetime = sdatetime;
    }

    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
    }

    public Integer getLengthen() {
        return lengthen;
    }

    public void setLengthen(Integer lengthen) {
        this.lengthen = lengthen;
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
        AccountOnline other = (AccountOnline) that;
        return (this.getAccountOnlineId() == null ? other.getAccountOnlineId() == null : this.getAccountOnlineId().equals(other.getAccountOnlineId()))
                && (this.getAccountId() == null ? other.getAccountId() == null : this.getAccountId().equals(other.getAccountId()))
                && (this.getOnlineKey() == null ? other.getOnlineKey() == null : this.getOnlineKey().equals(other.getOnlineKey()))
                && (this.getEdatetime() == null ? other.getEdatetime() == null : this.getEdatetime().equals(other.getEdatetime()))
                && (this.getSdatetime() == null ? other.getSdatetime() == null : this.getSdatetime().equals(other.getSdatetime()))
                && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
                && (this.getLengthen() == null ? other.getLengthen() == null : this.getLengthen().equals(other.getLengthen()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getAccountOnlineId() == null) ? 0 : getAccountOnlineId().hashCode());
        result = prime * result + ((getAccountId() == null) ? 0 : getAccountId().hashCode());
        result = prime * result + ((getOnlineKey() == null) ? 0 : getOnlineKey().hashCode());
        result = prime * result + ((getEdatetime() == null) ? 0 : getEdatetime().hashCode());
        result = prime * result + ((getSdatetime() == null) ? 0 : getSdatetime().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getLengthen() == null) ? 0 : getLengthen().hashCode());
        return result;
    }
}