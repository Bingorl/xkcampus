package com.biu.wifi.campus.dao.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FollowCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public FollowCriteria() {
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

        public Criteria andFollowerIdIsNull() {
            addCriterion("follower_id is null");
            return (Criteria) this;
        }

        public Criteria andFollowerIdIsNotNull() {
            addCriterion("follower_id is not null");
            return (Criteria) this;
        }

        public Criteria andFollowerIdEqualTo(Integer value) {
            addCriterion("follower_id =", value, "followerId");
            return (Criteria) this;
        }

        public Criteria andFollowerIdNotEqualTo(Integer value) {
            addCriterion("follower_id <>", value, "followerId");
            return (Criteria) this;
        }

        public Criteria andFollowerIdGreaterThan(Integer value) {
            addCriterion("follower_id >", value, "followerId");
            return (Criteria) this;
        }

        public Criteria andFollowerIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("follower_id >=", value, "followerId");
            return (Criteria) this;
        }

        public Criteria andFollowerIdLessThan(Integer value) {
            addCriterion("follower_id <", value, "followerId");
            return (Criteria) this;
        }

        public Criteria andFollowerIdLessThanOrEqualTo(Integer value) {
            addCriterion("follower_id <=", value, "followerId");
            return (Criteria) this;
        }

        public Criteria andFollowerIdIn(List<Integer> values) {
            addCriterion("follower_id in", values, "followerId");
            return (Criteria) this;
        }

        public Criteria andFollowerIdNotIn(List<Integer> values) {
            addCriterion("follower_id not in", values, "followerId");
            return (Criteria) this;
        }

        public Criteria andFollowerIdBetween(Integer value1, Integer value2) {
            addCriterion("follower_id between", value1, value2, "followerId");
            return (Criteria) this;
        }

        public Criteria andFollowerIdNotBetween(Integer value1, Integer value2) {
            addCriterion("follower_id not between", value1, value2, "followerId");
            return (Criteria) this;
        }

        public Criteria andBeFollowedIdIsNull() {
            addCriterion("be_followed_id is null");
            return (Criteria) this;
        }

        public Criteria andBeFollowedIdIsNotNull() {
            addCriterion("be_followed_id is not null");
            return (Criteria) this;
        }

        public Criteria andBeFollowedIdEqualTo(Integer value) {
            addCriterion("be_followed_id =", value, "beFollowedId");
            return (Criteria) this;
        }

        public Criteria andBeFollowedIdNotEqualTo(Integer value) {
            addCriterion("be_followed_id <>", value, "beFollowedId");
            return (Criteria) this;
        }

        public Criteria andBeFollowedIdGreaterThan(Integer value) {
            addCriterion("be_followed_id >", value, "beFollowedId");
            return (Criteria) this;
        }

        public Criteria andBeFollowedIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("be_followed_id >=", value, "beFollowedId");
            return (Criteria) this;
        }

        public Criteria andBeFollowedIdLessThan(Integer value) {
            addCriterion("be_followed_id <", value, "beFollowedId");
            return (Criteria) this;
        }

        public Criteria andBeFollowedIdLessThanOrEqualTo(Integer value) {
            addCriterion("be_followed_id <=", value, "beFollowedId");
            return (Criteria) this;
        }

        public Criteria andBeFollowedIdIn(List<Integer> values) {
            addCriterion("be_followed_id in", values, "beFollowedId");
            return (Criteria) this;
        }

        public Criteria andBeFollowedIdNotIn(List<Integer> values) {
            addCriterion("be_followed_id not in", values, "beFollowedId");
            return (Criteria) this;
        }

        public Criteria andBeFollowedIdBetween(Integer value1, Integer value2) {
            addCriterion("be_followed_id between", value1, value2, "beFollowedId");
            return (Criteria) this;
        }

        public Criteria andBeFollowedIdNotBetween(Integer value1, Integer value2) {
            addCriterion("be_followed_id not between", value1, value2, "beFollowedId");
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

        public Criteria andIsTwoWayIsNull() {
            addCriterion("is_two_way is null");
            return (Criteria) this;
        }

        public Criteria andIsTwoWayIsNotNull() {
            addCriterion("is_two_way is not null");
            return (Criteria) this;
        }

        public Criteria andIsTwoWayEqualTo(Short value) {
            addCriterion("is_two_way =", value, "isTwoWay");
            return (Criteria) this;
        }

        public Criteria andIsTwoWayNotEqualTo(Short value) {
            addCriterion("is_two_way <>", value, "isTwoWay");
            return (Criteria) this;
        }

        public Criteria andIsTwoWayGreaterThan(Short value) {
            addCriterion("is_two_way >", value, "isTwoWay");
            return (Criteria) this;
        }

        public Criteria andIsTwoWayGreaterThanOrEqualTo(Short value) {
            addCriterion("is_two_way >=", value, "isTwoWay");
            return (Criteria) this;
        }

        public Criteria andIsTwoWayLessThan(Short value) {
            addCriterion("is_two_way <", value, "isTwoWay");
            return (Criteria) this;
        }

        public Criteria andIsTwoWayLessThanOrEqualTo(Short value) {
            addCriterion("is_two_way <=", value, "isTwoWay");
            return (Criteria) this;
        }

        public Criteria andIsTwoWayIn(List<Short> values) {
            addCriterion("is_two_way in", values, "isTwoWay");
            return (Criteria) this;
        }

        public Criteria andIsTwoWayNotIn(List<Short> values) {
            addCriterion("is_two_way not in", values, "isTwoWay");
            return (Criteria) this;
        }

        public Criteria andIsTwoWayBetween(Short value1, Short value2) {
            addCriterion("is_two_way between", value1, value2, "isTwoWay");
            return (Criteria) this;
        }

        public Criteria andIsTwoWayNotBetween(Short value1, Short value2) {
            addCriterion("is_two_way not between", value1, value2, "isTwoWay");
            return (Criteria) this;
        }

        public Criteria andIsCancelIsNull() {
            addCriterion("is_cancel is null");
            return (Criteria) this;
        }

        public Criteria andIsCancelIsNotNull() {
            addCriterion("is_cancel is not null");
            return (Criteria) this;
        }

        public Criteria andIsCancelEqualTo(Short value) {
            addCriterion("is_cancel =", value, "isCancel");
            return (Criteria) this;
        }

        public Criteria andIsCancelNotEqualTo(Short value) {
            addCriterion("is_cancel <>", value, "isCancel");
            return (Criteria) this;
        }

        public Criteria andIsCancelGreaterThan(Short value) {
            addCriterion("is_cancel >", value, "isCancel");
            return (Criteria) this;
        }

        public Criteria andIsCancelGreaterThanOrEqualTo(Short value) {
            addCriterion("is_cancel >=", value, "isCancel");
            return (Criteria) this;
        }

        public Criteria andIsCancelLessThan(Short value) {
            addCriterion("is_cancel <", value, "isCancel");
            return (Criteria) this;
        }

        public Criteria andIsCancelLessThanOrEqualTo(Short value) {
            addCriterion("is_cancel <=", value, "isCancel");
            return (Criteria) this;
        }

        public Criteria andIsCancelIn(List<Short> values) {
            addCriterion("is_cancel in", values, "isCancel");
            return (Criteria) this;
        }

        public Criteria andIsCancelNotIn(List<Short> values) {
            addCriterion("is_cancel not in", values, "isCancel");
            return (Criteria) this;
        }

        public Criteria andIsCancelBetween(Short value1, Short value2) {
            addCriterion("is_cancel between", value1, value2, "isCancel");
            return (Criteria) this;
        }

        public Criteria andIsCancelNotBetween(Short value1, Short value2) {
            addCriterion("is_cancel not between", value1, value2, "isCancel");
            return (Criteria) this;
        }

        public Criteria andCancelTimeIsNull() {
            addCriterion("cancel_time is null");
            return (Criteria) this;
        }

        public Criteria andCancelTimeIsNotNull() {
            addCriterion("cancel_time is not null");
            return (Criteria) this;
        }

        public Criteria andCancelTimeEqualTo(Date value) {
            addCriterion("cancel_time =", value, "cancelTime");
            return (Criteria) this;
        }

        public Criteria andCancelTimeNotEqualTo(Date value) {
            addCriterion("cancel_time <>", value, "cancelTime");
            return (Criteria) this;
        }

        public Criteria andCancelTimeGreaterThan(Date value) {
            addCriterion("cancel_time >", value, "cancelTime");
            return (Criteria) this;
        }

        public Criteria andCancelTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("cancel_time >=", value, "cancelTime");
            return (Criteria) this;
        }

        public Criteria andCancelTimeLessThan(Date value) {
            addCriterion("cancel_time <", value, "cancelTime");
            return (Criteria) this;
        }

        public Criteria andCancelTimeLessThanOrEqualTo(Date value) {
            addCriterion("cancel_time <=", value, "cancelTime");
            return (Criteria) this;
        }

        public Criteria andCancelTimeIn(List<Date> values) {
            addCriterion("cancel_time in", values, "cancelTime");
            return (Criteria) this;
        }

        public Criteria andCancelTimeNotIn(List<Date> values) {
            addCriterion("cancel_time not in", values, "cancelTime");
            return (Criteria) this;
        }

        public Criteria andCancelTimeBetween(Date value1, Date value2) {
            addCriterion("cancel_time between", value1, value2, "cancelTime");
            return (Criteria) this;
        }

        public Criteria andCancelTimeNotBetween(Date value1, Date value2) {
            addCriterion("cancel_time not between", value1, value2, "cancelTime");
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