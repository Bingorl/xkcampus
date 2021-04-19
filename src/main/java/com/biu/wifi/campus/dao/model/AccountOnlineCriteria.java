package com.biu.wifi.campus.dao.model;

import java.util.ArrayList;
import java.util.List;

public class AccountOnlineCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public AccountOnlineCriteria() {
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

        public Criteria andAccountOnlineIdIsNull() {
            addCriterion("account_online_id is null");
            return (Criteria) this;
        }

        public Criteria andAccountOnlineIdIsNotNull() {
            addCriterion("account_online_id is not null");
            return (Criteria) this;
        }

        public Criteria andAccountOnlineIdEqualTo(Long value) {
            addCriterion("account_online_id =", value, "accountOnlineId");
            return (Criteria) this;
        }

        public Criteria andAccountOnlineIdNotEqualTo(Long value) {
            addCriterion("account_online_id <>", value, "accountOnlineId");
            return (Criteria) this;
        }

        public Criteria andAccountOnlineIdGreaterThan(Long value) {
            addCriterion("account_online_id >", value, "accountOnlineId");
            return (Criteria) this;
        }

        public Criteria andAccountOnlineIdGreaterThanOrEqualTo(Long value) {
            addCriterion("account_online_id >=", value, "accountOnlineId");
            return (Criteria) this;
        }

        public Criteria andAccountOnlineIdLessThan(Long value) {
            addCriterion("account_online_id <", value, "accountOnlineId");
            return (Criteria) this;
        }

        public Criteria andAccountOnlineIdLessThanOrEqualTo(Long value) {
            addCriterion("account_online_id <=", value, "accountOnlineId");
            return (Criteria) this;
        }

        public Criteria andAccountOnlineIdIn(List<Long> values) {
            addCriterion("account_online_id in", values, "accountOnlineId");
            return (Criteria) this;
        }

        public Criteria andAccountOnlineIdNotIn(List<Long> values) {
            addCriterion("account_online_id not in", values, "accountOnlineId");
            return (Criteria) this;
        }

        public Criteria andAccountOnlineIdBetween(Long value1, Long value2) {
            addCriterion("account_online_id between", value1, value2, "accountOnlineId");
            return (Criteria) this;
        }

        public Criteria andAccountOnlineIdNotBetween(Long value1, Long value2) {
            addCriterion("account_online_id not between", value1, value2, "accountOnlineId");
            return (Criteria) this;
        }

        public Criteria andAccountIdIsNull() {
            addCriterion("account_id is null");
            return (Criteria) this;
        }

        public Criteria andAccountIdIsNotNull() {
            addCriterion("account_id is not null");
            return (Criteria) this;
        }

        public Criteria andAccountIdEqualTo(Integer value) {
            addCriterion("account_id =", value, "accountId");
            return (Criteria) this;
        }

        public Criteria andAccountIdNotEqualTo(Integer value) {
            addCriterion("account_id <>", value, "accountId");
            return (Criteria) this;
        }

        public Criteria andAccountIdGreaterThan(Integer value) {
            addCriterion("account_id >", value, "accountId");
            return (Criteria) this;
        }

