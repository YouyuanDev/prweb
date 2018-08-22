package com.prweb.service;

import com.prweb.entity.Comment;

public interface CommentService {

    //分页搜索
    public String getCommentByLike(String comment_from_person_user_no,String comment_to_comany_no,int start,int rows);

    //保存Comment
    public String saveComment(Comment comment);

    //删除Comment信息
    public String delComment(String hlparam);
}
