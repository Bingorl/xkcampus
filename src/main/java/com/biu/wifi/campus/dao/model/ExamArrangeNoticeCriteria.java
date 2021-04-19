package com.biu.wifi.campus.dao.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExamArrangeNoticeCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private Integer limit;

    private Long offset;

    public ExamArrangeNoticeCriteria() {
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

        public Criteria andIsReceivedIsNull() {
            addCriterion("is_received is null");
            return (Criteria) this;
        }

        public Criteria andIsReceivedIsNotNull() {
            addCriterion("is_received is not null");
            return (Criteria) this;
        }

        public Criteria andIsReceivedEqualTo(Integer value) {
            addCriterion("is_received =", value, "isReceived");
            return (Criteria) this;
        }

        public Criteria andIsReceivedNotEqualTo(Integer value) {
            addCriterion("is_received <>", value, "isReceived");
            return (Criteria) this;
        }

        public Criteria andIsReceivedGreaterThan(Integer value) {
            addCriterion("is_received >", value, "isReceived");
            return (Criteria) this;
        }

        public Criteria andIsReceivedGreaterThanOrEqualTo(Integer value) {
            addCriterion("is_received >=", value, "isReceived");
            return (Criteria) this;
        }

        public Criteria andIsReceivedLessThan(Integer value) {
            addCriterion("is_received <", value, "isReceived");
            return (Criteria) this;
        }

        public Criteria andIsReceivedLessThanOrEqualTo(Integer value) {
            addCriterion("is_received <=", value, "isReceived");
            return (Criteria) this;
        }

        public Criteria andIsReceivedIn(List<Integer> values) {
            addCriterion("is_received in", values, "isReceived");
            return (Criteria) this;
        }

        public Criteria andIsReceivedNotIn(List<Integer> values) {
            addCriterion("is_received not in", values, "isReceived");
            return (Criteria) this;
        }

        public Criteria andIsReceivedBetween(Integer value1, Integer value2) {
            addCriterion("is_received between", value1, value2, "isReceived");
            return (Criteria) this;
        }

        public Criteria andIsReceivedNotBetween(Integer value1, Integer value2) {
            addCriterion("is_received not between", value1, value2, "isReceived");
            return (Criteria) this;
        }

        public Criteria andReceivedTimeIsNull() {
            addCriterion("received_time is null");
            return (Criteria) this;
        }

        public Criteria andReceivedTimeIsNotNull() {
            addCriterion("received_time is not null");
            return (Criteria) this;
        }

        public Criteria andReceivedTimeEqualTo(Date value) {
            addCriterion("received_time =", value, "receivedTime");
            return (Criteria) this;
        }

        public Criteria andReceivedTimeNotEqualTo(Date value) {
            addCriterion("received_time <>", value, "receivedTime");
            return (Criteria) this;
        }

        public Criteria andReceivedTimeGreaterThan(Date value) {
            addCriterion("received_time >", value, "receivedTime");
            return (Criteria) this;
        }

        public Criteria andReceivedTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("received_time >=", value, "receivedTime");
            return (Criteria) this;
        }

        public Criteria andReceivedTimeLessThan(Date value) {
            addCriterion("received_time <", value, "receivedTime");
            return (Criteria) this;
        }

        public Criteria andReceivedTimeLessThanOrEqualTo(Date value) {
            addCriterion("received_time <=", value, "receivedTime");
            return (Criteria) this;
        }

        public Criteria andReceivedTimeIn(List<Date> values) {
            addCriterion("received_time in", values, "receivedTime");
            return (Criteria) this;
        }

        public Criteria andReceivedTimeNotIn(List<Date> values) {
            addCriterion("received_time not in", values, "receivedTime");
            return (Criteria) this;
        }

        public Criteria andReceivedTimeBetween(Date value1, Date value2) {
            addCriterion("received_time between", value1, value2, "receivedTime");
            return (Criteria) this;
        }

        public Criteria andReceivedTimeNotBetween(Date value1, Date value2) {
            addCriterion("received_time not between", value1, value2, "receivedTime");
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

        public Criteria andToUserIsNull() {
            addCriterion("to_user is null");
            return (Criteria) this;
        }

        public Criteria andToUserIsNotNull() {
            addCriterion("to_user is not null");
            return (Criteria) this;
        }

        public Criteria andToUserEqualTo(Integer value) {
            addCriterion("to_user =", value, "toUser");
            return (Criteria) this;
        }

        public Criteria andToUserNotEqualTo(Integer value) {
            addCriterion("to_user <>", value, "toUser");
            return (Criteria) this;
        }

        public Criteria andToUserGreaterThan(Integer value) {
            addCriterion("to_user >", value, "toUser");
            return (Criteria) this;
        }

        public Criteria andToUserGreaterThanOrEqualTo(Integer value) {
            addCriterion("to_user >=", value, "toUser");
            return (Criteria) this;
        }

        public Criteria andToUserLessThan(Integer value) {
            addCriterion("to_user <", value, "toUser");
            return (Criteria) this;
        }

        public Criteria andToUserLessThanOrEqualTo(Integer value) {
            addCriterion("to_user <=", value, "toUser");
            return (Criteria) this;
        }

        public Criteria andToUserIn(List<Integer> values) {
            addCriterion("to_user in", values, "toUser");
            return (Criteria) this;
        }

        public Criteria andToUserNotIn(List<Integer> values) {
            addCriterion("to_user not in", values, "toUser");
            return (Criteria) this;
        }

        public Criteria andToUserBetween(Integer value1, Integer value2) {
            addCriterion("to_user between", value1, value2, "toUser");
            return (Criteria) this;
        }

        public Criteria andToUserNotBetween(Integer value1, Integer value2) {
            addCriterion("to_user not between", value1, value2, "toUser");
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

        public Criteria andIsPushedIsNull() {
            addCriterion("is_pushed is null");
            return (Criteria) this;
        }

        public Criteria andIsPushedIsNotNull() {
            addCriterion("is_pushed is not null");
            return (Criteria) this;
        }

        public Criteria andIsPushedEqualTo(Integer value) {
            addCriterion("is_pushed =", value, "isPushed");
            return (Criteria) this;
        }

        public Criteria andIsPushedNotEqualTo(Integer value) {
            addCriterion("is_pushed <>", value, "isPushed");
            return (Criteria) this;
        }

        public Criteria andIsPushedGreaterThan(Integer value) {
            addCriterion("is_pushed >", value, "isPushed");
            return (Criteria) this;
        }

        public Criteria andIsPushedGreaterThanOrEqualTo(Integer value) {
            addCriterion("is_pushed >=", value, "isPushed");
            return (Criteria) this;
        }

        public Criteria andIsPushedLessThan(Integer value) {
            addCriterion("is_pushed <", value, "isPushed");
            return (Criteria) this;
        }

        public Criteria andIsPushedLessThanOrEqualTo(Integer value) {
            addCriterion("is_pushed <=", value, "isPushed");
            return (Criteria) this;
        }

        public Criteria andIsPushedIn(List<Integer> values) {
            addCriterion("is_pushed in", values, "isPushed");
            return (Criteria) this;
        }

        public Criteria andIsPushedNotIn(List<Integer> values) {
            addCriterion("is_pushed not in", values, "isPushed");
            return (Criteria) this;
        }

        public Criteria andIsPushedBetween(Integer value1, Integer value2) {
            addCriterion("is_pushed between", value1, value2, "isPushed");
            return (Criteria) this;
        }

        public Criteria andIsPushedNotBetween(Integer value1, Integer value2) {
            addCriterion("is_pushed not between", value1, value2, "isPushed");
            return (Criteria) this;
        }

        public Criteria andPushedTimeIsNull() {
            addCriterion("pushed_time is null");
            return (Criteria) this;
        }

        public Criteria andPushedTimeIsNotNull() {
            addCriterion("pushed_time is not null");
            return (Criteria) this;
        }

        public Criteria andPushedTimeEqualTo(Date value) {
            addCriterion("pushed_time =", value, "pushedTime");
            return (Criteria) this;
        }

        public Criteria andPushedTimeNotEqualTo(Date value) {
            addCriterion("pushed_time <>", value, "pushedTime");
            return (Criteria) this;
        }

        public Criteria andPushedTimeGreaterThan(Date value) {
            addCriterion("pushed_time >", value, "pushedTime");
            return (Criteria) this;
        }

        public Criteria andPushedTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("pushed_time >=", value, "pushedTime");
            return (Criteria) this;
        }

        public Criteria andPushedTimeLessThan(Date value) {
            addCriterion("pushed_time <", value, "pushedTime");
            return (Criteria) this;
        }

        public Criteria andPushedTimeLessThanOrEqualTo(Date value) {
            addCriterion("pushed_time <=", value, "pushedTime");
            return (Criteria) this;
        }

        public Criteria andPushedTimeIn(List<Date> values) {
            addCriterion("pushed_time in", values, "pushedTime");
            return (Criteria) this;
        }

        public Criteria andPushedTimeNotIn(List<Date> values) {
            addCriterion("pushed_time not in", values, "pushedTime");
            return (Criteria) this;
        }

        public Criteria andPushedTimeBetween(Date value1, Date value2) {
            addCriterion("pushed_time between", value1, value2, "pushedTime");
            return (Criteria) this;
        }

        public Criteria andPushedTimeNotBetween(Date value1, Date value2) {
            addCriterion("pushed_time not between", value1, value2, "pushedTime");
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