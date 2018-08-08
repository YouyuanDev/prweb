package com.prweb.service;

import com.alibaba.fastjson.JSONObject;
import com.prweb.dao.AccountDao;
import com.prweb.dao.CompanyDao;
import com.prweb.dao.OrderDao;
import com.prweb.dao.PersonUserDao;
import com.prweb.entity.Company;
import com.prweb.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

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
            if (order != null&&order.getOrder_status().equals("finished")&&
                    (order.getOrder_fund_transfer_method()==null||order.getOrder_fund_transfer_method().equals(""))) {
                String company_user_no=order.getCompany_user_no();
                String payee_account="";
                String payee_real_name="";
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
                        order.setOrder_fund_transfer_method("fund");
                        int res=orderDao.updateOrder(order);
                        if(res>0){
                            Company cmp=cmplist.get(0);
                            cmp.setPr_account_amount(cmp.getPr_account_amount()+f_amount);
                            int count=companyDao.updateCompany(cmp);
                            count=0;
                            if(count>0){
                                json.put("success", true);
                                json.put("msg", "订单确认完成成功！");
                            }else{
                                json.put("success", false);
                                json.put("msg", "商户余额修改失败,订单确认完成失败！");
                                throw new RuntimeException("手动模拟转账时出现异常");
                                //throw new RuntimeSqlException("手动模拟转账时出现异常");
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

}
