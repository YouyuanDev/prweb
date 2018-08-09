package com.prweb.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.prweb.dao.AccountDao;
import com.prweb.dao.LocationDao;
import com.prweb.dao.OrderDao;
import com.prweb.dao.OrderStatusDao;
import com.prweb.entity.Order;
import com.prweb.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private OrderStatusDao orderStatusDao;

    @Autowired
    private LocationDao locationDao;

    @Autowired
    private PushNotificationService pushNotificationService;



    public String getAllOrderList(String username,String accountType,String page, String rows ){
        JSONObject json = new JSONObject();
        List<Order> orderList = null;
        if (username != null && accountType != null) {
            if(page!=null){
                if (rows == null) {
                    rows = "20";
                }
                int start = (Integer.parseInt(page) - 1) * Integer.parseInt(rows);
                if (accountType.equals("person_user")) {
                    orderList = orderDao.getAllPersonUserOrderByUsername(username,start,Integer.parseInt(rows));
                } else if (accountType.equals("company_user")) {
                    orderList = orderDao.getAllCompanyUserOrderByUsername(username,start,Integer.parseInt(rows));
                }
                if (orderList != null&&orderList.size()>0) {
                    json.put("success", true);
                    json.put("orderList", orderList);
                    json.put("accountType", accountType);
                    json.put("msg", "订单加载成功!");
                } else {
                    json.put("success", false);
                    json.put("accountType", accountType);
                    json.put("msg", "订单加载完毕!");
                }
            }else{
                json.put("success", false);
                json.put("accountType", accountType);
                json.put("msg", "订单加载完毕!");
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

    public String getCurrentOrder(String username,String accountType){
        Order order = null;
        JSONObject json = new JSONObject();
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


    public String getOrderByLike(String order_no, String order_status, Date beginTime, Date endTime, int start, int rows){
        List<HashMap<String, Object>> list = orderDao.getAllByLike(order_no, order_status, beginTime, endTime, start, rows);
        int count = orderDao.getCountAllByLike(order_no, order_status, beginTime, endTime);
        Map<String, Object> maps = new HashMap<String, Object>();
        maps.put("total", count);
        maps.put("rows", list);
        //System.out.println("rrrrrrrrrrrow="+count);
        String mmp = JSONArray.toJSONString(maps);
        //System.out.print("mmp:"+mmp);
        return mmp;
    }

    public String getOrderByOrderNo(String order_no){
        List<Order> list = orderDao.getOrderByOrderNo(order_no);
        String mmp = JSONArray.toJSONString(list);
        System.out.println(mmp);
        return mmp;
    }

    public String getOrderMapByOrderNo(String order_no){
        List<HashMap<String, Object>> list = orderDao.getOrderMapByOrderNo(order_no);
        String mmp = JSONArray.toJSONString(list);
        System.out.println(mmp);
        return mmp;
    }

    public String saveOrder(Order order,String basePath){
        int resTotal=0;
        JSONObject json = new JSONObject();
        if(order.getOrder_time()==null){
            order.setOrder_time(new Date());
        }
        if(order.getId()==0){
            String uuuid= UUID.randomUUID().toString();
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
            pushNotificationService.SendPushNotification(basePath,json,order.getOrder_no(),"order_"+order.getOrder_status());

        }else{
            json.put("success",false);
            json.put("message","保存失败");
        }
        String mmp = JSONArray.toJSONString(json);
        System.out.println(mmp);
        return mmp;
    }

    public String delOrder(String hlparam){
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
        String mmp = JSONArray.toJSONString(json);
        System.out.println(mmp);
        return mmp;
    }
}
