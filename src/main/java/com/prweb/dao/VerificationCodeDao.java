package com.prweb.dao;


import com.prweb.entity.VerificationCode;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VerificationCodeDao {


    //该手机号是否可以再次发送验证码
    public int CanResendVerificationCode(@Param("cell_phone_no")String cell_phone_no);


    //验证码是否有效 5分钟以内有效
    public int IsVerificationCodeValid(@Param("cell_phone_no")String cell_phone_no,@Param("verification_code")String verification_code);

    //VerificationCode
    public int addVerificationCode(VerificationCode verificationCode);
    //删除VerificationCode
    public int delVerificationCode(String[]arrId);


    public int delVerificationCodeByCellPhoneNo(@Param("cell_phone_no")String cell_phone_no);
}
