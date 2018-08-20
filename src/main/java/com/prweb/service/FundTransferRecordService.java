package com.prweb.service;

public interface FundTransferRecordService {


    //公司账户提现
    public String withdrawDeposit(String basePath,String username, String amount,String remark);

}
