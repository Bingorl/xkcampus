package com.biu.wifi.campus.dao.model;

import java.util.ArrayList;
import java.util.List;

public class JwCjcxCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public JwCjcxCriteria() {
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

        public Criteria andSchoolIdIsNull() {
            addCriterion("school_id is null");
            return (Criteria) this;
        }

        public Criteria andSchoolIdIsNotNull() {
            addCriterion("school_id is not null");
            return (Criteria) this;
        }

        public Criteria andSchoolIdEqualTo(String value) {
            addCriterion("school_id =", value, "schoolId");
            return (Criteria) this;
        }

        public Criteria andSchoolIdNotEqualTo(String value) {
            addCriterion("school_id <>", value, "schoolId");
            return (Criteria) this;
        }

        public Criteria andSchoolIdGreaterThan(String value) {
            addCriterion("school_id >", value, "schoolId");
            return (Criteria) this;
        }

        public Criteria andSchoolIdGreaterThanOrEqualTo(String value) {
            addCriterion("school_id >=", value, "schoolId");
            return (Criteria) this;
        }

        public Criteria andSchoolIdLessThan(String value) {
            addCriterion("school_id <", value, "schoolId");
            return (Criteria) this;
        }

        public Criteria andSchoolIdLessThanOrEqualTo(String value) {
            addCriterion("school_id <=", value, "schoolId");
            return (Criteria) this;
        }

        public Criteria andSchoolIdLike(String value) {
            addCriterion("school_id like", value, "schoolId");
            return (Criteria) this;
        }

        public Criteria andSchoolIdNotLike(String value) {
            addCriterion("school_id not like", value, "schoolId");
            return (Criteria) this;
        }

        public Criteria andSchoolIdIn(List<String> values) {
            addCriterion("school_id in", values, "schoolId");
            return (Criteria) this;
        }

        public Criteria andSchoolIdNotIn(List<String> values) {
            addCriterion("school_id not in", values, "schoolId");
            return (Criteria) this;
        }

        public Criteria andSchoolIdBetween(String value1, String value2) {
            addCriterion("school_id between", value1, value2, "schoolId");
            return (Criteria) this;
        }

        public Criteria andSchoolIdNotBetween(String value1, String value2) {
            addCriterion("school_id not between", value1, value2, "schoolId");
            return (Criteria) this;
        }

        public Criteria andRealNameIsNull() {
            addCriterion("real_name is null");
            return (Criteria) this;
        }

        public Criteria andRealNameIsNotNull() {
            addCriterion("real_name is not null");
            return (Criteria) this;
        }

        public Criteria andRealNameEqualTo(String value) {
            addCriterion("real_name =", value, "realName");
            return (Criteria) this;
        }

        public Criteria andRealNameNotEqualTo(String value) {
            addCriterion("real_name <>", value, "realName");
            return (Criteria) this;
        }

        public Criteria andRealNameGreaterThan(String value) {
            addCriterion("real_name >", value, "realName");
            return (Criteria) this;
        }

        public Criteria andRealNameGreaterThanOrEqualTo(String value) {
            addCriterion("real_name >=", value, "realName");
            return (Criteria) this;
        }

        public Criteria andRealNameLessThan(String value) {
            addCriterion("real_name <", value, "realName");
            return (Criteria) this;
        }

        public Criteria andRealNameLessThanOrEqualTo(String value) {
            addCriterion("real_name <=", value, "realName");
            return (Criteria) this;
        }

        public Criteria andRealNameLike(String value) {
            addCriterion("real_name like", value, "realName");
            return (Criteria) this;
        }

        public Criteria andRealNameNotLike(String value) {
            addCriterion("real_name not like", value, "realName");
            return (Criteria) this;
        }

        public Criteria andRealNameIn(List<String> values) {
            addCriterion("real_name in", values, "realName");
            return (Criteria) this;
        }

        public Criteria andRealNameNotIn(List<String> values) {
            addCriterion("real_name not in", values, "realName");
            return (Criteria) this;
        }

        public Criteria andRealNameBetween(String value1, String value2) {
            addCriterion("real_name between", value1, value2, "realName");
            return (Criteria) this;
        }

        public Criteria andRealNameNotBetween(String value1, String value2) {
            addCriterion("real_name not between", value1, value2, "realName");
            return (Criteria) this;
        }

        public Criteria andSexIsNull() {
            addCriterion("sex is null");
            return (Criteria) this;
        }

        public Criteria andSexIsNotNull() {
            addCriterion("sex is not null");
            return (Criteria) this;
        }

        public Criteria andSexEqualTo(String value) {
            addCriterion("sex =", value, "sex");
            return (Criteria) this;
        }

        public Criteria andSexNotEqualTo(String value) {
            addCriterion("sex <>", value, "sex");
            return (Criteria) this;
        }

        public Criteria andSexGreaterThan(String value) {
            addCriterion("sex >", value, "sex");
            return (Criteria) this;
        }

        public Criteria andSexGreaterThanOrEqualTo(String value) {
            addCriterion("sex >=", value, "sex");
            return (Criteria) this;
        }

        public Criteria andSexLessThan(String value) {
            addCriterion("sex <", value, "sex");
            return (Criteria) this;
        }

