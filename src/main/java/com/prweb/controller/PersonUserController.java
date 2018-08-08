package com.prweb.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.prweb.dao.*;
import com.prweb.entity.*;
import com.prweb.service.PersonUserService;
import com.prweb.service.PersonUserServiceImpl;
import com.prweb.service.PushNotificationService;
import com.prweb.util.APICloudPushService;
import com.prweb.util.AliPayService;
import com.prweb.util.ComboxItem;
import com.prweb.util.ResponseUtil;
import org.apache.ibatis.jdbc.RuntimeSqlException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;



@Controller
@RequestMapping("/PersonUser")
public class PersonUserController {


    @Autowired
    private PersonUserService personUserService;




    //用于获取个人用户附近商户
    @RequestMapping(value = "getNearByCompany",produces = "text/plain;charset=utf-8")
    @ResponseBody
    public String getNearByCompany(HttpServletRequest request){

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

        String map=personUserService.getNearByCompany(lon,lat);

        return map;

    }

    //个人用户订单取消
    @RequestMapping(value = "/PersonUserCancelOrder")
    @ResponseBody
    public String PersonUserCancelOrder(HttpServletRequest request, HttpServletResponse response){
        System.out.print("PersonUserCancelOrder");
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
            map=personUserService.PersonUserCancelOrder(username,accountType,basePath);


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


    //个人用户订单确认完成
//    @RequestMapping(value = "/PersonUserConfirmFinishedOrder")
//    @ResponseBody
//    public String PersonUserConfirmFinishedOrder(HttpServletRequest request, HttpServletResponse response){
//        System.out.print("PersonUserConfirmFinishedOrder");
//        JSONObject json=new JSONObject();
//        //返回用户session数据
//        HttpSession session = request.getSession();
//        //把用户数据保存在session域对象中
//        String username=(String)session.getAttribute("userSession");
//        String accountType=(String)session.getAttribute("accountType");
//        int resTotal=0;
//        Order order=null;
//        String map="";
//        try{
//            map=personUserService.PersonUserSubmitPendingOrder(username,accountType,order,basePath);
//            if(accountType==null||accountType.equals("")){
//                json.put("success",false);
//                json.put("relogin",true);
//                json.put("message","不存在session，重新登录");
//            }
//            else{
//
//                if(accountType.equals("person_user")){
//
//                    //先判断person_user下是否有未完成的order
//                    order = orderDao.getCurrentPersonUserOrderByUsername(username);
//                    if(order!=null&&order.getOrder_status().equals("finished")){
//                        //设置order状态
//                        order.setOrder_status("finishedconfirmed");
//                        resTotal=orderDao.updateOrder(order);
//                    }else{
//                        json.put("success",false);
//                        json.put("message","不存在可确认完成的订单");
//                    }
//
//                    if(resTotal>0){
//                        json.put("success",true);
//                        json.put("OrderNo",order.getOrder_no());
//                        json.put("message","订单确认完成成功");
//
//
//                    }else{
//                        json.put("success",false);
//                        json.put("message","订单确认完成失败");
//                    }
//                }else{
//                    json.put("success",false);
//                    json.put("message","订单确认完成失败,accountType为"+accountType);
//                }
//
//            }
//
//        }catch (Exception e){
//            e.printStackTrace();
//            json.put("success",false);
//            json.put("message",e.getMessage());
//
//        }finally {
//            try {
//                ResponseUtil.write(response, json);
//                if(resTotal>0){
//                    String basePath = request.getSession().getServletContext().getRealPath("/");
//                    if(basePath.lastIndexOf('/')==-1){
//                        basePath=basePath.replace('\\','/');
//                    }
//                    pushNotificationService.SendPushNotification(basePath,json,order.getOrder_no(),"order_"+order.getOrder_status());
//                }
//
//            }catch  (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return null;
//
//    }



//    //个人用户订单付款
//    @RequestMapping(value = "/PersonUserPayOrder")
//    @ResponseBody
//    public String PersonUserPayOrder(HttpServletRequest request, HttpServletResponse response){
//        System.out.print("PersonUserPayOrder");
//        JSONObject json=new JSONObject();
//        //返回用户session数据
//        HttpSession session = request.getSession();
//        //把用户数据保存在session域对象中
//        String username=(String)session.getAttribute("userSession");
//        String accountType=(String)session.getAttribute("accountType");
//        int resTotal=0;
//        Order order=null;
//        try{
//
//            if(accountType==null||accountType.equals("")){
//                json.put("success",false);
//                json.put("relogin",true);
//                json.put("message","不存在session，重新登录");
//            }
//            else{
//
//                if(accountType.equals("person_user")){
//
//                    //先判断person_user下是否有未完成的order
//                    order = orderDao.getCurrentPersonUserOrderByUsername(username);
//                    if(order!=null&&order.getOrder_status().equals("confirmed")){
//                        //这里验证付费状态
//
//                        //。。。。。。。。。。
//
//                        //设置order状态
//                        order.setOrder_status("confirmedpaid");
//                        resTotal=orderDao.updateOrder(order);
//                    }else{
//                        json.put("success",false);
//                        json.put("message","不存在未付费的订单");
//                    }
//
//                    if(resTotal>0){
//                        json.put("success",true);
//                        json.put("OrderNo",order.getOrder_no());
//                        json.put("message","订单支付成功");
//
//
//                    }else{
//                        json.put("success",false);
//                        json.put("message","订单支付失败");
//                    }
//                }else{
//                    json.put("success",false);
//                    json.put("message","订单支付失败,accountType为"+accountType);
//                }
//
//            }
//
//        }catch (Exception e){
//            e.printStackTrace();
//            json.put("success",false);
//            json.put("message",e.getMessage());
//
//        }finally {
//            try {
//                ResponseUtil.write(response, json);
//                if(resTotal>0){
//                    String basePath = request.getSession().getServletContext().getRealPath("/");
//                    if(basePath.lastIndexOf('/')==-1){
//                        basePath=basePath.replace('\\','/');
//                    }
//                    pushNotificationService.SendPushNotification(basePath,json,order.getOrder_no(),"order_"+order.getOrder_status());
//                }
//
//            }catch  (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return null;
//
//    }


    //个人用户生成订单
    @RequestMapping(value = "/PersonUserSubmitPendingOrder")
    @ResponseBody
    public String PersonUserSubmitPendingOrder(Order order, HttpServletRequest request){
        System.out.print("PersonUserSubmitPendingOrder");
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
            map=personUserService.PersonUserSubmitPendingOrder(username,accountType,order,basePath);
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

            System.out.print("map:"+map);
            return map;
        }

    }






