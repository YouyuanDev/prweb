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
    OrderDao orderDao;

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private CompanyUserDao companyUserDao;
    @Autowired
    private CompanyDao companyDao;

    @Autowired
    private PushNotificationService pushNotificationService;

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
    @RequestMapping(value = "/CompanyUserCancelOrder")
    @ResponseBody
    public String CompanyUserCancelOrder(HttpServletRequest request, HttpServletResponse response){
        System.out.print("CompanyUserCancelOrder");
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
                    String basePath = request.getSession().getServletContext().getRealPath("/");
                    if(basePath.lastIndexOf('/')==-1){
                        basePath=basePath.replace('\\','/');
                    }
                    pushNotificationService.SendPushNotification(basePath,json,order.getOrder_no(),"order_"+order.getOrder_status());

                }

            }catch  (Exception e) {
                e.printStackTrace();
            }
        }
        return null;

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
                    String basePath = request.getSession().getServletContext().getRealPath("/");
                    if(basePath.lastIndexOf('/')==-1){
                        basePath=basePath.replace('\\','/');
                    }
                    pushNotificationService.SendPushNotification(basePath,json,order.getOrder_no(),"order_"+order.getOrder_status());

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
                    String basePath = request.getSession().getServletContext().getRealPath("/");
                    if(basePath.lastIndexOf('/')==-1){
                        basePath=basePath.replace('\\','/');
                    }
                    pushNotificationService.SendPushNotification(basePath,json,order.getOrder_no(),"order_"+order.getOrder_status());

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
                    String basePath = request.getSession().getServletContext().getRealPath("/");
                    if(basePath.lastIndexOf('/')==-1){
                        basePath=basePath.replace('\\','/');
                    }
                    pushNotificationService.SendPushNotification(basePath,json,order.getOrder_no(),"order_"+order.getOrder_status());
                }

            }catch  (Exception e) {
                e.printStackTrace();
            }
        }
        return null;

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
        List<Company> list=companyUserDao.getCompanyInfoByUsername(username);
        Company company=null;
        if(list.size()>0){
            company=list.get(0);
            json.put("success",true);
            json.put("company",company);
            json.put("message","获取商户信息成功");

        }else{
            json.put("success",false);
            json.put("message","获取商户信息失败");

        }

        String mmp= JSONArray.toJSONString(json);
        System.out.print("mmp:"+mmp);
        return mmp;

    }
    //保存PersonUser
    @RequestMapping(value = "/saveCompanyUserInfo")
    @ResponseBody
    public String saveCompanyUserInfo(Company company, HttpServletResponse response){
        System.out.print("saveCompanyUserInfo");

        JSONObject json=new JSONObject();
        try{
            int resTotal=0;
            if(company.getCompany_found_date()==null){
                company.setCompany_found_date(new Date());
            }
            if(company.getId()==0){
                //添加
                resTotal=companyDao.addCompany(company);
            }else{
                //修改！
                resTotal=companyDao.updateCompany(company);
            }
            if(resTotal>0){
                json.put("success",true);
                json.put("message","保存成功");
            }else{
                json.put("success",false);
                json.put("message","保存失败");
            }

        }catch (Exception e){
            e.printStackTrace();
            json.put("success",false);
            json.put("message",e.getMessage());

        }finally {
            try {
                ResponseUtil.write(response, json);
            }catch  (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
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

        if(username==null){
            json.put("success",false);
            json.put("relogin",true);
            json.put("message","session不存在，请登录");
        }
        else{
            List<Company> list=companyUserDao.getCompanyInfoByUsername(username);
            if(list.size()>0){
                Company comp=list.get(0);
                if(comp.getIs_verified().equals("2")){
                    comp.setIs_verified("0");
                    int res=companyDao.updateCompany(comp);
                    if(res>0){
                        json.put("success",true);
                        json.put("message","商户认证撤销成功");
                    }else{
                        json.put("success",false);
                        json.put("message","系统错误");
                    }

                }else{
                    json.put("success",false);
                    json.put("message","商户认证撤销失败，没有审核中的认证");
                }

            }else{
                json.put("success",false);
                json.put("message","不存在该用户名，认证撤销失败");
            }
        }

        String mmp= JSONArray.toJSONString(json);
        System.out.print("mmp:"+mmp);
        return mmp;
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

        if(username==null){
            json.put("success",false);
            json.put("relogin",true);
            json.put("message","session不存在，请登录");
        }
        else if(id_card_picture_back==null||id_card_picture_front==null||business_certificate_picture==null){
            json.put("success",false);
            json.put("message","身份证照片或营业执照照片不存在，认证提交失败");
        }else{
            List<Company> list=companyUserDao.getCompanyInfoByUsername(username);
            if(list.size()>0){
                Company comp=list.get(0);
                if(comp.getIs_verified().equals("0")||comp.getIs_verified().equals("3")){
                    comp.setId_card_picture_front(id_card_picture_front);
                    comp.setId_card_picture_back(id_card_picture_back);
                    comp.setBusiness_certificate_picture(business_certificate_picture);
                    comp.setIs_verified("2");
                    int res=companyDao.updateCompany(comp);
                    if(res>0){
                        json.put("success",true);
                        json.put("message","商户认证提交成功");
                    }else{
                        json.put("success",false);
                        json.put("message","系统错误");
                    }

                }else{
                    json.put("success",false);
                    json.put("message","商户认证提交失败，账户已认证或正在认证审核中");
                }

            }else{
                json.put("success",false);
                json.put("message","不存在该用户名，认证提交失败");
            }
        }

        String mmp= JSONArray.toJSONString(json);
        System.out.print("mmp:"+mmp);
        return mmp;
    }

}
