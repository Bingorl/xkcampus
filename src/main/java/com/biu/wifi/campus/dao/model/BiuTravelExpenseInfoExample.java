package com.biu.wifi.campus.dao.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BiuTravelExpenseInfoExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public BiuTravelExpenseInfoExample() {
        oredCriteria = new ArrayList<>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andPaymentTypeIsNull() {
            addCriterion("payment_type is null");
            return (Criteria) this;
        }

        public Criteria andPaymentTypeIsNotNull() {
            addCriterion("payment_type is not null");
            return (Criteria) this;
        }

        public Criteria andPaymentTypeEqualTo(Integer value) {
            addCriterion("payment_type =", value, "paymentType");
            return (Criteria) this;
        }

        public Criteria andPaymentTypeNotEqualTo(Integer value) {
            addCriterion("payment_type <>", value, "paymentType");
            return (Criteria) this;
        }

        public Criteria andPaymentTypeGreaterThan(Integer value) {
            addCriterion("payment_type >", value, "paymentType");
            return (Criteria) this;
        }

        public Criteria andPaymentTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("payment_type >=", value, "paymentType");
            return (Criteria) this;
        }

        public Criteria andPaymentTypeLessThan(Integer value) {
            addCriterion("payment_type <", value, "paymentType");
            return (Criteria) this;
        }

        public Criteria andPaymentTypeLessThanOrEqualTo(Integer value) {
            addCriterion("payment_type <=", value, "paymentType");
            return (Criteria) this;
        }

        public Criteria andPaymentTypeIn(List<Integer> values) {
            addCriterion("payment_type in", values, "paymentType");
            return (Criteria) this;
        }

        public Criteria andPaymentTypeNotIn(List<Integer> values) {
            addCriterion("payment_type not in", values, "paymentType");
            return (Criteria) this;
        }

        public Criteria andPaymentTypeBetween(Integer value1, Integer value2) {
            addCriterion("payment_type between", value1, value2, "paymentType");
            return (Criteria) this;
        }

        public Criteria andPaymentTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("payment_type not between", value1, value2, "paymentType");
            return (Criteria) this;
        }

        public Criteria andPersonsIsNull() {
            addCriterion("persons is null");
            return (Criteria) this;
        }

        public Criteria andPersonsIsNotNull() {
            addCriterion("persons is not null");
            return (Criteria) this;
        }

        public Criteria andPersonsEqualTo(Integer value) {
            addCriterion("persons =", value, "persons");
            return (Criteria) this;
        }

        public Criteria andPersonsNotEqualTo(Integer value) {
            addCriterion("persons <>", value, "persons");
            return (Criteria) this;
        }

        public Criteria andPersonsGreaterThan(Integer value) {
            addCriterion("persons >", value, "persons");
            return (Criteria) this;
        }

        public Criteria andPersonsGreaterThanOrEqualTo(Integer value) {
            addCriterion("persons >=", value, "persons");
            return (Criteria) this;
        }

        public Criteria andPersonsLessThan(Integer value) {
            addCriterion("persons <", value, "persons");
            return (Criteria) this;
        }

        public Criteria andPersonsLessThanOrEqualTo(Integer value) {
            addCriterion("persons <=", value, "persons");
            return (Criteria) this;
        }

        public Criteria andPersonsIn(List<Integer> values) {
            addCriterion("persons in", values, "persons");
            return (Criteria) this;
        }

        public Criteria andPersonsNotIn(List<Integer> values) {
            addCriterion("persons not in", values, "persons");
            return (Criteria) this;
        }

        public Criteria andPersonsBetween(Integer value1, Integer value2) {
            addCriterion("persons between", value1, value2, "persons");
            return (Criteria) this;
        }

        public Criteria andPersonsNotBetween(Integer value1, Integer value2) {
            addCriterion("persons not between", value1, value2, "persons");
            return (Criteria) this;
        }

        public Criteria andVehicleIsNull() {
            addCriterion("vehicle is null");
            return (Criteria) this;
        }

        public Criteria andVehicleIsNotNull() {
            addCriterion("vehicle is not null");
            return (Criteria) this;
        }

        public Criteria andVehicleEqualTo(Integer value) {
            addCriterion("vehicle =", value, "vehicle");
            return (Criteria) this;
        }

        public Criteria andVehicleNotEqualTo(Integer value) {
            addCriterion("vehicle <>", value, "vehicle");
            return (Criteria) this;
        }

        public Criteria andVehicleGreaterThan(Integer value) {
            addCriterion("vehicle >", value, "vehicle");
            return (Criteria) this;
        }

        public Criteria andVehicleGreaterThanOrEqualTo(Integer value) {
            addCriterion("vehicle >=", value, "vehicle");
            return (Criteria) this;
        }

        public Criteria andVehicleLessThan(Integer value) {
            addCriterion("vehicle <", value, "vehicle");
            return (Criteria) this;
        }

        public Criteria andVehicleLessThanOrEqualTo(Integer value) {
            addCriterion("vehicle <=", value, "vehicle");
            return (Criteria) this;
        }

        public Criteria andVehicleIn(List<Integer> values) {
            addCriterion("vehicle in", values, "vehicle");
            return (Criteria) this;
        }

        public Criteria andVehicleNotIn(List<Integer> values) {
            addCriterion("vehicle not in", values, "vehicle");
            return (Criteria) this;
        }

        public Criteria andVehicleBetween(Integer value1, Integer value2) {
            addCriterion("vehicle between", value1, value2, "vehicle");
            return (Criteria) this;
        }

        public Criteria andVehicleNotBetween(Integer value1, Integer value2) {
            addCriterion("vehicle not between", value1, value2, "vehicle");
            return (Criteria) this;
        }

        public Criteria andAddressIsNull() {
            addCriterion("address is null");
            return (Criteria) this;
        }

        public Criteria andAddressIsNotNull() {
            addCriterion("address is not null");
            return (Criteria) this;
        }

        public Criteria andAddressEqualTo(String value) {
            addCriterion("address =", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressNotEqualTo(String value) {
            addCriterion("address <>", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressGreaterThan(String value) {
            addCriterion("address >", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressGreaterThanOrEqualTo(String value) {
            addCriterion("address >=", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressLessThan(String value) {
            addCriterion("address <", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressLessThanOrEqualTo(String value) {
            addCriterion("address <=", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressLike(String value) {
            addCriterion("address like", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressNotLike(String value) {
            addCriterion("address not like", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressIn(List<String> values) {
            addCriterion("address in", values, "address");
            return (Criteria) this;
        }

        public Criteria andAddressNotIn(List<String> values) {
            addCriterion("address not in", values, "address");
            return (Criteria) this;
        }

        public Criteria andAddressBetween(String value1, String value2) {
            addCriterion("address between", value1, value2, "address");
            return (Criteria) this;
        }

        public Criteria andAddressNotBetween(String value1, String value2) {
            addCriterion("address not between", value1, value2, "address");
            return (Criteria) this;
        }

        public Criteria andCostMoneyIsNull() {
            addCriterion("cost_money is null");
            return (Criteria) this;
        }

        public Criteria andCostMoneyIsNotNull() {
            addCriterion("cost_money is not null");
            return (Criteria) this;
        }

        public Criteria andCostMoneyEqualTo(BigDecimal value) {
            addCriterion("cost_money =", value, "costMoney");
            return (Criteria) this;
        }

        public Criteria andCostMoneyNotEqualTo(BigDecimal value) {
            addCriterion("cost_money <>", value, "costMoney");
            return (Criteria) this;
        }

        public Criteria andCostMoneyGreaterThan(BigDecimal value) {
            addCriterion("cost_money >", value, "costMoney");
            return (Criteria) this;
        }

        public Criteria andCostMoneyGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("cost_money >=", value, "costMoney");
            return (Criteria) this;
        }

        public Criteria andCostMoneyLessThan(BigDecimal value) {
            addCriterion("cost_money <", value, "costMoney");
            return (Criteria) this;
        }

        public Criteria andCostMoneyLessThanOrEqualTo(BigDecimal value) {
            addCriterion("cost_money <=", value, "costMoney");
            return (Criteria) this;
        }

        public Criteria andCostMoneyIn(List<BigDecimal> values) {
            addCriterion("cost_money in", values, "costMoney");
            return (Criteria) this;
        }

        public Criteria andCostMoneyNotIn(List<BigDecimal> values) {
            addCriterion("cost_money not in", values, "costMoney");
            return (Criteria) this;
        }

        public Criteria andCostMoneyBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("cost_money between", value1, value2, "costMoney");
            return (Criteria) this;
        }

        public Criteria andCostMoneyNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("cost_money not between", value1, value2, "costMoney");
            return (Criteria) this;
        }

        public Criteria andAmountInWordsIsNull() {
            addCriterion("amount_in_words is null");
            return (Criteria) this;
        }

        public Criteria andAmountInWordsIsNotNull() {
            addCriterion("amount_in_words is not null");
            return (Criteria) this;
        }

        public Criteria andAmountInWordsEqualTo(String value) {
            addCriterion("amount_in_words =", value, "amountInWords");
            return (Criteria) this;
        }

        public Criteria andAmountInWordsNotEqualTo(String value) {
            addCriterion("amount_in_words <>", value, "amountInWords");
            return (Criteria) this;
        }

        public Criteria andAmountInWordsGreaterThan(String value) {
            addCriterion("amount_in_words >", value, "amountInWords");
            return (Criteria) this;
        }

        public Criteria andAmountInWordsGreaterThanOrEqualTo(String value) {
            addCriterion("amount_in_words >=", value, "amountInWords");
            return (Criteria) this;
        }

        public Criteria andAmountInWordsLessThan(String value) {
            addCriterion("amount_in_words <", value, "amountInWords");
            return (Criteria) this;
        }

        public Criteria andAmountInWordsLessThanOrEqualTo(String value) {
            addCriterion("amount_in_words <=", value, "amountInWords");
            return (Criteria) this;
        }

        public Criteria andAmountInWordsLike(String value) {
            addCriterion("amount_in_words like", value, "amountInWords");
            return (Criteria) this;
        }

        public Criteria andAmountInWordsNotLike(String value) {
            addCriterion("amount_in_words not like", value, "amountInWords");
            return (Criteria) this;
        }

        public Criteria andAmountInWordsIn(List<String> values) {
            addCriterion("amount_in_words in", values, "amountInWords");
            return (Criteria) this;
        }

        public Criteria andAmountInWordsNotIn(List<String> values) {
            addCriterion("amount_in_words not in", values, "amountInWords");
            return (Criteria) this;
        }

        public Criteria andAmountInWordsBetween(String value1, String value2) {
            addCriterion("amount_in_words between", value1, value2, "amountInWords");
            return (Criteria) this;
        }

        public Criteria andAmountInWordsNotBetween(String value1, String value2) {
            addCriterion("amount_in_words not between", value1, value2, "amountInWords");
            return (Criteria) this;
        }

        public Criteria andAttachmentIsNull() {
            addCriterion("attachment is null");
            return (Criteria) this;
        }

        public Criteria andAttachmentIsNotNull() {
            addCriterion("attachment is not null");
            return (Criteria) this;
        }

        public Criteria andAttachmentEqualTo(String value) {
            addCriterion("attachment =", value, "attachment");
            return (Criteria) this;
        }

        public Criteria andAttachmentNotEqualTo(String value) {
            addCriterion("attachment <>", value, "attachment");
            return (Criteria) this;
        }

        public Criteria andAttachmentGreaterThan(String value) {
            addCriterion("attachment >", value, "attachment");
            return (Criteria) this;
        }

        public Criteria andAttachmentGreaterThanOrEqualTo(String value) {
            addCriterion("attachment >=", value, "attachment");
            return (Criteria) this;
        }

        public Criteria andAttachmentLessThan(String value) {
            addCriterion("attachment <", value, "attachment");
            return (Criteria) this;
        }

        public Criteria andAttachmentLessThanOrEqualTo(String value) {
            addCriterion("attachment <=", value, "attachment");
            return (Criteria) this;
        }

        public Criteria andAttachmentLike(String value) {
            addCriterion("attachment like", value, "attachment");
            return (Criteria) this;
        }

        public Criteria andAttachmentNotLike(String value) {
            addCriterion("attachment not like", value, "attachment");
            return (Criteria) this;
        }

        public Criteria andAttachmentIn(List<String> values) {
            addCriterion("attachment in", values, "attachment");
            return (Criteria) this;
        }

        public Criteria andAttachmentNotIn(List<String> values) {
            addCriterion("attachment not in", values, "attachment");
            return (Criteria) this;
        }

        public Criteria andAttachmentBetween(String value1, String value2) {
            addCriterion("attachment between", value1, value2, "attachment");
            return (Criteria) this;
        }

        public Criteria andAttachmentNotBetween(String value1, String value2) {
            addCriterion("attachment not between", value1, value2, "attachment");
            return (Criteria) this;
        }

        public Criteria andStartDateIsNull() {
            addCriterion("start_date is null");
            return (Criteria) this;
        }

        public Criteria andStartDateIsNotNull() {
            addCriterion("start_date is not null");
            return (Criteria) this;
        }

        public Criteria andStartDateEqualTo(Date value) {
            addCriterion("start_date =", value, "startDate");
            return (Criteria) this;
        }

        public Criteria andStartDateNotEqualTo(Date value) {
            addCriterion("start_date <>", value, "startDate");
            return (Criteria) this;
        }

        public Criteria andStartDateGreaterThan(Date value) {
            addCriterion("start_date >", value, "startDate");
            return (Criteria) this;
        }

        public Criteria andStartDateGreaterThanOrEqualTo(Date value) {
            addCriterion("start_date >=", value, "startDate");
            return (Criteria) this;
        }

        public Criteria andStartDateLessThan(Date value) {
            addCriterion("start_date <", value, "startDate");
            return (Criteria) this;
        }

        public Criteria andStartDateLessThanOrEqualTo(Date value) {
            addCriterion("start_date <=", value, "startDate");
            return (Criteria) this;
        }

        public Criteria andStartDateIn(List<Date> values) {
            addCriterion("start_date in", values, "startDate");
            return (Criteria) this;
        }

        public Criteria andStartDateNotIn(List<Date> values) {
            addCriterion("start_date not in", values, "startDate");
            return (Criteria) this;
        }

        public Criteria andStartDateBetween(Date value1, Date value2) {
            addCriterion("start_date between", value1, value2, "startDate");
            return (Criteria) this;
        }

        public Criteria andStartDateNotBetween(Date value1, Date value2) {
            addCriterion("start_date not between", value1, value2, "startDate");
            return (Criteria) this;
        }

        public Criteria andEndDateIsNull() {
            addCriterion("end_date is null");
            return (Criteria) this;
        }

        public Criteria andEndDateIsNotNull() {
            addCriterion("end_date is not null");
            return (Criteria) this;
        }

        public Criteria andEndDateEqualTo(Date value) {
            addCriterion("end_date =", value, "endDate");
            return (Criteria) this;
        }

        public Criteria andEndDateNotEqualTo(Date value) {
            addCriterion("end_date <>", value, "endDate");
            return (Criteria) this;
        }

        public Criteria andEndDateGreaterThan(Date value) {
            addCriterion("end_date >", value, "endDate");
            return (Criteria) this;
        }

        public Criteria andEndDateGreaterThanOrEqualTo(Date value) {
            addCriterion("end_date >=", value, "endDate");
            return (Criteria) this;
        }

        public Criteria andEndDateLessThan(Date value) {
            addCriterion("end_date <", value, "endDate");
            return (Criteria) this;
        }

        public Criteria andEndDateLessThanOrEqualTo(Date value) {
            addCriterion("end_date <=", value, "endDate");
            return (Criteria) this;
        }

        public Criteria andEndDateIn(List<Date> values) {
            addCriterion("end_date in", values, "endDate");
            return (Criteria) this;
        }

        public Criteria andEndDateNotIn(List<Date> values) {
            addCriterion("end_date not in", values, "endDate");
            return (Criteria) this;
        }

        public Criteria andEndDateBetween(Date value1, Date value2) {
            addCriterion("end_date between", value1, value2, "endDate");
            return (Criteria) this;
        }

        public Criteria andEndDateNotBetween(Date value1, Date value2) {
            addCriterion("end_date not between", value1, value2, "endDate");
            return (Criteria) this;
        }

        public Criteria andPlanDaysIsNull() {
            addCriterion("plan_days is null");
            return (Criteria) this;
        }

        public Criteria andPlanDaysIsNotNull() {
            addCriterion("plan_days is not null");
            return (Criteria) this;
        }

        public Criteria andPlanDaysEqualTo(Integer value) {
            addCriterion("plan_days =", value, "planDays");
            return (Criteria) this;
        }

        public Criteria andPlanDaysNotEqualTo(Integer value) {
            addCriterion("plan_days <>", value, "planDays");
            return (Criteria) this;
        }

        public Criteria andPlanDaysGreaterThan(Integer value) {
            addCriterion("plan_days >", value, "planDays");
            return (Criteria) this;
        }

        public Criteria andPlanDaysGreaterThanOrEqualTo(Integer value) {
            addCriterion("plan_days >=", value, "planDays");
            return (Criteria) this;
        }

        public Criteria andPlanDaysLessThan(Integer value) {
            addCriterion("plan_days <", value, "planDays");
            return (Criteria) this;
        }

        public Criteria andPlanDaysLessThanOrEqualTo(Integer value) {
            addCriterion("plan_days <=", value, "planDays");
            return (Criteria) this;
        }

        public Criteria andPlanDaysIn(List<Integer> values) {
            addCriterion("plan_days in", values, "planDays");
            return (Criteria) this;
        }

        public Criteria andPlanDaysNotIn(List<Integer> values) {
            addCriterion("plan_days not in", values, "planDays");
            return (Criteria) this;
        }

        public Criteria andPlanDaysBetween(Integer value1, Integer value2) {
            addCriterion("plan_days between", value1, value2, "planDays");
            return (Criteria) this;
        }

        public Criteria andPlanDaysNotBetween(Integer value1, Integer value2) {
            addCriterion("plan_days not between", value1, value2, "planDays");
            return (Criteria) this;
        }

        public Criteria andApplyUserIdIsNull() {
            addCriterion("apply_user_id is null");
            return (Criteria) this;
        }

        public Criteria andApplyUserIdIsNotNull() {
            addCriterion("apply_user_id is not null");
            return (Criteria) this;
        }

        public Criteria andApplyUserIdEqualTo(Integer value) {
            addCriterion("apply_user_id =", value, "applyUserId");
            return (Criteria) this;
        }

        public Criteria andApplyUserIdNotEqualTo(Integer value) {
            addCriterion("apply_user_id <>", value, "applyUserId");
            return (Criteria) this;
        }

        public Criteria andApplyUserIdGreaterThan(Integer value) {
            addCriterion("apply_user_id >", value, "applyUserId");
            return (Criteria) this;
        }

        public Criteria andApplyUserIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("apply_user_id >=", value, "applyUserId");
            return (Criteria) this;
        }

        public Criteria andApplyUserIdLessThan(Integer value) {
            addCriterion("apply_user_id <", value, "applyUserId");
            return (Criteria) this;
        }

        public Criteria andApplyUserIdLessThanOrEqualTo(Integer value) {
            addCriterion("apply_user_id <=", value, "applyUserId");
            return (Criteria) this;
        }

        public Criteria andApplyUserIdIn(List<Integer> values) {
            addCriterion("apply_user_id in", values, "applyUserId");
            return (Criteria) this;
        }

        public Criteria andApplyUserIdNotIn(List<Integer> values) {
            addCriterion("apply_user_id not in", values, "applyUserId");
            return (Criteria) this;
        }

        public Criteria andApplyUserIdBetween(Integer value1, Integer value2) {
            addCriterion("apply_user_id between", value1, value2, "applyUserId");
            return (Criteria) this;
        }

        public Criteria andApplyUserIdNotBetween(Integer value1, Integer value2) {
            addCriterion("apply_user_id not between", value1, value2, "applyUserId");
            return (Criteria) this;
        }

        public Criteria andApplyUserNameIsNull() {
            addCriterion("apply_user_name is null");
            return (Criteria) this;
        }

        public Criteria andApplyUserNameIsNotNull() {
            addCriterion("apply_user_name is not null");
            return (Criteria) this;
        }

        public Criteria andApplyUserNameEqualTo(String value) {
            addCriterion("apply_user_name =", value, "applyUserName");
            return (Criteria) this;
        }

        public Criteria andApplyUserNameNotEqualTo(String value) {
            addCriterion("apply_user_name <>", value, "applyUserName");
            return (Criteria) this;
        }

        public Criteria andApplyUserNameGreaterThan(String value) {
            addCriterion("apply_user_name >", value, "applyUserName");
            return (Criteria) this;
        }

        public Criteria andApplyUserNameGreaterThanOrEqualTo(String value) {
            addCriterion("apply_user_name >=", value, "applyUserName");
            return (Criteria) this;
        }

        public Criteria andApplyUserNameLessThan(String value) {
            addCriterion("apply_user_name <", value, "applyUserName");
            return (Criteria) this;
        }

        public Criteria andApplyUserNameLessThanOrEqualTo(String value) {
            addCriterion("apply_user_name <=", value, "applyUserName");
            return (Criteria) this;
        }

        public Criteria andApplyUserNameLike(String value) {
            addCriterion("apply_user_name like", value, "applyUserName");
            return (Criteria) this;
        }

        public Criteria andApplyUserNameNotLike(String value) {
            addCriterion("apply_user_name not like", value, "applyUserName");
            return (Criteria) this;
        }

        public Criteria andApplyUserNameIn(List<String> values) {
            addCriterion("apply_user_name in", values, "applyUserName");
            return (Criteria) this;
        }

        public Criteria andApplyUserNameNotIn(List<String> values) {
            addCriterion("apply_user_name not in", values, "applyUserName");
            return (Criteria) this;
        }

        public Criteria andApplyUserNameBetween(String value1, String value2) {
            addCriterion("apply_user_name between", value1, value2, "applyUserName");
            return (Criteria) this;
        }

        public Criteria andApplyUserNameNotBetween(String value1, String value2) {
            addCriterion("apply_user_name not between", value1, value2, "applyUserName");
            return (Criteria) this;
        }

        public Criteria andApplyUserNoIsNull() {
            addCriterion("apply_user_no is null");
            return (Criteria) this;
        }

        public Criteria andApplyUserNoIsNotNull() {
            addCriterion("apply_user_no is not null");
            return (Criteria) this;
        }

        public Criteria andApplyUserNoEqualTo(String value) {
            addCriterion("apply_user_no =", value, "applyUserNo");
            return (Criteria) this;
        }

        public Criteria andApplyUserNoNotEqualTo(String value) {
            addCriterion("apply_user_no <>", value, "applyUserNo");
            return (Criteria) this;
        }

        public Criteria andApplyUserNoGreaterThan(String value) {
            addCriterion("apply_user_no >", value, "applyUserNo");
            return (Criteria) this;
        }

        public Criteria andApplyUserNoGreaterThanOrEqualTo(String value) {
            addCriterion("apply_user_no >=", value, "applyUserNo");
            return (Criteria) this;
        }

        public Criteria andApplyUserNoLessThan(String value) {
            addCriterion("apply_user_no <", value, "applyUserNo");
            return (Criteria) this;
        }

        public Criteria andApplyUserNoLessThanOrEqualTo(String value) {
            addCriterion("apply_user_no <=", value, "applyUserNo");
            return (Criteria) this;
        }

        public Criteria andApplyUserNoLike(String value) {
            addCriterion("apply_user_no like", value, "applyUserNo");
            return (Criteria) this;
        }

        public Criteria andApplyUserNoNotLike(String value) {
            addCriterion("apply_user_no not like", value, "applyUserNo");
            return (Criteria) this;
        }

        public Criteria andApplyUserNoIn(List<String> values) {
            addCriterion("apply_user_no in", values, "applyUserNo");
            return (Criteria) this;
        }

        public Criteria andApplyUserNoNotIn(List<String> values) {
            addCriterion("apply_user_no not in", values, "applyUserNo");
            return (Criteria) this;
        }

        public Criteria andApplyUserNoBetween(String value1, String value2) {
            addCriterion("apply_user_no between", value1, value2, "applyUserNo");
            return (Criteria) this;
        }

        public Criteria andApplyUserNoNotBetween(String value1, String value2) {
            addCriterion("apply_user_no not between", value1, value2, "applyUserNo");
            return (Criteria) this;
        }

        public Criteria andApplyUserTelIsNull() {
            addCriterion("apply_user_tel is null");
            return (Criteria) this;
        }

        public Criteria andApplyUserTelIsNotNull() {
            addCriterion("apply_user_tel is not null");
            return (Criteria) this;
        }

        public Criteria andApplyUserTelEqualTo(String value) {
            addCriterion("apply_user_tel =", value, "applyUserTel");
            return (Criteria) this;
        }

        public Criteria andApplyUserTelNotEqualTo(String value) {
            addCriterion("apply_user_tel <>", value, "applyUserTel");
            return (Criteria) this;
        }

        public Criteria andApplyUserTelGreaterThan(String value) {
            addCriterion("apply_user_tel >", value, "applyUserTel");
            return (Criteria) this;
        }

        public Criteria andApplyUserTelGreaterThanOrEqualTo(String value) {
            addCriterion("apply_user_tel >=", value, "applyUserTel");
            return (Criteria) this;
        }

        public Criteria andApplyUserTelLessThan(String value) {
            addCriterion("apply_user_tel <", value, "applyUserTel");
            return (Criteria) this;
        }

        public Criteria andApplyUserTelLessThanOrEqualTo(String value) {
            addCriterion("apply_user_tel <=", value, "applyUserTel");
            return (Criteria) this;
        }

        public Criteria andApplyUserTelLike(String value) {
            addCriterion("apply_user_tel like", value, "applyUserTel");
            return (Criteria) this;
        }

        public Criteria andApplyUserTelNotLike(String value) {
            addCriterion("apply_user_tel not like", value, "applyUserTel");
            return (Criteria) this;
        }

        public Criteria andApplyUserTelIn(List<String> values) {
            addCriterion("apply_user_tel in", values, "applyUserTel");
            return (Criteria) this;
        }

        public Criteria andApplyUserTelNotIn(List<String> values) {
            addCriterion("apply_user_tel not in", values, "applyUserTel");
            return (Criteria) this;
        }

        public Criteria andApplyUserTelBetween(String value1, String value2) {
            addCriterion("apply_user_tel between", value1, value2, "applyUserTel");
            return (Criteria) this;
        }

        public Criteria andApplyUserTelNotBetween(String value1, String value2) {
            addCriterion("apply_user_tel not between", value1, value2, "applyUserTel");
            return (Criteria) this;
        }

        public Criteria andApplyUserDeptIdIsNull() {
            addCriterion("apply_user_dept_id is null");
            return (Criteria) this;
        }

        public Criteria andApplyUserDeptIdIsNotNull() {
            addCriterion("apply_user_dept_id is not null");
            return (Criteria) this;
        }

        public Criteria andApplyUserDeptIdEqualTo(String value) {
            addCriterion("apply_user_dept_id =", value, "applyUserDeptId");
            return (Criteria) this;
        }

        public Criteria andApplyUserDeptIdNotEqualTo(String value) {
            addCriterion("apply_user_dept_id <>", value, "applyUserDeptId");
            return (Criteria) this;
        }

        public Criteria andApplyUserDeptIdGreaterThan(String value) {
            addCriterion("apply_user_dept_id >", value, "applyUserDeptId");
            return (Criteria) this;
        }

        public Criteria andApplyUserDeptIdGreaterThanOrEqualTo(String value) {
            addCriterion("apply_user_dept_id >=", value, "applyUserDeptId");
            return (Criteria) this;
        }

        public Criteria andApplyUserDeptIdLessThan(String value) {
            addCriterion("apply_user_dept_id <", value, "applyUserDeptId");
            return (Criteria) this;
        }

        public Criteria andApplyUserDeptIdLessThanOrEqualTo(String value) {
            addCriterion("apply_user_dept_id <=", value, "applyUserDeptId");
            return (Criteria) this;
        }

        public Criteria andApplyUserDeptIdLike(String value) {
            addCriterion("apply_user_dept_id like", value, "applyUserDeptId");
            return (Criteria) this;
        }

        public Criteria andApplyUserDeptIdNotLike(String value) {
            addCriterion("apply_user_dept_id not like", value, "applyUserDeptId");
            return (Criteria) this;
        }

        public Criteria andApplyUserDeptIdIn(List<String> values) {
            addCriterion("apply_user_dept_id in", values, "applyUserDeptId");
            return (Criteria) this;
        }

        public Criteria andApplyUserDeptIdNotIn(List<String> values) {
            addCriterion("apply_user_dept_id not in", values, "applyUserDeptId");
            return (Criteria) this;
        }

        public Criteria andApplyUserDeptIdBetween(String value1, String value2) {
            addCriterion("apply_user_dept_id between", value1, value2, "applyUserDeptId");
            return (Criteria) this;
        }

        public Criteria andApplyUserDeptIdNotBetween(String value1, String value2) {
            addCriterion("apply_user_dept_id not between", value1, value2, "applyUserDeptId");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("`status` is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("`status` is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(Short value) {
            addCriterion("`status` =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Short value) {
            addCriterion("`status` <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Short value) {
            addCriterion("`status` >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Short value) {
            addCriterion("`status` >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Short value) {
            addCriterion("`status` <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Short value) {
            addCriterion("`status` <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Short> values) {
            addCriterion("`status` in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<Short> values) {
            addCriterion("`status` not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Short value1, Short value2) {
            addCriterion("`status` between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Short value1, Short value2) {
            addCriterion("`status` not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andAuditUserIsNull() {
            addCriterion("audit_user is null");
            return (Criteria) this;
        }

        public Criteria andAuditUserIsNotNull() {
            addCriterion("audit_user is not null");
            return (Criteria) this;
        }

        public Criteria andAuditUserEqualTo(String value) {
            addCriterion("audit_user =", value, "auditUser");
            return (Criteria) this;
        }

        public Criteria andAuditUserNotEqualTo(String value) {
            addCriterion("audit_user <>", value, "auditUser");
            return (Criteria) this;
        }

        public Criteria andAuditUserGreaterThan(String value) {
            addCriterion("audit_user >", value, "auditUser");
            return (Criteria) this;
        }

        public Criteria andAuditUserGreaterThanOrEqualTo(String value) {
            addCriterion("audit_user >=", value, "auditUser");
            return (Criteria) this;
        }

        public Criteria andAuditUserLessThan(String value) {
            addCriterion("audit_user <", value, "auditUser");
            return (Criteria) this;
        }

        public Criteria andAuditUserLessThanOrEqualTo(String value) {
            addCriterion("audit_user <=", value, "auditUser");
            return (Criteria) this;
        }

        public Criteria andAuditUserLike(String value) {
            addCriterion("audit_user like", value, "auditUser");
            return (Criteria) this;
        }

        public Criteria andAuditUserNotLike(String value) {
            addCriterion("audit_user not like", value, "auditUser");
            return (Criteria) this;
        }

        public Criteria andAuditUserIn(List<String> values) {
            addCriterion("audit_user in", values, "auditUser");
            return (Criteria) this;
        }

        public Criteria andAuditUserNotIn(List<String> values) {
            addCriterion("audit_user not in", values, "auditUser");
            return (Criteria) this;
        }

        public Criteria andAuditUserBetween(String value1, String value2) {
            addCriterion("audit_user between", value1, value2, "auditUser");
            return (Criteria) this;
        }

        public Criteria andAuditUserNotBetween(String value1, String value2) {
            addCriterion("audit_user not between", value1, value2, "auditUser");
            return (Criteria) this;
        }

        public Criteria andCurrentAuditUserIdIsNull() {
            addCriterion("current_audit_user_id is null");
            return (Criteria) this;
        }

        public Criteria andCurrentAuditUserIdIsNotNull() {
            addCriterion("current_audit_user_id is not null");
            return (Criteria) this;
        }

        public Criteria andCurrentAuditUserIdEqualTo(Integer value) {
            addCriterion("current_audit_user_id =", value, "currentAuditUserId");
            return (Criteria) this;
        }

        public Criteria andCurrentAuditUserIdNotEqualTo(Integer value) {
            addCriterion("current_audit_user_id <>", value, "currentAuditUserId");
            return (Criteria) this;
        }

        public Criteria andCurrentAuditUserIdGreaterThan(Integer value) {
            addCriterion("current_audit_user_id >", value, "currentAuditUserId");
            return (Criteria) this;
        }

        public Criteria andCurrentAuditUserIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("current_audit_user_id >=", value, "currentAuditUserId");
            return (Criteria) this;
        }

        public Criteria andCurrentAuditUserIdLessThan(Integer value) {
            addCriterion("current_audit_user_id <", value, "currentAuditUserId");
            return (Criteria) this;
        }

        public Criteria andCurrentAuditUserIdLessThanOrEqualTo(Integer value) {
            addCriterion("current_audit_user_id <=", value, "currentAuditUserId");
            return (Criteria) this;
        }

        public Criteria andCurrentAuditUserIdIn(List<Integer> values) {
            addCriterion("current_audit_user_id in", values, "currentAuditUserId");
            return (Criteria) this;
        }

        public Criteria andCurrentAuditUserIdNotIn(List<Integer> values) {
            addCriterion("current_audit_user_id not in", values, "currentAuditUserId");
            return (Criteria) this;
        }

        public Criteria andCurrentAuditUserIdBetween(Integer value1, Integer value2) {
            addCriterion("current_audit_user_id between", value1, value2, "currentAuditUserId");
            return (Criteria) this;
        }

        public Criteria andCurrentAuditUserIdNotBetween(Integer value1, Integer value2) {
            addCriterion("current_audit_user_id not between", value1, value2, "currentAuditUserId");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("update_time is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("update_time is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("update_time =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("update_time <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("update_time >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("update_time >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("update_time <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("update_time <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("update_time in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("update_time not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("update_time between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("update_time not between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andIsDeleteIsNull() {
            addCriterion("is_delete is null");
            return (Criteria) this;
        }

        public Criteria andIsDeleteIsNotNull() {
            addCriterion("is_delete is not null");
            return (Criteria) this;
        }

        public Criteria andIsDeleteEqualTo(Short value) {
            addCriterion("is_delete =", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteNotEqualTo(Short value) {
            addCriterion("is_delete <>", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteGreaterThan(Short value) {
            addCriterion("is_delete >", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteGreaterThanOrEqualTo(Short value) {
            addCriterion("is_delete >=", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteLessThan(Short value) {
            addCriterion("is_delete <", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteLessThanOrEqualTo(Short value) {
            addCriterion("is_delete <=", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteIn(List<Short> values) {
            addCriterion("is_delete in", values, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteNotIn(List<Short> values) {
            addCriterion("is_delete not in", values, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteBetween(Short value1, Short value2) {
            addCriterion("is_delete between", value1, value2, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteNotBetween(Short value1, Short value2) {
            addCriterion("is_delete not between", value1, value2, "isDelete");
            return (Criteria) this;
        }

        public Criteria andDeleteTimeIsNull() {
            addCriterion("delete_time is null");
            return (Criteria) this;
        }

        public Criteria andDeleteTimeIsNotNull() {
            addCriterion("delete_time is not null");
            return (Criteria) this;
        }

        public Criteria andDeleteTimeEqualTo(Date value) {
            addCriterion("delete_time =", value, "deleteTime");
            return (Criteria) this;
        }

        public Criteria andDeleteTimeNotEqualTo(Date value) {
            addCriterion("delete_time <>", value, "deleteTime");
            return (Criteria) this;
        }

        public Criteria andDeleteTimeGreaterThan(Date value) {
            addCriterion("delete_time >", value, "deleteTime");
            return (Criteria) this;
        }

        public Criteria andDeleteTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("delete_time >=", value, "deleteTime");
            return (Criteria) this;
        }

        public Criteria andDeleteTimeLessThan(Date value) {
            addCriterion("delete_time <", value, "deleteTime");
            return (Criteria) this;
        }

        public Criteria andDeleteTimeLessThanOrEqualTo(Date value) {
            addCriterion("delete_time <=", value, "deleteTime");
            return (Criteria) this;
        }

        public Criteria andDeleteTimeIn(List<Date> values) {
            addCriterion("delete_time in", values, "deleteTime");
            return (Criteria) this;
        }

        public Criteria andDeleteTimeNotIn(List<Date> values) {
            addCriterion("delete_time not in", values, "deleteTime");
            return (Criteria) this;
        }

        public Criteria andDeleteTimeBetween(Date value1, Date value2) {
            addCriterion("delete_time between", value1, value2, "deleteTime");
            return (Criteria) this;
        }

        public Criteria andDeleteTimeNotBetween(Date value1, Date value2) {
            addCriterion("delete_time not between", value1, value2, "deleteTime");
            return (Criteria) this;
        }
    }

    /**
     */
    public static class Criteria extends GeneratedCriteria {
        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}