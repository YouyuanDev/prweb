package com.prweb.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.prweb.dao.AccountDao;
import com.prweb.dao.CompanyDao;
import com.prweb.dao.CompanyUserDao;
import com.prweb.dao.OrderDao;
import com.prweb.entity.Account;
import com.prweb.entity.Company;
import com.prweb.entity.Order;
import com.prweb.entity.PersonUser;
import com.prweb.service.CompanyUserService;
import com.prweb.service.PushNotificationService;
import com.prweb.util.APICloudPushService;
import com.prweb.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/CompanyUser")
public class CompanyUserController {

    @Autowired
    private CompanyUserService companyUserService;


    //用于商户用户获取附近待接的订单
    @RequestMapping(value = "getNearByPendingOrders",produces = "text/plain;charset=utf-8")
    @ResponseBody
    public String getNearByPendingOrders(HttpServletRequest request){

        String lon= request.getParameter("lon");
        String lat= request.getParameter("lat");

        System.out.println("center lon="+lon);
        System.out.println("center lat="+lat);

        if(lon==null||lon.equals("")){
            lon="121.480242";//人民广场的坐标
        }
        if(lat==null||lat.equals("")){
            lat="31.238269";
        }

        return companyUserService.getNearByPendingOrders(lon,lat);

    }

    //商户用户取消订单服务
    @RequestMapping(value = "/CompanyUserCancelOrder")
    @ResponseBody
    public String CompanyUserCancelOrder(HttpServletRequest request){
        System.out.print("CompanyUserCancelOrder");
        JSONObject json=new JSONObject();
        //返回用户session数据
        HttpSession session = request.getSession();
        //把用户数据保存在session域对象中
        String username=(String)session.getAttribute("userSession");
        String accountType=(String)session.getAttribute("accountType");
        String basePath = request.getSession().getServletContext().getRealPath("/");
        if(basePath.lastIndexOf('/')==-1){
            basePath=basePath.replace('\\','/');
        }
        String map="";
        try{
            map=companyUserService.CompanyUserCancelOrder(username,accountType,basePath);

        }catch (Exception e){
            e.printStackTrace();
            json.put("success",false);
            json.put("message",e.getMessage());
            map= JSONArray.toJSONString(json);
        }finally {
//            try {
//                ResponseUtil.write(response, json);
//
//
//            }catch  (Exception e) {
//                e.printStackTrace();
//            }
            return map;
        }
        //return null;

    }



    //商户用户完成订单服务
    @RequestMapping(value = "/CompanyUserFinishService")
    @ResponseBody
    public String CompanyUserFinishService(HttpServletRequest request){
        System.out.print("CompanyUserFinishService");
        JSONObject json=new JSONObject();
        //返回用户session数据
        HttpSession session = request.getSession();
        //把用户数据保存在session域对象中
        String username=(String)session.getAttribute("userSession");
        String accountType=(String)session.getAttribute("accountType");
        String basePath = request.getSession().getServletContext().getRealPath("/");
        if(basePath.lastIndexOf('/')==-1){
            basePath=basePath.replace('\\','/');
        }
        String map="";
        try{

            map=companyUserService.CompanyUserFinishService(username,accountType,basePath);

        }catch (Exception e){
            e.printStackTrace();
            json.put("success",false);
            json.put("message",e.getMessage());
            map= JSONArray.toJSONString(json);
        }finally {
//            try {
//                ResponseUtil.write(response, json);
//            }catch  (Exception e) {
//                e.printStackTrace();
//            }
            return map;
        }

    }




    //商户用户开始服务订单
    @RequestMapping(value = "/CompanyUserStartService")
    @ResponseBody
    public String CompanyUserStartService(HttpServletRequest request, HttpServletResponse response){
        System.out.print("CompanyUserStartService");
        JSONObject json=new JSONObject();
        //返回用户session数据
        HttpSession session = request.getSession();
        //把用户数据保存在session域对象中
        String username=(String)session.getAttribute("userSession");
        String accountType=(String)session.getAttribute("accountType");
        String basePath = request.getSession().getServletContext().getRealPath("/");
        if(basePath.lastIndexOf('/')==-1){
            basePath=basePath.replace('\\','/');
        }
        String map="";
        try{
            map=companyUserService.CompanyUserStartService(username,accountType,basePath);

        }catch (Exception e){
            e.printStackTrace();
            json.put("success",false);
            json.put("message",e.getMessage());
            map= JSONArray.toJSONString(json);
        }finally {
//            try {
//                ResponseUtil.write(response, json);
//            }catch  (Exception e) {
//                e.printStackTrace();
//            }
            return map;
        }


    }


