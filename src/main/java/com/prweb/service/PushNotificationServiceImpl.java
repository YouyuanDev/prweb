package com.prweb.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.prweb.dao.CompanyDao;
import com.prweb.dao.OrderDao;
import com.prweb.entity.Order;
import com.prweb.util.APICloudPushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;


@Service
public class PushNotificationServiceImpl implements PushNotificationService{


    @Autowired
    private OrderDao orderDao;


    @Autowired
    CompanyDao companyDao;


    //发送推送消息 accounts  phone ,分隔
    public void SendPushNotificationToAccounts(String basePath, String event, String title, String content, String userIds) {
        //发消息
        APICloudPushService.SendPushNotification(basePath, title, content, "1", "0", "", userIds);
    }


    //发送推送给相关人员
    public void SendPushNotification(String basePath, JSONObject json, String orderNo, String event){


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

}
