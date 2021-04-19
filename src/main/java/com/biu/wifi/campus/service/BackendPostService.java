package com.biu.wifi.campus.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biu.wifi.campus.dao.PostCommentMapper;
import com.biu.wifi.campus.dao.PostMapper;
import com.biu.wifi.campus.dao.PostReplyMapper;
import com.biu.wifi.campus.dao.PostSelectMapper;
import com.biu.wifi.campus.dao.model.Post;
import com.biu.wifi.campus.dao.model.PostComment;
import com.biu.wifi.campus.dao.model.PostCriteria;
import com.biu.wifi.campus.dao.model.PostCriteria.Criteria;
import com.biu.wifi.campus.dao.model.PostReply;
import com.biu.wifi.campus.dao.model.PostSelect;
import com.biu.wifi.campus.dao.model.PostSelectCriteria;
import com.biu.wifi.campus.daoEx.BackendPostMapperEx;
import com.biu.wifi.core.support.orm.mybatis3.util.IbatisServiceUtils;

@Service
public class BackendPostService {

    @Autowired
    private BackendPostMapperEx postMapperEx;

    @Autowired
    private PostCommentMapper postCommentMapper;

    @Autowired
    private PostReplyMapper postReplyMapper;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private PostSelectMapper postSelectMapper;

    public List<Map<String, Object>> findPostList(Integer schoolId, Long time, String title, String content, String studentNumber, String startTime, String endTime, Short isSelect, Short isSpecial) {
        return postMapperEx.findPostList(schoolId, time, title, content, studentNumber, startTime, endTime, isSelect, isSpecial);
    }

    public List<Map<String, Object>> findPostComments(Integer postId, Long time) {
        return postMapperEx.findPostComments(postId, time);
    }

    public List<Map<String, Object>> findPostReplys(Integer commentId) {
        return postMapperEx.findPostReplys(commentId);
    }

    public void deletePostComment(PostComment entity) {
        postCommentMapper.updateByPrimaryKeySelective(entity);
    }

    public void deletePostCommentReply(PostReply entity) {
        postReplyMapper.updateByPrimaryKeySelective(entity);
    }

    public void updatePost(Post entity) {
        postMapper.updateByPrimaryKeySelective(entity);
    }

    public Post getPost(Post post) {
        return IbatisServiceUtils.get(post, postMapper, true);
    }

    public void cancelNoticeTopPost(Post entity) {
        postMapperEx.cancelNoticeTopPost(entity);
    }

    public void cancelSelectPost(Post entity) {
        postMapperEx.cancelSelectPost(entity);
    }

    public void deletePost(Post entity) {
        postMapper.updateByPrimaryKeySelective(entity);
    }

    public int getNoticeTopPostCount(Integer schoolId, List<Short> postTypes) {
        PostCriteria postCriteria = new PostCriteria();
        Criteria criteria = postCriteria.createCriteria();
        criteria.andIsDeleteEqualTo((short) 2);
        criteria.andSchoolIdEqualTo(schoolId);
        criteria.andPostTypeIn(postTypes);
        return postMapper.countByExample(postCriteria);
    }

    public List<Map<String, Object>> findPostSelectListInfo(Integer schoolId) {
        return postMapperEx.findPostSelectListInfo(schoolId);
    }

    public List<PostSelect> findPostSelectList(Integer schoolId) {
        PostSelectCriteria postSelectCriteria = new PostSelectCriteria();
        PostSelectCriteria.Criteria criteria = postSelectCriteria.createCriteria();
        criteria.andSchoolIdEqualTo(schoolId);
        criteria.andIsDeleteEqualTo((short) 2);
        return postSelectMapper.selectByExample(postSelectCriteria);
    }

    public int getPostSelectTypeCount(Integer schoolId) {
        PostSelectCriteria postSelectCriteria = new PostSelectCriteria();
        PostSelectCriteria.Criteria criteria = postSelectCriteria.createCriteria();
        criteria.andSchoolIdEqualTo(schoolId);
        criteria.andIsDeleteEqualTo((short) 2);
        return (new Long(postSelectMapper.countByExample(postSelectCriteria))).intValue();
    }

    public void addPostSelectType(PostSelect entity) {
        postSelectMapper.insertSelective(entity);
    }

    public void updatePostSelectType(PostSelect entity) {
        postSelectMapper.updateByPrimaryKeySelective(entity);
    }

    public boolean isExistPostInSelectType(Integer selectTypeId) {
        PostCriteria postCriteria = new PostCriteria();
        Criteria criteria = postCriteria.createCriteria();
        criteria.andIsDeleteEqualTo((short) 2);
        criteria.andIsSelectEqualTo((short) 1);
        criteria.andSelectTypeIdEqualTo(selectTypeId);

        if (postMapper.countByExample(postCriteria) == 0) {
            return false;
        } else {
            return true;
        }
    }
}
