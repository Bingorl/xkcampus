package com.biu.wifi.campus.dao.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TeachingWeekCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private Integer limit;

    private Long offset;

    public TeachingWeekCriteria() {
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

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setOffset(Long offset) {
        this.offset = offset;
    }

    public Long getOffset() {
        return offset;
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

        public Criteria andSchoolIdIsNull() {
            addCriterion("school_id is null");
            return (Criteria) this;
        }

        public Criteria andSchoolIdIsNotNull() {
            addCriterion("school_id is not null");
            return (Criteria) this;
        }

        public Criteria andSchoolIdEqualTo(Integer value) {
            addCriterion("school_id =", value, "schoolId");
            return (Criteria) this;
        }

        public Criteria andSchoolIdNotEqualTo(Integer value) {
            addCriterion("school_id <>", value, "schoolId");
            return (Criteria) this;
        }

        public Criteria andSchoolIdGreaterThan(Integer value) {
            addCriterion("school_id >", value, "schoolId");
            return (Criteria) this;
        }

        public Criteria andSchoolIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("school_id >=", value, "schoolId");
            return (Criteria) this;
        }

        public Criteria andSchoolIdLessThan(Integer value) {
            addCriterion("school_id <", value, "schoolId");
            return (Criteria) this;
        }

        public Criteria andSchoolIdLessThanOrEqualTo(Integer value) {
            addCriterion("school_id <=", value, "schoolId");
            return (Criteria) this;
        }

        public Criteria andSchoolIdIn(List<Integer> values) {
            addCriterion("school_id in", values, "schoolId");
            return (Criteria) this;
        }

        public Criteria andSchoolIdNotIn(List<Integer> values) {
            addCriterion("school_id not in", values, "schoolId");
            return (Criteria) this;
        }

        public Criteria andSchoolIdBetween(Integer value1, Integer value2) {
            addCriterion("school_id between", value1, value2, "schoolId");
            return (Criteria) this;
        }

        public Criteria andSchoolIdNotBetween(Integer value1, Integer value2) {
            addCriterion("school_id not between", value1, value2, "schoolId");
            return (Criteria) this;
        }

        public Criteria andTermCodeIsNull() {
            addCriterion("term_code is null");
            return (Criteria) this;
        }

        public Criteria andTermCodeIsNotNull() {
            addCriterion("term_code is not null");
            return (Criteria) this;
        }

        public Criteria andTermCodeEqualTo(String value) {
            addCriterion("term_code =", value, "termCode");
            return (Criteria) this;
        }

        public Criteria andTermCodeNotEqualTo(String value) {
            addCriterion("term_code <>", value, "termCode");
            return (Criteria) this;
        }

        public Criteria andTermCodeGreaterThan(String value) {
            addCriterion("term_code >", value, "termCode");
            return (Criteria) this;
        }

        public Criteria andTermCodeGreaterThanOrEqualTo(String value) {
            addCriterion("term_code >=", value, "termCode");
            return (Criteria) this;
        }

        public Criteria andTermCodeLessThan(String value) {
            addCriterion("term_code <", value, "termCode");
            return (Criteria) this;
        }

        public Criteria andTermCodeLessThanOrEqualTo(String value) {
            addCriterion("term_code <=", value, "termCode");
            return (Criteria) this;
        }

        public Criteria andTermCodeLike(String value) {
            addCriterion("term_code like", value, "termCode");
            return (Criteria) this;
        }

        public Criteria andTermCodeNotLike(String value) {
            addCriterion("term_code not like", value, "termCode");
            return (Criteria) this;
        }

        public Criteria andTermCodeIn(List<String> values) {
            addCriterion("term_code in", values, "termCode");
            return (Criteria) this;
        }

        public Criteria andTermCodeNotIn(List<String> values) {
            addCriterion("term_code not in", values, "termCode");
            return (Criteria) this;
        }

        public Criteria andTermCodeBetween(String value1, String value2) {
            addCriterion("term_code between", value1, value2, "termCode");
            return (Criteria) this;
        }

        public Criteria andTermCodeNotBetween(String value1, String value2) {
            addCriterion("term_code not between", value1, value2, "termCode");
            return (Criteria) this;
        }

        public Criteria andTermNameIsNull() {
            addCriterion("term_name is null");
            return (Criteria) this;
        }

        public Criteria andTermNameIsNotNull() {
            addCriterion("term_name is not null");
            return (Criteria) this;
        }

        public Criteria andTermNameEqualTo(String value) {
            addCriterion("term_name =", value, "termName");
            return (Criteria) this;
        }

        public Criteria andTermNameNotEqualTo(String value) {
            addCriterion("term_name <>", value, "termName");
            return (Criteria) this;
        }

        public Criteria andTermNameGreaterThan(String value) {
            addCriterion("term_name >", value, "termName");
            return (Criteria) this;
        }

        public Criteria andTermNameGreaterThanOrEqualTo(String value) {
            addCriterion("term_name >=", value, "termName");
            return (Criteria) this;
        }

        public Criteria andTermNameLessThan(String value) {
            addCriterion("term_name <", value, "termName");
            return (Criteria) this;
        }

        public Criteria andTermNameLessThanOrEqualTo(String value) {
            addCriterion("term_name <=", value, "termName");
            return (Criteria) this;
        }

        public Criteria andTermNameLike(String value) {
            addCriterion("term_name like", value, "termName");
            return (Criteria) this;
        }

        public Criteria andTermNameNotLike(String value) {
            addCriterion("term_name not like", value, "termName");
            return (Criteria) this;
        }

        public Criteria andTermNameIn(List<String> values) {
            addCriterion("term_name in", values, "termName");
            return (Criteria) this;
        }

        public Criteria andTermNameNotIn(List<String> values) {
            addCriterion("term_name not in", values, "termName");
            return (Criteria) this;
        }

        public Criteria andTermNameBetween(String value1, String value2) {
            addCriterion("term_name between", value1, value2, "termName");
            return (Criteria) this;
        }

        public Criteria andTermNameNotBetween(String value1, String value2) {
            addCriterion("term_name not between", value1, value2, "termName");
            return (Criteria) this;
        }

        public Criteria andTermPeriodIsNull() {
            addCriterion("term_period is null");
            return (Criteria) this;
        }

        public Criteria andTermPeriodIsNotNull() {
            addCriterion("term_period is not null");
            return (Criteria) this;
        }

        public Criteria andTermPeriodEqualTo(String value) {
            addCriterion("term_period =", value, "termPeriod");
            return (Criteria) this;
        }

        public Criteria andTermPeriodNotEqualTo(String value) {
            addCriterion("term_period <>", value, "termPeriod");
            return (Criteria) this;
        }

        public Criteria andTermPeriodGreaterThan(String value) {
            addCriterion("term_period >", value, "termPeriod");
            return (Criteria) this;
        }

        public Criteria andTermPeriodGreaterThanOrEqualTo(String value) {
            addCriterion("term_period >=", value, "termPeriod");
            return (Criteria) this;
        }

        public Criteria andTermPeriodLessThan(String value) {
            addCriterion("term_period <", value, "termPeriod");
            return (Criteria) this;
        }

        public Criteria andTermPeriodLessThanOrEqualTo(String value) {
            addCriterion("term_period <=", value, "termPeriod");
            return (Criteria) this;
        }

        public Criteria andTermPeriodLike(String value) {
            addCriterion("term_period like", value, "termPeriod");
            return (Criteria) this;
        }

        public Criteria andTermPeriodNotLike(String value) {
            addCriterion("term_period not like", value, "termPeriod");
            return (Criteria) this;
        }

        public Criteria andTermPeriodIn(List<String> values) {
            addCriterion("term_period in", values, "termPeriod");
            return (Criteria) this;
        }

        public Criteria andTermPeriodNotIn(List<String> values) {
            addCriterion("term_period not in", values, "termPeriod");
            return (Criteria) this;
        }

        public Criteria andTermPeriodBetween(String value1, String value2) {
            addCriterion("term_period between", value1, value2, "termPeriod");
            return (Criteria) this;
        }

        public Criteria andTermPeriodNotBetween(String value1, String value2) {
            addCriterion("term_period not between", value1, value2, "termPeriod");
            return (Criteria) this;
        }

        public Criteria andWeekCountIsNull() {
            addCriterion("week_count is null");
            return (Criteria) this;
        }

        public Criteria andWeekCountIsNotNull() {
            addCriterion("week_count is not null");
            return (Criteria) this;
        }

        public Criteria andWeekCountEqualTo(Integer value) {
            addCriterion("week_count =", value, "weekCount");
            return (Criteria) this;
        }

        public Criteria andWeekCountNotEqualTo(Integer value) {
            addCriterion("week_count <>", value, "weekCount");
            return (Criteria) this;
        }

        public Criteria andWeekCountGreaterThan(Integer value) {
            addCriterion("week_count >", value, "weekCount");
            return (Criteria) this;
        }

        public Criteria andWeekCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("week_count >=", value, "weekCount");
            return (Criteria) this;
        }

        public Criteria andWeekCountLessThan(Integer value) {
            addCriterion("week_count <", value, "weekCount");
            return (Criteria) this;
        }

        public Criteria andWeekCountLessThanOrEqualTo(Integer value) {
            addCriterion("week_count <=", value, "weekCount");
            return (Criteria) this;
        }

        public Criteria andWeekCountIn(List<Integer> values) {
            addCriterion("week_count in", values, "weekCount");
            return (Criteria) this;
        }

        public Criteria andWeekCountNotIn(List<Integer> values) {
            addCriterion("week_count not in", values, "weekCount");
            return (Criteria) this;
        }

        public Criteria andWeekCountBetween(Integer value1, Integer value2) {
            addCriterion("week_count between", value1, value2, "weekCount");
            return (Criteria) this;
        }

        public Criteria andWeekCountNotBetween(Integer value1, Integer value2) {
            addCriterion("week_count not between", value1, value2, "weekCount");
            return (Criteria) this;
        }

        public Criteria andMondayDateIsNull() {
            addCriterion("monday_date is null");
            return (Criteria) this;
        }

        public Criteria andMondayDateIsNotNull() {
            addCriterion("monday_date is not null");
            return (Criteria) this;
        }

        public Criteria andMondayDateEqualTo(String value) {
            addCriterion("monday_date =", value, "mondayDate");
            return (Criteria) this;
        }

        public Criteria andMondayDateNotEqualTo(String value) {
            addCriterion("monday_date <>", value, "mondayDate");
            return (Criteria) this;
        }

        public Criteria andMondayDateGreaterThan(String value) {
            addCriterion("monday_date >", value, "mondayDate");
            return (Criteria) this;
        }

        public Criteria andMondayDateGreaterThanOrEqualTo(String value) {
            addCriterion("monday_date >=", value, "mondayDate");
            return (Criteria) this;
        }

        public Criteria andMondayDateLessThan(String value) {
            addCriterion("monday_date <", value, "mondayDate");
            return (Criteria) this;
        }

        public Criteria andMondayDateLessThanOrEqualTo(String value) {
            addCriterion("monday_date <=", value, "mondayDate");
            return (Criteria) this;
        }

        public Criteria andMondayDateLike(String value) {
            addCriterion("monday_date like", value, "mondayDate");
            return (Criteria) this;
        }

        public Criteria andMondayDateNotLike(String value) {
            addCriterion("monday_date not like", value, "mondayDate");
            return (Criteria) this;
        }

        public Criteria andMondayDateIn(List<String> values) {
            addCriterion("monday_date in", values, "mondayDate");
            return (Criteria) this;
        }

        public Criteria andMondayDateNotIn(List<String> values) {
            addCriterion("monday_date not in", values, "mondayDate");
            return (Criteria) this;
        }

        public Criteria andMondayDateBetween(String value1, String value2) {
            addCriterion("monday_date between", value1, value2, "mondayDate");
            return (Criteria) this;
        }

        public Criteria andMondayDateNotBetween(String value1, String value2) {
            addCriterion("monday_date not between", value1, value2, "mondayDate");
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
    }

    /**
     *
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