package com.prweb.dao;


import com.prweb.entity.FundTransferRecord;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

public interface FundTransferRecordDao {

    //模糊搜索带分页
    public List<HashMap<String,Object>> getAllByLike(@Param("transfer_no")String transfer_no, @Param("company_no")String company_no, @Param("skip")int skip, @Param("take")int take);

    //模糊搜索总数
    public int getCountAllByLike(@Param("transfer_no")String transfer_no, @Param("company_no")String company_no);

    //修改 FundTransferRecord
    public int updateFundTransferRecord(FundTransferRecord fundTransferRecord);
    //增加 FundTransferRecord
    public int addFundTransferRecord(FundTransferRecord fundTransferRecord);
    //删除 FundTransferRecord
    public int delFundTransferRecord(String[]arrId);

    //根据transfer_no得到FundTransferRecord
    public List<FundTransferRecord> getFundTransferRecordByTransferNo(@Param("transfer_no")String transfer_no);
}
