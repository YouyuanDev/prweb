package com.prweb.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.prweb.dao.AccountDao;
import com.prweb.dao.CompanyDao;
import com.prweb.dao.OrderDao;
import com.prweb.dao.PersonUserDao;
import com.prweb.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class PersonUserServiceImpl implements PersonUserService{
    @Autowired
    CompanyDao companyDao;

    @Autowired
    PersonUserDao personUserDao;

    @Autowired
    private OrderDao orderDao;


    @Autowired
    private AccountDao accountDao;


    @Autowired
    private PushNotificationService pushNotificationService;

    @Autowired
    private ServiceTypeService serviceTypeService;

    @Transactional
    @Override
    public String confirmOrderFinish(String username, String accountType) throws  RuntimeException {
        System.out.println("username="+username);
        JSONObject json = new JSONObject();
                Order order = null;
        if (username != null && accountType != null) {
            if (accountType.equals("person_user")) {
                order = orderDao.getCurrentPersonUserOrderByUsername(username);
            }
            if (order != null&&order.getOrder_status().equals("finished")) {
                String company_user_no=order.getCompany_user_no();
//                String payee_account="";
//                String payee_real_name="";
                String amount="0.0";
                float f_amount=0;
                f_amount=order.getService_fee();
                amount=String.valueOf(f_amount);
                String remark="熊猫救援服务费清算";
                if(company_user_no!=null){
                    List<Company> cmplist=companyDao.getCompanyByCompanyUserNo(company_user_no);
                    if(cmplist.size()>0){
//                        payee_account=cmplist.get(0).getAlipay_payee_account();
//                        payee_real_name=cmplist.get(0).getAlipay_payee_real_name();

                        order.setOrder_status("finishedconfirmed");
                        //order.setOrder_fund_transfer_method("fund");
                        int res=orderDao.updateOrder(order);
                        if(res>0){
                            Company cmp=cmplist.get(0);
                            cmp.setPr_account_amount(cmp.getPr_account_amount()+f_amount);
                            int count=companyDao.updateCompany(cmp);
                            if(count>0){
                                json.put("success", true);
                                json.put("msg", "订单确认完成成功！");
                            }else{
                                throw new RuntimeException("向商户转账时出现异常");
                            }
                        }else{
                            json.put("success", false);
                            json.put("msg", "订单保存失败,订单确认完成失败！");
                        }

                    }
                }

                    //支付开始
//                AliPayService alipay=new AliPayService();
//                AlipayFundTransToaccountTransferResponse response=alipay.transferOrderPaymentToComanyAccount(order.getOrder_no(),payee_account,payee_real_name,amount,remark);
//                if(response.isSuccess()){
//                    order.setOrder_status("finishedconfirmed");
//                    order.setOrder_fund_transfer_method("alipay");
//                    order.setOrder_fund_transfer_status(response.getCode());
//                    order.setAlipay_fund_order_id(response.getOrderId());
//                    order.setAlipay_out_biz_no(response.getOutBizNo());
//                    order.setAlipay_fund_transfer_time(new Date());
//                    System.out.println("getCode="+response.getCode());
//                    System.out.println("getSubMsg="+response.getSubMsg());
//                    int res=orderDao.updateOrder(order);
//                    if(res>0){
//                        json.put("success", true);
//                        json.put("msg", "转账成功,订单确认完成成功！");
//                        SendPushNotification(request,json,order.getOrder_no(),"order_"+order.getOrder_status());
//                    }else{
//                        json.put("success", false);
//                        json.put("msg", "转账成功,订单确认完成失败..");
//                    }
//                }else{
//                    json.put("success", false);
//                    json.put("msg", "转账失败，"+response.getSubMsg());
//                }

            } else {
                json.put("success", false);
                json.put("accountType", accountType);
                json.put("msg", "不存在需要确认的订单");
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

    public String cancelVerifyPersonUserInfo(String username, String accountType){
        System.out.println("username="+username);
        JSONObject json = new JSONObject();
        if(username==null||accountType==null){
            json.put("success",false);
            json.put("relogin",true);
            json.put("message","session不存在，请登录");
        }
        else if(accountType.equals("person_user")){
            List<PersonUser> list=personUserDao.getPersonUserByUsername(username);
            if(list.size()>0){
                PersonUser pu=list.get(0);
                if(pu.getIs_verified().equals("2")){
                    pu.setIs_verified("0");
                    int res=personUserDao.updatePersonUser(pu);
                    if(res>0){
                        json.put("success",true);
                        json.put("message","个人认证撤销成功");
                    }else{
                        json.put("success",false);
                        json.put("message","系统错误");
                    }

                }else{
                    json.put("success",false);
                    json.put("message","个人认证撤销失败，没有审核中的认证");
                }

            }else{
                json.put("success",false);
                json.put("message","不存在该用户名，认证撤销失败");
            }
        }else{
            json.put("success", false);
            json.put("accountType", accountType);
            json.put("msg", "账户类型不为个人账户，请切换至个人账户");
        }

        String mmp= JSONArray.toJSONString(json);
        System.out.print("mmp:"+mmp);
        return mmp;

    }



    public String verifyPersonUserInfo(String username, String accountType,String id_card_picture_back, String id_card_picture_front){
        System.out.println("username="+username);
        JSONObject json = new JSONObject();
        if(username==null||accountType==null){
            json.put("success",false);
            json.put("relogin",true);
            json.put("message","session不存在，请登录");
        }
        else if(id_card_picture_back==null||id_card_picture_front==null){
            json.put("success",false);
            json.put("message","不存在身份证照片，认证提交失败");
        }else if(accountType.equals("person_user")){
            List<PersonUser> list=personUserDao.getPersonUserByUsername(username);
            if(list.size()>0){
                PersonUser pu=list.get(0);
                if(pu.getIs_verified().equals("0")||pu.getIs_verified().equals("3")){
                    pu.setId_card_picture_front(id_card_picture_front);
                    pu.setId_card_picture_back(id_card_picture_back);
                    pu.setIs_verified("2");
                    int res=personUserDao.updatePersonUser(pu);
                    if(res>0){
                        json.put("success",true);
                        json.put("message","个人认证提交成功");
                    }else{
                        json.put("success",false);
                        json.put("message","系统错误");
                    }

                }else{
                    json.put("success",false);
                    json.put("message","个人认证提交失败，账户已认证或正在认证审核中");
                }

            }else{
                json.put("success",false);
                json.put("message","不存在该用户名，认证提交失败");
            }
        }else{
            json.put("success", false);
            json.put("accountType", accountType);
            json.put("msg", "账户类型不为个人账户，请切换至个人账户");
        }

        String mmp= JSONArray.toJSONString(json);
        System.out.print("mmp:"+mmp);
        return mmp;
    }



    public String savePersonUserInfo(PersonUser personUser){
        JSONObject json = new JSONObject();
        int resTotal=0;
        if(personUser.getId()==0){
            //添加
            resTotal=personUserDao.addPersonUser(personUser);

        }else{
            //修改！
            resTotal=personUserDao.updatePersonUser(personUser);
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



    public String PersonUserSubmitPendingOrder(String username,String accountType,Order order,String basePath){
        JSONObject json = new JSONObject();

        if(accountType==null||accountType.equals("")){
            json.put("success",false);
            json.put("relogin",true);
            json.put("message","不存在session，重新登录");
        }
        else{

            if(accountType.equals("person_user")){
                int resTotal=0;
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
                        order.setOrder_no("OR"+uuuid.toLowerCase());
                        //设置order状态
                        if(order.getOrder_status()==null)
                            order.setOrder_status("pending");
                        //计算服务费
                        String service_type_code=order.getService_type_code();
                        ServiceType st=serviceTypeService.getServiceRateByServiceCode(service_type_code);
                        if(st!=null){
                            order.setService_fee(st.getService_rate());
                        }

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
                    pushNotificationService.SendPushNotification(basePath,json,order.getOrder_no(),"order_"+order.getOrder_status());

                }else{
                    json.put("success",false);
                    json.put("message","订单生成失败");
                }
            }else{
                json.put("success",false);
                json.put("message","订单生成失败,accountType为"+accountType);
            }

        }
        String mmp= JSONArray.toJSONString(json);
        System.out.print("mmp:"+mmp);
        return mmp;
    }


    public String getPersonUserInfo(String username){
        JSONObject json = new JSONObject();
        List<PersonUser> list=personUserDao.getPersonUserByUsername(username);
        if(list.size()>0){
            PersonUser personuser=list.get(0);
            json.put("success",true);
            json.put("personUser",personuser);
            json.put("message","获取个人信息成功");

        }else{
            json.put("success",false);
            json.put("message","获取个人信息失败");

        }

        String mmp= JSONArray.toJSONString(json);
        System.out.print("mmp:"+mmp);
        return mmp;
    }


    public String PersonUserCancelOrder(String username,String accountType,String basePath){
        JSONObject json = new JSONObject();
        if(accountType==null||accountType.equals("")){
            json.put("success",false);
            json.put("relogin",true);
            json.put("message","不存在session，重新登录");
        }
        else{
            if(accountType.equals("person_user")){
                //先判断person_user下是否有未完成的order
                int resTotal=0;
                Order order=orderDao.getCurrentPersonUserOrderByUsername(username);
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
        String mmp= JSONArray.toJSONString(json);
        System.out.print("mmp:"+mmp);
        return mmp;
    }


    public String getNearByCompany(String lon,String lat,int start, int rows){
        List<HashMap<String,Object>> list=companyDao.getNearByCompany(lon,lat,start,rows);
        int count = companyDao.getCountNearByCompany(lon,lat);
        Map<String, Object> maps = new HashMap<String, Object>();
        maps.put("total", count);
        maps.put("rows", list);
        //System.out.println("rrrrrrrrrrrow="+count);

        String map= JSONObject.toJSONString(maps);
        return map;
    }
}