        public Criteria andSexLessThanOrEqualTo(String value) {
            addCriterion("sex <=", value, "sex");
            return (Criteria) this;
        }

        public Criteria andSexLike(String value) {
            addCriterion("sex like", value, "sex");
            return (Criteria) this;
        }

        public Criteria andSexNotLike(String value) {
            addCriterion("sex not like", value, "sex");
            return (Criteria) this;
        }

        public Criteria andSexIn(List<String> values) {
            addCriterion("sex in", values, "sex");
            return (Criteria) this;
        }

        public Criteria andSexNotIn(List<String> values) {
            addCriterion("sex not in", values, "sex");
            return (Criteria) this;
        }

        public Criteria andSexBetween(String value1, String value2) {
            addCriterion("sex between", value1, value2, "sex");
            return (Criteria) this;
        }

        public Criteria andSexNotBetween(String value1, String value2) {
            addCriterion("sex not between", value1, value2, "sex");
            return (Criteria) this;
        }

        public Criteria andStuNoIsNull() {
            addCriterion("stu_no is null");
            return (Criteria) this;
        }

        public Criteria andStuNoIsNotNull() {
            addCriterion("stu_no is not null");
            return (Criteria) this;
        }

        public Criteria andStuNoEqualTo(String value) {
            addCriterion("stu_no =", value, "stuNo");
            return (Criteria) this;
        }

        public Criteria andStuNoNotEqualTo(String value) {
            addCriterion("stu_no <>", value, "stuNo");
            return (Criteria) this;
        }

        public Criteria andStuNoGreaterThan(String value) {
            addCriterion("stu_no >", value, "stuNo");
            return (Criteria) this;
        }

        public Criteria andStuNoGreaterThanOrEqualTo(String value) {
            addCriterion("stu_no >=", value, "stuNo");
            return (Criteria) this;
        }

        public Criteria andStuNoLessThan(String value) {
            addCriterion("stu_no <", value, "stuNo");
            return (Criteria) this;
        }

        public Criteria andStuNoLessThanOrEqualTo(String value) {
            addCriterion("stu_no <=", value, "stuNo");
            return (Criteria) this;
        }

        public Criteria andStuNoLike(String value) {
            addCriterion("stu_no like", value, "stuNo");
            return (Criteria) this;
        }

        public Criteria andStuNoNotLike(String value) {
            addCriterion("stu_no not like", value, "stuNo");
            return (Criteria) this;
        }

        public Criteria andStuNoIn(List<String> values) {
            addCriterion("stu_no in", values, "stuNo");
            return (Criteria) this;
        }

        public Criteria andStuNoNotIn(List<String> values) {
            addCriterion("stu_no not in", values, "stuNo");
            return (Criteria) this;
        }

        public Criteria andStuNoBetween(String value1, String value2) {
            addCriterion("stu_no between", value1, value2, "stuNo");
            return (Criteria) this;
        }

        public Criteria andStuNoNotBetween(String value1, String value2) {
            addCriterion("stu_no not between", value1, value2, "stuNo");
            return (Criteria) this;
        }

        public Criteria andIdTypeIsNull() {
            addCriterion("id_type is null");
            return (Criteria) this;
        }

        public Criteria andIdTypeIsNotNull() {
            addCriterion("id_type is not null");
            return (Criteria) this;
        }

        public Criteria andIdTypeEqualTo(String value) {
            addCriterion("id_type =", value, "idType");
            return (Criteria) this;
        }

        public Criteria andIdTypeNotEqualTo(String value) {
            addCriterion("id_type <>", value, "idType");
            return (Criteria) this;
        }

        public Criteria andIdTypeGreaterThan(String value) {
            addCriterion("id_type >", value, "idType");
            return (Criteria) this;
        }

        public Criteria andIdTypeGreaterThanOrEqualTo(String value) {
            addCriterion("id_type >=", value, "idType");
            return (Criteria) this;
        }

        public Criteria andIdTypeLessThan(String value) {
            addCriterion("id_type <", value, "idType");
            return (Criteria) this;
        }

        public Criteria andIdTypeLessThanOrEqualTo(String value) {
            addCriterion("id_type <=", value, "idType");
            return (Criteria) this;
        }

        public Criteria andIdTypeLike(String value) {
            addCriterion("id_type like", value, "idType");
            return (Criteria) this;
        }

        public Criteria andIdTypeNotLike(String value) {
            addCriterion("id_type not like", value, "idType");
            return (Criteria) this;
        }

        public Criteria andIdTypeIn(List<String> values) {
            addCriterion("id_type in", values, "idType");
            return (Criteria) this;
        }

        public Criteria andIdTypeNotIn(List<String> values) {
            addCriterion("id_type not in", values, "idType");
            return (Criteria) this;
        }

        public Criteria andIdTypeBetween(String value1, String value2) {
            addCriterion("id_type between", value1, value2, "idType");
            return (Criteria) this;
        }

        public Criteria andIdTypeNotBetween(String value1, String value2) {
            addCriterion("id_type not between", value1, value2, "idType");
            return (Criteria) this;
        }

