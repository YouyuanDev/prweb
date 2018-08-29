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


    public List<HashMap<String,Object>> getCommentByOrderNo(@Param("order_no")String order_no);

    //根据company_no搜索总数
    public int getCountAllByCompanyNo(@Param("company_no")String company_no);

    public List<HashMap<String,Object>> getCommentByCompanyNo(@Param("company_no")String company_no,@Param("skip")int skip, @Param("take")int take);

    public List<HashMap<String,Object>> getAvgRatingByCompanyNo(@Param("company_no")String company_no);
}
