package com.biu.wifi.campus.dao.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * biu_travel_expense_detail
 * @author 
 */
public class BiuTravelExpenseDetail implements Serializable {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 申请ID
     */
    private Integer expenseId;

    /**
     * 费用名称
     */
    private String costTitle;

    /**
     * 金额
     */
    private BigDecimal costMoney;

    /**
     * 摘要
     */
    private String digest;

    /**
     * 备注说明
     */
    private String remark;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(Integer expenseId) {
        this.expenseId = expenseId;
    }

    public String getCostTitle() {
        return costTitle;
    }

    public void setCostTitle(String costTitle) {
        this.costTitle = costTitle;
    }

    public BigDecimal getCostMoney() {
        return costMoney;
    }

    public void setCostMoney(BigDecimal costMoney) {
        this.costMoney = costMoney;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}