package com.prweb.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


import com.prweb.dao.*;
import com.prweb.entity.Account;
import com.prweb.entity.Business;
import com.prweb.entity.Location;
import com.prweb.entity.Order;
import com.prweb.entity.OrderStatus;
import com.prweb.service.OrderService;
import com.prweb.service.PushNotificationService;
import com.prweb.util.APICloudPushService;
import com.prweb.util.AliPayService;
import com.prweb.util.ComboxItem;
import com.prweb.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/Order")
public class OrderController {

    
    @Autowired
    private OrderService orderService;



    //APP使用 获取所有历史订单  /Order/getAllOrderList.action
    @RequestMapping(value = "getAllOrderList", produces = "text/plain;charset=utf-8")
    @ResponseBody
    public String getAllOrderList(HttpServletRequest request) {
        String page = request.getParameter("page");
        String rows = request.getParameter("rows");
            JSONObject json = new JSONObject();
            //返回用户session数据
            HttpSession session = request.getSession();
            //把用户数据保存在session域对象中
            String username = (String) session.getAttribute("userSession");
            String accountType = (String) session.getAttribute("accountType");
            return orderService.getAllOrderList(username,accountType,page,rows);
    }


    //APP使用 获取当前订单
    @RequestMapping(value = "getCurrentOrder", produces = "text/plain;charset=utf-8")
    @ResponseBody
    public String getCurrentOrder(HttpServletRequest request) {

        JSONObject json = new JSONObject();
        //返回用户session数据
        HttpSession session = request.getSession();
        //把用户数据保存在session域对象中
        String username = (String) session.getAttribute("userSession");
        String accountType = (String) session.getAttribute("accountType");
        return orderService.getCurrentOrder(username,accountType);

    }




