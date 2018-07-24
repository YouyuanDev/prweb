package com.prweb.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


import com.prweb.dao.AccountDao;
import com.prweb.dao.OrderDao;
import com.prweb.dao.OrderStatusDao;
import com.prweb.entity.Account;
import com.prweb.entity.Order;
import com.prweb.entity.OrderStatus;
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
    public String getOrderByOrderNo(@RequestParam(value = "business_no",required = false)String business_no, HttpServletRequest request){
        List<Order> list=orderDao.getOrderByOrderNo(business_no);
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

        try{
            int resTotal=0;
            if(order.getOrder_time()==null){
                order.setOrder_time(new Date());
            }

            if(order.getId()==0){
                //添加
                //设置orderno
                String uuuid=UUID.randomUUID().toString();
                uuuid=uuuid.replace("-","");
                order.setOrder_no("OR"+uuuid);
                //设置order状态
                order.setOrder_status("pending");

                if(username!=null){
                    List<Account> accountlist=accountDao.getAccountByUserName(username);
                    if(accountlist.size()>0){
                        order.setPerson_user_no(accountlist.get(0).getPerson_user_no());
                    }
                }


                resTotal=orderDao.addOrder(order);

            }else{
                //修改！

                resTotal=orderDao.updateOrder(order);
            }
            if(resTotal>0){
                String order_status=order.getOrder_status();
                if(order_status!=null){
                    List<OrderStatus> oslist= orderStatusDao.getOrderStatusByCode(order_status);
                    if(oslist.size()>0){
                        json.put("OrderStatus",oslist.get(0));
                    }
                }

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

}
