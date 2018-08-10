package com.prweb.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.prweb.dao.BusinessDao;


import com.prweb.entity.Business;
import com.prweb.service.BusinessService;
import com.prweb.util.ComboxItem;
import com.prweb.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
@RequestMapping("/Business")
public class BusinessController {


    @Autowired
    private BusinessService businessService;


    //搜索
    @RequestMapping(value = "getBusinessByLike",produces = "text/plain;charset=utf-8")
    @ResponseBody
    public String getBusinessByLike(@RequestParam(value = "business_no",required = false)String business_no, @RequestParam(value = "business_name",required = false)String business_name, @RequestParam(value = "business_type",required = false)String business_type,  HttpServletRequest request){
        String page= request.getParameter("page");
        String rows= request.getParameter("rows");
        if(page==null){
            page="1";
        }
        if(rows==null){
            rows="20";
        }
        int start=(Integer.parseInt(page)-1)*Integer.parseInt(rows);

        return businessService.getBusinessByLike(business_no,business_name,business_type,start,Integer.parseInt(rows));

    }


    @RequestMapping(value = "getBusinessByBusinessNo",produces = "text/plain;charset=utf-8")
    @ResponseBody
    public String getBusinessByBusinessNo(@RequestParam(value = "business_no",required = false)String business_no, HttpServletRequest request){
        return businessService.getBusinessByBusinessNo(business_no);
    }
    //获取所有business用于下拉框
    @RequestMapping("/getAllBusiness")
    @ResponseBody
    public String getAllBusiness(HttpServletRequest request){
        return businessService.getAllBusiness();
    }



    //保存Business
    @RequestMapping(value = "/saveBusiness")
    @ResponseBody
    public String saveBusiness(Business business, HttpServletResponse response)throws Exception{
        System.out.print("saveBusiness");
        JSONObject json=new JSONObject();
        String mmp="";
        try{
            mmp=businessService.saveBusiness(business);
        }catch (Exception e){
            e.printStackTrace();
            json.put("success",false);
            json.put("message",e.getMessage());
            mmp= JSONObject.toJSONString(json);
        }finally {
//            try {
//                ResponseUtil.write(response, json);
//            }catch  (Exception e) {
//                e.printStackTrace();
//            }
            return mmp;
        }
    }


    //删除Business信息
    @RequestMapping("/delBusiness")
    public String delBusiness(@RequestParam(value = "hlparam")String hlparam,HttpServletResponse response)throws Exception{
        JSONObject json=new JSONObject();
        String mmp="";
        try{
            mmp=businessService.delBusiness(hlparam);
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
