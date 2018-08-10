package com.prweb.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.prweb.dao.FailureTypeDao;
import com.prweb.entity.FailureType;
import com.prweb.service.FailureTypeService;
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
@RequestMapping("/FailureType")
public class FailureTypeController {

    @Autowired
    private FailureTypeService failureTypeService;

    //得到所有的FailureType
    @RequestMapping(value ="/getAllFailureType",produces = "text/plain;charset=utf-8")
    @ResponseBody
    public String getAllFailureType(HttpServletRequest request){
        return failureTypeService.getAllFailureType();
    }


    //获取所有FailureType列表
    @RequestMapping("getFailureTypeByLike")
    @ResponseBody
    public String getFailureTypeByLike(@RequestParam(value = "failure_type_name",required = false)String failure_type_name, HttpServletRequest request){
        String page= request.getParameter("page");
        String rows= request.getParameter("rows");
        if(page==null){
            page="1";
        }
        if(rows==null){
            rows="20";
        }
        int start=(Integer.parseInt(page)-1)*Integer.parseInt(rows);
        return failureTypeService.getFailureTypeByLike(failure_type_name,start,Integer.parseInt(rows));

    }

    //保存FailureType
    @RequestMapping(value = "/saveFailureType")
    @ResponseBody
    public String saveFailureType(FailureType failureType, HttpServletResponse response){
        System.out.print("saveFailureType");

        JSONObject json=new JSONObject();
        String mmp="";
        try{
            mmp=failureTypeService.saveFailureType(failureType);
        }catch (Exception e){
            e.printStackTrace();
            json.put("success",false);
            json.put("message",e.getMessage());
            mmp = JSONArray.toJSONString(json);
        }finally {
//            try {
//                ResponseUtil.write(response, json);
//            }catch  (Exception e) {
//                e.printStackTrace();
//            }
            return mmp;
        }

    }


    //删除failureType信息
    @RequestMapping("/delFailureType")
    public String delFailureType(@RequestParam(value = "hlparam")String hlparam,HttpServletResponse response)throws Exception{
        JSONObject json=new JSONObject();
        String mmp="";
        try{
            mmp=failureTypeService.delFailureType(hlparam);
        }catch (Exception e){
            e.printStackTrace();
            json.put("success",false);
            json.put("message",e.getMessage());
            mmp = JSONArray.toJSONString(json);
        }finally {
            return mmp;
        }
    }

}