    //搜索
    @RequestMapping(value = "getOrderByLike", produces = "text/plain;charset=utf-8")
    @ResponseBody
    public String getOrderByLike(@RequestParam(value = "order_no", required = false) String order_no, @RequestParam(value = "order_status", required = false) String order_status, @RequestParam(value = "begin_time", required = false) String begin_time, @RequestParam(value = "end_time", required = false) String end_time, HttpServletRequest request) {
        String page = request.getParameter("page");
        String rows = request.getParameter("rows");
        if (page == null) {
            page = "1";
        }
        if (rows == null) {
            rows = "20";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date beginTime = null;
        Date endTime = null;

        try {
            if (begin_time != null && begin_time != "") {
                beginTime = sdf.parse(begin_time);
                System.out.println(beginTime.toString());
            }
            if (end_time != null && end_time != "") {
                endTime = sdf.parse(end_time);
                System.out.println(endTime.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        int start = (Integer.parseInt(page) - 1) * Integer.parseInt(rows);

        return orderService.getOrderByLike(order_no,order_status,beginTime,endTime,start,Integer.parseInt(rows));
    }

    //根据order_no获得order
    @RequestMapping(value = "getOrderByOrderNo", produces = "text/plain;charset=utf-8")
    @ResponseBody
    public String getOrderByOrderNo(@RequestParam(value = "order_no", required = false) String order_no, HttpServletRequest request) {
        return orderService.getOrderByOrderNo(order_no);
    }


    //根据order_no获得order相关信息map
    @RequestMapping(value = "getOrderMapByOrderNo", produces = "text/plain;charset=utf-8")
    @ResponseBody
    public String getOrderMapByOrderNo(@RequestParam(value = "order_no", required = false) String order_no, HttpServletRequest request) {
        return orderService.getOrderMapByOrderNo(order_no);
    }






    //保存Order
    @RequestMapping(value = "/saveOrder")
    @ResponseBody
    public String saveOrder(Order order,HttpServletRequest request, HttpServletResponse response){
        System.out.print("saveOrder");
        JSONObject json=new JSONObject();
        String basePath = request.getSession().getServletContext().getRealPath("/");
        if(basePath.lastIndexOf('/')==-1){
            basePath=basePath.replace('\\','/');
        }
        String map="";
        try{

            map=orderService.saveOrder(order,basePath);

        }catch (Exception e){
            e.printStackTrace();
            json.put("success",false);
            json.put("message",e.getMessage());
            map= JSONArray.toJSONString(json);
        }finally {
//            try {
//                ResponseUtil.write(response, json);
//
//            }catch  (Exception e) {
//                e.printStackTrace();
//            }
            return map;
        }

    }


    //删除Order信息
    @RequestMapping("/delOrder")
    @ResponseBody
    public String delOrder(@RequestParam(value = "hlparam")String hlparam,HttpServletResponse response)throws Exception{
        return orderService.delOrder(hlparam);
    }

    //用于下拉框
    @RequestMapping(value = "getAllOrderStatus",produces = "text/plain;charset=utf-8")
    @ResponseBody
    public String getAllOrderStatus(HttpServletRequest request){
        return orderService.getAllOrderStatus();
    }

    //得到所有的订单状态用于app
    @RequestMapping(value ="/getAllOrderStatusForAPP",produces = "text/plain;charset=utf-8")
    @ResponseBody
    public String getAllOrderStatusForAPP(HttpServletRequest request){
        return orderService.getAllOrderStatusForAPP();
    }
    //APP定位更新order的person 或 company_user 位置信息

    public OrderController() {
    }


    //获取当前订单的personUser和CompanyUser的Location
    @RequestMapping(value = "/getPersonUserAndCompanyUserCurrentLocation")
    @ResponseBody
    public String getPersonUserAndCompanyUserCurrentLocation(HttpServletRequest request, HttpServletResponse response) {
        //System.out.print("getPersonUserAndCompanyUserCurrentLocation");
        //返回用户session数据
        HttpSession session = request.getSession();
        //把用户数据保存在session域对象中
        String username = (String) session.getAttribute("userSession");
        String accountType = (String) session.getAttribute("accountType");
        return orderService.getPersonUserAndCompanyUserCurrentLocation(username,accountType);
    }



    //更新个人location到历史location，轨迹保存
    @RequestMapping(value = "/updateLocation")
    @ResponseBody
    public String updateLocation(HttpServletRequest request, HttpServletResponse response) {
        //System.out.print("updateLocation");
        String order_no= request.getParameter("order_no");
        String my_location= request.getParameter("my_location");
        //返回用户session数据
        HttpSession session = request.getSession();
        //把用户数据保存在session域对象中
        String username = (String) session.getAttribute("userSession");
        String accountType = (String) session.getAttribute("accountType");

        return orderService.updateLocation(username,accountType,order_no,my_location);
    }


    //获取当前订单的支付宝支付信息
    @RequestMapping(value = "/getCurrentOrderAliPayInfo")
    @ResponseBody
    public String getCurrentOrderAliPayInfo(HttpServletRequest request, HttpServletResponse response) {
        System.out.print("getCurrentOrderAliPayInfo");

        JSONObject json = new JSONObject();
        //返回用户session数据
        HttpSession session = request.getSession();
        //把用户数据保存在session域对象中
        String username = (String) session.getAttribute("userSession");
        String accountType = (String) session.getAttribute("accountType");

        return orderService.getCurrentOrderAliPayInfo(username,accountType);
    }


    //支付宝支付回调函数
    @RequestMapping(value = "/AliPayNotify")
    @ResponseBody
    public String AliPayNotify(HttpServletRequest request) {
        System.out.print("AliPayNotify");
        Map<String,String> params=new HashMap<String,String>();
        Map requestParamMap=request.getParameterMap();
        String content="";
        List<String> lt=new ArrayList<>();
        for(Iterator iter=requestParamMap.keySet().iterator();iter.hasNext();){
            String name=(String)iter.next();
            String[] values=(String[])requestParamMap.get(name);
            String valueStr="";
            for(int i=0;i< values.length;i++){
                valueStr=(i==values.length-1)?valueStr+values[i]:valueStr+values[i]+",";
            }
            params.put(name,valueStr);
            System.out.println(name+"="+valueStr);
            if(!name.equals("sign")&&!name.equals("sign_type"))
            lt.add(name);
        }
        //签名
        String sign=params.get("sign");
//        String sign_type=params.get("sign_type");
        //熊猫救援的订单号
        String out_trade_no=params.get("out_trade_no");
        //支付宝交易号
        String trade_no=params.get("trade_no");
        //支付宝交易状态
        String trade_status=params.get("trade_status");
//        System.out.println("sign="+sign);
//        System.out.println("sign_type="+sign_type);
//        System.out.println("out_trade_no="+out_trade_no);
        if(lt.size()>0){
            String[] contentArray =new String[lt.size()];
            lt.toArray(contentArray);
            //排序
            Arrays.sort(contentArray);
//        for(int i=0;i<contentArray.length;i++){
//            System.out.println(contentArray[i]);
//        }
            StringBuffer sb = new StringBuffer("");
            for (int i = 0; i < contentArray.length; i++) {
                sb.append(contentArray[i]);
                sb.append("=");
                sb.append(params.get(contentArray[i]));
                //sb.append("\"");
                if(i<(contentArray.length-1)){
                    sb.append("&");
                }
            }
            content=sb.toString();
            System.out.println("=================================");
            System.out.println(content);
            System.out.println("=================================");
        }
        String basePath = request.getSession().getServletContext().getRealPath("/");
        if(basePath.lastIndexOf('/')==-1){
            basePath=basePath.replace('\\','/');
        }

        return orderService.AliPayNotify(content,sign,out_trade_no,trade_status,trade_no,basePath);
        //return map;

    }







}
