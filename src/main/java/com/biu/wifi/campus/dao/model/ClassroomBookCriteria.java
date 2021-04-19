package com.biu.wifi.campus.dao.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ClassroomBookCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private Integer limit;

    private Long offset;

    public ClassroomBookCriteria() {
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

        public Criteria andVersionIsNull() {
            addCriterion("version is null");
            return (Criteria) this;
        }

        public Criteria andVersionIsNotNull() {
            addCriterion("version is not null");
            return (Criteria) this;
        }

        public Criteria andVersionEqualTo(String value) {
            addCriterion("version =", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionNotEqualTo(String value) {
            addCriterion("version <>", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionGreaterThan(String value) {
            addCriterion("version >", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionGreaterThanOrEqualTo(String value) {
            addCriterion("version >=", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionLessThan(String value) {
            addCriterion("version <", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionLessThanOrEqualTo(String value) {
            addCriterion("version <=", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionLike(String value) {
            addCriterion("version like", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionNotLike(String value) {
            addCriterion("version not like", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionIn(List<String> values) {
            addCriterion("version in", values, "version");
            return (Criteria) this;
        }

        public Criteria andVersionNotIn(List<String> values) {
            addCriterion("version not in", values, "version");
            return (Criteria) this;
        }

        public Criteria andVersionBetween(String value1, String value2) {
            addCriterion("version between", value1, value2, "version");
            return (Criteria) this;
        }

        public Criteria andVersionNotBetween(String value1, String value2) {
            addCriterion("version not between", value1, value2, "version");
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

        public Criteria andAuditUserNameIsNull() {
            addCriterion("audit_user_name is null");
            return (Criteria) this;
        }

        public Criteria andAuditUserNameIsNotNull() {
            addCriterion("audit_user_name is not null");
            return (Criteria) this;
        }

        public Criteria andAuditUserNameEqualTo(String value) {
            addCriterion("audit_user_name =", value, "auditUserName");
            return (Criteria) this;
        }

        public Criteria andAuditUserNameNotEqualTo(String value) {
            addCriterion("audit_user_name <>", value, "auditUserName");
            return (Criteria) this;
        }

        public Criteria andAuditUserNameGreaterThan(String value) {
            addCriterion("audit_user_name >", value, "auditUserName");
            return (Criteria) this;
        }

        public Criteria andAuditUserNameGreaterThanOrEqualTo(String value) {
            addCriterion("audit_user_name >=", value, "auditUserName");
            return (Criteria) this;
        }

        public Criteria andAuditUserNameLessThan(String value) {
            addCriterion("audit_user_name <", value, "auditUserName");
            return (Criteria) this;
        }

        public Criteria andAuditUserNameLessThanOrEqualTo(String value) {
            addCriterion("audit_user_name <=", value, "auditUserName");
            return (Criteria) this;
        }

        public Criteria andAuditUserNameLike(String value) {
            addCriterion("audit_user_name like", value, "auditUserName");
            return (Criteria) this;
        }

        public Criteria andAuditUserNameNotLike(String value) {
            addCriterion("audit_user_name not like", value, "auditUserName");
            return (Criteria) this;
        }

        public Criteria andAuditUserNameIn(List<String> values) {
            addCriterion("audit_user_name in", values, "auditUserName");
            return (Criteria) this;
        }

        public Criteria andAuditUserNameNotIn(List<String> values) {
            addCriterion("audit_user_name not in", values, "auditUserName");
            return (Criteria) this;
        }

        public Criteria andAuditUserNameBetween(String value1, String value2) {
            addCriterion("audit_user_name between", value1, value2, "auditUserName");
            return (Criteria) this;
        }

        public Criteria andAuditUserNameNotBetween(String value1, String value2) {
            addCriterion("audit_user_name not between", value1, value2, "auditUserName");
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

        public Criteria andClassroomBuildingIdIsNull() {
            addCriterion("classroom_building_id is null");
            return (Criteria) this;
        }

        public Criteria andClassroomBuildingIdIsNotNull() {
            addCriterion("classroom_building_id is not null");
            return (Criteria) this;
        }

        public Criteria andClassroomBuildingIdEqualTo(Integer value) {
            addCriterion("classroom_building_id =", value, "classroomBuildingId");
            return (Criteria) this;
        }

        public Criteria andClassroomBuildingIdNotEqualTo(Integer value) {
            addCriterion("classroom_building_id <>", value, "classroomBuildingId");
            return (Criteria) this;
        }

        public Criteria andClassroomBuildingIdGreaterThan(Integer value) {
            addCriterion("classroom_building_id >", value, "classroomBuildingId");
            return (Criteria) this;
        }

        public Criteria andClassroomBuildingIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("classroom_building_id >=", value, "classroomBuildingId");
            return (Criteria) this;
        }

        public Criteria andClassroomBuildingIdLessThan(Integer value) {
            addCriterion("classroom_building_id <", value, "classroomBuildingId");
            return (Criteria) this;
        }

        public Criteria andClassroomBuildingIdLessThanOrEqualTo(Integer value) {
            addCriterion("classroom_building_id <=", value, "classroomBuildingId");
            return (Criteria) this;
        }

        public Criteria andClassroomBuildingIdIn(List<Integer> values) {
            addCriterion("classroom_building_id in", values, "classroomBuildingId");
            return (Criteria) this;
        }

        public Criteria andClassroomBuildingIdNotIn(List<Integer> values) {
            addCriterion("classroom_building_id not in", values, "classroomBuildingId");
            return (Criteria) this;
        }

        public Criteria andClassroomBuildingIdBetween(Integer value1, Integer value2) {
            addCriterion("classroom_building_id between", value1, value2, "classroomBuildingId");
            return (Criteria) this;
        }

        public Criteria andClassroomBuildingIdNotBetween(Integer value1, Integer value2) {
            addCriterion("classroom_building_id not between", value1, value2, "classroomBuildingId");
            return (Criteria) this;
        }

        public Criteria andPersonCountIsNull() {
            addCriterion("person_count is null");
            return (Criteria) this;
        }

        public Criteria andPersonCountIsNotNull() {
            addCriterion("person_count is not null");
            return (Criteria) this;
        }

        public Criteria andPersonCountEqualTo(String value) {
            addCriterion("person_count =", value, "personCount");
            return (Criteria) this;
        }

        public Criteria andPersonCountNotEqualTo(String value) {
            addCriterion("person_count <>", value, "personCount");
            return (Criteria) this;
        }

        public Criteria andPersonCountGreaterThan(String value) {
            addCriterion("person_count >", value, "personCount");
            return (Criteria) this;
        }

        public Criteria andPersonCountGreaterThanOrEqualTo(String value) {
            addCriterion("person_count >=", value, "personCount");
            return (Criteria) this;
        }

        public Criteria andPersonCountLessThan(String value) {
            addCriterion("person_count <", value, "personCount");
            return (Criteria) this;
        }

        public Criteria andPersonCountLessThanOrEqualTo(String value) {
            addCriterion("person_count <=", value, "personCount");
            return (Criteria) this;
        }

        public Criteria andPersonCountLike(String value) {
            addCriterion("person_count like", value, "personCount");
            return (Criteria) this;
        }

        public Criteria andPersonCountNotLike(String value) {
            addCriterion("person_count not like", value, "personCount");
            return (Criteria) this;
        }

        public Criteria andPersonCountIn(List<String> values) {
            addCriterion("person_count in", values, "personCount");
            return (Criteria) this;
        }

        public Criteria andPersonCountNotIn(List<String> values) {
            addCriterion("person_count not in", values, "personCount");
            return (Criteria) this;
        }

        public Criteria andPersonCountBetween(String value1, String value2) {
            addCriterion("person_count between", value1, value2, "personCount");
            return (Criteria) this;
        }

        public Criteria andPersonCountNotBetween(String value1, String value2) {
            addCriterion("person_count not between", value1, value2, "personCount");
            return (Criteria) this;
        }

        public Criteria andUseTypeIdIsNull() {
            addCriterion("use_type_id is null");
            return (Criteria) this;
        }

        public Criteria andUseTypeIdIsNotNull() {
            addCriterion("use_type_id is not null");
            return (Criteria) this;
        }

        public Criteria andUseTypeIdEqualTo(Integer value) {
            addCriterion("use_type_id =", value, "useTypeId");
            return (Criteria) this;
        }

        public Criteria andUseTypeIdNotEqualTo(Integer value) {
            addCriterion("use_type_id <>", value, "useTypeId");
            return (Criteria) this;
        }

        public Criteria andUseTypeIdGreaterThan(Integer value) {
            addCriterion("use_type_id >", value, "useTypeId");
            return (Criteria) this;
        }

        public Criteria andUseTypeIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("use_type_id >=", value, "useTypeId");
            return (Criteria) this;
        }

        public Criteria andUseTypeIdLessThan(Integer value) {
            addCriterion("use_type_id <", value, "useTypeId");
            return (Criteria) this;
        }

        public Criteria andUseTypeIdLessThanOrEqualTo(Integer value) {
            addCriterion("use_type_id <=", value, "useTypeId");
            return (Criteria) this;
        }

        public Criteria andUseTypeIdIn(List<Integer> values) {
            addCriterion("use_type_id in", values, "useTypeId");
            return (Criteria) this;
        }

        public Criteria andUseTypeIdNotIn(List<Integer> values) {
            addCriterion("use_type_id not in", values, "useTypeId");
            return (Criteria) this;
        }

        public Criteria andUseTypeIdBetween(Integer value1, Integer value2) {
            addCriterion("use_type_id between", value1, value2, "useTypeId");
            return (Criteria) this;
        }

        public Criteria andUseTypeIdNotBetween(Integer value1, Integer value2) {
            addCriterion("use_type_id not between", value1, value2, "useTypeId");
            return (Criteria) this;
        }

        public Criteria andOrganizationIdIsNull() {
            addCriterion("organization_id is null");
            return (Criteria) this;
        }

        public Criteria andOrganizationIdIsNotNull() {
            addCriterion("organization_id is not null");
            return (Criteria) this;
        }

        public Criteria andOrganizationIdEqualTo(Integer value) {
            addCriterion("organization_id =", value, "organizationId");
            return (Criteria) this;
        }

        public Criteria andOrganizationIdNotEqualTo(Integer value) {
            addCriterion("organization_id <>", value, "organizationId");
            return (Criteria) this;
        }

        public Criteria andOrganizationIdGreaterThan(Integer value) {
            addCriterion("organization_id >", value, "organizationId");
            return (Criteria) this;
        }

        public Criteria andOrganizationIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("organization_id >=", value, "organizationId");
            return (Criteria) this;
        }

        public Criteria andOrganizationIdLessThan(Integer value) {
            addCriterion("organization_id <", value, "organizationId");
            return (Criteria) this;
        }

        public Criteria andOrganizationIdLessThanOrEqualTo(Integer value) {
            addCriterion("organization_id <=", value, "organizationId");
            return (Criteria) this;
        }

        public Criteria andOrganizationIdIn(List<Integer> values) {
            addCriterion("organization_id in", values, "organizationId");
            return (Criteria) this;
        }

        public Criteria andOrganizationIdNotIn(List<Integer> values) {
            addCriterion("organization_id not in", values, "organizationId");
            return (Criteria) this;
        }

        public Criteria andOrganizationIdBetween(Integer value1, Integer value2) {
            addCriterion("organization_id between", value1, value2, "organizationId");
            return (Criteria) this;
        }

        public Criteria andOrganizationIdNotBetween(Integer value1, Integer value2) {
            addCriterion("organization_id not between", value1, value2, "organizationId");
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

        public Criteria andAdjustClassroomNoIsNull() {
            addCriterion("adjust_classroom_no is null");
            return (Criteria) this;
        }

        public Criteria andAdjustClassroomNoIsNotNull() {
            addCriterion("adjust_classroom_no is not null");
            return (Criteria) this;
        }

        public Criteria andAdjustClassroomNoEqualTo(String value) {
            addCriterion("adjust_classroom_no =", value, "adjustClassroomNo");
            return (Criteria) this;
        }

        public Criteria andAdjustClassroomNoNotEqualTo(String value) {
            addCriterion("adjust_classroom_no <>", value, "adjustClassroomNo");
            return (Criteria) this;
        }

        public Criteria andAdjustClassroomNoGreaterThan(String value) {
            addCriterion("adjust_classroom_no >", value, "adjustClassroomNo");
            return (Criteria) this;
        }

        public Criteria andAdjustClassroomNoGreaterThanOrEqualTo(String value) {
            addCriterion("adjust_classroom_no >=", value, "adjustClassroomNo");
            return (Criteria) this;
        }

        public Criteria andAdjustClassroomNoLessThan(String value) {
            addCriterion("adjust_classroom_no <", value, "adjustClassroomNo");
            return (Criteria) this;
        }

        public Criteria andAdjustClassroomNoLessThanOrEqualTo(String value) {
            addCriterion("adjust_classroom_no <=", value, "adjustClassroomNo");
            return (Criteria) this;
        }

        public Criteria andAdjustClassroomNoLike(String value) {
            addCriterion("adjust_classroom_no like", value, "adjustClassroomNo");
            return (Criteria) this;
        }

        public Criteria andAdjustClassroomNoNotLike(String value) {
            addCriterion("adjust_classroom_no not like", value, "adjustClassroomNo");
            return (Criteria) this;
        }

        public Criteria andAdjustClassroomNoIn(List<String> values) {
            addCriterion("adjust_classroom_no in", values, "adjustClassroomNo");
            return (Criteria) this;
        }

        public Criteria andAdjustClassroomNoNotIn(List<String> values) {
            addCriterion("adjust_classroom_no not in", values, "adjustClassroomNo");
            return (Criteria) this;
        }

        public Criteria andAdjustClassroomNoBetween(String value1, String value2) {
            addCriterion("adjust_classroom_no between", value1, value2, "adjustClassroomNo");
            return (Criteria) this;
        }

        public Criteria andAdjustClassroomNoNotBetween(String value1, String value2) {
            addCriterion("adjust_classroom_no not between", value1, value2, "adjustClassroomNo");
            return (Criteria) this;
        }

        public Criteria andIsUseMediaIsNull() {
            addCriterion("is_use_media is null");
            return (Criteria) this;
        }

        public Criteria andIsUseMediaIsNotNull() {
            addCriterion("is_use_media is not null");
            return (Criteria) this;
        }

        public Criteria andIsUseMediaEqualTo(Short value) {
            addCriterion("is_use_media =", value, "isUseMedia");
            return (Criteria) this;
        }

        public Criteria andIsUseMediaNotEqualTo(Short value) {
            addCriterion("is_use_media <>", value, "isUseMedia");
            return (Criteria) this;
        }

        public Criteria andIsUseMediaGreaterThan(Short value) {
            addCriterion("is_use_media >", value, "isUseMedia");
            return (Criteria) this;
        }

        public Criteria andIsUseMediaGreaterThanOrEqualTo(Short value) {
            addCriterion("is_use_media >=", value, "isUseMedia");
            return (Criteria) this;
        }

        public Criteria andIsUseMediaLessThan(Short value) {
            addCriterion("is_use_media <", value, "isUseMedia");
            return (Criteria) this;
        }

        public Criteria andIsUseMediaLessThanOrEqualTo(Short value) {
            addCriterion("is_use_media <=", value, "isUseMedia");
            return (Criteria) this;
        }

        public Criteria andIsUseMediaIn(List<Short> values) {
            addCriterion("is_use_media in", values, "isUseMedia");
            return (Criteria) this;
        }

        public Criteria andIsUseMediaNotIn(List<Short> values) {
            addCriterion("is_use_media not in", values, "isUseMedia");
            return (Criteria) this;
        }

        public Criteria andIsUseMediaBetween(Short value1, Short value2) {
            addCriterion("is_use_media between", value1, value2, "isUseMedia");
            return (Criteria) this;
        }

        public Criteria andIsUseMediaNotBetween(Short value1, Short value2) {
            addCriterion("is_use_media not between", value1, value2, "isUseMedia");
            return (Criteria) this;
        }

        public Criteria andLinkManIsNull() {
            addCriterion("link_man is null");
            return (Criteria) this;
        }

        public Criteria andLinkManIsNotNull() {
            addCriterion("link_man is not null");
            return (Criteria) this;
        }

        public Criteria andLinkManEqualTo(String value) {
            addCriterion("link_man =", value, "linkMan");
            return (Criteria) this;
        }

        public Criteria andLinkManNotEqualTo(String value) {
            addCriterion("link_man <>", value, "linkMan");
            return (Criteria) this;
        }

        public Criteria andLinkManGreaterThan(String value) {
            addCriterion("link_man >", value, "linkMan");
            return (Criteria) this;
        }

        public Criteria andLinkManGreaterThanOrEqualTo(String value) {
            addCriterion("link_man >=", value, "linkMan");
            return (Criteria) this;
        }

        public Criteria andLinkManLessThan(String value) {
            addCriterion("link_man <", value, "linkMan");
            return (Criteria) this;
        }

        public Criteria andLinkManLessThanOrEqualTo(String value) {
            addCriterion("link_man <=", value, "linkMan");
            return (Criteria) this;
        }

        public Criteria andLinkManLike(String value) {
            addCriterion("link_man like", value, "linkMan");
            return (Criteria) this;
        }

        public Criteria andLinkManNotLike(String value) {
            addCriterion("link_man not like", value, "linkMan");
            return (Criteria) this;
        }

        public Criteria andLinkManIn(List<String> values) {
            addCriterion("link_man in", values, "linkMan");
            return (Criteria) this;
        }

        public Criteria andLinkManNotIn(List<String> values) {
            addCriterion("link_man not in", values, "linkMan");
            return (Criteria) this;
        }

        public Criteria andLinkManBetween(String value1, String value2) {
            addCriterion("link_man between", value1, value2, "linkMan");
            return (Criteria) this;
        }

        public Criteria andLinkManNotBetween(String value1, String value2) {
            addCriterion("link_man not between", value1, value2, "linkMan");
            return (Criteria) this;
        }

        public Criteria andLinkPhoneIsNull() {
            addCriterion("link_phone is null");
            return (Criteria) this;
        }

        public Criteria andLinkPhoneIsNotNull() {
            addCriterion("link_phone is not null");
            return (Criteria) this;
        }

        public Criteria andLinkPhoneEqualTo(String value) {
            addCriterion("link_phone =", value, "linkPhone");
            return (Criteria) this;
        }

        public Criteria andLinkPhoneNotEqualTo(String value) {
            addCriterion("link_phone <>", value, "linkPhone");
            return (Criteria) this;
        }

        public Criteria andLinkPhoneGreaterThan(String value) {
            addCriterion("link_phone >", value, "linkPhone");
            return (Criteria) this;
        }

        public Criteria andLinkPhoneGreaterThanOrEqualTo(String value) {
            addCriterion("link_phone >=", value, "linkPhone");
            return (Criteria) this;
        }

        public Criteria andLinkPhoneLessThan(String value) {
            addCriterion("link_phone <", value, "linkPhone");
            return (Criteria) this;
        }

        public Criteria andLinkPhoneLessThanOrEqualTo(String value) {
            addCriterion("link_phone <=", value, "linkPhone");
            return (Criteria) this;
        }

        public Criteria andLinkPhoneLike(String value) {
            addCriterion("link_phone like", value, "linkPhone");
            return (Criteria) this;
        }

        public Criteria andLinkPhoneNotLike(String value) {
            addCriterion("link_phone not like", value, "linkPhone");
            return (Criteria) this;
        }

        public Criteria andLinkPhoneIn(List<String> values) {
            addCriterion("link_phone in", values, "linkPhone");
            return (Criteria) this;
        }

        public Criteria andLinkPhoneNotIn(List<String> values) {
            addCriterion("link_phone not in", values, "linkPhone");
            return (Criteria) this;
        }

        public Criteria andLinkPhoneBetween(String value1, String value2) {
            addCriterion("link_phone between", value1, value2, "linkPhone");
            return (Criteria) this;
        }

        public Criteria andLinkPhoneNotBetween(String value1, String value2) {
            addCriterion("link_phone not between", value1, value2, "linkPhone");
            return (Criteria) this;
        }

        public Criteria andLinkManNoIsNull() {
            addCriterion("link_man_no is null");
            return (Criteria) this;
        }

        public Criteria andLinkManNoIsNotNull() {
            addCriterion("link_man_no is not null");
            return (Criteria) this;
        }

        public Criteria andLinkManNoEqualTo(String value) {
            addCriterion("link_man_no =", value, "linkManNo");
            return (Criteria) this;
        }

        public Criteria andLinkManNoNotEqualTo(String value) {
            addCriterion("link_man_no <>", value, "linkManNo");
            return (Criteria) this;
        }

        public Criteria andLinkManNoGreaterThan(String value) {
            addCriterion("link_man_no >", value, "linkManNo");
            return (Criteria) this;
        }

        public Criteria andLinkManNoGreaterThanOrEqualTo(String value) {
            addCriterion("link_man_no >=", value, "linkManNo");
            return (Criteria) this;
        }

        public Criteria andLinkManNoLessThan(String value) {
            addCriterion("link_man_no <", value, "linkManNo");
            return (Criteria) this;
        }

        public Criteria andLinkManNoLessThanOrEqualTo(String value) {
            addCriterion("link_man_no <=", value, "linkManNo");
            return (Criteria) this;
        }

        public Criteria andLinkManNoLike(String value) {
            addCriterion("link_man_no like", value, "linkManNo");
            return (Criteria) this;
        }

        public Criteria andLinkManNoNotLike(String value) {
            addCriterion("link_man_no not like", value, "linkManNo");
            return (Criteria) this;
        }

        public Criteria andLinkManNoIn(List<String> values) {
            addCriterion("link_man_no in", values, "linkManNo");
            return (Criteria) this;
        }

        public Criteria andLinkManNoNotIn(List<String> values) {
            addCriterion("link_man_no not in", values, "linkManNo");
            return (Criteria) this;
        }

        public Criteria andLinkManNoBetween(String value1, String value2) {
            addCriterion("link_man_no between", value1, value2, "linkManNo");
            return (Criteria) this;
        }

        public Criteria andLinkManNoNotBetween(String value1, String value2) {
            addCriterion("link_man_no not between", value1, value2, "linkManNo");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNull() {
            addCriterion("user_id is null");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNotNull() {
            addCriterion("user_id is not null");
            return (Criteria) this;
        }

        public Criteria andUserIdEqualTo(Integer value) {
            addCriterion("user_id =", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotEqualTo(Integer value) {
            addCriterion("user_id <>", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThan(Integer value) {
            addCriterion("user_id >", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("user_id >=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThan(Integer value) {
            addCriterion("user_id <", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThanOrEqualTo(Integer value) {
            addCriterion("user_id <=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdIn(List<Integer> values) {
            addCriterion("user_id in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotIn(List<Integer> values) {
            addCriterion("user_id not in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdBetween(Integer value1, Integer value2) {
            addCriterion("user_id between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotBetween(Integer value1, Integer value2) {
            addCriterion("user_id not between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andBookDateIsNull() {
            addCriterion("book_date is null");
            return (Criteria) this;
        }

        public Criteria andBookDateIsNotNull() {
            addCriterion("book_date is not null");
            return (Criteria) this;
        }

        public Criteria andBookDateEqualTo(String value) {
            addCriterion("book_date =", value, "bookDate");
            return (Criteria) this;
        }

        public Criteria andBookDateNotEqualTo(String value) {
            addCriterion("book_date <>", value, "bookDate");
            return (Criteria) this;
        }

        public Criteria andBookDateGreaterThan(String value) {
            addCriterion("book_date >", value, "bookDate");
            return (Criteria) this;
        }

        public Criteria andBookDateGreaterThanOrEqualTo(String value) {
            addCriterion("book_date >=", value, "bookDate");
            return (Criteria) this;
        }

        public Criteria andBookDateLessThan(String value) {
            addCriterion("book_date <", value, "bookDate");
            return (Criteria) this;
        }

        public Criteria andBookDateLessThanOrEqualTo(String value) {
            addCriterion("book_date <=", value, "bookDate");
            return (Criteria) this;
        }

        public Criteria andBookDateLike(String value) {
            addCriterion("book_date like", value, "bookDate");
            return (Criteria) this;
        }

        public Criteria andBookDateNotLike(String value) {
            addCriterion("book_date not like", value, "bookDate");
            return (Criteria) this;
        }

        public Criteria andBookDateIn(List<String> values) {
            addCriterion("book_date in", values, "bookDate");
            return (Criteria) this;
        }

        public Criteria andBookDateNotIn(List<String> values) {
            addCriterion("book_date not in", values, "bookDate");
            return (Criteria) this;
        }

        public Criteria andBookDateBetween(String value1, String value2) {
            addCriterion("book_date between", value1, value2, "bookDate");
            return (Criteria) this;
        }

        public Criteria andBookDateNotBetween(String value1, String value2) {
            addCriterion("book_date not between", value1, value2, "bookDate");
            return (Criteria) this;
        }

        public Criteria andBookPeriodIsNull() {
            addCriterion("book_period is null");
            return (Criteria) this;
        }

        public Criteria andBookPeriodIsNotNull() {
            addCriterion("book_period is not null");
            return (Criteria) this;
        }

        public Criteria andBookPeriodEqualTo(String value) {
            addCriterion("book_period =", value, "bookPeriod");
            return (Criteria) this;
        }

        public Criteria andBookPeriodNotEqualTo(String value) {
            addCriterion("book_period <>", value, "bookPeriod");
            return (Criteria) this;
        }

        public Criteria andBookPeriodGreaterThan(String value) {
            addCriterion("book_period >", value, "bookPeriod");
            return (Criteria) this;
        }

        public Criteria andBookPeriodGreaterThanOrEqualTo(String value) {
            addCriterion("book_period >=", value, "bookPeriod");
            return (Criteria) this;
        }

        public Criteria andBookPeriodLessThan(String value) {
            addCriterion("book_period <", value, "bookPeriod");
            return (Criteria) this;
        }

        public Criteria andBookPeriodLessThanOrEqualTo(String value) {
            addCriterion("book_period <=", value, "bookPeriod");
            return (Criteria) this;
        }

        public Criteria andBookPeriodLike(String value) {
            addCriterion("book_period like", value, "bookPeriod");
            return (Criteria) this;
        }

        public Criteria andBookPeriodNotLike(String value) {
            addCriterion("book_period not like", value, "bookPeriod");
            return (Criteria) this;
        }

        public Criteria andBookPeriodIn(List<String> values) {
            addCriterion("book_period in", values, "bookPeriod");
            return (Criteria) this;
        }

        public Criteria andBookPeriodNotIn(List<String> values) {
            addCriterion("book_period not in", values, "bookPeriod");
            return (Criteria) this;
        }

        public Criteria andBookPeriodBetween(String value1, String value2) {
            addCriterion("book_period between", value1, value2, "bookPeriod");
            return (Criteria) this;
        }

        public Criteria andBookPeriodNotBetween(String value1, String value2) {
            addCriterion("book_period not between", value1, value2, "bookPeriod");
            return (Criteria) this;
        }

        public Criteria andBookSectionIsNull() {
            addCriterion("book_section is null");
            return (Criteria) this;
        }

        public Criteria andBookSectionIsNotNull() {
            addCriterion("book_section is not null");
            return (Criteria) this;
        }

        public Criteria andBookSectionEqualTo(String value) {
            addCriterion("book_section =", value, "bookSection");
            return (Criteria) this;
        }

        public Criteria andBookSectionNotEqualTo(String value) {
            addCriterion("book_section <>", value, "bookSection");
            return (Criteria) this;
        }

        public Criteria andBookSectionGreaterThan(String value) {
            addCriterion("book_section >", value, "bookSection");
            return (Criteria) this;
        }

        public Criteria andBookSectionGreaterThanOrEqualTo(String value) {
            addCriterion("book_section >=", value, "bookSection");
            return (Criteria) this;
        }

        public Criteria andBookSectionLessThan(String value) {
            addCriterion("book_section <", value, "bookSection");
            return (Criteria) this;
        }

        public Criteria andBookSectionLessThanOrEqualTo(String value) {
            addCriterion("book_section <=", value, "bookSection");
            return (Criteria) this;
        }

        public Criteria andBookSectionLike(String value) {
            addCriterion("book_section like", value, "bookSection");
            return (Criteria) this;
        }

        public Criteria andBookSectionNotLike(String value) {
            addCriterion("book_section not like", value, "bookSection");
            return (Criteria) this;
        }

        public Criteria andBookSectionIn(List<String> values) {
            addCriterion("book_section in", values, "bookSection");
            return (Criteria) this;
        }

        public Criteria andBookSectionNotIn(List<String> values) {
            addCriterion("book_section not in", values, "bookSection");
            return (Criteria) this;
        }

        public Criteria andBookSectionBetween(String value1, String value2) {
            addCriterion("book_section between", value1, value2, "bookSection");
            return (Criteria) this;
        }

        public Criteria andBookSectionNotBetween(String value1, String value2) {
            addCriterion("book_section not between", value1, value2, "bookSection");
            return (Criteria) this;
        }

        public Criteria andBookConditionIsNull() {
            addCriterion("book_condition is null");
            return (Criteria) this;
        }

        public Criteria andBookConditionIsNotNull() {
            addCriterion("book_condition is not null");
            return (Criteria) this;
        }

        public Criteria andBookConditionEqualTo(String value) {
            addCriterion("book_condition =", value, "bookCondition");
            return (Criteria) this;
        }

        public Criteria andBookConditionNotEqualTo(String value) {
            addCriterion("book_condition <>", value, "bookCondition");
            return (Criteria) this;
        }

        public Criteria andBookConditionGreaterThan(String value) {
            addCriterion("book_condition >", value, "bookCondition");
            return (Criteria) this;
        }

        public Criteria andBookConditionGreaterThanOrEqualTo(String value) {
            addCriterion("book_condition >=", value, "bookCondition");
            return (Criteria) this;
        }

        public Criteria andBookConditionLessThan(String value) {
            addCriterion("book_condition <", value, "bookCondition");
            return (Criteria) this;
        }

        public Criteria andBookConditionLessThanOrEqualTo(String value) {
            addCriterion("book_condition <=", value, "bookCondition");
            return (Criteria) this;
        }

        public Criteria andBookConditionLike(String value) {
            addCriterion("book_condition like", value, "bookCondition");
            return (Criteria) this;
        }

        public Criteria andBookConditionNotLike(String value) {
            addCriterion("book_condition not like", value, "bookCondition");
            return (Criteria) this;
        }

        public Criteria andBookConditionIn(List<String> values) {
            addCriterion("book_condition in", values, "bookCondition");
            return (Criteria) this;
        }

        public Criteria andBookConditionNotIn(List<String> values) {
            addCriterion("book_condition not in", values, "bookCondition");
            return (Criteria) this;
        }

        public Criteria andBookConditionBetween(String value1, String value2) {
            addCriterion("book_condition between", value1, value2, "bookCondition");
            return (Criteria) this;
        }

        public Criteria andBookConditionNotBetween(String value1, String value2) {
            addCriterion("book_condition not between", value1, value2, "bookCondition");
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