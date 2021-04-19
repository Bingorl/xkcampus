package com.biu.wifi.campus.controller.admin;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.biu.wifi.campus.Tool.TimeUtil;
import com.biu.wifi.campus.dao.model.Post;
import com.biu.wifi.campus.dao.model.PostComment;
import com.biu.wifi.campus.dao.model.PostReply;
import com.biu.wifi.campus.dao.model.PostSelect;
import com.biu.wifi.campus.dao.model.User;
import com.biu.wifi.campus.result.Result;
import com.biu.wifi.campus.service.BackendPostService;
import com.biu.wifi.campus.service.BackendUserService;
import com.biu.wifi.campus.service.PostCommentService;
import com.biu.wifi.core.support.pageLimit.PageLimitHolderFilter;
import com.biu.wifi.core.util.ServletUtilsEx;

@Controller
public class BackendPostController {

    @Autowired
    private BackendPostService postService;

    @Autowired
    private PostCommentService postCommentService;

    @Autowired
    private BackendUserService userService;

    @RequestMapping("/back_api_findPostList_s")
    public void back_api_findPostList_s(Integer page, Integer pageSize, Integer schoolId, Long time, String title, String content, String studentNumber, String startTime, String endTime, Short isSelect, Short isSpecial, HttpServletResponse response) {
        if (page != null && pageSize != null && schoolId != null && time != null && isSelect != null && isSpecial != null) {
            PageLimitHolderFilter.setContext(page, pageSize, null);

            if (page == 1) {
                time = TimeUtil.getNowTime();
            }

            List<Map<String, Object>> list = postService.findPostList(schoolId, time, title, content, studentNumber, startTime, endTime, isSelect, isSpecial);

            Map<String, Object> map = new HashMap<>();
            map.put("list", list);
            map.put("totalCount", PageLimitHolderFilter.getContext().getTotalCount());
            map.put("time", time);

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", map));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_findPostComment_s")
    public void back_api_findPostComment_s(Integer page, Integer pageSize, Integer postId, Long time, HttpServletResponse response) {
        if (page != null && pageSize != null && postId != null && time != null) {
            PageLimitHolderFilter.setContext(page, pageSize, null);

            if (page == 1) {
                time = TimeUtil.getNowTime();
            }

            List<Map<String, Object>> list = postService.findPostComments(postId, time);

            for (Map<String, Object> map : list) {
                List<Map<String, Object>> replyList = postService.findPostReplys(Integer.parseInt(map.get("id").toString()));
                map.put("replyList", replyList);
            }

            Map<String, Object> map = new HashMap<>();
            map.put("list", list);
            map.put("totalCount", PageLimitHolderFilter.getContext().getTotalCount());
            map.put("time", time);
            map.put("postId", postId);

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", map));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_findPostReply_s")
    public void back_api_findPostReply_s(Integer commentId, HttpServletResponse response) {
        if (commentId != null) {
            List<Map<String, Object>> list = postService.findPostReplys(commentId);

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", list));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_deletePostComment_s")
    public void back_api_deletePostComment_s(Integer commentId, HttpServletResponse response) {
        if (commentId != null) {
            PostComment postComment = new PostComment();
            postComment.setId(commentId);

            PostComment getPostComment = postCommentService.getPostComment(postComment);

            if (getPostComment != null) {
                Post post = new Post();
                post.setId(getPostComment.getPostId());
                post = postService.getPost(post);
                post.setCommentNumber(post.getCommentNumber() - 1);
                postService.updatePost(post);
            }

            postComment.setIsDelete((short) 1);
            postComment.setDeleteTime(new Date());
            postService.deletePostComment(postComment);

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", null));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_deletePostReply_s")
    public void back_api_deletePostReply_s(Integer replyId, HttpServletResponse response) {
        if (replyId != null) {
            PostReply postReply = new PostReply();
            postReply.setId(replyId);
            postReply.setIsDelete((short) 1);
            postReply.setDeleteTime(new Date());
            postService.deletePostCommentReply(postReply);

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", null));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_changePostProperty_s")
    public void back_api_changePostProperty_s(Integer schoolId, Integer postId, Short postType, Short operateType, Integer selectTypeId, Integer weight, HttpServletResponse response) {

        if (postId != null && postType != null && operateType != null) {

            Post post = new Post();
            post.setId(postId);
            post = postService.getPost(post);

            if (postType == 2 || postType == 3) {
                if (operateType == 1 && weight != null) {

                    if (post.getPostType() == null || post.getPostType() == 1) {

                        //判断是否超过限制
                        List<Short> postTypes = new ArrayList<Short>();
                        postTypes.add((short) 2);
                        postTypes.add((short) 3);
                        int cnt = postService.getNoticeTopPostCount(schoolId, postTypes);

                        if (cnt >= 6) {
                            ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "限制个数为6", null));
                            return;
                        }
                    }

                    post.setPostType(postType);
                    post.setWeight(weight);
                    post.setNoticeTopTime(new Date());
                    postService.updatePost(post);
                }

                //cancel
                if (operateType == 2) {
                    post.setPostType((short) 1);
                    post.setWeight(null);
                    post.setNoticeTopTime(null);
                    postService.cancelNoticeTopPost(post);
                }
            } else if (postType == 1) {
                if (operateType == 1 && selectTypeId != null) {
                    post.setIsSelect((short) 1);
                    post.setSelectTypeId(selectTypeId);
                    post.setSelectTime(new Date());
                    postService.updatePost(post);
                }

                if (operateType == 2) {
                    post.setIsSelect((short) 2);
                    post.setSelectTypeId(null);
                    post.setSelectTime(null);
                    postService.cancelSelectPost(post);
                }
            }

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", null));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_deletePost_s")
    public void back_api_deletePost_s(Integer postId, HttpServletResponse response) {
        if (postId != null) {

            Post post = new Post();
            post.setId(postId);

            Post getPost = postService.getPost(post);

            if (getPost != null) {
                User user = new User();
                user.setId(getPost.getCreatorId());
                User getUser = userService.getUser(user);
                user.setPostNum(getUser.getPostNum() - 1);
                userService.updateUser(user);
            }

            post.setIsDelete((short) 1);
            post.setDeleteTime(new Date());
            postService.deletePost(post);

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", null));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_findSelectTypeList_s")
    public void back_api_findSelectTypeList_s(Integer schoolId, Short isSelectCount, HttpServletResponse response) {
        if (schoolId != null && isSelectCount != null) {

            if (isSelectCount == 1) {
                List<Map<String, Object>> list = postService.findPostSelectListInfo(schoolId);
                ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", list));
            } else if (isSelectCount == 2) {
                List<PostSelect> ls = postService.findPostSelectList(schoolId);
                ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", ls));
            } else {
                ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "参数值不正确", null));
            }
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_addSelectType_s")
    public void back_api_addSelectType_s(String name, Integer schoolId, Integer weight, HttpServletResponse response) {
        if (schoolId != null && StringUtils.isNotBlank(name) && weight != null) {

            if (postService.getPostSelectTypeCount(schoolId) >= 10) {
                ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "限制个数为10", null));
                return;
            }

            PostSelect postSelect = new PostSelect();
            postSelect.setCreateTime(new Date());
            postSelect.setIsDelete((short) 2);
            postSelect.setName(name);
            postSelect.setSchoolId(schoolId);
            postSelect.setWeight(weight);
            postService.addPostSelectType(postSelect);

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", null));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_updateSelectType_s")
    public void back_api_updateSelectType_s(Integer selectTypeId, String name, Integer weight, HttpServletResponse response) {
        if (selectTypeId != null && StringUtils.isNotBlank(name) && weight != null) {

            PostSelect postSelect = new PostSelect();
            postSelect.setId(selectTypeId);
            postSelect.setName(name);
            postSelect.setWeight(weight);
            postService.updatePostSelectType(postSelect);

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", null));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_deleteSelectType_s")
    public void back_api_deleteSelectType_s(Integer selectTypeId, HttpServletResponse response) {
        if (selectTypeId != null) {

            if (postService.isExistPostInSelectType(selectTypeId)) {
                ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "分类下存在帖子，不能删除分类", null));
                return;
            }

            PostSelect postSelect = new PostSelect();
            postSelect.setId(selectTypeId);
            postSelect.setIsDelete((short) 1);
            postSelect.setDeleteTime(new Date());
            postService.updatePostSelectType(postSelect);

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", null));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_getPostById")
    public void back_api_getPostById(Integer id, HttpServletResponse response) {
        if (id != null) {
            Post post = new Post();
            post.setId(id);
            post.setIsDelete((short) 2);
            post = postService.getPost(post);

            if (post != null) {
                Map<String, Object> map = new HashMap<>();
                map.put("titile", post.getTitle());
                map.put("content", post.getContent());
                map.put("img", post.getImg());
                ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", map));
            } else {
                ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "该帖子不存在或已被删除", null));
            }
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }
}
