package com.prweb.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.prweb.dao.FunctionDao;
import com.prweb.dao.RoleDao;
import com.prweb.entity.Function;
import com.prweb.entity.Role;
import com.prweb.service.FunctionService;
import com.prweb.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/Function")
public class FunctionController {



    @Autowired
    private FunctionService functionService;
    //搜索
    @RequestMapping(value = "getFunctionByLike",produces = "text/plain;charset=utf-8")
    @ResponseBody
    public String getFunctionByLike(@RequestParam(value = "function_no",required = false)String function_no, @RequestParam(value = "function_name",required = false)String function_name, HttpServletRequest request){
        String page= request.getParameter("page");
        String rows= request.getParameter("rows");
        if(page==null){
            page="1";
        }
        if(rows==null){
            rows="20";
        }
        int start=(Integer.parseInt(page)-1)*Integer.parseInt(rows);
        return functionService.getFunctionByLike(function_no,function_name,start,Integer.parseInt(rows));

    }


    @RequestMapping(value = "getFunctionByNoName",produces = "text/plain;charset=utf-8")
    @ResponseBody
    public String getFunctionByNoName(@RequestParam(value = "function_no",required = false)String function_no, @RequestParam(value = "function_name",required = false)String function_name, HttpServletRequest request){
        return functionService.getFunctionByNoName(function_no,function_name);
    }

    //保存function
    @RequestMapping(value = "/saveFunction")
    @ResponseBody
    public String saveFunction(Function function, HttpServletResponse response){
        System.out.print("saveFunction");
        String mmp="";
        try{
            mmp=functionService.saveFunction(function);
        }catch (Exception e){
            e.printStackTrace();
            JSONObject json=new JSONObject();
            json.put("success",false);
            json.put("message",e.getMessage());
            mmp= JSONArray.toJSONString(json);
        }finally {
            return mmp;
        }
    }


    //删除Function信息
    @RequestMapping("/delFunction")
    @ResponseBody
    public String delFunction(@RequestParam(value = "hlparam")String hlparam,HttpServletResponse response)throws Exception{
        String mmp="";
        try{
            mmp=functionService.delFunction(hlparam);
        }catch (Exception e){
            e.printStackTrace();
            JSONObject json=new JSONObject();
            json.put("success",false);
            json.put("message",e.getMessage());
            mmp= JSONArray.toJSONString(json);
        }finally {
            return mmp;
        }
    }

}