        public Criteria andIdNoIsNull() {
            addCriterion("id_no is null");
            return (Criteria) this;
        }

        public Criteria andIdNoIsNotNull() {
            addCriterion("id_no is not null");
            return (Criteria) this;
        }

        public Criteria andIdNoEqualTo(String value) {
            addCriterion("id_no =", value, "idNo");
            return (Criteria) this;
        }

        public Criteria andIdNoNotEqualTo(String value) {
            addCriterion("id_no <>", value, "idNo");
            return (Criteria) this;
        }

        public Criteria andIdNoGreaterThan(String value) {
            addCriterion("id_no >", value, "idNo");
            return (Criteria) this;
        }

        public Criteria andIdNoGreaterThanOrEqualTo(String value) {
            addCriterion("id_no >=", value, "idNo");
            return (Criteria) this;
        }

        public Criteria andIdNoLessThan(String value) {
            addCriterion("id_no <", value, "idNo");
            return (Criteria) this;
        }

        public Criteria andIdNoLessThanOrEqualTo(String value) {
            addCriterion("id_no <=", value, "idNo");
            return (Criteria) this;
        }

        public Criteria andIdNoLike(String value) {
            addCriterion("id_no like", value, "idNo");
            return (Criteria) this;
        }

        public Criteria andIdNoNotLike(String value) {
            addCriterion("id_no not like", value, "idNo");
            return (Criteria) this;
        }

        public Criteria andIdNoIn(List<String> values) {
            addCriterion("id_no in", values, "idNo");
            return (Criteria) this;
        }

        public Criteria andIdNoNotIn(List<String> values) {
            addCriterion("id_no not in", values, "idNo");
            return (Criteria) this;
        }

        public Criteria andIdNoBetween(String value1, String value2) {
            addCriterion("id_no between", value1, value2, "idNo");
            return (Criteria) this;
        }

        public Criteria andIdNoNotBetween(String value1, String value2) {
            addCriterion("id_no not between", value1, value2, "idNo");
            return (Criteria) this;
        }

        public Criteria andEduBgIsNull() {
            addCriterion("edu_bg is null");
            return (Criteria) this;
        }

        public Criteria andEduBgIsNotNull() {
            addCriterion("edu_bg is not null");
            return (Criteria) this;
        }

        public Criteria andEduBgEqualTo(String value) {
            addCriterion("edu_bg =", value, "eduBg");
            return (Criteria) this;
        }

        public Criteria andEduBgNotEqualTo(String value) {
            addCriterion("edu_bg <>", value, "eduBg");
            return (Criteria) this;
        }

        public Criteria andEduBgGreaterThan(String value) {
            addCriterion("edu_bg >", value, "eduBg");
            return (Criteria) this;
        }

        public Criteria andEduBgGreaterThanOrEqualTo(String value) {
            addCriterion("edu_bg >=", value, "eduBg");
            return (Criteria) this;
        }

        public Criteria andEduBgLessThan(String value) {
            addCriterion("edu_bg <", value, "eduBg");
            return (Criteria) this;
        }

        public Criteria andEduBgLessThanOrEqualTo(String value) {
            addCriterion("edu_bg <=", value, "eduBg");
            return (Criteria) this;
        }

        public Criteria andEduBgLike(String value) {
            addCriterion("edu_bg like", value, "eduBg");
            return (Criteria) this;
        }

        public Criteria andEduBgNotLike(String value) {
            addCriterion("edu_bg not like", value, "eduBg");
            return (Criteria) this;
        }

        public Criteria andEduBgIn(List<String> values) {
            addCriterion("edu_bg in", values, "eduBg");
            return (Criteria) this;
        }

        public Criteria andEduBgNotIn(List<String> values) {
            addCriterion("edu_bg not in", values, "eduBg");
            return (Criteria) this;
        }

        public Criteria andEduBgBetween(String value1, String value2) {
            addCriterion("edu_bg between", value1, value2, "eduBg");
            return (Criteria) this;
        }

        public Criteria andEduBgNotBetween(String value1, String value2) {
            addCriterion("edu_bg not between", value1, value2, "eduBg");
            return (Criteria) this;
        }

        public Criteria andEduSysIsNull() {
            addCriterion("edu_sys is null");
            return (Criteria) this;
        }

        public Criteria andEduSysIsNotNull() {
            addCriterion("edu_sys is not null");
            return (Criteria) this;
        }

        public Criteria andEduSysEqualTo(String value) {
            addCriterion("edu_sys =", value, "eduSys");
            return (Criteria) this;
        }

        public Criteria andEduSysNotEqualTo(String value) {
            addCriterion("edu_sys <>", value, "eduSys");
            return (Criteria) this;
        }

        public Criteria andEduSysGreaterThan(String value) {
            addCriterion("edu_sys >", value, "eduSys");
            return (Criteria) this;
        }

        public Criteria andEduSysGreaterThanOrEqualTo(String value) {
            addCriterion("edu_sys >=", value, "eduSys");
            return (Criteria) this;
        }

        public Criteria andEduSysLessThan(String value) {
            addCriterion("edu_sys <", value, "eduSys");
            return (Criteria) this;
        }

