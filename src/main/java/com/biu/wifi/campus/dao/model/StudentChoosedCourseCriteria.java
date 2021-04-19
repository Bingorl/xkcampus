package com.biu.wifi.campus.dao.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StudentChoosedCourseCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private Integer limit;

    private Long offset;

    public StudentChoosedCourseCriteria() {
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

        public Criteria andCourseNoIsNull() {
            addCriterion("course_no is null");
            return (Criteria) this;
        }

        public Criteria andCourseNoIsNotNull() {
            addCriterion("course_no is not null");
            return (Criteria) this;
        }

        public Criteria andCourseNoEqualTo(String value) {
            addCriterion("course_no =", value, "courseNo");
            return (Criteria) this;
        }

        public Criteria andCourseNoNotEqualTo(String value) {
            addCriterion("course_no <>", value, "courseNo");
            return (Criteria) this;
        }

        public Criteria andCourseNoGreaterThan(String value) {
            addCriterion("course_no >", value, "courseNo");
            return (Criteria) this;
        }

        public Criteria andCourseNoGreaterThanOrEqualTo(String value) {
            addCriterion("course_no >=", value, "courseNo");
            return (Criteria) this;
        }

        public Criteria andCourseNoLessThan(String value) {
            addCriterion("course_no <", value, "courseNo");
            return (Criteria) this;
        }

        public Criteria andCourseNoLessThanOrEqualTo(String value) {
            addCriterion("course_no <=", value, "courseNo");
            return (Criteria) this;
        }

        public Criteria andCourseNoLike(String value) {
            addCriterion("course_no like", value, "courseNo");
            return (Criteria) this;
        }

        public Criteria andCourseNoNotLike(String value) {
            addCriterion("course_no not like", value, "courseNo");
            return (Criteria) this;
        }

        public Criteria andCourseNoIn(List<String> values) {
            addCriterion("course_no in", values, "courseNo");
            return (Criteria) this;
        }

        public Criteria andCourseNoNotIn(List<String> values) {
            addCriterion("course_no not in", values, "courseNo");
            return (Criteria) this;
        }

        public Criteria andCourseNoBetween(String value1, String value2) {
            addCriterion("course_no between", value1, value2, "courseNo");
            return (Criteria) this;
        }

        public Criteria andCourseNoNotBetween(String value1, String value2) {
            addCriterion("course_no not between", value1, value2, "courseNo");
            return (Criteria) this;
        }

        public Criteria andCourseSerialNoIsNull() {
            addCriterion("course_serial_no is null");
            return (Criteria) this;
        }

        public Criteria andCourseSerialNoIsNotNull() {
            addCriterion("course_serial_no is not null");
            return (Criteria) this;
        }

        public Criteria andCourseSerialNoEqualTo(String value) {
            addCriterion("course_serial_no =", value, "courseSerialNo");
            return (Criteria) this;
        }

        public Criteria andCourseSerialNoNotEqualTo(String value) {
            addCriterion("course_serial_no <>", value, "courseSerialNo");
            return (Criteria) this;
        }

        public Criteria andCourseSerialNoGreaterThan(String value) {
            addCriterion("course_serial_no >", value, "courseSerialNo");
            return (Criteria) this;
        }

        public Criteria andCourseSerialNoGreaterThanOrEqualTo(String value) {
            addCriterion("course_serial_no >=", value, "courseSerialNo");
            return (Criteria) this;
        }

        public Criteria andCourseSerialNoLessThan(String value) {
            addCriterion("course_serial_no <", value, "courseSerialNo");
            return (Criteria) this;
        }

        public Criteria andCourseSerialNoLessThanOrEqualTo(String value) {
            addCriterion("course_serial_no <=", value, "courseSerialNo");
            return (Criteria) this;
        }

        public Criteria andCourseSerialNoLike(String value) {
            addCriterion("course_serial_no like", value, "courseSerialNo");
            return (Criteria) this;
        }

        public Criteria andCourseSerialNoNotLike(String value) {
            addCriterion("course_serial_no not like", value, "courseSerialNo");
            return (Criteria) this;
        }

        public Criteria andCourseSerialNoIn(List<String> values) {
            addCriterion("course_serial_no in", values, "courseSerialNo");
            return (Criteria) this;
        }

        public Criteria andCourseSerialNoNotIn(List<String> values) {
            addCriterion("course_serial_no not in", values, "courseSerialNo");
            return (Criteria) this;
        }

        public Criteria andCourseSerialNoBetween(String value1, String value2) {
            addCriterion("course_serial_no between", value1, value2, "courseSerialNo");
            return (Criteria) this;
        }

        public Criteria andCourseSerialNoNotBetween(String value1, String value2) {
            addCriterion("course_serial_no not between", value1, value2, "courseSerialNo");
            return (Criteria) this;
        }

        public Criteria andStuNumberIsNull() {
            addCriterion("stu_number is null");
            return (Criteria) this;
        }

        public Criteria andStuNumberIsNotNull() {
            addCriterion("stu_number is not null");
            return (Criteria) this;
        }

        public Criteria andStuNumberEqualTo(String value) {
            addCriterion("stu_number =", value, "stuNumber");
            return (Criteria) this;
        }

        public Criteria andStuNumberNotEqualTo(String value) {
            addCriterion("stu_number <>", value, "stuNumber");
            return (Criteria) this;
        }

        public Criteria andStuNumberGreaterThan(String value) {
            addCriterion("stu_number >", value, "stuNumber");
            return (Criteria) this;
        }

        public Criteria andStuNumberGreaterThanOrEqualTo(String value) {
            addCriterion("stu_number >=", value, "stuNumber");
            return (Criteria) this;
        }

        public Criteria andStuNumberLessThan(String value) {
            addCriterion("stu_number <", value, "stuNumber");
            return (Criteria) this;
        }

        public Criteria andStuNumberLessThanOrEqualTo(String value) {
            addCriterion("stu_number <=", value, "stuNumber");
            return (Criteria) this;
        }

        public Criteria andStuNumberLike(String value) {
            addCriterion("stu_number like", value, "stuNumber");
            return (Criteria) this;
        }

        public Criteria andStuNumberNotLike(String value) {
            addCriterion("stu_number not like", value, "stuNumber");
            return (Criteria) this;
        }

        public Criteria andStuNumberIn(List<String> values) {
            addCriterion("stu_number in", values, "stuNumber");
            return (Criteria) this;
        }

        public Criteria andStuNumberNotIn(List<String> values) {
            addCriterion("stu_number not in", values, "stuNumber");
            return (Criteria) this;
        }

        public Criteria andStuNumberBetween(String value1, String value2) {
            addCriterion("stu_number between", value1, value2, "stuNumber");
            return (Criteria) this;
        }

        public Criteria andStuNumberNotBetween(String value1, String value2) {
            addCriterion("stu_number not between", value1, value2, "stuNumber");
            return (Criteria) this;
        }

        public Criteria andPlanNoIsNull() {
            addCriterion("plan_no is null");
            return (Criteria) this;
        }

        public Criteria andPlanNoIsNotNull() {
            addCriterion("plan_no is not null");
            return (Criteria) this;
        }

        public Criteria andPlanNoEqualTo(String value) {
            addCriterion("plan_no =", value, "planNo");
            return (Criteria) this;
        }

        public Criteria andPlanNoNotEqualTo(String value) {
            addCriterion("plan_no <>", value, "planNo");
            return (Criteria) this;
        }

        public Criteria andPlanNoGreaterThan(String value) {
            addCriterion("plan_no >", value, "planNo");
            return (Criteria) this;
        }

        public Criteria andPlanNoGreaterThanOrEqualTo(String value) {
            addCriterion("plan_no >=", value, "planNo");
            return (Criteria) this;
        }

        public Criteria andPlanNoLessThan(String value) {
            addCriterion("plan_no <", value, "planNo");
            return (Criteria) this;
        }

        public Criteria andPlanNoLessThanOrEqualTo(String value) {
            addCriterion("plan_no <=", value, "planNo");
            return (Criteria) this;
        }

        public Criteria andPlanNoLike(String value) {
            addCriterion("plan_no like", value, "planNo");
            return (Criteria) this;
        }

        public Criteria andPlanNoNotLike(String value) {
            addCriterion("plan_no not like", value, "planNo");
            return (Criteria) this;
        }

        public Criteria andPlanNoIn(List<String> values) {
            addCriterion("plan_no in", values, "planNo");
            return (Criteria) this;
        }

        public Criteria andPlanNoNotIn(List<String> values) {
            addCriterion("plan_no not in", values, "planNo");
            return (Criteria) this;
        }

        public Criteria andPlanNoBetween(String value1, String value2) {
            addCriterion("plan_no between", value1, value2, "planNo");
            return (Criteria) this;
        }

        public Criteria andPlanNoNotBetween(String value1, String value2) {
            addCriterion("plan_no not between", value1, value2, "planNo");
            return (Criteria) this;
        }

        public Criteria andStudyTypeIsNull() {
            addCriterion("study_type is null");
            return (Criteria) this;
        }

        public Criteria andStudyTypeIsNotNull() {
            addCriterion("study_type is not null");
            return (Criteria) this;
        }

        public Criteria andStudyTypeEqualTo(String value) {
            addCriterion("study_type =", value, "studyType");
            return (Criteria) this;
        }

        public Criteria andStudyTypeNotEqualTo(String value) {
            addCriterion("study_type <>", value, "studyType");
            return (Criteria) this;
        }

        public Criteria andStudyTypeGreaterThan(String value) {
            addCriterion("study_type >", value, "studyType");
            return (Criteria) this;
        }

        public Criteria andStudyTypeGreaterThanOrEqualTo(String value) {
            addCriterion("study_type >=", value, "studyType");
            return (Criteria) this;
        }

        public Criteria andStudyTypeLessThan(String value) {
            addCriterion("study_type <", value, "studyType");
            return (Criteria) this;
        }

        public Criteria andStudyTypeLessThanOrEqualTo(String value) {
            addCriterion("study_type <=", value, "studyType");
            return (Criteria) this;
        }

        public Criteria andStudyTypeLike(String value) {
            addCriterion("study_type like", value, "studyType");
            return (Criteria) this;
        }

        public Criteria andStudyTypeNotLike(String value) {
            addCriterion("study_type not like", value, "studyType");
            return (Criteria) this;
        }

        public Criteria andStudyTypeIn(List<String> values) {
            addCriterion("study_type in", values, "studyType");
            return (Criteria) this;
        }

        public Criteria andStudyTypeNotIn(List<String> values) {
            addCriterion("study_type not in", values, "studyType");
            return (Criteria) this;
        }

        public Criteria andStudyTypeBetween(String value1, String value2) {
            addCriterion("study_type between", value1, value2, "studyType");
            return (Criteria) this;
        }

        public Criteria andStudyTypeNotBetween(String value1, String value2) {
            addCriterion("study_type not between", value1, value2, "studyType");
            return (Criteria) this;
        }

        public Criteria andCourseAttrIsNull() {
            addCriterion("course_attr is null");
            return (Criteria) this;
        }

        public Criteria andCourseAttrIsNotNull() {
            addCriterion("course_attr is not null");
            return (Criteria) this;
        }

        public Criteria andCourseAttrEqualTo(String value) {
            addCriterion("course_attr =", value, "courseAttr");
            return (Criteria) this;
        }

        public Criteria andCourseAttrNotEqualTo(String value) {
            addCriterion("course_attr <>", value, "courseAttr");
            return (Criteria) this;
        }

        public Criteria andCourseAttrGreaterThan(String value) {
            addCriterion("course_attr >", value, "courseAttr");
            return (Criteria) this;
        }

        public Criteria andCourseAttrGreaterThanOrEqualTo(String value) {
            addCriterion("course_attr >=", value, "courseAttr");
            return (Criteria) this;
        }

        public Criteria andCourseAttrLessThan(String value) {
            addCriterion("course_attr <", value, "courseAttr");
            return (Criteria) this;
        }

        public Criteria andCourseAttrLessThanOrEqualTo(String value) {
            addCriterion("course_attr <=", value, "courseAttr");
            return (Criteria) this;
        }

        public Criteria andCourseAttrLike(String value) {
            addCriterion("course_attr like", value, "courseAttr");
            return (Criteria) this;
        }

        public Criteria andCourseAttrNotLike(String value) {
            addCriterion("course_attr not like", value, "courseAttr");
            return (Criteria) this;
        }

        public Criteria andCourseAttrIn(List<String> values) {
            addCriterion("course_attr in", values, "courseAttr");
            return (Criteria) this;
        }

        public Criteria andCourseAttrNotIn(List<String> values) {
            addCriterion("course_attr not in", values, "courseAttr");
            return (Criteria) this;
        }

        public Criteria andCourseAttrBetween(String value1, String value2) {
            addCriterion("course_attr between", value1, value2, "courseAttr");
            return (Criteria) this;
        }

        public Criteria andCourseAttrNotBetween(String value1, String value2) {
            addCriterion("course_attr not between", value1, value2, "courseAttr");
            return (Criteria) this;
        }

        public Criteria andExamTypeIsNull() {
            addCriterion("exam_type is null");
            return (Criteria) this;
        }

        public Criteria andExamTypeIsNotNull() {
            addCriterion("exam_type is not null");
            return (Criteria) this;
        }

        public Criteria andExamTypeEqualTo(String value) {
            addCriterion("exam_type =", value, "examType");
            return (Criteria) this;
        }

        public Criteria andExamTypeNotEqualTo(String value) {
            addCriterion("exam_type <>", value, "examType");
            return (Criteria) this;
        }

        public Criteria andExamTypeGreaterThan(String value) {
            addCriterion("exam_type >", value, "examType");
            return (Criteria) this;
        }

        public Criteria andExamTypeGreaterThanOrEqualTo(String value) {
            addCriterion("exam_type >=", value, "examType");
            return (Criteria) this;
        }

        public Criteria andExamTypeLessThan(String value) {
            addCriterion("exam_type <", value, "examType");
            return (Criteria) this;
        }

        public Criteria andExamTypeLessThanOrEqualTo(String value) {
            addCriterion("exam_type <=", value, "examType");
            return (Criteria) this;
        }

        public Criteria andExamTypeLike(String value) {
            addCriterion("exam_type like", value, "examType");
            return (Criteria) this;
        }

        public Criteria andExamTypeNotLike(String value) {
            addCriterion("exam_type not like", value, "examType");
            return (Criteria) this;
        }

        public Criteria andExamTypeIn(List<String> values) {
            addCriterion("exam_type in", values, "examType");
            return (Criteria) this;
        }

        public Criteria andExamTypeNotIn(List<String> values) {
            addCriterion("exam_type not in", values, "examType");
            return (Criteria) this;
        }

        public Criteria andExamTypeBetween(String value1, String value2) {
            addCriterion("exam_type between", value1, value2, "examType");
            return (Criteria) this;
        }

        public Criteria andExamTypeNotBetween(String value1, String value2) {
            addCriterion("exam_type not between", value1, value2, "examType");
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

        public Criteria andStatusEqualTo(String value) {
            addCriterion("`status` =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(String value) {
            addCriterion("`status` <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(String value) {
            addCriterion("`status` >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(String value) {
            addCriterion("`status` >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(String value) {
            addCriterion("`status` <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(String value) {
            addCriterion("`status` <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLike(String value) {
            addCriterion("`status` like", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotLike(String value) {
            addCriterion("`status` not like", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<String> values) {
            addCriterion("`status` in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<String> values) {
            addCriterion("`status` not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(String value1, String value2) {
            addCriterion("`status` between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(String value1, String value2) {
            addCriterion("`status` not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andRemarkIsNull() {
            addCriterion("remark is null");
            return (Criteria) this;
        }

        public Criteria andRemarkIsNotNull() {
            addCriterion("remark is not null");
            return (Criteria) this;
        }

        public Criteria andRemarkEqualTo(String value) {
            addCriterion("remark =", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotEqualTo(String value) {
            addCriterion("remark <>", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkGreaterThan(String value) {
            addCriterion("remark >", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkGreaterThanOrEqualTo(String value) {
            addCriterion("remark >=", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLessThan(String value) {
            addCriterion("remark <", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLessThanOrEqualTo(String value) {
            addCriterion("remark <=", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLike(String value) {
            addCriterion("remark like", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotLike(String value) {
            addCriterion("remark not like", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkIn(List<String> values) {
            addCriterion("remark in", values, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotIn(List<String> values) {
            addCriterion("remark not in", values, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkBetween(String value1, String value2) {
            addCriterion("remark between", value1, value2, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotBetween(String value1, String value2) {
            addCriterion("remark not between", value1, value2, "remark");
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

        public Criteria andIsDeleteEqualTo(Integer value) {
            addCriterion("is_delete =", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteNotEqualTo(Integer value) {
            addCriterion("is_delete <>", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteGreaterThan(Integer value) {
            addCriterion("is_delete >", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteGreaterThanOrEqualTo(Integer value) {
            addCriterion("is_delete >=", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteLessThan(Integer value) {
            addCriterion("is_delete <", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteLessThanOrEqualTo(Integer value) {
            addCriterion("is_delete <=", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteIn(List<Integer> values) {
            addCriterion("is_delete in", values, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteNotIn(List<Integer> values) {
            addCriterion("is_delete not in", values, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteBetween(Integer value1, Integer value2) {
            addCriterion("is_delete between", value1, value2, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteNotBetween(Integer value1, Integer value2) {
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