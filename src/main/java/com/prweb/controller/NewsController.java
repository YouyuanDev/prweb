package com.prweb.controller;

import com.alibaba.fastjson.JSONObject;
import com.prweb.entity.News;
import com.prweb.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
@Controller
@RequestMapping("/News")
public class NewsController {
    @Autowired
    private NewsService newsService;

    //模糊查询news列表
    @RequestMapping("getNewsByLike")
    @ResponseBody
    public String getNewsByLike(@RequestParam(value = "username",required = false)String username, @RequestParam(value = "title",required = false)String title, HttpServletRequest request){
        String page= request.getParameter("page");
        String rows= request.getParameter("rows");
        if(page==null){
            page="1";
        }
        if(rows==null){
            rows="20";
        }
        int start=(Integer.parseInt(page)-1)*Integer.parseInt(rows);
        return newsService.getAllByLike(username,title,start,Integer.parseInt(rows));
    }
    //保存News
    @RequestMapping(value = "/saveNews")
    @ResponseBody
    public String saveNews(HttpServletRequest request,HttpServletResponse response){
        JSONObject json=new JSONObject();
        String mmp="";
        try{
            HttpSession session = request.getSession();
            String username = (String) session.getAttribute("userSession");
            String idStr=request.getParameter("id");
            String title=request.getParameter("title");
            String content=request.getParameter("content");
            String publish_time=request.getParameter("publish_time");
            mmp=newsService.saveNews(idStr,username,title,content,publish_time);
        }catch (Exception e){
            e.printStackTrace();
            json.put("success",false);
            json.put("message",e.getMessage());
            mmp= JSONObject.toJSONString(json);
        }finally {
            return mmp;
        }
    }
    //删除News
    @RequestMapping("/delNews")
    @ResponseBody
    public String delNews(@RequestParam(value = "hlparam")String hlparam,HttpServletResponse response)throws Exception{
        JSONObject json=new JSONObject();
        String mmp="";
        try{
            mmp=newsService.delNews(hlparam);
        }catch (Exception e){
            e.printStackTrace();
            json.put("success",false);
            json.put("message",e.getMessage());
            mmp= JSONObject.toJSONString(json);
        }finally {
            return mmp;
        }
    }
    //根据id查询news
    @RequestMapping("/getNewsById")
    @ResponseBody
    public String getNewsById(HttpServletRequest request,HttpServletResponse response)throws Exception{
        JSONObject json=new JSONObject();
        String mmp="";
        try{
            String idStr=request.getParameter("id");
            int id=-1;
            if(idStr!=null&&!"".equals(idStr)){
                id=Integer.parseInt(idStr);
                mmp=newsService.getNewsById(id);
            }else{
                json.put("success",false);
                json.put("message",mmp);
            }
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