        public Criteria andEduSysLessThanOrEqualTo(String value) {
            addCriterion("edu_sys <=", value, "eduSys");
            return (Criteria) this;
        }

        public Criteria andEduSysLike(String value) {
            addCriterion("edu_sys like", value, "eduSys");
            return (Criteria) this;
        }

        public Criteria andEduSysNotLike(String value) {
            addCriterion("edu_sys not like", value, "eduSys");
            return (Criteria) this;
        }

        public Criteria andEduSysIn(List<String> values) {
            addCriterion("edu_sys in", values, "eduSys");
            return (Criteria) this;
        }

        public Criteria andEduSysNotIn(List<String> values) {
            addCriterion("edu_sys not in", values, "eduSys");
            return (Criteria) this;
        }

        public Criteria andEduSysBetween(String value1, String value2) {
            addCriterion("edu_sys between", value1, value2, "eduSys");
            return (Criteria) this;
        }

        public Criteria andEduSysNotBetween(String value1, String value2) {
            addCriterion("edu_sys not between", value1, value2, "eduSys");
            return (Criteria) this;
        }

        public Criteria andEnterYearIsNull() {
            addCriterion("enter_year is null");
            return (Criteria) this;
        }

        public Criteria andEnterYearIsNotNull() {
            addCriterion("enter_year is not null");
            return (Criteria) this;
        }

        public Criteria andEnterYearEqualTo(String value) {
            addCriterion("enter_year =", value, "enterYear");
            return (Criteria) this;
        }

        public Criteria andEnterYearNotEqualTo(String value) {
            addCriterion("enter_year <>", value, "enterYear");
            return (Criteria) this;
        }

        public Criteria andEnterYearGreaterThan(String value) {
            addCriterion("enter_year >", value, "enterYear");
            return (Criteria) this;
        }

        public Criteria andEnterYearGreaterThanOrEqualTo(String value) {
            addCriterion("enter_year >=", value, "enterYear");
            return (Criteria) this;
        }

        public Criteria andEnterYearLessThan(String value) {
            addCriterion("enter_year <", value, "enterYear");
            return (Criteria) this;
        }

        public Criteria andEnterYearLessThanOrEqualTo(String value) {
            addCriterion("enter_year <=", value, "enterYear");
            return (Criteria) this;
        }

        public Criteria andEnterYearLike(String value) {
            addCriterion("enter_year like", value, "enterYear");
            return (Criteria) this;
        }

        public Criteria andEnterYearNotLike(String value) {
            addCriterion("enter_year not like", value, "enterYear");
            return (Criteria) this;
        }

        public Criteria andEnterYearIn(List<String> values) {
            addCriterion("enter_year in", values, "enterYear");
            return (Criteria) this;
        }

        public Criteria andEnterYearNotIn(List<String> values) {
            addCriterion("enter_year not in", values, "enterYear");
            return (Criteria) this;
        }

        public Criteria andEnterYearBetween(String value1, String value2) {
            addCriterion("enter_year between", value1, value2, "enterYear");
            return (Criteria) this;
        }

        public Criteria andEnterYearNotBetween(String value1, String value2) {
            addCriterion("enter_year not between", value1, value2, "enterYear");
            return (Criteria) this;
        }

        public Criteria andGradeIsNull() {
            addCriterion("grade is null");
            return (Criteria) this;
        }

        public Criteria andGradeIsNotNull() {
            addCriterion("grade is not null");
            return (Criteria) this;
        }

        public Criteria andGradeEqualTo(String value) {
            addCriterion("grade =", value, "grade");
            return (Criteria) this;
        }

        public Criteria andGradeNotEqualTo(String value) {
            addCriterion("grade <>", value, "grade");
            return (Criteria) this;
        }

        public Criteria andGradeGreaterThan(String value) {
            addCriterion("grade >", value, "grade");
            return (Criteria) this;
        }

        public Criteria andGradeGreaterThanOrEqualTo(String value) {
            addCriterion("grade >=", value, "grade");
            return (Criteria) this;
        }

        public Criteria andGradeLessThan(String value) {
            addCriterion("grade <", value, "grade");
            return (Criteria) this;
        }

        public Criteria andGradeLessThanOrEqualTo(String value) {
            addCriterion("grade <=", value, "grade");
            return (Criteria) this;
        }

        public Criteria andGradeLike(String value) {
            addCriterion("grade like", value, "grade");
            return (Criteria) this;
        }

        public Criteria andGradeNotLike(String value) {
            addCriterion("grade not like", value, "grade");
            return (Criteria) this;
        }

        public Criteria andGradeIn(List<String> values) {
            addCriterion("grade in", values, "grade");
            return (Criteria) this;
        }

        public Criteria andGradeNotIn(List<String> values) {
            addCriterion("grade not in", values, "grade");
            return (Criteria) this;
        }

        public Criteria andGradeBetween(String value1, String value2) {
            addCriterion("grade between", value1, value2, "grade");
            return (Criteria) this;
        }

        public Criteria andGradeNotBetween(String value1, String value2) {
            addCriterion("grade not between", value1, value2, "grade");
            return (Criteria) this;
        }

        public Criteria andInstituteNameIsNull() {
            addCriterion("institute_name is null");
            return (Criteria) this;
        }

