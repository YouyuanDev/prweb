package com.prweb.service;


import com.prweb.entity.FundTransferRecord;

public interface FundTransferRecordService {


    //公司账户提现
    public String withdrawDeposit(String basePath,String username, String amount,String remark);

    //分页搜索
    public String getFundTransferRecordByLike(String transfer_no,String company_no,int start,int rows);

//    //保存 FundTransferRecord
//    public String saveFundTransferRecord(FundTransferRecord fundTransferRecord);
//
//    //删除FundTransferRecord信息
//    public String delFundTransferRecord(String hlparam);

}