    //获取person_user_info
    @RequestMapping(value = "getPersonUserInfo",produces = "text/plain;charset=utf-8")
    @ResponseBody
    public String getPersonUserInfo( HttpServletRequest request){

        System.out.print("getPersonUserInfo");
        //返回用户session数据
        HttpSession session = request.getSession();
        //把用户数据保存在session域对象中
        String username = (String) session.getAttribute("userSession");
        String map="";
        map =personUserService.getPersonUserInfo(username);

        return map;

    }
    //保存PersonUser
    @RequestMapping(value = "/savePersonUserInfo")
    @ResponseBody
    public String savePersonUserInfo(PersonUser personUser){
        System.out.print("savePersonUserInfo");
        JSONObject json=new JSONObject();
        String map="";
        try{
            map=personUserService.savePersonUserInfo(personUser);
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



    //认证PersonUser
    @RequestMapping(value = "/verifyPersonUserInfo")
    @ResponseBody
    public String verifyPersonUserInfo(HttpServletRequest request){
        System.out.print("verifyPersonUserInfo");
        String id_card_picture_front= request.getParameter("id_card_picture_front");
        String id_card_picture_back= request.getParameter("id_card_picture_back");
        JSONObject json=new JSONObject();
        //返回用户session数据
        HttpSession session = request.getSession();
        //把用户数据保存在session域对象中
        String username = (String) session.getAttribute("userSession");
        String accountType = (String) session.getAttribute("accountType");
        String map=personUserService.verifyPersonUserInfo(username,accountType,id_card_picture_back,id_card_picture_front);

        return map;
    }


    //撤回认证PersonUser
    @RequestMapping(value = "/cancelVerifyPersonUserInfo")
    @ResponseBody
    public String cancelVerifyPersonUserInfo(HttpServletRequest request){
        System.out.print("cancelVerifyPersonUserInfo");
        //返回用户session数据
        HttpSession session = request.getSession();
        //把用户数据保存在session域对象中
        String username = (String) session.getAttribute("userSession");
        String accountType = (String) session.getAttribute("accountType");
        String map=personUserService.cancelVerifyPersonUserInfo(username,accountType);

        return map;
    }


    //用户确认订单完成
    @RequestMapping(value = "/confirmOrderFinish")
    @ResponseBody
    public String confirmOrderFinish(HttpServletRequest request) throws Exception{
        System.out.print("confirmOrderFinish");

      //  JSONObject json = new JSONObject();
        //返回用户session数据
        HttpSession session = request.getSession();
        //把用户数据保存在session域对象中
        String username = (String) session.getAttribute("userSession");
        String accountType = (String) session.getAttribute("accountType");

        String map="";
        try{
            map=personUserService.confirmOrderFinish(username,accountType);
        }catch (Exception e){
            JSONObject json=new JSONObject();
            json.put("success", false);
            json.put("msg", "商户余额修改失败,订单确认完成失败！");
            map= JSONObject.toJSONString(json);
            System.out.print(map);
            return map;
        }
        return map;
    }



}