        public Criteria andInstituteNameIsNotNull() {
            addCriterion("institute_name is not null");
            return (Criteria) this;
        }

        public Criteria andInstituteNameEqualTo(String value) {
            addCriterion("institute_name =", value, "instituteName");
            return (Criteria) this;
        }

        public Criteria andInstituteNameNotEqualTo(String value) {
            addCriterion("institute_name <>", value, "instituteName");
            return (Criteria) this;
        }

        public Criteria andInstituteNameGreaterThan(String value) {
            addCriterion("institute_name >", value, "instituteName");
            return (Criteria) this;
        }

        public Criteria andInstituteNameGreaterThanOrEqualTo(String value) {
            addCriterion("institute_name >=", value, "instituteName");
            return (Criteria) this;
        }

        public Criteria andInstituteNameLessThan(String value) {
            addCriterion("institute_name <", value, "instituteName");
            return (Criteria) this;
        }

        public Criteria andInstituteNameLessThanOrEqualTo(String value) {
            addCriterion("institute_name <=", value, "instituteName");
            return (Criteria) this;
        }

        public Criteria andInstituteNameLike(String value) {
            addCriterion("institute_name like", value, "instituteName");
            return (Criteria) this;
        }

        public Criteria andInstituteNameNotLike(String value) {
            addCriterion("institute_name not like", value, "instituteName");
            return (Criteria) this;
        }

        public Criteria andInstituteNameIn(List<String> values) {
            addCriterion("institute_name in", values, "instituteName");
            return (Criteria) this;
        }

        public Criteria andInstituteNameNotIn(List<String> values) {
            addCriterion("institute_name not in", values, "instituteName");
            return (Criteria) this;
        }

        public Criteria andInstituteNameBetween(String value1, String value2) {
            addCriterion("institute_name between", value1, value2, "instituteName");
            return (Criteria) this;
        }

        public Criteria andInstituteNameNotBetween(String value1, String value2) {
            addCriterion("institute_name not between", value1, value2, "instituteName");
            return (Criteria) this;
        }

        public Criteria andMajorNameIsNull() {
            addCriterion("major_name is null");
            return (Criteria) this;
        }

        public Criteria andMajorNameIsNotNull() {
            addCriterion("major_name is not null");
            return (Criteria) this;
        }

        public Criteria andMajorNameEqualTo(String value) {
            addCriterion("major_name =", value, "majorName");
            return (Criteria) this;
        }

        public Criteria andMajorNameNotEqualTo(String value) {
            addCriterion("major_name <>", value, "majorName");
            return (Criteria) this;
        }

        public Criteria andMajorNameGreaterThan(String value) {
            addCriterion("major_name >", value, "majorName");
            return (Criteria) this;
        }

        public Criteria andMajorNameGreaterThanOrEqualTo(String value) {
            addCriterion("major_name >=", value, "majorName");
            return (Criteria) this;
        }

        public Criteria andMajorNameLessThan(String value) {
            addCriterion("major_name <", value, "majorName");
            return (Criteria) this;
        }

        public Criteria andMajorNameLessThanOrEqualTo(String value) {
            addCriterion("major_name <=", value, "majorName");
            return (Criteria) this;
        }

        public Criteria andMajorNameLike(String value) {
            addCriterion("major_name like", value, "majorName");
            return (Criteria) this;
        }

        public Criteria andMajorNameNotLike(String value) {
            addCriterion("major_name not like", value, "majorName");
            return (Criteria) this;
        }

        public Criteria andMajorNameIn(List<String> values) {
            addCriterion("major_name in", values, "majorName");
            return (Criteria) this;
        }

        public Criteria andMajorNameNotIn(List<String> values) {
            addCriterion("major_name not in", values, "majorName");
            return (Criteria) this;
        }

        public Criteria andMajorNameBetween(String value1, String value2) {
            addCriterion("major_name between", value1, value2, "majorName");
            return (Criteria) this;
        }

        public Criteria andMajorNameNotBetween(String value1, String value2) {
            addCriterion("major_name not between", value1, value2, "majorName");
            return (Criteria) this;
        }

        public Criteria andClassNameIsNull() {
            addCriterion("class_name is null");
            return (Criteria) this;
        }

        public Criteria andClassNameIsNotNull() {
            addCriterion("class_name is not null");
            return (Criteria) this;
        }

        public Criteria andClassNameEqualTo(String value) {
            addCriterion("class_name =", value, "className");
            return (Criteria) this;
        }

        public Criteria andClassNameNotEqualTo(String value) {
            addCriterion("class_name <>", value, "className");
            return (Criteria) this;
        }

        public Criteria andClassNameGreaterThan(String value) {
            addCriterion("class_name >", value, "className");
            return (Criteria) this;
        }

        public Criteria andClassNameGreaterThanOrEqualTo(String value) {
            addCriterion("class_name >=", value, "className");
            return (Criteria) this;
        }

        public Criteria andClassNameLessThan(String value) {
            addCriterion("class_name <", value, "className");
            return (Criteria) this;
        }

        public Criteria andClassNameLessThanOrEqualTo(String value) {
            addCriterion("class_name <=", value, "className");
            return (Criteria) this;
        }

