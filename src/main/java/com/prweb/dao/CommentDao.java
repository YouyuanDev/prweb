package com.prweb.dao;


import com.prweb.entity.Comment;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

public interface CommentDao {
    //模糊搜索带分页
    public List<HashMap<String,Object>> getAllByLike(@Param("comment_from_person_user_no")String comment_from_person_user_no, @Param("comment_to_comany_no")String comment_to_comany_no, @Param("skip")int skip, @Param("take")int take);

    //模糊搜索总数
    public int getCountAllByLike(@Param("comment_from_person_user_no")String comment_from_person_user_no,@Param("comment_to_comany_no")String comment_to_comany_no);

    //修改 Comment
    public int updateComment(Comment comment);
    //增加 Comment
    public int addComment(Comment comment);
    //删除 Comment
    public int delComment(String[]arrId);


    public List<HashMap<String,Object>> getCommentByOrderNo(String order_no);


    public List<HashMap<String,Object>> getCommentByCompanyNo(String company_no);
}
