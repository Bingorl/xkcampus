package com.biu.wifi.campus.dao.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PostCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public PostCriteria() {
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

        public Criteria andTitleIsNull() {
            addCriterion("title is null");
            return (Criteria) this;
        }

        public Criteria andTitleIsNotNull() {
            addCriterion("title is not null");
            return (Criteria) this;
        }

        public Criteria andTitleEqualTo(String value) {
            addCriterion("title =", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotEqualTo(String value) {
            addCriterion("title <>", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleGreaterThan(String value) {
            addCriterion("title >", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleGreaterThanOrEqualTo(String value) {
            addCriterion("title >=", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleLessThan(String value) {
            addCriterion("title <", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleLessThanOrEqualTo(String value) {
            addCriterion("title <=", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleLike(String value) {
            addCriterion("title like", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotLike(String value) {
            addCriterion("title not like", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleIn(List<String> values) {
            addCriterion("title in", values, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotIn(List<String> values) {
            addCriterion("title not in", values, "title");
            return (Criteria) this;
        }

        public Criteria andTitleBetween(String value1, String value2) {
            addCriterion("title between", value1, value2, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotBetween(String value1, String value2) {
            addCriterion("title not between", value1, value2, "title");
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

        public Criteria andPostTypeIsNull() {
            addCriterion("post_type is null");
            return (Criteria) this;
        }

        public Criteria andPostTypeIsNotNull() {
            addCriterion("post_type is not null");
            return (Criteria) this;
        }

        public Criteria andPostTypeEqualTo(Short value) {
            addCriterion("post_type =", value, "postType");
            return (Criteria) this;
        }

        public Criteria andPostTypeNotEqualTo(Short value) {
            addCriterion("post_type <>", value, "postType");
            return (Criteria) this;
        }

        public Criteria andPostTypeGreaterThan(Short value) {
            addCriterion("post_type >", value, "postType");
            return (Criteria) this;
        }

        public Criteria andPostTypeGreaterThanOrEqualTo(Short value) {
            addCriterion("post_type >=", value, "postType");
            return (Criteria) this;
        }

        public Criteria andPostTypeLessThan(Short value) {
            addCriterion("post_type <", value, "postType");
            return (Criteria) this;
        }

        public Criteria andPostTypeLessThanOrEqualTo(Short value) {
            addCriterion("post_type <=", value, "postType");
            return (Criteria) this;
        }

        public Criteria andPostTypeIn(List<Short> values) {
            addCriterion("post_type in", values, "postType");
            return (Criteria) this;
        }

        public Criteria andPostTypeNotIn(List<Short> values) {
            addCriterion("post_type not in", values, "postType");
            return (Criteria) this;
        }

        public Criteria andPostTypeBetween(Short value1, Short value2) {
            addCriterion("post_type between", value1, value2, "postType");
            return (Criteria) this;
        }

        public Criteria andPostTypeNotBetween(Short value1, Short value2) {
            addCriterion("post_type not between", value1, value2, "postType");
            return (Criteria) this;
        }

        public Criteria andIsSelectIsNull() {
            addCriterion("is_select is null");
            return (Criteria) this;
        }

        public Criteria andIsSelectIsNotNull() {
            addCriterion("is_select is not null");
            return (Criteria) this;
        }

        public Criteria andIsSelectEqualTo(Short value) {
            addCriterion("is_select =", value, "isSelect");
            return (Criteria) this;
        }

        public Criteria andIsSelectNotEqualTo(Short value) {
            addCriterion("is_select <>", value, "isSelect");
            return (Criteria) this;
        }

        public Criteria andIsSelectGreaterThan(Short value) {
            addCriterion("is_select >", value, "isSelect");
            return (Criteria) this;
        }

        public Criteria andIsSelectGreaterThanOrEqualTo(Short value) {
            addCriterion("is_select >=", value, "isSelect");
            return (Criteria) this;
        }

        public Criteria andIsSelectLessThan(Short value) {
            addCriterion("is_select <", value, "isSelect");
            return (Criteria) this;
        }

        public Criteria andIsSelectLessThanOrEqualTo(Short value) {
            addCriterion("is_select <=", value, "isSelect");
            return (Criteria) this;
        }

        public Criteria andIsSelectIn(List<Short> values) {
            addCriterion("is_select in", values, "isSelect");
            return (Criteria) this;
        }

        public Criteria andIsSelectNotIn(List<Short> values) {
            addCriterion("is_select not in", values, "isSelect");
            return (Criteria) this;
        }

        public Criteria andIsSelectBetween(Short value1, Short value2) {
            addCriterion("is_select between", value1, value2, "isSelect");
            return (Criteria) this;
        }

        public Criteria andIsSelectNotBetween(Short value1, Short value2) {
            addCriterion("is_select not between", value1, value2, "isSelect");
            return (Criteria) this;
        }

        public Criteria andWeightIsNull() {
            addCriterion("weight is null");
            return (Criteria) this;
        }

        public Criteria andWeightIsNotNull() {
            addCriterion("weight is not null");
            return (Criteria) this;
        }

        public Criteria andWeightEqualTo(Integer value) {
            addCriterion("weight =", value, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightNotEqualTo(Integer value) {
            addCriterion("weight <>", value, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightGreaterThan(Integer value) {
            addCriterion("weight >", value, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightGreaterThanOrEqualTo(Integer value) {
            addCriterion("weight >=", value, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightLessThan(Integer value) {
            addCriterion("weight <", value, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightLessThanOrEqualTo(Integer value) {
            addCriterion("weight <=", value, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightIn(List<Integer> values) {
            addCriterion("weight in", values, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightNotIn(List<Integer> values) {
            addCriterion("weight not in", values, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightBetween(Integer value1, Integer value2) {
            addCriterion("weight between", value1, value2, "weight");
            return (Criteria) this;
        }

        public Criteria andWeightNotBetween(Integer value1, Integer value2) {
            addCriterion("weight not between", value1, value2, "weight");
            return (Criteria) this;
        }

        public Criteria andSelectTypeIdIsNull() {
            addCriterion("select_type_id is null");
            return (Criteria) this;
        }

        public Criteria andSelectTypeIdIsNotNull() {
            addCriterion("select_type_id is not null");
            return (Criteria) this;
        }

        public Criteria andSelectTypeIdEqualTo(Integer value) {
            addCriterion("select_type_id =", value, "selectTypeId");
            return (Criteria) this;
        }

        public Criteria andSelectTypeIdNotEqualTo(Integer value) {
            addCriterion("select_type_id <>", value, "selectTypeId");
            return (Criteria) this;
        }

        public Criteria andSelectTypeIdGreaterThan(Integer value) {
            addCriterion("select_type_id >", value, "selectTypeId");
            return (Criteria) this;
        }

        public Criteria andSelectTypeIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("select_type_id >=", value, "selectTypeId");
            return (Criteria) this;
        }

        public Criteria andSelectTypeIdLessThan(Integer value) {
            addCriterion("select_type_id <", value, "selectTypeId");
            return (Criteria) this;
        }

        public Criteria andSelectTypeIdLessThanOrEqualTo(Integer value) {
            addCriterion("select_type_id <=", value, "selectTypeId");
            return (Criteria) this;
        }

        public Criteria andSelectTypeIdIn(List<Integer> values) {
            addCriterion("select_type_id in", values, "selectTypeId");
            return (Criteria) this;
        }

        public Criteria andSelectTypeIdNotIn(List<Integer> values) {
            addCriterion("select_type_id not in", values, "selectTypeId");
            return (Criteria) this;
        }

        public Criteria andSelectTypeIdBetween(Integer value1, Integer value2) {
            addCriterion("select_type_id between", value1, value2, "selectTypeId");
            return (Criteria) this;
        }

        public Criteria andSelectTypeIdNotBetween(Integer value1, Integer value2) {
            addCriterion("select_type_id not between", value1, value2, "selectTypeId");
            return (Criteria) this;
        }

        public Criteria andSelectTimeIsNull() {
            addCriterion("select_time is null");
            return (Criteria) this;
        }

        public Criteria andSelectTimeIsNotNull() {
            addCriterion("select_time is not null");
            return (Criteria) this;
        }

        public Criteria andSelectTimeEqualTo(Date value) {
            addCriterion("select_time =", value, "selectTime");
            return (Criteria) this;
        }

        public Criteria andSelectTimeNotEqualTo(Date value) {
            addCriterion("select_time <>", value, "selectTime");
            return (Criteria) this;
        }

        public Criteria andSelectTimeGreaterThan(Date value) {
            addCriterion("select_time >", value, "selectTime");
            return (Criteria) this;
        }

        public Criteria andSelectTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("select_time >=", value, "selectTime");
            return (Criteria) this;
        }

        public Criteria andSelectTimeLessThan(Date value) {
            addCriterion("select_time <", value, "selectTime");
            return (Criteria) this;
        }

        public Criteria andSelectTimeLessThanOrEqualTo(Date value) {
            addCriterion("select_time <=", value, "selectTime");
            return (Criteria) this;
        }

        public Criteria andSelectTimeIn(List<Date> values) {
            addCriterion("select_time in", values, "selectTime");
            return (Criteria) this;
        }

        public Criteria andSelectTimeNotIn(List<Date> values) {
            addCriterion("select_time not in", values, "selectTime");
            return (Criteria) this;
        }

        public Criteria andSelectTimeBetween(Date value1, Date value2) {
            addCriterion("select_time between", value1, value2, "selectTime");
            return (Criteria) this;
        }

        public Criteria andSelectTimeNotBetween(Date value1, Date value2) {
            addCriterion("select_time not between", value1, value2, "selectTime");
            return (Criteria) this;
        }

        public Criteria andModifyTimeIsNull() {
            addCriterion("modify_time is null");
            return (Criteria) this;
        }

        public Criteria andModifyTimeIsNotNull() {
            addCriterion("modify_time is not null");
            return (Criteria) this;
        }

        public Criteria andModifyTimeEqualTo(Date value) {
            addCriterion("modify_time =", value, "modifyTime");
            return (Criteria) this;
        }

        public Criteria andModifyTimeNotEqualTo(Date value) {
            addCriterion("modify_time <>", value, "modifyTime");
            return (Criteria) this;
        }

        public Criteria andModifyTimeGreaterThan(Date value) {
            addCriterion("modify_time >", value, "modifyTime");
            return (Criteria) this;
        }

        public Criteria andModifyTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("modify_time >=", value, "modifyTime");
            return (Criteria) this;
        }

        public Criteria andModifyTimeLessThan(Date value) {
            addCriterion("modify_time <", value, "modifyTime");
            return (Criteria) this;
        }

        public Criteria andModifyTimeLessThanOrEqualTo(Date value) {
            addCriterion("modify_time <=", value, "modifyTime");
            return (Criteria) this;
        }

        public Criteria andModifyTimeIn(List<Date> values) {
            addCriterion("modify_time in", values, "modifyTime");
            return (Criteria) this;
        }

        public Criteria andModifyTimeNotIn(List<Date> values) {
            addCriterion("modify_time not in", values, "modifyTime");
            return (Criteria) this;
        }

        public Criteria andModifyTimeBetween(Date value1, Date value2) {
            addCriterion("modify_time between", value1, value2, "modifyTime");
            return (Criteria) this;
        }

        public Criteria andModifyTimeNotBetween(Date value1, Date value2) {
            addCriterion("modify_time not between", value1, value2, "modifyTime");
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

        public Criteria andNoticeTopTimeIsNull() {
            addCriterion("notice_top_time is null");
            return (Criteria) this;
        }

        public Criteria andNoticeTopTimeIsNotNull() {
            addCriterion("notice_top_time is not null");
            return (Criteria) this;
        }

        public Criteria andNoticeTopTimeEqualTo(Date value) {
            addCriterion("notice_top_time =", value, "noticeTopTime");
            return (Criteria) this;
        }

        public Criteria andNoticeTopTimeNotEqualTo(Date value) {
            addCriterion("notice_top_time <>", value, "noticeTopTime");
            return (Criteria) this;
        }

        public Criteria andNoticeTopTimeGreaterThan(Date value) {
            addCriterion("notice_top_time >", value, "noticeTopTime");
            return (Criteria) this;
        }

        public Criteria andNoticeTopTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("notice_top_time >=", value, "noticeTopTime");
            return (Criteria) this;
        }

        public Criteria andNoticeTopTimeLessThan(Date value) {
            addCriterion("notice_top_time <", value, "noticeTopTime");
            return (Criteria) this;
        }

        public Criteria andNoticeTopTimeLessThanOrEqualTo(Date value) {
            addCriterion("notice_top_time <=", value, "noticeTopTime");
            return (Criteria) this;
        }

        public Criteria andNoticeTopTimeIn(List<Date> values) {
            addCriterion("notice_top_time in", values, "noticeTopTime");
            return (Criteria) this;
        }

        public Criteria andNoticeTopTimeNotIn(List<Date> values) {
            addCriterion("notice_top_time not in", values, "noticeTopTime");
            return (Criteria) this;
        }

        public Criteria andNoticeTopTimeBetween(Date value1, Date value2) {
            addCriterion("notice_top_time between", value1, value2, "noticeTopTime");
            return (Criteria) this;
        }

        public Criteria andNoticeTopTimeNotBetween(Date value1, Date value2) {
            addCriterion("notice_top_time not between", value1, value2, "noticeTopTime");
            return (Criteria) this;
        }

        public Criteria andHotIsNull() {
            addCriterion("hot is null");
            return (Criteria) this;
        }

        public Criteria andHotIsNotNull() {
            addCriterion("hot is not null");
            return (Criteria) this;
        }

        public Criteria andHotEqualTo(Integer value) {
            addCriterion("hot =", value, "hot");
            return (Criteria) this;
        }

        public Criteria andHotNotEqualTo(Integer value) {
            addCriterion("hot <>", value, "hot");
            return (Criteria) this;
        }

        public Criteria andHotGreaterThan(Integer value) {
            addCriterion("hot >", value, "hot");
            return (Criteria) this;
        }

        public Criteria andHotGreaterThanOrEqualTo(Integer value) {
            addCriterion("hot >=", value, "hot");
            return (Criteria) this;
        }

        public Criteria andHotLessThan(Integer value) {
            addCriterion("hot <", value, "hot");
            return (Criteria) this;
        }

        public Criteria andHotLessThanOrEqualTo(Integer value) {
            addCriterion("hot <=", value, "hot");
            return (Criteria) this;
        }

        public Criteria andHotIn(List<Integer> values) {
            addCriterion("hot in", values, "hot");
            return (Criteria) this;
        }

        public Criteria andHotNotIn(List<Integer> values) {
            addCriterion("hot not in", values, "hot");
            return (Criteria) this;
        }

        public Criteria andHotBetween(Integer value1, Integer value2) {
            addCriterion("hot between", value1, value2, "hot");
            return (Criteria) this;
        }

        public Criteria andHotNotBetween(Integer value1, Integer value2) {
            addCriterion("hot not between", value1, value2, "hot");
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

        public Criteria andNewestCmtTimeIsNull() {
            addCriterion("newest_cmt_time is null");
            return (Criteria) this;
        }

        public Criteria andNewestCmtTimeIsNotNull() {
            addCriterion("newest_cmt_time is not null");
            return (Criteria) this;
        }

        public Criteria andNewestCmtTimeEqualTo(Date value) {
            addCriterion("newest_cmt_time =", value, "newestCmtTime");
            return (Criteria) this;
        }

        public Criteria andNewestCmtTimeNotEqualTo(Date value) {
            addCriterion("newest_cmt_time <>", value, "newestCmtTime");
            return (Criteria) this;
        }

        public Criteria andNewestCmtTimeGreaterThan(Date value) {
            addCriterion("newest_cmt_time >", value, "newestCmtTime");
            return (Criteria) this;
        }

        public Criteria andNewestCmtTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("newest_cmt_time >=", value, "newestCmtTime");
            return (Criteria) this;
        }

        public Criteria andNewestCmtTimeLessThan(Date value) {
            addCriterion("newest_cmt_time <", value, "newestCmtTime");
            return (Criteria) this;
        }

        public Criteria andNewestCmtTimeLessThanOrEqualTo(Date value) {
            addCriterion("newest_cmt_time <=", value, "newestCmtTime");
            return (Criteria) this;
        }

        public Criteria andNewestCmtTimeIn(List<Date> values) {
            addCriterion("newest_cmt_time in", values, "newestCmtTime");
            return (Criteria) this;
        }

        public Criteria andNewestCmtTimeNotIn(List<Date> values) {
            addCriterion("newest_cmt_time not in", values, "newestCmtTime");
            return (Criteria) this;
        }

        public Criteria andNewestCmtTimeBetween(Date value1, Date value2) {
            addCriterion("newest_cmt_time between", value1, value2, "newestCmtTime");
            return (Criteria) this;
        }

        public Criteria andNewestCmtTimeNotBetween(Date value1, Date value2) {
            addCriterion("newest_cmt_time not between", value1, value2, "newestCmtTime");
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