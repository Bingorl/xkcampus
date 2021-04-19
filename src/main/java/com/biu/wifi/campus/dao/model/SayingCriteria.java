package com.biu.wifi.campus.dao.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SayingCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public SayingCriteria() {
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

        public Criteria andCreatorIdIsNull() {
            addCriterion("creator_id is null");
            return (Criteria) this;
        }

        public Criteria andCreatorIdIsNotNull() {
            addCriterion("creator_id is not null");
            return (Criteria) this;
        }

        public Criteria andCreatorIdEqualTo(Integer value) {
            addCriterion("creator_id =", value, "creatorId");
            return (Criteria) this;
        }

        public Criteria andCreatorIdNotEqualTo(Integer value) {
            addCriterion("creator_id <>", value, "creatorId");
            return (Criteria) this;
        }

        public Criteria andCreatorIdGreaterThan(Integer value) {
            addCriterion("creator_id >", value, "creatorId");
            return (Criteria) this;
        }

        public Criteria andCreatorIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("creator_id >=", value, "creatorId");
            return (Criteria) this;
        }

        public Criteria andCreatorIdLessThan(Integer value) {
            addCriterion("creator_id <", value, "creatorId");
            return (Criteria) this;
        }

        public Criteria andCreatorIdLessThanOrEqualTo(Integer value) {
            addCriterion("creator_id <=", value, "creatorId");
            return (Criteria) this;
        }

        public Criteria andCreatorIdIn(List<Integer> values) {
            addCriterion("creator_id in", values, "creatorId");
            return (Criteria) this;
        }

        public Criteria andCreatorIdNotIn(List<Integer> values) {
            addCriterion("creator_id not in", values, "creatorId");
            return (Criteria) this;
        }

        public Criteria andCreatorIdBetween(Integer value1, Integer value2) {
            addCriterion("creator_id between", value1, value2, "creatorId");
            return (Criteria) this;
        }

        public Criteria andCreatorIdNotBetween(Integer value1, Integer value2) {
            addCriterion("creator_id not between", value1, value2, "creatorId");
            return (Criteria) this;
        }

        public Criteria andAtUserIsNull() {
            addCriterion("at_user is null");
            return (Criteria) this;
        }

        public Criteria andAtUserIsNotNull() {
            addCriterion("at_user is not null");
            return (Criteria) this;
        }

        public Criteria andAtUserEqualTo(String value) {
            addCriterion("at_user =", value, "atUser");
            return (Criteria) this;
        }

        public Criteria andAtUserNotEqualTo(String value) {
            addCriterion("at_user <>", value, "atUser");
            return (Criteria) this;
        }

        public Criteria andAtUserGreaterThan(String value) {
            addCriterion("at_user >", value, "atUser");
            return (Criteria) this;
        }

        public Criteria andAtUserGreaterThanOrEqualTo(String value) {
            addCriterion("at_user >=", value, "atUser");
            return (Criteria) this;
        }

        public Criteria andAtUserLessThan(String value) {
            addCriterion("at_user <", value, "atUser");
            return (Criteria) this;
        }

        public Criteria andAtUserLessThanOrEqualTo(String value) {
            addCriterion("at_user <=", value, "atUser");
            return (Criteria) this;
        }

        public Criteria andAtUserLike(String value) {
            addCriterion("at_user like", value, "atUser");
            return (Criteria) this;
        }

        public Criteria andAtUserNotLike(String value) {
            addCriterion("at_user not like", value, "atUser");
            return (Criteria) this;
        }

        public Criteria andAtUserIn(List<String> values) {
            addCriterion("at_user in", values, "atUser");
            return (Criteria) this;
        }

        public Criteria andAtUserNotIn(List<String> values) {
            addCriterion("at_user not in", values, "atUser");
            return (Criteria) this;
        }

        public Criteria andAtUserBetween(String value1, String value2) {
            addCriterion("at_user between", value1, value2, "atUser");
            return (Criteria) this;
        }

        public Criteria andAtUserNotBetween(String value1, String value2) {
            addCriterion("at_user not between", value1, value2, "atUser");
            return (Criteria) this;
        }

        public Criteria andImgIsNull() {
            addCriterion("img is null");
            return (Criteria) this;
        }

        public Criteria andImgIsNotNull() {
            addCriterion("img is not null");
            return (Criteria) this;
        }

        public Criteria andImgEqualTo(String value) {
            addCriterion("img =", value, "img");
            return (Criteria) this;
        }

        public Criteria andImgNotEqualTo(String value) {
            addCriterion("img <>", value, "img");
            return (Criteria) this;
        }

        public Criteria andImgGreaterThan(String value) {
            addCriterion("img >", value, "img");
            return (Criteria) this;
        }

        public Criteria andImgGreaterThanOrEqualTo(String value) {
            addCriterion("img >=", value, "img");
            return (Criteria) this;
        }

        public Criteria andImgLessThan(String value) {
            addCriterion("img <", value, "img");
            return (Criteria) this;
        }

        public Criteria andImgLessThanOrEqualTo(String value) {
            addCriterion("img <=", value, "img");
            return (Criteria) this;
        }

        public Criteria andImgLike(String value) {
            addCriterion("img like", value, "img");
            return (Criteria) this;
        }

        public Criteria andImgNotLike(String value) {
            addCriterion("img not like", value, "img");
            return (Criteria) this;
        }

        public Criteria andImgIn(List<String> values) {
            addCriterion("img in", values, "img");
            return (Criteria) this;
        }

        public Criteria andImgNotIn(List<String> values) {
            addCriterion("img not in", values, "img");
            return (Criteria) this;
        }

        public Criteria andImgBetween(String value1, String value2) {
            addCriterion("img between", value1, value2, "img");
            return (Criteria) this;
        }

        public Criteria andImgNotBetween(String value1, String value2) {
            addCriterion("img not between", value1, value2, "img");
            return (Criteria) this;
        }

        public Criteria andWatchCountIsNull() {
            addCriterion("watch_count is null");
            return (Criteria) this;
        }

        public Criteria andWatchCountIsNotNull() {
            addCriterion("watch_count is not null");
            return (Criteria) this;
        }

        public Criteria andWatchCountEqualTo(Integer value) {
            addCriterion("watch_count =", value, "watchCount");
            return (Criteria) this;
        }

        public Criteria andWatchCountNotEqualTo(Integer value) {
            addCriterion("watch_count <>", value, "watchCount");
            return (Criteria) this;
        }

        public Criteria andWatchCountGreaterThan(Integer value) {
            addCriterion("watch_count >", value, "watchCount");
            return (Criteria) this;
        }

        public Criteria andWatchCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("watch_count >=", value, "watchCount");
            return (Criteria) this;
        }

        public Criteria andWatchCountLessThan(Integer value) {
            addCriterion("watch_count <", value, "watchCount");
            return (Criteria) this;
        }

        public Criteria andWatchCountLessThanOrEqualTo(Integer value) {
            addCriterion("watch_count <=", value, "watchCount");
            return (Criteria) this;
        }

        public Criteria andWatchCountIn(List<Integer> values) {
            addCriterion("watch_count in", values, "watchCount");
            return (Criteria) this;
        }

        public Criteria andWatchCountNotIn(List<Integer> values) {
            addCriterion("watch_count not in", values, "watchCount");
            return (Criteria) this;
        }

        public Criteria andWatchCountBetween(Integer value1, Integer value2) {
            addCriterion("watch_count between", value1, value2, "watchCount");
            return (Criteria) this;
        }

        public Criteria andWatchCountNotBetween(Integer value1, Integer value2) {
            addCriterion("watch_count not between", value1, value2, "watchCount");
            return (Criteria) this;
        }

        public Criteria andPrivacyIsNull() {
            addCriterion("privacy is null");
            return (Criteria) this;
        }

        public Criteria andPrivacyIsNotNull() {
            addCriterion("privacy is not null");
            return (Criteria) this;
        }

        public Criteria andPrivacyEqualTo(Short value) {
            addCriterion("privacy =", value, "privacy");
            return (Criteria) this;
        }

        public Criteria andPrivacyNotEqualTo(Short value) {
            addCriterion("privacy <>", value, "privacy");
            return (Criteria) this;
        }

        public Criteria andPrivacyGreaterThan(Short value) {
            addCriterion("privacy >", value, "privacy");
            return (Criteria) this;
        }

        public Criteria andPrivacyGreaterThanOrEqualTo(Short value) {
            addCriterion("privacy >=", value, "privacy");
            return (Criteria) this;
        }

        public Criteria andPrivacyLessThan(Short value) {
            addCriterion("privacy <", value, "privacy");
            return (Criteria) this;
        }

        public Criteria andPrivacyLessThanOrEqualTo(Short value) {
            addCriterion("privacy <=", value, "privacy");
            return (Criteria) this;
        }

        public Criteria andPrivacyIn(List<Short> values) {
            addCriterion("privacy in", values, "privacy");
            return (Criteria) this;
        }

        public Criteria andPrivacyNotIn(List<Short> values) {
            addCriterion("privacy not in", values, "privacy");
            return (Criteria) this;
        }

        public Criteria andPrivacyBetween(Short value1, Short value2) {
            addCriterion("privacy between", value1, value2, "privacy");
            return (Criteria) this;
        }

        public Criteria andPrivacyNotBetween(Short value1, Short value2) {
            addCriterion("privacy not between", value1, value2, "privacy");
            return (Criteria) this;
        }

        public Criteria andIsForwardIsNull() {
            addCriterion("is_forward is null");
            return (Criteria) this;
        }

        public Criteria andIsForwardIsNotNull() {
            addCriterion("is_forward is not null");
            return (Criteria) this;
        }

        public Criteria andIsForwardEqualTo(Short value) {
            addCriterion("is_forward =", value, "isForward");
            return (Criteria) this;
        }

        public Criteria andIsForwardNotEqualTo(Short value) {
            addCriterion("is_forward <>", value, "isForward");
            return (Criteria) this;
        }

        public Criteria andIsForwardGreaterThan(Short value) {
            addCriterion("is_forward >", value, "isForward");
            return (Criteria) this;
        }

        public Criteria andIsForwardGreaterThanOrEqualTo(Short value) {
            addCriterion("is_forward >=", value, "isForward");
            return (Criteria) this;
        }

        public Criteria andIsForwardLessThan(Short value) {
            addCriterion("is_forward <", value, "isForward");
            return (Criteria) this;
        }

        public Criteria andIsForwardLessThanOrEqualTo(Short value) {
            addCriterion("is_forward <=", value, "isForward");
            return (Criteria) this;
        }

        public Criteria andIsForwardIn(List<Short> values) {
            addCriterion("is_forward in", values, "isForward");
            return (Criteria) this;
        }

        public Criteria andIsForwardNotIn(List<Short> values) {
            addCriterion("is_forward not in", values, "isForward");
            return (Criteria) this;
        }

        public Criteria andIsForwardBetween(Short value1, Short value2) {
            addCriterion("is_forward between", value1, value2, "isForward");
            return (Criteria) this;
        }

        public Criteria andIsForwardNotBetween(Short value1, Short value2) {
            addCriterion("is_forward not between", value1, value2, "isForward");
            return (Criteria) this;
        }

        public Criteria andOriginTypeIsNull() {
            addCriterion("origin_type is null");
            return (Criteria) this;
        }

        public Criteria andOriginTypeIsNotNull() {
            addCriterion("origin_type is not null");
            return (Criteria) this;
        }

        public Criteria andOriginTypeEqualTo(Short value) {
            addCriterion("origin_type =", value, "originType");
            return (Criteria) this;
        }

        public Criteria andOriginTypeNotEqualTo(Short value) {
            addCriterion("origin_type <>", value, "originType");
            return (Criteria) this;
        }

        public Criteria andOriginTypeGreaterThan(Short value) {
            addCriterion("origin_type >", value, "originType");
            return (Criteria) this;
        }

        public Criteria andOriginTypeGreaterThanOrEqualTo(Short value) {
            addCriterion("origin_type >=", value, "originType");
            return (Criteria) this;
        }

        public Criteria andOriginTypeLessThan(Short value) {
            addCriterion("origin_type <", value, "originType");
            return (Criteria) this;
        }

        public Criteria andOriginTypeLessThanOrEqualTo(Short value) {
            addCriterion("origin_type <=", value, "originType");
            return (Criteria) this;
        }

        public Criteria andOriginTypeIn(List<Short> values) {
            addCriterion("origin_type in", values, "originType");
            return (Criteria) this;
        }

        public Criteria andOriginTypeNotIn(List<Short> values) {
            addCriterion("origin_type not in", values, "originType");
            return (Criteria) this;
        }

        public Criteria andOriginTypeBetween(Short value1, Short value2) {
            addCriterion("origin_type between", value1, value2, "originType");
            return (Criteria) this;
        }

        public Criteria andOriginTypeNotBetween(Short value1, Short value2) {
            addCriterion("origin_type not between", value1, value2, "originType");
            return (Criteria) this;
        }

        public Criteria andOriginIdIsNull() {
            addCriterion("origin_id is null");
            return (Criteria) this;
        }

        public Criteria andOriginIdIsNotNull() {
            addCriterion("origin_id is not null");
            return (Criteria) this;
        }

        public Criteria andOriginIdEqualTo(Integer value) {
            addCriterion("origin_id =", value, "originId");
            return (Criteria) this;
        }

        public Criteria andOriginIdNotEqualTo(Integer value) {
            addCriterion("origin_id <>", value, "originId");
            return (Criteria) this;
        }

        public Criteria andOriginIdGreaterThan(Integer value) {
            addCriterion("origin_id >", value, "originId");
            return (Criteria) this;
        }

        public Criteria andOriginIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("origin_id >=", value, "originId");
            return (Criteria) this;
        }

        public Criteria andOriginIdLessThan(Integer value) {
            addCriterion("origin_id <", value, "originId");
            return (Criteria) this;
        }

        public Criteria andOriginIdLessThanOrEqualTo(Integer value) {
            addCriterion("origin_id <=", value, "originId");
            return (Criteria) this;
        }

        public Criteria andOriginIdIn(List<Integer> values) {
            addCriterion("origin_id in", values, "originId");
            return (Criteria) this;
        }

        public Criteria andOriginIdNotIn(List<Integer> values) {
            addCriterion("origin_id not in", values, "originId");
            return (Criteria) this;
        }

        public Criteria andOriginIdBetween(Integer value1, Integer value2) {
            addCriterion("origin_id between", value1, value2, "originId");
            return (Criteria) this;
        }

        public Criteria andOriginIdNotBetween(Integer value1, Integer value2) {
            addCriterion("origin_id not between", value1, value2, "originId");
            return (Criteria) this;
        }

        public Criteria andOriginSayingIdIsNull() {
            addCriterion("origin_saying_id is null");
            return (Criteria) this;
        }

        public Criteria andOriginSayingIdIsNotNull() {
            addCriterion("origin_saying_id is not null");
            return (Criteria) this;
        }

        public Criteria andOriginSayingIdEqualTo(Integer value) {
            addCriterion("origin_saying_id =", value, "originSayingId");
            return (Criteria) this;
        }

        public Criteria andOriginSayingIdNotEqualTo(Integer value) {
            addCriterion("origin_saying_id <>", value, "originSayingId");
            return (Criteria) this;
        }

        public Criteria andOriginSayingIdGreaterThan(Integer value) {
            addCriterion("origin_saying_id >", value, "originSayingId");
            return (Criteria) this;
        }

        public Criteria andOriginSayingIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("origin_saying_id >=", value, "originSayingId");
            return (Criteria) this;
        }

        public Criteria andOriginSayingIdLessThan(Integer value) {
            addCriterion("origin_saying_id <", value, "originSayingId");
            return (Criteria) this;
        }

        public Criteria andOriginSayingIdLessThanOrEqualTo(Integer value) {
            addCriterion("origin_saying_id <=", value, "originSayingId");
            return (Criteria) this;
        }

        public Criteria andOriginSayingIdIn(List<Integer> values) {
            addCriterion("origin_saying_id in", values, "originSayingId");
            return (Criteria) this;
        }

        public Criteria andOriginSayingIdNotIn(List<Integer> values) {
            addCriterion("origin_saying_id not in", values, "originSayingId");
            return (Criteria) this;
        }

        public Criteria andOriginSayingIdBetween(Integer value1, Integer value2) {
            addCriterion("origin_saying_id between", value1, value2, "originSayingId");
            return (Criteria) this;
        }

        public Criteria andOriginSayingIdNotBetween(Integer value1, Integer value2) {
            addCriterion("origin_saying_id not between", value1, value2, "originSayingId");
            return (Criteria) this;
        }

        public Criteria andForwardNumberIsNull() {
            addCriterion("forward_number is null");
            return (Criteria) this;
        }

        public Criteria andForwardNumberIsNotNull() {
            addCriterion("forward_number is not null");
            return (Criteria) this;
        }

        public Criteria andForwardNumberEqualTo(Integer value) {
            addCriterion("forward_number =", value, "forwardNumber");
            return (Criteria) this;
        }

        public Criteria andForwardNumberNotEqualTo(Integer value) {
            addCriterion("forward_number <>", value, "forwardNumber");
            return (Criteria) this;
        }

        public Criteria andForwardNumberGreaterThan(Integer value) {
            addCriterion("forward_number >", value, "forwardNumber");
            return (Criteria) this;
        }

        public Criteria andForwardNumberGreaterThanOrEqualTo(Integer value) {
            addCriterion("forward_number >=", value, "forwardNumber");
            return (Criteria) this;
        }

        public Criteria andForwardNumberLessThan(Integer value) {
            addCriterion("forward_number <", value, "forwardNumber");
            return (Criteria) this;
        }

        public Criteria andForwardNumberLessThanOrEqualTo(Integer value) {
            addCriterion("forward_number <=", value, "forwardNumber");
            return (Criteria) this;
        }

        public Criteria andForwardNumberIn(List<Integer> values) {
            addCriterion("forward_number in", values, "forwardNumber");
            return (Criteria) this;
        }

        public Criteria andForwardNumberNotIn(List<Integer> values) {
            addCriterion("forward_number not in", values, "forwardNumber");
            return (Criteria) this;
        }

        public Criteria andForwardNumberBetween(Integer value1, Integer value2) {
            addCriterion("forward_number between", value1, value2, "forwardNumber");
            return (Criteria) this;
        }

        public Criteria andForwardNumberNotBetween(Integer value1, Integer value2) {
            addCriterion("forward_number not between", value1, value2, "forwardNumber");
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

        public Criteria andLikeNumberIsNull() {
            addCriterion("like_number is null");
            return (Criteria) this;
        }

        public Criteria andLikeNumberIsNotNull() {
            addCriterion("like_number is not null");
            return (Criteria) this;
        }

        public Criteria andLikeNumberEqualTo(Integer value) {
            addCriterion("like_number =", value, "likeNumber");
            return (Criteria) this;
        }

        public Criteria andLikeNumberNotEqualTo(Integer value) {
            addCriterion("like_number <>", value, "likeNumber");
            return (Criteria) this;
        }

        public Criteria andLikeNumberGreaterThan(Integer value) {
            addCriterion("like_number >", value, "likeNumber");
            return (Criteria) this;
        }

        public Criteria andLikeNumberGreaterThanOrEqualTo(Integer value) {
            addCriterion("like_number >=", value, "likeNumber");
            return (Criteria) this;
        }

        public Criteria andLikeNumberLessThan(Integer value) {
            addCriterion("like_number <", value, "likeNumber");
            return (Criteria) this;
        }

        public Criteria andLikeNumberLessThanOrEqualTo(Integer value) {
            addCriterion("like_number <=", value, "likeNumber");
            return (Criteria) this;
        }

        public Criteria andLikeNumberIn(List<Integer> values) {
            addCriterion("like_number in", values, "likeNumber");
            return (Criteria) this;
        }

        public Criteria andLikeNumberNotIn(List<Integer> values) {
            addCriterion("like_number not in", values, "likeNumber");
            return (Criteria) this;
        }

        public Criteria andLikeNumberBetween(Integer value1, Integer value2) {
            addCriterion("like_number between", value1, value2, "likeNumber");
            return (Criteria) this;
        }

        public Criteria andLikeNumberNotBetween(Integer value1, Integer value2) {
            addCriterion("like_number not between", value1, value2, "likeNumber");
            return (Criteria) this;
        }

        public Criteria andCommentNumberIsNull() {
            addCriterion("comment_number is null");
            return (Criteria) this;
        }

        public Criteria andCommentNumberIsNotNull() {
            addCriterion("comment_number is not null");
            return (Criteria) this;
        }

        public Criteria andCommentNumberEqualTo(Integer value) {
            addCriterion("comment_number =", value, "commentNumber");
            return (Criteria) this;
        }

        public Criteria andCommentNumberNotEqualTo(Integer value) {
            addCriterion("comment_number <>", value, "commentNumber");
            return (Criteria) this;
        }

        public Criteria andCommentNumberGreaterThan(Integer value) {
            addCriterion("comment_number >", value, "commentNumber");
            return (Criteria) this;
        }

        public Criteria andCommentNumberGreaterThanOrEqualTo(Integer value) {
            addCriterion("comment_number >=", value, "commentNumber");
            return (Criteria) this;
        }

        public Criteria andCommentNumberLessThan(Integer value) {
            addCriterion("comment_number <", value, "commentNumber");
            return (Criteria) this;
        }

        public Criteria andCommentNumberLessThanOrEqualTo(Integer value) {
            addCriterion("comment_number <=", value, "commentNumber");
            return (Criteria) this;
        }

        public Criteria andCommentNumberIn(List<Integer> values) {
            addCriterion("comment_number in", values, "commentNumber");
            return (Criteria) this;
        }

        public Criteria andCommentNumberNotIn(List<Integer> values) {
            addCriterion("comment_number not in", values, "commentNumber");
            return (Criteria) this;
        }

        public Criteria andCommentNumberBetween(Integer value1, Integer value2) {
            addCriterion("comment_number between", value1, value2, "commentNumber");
            return (Criteria) this;
        }

        public Criteria andCommentNumberNotBetween(Integer value1, Integer value2) {
            addCriterion("comment_number not between", value1, value2, "commentNumber");
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