        public Criteria andClassNameLike(String value) {
            addCriterion("class_name like", value, "className");
            return (Criteria) this;
        }

        public Criteria andClassNameNotLike(String value) {
            addCriterion("class_name not like", value, "className");
            return (Criteria) this;
        }

        public Criteria andClassNameIn(List<String> values) {
            addCriterion("class_name in", values, "className");
            return (Criteria) this;
        }

        public Criteria andClassNameNotIn(List<String> values) {
            addCriterion("class_name not in", values, "className");
            return (Criteria) this;
        }

        public Criteria andClassNameBetween(String value1, String value2) {
            addCriterion("class_name between", value1, value2, "className");
            return (Criteria) this;
        }

        public Criteria andClassNameNotBetween(String value1, String value2) {
            addCriterion("class_name not between", value1, value2, "className");
            return (Criteria) this;
        }

        public Criteria andLanTypeIsNull() {
            addCriterion("lan_type is null");
            return (Criteria) this;
        }

        public Criteria andLanTypeIsNotNull() {
            addCriterion("lan_type is not null");
            return (Criteria) this;
        }

        public Criteria andLanTypeEqualTo(String value) {
            addCriterion("lan_type =", value, "lanType");
            return (Criteria) this;
        }

        public Criteria andLanTypeNotEqualTo(String value) {
            addCriterion("lan_type <>", value, "lanType");
            return (Criteria) this;
        }

        public Criteria andLanTypeGreaterThan(String value) {
            addCriterion("lan_type >", value, "lanType");
            return (Criteria) this;
        }

        public Criteria andLanTypeGreaterThanOrEqualTo(String value) {
            addCriterion("lan_type >=", value, "lanType");
            return (Criteria) this;
        }

        public Criteria andLanTypeLessThan(String value) {
            addCriterion("lan_type <", value, "lanType");
            return (Criteria) this;
        }

        public Criteria andLanTypeLessThanOrEqualTo(String value) {
            addCriterion("lan_type <=", value, "lanType");
            return (Criteria) this;
        }

        public Criteria andLanTypeLike(String value) {
            addCriterion("lan_type like", value, "lanType");
            return (Criteria) this;
        }

        public Criteria andLanTypeNotLike(String value) {
            addCriterion("lan_type not like", value, "lanType");
            return (Criteria) this;
        }

        public Criteria andLanTypeIn(List<String> values) {
            addCriterion("lan_type in", values, "lanType");
            return (Criteria) this;
        }

        public Criteria andLanTypeNotIn(List<String> values) {
            addCriterion("lan_type not in", values, "lanType");
            return (Criteria) this;
        }

        public Criteria andLanTypeBetween(String value1, String value2) {
            addCriterion("lan_type between", value1, value2, "lanType");
            return (Criteria) this;
        }

        public Criteria andLanTypeNotBetween(String value1, String value2) {
            addCriterion("lan_type not between", value1, value2, "lanType");
            return (Criteria) this;
        }

        public Criteria andIsApplyIsNull() {
            addCriterion("is_apply is null");
            return (Criteria) this;
        }

        public Criteria andIsApplyIsNotNull() {
            addCriterion("is_apply is not null");
            return (Criteria) this;
        }

        public Criteria andIsApplyEqualTo(String value) {
            addCriterion("is_apply =", value, "isApply");
            return (Criteria) this;
        }

        public Criteria andIsApplyNotEqualTo(String value) {
            addCriterion("is_apply <>", value, "isApply");
            return (Criteria) this;
        }

        public Criteria andIsApplyGreaterThan(String value) {
            addCriterion("is_apply >", value, "isApply");
            return (Criteria) this;
        }

        public Criteria andIsApplyGreaterThanOrEqualTo(String value) {
            addCriterion("is_apply >=", value, "isApply");
            return (Criteria) this;
        }

        public Criteria andIsApplyLessThan(String value) {
            addCriterion("is_apply <", value, "isApply");
            return (Criteria) this;
        }

        public Criteria andIsApplyLessThanOrEqualTo(String value) {
            addCriterion("is_apply <=", value, "isApply");
            return (Criteria) this;
        }

        public Criteria andIsApplyLike(String value) {
            addCriterion("is_apply like", value, "isApply");
            return (Criteria) this;
        }

        public Criteria andIsApplyNotLike(String value) {
            addCriterion("is_apply not like", value, "isApply");
            return (Criteria) this;
        }

        public Criteria andIsApplyIn(List<String> values) {
            addCriterion("is_apply in", values, "isApply");
            return (Criteria) this;
        }

        public Criteria andIsApplyNotIn(List<String> values) {
            addCriterion("is_apply not in", values, "isApply");
            return (Criteria) this;
        }

        public Criteria andIsApplyBetween(String value1, String value2) {
            addCriterion("is_apply between", value1, value2, "isApply");
            return (Criteria) this;
        }

        public Criteria andIsApplyNotBetween(String value1, String value2) {
            addCriterion("is_apply not between", value1, value2, "isApply");
            return (Criteria) this;
        }

        public Criteria andClassCodeIsNull() {
            addCriterion("class_code is null");
            return (Criteria) this;
        }

