package com.prweb.service;

import com.prweb.entity.Comment;

import java.util.HashMap;
import java.util.List;

public interface CommentService {

    //分页搜索
    public String getCommentByLike(String comment_from_person_user_no,String comment_to_comany_no,int start,int rows);

    //保存Comment
    public String saveComment(Comment comment);

    //删除Comment信息
    public String delComment(String hlparam);

    //获取某个订单的评论
    public String getCommentByOrderNo(String order_no);

    //获取某个商户的评论
    public String getCommentByCompanyNo(String company_no);
}
