package com.prweb.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.prweb.dao.AccountDao;
import com.prweb.dao.CompanyDao;
import com.prweb.dao.OrderDao;
import com.prweb.entity.Account;
import com.prweb.entity.Business;
import com.prweb.entity.CompanyUser;
import com.prweb.entity.Order;
import com.prweb.util.APICloudPushService;
import com.prweb.util.ComboxItem;
import com.prweb.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping("/PersonUser")
public class PersonUserController {

    @Autowired
    CompanyDao companyDao;

    @Autowired
    private OrderDao orderDao;


    @Autowired
    private AccountDao accountDao;


    //用于
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



        List<HashMap<String,Object>> list=companyDao.getNearByCompany(lon,lat);
        String map= JSONObject.toJSONString(list);
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
        int resTotal=0;
        Order order=null;
        try{

            if(accountType==null||accountType.equals("")){
                json.put("success",false);
                json.put("relogin",true);
                json.put("message","不存在session，重新登录");
            }
            else{

                if(accountType.equals("person_user")){
                    //先判断person_user下是否有未完成的order
                    order = orderDao.getCurrentPersonUserOrderByUsername(username);
                    if(order!=null&&!order.getOrder_status().equals("cancelled")&&
                            !order.getOrder_status().equals("finishedconfirmed")){
                        //设置order状态
                        order.setOrder_status("cancelled");
                        resTotal=orderDao.updateOrder(order);
                    }else{
                        json.put("success",false);
                        json.put("message","不存在可取消的订单");
                    }

                    if(resTotal>0){
                        json.put("success",true);
                        json.put("OrderNo",order.getOrder_no());
                        json.put("message","订单取消成功");


                    }else{
                        json.put("success",false);
                        json.put("message","订单取消失败");
                    }
                }else{
                    json.put("success",false);
                    json.put("message","订单取消失败,accountType为"+accountType);
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
                    SendPushNotification(request,json,order.getOrder_no(),"order_"+order.getOrder_status());
                }

            }catch  (Exception e) {
                e.printStackTrace();
            }
        }
        return null;

    }


    //个人用户订单确认完成
    @RequestMapping(value = "/PersonUserConfirmFinishedOrder")
    @ResponseBody
    public String PersonUserConfirmFinishedOrder(HttpServletRequest request, HttpServletResponse response){
        System.out.print("PersonUserConfirmFinishedOrder");
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

                if(accountType.equals("person_user")){

                    //先判断person_user下是否有未完成的order
                    order = orderDao.getCurrentPersonUserOrderByUsername(username);
                    if(order!=null&&order.getOrder_status().equals("finished")){
                        //设置order状态
                        order.setOrder_status("finishedconfirmed");
                        resTotal=orderDao.updateOrder(order);
                    }else{
                        json.put("success",false);
                        json.put("message","不存在可确认完成的订单");
                    }

                    if(resTotal>0){
                        json.put("success",true);
                        json.put("OrderNo",order.getOrder_no());
                        json.put("message","订单确认完成成功");


                    }else{
                        json.put("success",false);
                        json.put("message","订单确认完成失败");
                    }
                }else{
                    json.put("success",false);
                    json.put("message","订单确认完成失败,accountType为"+accountType);
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
                    SendPushNotification(request,json,order.getOrder_no(),"order_"+order.getOrder_status());
                }

            }catch  (Exception e) {
                e.printStackTrace();
            }
        }
        return null;

    }



    //个人用户订单付款
    @RequestMapping(value = "/PersonUserPayOrder")
    @ResponseBody
    public String PersonUserPayOrder(HttpServletRequest request, HttpServletResponse response){
        System.out.print("PersonUserPayOrder");
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

                if(accountType.equals("person_user")){

                    //先判断person_user下是否有未完成的order
                    order = orderDao.getCurrentPersonUserOrderByUsername(username);
                    if(order!=null&&order.getOrder_status().equals("confirmed")){
                        //这里验证付费状态

                        //。。。。。。。。。。

                        //设置order状态
                        order.setOrder_status("confirmedpaid");
                        resTotal=orderDao.updateOrder(order);
                    }else{
                        json.put("success",false);
                        json.put("message","不存在未付费的订单");
                    }

                    if(resTotal>0){
                        json.put("success",true);
                        json.put("OrderNo",order.getOrder_no());
                        json.put("message","订单支付成功");


                    }else{
                        json.put("success",false);
                        json.put("message","订单支付失败");
                    }
                }else{
                    json.put("success",false);
                    json.put("message","订单支付失败,accountType为"+accountType);
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
                    SendPushNotification(request,json,order.getOrder_no(),"order_"+order.getOrder_status());
                }

            }catch  (Exception e) {
                e.printStackTrace();
            }
        }
        return null;

    }


    //个人用户生成订单
    @RequestMapping(value = "/PersonUserSubmitPendingOrder")
    @ResponseBody
    public String PersonUserSubmitPendingOrder(Order order, HttpServletRequest request, HttpServletResponse response){
        System.out.print("PersonUserSubmitPendingOrder");
        JSONObject json=new JSONObject();
        //返回用户session数据
        HttpSession session = request.getSession();
        //把用户数据保存在session域对象中
        String username=(String)session.getAttribute("userSession");
        String accountType=(String)session.getAttribute("accountType");
        int resTotal=0;
        try{

            if(accountType==null||accountType.equals("")){
                json.put("success",false);
                json.put("relogin",true);
                json.put("message","不存在session，重新登录");
            }
            else{

                if(accountType.equals("person_user")){
                    if(order.getId()==0){
                        //先判断person_user下是否有未完成的order
                        if(order.getOrder_time()==null){
                            order.setOrder_time(new Date());
                        }
                        Order currentorder = orderDao.getCurrentPersonUserOrderByUsername(username);
                        if(currentorder==null){
                            //添加
                            //设置orderno
                            String uuuid= UUID.randomUUID().toString();
                            uuuid=uuuid.replace("-","");
                            order.setOrder_no("OR"+uuuid);
                            //设置order状态
                            if(order.getOrder_status()==null)
                                order.setOrder_status("pending");

                            if(username!=null){
                                List<Account> accountlist=accountDao.getAccountByUserName(username);
                                if(accountlist.size()>0){
                                    order.setPerson_user_no(accountlist.get(0).getPerson_user_no());
                                }
                            }


                            resTotal=orderDao.addOrder(order);
                        }else{
                            json.put("success",false);
                            json.put("message","不能生成新的订单，存在未完成的订单");
                        }

                    }
                    if(resTotal>0){
                        json.put("success",true);
                        json.put("OrderNo",order.getOrder_no());
                        json.put("message","订单生成成功");


                    }else{
                        json.put("success",false);
                        json.put("message","订单生成失败");
                    }
                }else{
                    json.put("success",false);
                    json.put("message","订单生成失败,accountType为"+accountType);
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
                    SendPushNotification(request,json,order.getOrder_no(),"order_"+order.getOrder_status());
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

            //如果是新增订单，范围推送
            if(event!=null&&event.equals("order_pending")){
                List<Order> orderlist=orderDao.getOrderByOrderNo(orderNo);
                if(orderlist.size()>0){
                    Order od=orderlist.get(0);
                    String person_user_location=od.getPerson_user_location();
                    String[] loc=null;
                    if(person_user_location!=null){
                        loc=person_user_location.split(",");
                    }
                    if(loc!=null&&loc.length==2){
                        //获取用户周围附近的商户下的商户用户
                        List<HashMap<String,Object>> cmpuserActlist=companyDao.getNearByCompanyUsers(loc[0],loc[1]);
                        for(int i=0;i<cmpuserActlist.size();i++){
                            String cell_phone=(String)cmpuserActlist.get(i).get("cell_phone");
                            if(cell_phone!=null&&!cell_phone.equals("")){
                                userIds=userIds+","+cell_phone;
                            }
                        }
                        System.out.println("new Order push to："+userIds);
                    }

                }
            }



            SendPushNotificationToAccounts(basePath,event,event+"订单:",jsonstr,userIds);
        }
    }

    //发送推送消息 accounts  phone ,分隔
    public void SendPushNotificationToAccounts(String basePath, String event, String title, String content, String userIds) {


        //发消息
        APICloudPushService.SendPushNotification(basePath, title, content, "1", "0", "", userIds);


    }

}
