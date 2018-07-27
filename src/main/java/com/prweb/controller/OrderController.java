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


    //APP使用 获取当前订单
    @RequestMapping(value = "getCurrentOrder",produces = "text/plain;charset=utf-8")
    @ResponseBody
    public String getCurrentOrder( HttpServletRequest request){

        JSONObject json = new JSONObject();
        //返回用户session数据
        HttpSession session = request.getSession();
        //把用户数据保存在session域对象中
        String username = (String) session.getAttribute("userSession");
        String accountType = (String) session.getAttribute("accountType");

        Order order=null;
        if(username!=null&&accountType!=null){
            if(accountType.equals("person_user")) {
                order = orderDao.getCurrentPersonUserOrderByUsername(username);
            }
            else if(accountType.equals("company_user")){
                order = orderDao.getCurrentOrderCompanyUserByUsername(username);
            }
            if(order!=null){
                json.put("success",true);
                json.put("order_no",order.getOrder_no());
                json.put("accountType",accountType);
                json.put("msg","存在Order");
            }else{
                json.put("success",false);
                json.put("msg","不存在进行中的Order");
            }
        }else{
            json.put("success",false);
            json.put("relogin",true);
            json.put("msg","session不存在，重新登录");
        }


        String mmp= JSONArray.toJSONString(json);
        System.out.println(mmp);
        return mmp;
    }


    //发送推送消息
    public void SendEventToRoles(String basePath,String event, String title,String content){
        List<HashMap<String,Object>>  lt=roleDao.getRolesByEvent(event);

        for(int i=0;i<lt.size();i++){
            String role=(String)lt.get(i).get("role_no");
            //发消息
            APICloudPushService.SendPushNotification(basePath,title,content,"1","0",role,"");
        }


    }

    //发送推送消息 accounts  phone ,分隔
    public void SendPushNotificationToAccounts(String basePath,String event, String title,String content,String userIds){


        //发消息
        APICloudPushService.SendPushNotification(basePath,title,content,"1","0","",userIds);



    }



    //搜索
    @RequestMapping(value = "getOrderByLike",produces = "text/plain;charset=utf-8")
    @ResponseBody
    public String getOrderByLike(@RequestParam(value = "order_no",required = false)String order_no, @RequestParam(value = "order_status",required = false)String order_status, @RequestParam(value = "begin_time",required = false)String begin_time, @RequestParam(value = "end_time",required = false)String end_time, HttpServletRequest request){
        String page= request.getParameter("page");
        String rows= request.getParameter("rows");
        if(page==null){
            page="1";
        }
        if(rows==null){
            rows="20";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date beginTime=null;
        Date endTime=null;

        try{
            if(begin_time!=null&&begin_time!=""){
                beginTime=sdf.parse(begin_time);
                System.out.println(beginTime.toString());
            }
            if(end_time!=null&&end_time!=""){
                endTime=sdf.parse(end_time);
                System.out.println(endTime.toString());
            }
        }catch (Exception e){
            e.printStackTrace();
        }


        int start=(Integer.parseInt(page)-1)*Integer.parseInt(rows);
        List<HashMap<String,Object>> list=orderDao.getAllByLike(order_no,order_status,beginTime,endTime,start,Integer.parseInt(rows));
        int count=orderDao.getCountAllByLike(order_no,order_status,beginTime,endTime);
        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",list);
        //System.out.println("rrrrrrrrrrrow="+count);
        String mmp= JSONArray.toJSONString(maps);
        //System.out.print("mmp:"+mmp);
        return mmp;

    }
    @RequestMapping(value = "getOrderByOrderNo",produces = "text/plain;charset=utf-8")
    @ResponseBody
    public String getOrderByOrderNo(@RequestParam(value = "order_no",required = false)String order_no, HttpServletRequest request){
        List<Order> list=orderDao.getOrderByOrderNo(order_no);
        String mmp= JSONArray.toJSONString(list);
        System.out.println(mmp);
        return mmp;
    }

    @RequestMapping(value = "getOrderMapByOrderNo",produces = "text/plain;charset=utf-8")
    @ResponseBody
    public String getOrderMapByOrderNo(@RequestParam(value = "order_no",required = false)String order_no, HttpServletRequest request){
        List<HashMap<String,Object>> list=orderDao.getOrderMapByOrderNo(order_no);
        String mmp= JSONArray.toJSONString(list);
        System.out.println(mmp);
        return mmp;
    }





    //保存Order
    @RequestMapping(value = "/saveOrder")
    @ResponseBody
    public String saveOrder(Order order,HttpServletRequest request, HttpServletResponse response){
        System.out.print("saveOrder");
        JSONObject json=new JSONObject();
        //返回用户session数据
        HttpSession session = request.getSession();
        //把用户数据保存在session域对象中
        String username=(String)session.getAttribute("userSession");
        int resTotal=0;
        try{

            if(order.getOrder_time()==null){
                order.setOrder_time(new Date());
            }

            if(order.getId()==0){

                //先判断person_user下是否有未完成的order

                Order currentorder = orderDao.getCurrentPersonUserOrderByUsername(username);
                if(currentorder==null){
                    //添加
                    //设置orderno
                    String uuuid=UUID.randomUUID().toString();
                    uuuid=uuuid.replace("-","");
                    order.setOrder_no("OR"+uuuid);
                    //设置order状态
                    if(order.getOrder_status()==null)
                        order.setOrder_status("pending");

                    if(order.getPerson_user_no()==null&&username!=null){
                        List<Account> accountlist=accountDao.getAccountByUserName(username);
                        if(accountlist.size()>0){
                            order.setPerson_user_no(accountlist.get(0).getPerson_user_no());
                        }
                    }
                    resTotal=orderDao.addOrder(order);
                }else{
                    json.put("success",false);
                    json.put("message","存在未完成的订单");
                }

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
                    String basePath = request.getSession().getServletContext().getRealPath("/");
                    if(basePath.lastIndexOf('/')==-1){
                        basePath=basePath.replace('\\','/');
                    }

                    String jsonstr= JSONArray.toJSONString(json);
                    //查找订单的两个有关人员电话
                    List<HashMap<String,Object>> lt=orderDao.getPushPhoneNosByOrderNo(order.getOrder_no());
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
                        SendPushNotificationToAccounts(basePath,"order_submit","订单:"+order.getOrder_no(),jsonstr,userIds);
                    }

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

    //更新个人location到历史location，轨迹保存
    @RequestMapping(value = "/updateLocation")
    @ResponseBody
    public String updateLocation(HttpServletRequest request, HttpServletResponse response) {
        System.out.print("updateLocation");
        String order_no= request.getParameter("order_no");
        String my_location= request.getParameter("my_location");

        JSONObject json = new JSONObject();
        //返回用户session数据
        HttpSession session = request.getSession();
        //把用户数据保存在session域对象中
        String username = (String) session.getAttribute("userSession");
        String accountType = (String) session.getAttribute("accountType");


        if(username!=null&&accountType!=null&&my_location!=null){

            if(order_no!=null) {
                List<Order> orderList = orderDao.getOrderByOrderNo(order_no);
                if (orderList.size() > 0) {
                    Order order = orderList.get(0);
                    if (accountType.equals("person_user")) {
                        order.setPerson_user_location(my_location);
                    } else if (accountType.equals("company_user")) {
                        order.setCompany_user_location(my_location);
                    }
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
        System.out.print("map");
        return map;


    }


}
