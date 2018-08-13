package com.prweb.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.prweb.dao.AccountDao;
import com.prweb.dao.CompanyDao;
import com.prweb.dao.CompanyUserDao;
import com.prweb.dao.OrderDao;
import com.prweb.entity.Account;
import com.prweb.entity.Company;
import com.prweb.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class CompanyUserServiceImpl implements CompanyUserService{

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private CompanyUserDao companyUserDao;

    @Autowired
    private CompanyDao companyDao;



    @Autowired
    private PushNotificationService pushNotificationService;


    public String getNearByPendingOrders(String lon,String lat,int start, int rows){


        List<HashMap<String,Object>> list=orderDao.getNearByPendingOrders(lon,lat, start, rows);
        int count = orderDao.getCountNearByPendingOrders(lon,lat);
        Map<String, Object> maps = new HashMap<String, Object>();
        maps.put("total", count);
        maps.put("rows", list);
        //System.out.println("rrrrrrrrrrrow="+count);

        String map= JSONObject.toJSONString(maps);
        return map;
    }

    public String CompanyUserCancelOrder(String username,String accountType,String basePath){
        JSONObject json = new JSONObject();
        if(accountType==null||accountType.equals("")){
            json.put("success",false);
            json.put("relogin",true);
            json.put("message","不存在session，重新登录");
        }
        else{

            if(accountType.equals("company_user")){
                //先判断company_user下是否有未完成的order
                int resTotal=0;
                Order order = orderDao.getCurrentOrderCompanyUserByUsername(username);
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
                    pushNotificationService.SendPushNotification(basePath,json,order.getOrder_no(),"order_"+order.getOrder_status());

                }else{
                    json.put("success",false);
                    json.put("message","订单取消失败");
                }
            }else{
                json.put("success",false);
                json.put("message","订单取消失败,accountType为"+accountType);
            }

        }

        String map= JSONObject.toJSONString(json);
        System.out.print(map);
        return map;
    }


    public String CompanyUserFinishService(String username,String accountType,String basePath){
        JSONObject json = new JSONObject();
        if(accountType==null||accountType.equals("")){
            json.put("success",false);
            json.put("relogin",true);
            json.put("message","不存在session，重新登录");
        }
        else{
            if(accountType.equals("company_user")){
                int resTotal=0;
                //先判断company_user下是否有未完成的order
                Order order = orderDao.getCurrentOrderCompanyUserByUsername(username);
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
                    pushNotificationService.SendPushNotification(basePath,json,order.getOrder_no(),"order_"+order.getOrder_status());

                }else{
                    json.put("success",false);
                    json.put("message","订单服务完成失败");
                }
            }else{
                json.put("success",false);
                json.put("message","订单服务完成失败,accountType为"+accountType);
            }

        }
        String map= JSONObject.toJSONString(json);
        System.out.print(map);
        return map;
    }

    public String CompanyUserStartService(String username,String accountType,String basePath){
        JSONObject json = new JSONObject();
        if(accountType==null||accountType.equals("")){
            json.put("success",false);
            json.put("relogin",true);
            json.put("message","不存在session，重新登录");
        }
        else{

            if(accountType.equals("company_user")){
                int resTotal=0;
                //先判断company_user下是否有未完成的order
                Order order = orderDao.getCurrentOrderCompanyUserByUsername(username);
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
                    pushNotificationService.SendPushNotification(basePath,json,order.getOrder_no(),"order_"+order.getOrder_status());

                }else{
                    json.put("success",false);
                    json.put("message","订单服务开始失败");
                }
            }else{
                json.put("success",false);
                json.put("message","订单服务开始失败,accountType为"+accountType);
            }

        }
        String map= JSONObject.toJSONString(json);
        System.out.print(map);
        return map;
    }

    public String CompanyUserAcceptPendingOrder(String username,String accountType,String order_no,String basePath){
        JSONObject json = new JSONObject();
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
                        Order order=orderlt.get(0);
                        //设置order状态
                        order.setOrder_status("confirmed");
                        if(username!=null){
                            List<Account> accountlist=accountDao.getAccountByUserName(username);
                            if(accountlist.size()>0){
                                order.setCompany_user_no(accountlist.get(0).getCompany_user_no());
                                int resTotal=orderDao.updateOrder(order);
                                if(resTotal>0){
                                    json.put("success",true);
                                    json.put("OrderNo",order.getOrder_no());
                                    json.put("message","订单接受成功");
                                    pushNotificationService.SendPushNotification(basePath,json,order.getOrder_no(),"order_"+order.getOrder_status());

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
        String map= JSONObject.toJSONString(json);
        System.out.print(map);
        return map;
    }

    public String getCompanyUserInfo(String username){
        JSONObject json = new JSONObject();
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


    public String saveCompanyUserInfo(Company company){
        int resTotal=0;
        JSONObject json=new JSONObject();
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
        String mmp= JSONArray.toJSONString(json);
        System.out.print("mmp:"+mmp);
        return mmp;
    }



    public String cancelVerifyCompanyUserInfo(String username,String accountType){
        JSONObject json=new JSONObject();
        if(username==null){
            json.put("success",false);
            json.put("relogin",true);
            json.put("message","session不存在，请登录");
        }
        else if(accountType.equals("company_user")){
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
        }else{
            json.put("success", false);
            json.put("accountType", accountType);
            json.put("msg", "账户类型不为商户账户，请切换至商户账户");
        }

        String mmp= JSONArray.toJSONString(json);
        System.out.print("mmp:"+mmp);
        return mmp;



    }

    public String verifyCompanyUserInfo(String username,String accountType,String id_card_picture_front,String id_card_picture_back,String business_certificate_picture){
        JSONObject json=new JSONObject();
        if(username==null){
            json.put("success",false);
            json.put("relogin",true);
            json.put("message","session不存在，请登录");
        }
        else if(id_card_picture_back==null||id_card_picture_front==null||business_certificate_picture==null){
            json.put("success",false);
            json.put("message","身份证照片或营业执照照片不存在，认证提交失败");
        }else if(accountType.equals("company_user")){
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
        }else{
            json.put("success", false);
            json.put("accountType", accountType);
            json.put("msg", "账户类型不为商户账户，请切换至商户账户");
        }

        String mmp= JSONArray.toJSONString(json);
        System.out.print("mmp:"+mmp);
        return mmp;
    }
}
