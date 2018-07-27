package com.prweb.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.prweb.dao.AccountDao;
import com.prweb.dao.CompanyDao;
import com.prweb.dao.OrderDao;
import com.prweb.entity.Account;
import com.prweb.entity.Order;
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
    OrderDao orderDao;

    @Autowired
    private AccountDao accountDao;

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



        List<HashMap<String,Object>> list=orderDao.getNearByPendingOrders(lon,lat);
        String map= JSONObject.toJSONString(list);
        return map;
    }


    //商户用户完成订单服务
    @RequestMapping(value = "/CompanyUserFinishService")
    @ResponseBody
    public String CompanyUserFinishService(HttpServletRequest request, HttpServletResponse response){
        System.out.print("CompanyUserFinishService");
        JSONObject json=new JSONObject();
        //返回用户session数据
        HttpSession session = request.getSession();
        //把用户数据保存在session域对象中
        String username=(String)session.getAttribute("userSession");
        String accountType=(String)session.getAttribute("accountType");
        int resTotal=0;
        Order order=null;
        try{

            if(accountType==null||accountType.equals("")){
                json.put("success",false);
                json.put("relogin",true);
                json.put("message","不存在session，重新登录");
            }
            else{

                if(accountType.equals("company_user")){

                    //先判断company_user下是否有未完成的order
                    order = orderDao.getCurrentOrderCompanyUserByUsername(username);
                    if(order!=null&&order.getOrder_status().equals("inservice")){
                        //设置order状态
                        order.setOrder_status("finished");
                        //设置完成时间
                        order.setFinsh_time(new Date());
                        resTotal=orderDao.updateOrder(order);
                    }else{
                        json.put("success",false);
                        json.put("message","不存在正在服务的订单");
                    }

                    if(resTotal>0){
                        json.put("success",true);
                        json.put("OrderNo",order.getOrder_no());
                        json.put("message","订单服务完成成功");


                    }else{
                        json.put("success",false);
                        json.put("message","订单服务完成失败");
                    }
                }else{
                    json.put("success",false);
                    json.put("message","订单服务完成失败,accountType为"+accountType);
                }

            }

        }catch (Exception e){
            e.printStackTrace();
            json.put("success",false);
            json.put("message",e.getMessage());

        }finally {
            try {
                ResponseUtil.write(response, json);
                if(resTotal>0){
                    SendPushNotification(request,json,order.getOrder_no(),"order_finished");
                }

            }catch  (Exception e) {
                e.printStackTrace();
            }
        }
        return null;

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
        int resTotal=0;
        Order order=null;
        try{

            if(accountType==null||accountType.equals("")){
                json.put("success",false);
                json.put("relogin",true);
                json.put("message","不存在session，重新登录");
            }
            else{

                if(accountType.equals("company_user")){

                        //先判断company_user下是否有未完成的order
                        order = orderDao.getCurrentOrderCompanyUserByUsername(username);
                        if(order!=null&&order.getOrder_status().equals("confirmedpaid")){
                            //设置order状态
                            order.setOrder_status("inservice");
                            resTotal=orderDao.updateOrder(order);
                        }else{
                            json.put("success",false);
                            json.put("message","不存在可服务且已付费的订单");
                        }

                    if(resTotal>0){
                        json.put("success",true);
                        json.put("OrderNo",order.getOrder_no());
                        json.put("message","订单服务开始成功");


                    }else{
                        json.put("success",false);
                        json.put("message","订单服务开始失败");
                    }
                }else{
                    json.put("success",false);
                    json.put("message","订单服务开始失败,accountType为"+accountType);
                }

            }

        }catch (Exception e){
            e.printStackTrace();
            json.put("success",false);
            json.put("message",e.getMessage());

        }finally {
            try {
                ResponseUtil.write(response, json);
                if(resTotal>0){
                    SendPushNotification(request,json,order.getOrder_no(),"order_inservice");
                }

            }catch  (Exception e) {
                e.printStackTrace();
            }
        }
        return null;

    }


    //商户用户接订单
    @RequestMapping(value = "/CompanyUserAcceptPendingOrder")
    @ResponseBody
    public String CompanyUserAcceptPendingOrder(HttpServletRequest request, HttpServletResponse response){
        System.out.print("CompanyUserAcceptPendingOrder");
        JSONObject json=new JSONObject();
        //返回用户session数据
        HttpSession session = request.getSession();
        String order_no = (String) request.getParameter("order_no");

        //把用户数据保存在session域对象中
        String username=(String)session.getAttribute("userSession");
        String accountType=(String)session.getAttribute("accountType");
        int resTotal=0;
        Order order=null;
        try{

            if(accountType==null||accountType.equals("")){
                json.put("success",false);
                json.put("relogin",true);
                json.put("message","不存在session，重新登录");
            }
            else if(order_no==null){
                json.put("success",false);
                json.put("message","order_no不存在");
            }
            else{

                if(accountType.equals("company_user")){
                        //先判断company_user下是否有未完成的order
                        Order currentorder = orderDao.getCurrentOrderCompanyUserByUsername(username);
                        if(currentorder==null){

                            List<Order> orderlt=orderDao.getOrderByOrderNo(order_no);
                            if(orderlt.size()>0&&orderlt.get(0).getOrder_status().equals("pending")){
                                order=orderlt.get(0);
                                //设置order状态
                                order.setOrder_status("confirmed");
                                if(username!=null){
                                    List<Account> accountlist=accountDao.getAccountByUserName(username);
                                    if(accountlist.size()>0){
                                        order.setCompany_user_no(accountlist.get(0).getCompany_user_no());
                                        resTotal=orderDao.updateOrder(order);
                                        if(resTotal>0){
                                            json.put("success",true);
                                            json.put("OrderNo",order.getOrder_no());
                                            json.put("message","订单接受成功");
                                        }else{
                                            json.put("success",false);
                                            json.put("message","订单接受失败");
                                        }
                                    }
                                }
                            }
                            else{
                                json.put("success",false);
                                json.put("message","订单接受失败,订单不存在或订单状态不为pending");
                            }
                        }else{
                            json.put("success",false);
                            json.put("message","不能接受新的订单，存在未完成的订单");
                        }

                }else{
                    json.put("success",false);
                    json.put("message","订单接受失败,accountType为"+accountType);
                }

            }

        }catch (Exception e){
            e.printStackTrace();
            json.put("success",false);
            json.put("message",e.getMessage());

        }finally {
            try {
                ResponseUtil.write(response, json);
                if(resTotal>0){
                    SendPushNotification(request,json,order.getOrder_no(),"order_accepted");
                }

            }catch  (Exception e) {
                e.printStackTrace();
            }
        }
        return null;

    }


    //发送推送给相关人员
    private void SendPushNotification(HttpServletRequest request,JSONObject json,String orderNo,String event){
        String basePath = request.getSession().getServletContext().getRealPath("/");
        if(basePath.lastIndexOf('/')==-1){
            basePath=basePath.replace('\\','/');
        }

        String jsonstr= JSONArray.toJSONString(json);
        //查找订单的两个有关人员电话
        List<HashMap<String,Object>> lt=orderDao.getPushPhoneNosByOrderNo(orderNo);
        if(lt.size()>0){

            String userIds1=(String)lt.get(0).get("phone1");
            String userIds2=(String)lt.get(0).get("phone2");
            System.out.println("userIds1="+userIds1);
            System.out.println("userIds2="+userIds2);

            String userIds="";
            if(userIds1!=null&&!userIds1.equals("")){
                userIds=userIds1;
            }
            if(userIds2!=null&&!userIds2.equals("")){
                userIds=userIds+","+userIds2;
            }
            System.out.println("userIds="+userIds);
            SendPushNotificationToAccounts(basePath,event,event+"订单:"+orderNo,jsonstr,userIds);
        }
    }

    //发送推送消息 accounts  phone ,分隔
    public void SendPushNotificationToAccounts(String basePath, String event, String title, String content, String userIds) {


        //发消息
        APICloudPushService.SendPushNotification(basePath, title, content, "1", "0", "", userIds);


    }

}
