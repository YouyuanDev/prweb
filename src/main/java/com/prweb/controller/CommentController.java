package com.prweb.controller;

import com.alibaba.fastjson.JSONObject;
import com.prweb.entity.Business;
import com.prweb.entity.Comment;
import com.prweb.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/Comment")
public class CommentController {

    @Autowired
    private CommentService commentService;


    //搜索
    @RequestMapping(value = "getCommentByLike",produces = "text/plain;charset=utf-8")
    @ResponseBody
    public String getCommentByLike(@RequestParam(value = "comment_from_person_user_no",required = false)String comment_from_person_user_no, @RequestParam(value = "comment_to_comany_no",required = false)String comment_to_comany_no,  HttpServletRequest request){
        String page= request.getParameter("page");
        String rows= request.getParameter("rows");
        if(page==null){
            page="1";
        }
        if(rows==null){
            rows="20";
        }
        int start=(Integer.parseInt(page)-1)*Integer.parseInt(rows);

        return commentService.getCommentByLike(comment_from_person_user_no,comment_to_comany_no,start,Integer.parseInt(rows));

    }

    //保存 Comment
    @RequestMapping(value = "/saveComment")
    @ResponseBody
    public String saveComment(Comment comment, HttpServletResponse response)throws Exception{
        System.out.print("saveComment");
        JSONObject json=new JSONObject();
        String mmp="";
        try{
            mmp=commentService.saveComment(comment);
        }catch (Exception e){
            e.printStackTrace();
            json.put("success",false);
            json.put("message",e.getMessage());
            mmp= JSONObject.toJSONString(json);
        }finally {
            return mmp;
        }
    }

    //删除Comment信息
    @RequestMapping("/delComment")
    @ResponseBody
    public String delComment(@RequestParam(value = "hlparam")String hlparam,HttpServletResponse response)throws Exception{
        JSONObject json=new JSONObject();
        String mmp="";
        try{
            mmp=commentService.delComment(hlparam);
        }catch (Exception e){
            e.printStackTrace();
            json.put("success",false);
            json.put("message",e.getMessage());
            mmp= JSONObject.toJSONString(json);
        }finally {
            return mmp;
        }
    }
}
