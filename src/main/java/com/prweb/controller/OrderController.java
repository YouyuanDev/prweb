package com.prweb.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


import com.prweb.dao.*;
import com.prweb.entity.Account;
import com.prweb.entity.Business;
import com.prweb.entity.Location;
import com.prweb.entity.Order;
import com.prweb.entity.OrderStatus;
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
    private OrderDao orderDao;

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private OrderStatusDao orderStatusDao;

    @Autowired
    private LocationDao locationDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private CompanyDao companyDao;



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

        Order order = null;
        if (username != null && accountType != null) {
            if (accountType.equals("person_user")) {
                order = orderDao.getCurrentPersonUserOrderByUsername(username);
            } else if (accountType.equals("company_user")) {
                order = orderDao.getCurrentOrderCompanyUserByUsername(username);
            }
            if (order != null) {
                json.put("success", true);
                json.put("order_no", order.getOrder_no());
                json.put("accountType", accountType);
                json.put("msg", "存在Order");
            } else {
                json.put("success", false);
                json.put("accountType", accountType);
                json.put("msg", "不存在进行中的Order");
            }
        } else {
            json.put("success", false);
            json.put("relogin", true);
            json.put("msg", "session不存在，重新登录");
        }


        String mmp = JSONArray.toJSONString(json);
        System.out.println(mmp);
        return mmp;
    }


    //发送推送消息
    public void SendEventToRoles(String basePath, String event, String title, String content) {
        List<HashMap<String, Object>> lt = roleDao.getRolesByEvent(event);

        for (int i = 0; i < lt.size(); i++) {
            String role = (String) lt.get(i).get("role_no");
            //发消息
            APICloudPushService.SendPushNotification(basePath, title, content, "1", "0", role, "");
        }


    }

    //发送推送消息 accounts  phone ,分隔
    public void SendPushNotificationToAccounts(String basePath, String event, String title, String content, String userIds) {


        //发消息
        APICloudPushService.SendPushNotification(basePath, title, content, "1", "0", "", userIds);


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
        List<HashMap<String, Object>> list = orderDao.getAllByLike(order_no, order_status, beginTime, endTime, start, Integer.parseInt(rows));
        int count = orderDao.getCountAllByLike(order_no, order_status, beginTime, endTime);
        Map<String, Object> maps = new HashMap<String, Object>();
        maps.put("total", count);
        maps.put("rows", list);
        //System.out.println("rrrrrrrrrrrow="+count);
        String mmp = JSONArray.toJSONString(maps);
        //System.out.print("mmp:"+mmp);
        return mmp;

    }

    @RequestMapping(value = "getOrderByOrderNo", produces = "text/plain;charset=utf-8")
    @ResponseBody
    public String getOrderByOrderNo(@RequestParam(value = "order_no", required = false) String order_no, HttpServletRequest request) {
        List<Order> list = orderDao.getOrderByOrderNo(order_no);
        String mmp = JSONArray.toJSONString(list);
        System.out.println(mmp);
        return mmp;
    }

    @RequestMapping(value = "getOrderMapByOrderNo", produces = "text/plain;charset=utf-8")
    @ResponseBody
    public String getOrderMapByOrderNo(@RequestParam(value = "order_no", required = false) String order_no, HttpServletRequest request) {
        List<HashMap<String, Object>> list = orderDao.getOrderMapByOrderNo(order_no);
        String mmp = JSONArray.toJSONString(list);
        System.out.println(mmp);
        return mmp;
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


    //保存Order
    @RequestMapping(value = "/saveOrder")
    @ResponseBody
    public String saveOrder(Order order,HttpServletRequest request, HttpServletResponse response){
        System.out.print("saveOrder");
        JSONObject json=new JSONObject();

        int resTotal=0;
        try{

            if(order.getOrder_time()==null){
                order.setOrder_time(new Date());
            }
            if(order.getId()==0){
                String uuuid=UUID.randomUUID().toString();
                uuuid=uuuid.replace("-","");
                order.setOrder_no("OR"+uuuid);
                //设置order状态
                if(order.getOrder_status()==null)
                    order.setOrder_status("pending");
                resTotal=orderDao.addOrder(order);
            }else{
                //修改！
                resTotal=orderDao.updateOrder(order);
            }
            if(resTotal>0){
                json.put("success",true);
                json.put("OrderNo",order.getOrder_no());
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

                if(resTotal>0){
                    SendPushNotification(request,json,order.getOrder_no(),"order_"+order.getOrder_status());
                }

            }catch  (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    //删除Order信息
    @RequestMapping("/delOrder")
    public String delOrder(@RequestParam(value = "hlparam")String hlparam,HttpServletResponse response)throws Exception{
        String[]idArr=hlparam.split(",");
        int resTotal=0;
        resTotal=orderDao.delOrder(idArr);
        JSONObject json=new JSONObject();
        StringBuilder sbmessage = new StringBuilder();
        sbmessage.append("总共");
        sbmessage.append(Integer.toString(resTotal));
        sbmessage.append("项订单信息删除成功\n");
        if(resTotal>0){
            //System.out.print("删除成功");
            json.put("success",true);
        }else{
            //System.out.print("删除失败");
            json.put("success",false);
        }
        json.put("message",sbmessage.toString());
        ResponseUtil.write(response,json);
        return null;
    }

    //用于下拉框
    @RequestMapping(value = "getAllOrderStatus",produces = "text/plain;charset=utf-8")
    @ResponseBody
    public String getAllOrderStatus(HttpServletRequest request){
        List<OrderStatus>list=orderStatusDao.getAllOrderStatus();
        List<ComboxItem> colist=new ArrayList<ComboxItem>();
        for(int i=0;i<list.size();i++){
            ComboxItem citem= new ComboxItem();
            OrderStatus os=((OrderStatus)list.get(i));
            citem.id=os.getStatus_code();
            citem.text= os.getStatus_code()+"("+os.getStatus_name()+")";
            colist.add(citem);
        }
        String map= JSONObject.toJSONString(colist);
        return map;
    }

    //APP定位更新order的person 或 company_user 位置信息

    public OrderController() {
    }


    //获取当前订单的personUser和CompanyUser的Location
    @RequestMapping(value = "/getPersonUserAndCompanyUserCurrentLocation")
    @ResponseBody
    public String getPersonUserAndCompanyUserCurrentLocation(HttpServletRequest request, HttpServletResponse response) {
        //System.out.print("getPersonUserAndCompanyUserCurrentLocation");

        JSONObject json = new JSONObject();
        //返回用户session数据
        HttpSession session = request.getSession();
        //把用户数据保存在session域对象中
        String username = (String) session.getAttribute("userSession");
        String accountType = (String) session.getAttribute("accountType");

        Order order = null;
        if (username != null && accountType != null) {
            if (accountType.equals("person_user")) {
                order = orderDao.getCurrentPersonUserOrderByUsername(username);
            } else if (accountType.equals("company_user")) {
                order = orderDao.getCurrentOrderCompanyUserByUsername(username);
            }
            if (order != null) {
                json.put("success", true);
                json.put("accountType", accountType);
                json.put("person_user_location",order.getPerson_user_location());
                json.put("company_user_location",order.getCompany_user_location());
                json.put("msg", "存在Order");
            } else {
                json.put("success", false);
                json.put("accountType", accountType);
                json.put("msg", "不存在未完成的Order");
            }
        } else {
            json.put("success", false);
            json.put("relogin", true);
            json.put("msg", "session不存在，重新登录");
        }

        String map= JSONObject.toJSONString(json);
        //System.out.print(map);
        return map;

    }



    //更新个人location到历史location，轨迹保存
    @RequestMapping(value = "/updateLocation")
    @ResponseBody
    public String updateLocation(HttpServletRequest request, HttpServletResponse response) {
        //System.out.print("updateLocation");
        String order_no= request.getParameter("order_no");
        String my_location= request.getParameter("my_location");

        JSONObject json = new JSONObject();
        //返回用户session数据
        HttpSession session = request.getSession();
        //把用户数据保存在session域对象中
        String username = (String) session.getAttribute("userSession");
        String accountType = (String) session.getAttribute("accountType");


        if(username!=null&&accountType!=null&&my_location!=null){

            if(order_no!=null&&!order_no.equals("")) {
                List<Order> orderList = orderDao.getOrderByOrderNo(order_no);
                if (orderList.size() > 0) {
                    Order order = orderList.get(0);
                    if (accountType.equals("person_user")) {
                        order.setPerson_user_location(my_location);
                    } else if (accountType.equals("company_user")) {
                        order.setCompany_user_location(my_location);
                    }
                    json.put("person_user_location",order.getPerson_user_location());
                    json.put("company_user_location",order.getCompany_user_location());
                    int orderRes = orderDao.updateOrder(order);
                }
            }
               //更新定位轨迹坐标
               Location loc=new Location();
               loc.setId(0);
               loc.setOrder_no(order_no);
               loc.setLocating_time(new Date());
               loc.setUsername(username);
               loc.setCoordinate(my_location);
               int locRes=locationDao.addLocation(loc);
               if(locRes>0){
                   json.put("success",true);
                   json.put("msg","定位更新成功");
               }else{
                   json.put("success",false);
                   json.put("msg","定位更新失败");
               }

        }else{
            json.put("success",false);
            json.put("msg","session不存在");
        }


        String map= JSONObject.toJSONString(json);
        //System.out.print(map);
        return map;


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

        Order order = null;
        if (username != null && accountType != null) {
            if (accountType.equals("person_user")) {
                order = orderDao.getCurrentPersonUserOrderByUsername(username);
            }
            if (order != null) {
                AliPayService alipay=new AliPayService();
                order.setService_items("test order");
                String orderInfo=alipay.getOrderInfoByAliPay(order.getOrder_no(),order.getService_items(),order.getService_fee());
                json.put("orderInfo", orderInfo);
                json.put("success", true);
                json.put("msg", "存在支付信息");
            } else {
                json.put("success", false);
                json.put("accountType", accountType);
                json.put("msg", "不存在未完成的Order");
            }
        } else {
            json.put("success", false);
            json.put("relogin", true);
            json.put("msg", "session不存在，重新登录");
        }

        String map= JSONObject.toJSONString(json);
        System.out.print(map);
        return map;

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


        AliPayService alipay=new AliPayService();
        boolean verified=alipay.verify(content,sign);
        boolean success=false;
//        boolean flag = AlipaySignature.rsaCheckV1(params, alipaypublicKey, charset,"RSA2")
        JSONObject json = new JSONObject();
        Order order=null;
        if (verified&&out_trade_no!=null&&trade_status!=null) {
            System.out.println("sign verified!!!!");
            if(trade_status.equals("TRADE_SUCCESS")||trade_status.equals("TRADE_FINISHED")){
                List<Order> list = orderDao.getOrderByOrderNo(out_trade_no);
                if (list.size()>0) {
                    order=list.get(0);
                    order.setPay_method("alipay");
                    order.setPayment_status(trade_status);
                    order.setTrade_no(trade_no);
                    if(order.getOrder_status().equals("confirmed"))
                        order.setOrder_status("confirmedpaid");
                    int res=orderDao.updateOrder(order);
                    if(res>0){
//                        json.put("success", true);
//                        json.put("msg", "订单支付成功");
                        success=true;
                    }else{
                        success=false;
                        json.put("success", false);
                        json.put("msg", "系统错误，订单支付数据更新失败");
                    }

                } else {
                    success=false;
                    json.put("success", false);
                    json.put("msg", "订单支付数据更新失败");
                }
            }else{
                success=false;
                json.put("success", false);
                json.put("msg", "订单支付失败 trade_status="+trade_status);
            }


        }else{
            success=false;
            System.out.println("sign failed verfification!");
        }

        String map= JSONObject.toJSONString(json);
        System.out.print(map);

        if(success){
            SendPushNotification(request,json,order.getOrder_no(),"order_"+order.getOrder_status());
            return "success";
        }else{
            return "fail";
        }
        //return map;

    }

}
