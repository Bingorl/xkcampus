package com.biu.wifi.campus.dao.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TeacherCoursePlanCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private Integer limit;

    private Long offset;

    public TeacherCoursePlanCriteria() {
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

        public Criteria andBuildingIdIsNull() {
            addCriterion("building_id is null");
            return (Criteria) this;
        }

        public Criteria andBuildingIdIsNotNull() {
            addCriterion("building_id is not null");
            return (Criteria) this;
        }

        public Criteria andBuildingIdEqualTo(Integer value) {
            addCriterion("building_id =", value, "buildingId");
            return (Criteria) this;
        }

        public Criteria andBuildingIdNotEqualTo(Integer value) {
            addCriterion("building_id <>", value, "buildingId");
            return (Criteria) this;
        }

        public Criteria andBuildingIdGreaterThan(Integer value) {
            addCriterion("building_id >", value, "buildingId");
            return (Criteria) this;
        }

        public Criteria andBuildingIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("building_id >=", value, "buildingId");
            return (Criteria) this;
        }

        public Criteria andBuildingIdLessThan(Integer value) {
            addCriterion("building_id <", value, "buildingId");
            return (Criteria) this;
        }

        public Criteria andBuildingIdLessThanOrEqualTo(Integer value) {
            addCriterion("building_id <=", value, "buildingId");
            return (Criteria) this;
        }

        public Criteria andBuildingIdIn(List<Integer> values) {
            addCriterion("building_id in", values, "buildingId");
            return (Criteria) this;
        }

        public Criteria andBuildingIdNotIn(List<Integer> values) {
            addCriterion("building_id not in", values, "buildingId");
            return (Criteria) this;
        }

        public Criteria andBuildingIdBetween(Integer value1, Integer value2) {
            addCriterion("building_id between", value1, value2, "buildingId");
            return (Criteria) this;
        }

        public Criteria andBuildingIdNotBetween(Integer value1, Integer value2) {
            addCriterion("building_id not between", value1, value2, "buildingId");
            return (Criteria) this;
        }

        public Criteria andClassroomNoIsNull() {
            addCriterion("classroom_no is null");
            return (Criteria) this;
        }

        public Criteria andClassroomNoIsNotNull() {
            addCriterion("classroom_no is not null");
            return (Criteria) this;
        }

        public Criteria andClassroomNoEqualTo(String value) {
            addCriterion("classroom_no =", value, "classroomNo");
            return (Criteria) this;
        }

        public Criteria andClassroomNoNotEqualTo(String value) {
            addCriterion("classroom_no <>", value, "classroomNo");
            return (Criteria) this;
        }

        public Criteria andClassroomNoGreaterThan(String value) {
            addCriterion("classroom_no >", value, "classroomNo");
            return (Criteria) this;
        }

        public Criteria andClassroomNoGreaterThanOrEqualTo(String value) {
            addCriterion("classroom_no >=", value, "classroomNo");
            return (Criteria) this;
        }

        public Criteria andClassroomNoLessThan(String value) {
            addCriterion("classroom_no <", value, "classroomNo");
            return (Criteria) this;
        }

        public Criteria andClassroomNoLessThanOrEqualTo(String value) {
            addCriterion("classroom_no <=", value, "classroomNo");
            return (Criteria) this;
        }

        public Criteria andClassroomNoLike(String value) {
            addCriterion("classroom_no like", value, "classroomNo");
            return (Criteria) this;
        }

        public Criteria andClassroomNoNotLike(String value) {
            addCriterion("classroom_no not like", value, "classroomNo");
            return (Criteria) this;
        }

        public Criteria andClassroomNoIn(List<String> values) {
            addCriterion("classroom_no in", values, "classroomNo");
            return (Criteria) this;
        }

        public Criteria andClassroomNoNotIn(List<String> values) {
            addCriterion("classroom_no not in", values, "classroomNo");
            return (Criteria) this;
        }

        public Criteria andClassroomNoBetween(String value1, String value2) {
            addCriterion("classroom_no between", value1, value2, "classroomNo");
            return (Criteria) this;
        }

        public Criteria andClassroomNoNotBetween(String value1, String value2) {
            addCriterion("classroom_no not between", value1, value2, "classroomNo");
            return (Criteria) this;
        }

        public Criteria andCourseNameIsNull() {
            addCriterion("course_name is null");
            return (Criteria) this;
        }

        public Criteria andCourseNameIsNotNull() {
            addCriterion("course_name is not null");
            return (Criteria) this;
        }

        public Criteria andCourseNameEqualTo(String value) {
            addCriterion("course_name =", value, "courseName");
            return (Criteria) this;
        }

        public Criteria andCourseNameNotEqualTo(String value) {
            addCriterion("course_name <>", value, "courseName");
            return (Criteria) this;
        }

        public Criteria andCourseNameGreaterThan(String value) {
            addCriterion("course_name >", value, "courseName");
            return (Criteria) this;
        }

        public Criteria andCourseNameGreaterThanOrEqualTo(String value) {
            addCriterion("course_name >=", value, "courseName");
            return (Criteria) this;
        }

        public Criteria andCourseNameLessThan(String value) {
            addCriterion("course_name <", value, "courseName");
            return (Criteria) this;
        }

        public Criteria andCourseNameLessThanOrEqualTo(String value) {
            addCriterion("course_name <=", value, "courseName");
            return (Criteria) this;
        }

        public Criteria andCourseNameLike(String value) {
            addCriterion("course_name like", value, "courseName");
            return (Criteria) this;
        }

        public Criteria andCourseNameNotLike(String value) {
            addCriterion("course_name not like", value, "courseName");
            return (Criteria) this;
        }

        public Criteria andCourseNameIn(List<String> values) {
            addCriterion("course_name in", values, "courseName");
            return (Criteria) this;
        }

        public Criteria andCourseNameNotIn(List<String> values) {
            addCriterion("course_name not in", values, "courseName");
            return (Criteria) this;
        }

        public Criteria andCourseNameBetween(String value1, String value2) {
            addCriterion("course_name between", value1, value2, "courseName");
            return (Criteria) this;
        }

        public Criteria andCourseNameNotBetween(String value1, String value2) {
            addCriterion("course_name not between", value1, value2, "courseName");
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

        public Criteria andCourseWeekIsNull() {
            addCriterion("course_week is null");
            return (Criteria) this;
        }

        public Criteria andCourseWeekIsNotNull() {
            addCriterion("course_week is not null");
            return (Criteria) this;
        }

        public Criteria andCourseWeekEqualTo(String value) {
            addCriterion("course_week =", value, "courseWeek");
            return (Criteria) this;
        }

        public Criteria andCourseWeekNotEqualTo(String value) {
            addCriterion("course_week <>", value, "courseWeek");
            return (Criteria) this;
        }

        public Criteria andCourseWeekGreaterThan(String value) {
            addCriterion("course_week >", value, "courseWeek");
            return (Criteria) this;
        }

        public Criteria andCourseWeekGreaterThanOrEqualTo(String value) {
            addCriterion("course_week >=", value, "courseWeek");
            return (Criteria) this;
        }

        public Criteria andCourseWeekLessThan(String value) {
            addCriterion("course_week <", value, "courseWeek");
            return (Criteria) this;
        }

        public Criteria andCourseWeekLessThanOrEqualTo(String value) {
            addCriterion("course_week <=", value, "courseWeek");
            return (Criteria) this;
        }

        public Criteria andCourseWeekLike(String value) {
            addCriterion("course_week like", value, "courseWeek");
            return (Criteria) this;
        }

        public Criteria andCourseWeekNotLike(String value) {
            addCriterion("course_week not like", value, "courseWeek");
            return (Criteria) this;
        }

        public Criteria andCourseWeekIn(List<String> values) {
            addCriterion("course_week in", values, "courseWeek");
            return (Criteria) this;
        }

        public Criteria andCourseWeekNotIn(List<String> values) {
            addCriterion("course_week not in", values, "courseWeek");
            return (Criteria) this;
        }

        public Criteria andCourseWeekBetween(String value1, String value2) {
            addCriterion("course_week between", value1, value2, "courseWeek");
            return (Criteria) this;
        }

        public Criteria andCourseWeekNotBetween(String value1, String value2) {
            addCriterion("course_week not between", value1, value2, "courseWeek");
            return (Criteria) this;
        }

        public Criteria andCourseWeekDayIsNull() {
            addCriterion("course_week_day is null");
            return (Criteria) this;
        }

        public Criteria andCourseWeekDayIsNotNull() {
            addCriterion("course_week_day is not null");
            return (Criteria) this;
        }

        public Criteria andCourseWeekDayEqualTo(String value) {
            addCriterion("course_week_day =", value, "courseWeekDay");
            return (Criteria) this;
        }

        public Criteria andCourseWeekDayNotEqualTo(String value) {
            addCriterion("course_week_day <>", value, "courseWeekDay");
            return (Criteria) this;
        }

        public Criteria andCourseWeekDayGreaterThan(String value) {
            addCriterion("course_week_day >", value, "courseWeekDay");
            return (Criteria) this;
        }

        public Criteria andCourseWeekDayGreaterThanOrEqualTo(String value) {
            addCriterion("course_week_day >=", value, "courseWeekDay");
            return (Criteria) this;
        }

        public Criteria andCourseWeekDayLessThan(String value) {
            addCriterion("course_week_day <", value, "courseWeekDay");
            return (Criteria) this;
        }

        public Criteria andCourseWeekDayLessThanOrEqualTo(String value) {
            addCriterion("course_week_day <=", value, "courseWeekDay");
            return (Criteria) this;
        }

        public Criteria andCourseWeekDayLike(String value) {
            addCriterion("course_week_day like", value, "courseWeekDay");
            return (Criteria) this;
        }

        public Criteria andCourseWeekDayNotLike(String value) {
            addCriterion("course_week_day not like", value, "courseWeekDay");
            return (Criteria) this;
        }

        public Criteria andCourseWeekDayIn(List<String> values) {
            addCriterion("course_week_day in", values, "courseWeekDay");
            return (Criteria) this;
        }

        public Criteria andCourseWeekDayNotIn(List<String> values) {
            addCriterion("course_week_day not in", values, "courseWeekDay");
            return (Criteria) this;
        }

        public Criteria andCourseWeekDayBetween(String value1, String value2) {
            addCriterion("course_week_day between", value1, value2, "courseWeekDay");
            return (Criteria) this;
        }

        public Criteria andCourseWeekDayNotBetween(String value1, String value2) {
            addCriterion("course_week_day not between", value1, value2, "courseWeekDay");
            return (Criteria) this;
        }

        public Criteria andCourseSectionIsNull() {
            addCriterion("course_section is null");
            return (Criteria) this;
        }

        public Criteria andCourseSectionIsNotNull() {
            addCriterion("course_section is not null");
            return (Criteria) this;
        }

        public Criteria andCourseSectionEqualTo(String value) {
            addCriterion("course_section =", value, "courseSection");
            return (Criteria) this;
        }

        public Criteria andCourseSectionNotEqualTo(String value) {
            addCriterion("course_section <>", value, "courseSection");
            return (Criteria) this;
        }

        public Criteria andCourseSectionGreaterThan(String value) {
            addCriterion("course_section >", value, "courseSection");
            return (Criteria) this;
        }

        public Criteria andCourseSectionGreaterThanOrEqualTo(String value) {
            addCriterion("course_section >=", value, "courseSection");
            return (Criteria) this;
        }

        public Criteria andCourseSectionLessThan(String value) {
            addCriterion("course_section <", value, "courseSection");
            return (Criteria) this;
        }

        public Criteria andCourseSectionLessThanOrEqualTo(String value) {
            addCriterion("course_section <=", value, "courseSection");
            return (Criteria) this;
        }

        public Criteria andCourseSectionLike(String value) {
            addCriterion("course_section like", value, "courseSection");
            return (Criteria) this;
        }

        public Criteria andCourseSectionNotLike(String value) {
            addCriterion("course_section not like", value, "courseSection");
            return (Criteria) this;
        }

        public Criteria andCourseSectionIn(List<String> values) {
            addCriterion("course_section in", values, "courseSection");
            return (Criteria) this;
        }

        public Criteria andCourseSectionNotIn(List<String> values) {
            addCriterion("course_section not in", values, "courseSection");
            return (Criteria) this;
        }

        public Criteria andCourseSectionBetween(String value1, String value2) {
            addCriterion("course_section between", value1, value2, "courseSection");
            return (Criteria) this;
        }

        public Criteria andCourseSectionNotBetween(String value1, String value2) {
            addCriterion("course_section not between", value1, value2, "courseSection");
            return (Criteria) this;
        }

        public Criteria andCourseContinueSectionIsNull() {
            addCriterion("course_continue_section is null");
            return (Criteria) this;
        }

        public Criteria andCourseContinueSectionIsNotNull() {
            addCriterion("course_continue_section is not null");
            return (Criteria) this;
        }

        public Criteria andCourseContinueSectionEqualTo(String value) {
            addCriterion("course_continue_section =", value, "courseContinueSection");
            return (Criteria) this;
        }

        public Criteria andCourseContinueSectionNotEqualTo(String value) {
            addCriterion("course_continue_section <>", value, "courseContinueSection");
            return (Criteria) this;
        }

        public Criteria andCourseContinueSectionGreaterThan(String value) {
            addCriterion("course_continue_section >", value, "courseContinueSection");
            return (Criteria) this;
        }

        public Criteria andCourseContinueSectionGreaterThanOrEqualTo(String value) {
            addCriterion("course_continue_section >=", value, "courseContinueSection");
            return (Criteria) this;
        }

        public Criteria andCourseContinueSectionLessThan(String value) {
            addCriterion("course_continue_section <", value, "courseContinueSection");
            return (Criteria) this;
        }

        public Criteria andCourseContinueSectionLessThanOrEqualTo(String value) {
            addCriterion("course_continue_section <=", value, "courseContinueSection");
            return (Criteria) this;
        }

        public Criteria andCourseContinueSectionLike(String value) {
            addCriterion("course_continue_section like", value, "courseContinueSection");
            return (Criteria) this;
        }

        public Criteria andCourseContinueSectionNotLike(String value) {
            addCriterion("course_continue_section not like", value, "courseContinueSection");
            return (Criteria) this;
        }

        public Criteria andCourseContinueSectionIn(List<String> values) {
            addCriterion("course_continue_section in", values, "courseContinueSection");
            return (Criteria) this;
        }

        public Criteria andCourseContinueSectionNotIn(List<String> values) {
            addCriterion("course_continue_section not in", values, "courseContinueSection");
            return (Criteria) this;
        }

        public Criteria andCourseContinueSectionBetween(String value1, String value2) {
            addCriterion("course_continue_section between", value1, value2, "courseContinueSection");
            return (Criteria) this;
        }

        public Criteria andCourseContinueSectionNotBetween(String value1, String value2) {
            addCriterion("course_continue_section not between", value1, value2, "courseContinueSection");
            return (Criteria) this;
        }

        public Criteria andTeacherNoIsNull() {
            addCriterion("teacher_no is null");
            return (Criteria) this;
        }

        public Criteria andTeacherNoIsNotNull() {
            addCriterion("teacher_no is not null");
            return (Criteria) this;
        }

        public Criteria andTeacherNoEqualTo(String value) {
            addCriterion("teacher_no =", value, "teacherNo");
            return (Criteria) this;
        }

        public Criteria andTeacherNoNotEqualTo(String value) {
            addCriterion("teacher_no <>", value, "teacherNo");
            return (Criteria) this;
        }

        public Criteria andTeacherNoGreaterThan(String value) {
            addCriterion("teacher_no >", value, "teacherNo");
            return (Criteria) this;
        }

        public Criteria andTeacherNoGreaterThanOrEqualTo(String value) {
            addCriterion("teacher_no >=", value, "teacherNo");
            return (Criteria) this;
        }

        public Criteria andTeacherNoLessThan(String value) {
            addCriterion("teacher_no <", value, "teacherNo");
            return (Criteria) this;
        }

        public Criteria andTeacherNoLessThanOrEqualTo(String value) {
            addCriterion("teacher_no <=", value, "teacherNo");
            return (Criteria) this;
        }

        public Criteria andTeacherNoLike(String value) {
            addCriterion("teacher_no like", value, "teacherNo");
            return (Criteria) this;
        }

        public Criteria andTeacherNoNotLike(String value) {
            addCriterion("teacher_no not like", value, "teacherNo");
            return (Criteria) this;
        }

        public Criteria andTeacherNoIn(List<String> values) {
            addCriterion("teacher_no in", values, "teacherNo");
            return (Criteria) this;
        }

        public Criteria andTeacherNoNotIn(List<String> values) {
            addCriterion("teacher_no not in", values, "teacherNo");
            return (Criteria) this;
        }

        public Criteria andTeacherNoBetween(String value1, String value2) {
            addCriterion("teacher_no between", value1, value2, "teacherNo");
            return (Criteria) this;
        }

        public Criteria andTeacherNoNotBetween(String value1, String value2) {
            addCriterion("teacher_no not between", value1, value2, "teacherNo");
            return (Criteria) this;
        }

        public Criteria andTeacherNameIsNull() {
            addCriterion("teacher_name is null");
            return (Criteria) this;
        }

        public Criteria andTeacherNameIsNotNull() {
            addCriterion("teacher_name is not null");
            return (Criteria) this;
        }

        public Criteria andTeacherNameEqualTo(String value) {
            addCriterion("teacher_name =", value, "teacherName");
            return (Criteria) this;
        }

        public Criteria andTeacherNameNotEqualTo(String value) {
            addCriterion("teacher_name <>", value, "teacherName");
            return (Criteria) this;
        }

        public Criteria andTeacherNameGreaterThan(String value) {
            addCriterion("teacher_name >", value, "teacherName");
            return (Criteria) this;
        }

        public Criteria andTeacherNameGreaterThanOrEqualTo(String value) {
            addCriterion("teacher_name >=", value, "teacherName");
            return (Criteria) this;
        }

        public Criteria andTeacherNameLessThan(String value) {
            addCriterion("teacher_name <", value, "teacherName");
            return (Criteria) this;
        }

        public Criteria andTeacherNameLessThanOrEqualTo(String value) {
            addCriterion("teacher_name <=", value, "teacherName");
            return (Criteria) this;
        }

        public Criteria andTeacherNameLike(String value) {
            addCriterion("teacher_name like", value, "teacherName");
            return (Criteria) this;
        }

        public Criteria andTeacherNameNotLike(String value) {
            addCriterion("teacher_name not like", value, "teacherName");
            return (Criteria) this;
        }

        public Criteria andTeacherNameIn(List<String> values) {
            addCriterion("teacher_name in", values, "teacherName");
            return (Criteria) this;
        }

        public Criteria andTeacherNameNotIn(List<String> values) {
            addCriterion("teacher_name not in", values, "teacherName");
            return (Criteria) this;
        }

        public Criteria andTeacherNameBetween(String value1, String value2) {
            addCriterion("teacher_name between", value1, value2, "teacherName");
            return (Criteria) this;
        }

        public Criteria andTeacherNameNotBetween(String value1, String value2) {
            addCriterion("teacher_name not between", value1, value2, "teacherName");
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