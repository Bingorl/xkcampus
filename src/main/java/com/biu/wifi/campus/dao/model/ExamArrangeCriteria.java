package com.biu.wifi.campus.dao.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class ExamArrangeCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private Integer limit;

    private Long offset;

    public ExamArrangeCriteria() {
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

        protected void addCriterionForJDBCDate(String condition, Date value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value.getTime()), property);
        }

        protected void addCriterionForJDBCDate(String condition, List<Date> values, String property) {
            if (values == null || values.size() == 0) {
                throw new RuntimeException("Value list for " + property + " cannot be null or empty");
            }
            List<java.sql.Date> dateList = new ArrayList<java.sql.Date>();
            Iterator<Date> iter = values.iterator();
            while (iter.hasNext()) {
                dateList.add(new java.sql.Date(iter.next().getTime()));
            }
            addCriterion(condition, dateList, property);
        }

        protected void addCriterionForJDBCDate(String condition, Date value1, Date value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value1.getTime()), new java.sql.Date(value2.getTime()), property);
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

        public Criteria andClassNoIsNull() {
            addCriterion("class_no is null");
            return (Criteria) this;
        }

        public Criteria andClassNoIsNotNull() {
            addCriterion("class_no is not null");
            return (Criteria) this;
        }

        public Criteria andClassNoEqualTo(String value) {
            addCriterion("class_no =", value, "classNo");
            return (Criteria) this;
        }

        public Criteria andClassNoNotEqualTo(String value) {
            addCriterion("class_no <>", value, "classNo");
            return (Criteria) this;
        }

        public Criteria andClassNoGreaterThan(String value) {
            addCriterion("class_no >", value, "classNo");
            return (Criteria) this;
        }

        public Criteria andClassNoGreaterThanOrEqualTo(String value) {
            addCriterion("class_no >=", value, "classNo");
            return (Criteria) this;
        }

        public Criteria andClassNoLessThan(String value) {
            addCriterion("class_no <", value, "classNo");
            return (Criteria) this;
        }

        public Criteria andClassNoLessThanOrEqualTo(String value) {
            addCriterion("class_no <=", value, "classNo");
            return (Criteria) this;
        }

        public Criteria andClassNoLike(String value) {
            addCriterion("class_no like", value, "classNo");
            return (Criteria) this;
        }

        public Criteria andClassNoNotLike(String value) {
            addCriterion("class_no not like", value, "classNo");
            return (Criteria) this;
        }

        public Criteria andClassNoIn(List<String> values) {
            addCriterion("class_no in", values, "classNo");
            return (Criteria) this;
        }

        public Criteria andClassNoNotIn(List<String> values) {
            addCriterion("class_no not in", values, "classNo");
            return (Criteria) this;
        }

        public Criteria andClassNoBetween(String value1, String value2) {
            addCriterion("class_no between", value1, value2, "classNo");
            return (Criteria) this;
        }

        public Criteria andClassNoNotBetween(String value1, String value2) {
            addCriterion("class_no not between", value1, value2, "classNo");
            return (Criteria) this;
        }

        public Criteria andStudentIdIsNull() {
            addCriterion("student_id is null");
            return (Criteria) this;
        }

        public Criteria andStudentIdIsNotNull() {
            addCriterion("student_id is not null");
            return (Criteria) this;
        }

        public Criteria andStudentIdEqualTo(String value) {
            addCriterion("student_id =", value, "studentId");
            return (Criteria) this;
        }

        public Criteria andStudentIdNotEqualTo(String value) {
            addCriterion("student_id <>", value, "studentId");
            return (Criteria) this;
        }

        public Criteria andStudentIdGreaterThan(String value) {
            addCriterion("student_id >", value, "studentId");
            return (Criteria) this;
        }

        public Criteria andStudentIdGreaterThanOrEqualTo(String value) {
            addCriterion("student_id >=", value, "studentId");
            return (Criteria) this;
        }

        public Criteria andStudentIdLessThan(String value) {
            addCriterion("student_id <", value, "studentId");
            return (Criteria) this;
        }

        public Criteria andStudentIdLessThanOrEqualTo(String value) {
            addCriterion("student_id <=", value, "studentId");
            return (Criteria) this;
        }

        public Criteria andStudentIdLike(String value) {
            addCriterion("student_id like", value, "studentId");
            return (Criteria) this;
        }

        public Criteria andStudentIdNotLike(String value) {
            addCriterion("student_id not like", value, "studentId");
            return (Criteria) this;
        }

        public Criteria andStudentIdIn(List<String> values) {
            addCriterion("student_id in", values, "studentId");
            return (Criteria) this;
        }

        public Criteria andStudentIdNotIn(List<String> values) {
            addCriterion("student_id not in", values, "studentId");
            return (Criteria) this;
        }

        public Criteria andStudentIdBetween(String value1, String value2) {
            addCriterion("student_id between", value1, value2, "studentId");
            return (Criteria) this;
        }

        public Criteria andStudentIdNotBetween(String value1, String value2) {
            addCriterion("student_id not between", value1, value2, "studentId");
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

        public Criteria andOldClassroomNoIsNull() {
            addCriterion("old_classroom_no is null");
            return (Criteria) this;
        }

        public Criteria andOldClassroomNoIsNotNull() {
            addCriterion("old_classroom_no is not null");
            return (Criteria) this;
        }

        public Criteria andOldClassroomNoEqualTo(String value) {
            addCriterion("old_classroom_no =", value, "oldClassroomNo");
            return (Criteria) this;
        }

        public Criteria andOldClassroomNoNotEqualTo(String value) {
            addCriterion("old_classroom_no <>", value, "oldClassroomNo");
            return (Criteria) this;
        }

        public Criteria andOldClassroomNoGreaterThan(String value) {
            addCriterion("old_classroom_no >", value, "oldClassroomNo");
            return (Criteria) this;
        }

        public Criteria andOldClassroomNoGreaterThanOrEqualTo(String value) {
            addCriterion("old_classroom_no >=", value, "oldClassroomNo");
            return (Criteria) this;
        }

        public Criteria andOldClassroomNoLessThan(String value) {
            addCriterion("old_classroom_no <", value, "oldClassroomNo");
            return (Criteria) this;
        }

        public Criteria andOldClassroomNoLessThanOrEqualTo(String value) {
            addCriterion("old_classroom_no <=", value, "oldClassroomNo");
            return (Criteria) this;
        }

        public Criteria andOldClassroomNoLike(String value) {
            addCriterion("old_classroom_no like", value, "oldClassroomNo");
            return (Criteria) this;
        }

        public Criteria andOldClassroomNoNotLike(String value) {
            addCriterion("old_classroom_no not like", value, "oldClassroomNo");
            return (Criteria) this;
        }

        public Criteria andOldClassroomNoIn(List<String> values) {
            addCriterion("old_classroom_no in", values, "oldClassroomNo");
            return (Criteria) this;
        }

        public Criteria andOldClassroomNoNotIn(List<String> values) {
            addCriterion("old_classroom_no not in", values, "oldClassroomNo");
            return (Criteria) this;
        }

        public Criteria andOldClassroomNoBetween(String value1, String value2) {
            addCriterion("old_classroom_no between", value1, value2, "oldClassroomNo");
            return (Criteria) this;
        }

        public Criteria andOldClassroomNoNotBetween(String value1, String value2) {
            addCriterion("old_classroom_no not between", value1, value2, "oldClassroomNo");
            return (Criteria) this;
        }

        public Criteria andCourseEndTimeIsNull() {
            addCriterion("course_end_time is null");
            return (Criteria) this;
        }

        public Criteria andCourseEndTimeIsNotNull() {
            addCriterion("course_end_time is not null");
            return (Criteria) this;
        }

        public Criteria andCourseEndTimeEqualTo(Date value) {
            addCriterionForJDBCDate("course_end_time =", value, "courseEndTime");
            return (Criteria) this;
        }

        public Criteria andCourseEndTimeNotEqualTo(Date value) {
            addCriterionForJDBCDate("course_end_time <>", value, "courseEndTime");
            return (Criteria) this;
        }

        public Criteria andCourseEndTimeGreaterThan(Date value) {
            addCriterionForJDBCDate("course_end_time >", value, "courseEndTime");
            return (Criteria) this;
        }

        public Criteria andCourseEndTimeGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("course_end_time >=", value, "courseEndTime");
            return (Criteria) this;
        }

        public Criteria andCourseEndTimeLessThan(Date value) {
            addCriterionForJDBCDate("course_end_time <", value, "courseEndTime");
            return (Criteria) this;
        }

        public Criteria andCourseEndTimeLessThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("course_end_time <=", value, "courseEndTime");
            return (Criteria) this;
        }

        public Criteria andCourseEndTimeIn(List<Date> values) {
            addCriterionForJDBCDate("course_end_time in", values, "courseEndTime");
            return (Criteria) this;
        }

        public Criteria andCourseEndTimeNotIn(List<Date> values) {
            addCriterionForJDBCDate("course_end_time not in", values, "courseEndTime");
            return (Criteria) this;
        }

        public Criteria andCourseEndTimeBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("course_end_time between", value1, value2, "courseEndTime");
            return (Criteria) this;
        }

        public Criteria andCourseEndTimeNotBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("course_end_time not between", value1, value2, "courseEndTime");
            return (Criteria) this;
        }

        public Criteria andCourseEndTimeWeekIndexIsNull() {
            addCriterion("course_end_time_week_index is null");
            return (Criteria) this;
        }

        public Criteria andCourseEndTimeWeekIndexIsNotNull() {
            addCriterion("course_end_time_week_index is not null");
            return (Criteria) this;
        }

        public Criteria andCourseEndTimeWeekIndexEqualTo(Integer value) {
            addCriterion("course_end_time_week_index =", value, "courseEndTimeWeekIndex");
            return (Criteria) this;
        }

        public Criteria andCourseEndTimeWeekIndexNotEqualTo(Integer value) {
            addCriterion("course_end_time_week_index <>", value, "courseEndTimeWeekIndex");
            return (Criteria) this;
        }

        public Criteria andCourseEndTimeWeekIndexGreaterThan(Integer value) {
            addCriterion("course_end_time_week_index >", value, "courseEndTimeWeekIndex");
            return (Criteria) this;
        }

        public Criteria andCourseEndTimeWeekIndexGreaterThanOrEqualTo(Integer value) {
            addCriterion("course_end_time_week_index >=", value, "courseEndTimeWeekIndex");
            return (Criteria) this;
        }

        public Criteria andCourseEndTimeWeekIndexLessThan(Integer value) {
            addCriterion("course_end_time_week_index <", value, "courseEndTimeWeekIndex");
            return (Criteria) this;
        }

        public Criteria andCourseEndTimeWeekIndexLessThanOrEqualTo(Integer value) {
            addCriterion("course_end_time_week_index <=", value, "courseEndTimeWeekIndex");
            return (Criteria) this;
        }

        public Criteria andCourseEndTimeWeekIndexIn(List<Integer> values) {
            addCriterion("course_end_time_week_index in", values, "courseEndTimeWeekIndex");
            return (Criteria) this;
        }

        public Criteria andCourseEndTimeWeekIndexNotIn(List<Integer> values) {
            addCriterion("course_end_time_week_index not in", values, "courseEndTimeWeekIndex");
            return (Criteria) this;
        }

        public Criteria andCourseEndTimeWeekIndexBetween(Integer value1, Integer value2) {
            addCriterion("course_end_time_week_index between", value1, value2, "courseEndTimeWeekIndex");
            return (Criteria) this;
        }

        public Criteria andCourseEndTimeWeekIndexNotBetween(Integer value1, Integer value2) {
            addCriterion("course_end_time_week_index not between", value1, value2, "courseEndTimeWeekIndex");
            return (Criteria) this;
        }

        public Criteria andCourseEndTimeWeekNameIsNull() {
            addCriterion("course_end_time_week_name is null");
            return (Criteria) this;
        }

        public Criteria andCourseEndTimeWeekNameIsNotNull() {
            addCriterion("course_end_time_week_name is not null");
            return (Criteria) this;
        }

        public Criteria andCourseEndTimeWeekNameEqualTo(String value) {
            addCriterion("course_end_time_week_name =", value, "courseEndTimeWeekName");
            return (Criteria) this;
        }

        public Criteria andCourseEndTimeWeekNameNotEqualTo(String value) {
            addCriterion("course_end_time_week_name <>", value, "courseEndTimeWeekName");
            return (Criteria) this;
        }

        public Criteria andCourseEndTimeWeekNameGreaterThan(String value) {
            addCriterion("course_end_time_week_name >", value, "courseEndTimeWeekName");
            return (Criteria) this;
        }

        public Criteria andCourseEndTimeWeekNameGreaterThanOrEqualTo(String value) {
            addCriterion("course_end_time_week_name >=", value, "courseEndTimeWeekName");
            return (Criteria) this;
        }

        public Criteria andCourseEndTimeWeekNameLessThan(String value) {
            addCriterion("course_end_time_week_name <", value, "courseEndTimeWeekName");
            return (Criteria) this;
        }

        public Criteria andCourseEndTimeWeekNameLessThanOrEqualTo(String value) {
            addCriterion("course_end_time_week_name <=", value, "courseEndTimeWeekName");
            return (Criteria) this;
        }

        public Criteria andCourseEndTimeWeekNameLike(String value) {
            addCriterion("course_end_time_week_name like", value, "courseEndTimeWeekName");
            return (Criteria) this;
        }

        public Criteria andCourseEndTimeWeekNameNotLike(String value) {
            addCriterion("course_end_time_week_name not like", value, "courseEndTimeWeekName");
            return (Criteria) this;
        }

        public Criteria andCourseEndTimeWeekNameIn(List<String> values) {
            addCriterion("course_end_time_week_name in", values, "courseEndTimeWeekName");
            return (Criteria) this;
        }

        public Criteria andCourseEndTimeWeekNameNotIn(List<String> values) {
            addCriterion("course_end_time_week_name not in", values, "courseEndTimeWeekName");
            return (Criteria) this;
        }

        public Criteria andCourseEndTimeWeekNameBetween(String value1, String value2) {
            addCriterion("course_end_time_week_name between", value1, value2, "courseEndTimeWeekName");
            return (Criteria) this;
        }

        public Criteria andCourseEndTimeWeekNameNotBetween(String value1, String value2) {
            addCriterion("course_end_time_week_name not between", value1, value2, "courseEndTimeWeekName");
            return (Criteria) this;
        }

        public Criteria andCourseEndTimeSectionIsNull() {
            addCriterion("course_end_time_section is null");
            return (Criteria) this;
        }

        public Criteria andCourseEndTimeSectionIsNotNull() {
            addCriterion("course_end_time_section is not null");
            return (Criteria) this;
        }

        public Criteria andCourseEndTimeSectionEqualTo(String value) {
            addCriterion("course_end_time_section =", value, "courseEndTimeSection");
            return (Criteria) this;
        }

        public Criteria andCourseEndTimeSectionNotEqualTo(String value) {
            addCriterion("course_end_time_section <>", value, "courseEndTimeSection");
            return (Criteria) this;
        }

        public Criteria andCourseEndTimeSectionGreaterThan(String value) {
            addCriterion("course_end_time_section >", value, "courseEndTimeSection");
            return (Criteria) this;
        }

        public Criteria andCourseEndTimeSectionGreaterThanOrEqualTo(String value) {
            addCriterion("course_end_time_section >=", value, "courseEndTimeSection");
            return (Criteria) this;
        }

        public Criteria andCourseEndTimeSectionLessThan(String value) {
            addCriterion("course_end_time_section <", value, "courseEndTimeSection");
            return (Criteria) this;
        }

        public Criteria andCourseEndTimeSectionLessThanOrEqualTo(String value) {
            addCriterion("course_end_time_section <=", value, "courseEndTimeSection");
            return (Criteria) this;
        }

        public Criteria andCourseEndTimeSectionLike(String value) {
            addCriterion("course_end_time_section like", value, "courseEndTimeSection");
            return (Criteria) this;
        }

        public Criteria andCourseEndTimeSectionNotLike(String value) {
            addCriterion("course_end_time_section not like", value, "courseEndTimeSection");
            return (Criteria) this;
        }

        public Criteria andCourseEndTimeSectionIn(List<String> values) {
            addCriterion("course_end_time_section in", values, "courseEndTimeSection");
            return (Criteria) this;
        }

        public Criteria andCourseEndTimeSectionNotIn(List<String> values) {
            addCriterion("course_end_time_section not in", values, "courseEndTimeSection");
            return (Criteria) this;
        }

        public Criteria andCourseEndTimeSectionBetween(String value1, String value2) {
            addCriterion("course_end_time_section between", value1, value2, "courseEndTimeSection");
            return (Criteria) this;
        }

        public Criteria andCourseEndTimeSectionNotBetween(String value1, String value2) {
            addCriterion("course_end_time_section not between", value1, value2, "courseEndTimeSection");
            return (Criteria) this;
        }

        public Criteria andInvigilateTeacherIdIsNull() {
            addCriterion("invigilate_teacher_id is null");
            return (Criteria) this;
        }

        public Criteria andInvigilateTeacherIdIsNotNull() {
            addCriterion("invigilate_teacher_id is not null");
            return (Criteria) this;
        }

        public Criteria andInvigilateTeacherIdEqualTo(String value) {
            addCriterion("invigilate_teacher_id =", value, "invigilateTeacherId");
            return (Criteria) this;
        }

        public Criteria andInvigilateTeacherIdNotEqualTo(String value) {
            addCriterion("invigilate_teacher_id <>", value, "invigilateTeacherId");
            return (Criteria) this;
        }

        public Criteria andInvigilateTeacherIdGreaterThan(String value) {
            addCriterion("invigilate_teacher_id >", value, "invigilateTeacherId");
            return (Criteria) this;
        }

        public Criteria andInvigilateTeacherIdGreaterThanOrEqualTo(String value) {
            addCriterion("invigilate_teacher_id >=", value, "invigilateTeacherId");
            return (Criteria) this;
        }

        public Criteria andInvigilateTeacherIdLessThan(String value) {
            addCriterion("invigilate_teacher_id <", value, "invigilateTeacherId");
            return (Criteria) this;
        }

        public Criteria andInvigilateTeacherIdLessThanOrEqualTo(String value) {
            addCriterion("invigilate_teacher_id <=", value, "invigilateTeacherId");
            return (Criteria) this;
        }

        public Criteria andInvigilateTeacherIdLike(String value) {
            addCriterion("invigilate_teacher_id like", value, "invigilateTeacherId");
            return (Criteria) this;
        }

        public Criteria andInvigilateTeacherIdNotLike(String value) {
            addCriterion("invigilate_teacher_id not like", value, "invigilateTeacherId");
            return (Criteria) this;
        }

        public Criteria andInvigilateTeacherIdIn(List<String> values) {
            addCriterion("invigilate_teacher_id in", values, "invigilateTeacherId");
            return (Criteria) this;
        }

        public Criteria andInvigilateTeacherIdNotIn(List<String> values) {
            addCriterion("invigilate_teacher_id not in", values, "invigilateTeacherId");
            return (Criteria) this;
        }

        public Criteria andInvigilateTeacherIdBetween(String value1, String value2) {
            addCriterion("invigilate_teacher_id between", value1, value2, "invigilateTeacherId");
            return (Criteria) this;
        }

        public Criteria andInvigilateTeacherIdNotBetween(String value1, String value2) {
            addCriterion("invigilate_teacher_id not between", value1, value2, "invigilateTeacherId");
            return (Criteria) this;
        }

        public Criteria andPatrolTeacherIdIsNull() {
            addCriterion("patrol_teacher_id is null");
            return (Criteria) this;
        }

        public Criteria andPatrolTeacherIdIsNotNull() {
            addCriterion("patrol_teacher_id is not null");
            return (Criteria) this;
        }

        public Criteria andPatrolTeacherIdEqualTo(String value) {
            addCriterion("patrol_teacher_id =", value, "patrolTeacherId");
            return (Criteria) this;
        }

        public Criteria andPatrolTeacherIdNotEqualTo(String value) {
            addCriterion("patrol_teacher_id <>", value, "patrolTeacherId");
            return (Criteria) this;
        }

        public Criteria andPatrolTeacherIdGreaterThan(String value) {
            addCriterion("patrol_teacher_id >", value, "patrolTeacherId");
            return (Criteria) this;
        }

        public Criteria andPatrolTeacherIdGreaterThanOrEqualTo(String value) {
            addCriterion("patrol_teacher_id >=", value, "patrolTeacherId");
            return (Criteria) this;
        }

        public Criteria andPatrolTeacherIdLessThan(String value) {
            addCriterion("patrol_teacher_id <", value, "patrolTeacherId");
            return (Criteria) this;
        }

        public Criteria andPatrolTeacherIdLessThanOrEqualTo(String value) {
            addCriterion("patrol_teacher_id <=", value, "patrolTeacherId");
            return (Criteria) this;
        }

        public Criteria andPatrolTeacherIdLike(String value) {
            addCriterion("patrol_teacher_id like", value, "patrolTeacherId");
            return (Criteria) this;
        }

        public Criteria andPatrolTeacherIdNotLike(String value) {
            addCriterion("patrol_teacher_id not like", value, "patrolTeacherId");
            return (Criteria) this;
        }

        public Criteria andPatrolTeacherIdIn(List<String> values) {
            addCriterion("patrol_teacher_id in", values, "patrolTeacherId");
            return (Criteria) this;
        }

        public Criteria andPatrolTeacherIdNotIn(List<String> values) {
            addCriterion("patrol_teacher_id not in", values, "patrolTeacherId");
            return (Criteria) this;
        }

        public Criteria andPatrolTeacherIdBetween(String value1, String value2) {
            addCriterion("patrol_teacher_id between", value1, value2, "patrolTeacherId");
            return (Criteria) this;
        }

        public Criteria andPatrolTeacherIdNotBetween(String value1, String value2) {
            addCriterion("patrol_teacher_id not between", value1, value2, "patrolTeacherId");
            return (Criteria) this;
        }

        public Criteria andExamTimeIsNull() {
            addCriterion("exam_time is null");
            return (Criteria) this;
        }

        public Criteria andExamTimeIsNotNull() {
            addCriterion("exam_time is not null");
            return (Criteria) this;
        }

        public Criteria andExamTimeEqualTo(Date value) {
            addCriterionForJDBCDate("exam_time =", value, "examTime");
            return (Criteria) this;
        }

        public Criteria andExamTimeNotEqualTo(Date value) {
            addCriterionForJDBCDate("exam_time <>", value, "examTime");
            return (Criteria) this;
        }

        public Criteria andExamTimeGreaterThan(Date value) {
            addCriterionForJDBCDate("exam_time >", value, "examTime");
            return (Criteria) this;
        }

        public Criteria andExamTimeGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("exam_time >=", value, "examTime");
            return (Criteria) this;
        }

        public Criteria andExamTimeLessThan(Date value) {
            addCriterionForJDBCDate("exam_time <", value, "examTime");
            return (Criteria) this;
        }

        public Criteria andExamTimeLessThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("exam_time <=", value, "examTime");
            return (Criteria) this;
        }

        public Criteria andExamTimeIn(List<Date> values) {
            addCriterionForJDBCDate("exam_time in", values, "examTime");
            return (Criteria) this;
        }

        public Criteria andExamTimeNotIn(List<Date> values) {
            addCriterionForJDBCDate("exam_time not in", values, "examTime");
            return (Criteria) this;
        }

        public Criteria andExamTimeBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("exam_time between", value1, value2, "examTime");
            return (Criteria) this;
        }

        public Criteria andExamTimeNotBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("exam_time not between", value1, value2, "examTime");
            return (Criteria) this;
        }

        public Criteria andExamTimeWeekIndexIsNull() {
            addCriterion("exam_time_week_index is null");
            return (Criteria) this;
        }

        public Criteria andExamTimeWeekIndexIsNotNull() {
            addCriterion("exam_time_week_index is not null");
            return (Criteria) this;
        }

        public Criteria andExamTimeWeekIndexEqualTo(Integer value) {
            addCriterion("exam_time_week_index =", value, "examTimeWeekIndex");
            return (Criteria) this;
        }

        public Criteria andExamTimeWeekIndexNotEqualTo(Integer value) {
            addCriterion("exam_time_week_index <>", value, "examTimeWeekIndex");
            return (Criteria) this;
        }

        public Criteria andExamTimeWeekIndexGreaterThan(Integer value) {
            addCriterion("exam_time_week_index >", value, "examTimeWeekIndex");
            return (Criteria) this;
        }

        public Criteria andExamTimeWeekIndexGreaterThanOrEqualTo(Integer value) {
            addCriterion("exam_time_week_index >=", value, "examTimeWeekIndex");
            return (Criteria) this;
        }

        public Criteria andExamTimeWeekIndexLessThan(Integer value) {
            addCriterion("exam_time_week_index <", value, "examTimeWeekIndex");
            return (Criteria) this;
        }

        public Criteria andExamTimeWeekIndexLessThanOrEqualTo(Integer value) {
            addCriterion("exam_time_week_index <=", value, "examTimeWeekIndex");
            return (Criteria) this;
        }

        public Criteria andExamTimeWeekIndexIn(List<Integer> values) {
            addCriterion("exam_time_week_index in", values, "examTimeWeekIndex");
            return (Criteria) this;
        }

        public Criteria andExamTimeWeekIndexNotIn(List<Integer> values) {
            addCriterion("exam_time_week_index not in", values, "examTimeWeekIndex");
            return (Criteria) this;
        }

        public Criteria andExamTimeWeekIndexBetween(Integer value1, Integer value2) {
            addCriterion("exam_time_week_index between", value1, value2, "examTimeWeekIndex");
            return (Criteria) this;
        }

        public Criteria andExamTimeWeekIndexNotBetween(Integer value1, Integer value2) {
            addCriterion("exam_time_week_index not between", value1, value2, "examTimeWeekIndex");
            return (Criteria) this;
        }

        public Criteria andExamTimeWeekNameIsNull() {
            addCriterion("exam_time_week_name is null");
            return (Criteria) this;
        }

        public Criteria andExamTimeWeekNameIsNotNull() {
            addCriterion("exam_time_week_name is not null");
            return (Criteria) this;
        }

        public Criteria andExamTimeWeekNameEqualTo(String value) {
            addCriterion("exam_time_week_name =", value, "examTimeWeekName");
            return (Criteria) this;
        }

        public Criteria andExamTimeWeekNameNotEqualTo(String value) {
            addCriterion("exam_time_week_name <>", value, "examTimeWeekName");
            return (Criteria) this;
        }

        public Criteria andExamTimeWeekNameGreaterThan(String value) {
            addCriterion("exam_time_week_name >", value, "examTimeWeekName");
            return (Criteria) this;
        }

        public Criteria andExamTimeWeekNameGreaterThanOrEqualTo(String value) {
            addCriterion("exam_time_week_name >=", value, "examTimeWeekName");
            return (Criteria) this;
        }

        public Criteria andExamTimeWeekNameLessThan(String value) {
            addCriterion("exam_time_week_name <", value, "examTimeWeekName");
            return (Criteria) this;
        }

        public Criteria andExamTimeWeekNameLessThanOrEqualTo(String value) {
            addCriterion("exam_time_week_name <=", value, "examTimeWeekName");
            return (Criteria) this;
        }

        public Criteria andExamTimeWeekNameLike(String value) {
            addCriterion("exam_time_week_name like", value, "examTimeWeekName");
            return (Criteria) this;
        }

        public Criteria andExamTimeWeekNameNotLike(String value) {
            addCriterion("exam_time_week_name not like", value, "examTimeWeekName");
            return (Criteria) this;
        }

        public Criteria andExamTimeWeekNameIn(List<String> values) {
            addCriterion("exam_time_week_name in", values, "examTimeWeekName");
            return (Criteria) this;
        }

        public Criteria andExamTimeWeekNameNotIn(List<String> values) {
            addCriterion("exam_time_week_name not in", values, "examTimeWeekName");
            return (Criteria) this;
        }

        public Criteria andExamTimeWeekNameBetween(String value1, String value2) {
            addCriterion("exam_time_week_name between", value1, value2, "examTimeWeekName");
            return (Criteria) this;
        }

        public Criteria andExamTimeWeekNameNotBetween(String value1, String value2) {
            addCriterion("exam_time_week_name not between", value1, value2, "examTimeWeekName");
            return (Criteria) this;
        }

        public Criteria andExamTimeSectionIsNull() {
            addCriterion("exam_time_section is null");
            return (Criteria) this;
        }

        public Criteria andExamTimeSectionIsNotNull() {
            addCriterion("exam_time_section is not null");
            return (Criteria) this;
        }

        public Criteria andExamTimeSectionEqualTo(String value) {
            addCriterion("exam_time_section =", value, "examTimeSection");
            return (Criteria) this;
        }

        public Criteria andExamTimeSectionNotEqualTo(String value) {
            addCriterion("exam_time_section <>", value, "examTimeSection");
            return (Criteria) this;
        }

        public Criteria andExamTimeSectionGreaterThan(String value) {
            addCriterion("exam_time_section >", value, "examTimeSection");
            return (Criteria) this;
        }

        public Criteria andExamTimeSectionGreaterThanOrEqualTo(String value) {
            addCriterion("exam_time_section >=", value, "examTimeSection");
            return (Criteria) this;
        }

        public Criteria andExamTimeSectionLessThan(String value) {
            addCriterion("exam_time_section <", value, "examTimeSection");
            return (Criteria) this;
        }

        public Criteria andExamTimeSectionLessThanOrEqualTo(String value) {
            addCriterion("exam_time_section <=", value, "examTimeSection");
            return (Criteria) this;
        }

        public Criteria andExamTimeSectionLike(String value) {
            addCriterion("exam_time_section like", value, "examTimeSection");
            return (Criteria) this;
        }

        public Criteria andExamTimeSectionNotLike(String value) {
            addCriterion("exam_time_section not like", value, "examTimeSection");
            return (Criteria) this;
        }

        public Criteria andExamTimeSectionIn(List<String> values) {
            addCriterion("exam_time_section in", values, "examTimeSection");
            return (Criteria) this;
        }

        public Criteria andExamTimeSectionNotIn(List<String> values) {
            addCriterion("exam_time_section not in", values, "examTimeSection");
            return (Criteria) this;
        }

        public Criteria andExamTimeSectionBetween(String value1, String value2) {
            addCriterion("exam_time_section between", value1, value2, "examTimeSection");
            return (Criteria) this;
        }

        public Criteria andExamTimeSectionNotBetween(String value1, String value2) {
            addCriterion("exam_time_section not between", value1, value2, "examTimeSection");
            return (Criteria) this;
        }

        public Criteria andExamTimePeriodIsNull() {
            addCriterion("exam_time_period is null");
            return (Criteria) this;
        }

        public Criteria andExamTimePeriodIsNotNull() {
            addCriterion("exam_time_period is not null");
            return (Criteria) this;
        }

        public Criteria andExamTimePeriodEqualTo(String value) {
            addCriterion("exam_time_period =", value, "examTimePeriod");
            return (Criteria) this;
        }

        public Criteria andExamTimePeriodNotEqualTo(String value) {
            addCriterion("exam_time_period <>", value, "examTimePeriod");
            return (Criteria) this;
        }

        public Criteria andExamTimePeriodGreaterThan(String value) {
            addCriterion("exam_time_period >", value, "examTimePeriod");
            return (Criteria) this;
        }

        public Criteria andExamTimePeriodGreaterThanOrEqualTo(String value) {
            addCriterion("exam_time_period >=", value, "examTimePeriod");
            return (Criteria) this;
        }

        public Criteria andExamTimePeriodLessThan(String value) {
            addCriterion("exam_time_period <", value, "examTimePeriod");
            return (Criteria) this;
        }

        public Criteria andExamTimePeriodLessThanOrEqualTo(String value) {
            addCriterion("exam_time_period <=", value, "examTimePeriod");
            return (Criteria) this;
        }

        public Criteria andExamTimePeriodLike(String value) {
            addCriterion("exam_time_period like", value, "examTimePeriod");
            return (Criteria) this;
        }

        public Criteria andExamTimePeriodNotLike(String value) {
            addCriterion("exam_time_period not like", value, "examTimePeriod");
            return (Criteria) this;
        }

        public Criteria andExamTimePeriodIn(List<String> values) {
            addCriterion("exam_time_period in", values, "examTimePeriod");
            return (Criteria) this;
        }

        public Criteria andExamTimePeriodNotIn(List<String> values) {
            addCriterion("exam_time_period not in", values, "examTimePeriod");
            return (Criteria) this;
        }

        public Criteria andExamTimePeriodBetween(String value1, String value2) {
            addCriterion("exam_time_period between", value1, value2, "examTimePeriod");
            return (Criteria) this;
        }

        public Criteria andExamTimePeriodNotBetween(String value1, String value2) {
            addCriterion("exam_time_period not between", value1, value2, "examTimePeriod");
            return (Criteria) this;
        }

        public Criteria andOldExamTimeIsNull() {
            addCriterion("old_exam_time is null");
            return (Criteria) this;
        }

        public Criteria andOldExamTimeIsNotNull() {
            addCriterion("old_exam_time is not null");
            return (Criteria) this;
        }

        public Criteria andOldExamTimeEqualTo(Date value) {
            addCriterionForJDBCDate("old_exam_time =", value, "oldExamTime");
            return (Criteria) this;
        }

        public Criteria andOldExamTimeNotEqualTo(Date value) {
            addCriterionForJDBCDate("old_exam_time <>", value, "oldExamTime");
            return (Criteria) this;
        }

        public Criteria andOldExamTimeGreaterThan(Date value) {
            addCriterionForJDBCDate("old_exam_time >", value, "oldExamTime");
            return (Criteria) this;
        }

        public Criteria andOldExamTimeGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("old_exam_time >=", value, "oldExamTime");
            return (Criteria) this;
        }

        public Criteria andOldExamTimeLessThan(Date value) {
            addCriterionForJDBCDate("old_exam_time <", value, "oldExamTime");
            return (Criteria) this;
        }

        public Criteria andOldExamTimeLessThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("old_exam_time <=", value, "oldExamTime");
            return (Criteria) this;
        }

        public Criteria andOldExamTimeIn(List<Date> values) {
            addCriterionForJDBCDate("old_exam_time in", values, "oldExamTime");
            return (Criteria) this;
        }

        public Criteria andOldExamTimeNotIn(List<Date> values) {
            addCriterionForJDBCDate("old_exam_time not in", values, "oldExamTime");
            return (Criteria) this;
        }

        public Criteria andOldExamTimeBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("old_exam_time between", value1, value2, "oldExamTime");
            return (Criteria) this;
        }

        public Criteria andOldExamTimeNotBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("old_exam_time not between", value1, value2, "oldExamTime");
            return (Criteria) this;
        }

        public Criteria andOldExamTimeWeekIndexIsNull() {
            addCriterion("old_exam_time_week_index is null");
            return (Criteria) this;
        }

        public Criteria andOldExamTimeWeekIndexIsNotNull() {
            addCriterion("old_exam_time_week_index is not null");
            return (Criteria) this;
        }

        public Criteria andOldExamTimeWeekIndexEqualTo(Integer value) {
            addCriterion("old_exam_time_week_index =", value, "oldExamTimeWeekIndex");
            return (Criteria) this;
        }

        public Criteria andOldExamTimeWeekIndexNotEqualTo(Integer value) {
            addCriterion("old_exam_time_week_index <>", value, "oldExamTimeWeekIndex");
            return (Criteria) this;
        }

        public Criteria andOldExamTimeWeekIndexGreaterThan(Integer value) {
            addCriterion("old_exam_time_week_index >", value, "oldExamTimeWeekIndex");
            return (Criteria) this;
        }

        public Criteria andOldExamTimeWeekIndexGreaterThanOrEqualTo(Integer value) {
            addCriterion("old_exam_time_week_index >=", value, "oldExamTimeWeekIndex");
            return (Criteria) this;
        }

        public Criteria andOldExamTimeWeekIndexLessThan(Integer value) {
            addCriterion("old_exam_time_week_index <", value, "oldExamTimeWeekIndex");
            return (Criteria) this;
        }

        public Criteria andOldExamTimeWeekIndexLessThanOrEqualTo(Integer value) {
            addCriterion("old_exam_time_week_index <=", value, "oldExamTimeWeekIndex");
            return (Criteria) this;
        }

        public Criteria andOldExamTimeWeekIndexIn(List<Integer> values) {
            addCriterion("old_exam_time_week_index in", values, "oldExamTimeWeekIndex");
            return (Criteria) this;
        }

        public Criteria andOldExamTimeWeekIndexNotIn(List<Integer> values) {
            addCriterion("old_exam_time_week_index not in", values, "oldExamTimeWeekIndex");
            return (Criteria) this;
        }

        public Criteria andOldExamTimeWeekIndexBetween(Integer value1, Integer value2) {
            addCriterion("old_exam_time_week_index between", value1, value2, "oldExamTimeWeekIndex");
            return (Criteria) this;
        }

        public Criteria andOldExamTimeWeekIndexNotBetween(Integer value1, Integer value2) {
            addCriterion("old_exam_time_week_index not between", value1, value2, "oldExamTimeWeekIndex");
            return (Criteria) this;
        }

        public Criteria andOldExamTimeWeekNameIsNull() {
            addCriterion("old_exam_time_week_name is null");
            return (Criteria) this;
        }

        public Criteria andOldExamTimeWeekNameIsNotNull() {
            addCriterion("old_exam_time_week_name is not null");
            return (Criteria) this;
        }

        public Criteria andOldExamTimeWeekNameEqualTo(String value) {
            addCriterion("old_exam_time_week_name =", value, "oldExamTimeWeekName");
            return (Criteria) this;
        }

        public Criteria andOldExamTimeWeekNameNotEqualTo(String value) {
            addCriterion("old_exam_time_week_name <>", value, "oldExamTimeWeekName");
            return (Criteria) this;
        }

        public Criteria andOldExamTimeWeekNameGreaterThan(String value) {
            addCriterion("old_exam_time_week_name >", value, "oldExamTimeWeekName");
            return (Criteria) this;
        }

        public Criteria andOldExamTimeWeekNameGreaterThanOrEqualTo(String value) {
            addCriterion("old_exam_time_week_name >=", value, "oldExamTimeWeekName");
            return (Criteria) this;
        }

        public Criteria andOldExamTimeWeekNameLessThan(String value) {
            addCriterion("old_exam_time_week_name <", value, "oldExamTimeWeekName");
            return (Criteria) this;
        }

        public Criteria andOldExamTimeWeekNameLessThanOrEqualTo(String value) {
            addCriterion("old_exam_time_week_name <=", value, "oldExamTimeWeekName");
            return (Criteria) this;
        }

        public Criteria andOldExamTimeWeekNameLike(String value) {
            addCriterion("old_exam_time_week_name like", value, "oldExamTimeWeekName");
            return (Criteria) this;
        }

        public Criteria andOldExamTimeWeekNameNotLike(String value) {
            addCriterion("old_exam_time_week_name not like", value, "oldExamTimeWeekName");
            return (Criteria) this;
        }

        public Criteria andOldExamTimeWeekNameIn(List<String> values) {
            addCriterion("old_exam_time_week_name in", values, "oldExamTimeWeekName");
            return (Criteria) this;
        }

        public Criteria andOldExamTimeWeekNameNotIn(List<String> values) {
            addCriterion("old_exam_time_week_name not in", values, "oldExamTimeWeekName");
            return (Criteria) this;
        }

        public Criteria andOldExamTimeWeekNameBetween(String value1, String value2) {
            addCriterion("old_exam_time_week_name between", value1, value2, "oldExamTimeWeekName");
            return (Criteria) this;
        }

        public Criteria andOldExamTimeWeekNameNotBetween(String value1, String value2) {
            addCriterion("old_exam_time_week_name not between", value1, value2, "oldExamTimeWeekName");
            return (Criteria) this;
        }

        public Criteria andOldExamTimeSectionIsNull() {
            addCriterion("old_exam_time_section is null");
            return (Criteria) this;
        }

        public Criteria andOldExamTimeSectionIsNotNull() {
            addCriterion("old_exam_time_section is not null");
            return (Criteria) this;
        }

        public Criteria andOldExamTimeSectionEqualTo(String value) {
            addCriterion("old_exam_time_section =", value, "oldExamTimeSection");
            return (Criteria) this;
        }

        public Criteria andOldExamTimeSectionNotEqualTo(String value) {
            addCriterion("old_exam_time_section <>", value, "oldExamTimeSection");
            return (Criteria) this;
        }

        public Criteria andOldExamTimeSectionGreaterThan(String value) {
            addCriterion("old_exam_time_section >", value, "oldExamTimeSection");
            return (Criteria) this;
        }

        public Criteria andOldExamTimeSectionGreaterThanOrEqualTo(String value) {
            addCriterion("old_exam_time_section >=", value, "oldExamTimeSection");
            return (Criteria) this;
        }

        public Criteria andOldExamTimeSectionLessThan(String value) {
            addCriterion("old_exam_time_section <", value, "oldExamTimeSection");
            return (Criteria) this;
        }

        public Criteria andOldExamTimeSectionLessThanOrEqualTo(String value) {
            addCriterion("old_exam_time_section <=", value, "oldExamTimeSection");
            return (Criteria) this;
        }

        public Criteria andOldExamTimeSectionLike(String value) {
            addCriterion("old_exam_time_section like", value, "oldExamTimeSection");
            return (Criteria) this;
        }

        public Criteria andOldExamTimeSectionNotLike(String value) {
            addCriterion("old_exam_time_section not like", value, "oldExamTimeSection");
            return (Criteria) this;
        }

        public Criteria andOldExamTimeSectionIn(List<String> values) {
            addCriterion("old_exam_time_section in", values, "oldExamTimeSection");
            return (Criteria) this;
        }

        public Criteria andOldExamTimeSectionNotIn(List<String> values) {
            addCriterion("old_exam_time_section not in", values, "oldExamTimeSection");
            return (Criteria) this;
        }

        public Criteria andOldExamTimeSectionBetween(String value1, String value2) {
            addCriterion("old_exam_time_section between", value1, value2, "oldExamTimeSection");
            return (Criteria) this;
        }

        public Criteria andOldExamTimeSectionNotBetween(String value1, String value2) {
            addCriterion("old_exam_time_section not between", value1, value2, "oldExamTimeSection");
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

        public Criteria andStatusEqualTo(Integer value) {
            addCriterion("`status` =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Integer value) {
            addCriterion("`status` <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Integer value) {
            addCriterion("`status` >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("`status` >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Integer value) {
            addCriterion("`status` <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Integer value) {
            addCriterion("`status` <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Integer> values) {
            addCriterion("`status` in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<Integer> values) {
            addCriterion("`status` not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Integer value1, Integer value2) {
            addCriterion("`status` between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("`status` not between", value1, value2, "status");
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

        public Criteria andAuditUserIdIsNull() {
            addCriterion("audit_user_id is null");
            return (Criteria) this;
        }

        public Criteria andAuditUserIdIsNotNull() {
            addCriterion("audit_user_id is not null");
            return (Criteria) this;
        }

        public Criteria andAuditUserIdEqualTo(Integer value) {
            addCriterion("audit_user_id =", value, "auditUserId");
            return (Criteria) this;
        }

        public Criteria andAuditUserIdNotEqualTo(Integer value) {
            addCriterion("audit_user_id <>", value, "auditUserId");
            return (Criteria) this;
        }

        public Criteria andAuditUserIdGreaterThan(Integer value) {
            addCriterion("audit_user_id >", value, "auditUserId");
            return (Criteria) this;
        }

        public Criteria andAuditUserIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("audit_user_id >=", value, "auditUserId");
            return (Criteria) this;
        }

        public Criteria andAuditUserIdLessThan(Integer value) {
            addCriterion("audit_user_id <", value, "auditUserId");
            return (Criteria) this;
        }

        public Criteria andAuditUserIdLessThanOrEqualTo(Integer value) {
            addCriterion("audit_user_id <=", value, "auditUserId");
            return (Criteria) this;
        }

        public Criteria andAuditUserIdIn(List<Integer> values) {
            addCriterion("audit_user_id in", values, "auditUserId");
            return (Criteria) this;
        }

        public Criteria andAuditUserIdNotIn(List<Integer> values) {
            addCriterion("audit_user_id not in", values, "auditUserId");
            return (Criteria) this;
        }

        public Criteria andAuditUserIdBetween(Integer value1, Integer value2) {
            addCriterion("audit_user_id between", value1, value2, "auditUserId");
            return (Criteria) this;
        }

        public Criteria andAuditUserIdNotBetween(Integer value1, Integer value2) {
            addCriterion("audit_user_id not between", value1, value2, "auditUserId");
            return (Criteria) this;
        }

        public Criteria andAuditTimeIsNull() {
            addCriterion("audit_time is null");
            return (Criteria) this;
        }

        public Criteria andAuditTimeIsNotNull() {
            addCriterion("audit_time is not null");
            return (Criteria) this;
        }

        public Criteria andAuditTimeEqualTo(Date value) {
            addCriterion("audit_time =", value, "auditTime");
            return (Criteria) this;
        }

        public Criteria andAuditTimeNotEqualTo(Date value) {
            addCriterion("audit_time <>", value, "auditTime");
            return (Criteria) this;
        }

        public Criteria andAuditTimeGreaterThan(Date value) {
            addCriterion("audit_time >", value, "auditTime");
            return (Criteria) this;
        }

        public Criteria andAuditTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("audit_time >=", value, "auditTime");
            return (Criteria) this;
        }

        public Criteria andAuditTimeLessThan(Date value) {
            addCriterion("audit_time <", value, "auditTime");
            return (Criteria) this;
        }

        public Criteria andAuditTimeLessThanOrEqualTo(Date value) {
            addCriterion("audit_time <=", value, "auditTime");
            return (Criteria) this;
        }

        public Criteria andAuditTimeIn(List<Date> values) {
            addCriterion("audit_time in", values, "auditTime");
            return (Criteria) this;
        }

        public Criteria andAuditTimeNotIn(List<Date> values) {
            addCriterion("audit_time not in", values, "auditTime");
            return (Criteria) this;
        }

        public Criteria andAuditTimeBetween(Date value1, Date value2) {
            addCriterion("audit_time between", value1, value2, "auditTime");
            return (Criteria) this;
        }

        public Criteria andAuditTimeNotBetween(Date value1, Date value2) {
            addCriterion("audit_time not between", value1, value2, "auditTime");
            return (Criteria) this;
        }

        public Criteria andAuditRemarkIsNull() {
            addCriterion("audit_remark is null");
            return (Criteria) this;
        }

        public Criteria andAuditRemarkIsNotNull() {
            addCriterion("audit_remark is not null");
            return (Criteria) this;
        }

        public Criteria andAuditRemarkEqualTo(String value) {
            addCriterion("audit_remark =", value, "auditRemark");
            return (Criteria) this;
        }

        public Criteria andAuditRemarkNotEqualTo(String value) {
            addCriterion("audit_remark <>", value, "auditRemark");
            return (Criteria) this;
        }

        public Criteria andAuditRemarkGreaterThan(String value) {
            addCriterion("audit_remark >", value, "auditRemark");
            return (Criteria) this;
        }

        public Criteria andAuditRemarkGreaterThanOrEqualTo(String value) {
            addCriterion("audit_remark >=", value, "auditRemark");
            return (Criteria) this;
        }

        public Criteria andAuditRemarkLessThan(String value) {
            addCriterion("audit_remark <", value, "auditRemark");
            return (Criteria) this;
        }

        public Criteria andAuditRemarkLessThanOrEqualTo(String value) {
            addCriterion("audit_remark <=", value, "auditRemark");
            return (Criteria) this;
        }

        public Criteria andAuditRemarkLike(String value) {
            addCriterion("audit_remark like", value, "auditRemark");
            return (Criteria) this;
        }

        public Criteria andAuditRemarkNotLike(String value) {
            addCriterion("audit_remark not like", value, "auditRemark");
            return (Criteria) this;
        }

        public Criteria andAuditRemarkIn(List<String> values) {
            addCriterion("audit_remark in", values, "auditRemark");
            return (Criteria) this;
        }

        public Criteria andAuditRemarkNotIn(List<String> values) {
            addCriterion("audit_remark not in", values, "auditRemark");
            return (Criteria) this;
        }

        public Criteria andAuditRemarkBetween(String value1, String value2) {
            addCriterion("audit_remark between", value1, value2, "auditRemark");
            return (Criteria) this;
        }

        public Criteria andAuditRemarkNotBetween(String value1, String value2) {
            addCriterion("audit_remark not between", value1, value2, "auditRemark");
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