        public Criteria andAccountIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("account_id >=", value, "accountId");
            return (Criteria) this;
        }

        public Criteria andAccountIdLessThan(Integer value) {
            addCriterion("account_id <", value, "accountId");
            return (Criteria) this;
        }

        public Criteria andAccountIdLessThanOrEqualTo(Integer value) {
            addCriterion("account_id <=", value, "accountId");
            return (Criteria) this;
        }

        public Criteria andAccountIdIn(List<Integer> values) {
            addCriterion("account_id in", values, "accountId");
            return (Criteria) this;
        }

        public Criteria andAccountIdNotIn(List<Integer> values) {
            addCriterion("account_id not in", values, "accountId");
            return (Criteria) this;
        }

        public Criteria andAccountIdBetween(Integer value1, Integer value2) {
            addCriterion("account_id between", value1, value2, "accountId");
            return (Criteria) this;
        }

        public Criteria andAccountIdNotBetween(Integer value1, Integer value2) {
            addCriterion("account_id not between", value1, value2, "accountId");
            return (Criteria) this;
        }

        public Criteria andOnlineKeyIsNull() {
            addCriterion("online_key is null");
            return (Criteria) this;
        }

        public Criteria andOnlineKeyIsNotNull() {
            addCriterion("online_key is not null");
            return (Criteria) this;
        }

        public Criteria andOnlineKeyEqualTo(String value) {
            addCriterion("online_key =", value, "onlineKey");
            return (Criteria) this;
        }

        public Criteria andOnlineKeyNotEqualTo(String value) {
            addCriterion("online_key <>", value, "onlineKey");
            return (Criteria) this;
        }

        public Criteria andOnlineKeyGreaterThan(String value) {
            addCriterion("online_key >", value, "onlineKey");
            return (Criteria) this;
        }

        public Criteria andOnlineKeyGreaterThanOrEqualTo(String value) {
            addCriterion("online_key >=", value, "onlineKey");
            return (Criteria) this;
        }

        public Criteria andOnlineKeyLessThan(String value) {
            addCriterion("online_key <", value, "onlineKey");
            return (Criteria) this;
        }

        public Criteria andOnlineKeyLessThanOrEqualTo(String value) {
            addCriterion("online_key <=", value, "onlineKey");
            return (Criteria) this;
        }

        public Criteria andOnlineKeyLike(String value) {
            addCriterion("online_key like", value, "onlineKey");
            return (Criteria) this;
        }

        public Criteria andOnlineKeyNotLike(String value) {
            addCriterion("online_key not like", value, "onlineKey");
            return (Criteria) this;
        }

        public Criteria andOnlineKeyIn(List<String> values) {
            addCriterion("online_key in", values, "onlineKey");
            return (Criteria) this;
        }

        public Criteria andOnlineKeyNotIn(List<String> values) {
            addCriterion("online_key not in", values, "onlineKey");
            return (Criteria) this;
        }

        public Criteria andOnlineKeyBetween(String value1, String value2) {
            addCriterion("online_key between", value1, value2, "onlineKey");
            return (Criteria) this;
        }

        public Criteria andOnlineKeyNotBetween(String value1, String value2) {
            addCriterion("online_key not between", value1, value2, "onlineKey");
            return (Criteria) this;
        }

        public Criteria andEdatetimeIsNull() {
            addCriterion("edatetime is null");
            return (Criteria) this;
        }

        public Criteria andEdatetimeIsNotNull() {
            addCriterion("edatetime is not null");
            return (Criteria) this;
        }

        public Criteria andEdatetimeEqualTo(Long value) {
            addCriterion("edatetime =", value, "edatetime");
            return (Criteria) this;
        }

        public Criteria andEdatetimeNotEqualTo(Long value) {
            addCriterion("edatetime <>", value, "edatetime");
            return (Criteria) this;
        }

        public Criteria andEdatetimeGreaterThan(Long value) {
            addCriterion("edatetime >", value, "edatetime");
            return (Criteria) this;
        }

        public Criteria andEdatetimeGreaterThanOrEqualTo(Long value) {
            addCriterion("edatetime >=", value, "edatetime");
            return (Criteria) this;
        }

        public Criteria andEdatetimeLessThan(Long value) {
            addCriterion("edatetime <", value, "edatetime");
            return (Criteria) this;
        }

        public Criteria andEdatetimeLessThanOrEqualTo(Long value) {
            addCriterion("edatetime <=", value, "edatetime");
            return (Criteria) this;
        }

        public Criteria andEdatetimeIn(List<Long> values) {
            addCriterion("edatetime in", values, "edatetime");
            return (Criteria) this;
        }

        public Criteria andEdatetimeNotIn(List<Long> values) {
            addCriterion("edatetime not in", values, "edatetime");
            return (Criteria) this;
        }

        public Criteria andEdatetimeBetween(Long value1, Long value2) {
            addCriterion("edatetime between", value1, value2, "edatetime");
            return (Criteria) this;
        }

        public Criteria andEdatetimeNotBetween(Long value1, Long value2) {
            addCriterion("edatetime not between", value1, value2, "edatetime");
            return (Criteria) this;
        }

        public Criteria andSdatetimeIsNull() {
            addCriterion("sdatetime is null");
            return (Criteria) this;
        }

        public Criteria andSdatetimeIsNotNull() {
            addCriterion("sdatetime is not null");
            return (Criteria) this;
        }

        public Criteria andSdatetimeEqualTo(Long value) {
            addCriterion("sdatetime =", value, "sdatetime");
            return (Criteria) this;
        }

        public Criteria andSdatetimeNotEqualTo(Long value) {
            addCriterion("sdatetime <>", value, "sdatetime");
            return (Criteria) this;
        }

        public Criteria andSdatetimeGreaterThan(Long value) {
            addCriterion("sdatetime >", value, "sdatetime");
            return (Criteria) this;
        }

        public Criteria andSdatetimeGreaterThanOrEqualTo(Long value) {
            addCriterion("sdatetime >=", value, "sdatetime");
            return (Criteria) this;
        }

        public Criteria andSdatetimeLessThan(Long value) {
            addCriterion("sdatetime <", value, "sdatetime");
            return (Criteria) this;
        }

        public Criteria andSdatetimeLessThanOrEqualTo(Long value) {
            addCriterion("sdatetime <=", value, "sdatetime");
            return (Criteria) this;
        }

        public Criteria andSdatetimeIn(List<Long> values) {
            addCriterion("sdatetime in", values, "sdatetime");
            return (Criteria) this;
        }

        public Criteria andSdatetimeNotIn(List<Long> values) {
            addCriterion("sdatetime not in", values, "sdatetime");
            return (Criteria) this;
        }

        public Criteria andSdatetimeBetween(Long value1, Long value2) {
            addCriterion("sdatetime between", value1, value2, "sdatetime");
            return (Criteria) this;
        }

        public Criteria andSdatetimeNotBetween(Long value1, Long value2) {
            addCriterion("sdatetime not between", value1, value2, "sdatetime");
            return (Criteria) this;
        }

        public Criteria andTypeIsNull() {
            addCriterion("type is null");
            return (Criteria) this;
        }

        public Criteria andTypeIsNotNull() {
            addCriterion("type is not null");
            return (Criteria) this;
        }

        public Criteria andTypeEqualTo(Short value) {
            addCriterion("type =", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotEqualTo(Short value) {
            addCriterion("type <>", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThan(Short value) {
            addCriterion("type >", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThanOrEqualTo(Short value) {
            addCriterion("type >=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThan(Short value) {
            addCriterion("type <", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThanOrEqualTo(Short value) {
            addCriterion("type <=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeIn(List<Short> values) {
            addCriterion("type in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotIn(List<Short> values) {
            addCriterion("type not in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeBetween(Short value1, Short value2) {
            addCriterion("type between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotBetween(Short value1, Short value2) {
            addCriterion("type not between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andLengthenIsNull() {
            addCriterion("lengthen is null");
            return (Criteria) this;
        }

        public Criteria andLengthenIsNotNull() {
            addCriterion("lengthen is not null");
            return (Criteria) this;
        }

        public Criteria andLengthenEqualTo(Integer value) {
            addCriterion("lengthen =", value, "lengthen");
            return (Criteria) this;
        }

        public Criteria andLengthenNotEqualTo(Integer value) {
            addCriterion("lengthen <>", value, "lengthen");
            return (Criteria) this;
        }

        public Criteria andLengthenGreaterThan(Integer value) {
            addCriterion("lengthen >", value, "lengthen");
            return (Criteria) this;
        }

        public Criteria andLengthenGreaterThanOrEqualTo(Integer value) {
            addCriterion("lengthen >=", value, "lengthen");
            return (Criteria) this;
        }

        public Criteria andLengthenLessThan(Integer value) {
            addCriterion("lengthen <", value, "lengthen");
            return (Criteria) this;
        }

        public Criteria andLengthenLessThanOrEqualTo(Integer value) {
            addCriterion("lengthen <=", value, "lengthen");
            return (Criteria) this;
        }

        public Criteria andLengthenIn(List<Integer> values) {
            addCriterion("lengthen in", values, "lengthen");
            return (Criteria) this;
        }

        public Criteria andLengthenNotIn(List<Integer> values) {
            addCriterion("lengthen not in", values, "lengthen");
            return (Criteria) this;
        }

        public Criteria andLengthenBetween(Integer value1, Integer value2) {
            addCriterion("lengthen between", value1, value2, "lengthen");
            return (Criteria) this;
        }

        public Criteria andLengthenNotBetween(Integer value1, Integer value2) {
            addCriterion("lengthen not between", value1, value2, "lengthen");
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