        public Criteria andClassCodeIsNotNull() {
            addCriterion("class_code is not null");
            return (Criteria) this;
        }

        public Criteria andClassCodeEqualTo(String value) {
            addCriterion("class_code =", value, "classCode");
            return (Criteria) this;
        }

        public Criteria andClassCodeNotEqualTo(String value) {
            addCriterion("class_code <>", value, "classCode");
            return (Criteria) this;
        }

        public Criteria andClassCodeGreaterThan(String value) {
            addCriterion("class_code >", value, "classCode");
            return (Criteria) this;
        }

        public Criteria andClassCodeGreaterThanOrEqualTo(String value) {
            addCriterion("class_code >=", value, "classCode");
            return (Criteria) this;
        }

        public Criteria andClassCodeLessThan(String value) {
            addCriterion("class_code <", value, "classCode");
            return (Criteria) this;
        }

        public Criteria andClassCodeLessThanOrEqualTo(String value) {
            addCriterion("class_code <=", value, "classCode");
            return (Criteria) this;
        }

        public Criteria andClassCodeLike(String value) {
            addCriterion("class_code like", value, "classCode");
            return (Criteria) this;
        }

        public Criteria andClassCodeNotLike(String value) {
            addCriterion("class_code not like", value, "classCode");
            return (Criteria) this;
        }

        public Criteria andClassCodeIn(List<String> values) {
            addCriterion("class_code in", values, "classCode");
            return (Criteria) this;
        }

        public Criteria andClassCodeNotIn(List<String> values) {
            addCriterion("class_code not in", values, "classCode");
            return (Criteria) this;
        }

        public Criteria andClassCodeBetween(String value1, String value2) {
            addCriterion("class_code between", value1, value2, "classCode");
            return (Criteria) this;
        }

