package com.biu.wifi.campus.dao.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExamArrangeAuditAdjustLogCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private Integer limit;

    private Long offset;

    public ExamArrangeAuditAdjustLogCriteria() {
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

        public Criteria andExamArrangeIdIsNull() {
            addCriterion("exam_arrange_id is null");
            return (Criteria) this;
        }

        public Criteria andExamArrangeIdIsNotNull() {
            addCriterion("exam_arrange_id is not null");
            return (Criteria) this;
        }

        public Criteria andExamArrangeIdEqualTo(Integer value) {
            addCriterion("exam_arrange_id =", value, "examArrangeId");
            return (Criteria) this;
        }

        public Criteria andExamArrangeIdNotEqualTo(Integer value) {
            addCriterion("exam_arrange_id <>", value, "examArrangeId");
            return (Criteria) this;
        }

        public Criteria andExamArrangeIdGreaterThan(Integer value) {
            addCriterion("exam_arrange_id >", value, "examArrangeId");
            return (Criteria) this;
        }

        public Criteria andExamArrangeIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("exam_arrange_id >=", value, "examArrangeId");
            return (Criteria) this;
        }

        public Criteria andExamArrangeIdLessThan(Integer value) {
            addCriterion("exam_arrange_id <", value, "examArrangeId");
            return (Criteria) this;
        }

        public Criteria andExamArrangeIdLessThanOrEqualTo(Integer value) {
            addCriterion("exam_arrange_id <=", value, "examArrangeId");
            return (Criteria) this;
        }

        public Criteria andExamArrangeIdIn(List<Integer> values) {
            addCriterion("exam_arrange_id in", values, "examArrangeId");
            return (Criteria) this;
        }

        public Criteria andExamArrangeIdNotIn(List<Integer> values) {
            addCriterion("exam_arrange_id not in", values, "examArrangeId");
            return (Criteria) this;
        }

        public Criteria andExamArrangeIdBetween(Integer value1, Integer value2) {
            addCriterion("exam_arrange_id between", value1, value2, "examArrangeId");
            return (Criteria) this;
        }

        public Criteria andExamArrangeIdNotBetween(Integer value1, Integer value2) {
            addCriterion("exam_arrange_id not between", value1, value2, "examArrangeId");
            return (Criteria) this;
        }

        public Criteria andTypeIsNull() {
            addCriterion("`type` is null");
            return (Criteria) this;
        }

        public Criteria andTypeIsNotNull() {
            addCriterion("`type` is not null");
            return (Criteria) this;
        }

        public Criteria andTypeEqualTo(Integer value) {
            addCriterion("`type` =", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotEqualTo(Integer value) {
            addCriterion("`type` <>", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThan(Integer value) {
            addCriterion("`type` >", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("`type` >=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThan(Integer value) {
            addCriterion("`type` <", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThanOrEqualTo(Integer value) {
            addCriterion("`type` <=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeIn(List<Integer> values) {
            addCriterion("`type` in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotIn(List<Integer> values) {
            addCriterion("`type` not in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeBetween(Integer value1, Integer value2) {
            addCriterion("`type` between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("`type` not between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andOldContentIsNull() {
            addCriterion("old_content is null");
            return (Criteria) this;
        }

        public Criteria andOldContentIsNotNull() {
            addCriterion("old_content is not null");
            return (Criteria) this;
        }

        public Criteria andOldContentEqualTo(String value) {
            addCriterion("old_content =", value, "oldContent");
            return (Criteria) this;
        }

        public Criteria andOldContentNotEqualTo(String value) {
            addCriterion("old_content <>", value, "oldContent");
            return (Criteria) this;
        }

        public Criteria andOldContentGreaterThan(String value) {
            addCriterion("old_content >", value, "oldContent");
            return (Criteria) this;
        }

        public Criteria andOldContentGreaterThanOrEqualTo(String value) {
            addCriterion("old_content >=", value, "oldContent");
            return (Criteria) this;
        }

        public Criteria andOldContentLessThan(String value) {
            addCriterion("old_content <", value, "oldContent");
            return (Criteria) this;
        }

        public Criteria andOldContentLessThanOrEqualTo(String value) {
            addCriterion("old_content <=", value, "oldContent");
            return (Criteria) this;
        }

        public Criteria andOldContentLike(String value) {
            addCriterion("old_content like", value, "oldContent");
            return (Criteria) this;
        }

        public Criteria andOldContentNotLike(String value) {
            addCriterion("old_content not like", value, "oldContent");
            return (Criteria) this;
        }

        public Criteria andOldContentIn(List<String> values) {
            addCriterion("old_content in", values, "oldContent");
            return (Criteria) this;
        }

        public Criteria andOldContentNotIn(List<String> values) {
            addCriterion("old_content not in", values, "oldContent");
            return (Criteria) this;
        }

        public Criteria andOldContentBetween(String value1, String value2) {
            addCriterion("old_content between", value1, value2, "oldContent");
            return (Criteria) this;
        }

        public Criteria andOldContentNotBetween(String value1, String value2) {
            addCriterion("old_content not between", value1, value2, "oldContent");
            return (Criteria) this;
        }

        public Criteria andContentIsNull() {
            addCriterion("content is null");
            return (Criteria) this;
        }

        public Criteria andContentIsNotNull() {
            addCriterion("content is not null");
            return (Criteria) this;
        }

        public Criteria andContentEqualTo(String value) {
            addCriterion("content =", value, "content");
            return (Criteria) this;
        }

        public Criteria andContentNotEqualTo(String value) {
            addCriterion("content <>", value, "content");
            return (Criteria) this;
        }

        public Criteria andContentGreaterThan(String value) {
            addCriterion("content >", value, "content");
            return (Criteria) this;
        }

        public Criteria andContentGreaterThanOrEqualTo(String value) {
            addCriterion("content >=", value, "content");
            return (Criteria) this;
        }

        public Criteria andContentLessThan(String value) {
            addCriterion("content <", value, "content");
            return (Criteria) this;
        }

        public Criteria andContentLessThanOrEqualTo(String value) {
            addCriterion("content <=", value, "content");
            return (Criteria) this;
        }

        public Criteria andContentLike(String value) {
            addCriterion("content like", value, "content");
            return (Criteria) this;
        }

        public Criteria andContentNotLike(String value) {
            addCriterion("content not like", value, "content");
            return (Criteria) this;
        }

        public Criteria andContentIn(List<String> values) {
            addCriterion("content in", values, "content");
            return (Criteria) this;
        }

        public Criteria andContentNotIn(List<String> values) {
            addCriterion("content not in", values, "content");
            return (Criteria) this;
        }

        public Criteria andContentBetween(String value1, String value2) {
            addCriterion("content between", value1, value2, "content");
            return (Criteria) this;
        }

        public Criteria andContentNotBetween(String value1, String value2) {
            addCriterion("content not between", value1, value2, "content");
            return (Criteria) this;
        }

        public Criteria andAdjustUserIdIsNull() {
            addCriterion("adjust_user_id is null");
            return (Criteria) this;
        }

        public Criteria andAdjustUserIdIsNotNull() {
            addCriterion("adjust_user_id is not null");
            return (Criteria) this;
        }

        public Criteria andAdjustUserIdEqualTo(Integer value) {
            addCriterion("adjust_user_id =", value, "adjustUserId");
            return (Criteria) this;
        }

        public Criteria andAdjustUserIdNotEqualTo(Integer value) {
            addCriterion("adjust_user_id <>", value, "adjustUserId");
            return (Criteria) this;
        }

        public Criteria andAdjustUserIdGreaterThan(Integer value) {
            addCriterion("adjust_user_id >", value, "adjustUserId");
            return (Criteria) this;
        }

        public Criteria andAdjustUserIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("adjust_user_id >=", value, "adjustUserId");
            return (Criteria) this;
        }

        public Criteria andAdjustUserIdLessThan(Integer value) {
            addCriterion("adjust_user_id <", value, "adjustUserId");
            return (Criteria) this;
        }

        public Criteria andAdjustUserIdLessThanOrEqualTo(Integer value) {
            addCriterion("adjust_user_id <=", value, "adjustUserId");
            return (Criteria) this;
        }

        public Criteria andAdjustUserIdIn(List<Integer> values) {
            addCriterion("adjust_user_id in", values, "adjustUserId");
            return (Criteria) this;
        }

        public Criteria andAdjustUserIdNotIn(List<Integer> values) {
            addCriterion("adjust_user_id not in", values, "adjustUserId");
            return (Criteria) this;
        }

        public Criteria andAdjustUserIdBetween(Integer value1, Integer value2) {
            addCriterion("adjust_user_id between", value1, value2, "adjustUserId");
            return (Criteria) this;
        }

        public Criteria andAdjustUserIdNotBetween(Integer value1, Integer value2) {
            addCriterion("adjust_user_id not between", value1, value2, "adjustUserId");
            return (Criteria) this;
        }

        public Criteria andAdjustTimeIsNull() {
            addCriterion("adjust_time is null");
            return (Criteria) this;
        }

        public Criteria andAdjustTimeIsNotNull() {
            addCriterion("adjust_time is not null");
            return (Criteria) this;
        }

        public Criteria andAdjustTimeEqualTo(Date value) {
            addCriterion("adjust_time =", value, "adjustTime");
            return (Criteria) this;
        }

        public Criteria andAdjustTimeNotEqualTo(Date value) {
            addCriterion("adjust_time <>", value, "adjustTime");
            return (Criteria) this;
        }

        public Criteria andAdjustTimeGreaterThan(Date value) {
            addCriterion("adjust_time >", value, "adjustTime");
            return (Criteria) this;
        }

        public Criteria andAdjustTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("adjust_time >=", value, "adjustTime");
            return (Criteria) this;
        }

        public Criteria andAdjustTimeLessThan(Date value) {
            addCriterion("adjust_time <", value, "adjustTime");
            return (Criteria) this;
        }

        public Criteria andAdjustTimeLessThanOrEqualTo(Date value) {
            addCriterion("adjust_time <=", value, "adjustTime");
            return (Criteria) this;
        }

        public Criteria andAdjustTimeIn(List<Date> values) {
            addCriterion("adjust_time in", values, "adjustTime");
            return (Criteria) this;
        }

        public Criteria andAdjustTimeNotIn(List<Date> values) {
            addCriterion("adjust_time not in", values, "adjustTime");
            return (Criteria) this;
        }

        public Criteria andAdjustTimeBetween(Date value1, Date value2) {
            addCriterion("adjust_time between", value1, value2, "adjustTime");
            return (Criteria) this;
        }

        public Criteria andAdjustTimeNotBetween(Date value1, Date value2) {
            addCriterion("adjust_time not between", value1, value2, "adjustTime");
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