package com.prweb.dao;


import com.prweb.entity.VerificationCode;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VerificationCodeDao {


    public int IsVerificationCodeValid(@Param("cell_phone_no")String cell_phone_no,@Param("verification_code")String verification_code);

    //VerificationCode
    public int addVerificationCode(VerificationCode verificationCode);
    //删除VerificationCode
    public int delVerificationCode(String[]arrId);


    public int delVerificationCodeByCellPhoneNo(@Param("cell_phone_no")String cell_phone_no);
}