    //商户用户接订单
    @RequestMapping(value = "/CompanyUserAcceptPendingOrder")
    @ResponseBody
    public String CompanyUserAcceptPendingOrder(HttpServletRequest request){
        System.out.print("CompanyUserAcceptPendingOrder");
        JSONObject json=new JSONObject();
        //返回用户session数据
        HttpSession session = request.getSession();
        String order_no = (String) request.getParameter("order_no");

        //把用户数据保存在session域对象中
        String username=(String)session.getAttribute("userSession");
        String accountType=(String)session.getAttribute("accountType");
        String basePath = request.getSession().getServletContext().getRealPath("/");
        if(basePath.lastIndexOf('/')==-1){
            basePath=basePath.replace('\\','/');
        }
        String map="";
        try{
            map=companyUserService.CompanyUserAcceptPendingOrder(username,accountType,order_no,basePath);
        }catch (Exception e){
            e.printStackTrace();
            json.put("success",false);
            json.put("message",e.getMessage());
            map= JSONArray.toJSONString(json);
        }finally {
//            try {
//                ResponseUtil.write(response, json);
//
//
//            }catch  (Exception e) {
//                e.printStackTrace();
//            }
            return map;
        }

    }





    //获取CompanyUserInfo
    @RequestMapping(value = "getCompanyUserInfo",produces = "text/plain;charset=utf-8")
    @ResponseBody
    public String getCompanyUserInfo( HttpServletRequest request){
        System.out.print("getCompanyUserInfo");
        JSONObject json = new JSONObject();
        //返回用户session数据
        HttpSession session = request.getSession();
        //把用户数据保存在session域对象中
        String username = (String) session.getAttribute("userSession");
        return companyUserService.getCompanyUserInfo(username);
    }


    //保存CompanyUser
    @RequestMapping(value = "/saveCompanyUserInfo")
    @ResponseBody
    public String saveCompanyUserInfo(Company company){
        System.out.print("saveCompanyUserInfo");

        JSONObject json=new JSONObject();
        String map="";
        try{
            map=companyUserService.saveCompanyUserInfo(company);

        }catch (Exception e){
            e.printStackTrace();
            json.put("success",false);
            json.put("message",e.getMessage());
            map= JSONArray.toJSONString(json);
        }finally {
//            try {
//                ResponseUtil.write(response, json);
//            }catch  (Exception e) {
//                e.printStackTrace();
//            }
            return map;
        }
    }



    //撤销认证CompanyUser
    @RequestMapping(value = "/cancelVerifyCompanyUserInfo")
    @ResponseBody
    public String cancelVerifyCompanyUserInfo(HttpServletRequest request){
        System.out.print("cancelVerifyCompanyUserInfo");

        JSONObject json=new JSONObject();
        //返回用户session数据
        HttpSession session = request.getSession();
        //把用户数据保存在session域对象中
        String username = (String) session.getAttribute("userSession");
        String accountType = (String) session.getAttribute("accountType");

        return companyUserService.cancelVerifyCompanyUserInfo(username,accountType);
    }



    //认证CompanyUser
    @RequestMapping(value = "/verifyCompanyUserInfo")
    @ResponseBody
    public String verifyCompanyUserInfo(HttpServletRequest request){
        System.out.print("verifyCompanyUserInfo");
        String id_card_picture_front= request.getParameter("id_card_picture_front");
        String id_card_picture_back= request.getParameter("id_card_picture_back");
        String business_certificate_picture= request.getParameter("business_certificate_picture");

        JSONObject json=new JSONObject();
        //返回用户session数据
        HttpSession session = request.getSession();
        //把用户数据保存在session域对象中
        String username = (String) session.getAttribute("userSession");
        String accountType = (String) session.getAttribute("accountType");

        return companyUserService.verifyCompanyUserInfo(username,accountType,id_card_picture_front,id_card_picture_back,business_certificate_picture);

    }

}
