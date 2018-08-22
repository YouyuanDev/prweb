package com.prweb.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.prweb.dao.CompanyDao;
import com.prweb.dao.FundTransferRecordDao;
import com.prweb.entity.Company;
import com.prweb.entity.FundTransferRecord;
import com.prweb.util.AliPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class FundTransferRecordServiceImpl implements FundTransferRecordService{

    @Autowired
    private FundTransferRecordDao fundTransferRecordDao;

    @Autowired
    private CompanyDao companyDao;


    @Autowired
    private CompanyUserService companyUserService;

    @Autowired
    private PushNotificationService pushNotificationService;

    @Transactional
    @Override
    public String withdrawDeposit(String basePath,String username, String amount,String remark)  throws  RuntimeException{
        JSONObject json=new JSONObject();
        Company company=companyUserService.getCompanyInfo(username);
        if(company!=null){
            float balance=company.getPr_account_amount();
            String payee_account=company.getAlipay_payee_account();
            String payee_real_name=company.getAlipay_payee_real_name();
            if(balance>=Float.parseFloat(amount)){
                //减去提现数额
                company.setPr_account_amount(balance-Float.parseFloat(amount));
                int count=companyDao.updateCompany(company);
                if(count>0){
                    //支付开始
                    AliPayService alipay=new AliPayService();
                    String orderid= UUID.randomUUID().toString();
                    orderid=orderid.replace("-","");
                    orderid="WD"+orderid;
                    AlipayFundTransToaccountTransferResponse response=alipay.transferOrderPaymentToComanyAccount(orderid,payee_account,payee_real_name,amount,remark);
                    if(response.isSuccess()){
                        FundTransferRecord record=new FundTransferRecord();
                        record.setId(0);
                        record.setFund_transfer_method("aliPay");
                        record.setTransfer_no(response.getOrderId());
                        record.setAlipay_fund_order_id(response.getOrderId());
                        record.setAlipay_out_biz_no(response.getOutBizNo());
                        record.setTransfer_status(response.getCode());
                        record.setTransfer_date(new Date());
                        record.setCompany_no(company.getCompany_no());
                        record.setSub_msg(response.getCode()+" "+response.getSubMsg());
                        record.setPayee_account(payee_account);
                        record.setPayee_real_name(payee_real_name);
                        record.setTransfer_amount(Float.parseFloat(amount));
                        System.out.println("getCode="+response.getCode());
                        System.out.println("getSubMsg="+response.getSubMsg());
                        int res=fundTransferRecordDao.addFundTransferRecord(record);
                        if(res>0){
                            json.put("success", true);
                            json.put("msg", "提现成功！");
                            pushNotificationService.SendWithdrawPushNotification(basePath,json,company.getCell_phone());
                        }else{
                            json.put("success", false);
                            json.put("msg", "提现成功,提现记录保存失败..");
                        }
                    }else{
                        json.put("success", false);
                        json.put("msg", "提现失败，"+response.getSubMsg());
                        throw new RuntimeException("商户提现时支付宝出现异常");
                    }
                }
            }else{
                json.put("success", false);
                json.put("msg", "提现失败,余额不足");
            }
        }
        String mmp= JSONArray.toJSONString(json);
        System.out.println(mmp);
        return mmp;
    }


    public String getFundTransferRecordByLike(String transfer_no,String company_no,int start,int rows){
        List<HashMap<String,Object>> list=fundTransferRecordDao.getAllByLike(transfer_no,company_no,start,rows);
        int count=fundTransferRecordDao.getCountAllByLike(transfer_no,company_no);
        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",list);
        String mmp= JSONArray.toJSONString(maps);
        //System.out.print("mmp:"+mmp);
        return mmp;
    }


//    public String saveFundTransferRecord(FundTransferRecord fundTransferRecord){
//        int resTotal=0;
//        JSONObject json=new JSONObject();
//        if(fundTransferRecord.getId()==0){
//            //添加
//            resTotal=fundTransferRecordDao.addFundTransferRecord(fundTransferRecord);
//        }else{
//            //修改！
//            resTotal=fundTransferRecordDao.updateFundTransferRecord(fundTransferRecord);
//        }
//        if(resTotal>0){
//            json.put("success",true);
//            json.put("message","保存成功");
//        }else{
//            json.put("success",false);
//            json.put("message","保存失败");
//        }
//        String map= JSONObject.toJSONString(json);
//        return map;
//    }
//
//    public String delFundTransferRecord(String hlparam){
//        String[]idArr=hlparam.split(",");
//        int resTotal=0;
//        resTotal=fundTransferRecordDao.delFundTransferRecord(idArr);
//        JSONObject json=new JSONObject();
//        StringBuilder sbmessage = new StringBuilder();
//        sbmessage.append("总共");
//        sbmessage.append(Integer.toString(resTotal));
//        sbmessage.append("项提现信息删除成功\n");
//        if(resTotal>0){
//            //System.out.print("删除成功");
//            json.put("success",true);
//        }else{
//            //System.out.print("删除失败");
//            json.put("success",false);
//        }
//        json.put("message",sbmessage.toString());
//        String map= JSONObject.toJSONString(json);
//        return map;
//    }

}
