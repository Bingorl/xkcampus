package com.biu.wifi.campus.dao.model;

import java.util.ArrayList;
import java.util.List;

public class CetScoreCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private Integer limit;

    private Long offset;

    public CetScoreCriteria() {
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

        public Criteria andExCardNumIsNull() {
            addCriterion("ex_card_num is null");
            return (Criteria) this;
        }

        public Criteria andExCardNumIsNotNull() {
            addCriterion("ex_card_num is not null");
            return (Criteria) this;
        }

        public Criteria andExCardNumEqualTo(String value) {
            addCriterion("ex_card_num =", value, "exCardNum");
            return (Criteria) this;
        }

        public Criteria andExCardNumNotEqualTo(String value) {
            addCriterion("ex_card_num <>", value, "exCardNum");
            return (Criteria) this;
        }

        public Criteria andExCardNumGreaterThan(String value) {
            addCriterion("ex_card_num >", value, "exCardNum");
            return (Criteria) this;
        }

        public Criteria andExCardNumGreaterThanOrEqualTo(String value) {
            addCriterion("ex_card_num >=", value, "exCardNum");
            return (Criteria) this;
        }

        public Criteria andExCardNumLessThan(String value) {
            addCriterion("ex_card_num <", value, "exCardNum");
            return (Criteria) this;
        }

        public Criteria andExCardNumLessThanOrEqualTo(String value) {
            addCriterion("ex_card_num <=", value, "exCardNum");
            return (Criteria) this;
        }

        public Criteria andExCardNumLike(String value) {
            addCriterion("ex_card_num like", value, "exCardNum");
            return (Criteria) this;
        }

        public Criteria andExCardNumNotLike(String value) {
            addCriterion("ex_card_num not like", value, "exCardNum");
            return (Criteria) this;
        }

        public Criteria andExCardNumIn(List<String> values) {
            addCriterion("ex_card_num in", values, "exCardNum");
            return (Criteria) this;
        }

        public Criteria andExCardNumNotIn(List<String> values) {
            addCriterion("ex_card_num not in", values, "exCardNum");
            return (Criteria) this;
        }

        public Criteria andExCardNumBetween(String value1, String value2) {
            addCriterion("ex_card_num between", value1, value2, "exCardNum");
            return (Criteria) this;
        }

        public Criteria andExCardNumNotBetween(String value1, String value2) {
            addCriterion("ex_card_num not between", value1, value2, "exCardNum");
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

        public Criteria andSchoolNameIsNull() {
            addCriterion("school_name is null");
            return (Criteria) this;
        }

        public Criteria andSchoolNameIsNotNull() {
            addCriterion("school_name is not null");
            return (Criteria) this;
        }

        public Criteria andSchoolNameEqualTo(String value) {
            addCriterion("school_name =", value, "schoolName");
            return (Criteria) this;
        }

        public Criteria andSchoolNameNotEqualTo(String value) {
            addCriterion("school_name <>", value, "schoolName");
            return (Criteria) this;
        }

        public Criteria andSchoolNameGreaterThan(String value) {
            addCriterion("school_name >", value, "schoolName");
            return (Criteria) this;
        }

        public Criteria andSchoolNameGreaterThanOrEqualTo(String value) {
            addCriterion("school_name >=", value, "schoolName");
            return (Criteria) this;
        }

        public Criteria andSchoolNameLessThan(String value) {
            addCriterion("school_name <", value, "schoolName");
            return (Criteria) this;
        }

        public Criteria andSchoolNameLessThanOrEqualTo(String value) {
            addCriterion("school_name <=", value, "schoolName");
            return (Criteria) this;
        }

        public Criteria andSchoolNameLike(String value) {
            addCriterion("school_name like", value, "schoolName");
            return (Criteria) this;
        }

        public Criteria andSchoolNameNotLike(String value) {
            addCriterion("school_name not like", value, "schoolName");
            return (Criteria) this;
        }

        public Criteria andSchoolNameIn(List<String> values) {
            addCriterion("school_name in", values, "schoolName");
            return (Criteria) this;
        }

        public Criteria andSchoolNameNotIn(List<String> values) {
            addCriterion("school_name not in", values, "schoolName");
            return (Criteria) this;
        }

        public Criteria andSchoolNameBetween(String value1, String value2) {
            addCriterion("school_name between", value1, value2, "schoolName");
            return (Criteria) this;
        }

        public Criteria andSchoolNameNotBetween(String value1, String value2) {
            addCriterion("school_name not between", value1, value2, "schoolName");
            return (Criteria) this;
        }

        public Criteria andCetIsNull() {
            addCriterion("cet is null");
            return (Criteria) this;
        }

        public Criteria andCetIsNotNull() {
            addCriterion("cet is not null");
            return (Criteria) this;
        }

        public Criteria andCetEqualTo(String value) {
            addCriterion("cet =", value, "cet");
            return (Criteria) this;
        }

        public Criteria andCetNotEqualTo(String value) {
            addCriterion("cet <>", value, "cet");
            return (Criteria) this;
        }

        public Criteria andCetGreaterThan(String value) {
            addCriterion("cet >", value, "cet");
            return (Criteria) this;
        }

        public Criteria andCetGreaterThanOrEqualTo(String value) {
            addCriterion("cet >=", value, "cet");
            return (Criteria) this;
        }

        public Criteria andCetLessThan(String value) {
            addCriterion("cet <", value, "cet");
            return (Criteria) this;
        }

        public Criteria andCetLessThanOrEqualTo(String value) {
            addCriterion("cet <=", value, "cet");
            return (Criteria) this;
        }

        public Criteria andCetLike(String value) {
            addCriterion("cet like", value, "cet");
            return (Criteria) this;
        }

        public Criteria andCetNotLike(String value) {
            addCriterion("cet not like", value, "cet");
            return (Criteria) this;
        }

        public Criteria andCetIn(List<String> values) {
            addCriterion("cet in", values, "cet");
            return (Criteria) this;
        }

        public Criteria andCetNotIn(List<String> values) {
            addCriterion("cet not in", values, "cet");
            return (Criteria) this;
        }

        public Criteria andCetBetween(String value1, String value2) {
            addCriterion("cet between", value1, value2, "cet");
            return (Criteria) this;
        }

        public Criteria andCetNotBetween(String value1, String value2) {
            addCriterion("cet not between", value1, value2, "cet");
            return (Criteria) this;
        }

        public Criteria andTotalScoreIsNull() {
            addCriterion("total_score is null");
            return (Criteria) this;
        }

        public Criteria andTotalScoreIsNotNull() {
            addCriterion("total_score is not null");
            return (Criteria) this;
        }

        public Criteria andTotalScoreEqualTo(String value) {
            addCriterion("total_score =", value, "totalScore");
            return (Criteria) this;
        }

        public Criteria andTotalScoreNotEqualTo(String value) {
            addCriterion("total_score <>", value, "totalScore");
            return (Criteria) this;
        }

        public Criteria andTotalScoreGreaterThan(String value) {
            addCriterion("total_score >", value, "totalScore");
            return (Criteria) this;
        }

        public Criteria andTotalScoreGreaterThanOrEqualTo(String value) {
            addCriterion("total_score >=", value, "totalScore");
            return (Criteria) this;
        }

        public Criteria andTotalScoreLessThan(String value) {
            addCriterion("total_score <", value, "totalScore");
            return (Criteria) this;
        }

        public Criteria andTotalScoreLessThanOrEqualTo(String value) {
            addCriterion("total_score <=", value, "totalScore");
            return (Criteria) this;
        }

        public Criteria andTotalScoreLike(String value) {
            addCriterion("total_score like", value, "totalScore");
            return (Criteria) this;
        }

        public Criteria andTotalScoreNotLike(String value) {
            addCriterion("total_score not like", value, "totalScore");
            return (Criteria) this;
        }

        public Criteria andTotalScoreIn(List<String> values) {
            addCriterion("total_score in", values, "totalScore");
            return (Criteria) this;
        }

        public Criteria andTotalScoreNotIn(List<String> values) {
            addCriterion("total_score not in", values, "totalScore");
            return (Criteria) this;
        }

        public Criteria andTotalScoreBetween(String value1, String value2) {
            addCriterion("total_score between", value1, value2, "totalScore");
            return (Criteria) this;
        }

        public Criteria andTotalScoreNotBetween(String value1, String value2) {
            addCriterion("total_score not between", value1, value2, "totalScore");
            return (Criteria) this;
        }

        public Criteria andListenScoreIsNull() {
            addCriterion("listen_score is null");
            return (Criteria) this;
        }

        public Criteria andListenScoreIsNotNull() {
            addCriterion("listen_score is not null");
            return (Criteria) this;
        }

        public Criteria andListenScoreEqualTo(String value) {
            addCriterion("listen_score =", value, "listenScore");
            return (Criteria) this;
        }

        public Criteria andListenScoreNotEqualTo(String value) {
            addCriterion("listen_score <>", value, "listenScore");
            return (Criteria) this;
        }

        public Criteria andListenScoreGreaterThan(String value) {
            addCriterion("listen_score >", value, "listenScore");
            return (Criteria) this;
        }

        public Criteria andListenScoreGreaterThanOrEqualTo(String value) {
            addCriterion("listen_score >=", value, "listenScore");
            return (Criteria) this;
        }

        public Criteria andListenScoreLessThan(String value) {
            addCriterion("listen_score <", value, "listenScore");
            return (Criteria) this;
        }

        public Criteria andListenScoreLessThanOrEqualTo(String value) {
            addCriterion("listen_score <=", value, "listenScore");
            return (Criteria) this;
        }

        public Criteria andListenScoreLike(String value) {
            addCriterion("listen_score like", value, "listenScore");
            return (Criteria) this;
        }

        public Criteria andListenScoreNotLike(String value) {
            addCriterion("listen_score not like", value, "listenScore");
            return (Criteria) this;
        }

        public Criteria andListenScoreIn(List<String> values) {
            addCriterion("listen_score in", values, "listenScore");
            return (Criteria) this;
        }

        public Criteria andListenScoreNotIn(List<String> values) {
            addCriterion("listen_score not in", values, "listenScore");
            return (Criteria) this;
        }

        public Criteria andListenScoreBetween(String value1, String value2) {
            addCriterion("listen_score between", value1, value2, "listenScore");
            return (Criteria) this;
        }

        public Criteria andListenScoreNotBetween(String value1, String value2) {
            addCriterion("listen_score not between", value1, value2, "listenScore");
            return (Criteria) this;
        }

        public Criteria andReadScoreIsNull() {
            addCriterion("read_score is null");
            return (Criteria) this;
        }

        public Criteria andReadScoreIsNotNull() {
            addCriterion("read_score is not null");
            return (Criteria) this;
        }

        public Criteria andReadScoreEqualTo(String value) {
            addCriterion("read_score =", value, "readScore");
            return (Criteria) this;
        }

        public Criteria andReadScoreNotEqualTo(String value) {
            addCriterion("read_score <>", value, "readScore");
            return (Criteria) this;
        }

        public Criteria andReadScoreGreaterThan(String value) {
            addCriterion("read_score >", value, "readScore");
            return (Criteria) this;
        }

        public Criteria andReadScoreGreaterThanOrEqualTo(String value) {
            addCriterion("read_score >=", value, "readScore");
            return (Criteria) this;
        }

        public Criteria andReadScoreLessThan(String value) {
            addCriterion("read_score <", value, "readScore");
            return (Criteria) this;
        }

        public Criteria andReadScoreLessThanOrEqualTo(String value) {
            addCriterion("read_score <=", value, "readScore");
            return (Criteria) this;
        }

        public Criteria andReadScoreLike(String value) {
            addCriterion("read_score like", value, "readScore");
            return (Criteria) this;
        }

        public Criteria andReadScoreNotLike(String value) {
            addCriterion("read_score not like", value, "readScore");
            return (Criteria) this;
        }

        public Criteria andReadScoreIn(List<String> values) {
            addCriterion("read_score in", values, "readScore");
            return (Criteria) this;
        }

        public Criteria andReadScoreNotIn(List<String> values) {
            addCriterion("read_score not in", values, "readScore");
            return (Criteria) this;
        }

        public Criteria andReadScoreBetween(String value1, String value2) {
            addCriterion("read_score between", value1, value2, "readScore");
            return (Criteria) this;
        }

        public Criteria andReadScoreNotBetween(String value1, String value2) {
            addCriterion("read_score not between", value1, value2, "readScore");
            return (Criteria) this;
        }

        public Criteria andWritingScoreIsNull() {
            addCriterion("writing_score is null");
            return (Criteria) this;
        }

        public Criteria andWritingScoreIsNotNull() {
            addCriterion("writing_score is not null");
            return (Criteria) this;
        }

        public Criteria andWritingScoreEqualTo(String value) {
            addCriterion("writing_score =", value, "writingScore");
            return (Criteria) this;
        }

        public Criteria andWritingScoreNotEqualTo(String value) {
            addCriterion("writing_score <>", value, "writingScore");
            return (Criteria) this;
        }

        public Criteria andWritingScoreGreaterThan(String value) {
            addCriterion("writing_score >", value, "writingScore");
            return (Criteria) this;
        }

        public Criteria andWritingScoreGreaterThanOrEqualTo(String value) {
            addCriterion("writing_score >=", value, "writingScore");
            return (Criteria) this;
        }

        public Criteria andWritingScoreLessThan(String value) {
            addCriterion("writing_score <", value, "writingScore");
            return (Criteria) this;
        }

        public Criteria andWritingScoreLessThanOrEqualTo(String value) {
            addCriterion("writing_score <=", value, "writingScore");
            return (Criteria) this;
        }

        public Criteria andWritingScoreLike(String value) {
            addCriterion("writing_score like", value, "writingScore");
            return (Criteria) this;
        }

        public Criteria andWritingScoreNotLike(String value) {
            addCriterion("writing_score not like", value, "writingScore");
            return (Criteria) this;
        }

        public Criteria andWritingScoreIn(List<String> values) {
            addCriterion("writing_score in", values, "writingScore");
            return (Criteria) this;
        }

        public Criteria andWritingScoreNotIn(List<String> values) {
            addCriterion("writing_score not in", values, "writingScore");
            return (Criteria) this;
        }

        public Criteria andWritingScoreBetween(String value1, String value2) {
            addCriterion("writing_score between", value1, value2, "writingScore");
            return (Criteria) this;
        }

        public Criteria andWritingScoreNotBetween(String value1, String value2) {
            addCriterion("writing_score not between", value1, value2, "writingScore");
            return (Criteria) this;
        }

        public Criteria andOralTestScoreIsNull() {
            addCriterion("oral_test_score is null");
            return (Criteria) this;
        }

        public Criteria andOralTestScoreIsNotNull() {
            addCriterion("oral_test_score is not null");
            return (Criteria) this;
        }

        public Criteria andOralTestScoreEqualTo(String value) {
            addCriterion("oral_test_score =", value, "oralTestScore");
            return (Criteria) this;
        }

        public Criteria andOralTestScoreNotEqualTo(String value) {
            addCriterion("oral_test_score <>", value, "oralTestScore");
            return (Criteria) this;
        }

        public Criteria andOralTestScoreGreaterThan(String value) {
            addCriterion("oral_test_score >", value, "oralTestScore");
            return (Criteria) this;
        }

        public Criteria andOralTestScoreGreaterThanOrEqualTo(String value) {
            addCriterion("oral_test_score >=", value, "oralTestScore");
            return (Criteria) this;
        }

        public Criteria andOralTestScoreLessThan(String value) {
            addCriterion("oral_test_score <", value, "oralTestScore");
            return (Criteria) this;
        }

        public Criteria andOralTestScoreLessThanOrEqualTo(String value) {
            addCriterion("oral_test_score <=", value, "oralTestScore");
            return (Criteria) this;
        }

        public Criteria andOralTestScoreLike(String value) {
            addCriterion("oral_test_score like", value, "oralTestScore");
            return (Criteria) this;
        }

        public Criteria andOralTestScoreNotLike(String value) {
            addCriterion("oral_test_score not like", value, "oralTestScore");
            return (Criteria) this;
        }

        public Criteria andOralTestScoreIn(List<String> values) {
            addCriterion("oral_test_score in", values, "oralTestScore");
            return (Criteria) this;
        }

        public Criteria andOralTestScoreNotIn(List<String> values) {
            addCriterion("oral_test_score not in", values, "oralTestScore");
            return (Criteria) this;
        }

        public Criteria andOralTestScoreBetween(String value1, String value2) {
            addCriterion("oral_test_score between", value1, value2, "oralTestScore");
            return (Criteria) this;
        }

        public Criteria andOralTestScoreNotBetween(String value1, String value2) {
            addCriterion("oral_test_score not between", value1, value2, "oralTestScore");
            return (Criteria) this;
        }

        public Criteria andCetExamTimeIsNull() {
            addCriterion("cet_exam_time is null");
            return (Criteria) this;
        }

        public Criteria andCetExamTimeIsNotNull() {
            addCriterion("cet_exam_time is not null");
            return (Criteria) this;
        }

        public Criteria andCetExamTimeEqualTo(String value) {
            addCriterion("cet_exam_time =", value, "cetExamTime");
            return (Criteria) this;
        }

        public Criteria andCetExamTimeNotEqualTo(String value) {
            addCriterion("cet_exam_time <>", value, "cetExamTime");
            return (Criteria) this;
        }

        public Criteria andCetExamTimeGreaterThan(String value) {
            addCriterion("cet_exam_time >", value, "cetExamTime");
            return (Criteria) this;
        }

        public Criteria andCetExamTimeGreaterThanOrEqualTo(String value) {
            addCriterion("cet_exam_time >=", value, "cetExamTime");
            return (Criteria) this;
        }

        public Criteria andCetExamTimeLessThan(String value) {
            addCriterion("cet_exam_time <", value, "cetExamTime");
            return (Criteria) this;
        }

        public Criteria andCetExamTimeLessThanOrEqualTo(String value) {
            addCriterion("cet_exam_time <=", value, "cetExamTime");
            return (Criteria) this;
        }

        public Criteria andCetExamTimeLike(String value) {
            addCriterion("cet_exam_time like", value, "cetExamTime");
            return (Criteria) this;
        }

        public Criteria andCetExamTimeNotLike(String value) {
            addCriterion("cet_exam_time not like", value, "cetExamTime");
            return (Criteria) this;
        }

        public Criteria andCetExamTimeIn(List<String> values) {
            addCriterion("cet_exam_time in", values, "cetExamTime");
            return (Criteria) this;
        }

        public Criteria andCetExamTimeNotIn(List<String> values) {
            addCriterion("cet_exam_time not in", values, "cetExamTime");
            return (Criteria) this;
        }

        public Criteria andCetExamTimeBetween(String value1, String value2) {
            addCriterion("cet_exam_time between", value1, value2, "cetExamTime");
            return (Criteria) this;
        }

        public Criteria andCetExamTimeNotBetween(String value1, String value2) {
            addCriterion("cet_exam_time not between", value1, value2, "cetExamTime");
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