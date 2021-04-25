package com.biu.wifi.campus.dao.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StampToApplyInfoExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public StampToApplyInfoExample() {
        oredCriteria = new ArrayList<Criteria>();
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
            criteria = new ArrayList<Criterion>();
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

        public Criteria andApplyTypeIsNull() {
            addCriterion("apply_type is null");
            return (Criteria) this;
        }

        public Criteria andApplyTypeIsNotNull() {
            addCriterion("apply_type is not null");
            return (Criteria) this;
        }

        public Criteria andApplyTypeEqualTo(Integer value) {
            addCriterion("apply_type =", value, "applyType");
            return (Criteria) this;
        }

        public Criteria andApplyTypeNotEqualTo(Integer value) {
            addCriterion("apply_type <>", value, "applyType");
            return (Criteria) this;
        }

        public Criteria andApplyTypeGreaterThan(Integer value) {
            addCriterion("apply_type >", value, "applyType");
            return (Criteria) this;
        }

        public Criteria andApplyTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("apply_type >=", value, "applyType");
            return (Criteria) this;
        }

        public Criteria andApplyTypeLessThan(Integer value) {
            addCriterion("apply_type <", value, "applyType");
            return (Criteria) this;
        }

        public Criteria andApplyTypeLessThanOrEqualTo(Integer value) {
            addCriterion("apply_type <=", value, "applyType");
            return (Criteria) this;
        }

        public Criteria andApplyTypeIn(List<Integer> values) {
            addCriterion("apply_type in", values, "applyType");
            return (Criteria) this;
        }

        public Criteria andApplyTypeNotIn(List<Integer> values) {
            addCriterion("apply_type not in", values, "applyType");
            return (Criteria) this;
        }

        public Criteria andApplyTypeBetween(Integer value1, Integer value2) {
            addCriterion("apply_type between", value1, value2, "applyType");
            return (Criteria) this;
        }

        public Criteria andApplyTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("apply_type not between", value1, value2, "applyType");
            return (Criteria) this;
        }

        public Criteria andReasonIsNull() {
            addCriterion("reason is null");
            return (Criteria) this;
        }

        public Criteria andReasonIsNotNull() {
            addCriterion("reason is not null");
            return (Criteria) this;
        }

        public Criteria andReasonEqualTo(String value) {
            addCriterion("reason =", value, "reason");
            return (Criteria) this;
        }

        public Criteria andReasonNotEqualTo(String value) {
            addCriterion("reason <>", value, "reason");
            return (Criteria) this;
        }

        public Criteria andReasonGreaterThan(String value) {
            addCriterion("reason >", value, "reason");
            return (Criteria) this;
        }

        public Criteria andReasonGreaterThanOrEqualTo(String value) {
            addCriterion("reason >=", value, "reason");
            return (Criteria) this;
        }

        public Criteria andReasonLessThan(String value) {
            addCriterion("reason <", value, "reason");
            return (Criteria) this;
        }

        public Criteria andReasonLessThanOrEqualTo(String value) {
            addCriterion("reason <=", value, "reason");
            return (Criteria) this;
        }

        public Criteria andReasonLike(String value) {
            addCriterion("reason like", value, "reason");
            return (Criteria) this;
        }

        public Criteria andReasonNotLike(String value) {
            addCriterion("reason not like", value, "reason");
            return (Criteria) this;
        }

        public Criteria andReasonIn(List<String> values) {
            addCriterion("reason in", values, "reason");
            return (Criteria) this;
        }

        public Criteria andReasonNotIn(List<String> values) {
            addCriterion("reason not in", values, "reason");
            return (Criteria) this;
        }

        public Criteria andReasonBetween(String value1, String value2) {
            addCriterion("reason between", value1, value2, "reason");
            return (Criteria) this;
        }

        public Criteria andReasonNotBetween(String value1, String value2) {
            addCriterion("reason not between", value1, value2, "reason");
            return (Criteria) this;
        }

        public Criteria andStampTypeIsNull() {
            addCriterion("stamp_type is null");
            return (Criteria) this;
        }

        public Criteria andStampTypeIsNotNull() {
            addCriterion("stamp_type is not null");
            return (Criteria) this;
        }

        public Criteria andStampTypeEqualTo(Integer value) {
            addCriterion("stamp_type =", value, "stampType");
            return (Criteria) this;
        }

        public Criteria andStampTypeNotEqualTo(Integer value) {
            addCriterion("stamp_type <>", value, "stampType");
            return (Criteria) this;
        }

        public Criteria andStampTypeGreaterThan(Integer value) {
            addCriterion("stamp_type >", value, "stampType");
            return (Criteria) this;
        }

        public Criteria andStampTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("stamp_type >=", value, "stampType");
            return (Criteria) this;
        }

        public Criteria andStampTypeLessThan(Integer value) {
            addCriterion("stamp_type <", value, "stampType");
            return (Criteria) this;
        }

        public Criteria andStampTypeLessThanOrEqualTo(Integer value) {
            addCriterion("stamp_type <=", value, "stampType");
            return (Criteria) this;
        }

        public Criteria andStampTypeIn(List<Integer> values) {
            addCriterion("stamp_type in", values, "stampType");
            return (Criteria) this;
        }

        public Criteria andStampTypeNotIn(List<Integer> values) {
            addCriterion("stamp_type not in", values, "stampType");
            return (Criteria) this;
        }

        public Criteria andStampTypeBetween(Integer value1, Integer value2) {
            addCriterion("stamp_type between", value1, value2, "stampType");
            return (Criteria) this;
        }

        public Criteria andStampTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("stamp_type not between", value1, value2, "stampType");
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

        public Criteria andBackDateIsNull() {
            addCriterion("back_date is null");
            return (Criteria) this;
        }

        public Criteria andBackDateIsNotNull() {
            addCriterion("back_date is not null");
            return (Criteria) this;
        }

        public Criteria andBackDateEqualTo(Date value) {
            addCriterion("back_date =", value, "backDate");
            return (Criteria) this;
        }

        public Criteria andBackDateNotEqualTo(Date value) {
            addCriterion("back_date <>", value, "backDate");
            return (Criteria) this;
        }

        public Criteria andBackDateGreaterThan(Date value) {
            addCriterion("back_date >", value, "backDate");
            return (Criteria) this;
        }

        public Criteria andBackDateGreaterThanOrEqualTo(Date value) {
            addCriterion("back_date >=", value, "backDate");
            return (Criteria) this;
        }

        public Criteria andBackDateLessThan(Date value) {
            addCriterion("back_date <", value, "backDate");
            return (Criteria) this;
        }

        public Criteria andBackDateLessThanOrEqualTo(Date value) {
            addCriterion("back_date <=", value, "backDate");
            return (Criteria) this;
        }

        public Criteria andBackDateIn(List<Date> values) {
            addCriterion("back_date in", values, "backDate");
            return (Criteria) this;
        }

        public Criteria andBackDateNotIn(List<Date> values) {
            addCriterion("back_date not in", values, "backDate");
            return (Criteria) this;
        }

        public Criteria andBackDateBetween(Date value1, Date value2) {
            addCriterion("back_date between", value1, value2, "backDate");
            return (Criteria) this;
        }

        public Criteria andBackDateNotBetween(Date value1, Date value2) {
            addCriterion("back_date not between", value1, value2, "backDate");
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
            addCriterion("status is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("status is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(Short value) {
            addCriterion("status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Short value) {
            addCriterion("status <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Short value) {
            addCriterion("status >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Short value) {
            addCriterion("status >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Short value) {
            addCriterion("status <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Short value) {
            addCriterion("status <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Short> values) {
            addCriterion("status in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<Short> values) {
            addCriterion("status not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Short value1, Short value2) {
            addCriterion("status between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Short value1, Short value2) {
            addCriterion("status not between", value1, value2, "status");
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