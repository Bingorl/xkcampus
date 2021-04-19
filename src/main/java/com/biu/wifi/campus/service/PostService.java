package com.biu.wifi.campus.service;

import com.biu.wifi.campus.dao.PostMapper;
import com.biu.wifi.campus.dao.model.Post;
import com.biu.wifi.campus.dao.model.PostCriteria;
import com.biu.wifi.campus.daoEx.PostMapperEx;
import com.biu.wifi.core.support.orm.mybatis3.util.IbatisServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PostService {

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private PostMapperEx postMapperEx;

    public int addPost(Post entity) {
        try {
            IbatisServiceUtils.insert(entity, postMapper);
            return entity.getId();
        } catch (Exception e) {

            e.printStackTrace();
            return 0;
        }
    }

    public Post getPost(Post entity) {
        return IbatisServiceUtils.get(entity, postMapper, true);
    }

    public Post getPostAll(Post entity) {
        return IbatisServiceUtils.get(entity, postMapper, true);
    }

    public int updatePost(Post entity) {
        return IbatisServiceUtils.updateByPk(entity, postMapper);
    }

    public List<Post> findPostList(Post entity) {
        return IbatisServiceUtils.find(entity, postMapper);
    }

    //首页搜索,帖子搜索,通过帖子标题,名称或者楼搜索
    public List<Map<String, Object>> findPostListByName(String search_word, String time) {
        return postMapperEx.findPostListByName(search_word, time);
    }

    //首页搜索,新鲜事搜索,通过新鲜事内容搜索
    public List<Map<String, Object>> findSayingListByName(String search_word, String time, Integer user_id) {
        return postMapperEx.findSayingListByName(search_word, time, user_id);
    }

    //首页搜索,群组搜索,通过群名称或群号搜索
    public List<Map<String, Object>> findGroupListByname(String search_word, String time) {
        return postMapperEx.findGroupListByname(search_word, time);
    }

    //论坛首页上面的置顶公告贴列表
    public List<Map<String, Object>> findTopPostList(Integer school_id, String time) {
        return postMapperEx.findTopPostList(school_id, time);
    }

    //论坛帖子列表
    public List<Map<String, Object>> findLuntanPostList(Integer school_id, Integer type, Integer search_type, Integer essence_type, String time) {
        return postMapperEx.findPostList(type, school_id, search_type, essence_type, time);
    }

    //帖子楼层列表
    public List<Map<String, Object>> findPostFloorList(Integer type, Integer post_id, Integer search_type, String time, Integer user_id) {
        return postMapperEx.findPostFloorList(type, post_id, search_type, time, user_id);
    }

    //帖子楼层回复列表
    public List<Map<String, Object>> findPostReplyList(Integer id, String time) {
        return postMapperEx.findPostReplyList(id, time);
    }

    public List<Post> findList(PostCriteria example) {
        return postMapper.selectByExample(example);
    }
}
