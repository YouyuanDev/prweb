package com.prweb.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.prweb.dao.AccountDao;
import com.prweb.dao.LocationDao;
import com.prweb.dao.OrderDao;
import com.prweb.dao.OrderStatusDao;
import com.prweb.entity.Location;
import com.prweb.entity.Order;
import com.prweb.entity.OrderStatus;
import com.prweb.util.AliPayService;
import com.prweb.util.ComboxItem;
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

    public String getAllOrderStatus(){
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

    public String getAllOrderStatusForAPP(){
        List<OrderStatus> list=orderStatusDao.getAllOrderStatus();
        String mmp= JSONArray.toJSONString(list);
        return mmp;
    }

    public String getPersonUserAndCompanyUserCurrentLocation(String username,String accountType){
        Order order = null;
        JSONObject json=new JSONObject();
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


    public String updateLocation(String username,String accountType,String order_no,String my_location){
        JSONObject json=new JSONObject();
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


    public String getCurrentOrderAliPayInfo(String username,String accountType){
        Order order = null;
        JSONObject json=new JSONObject();
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


    public String AliPayNotify(String content,String sign,String out_trade_no,String trade_status,String trade_no,String basePath){
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
            pushNotificationService.SendPushNotification(basePath,json,order.getOrder_no(),"order_"+order.getOrder_status());
            return "success";
        }else{
            return "fail";
        }

    }

}