        public Criteria andClassCodeNotBetween(String value1, String value2) {
            addCriterion("class_code not between", value1, value2, "classCode");
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

        public Criteria andMajorNoIsNull() {
            addCriterion("major_no is null");
            return (Criteria) this;
        }

        public Criteria andMajorNoIsNotNull() {
            addCriterion("major_no is not null");
            return (Criteria) this;
        }

        public Criteria andMajorNoEqualTo(String value) {
            addCriterion("major_no =", value, "majorNo");
            return (Criteria) this;
        }

        public Criteria andMajorNoNotEqualTo(String value) {
            addCriterion("major_no <>", value, "majorNo");
            return (Criteria) this;
        }

        public Criteria andMajorNoGreaterThan(String value) {
            addCriterion("major_no >", value, "majorNo");
            return (Criteria) this;
        }

        public Criteria andMajorNoGreaterThanOrEqualTo(String value) {
            addCriterion("major_no >=", value, "majorNo");
            return (Criteria) this;
        }

        public Criteria andMajorNoLessThan(String value) {
            addCriterion("major_no <", value, "majorNo");
            return (Criteria) this;
        }

        public Criteria andMajorNoLessThanOrEqualTo(String value) {
            addCriterion("major_no <=", value, "majorNo");
            return (Criteria) this;
        }

        public Criteria andMajorNoLike(String value) {
            addCriterion("major_no like", value, "majorNo");
            return (Criteria) this;
        }

        public Criteria andMajorNoNotLike(String value) {
            addCriterion("major_no not like", value, "majorNo");
            return (Criteria) this;
        }

        public Criteria andMajorNoIn(List<String> values) {
            addCriterion("major_no in", values, "majorNo");
            return (Criteria) this;
        }

        public Criteria andMajorNoNotIn(List<String> values) {
            addCriterion("major_no not in", values, "majorNo");
            return (Criteria) this;
        }

        public Criteria andMajorNoBetween(String value1, String value2) {
            addCriterion("major_no between", value1, value2, "majorNo");
            return (Criteria) this;
        }

        public Criteria andMajorNoNotBetween(String value1, String value2) {
            addCriterion("major_no not between", value1, value2, "majorNo");
            return (Criteria) this;
        }

        public Criteria andInstituteNoIsNull() {
            addCriterion("institute_no is null");
            return (Criteria) this;
        }

        public Criteria andInstituteNoIsNotNull() {
            addCriterion("institute_no is not null");
            return (Criteria) this;
        }

        public Criteria andInstituteNoEqualTo(String value) {
            addCriterion("institute_no =", value, "instituteNo");
            return (Criteria) this;
        }

        public Criteria andInstituteNoNotEqualTo(String value) {
            addCriterion("institute_no <>", value, "instituteNo");
            return (Criteria) this;
        }

        public Criteria andInstituteNoGreaterThan(String value) {
            addCriterion("institute_no >", value, "instituteNo");
            return (Criteria) this;
        }

        public Criteria andInstituteNoGreaterThanOrEqualTo(String value) {
            addCriterion("institute_no >=", value, "instituteNo");
            return (Criteria) this;
        }

        public Criteria andInstituteNoLessThan(String value) {
            addCriterion("institute_no <", value, "instituteNo");
            return (Criteria) this;
        }

        public Criteria andInstituteNoLessThanOrEqualTo(String value) {
            addCriterion("institute_no <=", value, "instituteNo");
            return (Criteria) this;
        }

        public Criteria andInstituteNoLike(String value) {
            addCriterion("institute_no like", value, "instituteNo");
            return (Criteria) this;
        }

        public Criteria andInstituteNoNotLike(String value) {
            addCriterion("institute_no not like", value, "instituteNo");
            return (Criteria) this;
        }

        public Criteria andInstituteNoIn(List<String> values) {
            addCriterion("institute_no in", values, "instituteNo");
            return (Criteria) this;
        }

        public Criteria andInstituteNoNotIn(List<String> values) {
            addCriterion("institute_no not in", values, "instituteNo");
            return (Criteria) this;
        }

        public Criteria andInstituteNoBetween(String value1, String value2) {
            addCriterion("institute_no between", value1, value2, "instituteNo");
            return (Criteria) this;
        }

        public Criteria andInstituteNoNotBetween(String value1, String value2) {
            addCriterion("institute_no not between", value1, value2, "instituteNo");
            return (Criteria) this;
        }

        public Criteria andExamDateIsNull() {
            addCriterion("exam_date is null");
            return (Criteria) this;
        }

        public Criteria andExamDateIsNotNull() {
            addCriterion("exam_date is not null");
            return (Criteria) this;
        }

        public Criteria andExamDateEqualTo(String value) {
            addCriterion("exam_date =", value, "examDate");
            return (Criteria) this;
        }

        public Criteria andExamDateNotEqualTo(String value) {
            addCriterion("exam_date <>", value, "examDate");
            return (Criteria) this;
        }

        public Criteria andExamDateGreaterThan(String value) {
            addCriterion("exam_date >", value, "examDate");
            return (Criteria) this;
        }

        public Criteria andExamDateGreaterThanOrEqualTo(String value) {
            addCriterion("exam_date >=", value, "examDate");
            return (Criteria) this;
        }

        public Criteria andExamDateLessThan(String value) {
            addCriterion("exam_date <", value, "examDate");
            return (Criteria) this;
        }

        public Criteria andExamDateLessThanOrEqualTo(String value) {
            addCriterion("exam_date <=", value, "examDate");
            return (Criteria) this;
        }

        public Criteria andExamDateLike(String value) {
            addCriterion("exam_date like", value, "examDate");
            return (Criteria) this;
        }

        public Criteria andExamDateNotLike(String value) {
            addCriterion("exam_date not like", value, "examDate");
            return (Criteria) this;
        }

        public Criteria andExamDateIn(List<String> values) {
            addCriterion("exam_date in", values, "examDate");
            return (Criteria) this;
        }

        public Criteria andExamDateNotIn(List<String> values) {
            addCriterion("exam_date not in", values, "examDate");
            return (Criteria) this;
        }

        public Criteria andExamDateBetween(String value1, String value2) {
            addCriterion("exam_date between", value1, value2, "examDate");
            return (Criteria) this;
        }

        public Criteria andExamDateNotBetween(String value1, String value2) {
            addCriterion("exam_date not between", value1, value2, "examDate");
            return (Criteria) this;
        }

        public Criteria andExamScoreIsNull() {
            addCriterion("exam_score is null");
            return (Criteria) this;
        }

        public Criteria andExamScoreIsNotNull() {
            addCriterion("exam_score is not null");
            return (Criteria) this;
        }

        public Criteria andExamScoreEqualTo(String value) {
            addCriterion("exam_score =", value, "examScore");
            return (Criteria) this;
        }

        public Criteria andExamScoreNotEqualTo(String value) {
            addCriterion("exam_score <>", value, "examScore");
            return (Criteria) this;
        }

        public Criteria andExamScoreGreaterThan(String value) {
            addCriterion("exam_score >", value, "examScore");
            return (Criteria) this;
        }

        public Criteria andExamScoreGreaterThanOrEqualTo(String value) {
            addCriterion("exam_score >=", value, "examScore");
            return (Criteria) this;
        }

        public Criteria andExamScoreLessThan(String value) {
            addCriterion("exam_score <", value, "examScore");
            return (Criteria) this;
        }

        public Criteria andExamScoreLessThanOrEqualTo(String value) {
            addCriterion("exam_score <=", value, "examScore");
            return (Criteria) this;
        }

        public Criteria andExamScoreLike(String value) {
            addCriterion("exam_score like", value, "examScore");
            return (Criteria) this;
        }

        public Criteria andExamScoreNotLike(String value) {
            addCriterion("exam_score not like", value, "examScore");
            return (Criteria) this;
        }

        public Criteria andExamScoreIn(List<String> values) {
            addCriterion("exam_score in", values, "examScore");
            return (Criteria) this;
        }

        public Criteria andExamScoreNotIn(List<String> values) {
            addCriterion("exam_score not in", values, "examScore");
            return (Criteria) this;
        }

        public Criteria andExamScoreBetween(String value1, String value2) {
            addCriterion("exam_score between", value1, value2, "examScore");
            return (Criteria) this;
        }

        public Criteria andExamScoreNotBetween(String value1, String value2) {
            addCriterion("exam_score not between", value1, value2, "examScore");
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