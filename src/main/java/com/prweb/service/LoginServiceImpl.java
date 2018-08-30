package com.prweb.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.prweb.dao.*;
import com.prweb.entity.*;
import com.prweb.util.AliyunSMS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class LoginServiceImpl implements LoginService{

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private FunctionDao functionDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private VerificationCodeDao verificationCodeDao;

    public JSONObject getFunctionJson(String username){

        JSONObject json=new JSONObject();
        String msg="";
        //设置权限
        HashMap<String,Object> functionMap=new HashMap<String,Object>();
        //这里读取数据库设置所有权限
        String role_no_list=null;
        if(username!=null) {
            List<Account> lt=accountDao.getAccountByUserName(username);
            if(lt.size()>0) {
                Account account=lt.get(0);
                //账户类型转换
                String accountType=getCurrentOrderAccountType(username);
                if(accountType==null)
                    accountType="person_user";
                json.put("accountType",accountType);
                role_no_list=account.getRole_no_list();
                if(role_no_list!=null&&!role_no_list.equals("")){
                    role_no_list=role_no_list.replace(',',';');
                    String[] roles= role_no_list.split(";");
                    for(int i=0;i<roles.length;i++){
                        List<Role> rolelt=roleDao.getRoleByRoleNo(roles[i]);
                        //System.out.println("role ="+roles[i]);
                        if(rolelt.size()>0) {
                            Role role=rolelt.get(0);
                            String functionlist = role.getFunction_no_list();
                            if(functionlist!=null&&!functionlist.equals("")){
                                functionlist=functionlist.replace(',',';');
                                String[] func_no_s=functionlist.split(";");
                                for(int j=0;j<func_no_s.length;j++) {
                                    List<Function> funlst=functionDao.getFunctionByFunctionNo(func_no_s[j]);
                                    if(funlst.size()>0){
                                        //得到function
                                        Function f=funlst.get(0);
                                        String function_no=f.getFunction_no();
                                        String uris=f.getUri();
                                        functionMap.put(function_no,"1");
                                        String[] uriArray=uris.split(";");
                                        for(int n=0;n<uriArray.length;n++){
                                            functionMap.put(uriArray[n],"1");
                                            System.out.println("functionMap put="+function_no);
                                            System.out.println("uri put="+uriArray[n]);
                                        }

                                    }


                                }
                            }
                        }
                    }

                }


            }
        }

        //跳转到用户主页
        json.put("success",true);
        json.put("msg","登录成功"+msg);
        json.put("roles",role_no_list);
        json.put("userfunctionMap",functionMap);
        return json;
    }


    //根据账户的当前order,返回应该转往的账户类型
    private String getCurrentOrderAccountType(String username){
        Order order = null;
        if(username!=null&&!username.equals("")){
            List<Account> lt=accountDao.getAccountByUserName(username);
            if(lt.size()>0) {
                Account account = lt.get(0);
                if (account != null) {

                    order = orderDao.getCurrentPersonUserOrderByUsername(username);
                    if(order!=null){
                        return "person_user";
                    }

                    order = orderDao.getCurrentOrderCompanyUserByUsername(username);
                    if(order!=null){
                        return "company_user";
                    }

                }
            }
        }

        return null;
    }



//    public String LoginWithCellPhoneNo(String cellphoneno,String password){
//        List<Account> resultList= accountDao.VerifyCellphoneNoPassword(cellphoneno,password);
//        if(resultList.size()>0) {
//            return resultList.get(0).getUsername();
//        }
//        else
//            return null;
//    }

    public Account LoginWithCellPhoneNo(String cellphoneno,String password){
        List<Account> resultList= accountDao.VerifyCellphoneNoPassword(cellphoneno,password);
        if(resultList.size()>0) {
            return resultList.get(0);
        }
        else
            return null;
    }


    public boolean commitLogin(String username,String password){
        List<Account> resultList= accountDao.VerifyUserNamePassword(username,password);
        if(resultList.size()>0){
             return true;
        }else {
            return false;
        }
    }

    public String ForgotPassword(String cellphoneno) throws ClientException {
        JSONObject json=new JSONObject();
        Account account= accountDao.getPasswordByCellPhoneNo(cellphoneno);
        if(account!=null&&cellphoneno!=null){
            String password=account.getPassword();
            //此处发送手机短信
            SendSmsResponse smsresponse=AliyunSMS.sendPasswordSms(cellphoneno,password);
            if(smsresponse.getCode().equals("OK")){
                json.put("success",true);
                json.put("msg","登录密码已发送至手机");
            }else{
                json.put("success",false);
                json.put("msg","密码发送失败，"+smsresponse.getMessage());
            }
        }else{
            json.put("success",false);
            json.put("msg","手机号不存在");
            //System.out.println("fail");
        }
        String mmp = JSONArray.toJSONString(json);
        System.out.println(mmp);
        return mmp;
    }


    public String APPIsCellphoneNoValidForRegister(String cellphoneno){
        JSONObject json=new JSONObject();
        Account account= accountDao.getPasswordByCellPhoneNo(cellphoneno);
        if(account==null&&cellphoneno!=null){
            System.out.println("cellphoneno="+cellphoneno);
            json.put("success",true);
            json.put("msg","该手机号可以使用");
        }else{
            json.put("success",false);
            json.put("msg","该手机号已被注册");
        }
        String mmp = JSONArray.toJSONString(json);
        System.out.println(mmp);
        return mmp;
    }

    public JSONObject APPSwitchAccoutType(String ToAccountType,String username,String accountType){
        JSONObject json=new JSONObject();
        List<Account> lt=accountDao.getAccountByUserName(username);
        if(username!=null&&accountType!=null&&lt.size()>0) {
            Account account=lt.get(0);
            if(account!=null){

                Order order=null;
                if(accountType.equals("person_user")&&!ToAccountType.equals("person_user")) {
                    order = orderDao.getCurrentPersonUserOrderByUsername(username);
                }
                else if(accountType.equals("company_user")&&!ToAccountType.equals("company_user")){
                    order = orderDao.getCurrentOrderCompanyUserByUsername(username);
                }
                if(order!=null){
                    json.put("success",false);
                    json.put("msg","存在未完成订单，切换到"+ToAccountType+"失败");


                }else{
                    if(ToAccountType!=null&&ToAccountType.equals("person_user")&&account.getPerson_user_no()!=null&&!account.getPerson_user_no().equals("")){
                        json.put("success",true);
                        //session.setAttribute("accountType","person_user");
                        json.put("accountType","person_user");
                        json.put("msg","切换到person_user成功");
                    }
                    else if(ToAccountType!=null&&ToAccountType.equals("company_user")&&account.getCompany_user_no()!=null&&!account.getCompany_user_no().equals("")){
                        json.put("success",true);
                        //session.setAttribute("accountType","company_user");
                        json.put("accountType","company_user");
                        json.put("msg","切换到company_user成功");
                    }
                    else{
                        json.put("success",false);
                        json.put("msg","切换到"+ToAccountType+"失败");
                    }
                }
            }
            else{
                json.put("success",false);
                json.put("msg","切换失败，不存在账户信息");
            }

        }
        else{
            json.put("success",false);
            json.put("relogin", true);
            json.put("msg","切换失败，session不存在或不存在账户信息");
        }

        return json;
    }



    public String APPChangePassword(String cellphoneno,String old_password,String new_password){
        JSONObject json=new JSONObject();
        System.out.println("APPChangePassword cellphoneno="+cellphoneno);
        Account account= accountDao.getPasswordByCellPhoneNo(cellphoneno);
        if(account!=null){
            if(account.getPassword().equals(old_password)){
                account.setPassword(new_password);
                int count=accountDao.updateAccount(account);
                if(count>0){
                    json.put("success",true);
                    json.put("msg","密码修改成功");
                }else{
                    json.put("success",false);
                    json.put("msg","密码修改保存失败");
                }
            }else{
                json.put("success",false);
                json.put("msg","原始密码错误，密码修改失败");
            }
        }else{
            json.put("success",false);
            json.put("msg","手机号不存在，密码修改失败");
        }

        String mmp = JSONArray.toJSONString(json);
        System.out.println(mmp);
        return mmp;
    }

    public String APPChangeCellphone(String old_cellphoneno,String new_cellphoneno,String verifycode){
        JSONObject json=new JSONObject();
        System.out.println("APPChangeCellphone cellphoneno="+old_cellphoneno);
        Account account= accountDao.getPasswordByCellPhoneNo(old_cellphoneno);
        if(account!=null) {
            System.out.println("old_cellphoneno=" + old_cellphoneno);
            System.out.println("verifycode=" + verifycode);
            int count=verificationCodeDao.IsVerificationCodeValid(new_cellphoneno,verifycode,new Date());
            if(count==1||verifycode.equals("1")) {//测试使用 验证码为1时
                verificationCodeDao.delVerificationCodeByCellPhoneNo(new_cellphoneno);
                account.setCell_phone(new_cellphoneno);
                account.setUsername(new_cellphoneno);
                int res=accountDao.updateAccount(account);
                if(res==1){
                    json.put("success",true);
                    json.put("msg","修改登录手机号成功");
                }
                else{
                    json.put("success",false);
                    json.put("msg","系统错误，修改登录手机号失败");
                }
            }else{
                json.put("success",false);
                json.put("msg","验证码错误，修改登录手机号失败");
            }
        }
        else{
            json.put("success",false);
            json.put("msg","账户不存在，修改登录手机号失败");
        }
        String mmp = JSONArray.toJSONString(json);
        System.out.println(mmp);
        return mmp;
    }


    public String APPRegister(String cellphoneno,String password,String verifycode){
        JSONObject json=new JSONObject();
        System.out.println("APPRegister cellphoneno="+cellphoneno);
        Account account= accountDao.getPasswordByCellPhoneNo(cellphoneno);
        if(account==null&&cellphoneno!=null&&password!=null){
            System.out.println("cellphoneno="+cellphoneno);
            System.out.println("verifycode="+verifycode);
            //此处验证验证码是否可用
            int count=verificationCodeDao.IsVerificationCodeValid(cellphoneno,verifycode,new Date());
            if(count==1||verifycode.equals("1")){//测试使用 验证码为1时
                verificationCodeDao.delVerificationCodeByCellPhoneNo(cellphoneno);
                //开始注册
                Account newaccount=new Account();
                newaccount.setId(0);
                newaccount.setCell_phone(cellphoneno);
                newaccount.setUsername(cellphoneno);
                newaccount.setRole_no_list("person_user");
                newaccount.setLast_login_time(null);
                newaccount.setRegister_time(new Date());
                newaccount.setAccount_status("1");
                newaccount.setPassword(password);
                String uuuid= UUID.randomUUID().toString();
                uuuid=uuuid.replace("-","");
                newaccount.setPerson_user_no("PU"+uuuid);

                int res=accountDao.addAccount(newaccount);
                if(res==1){
                    json.put("success",true);
                    json.put("msg","注册成功");
                }
                else{
                    json.put("success",false);
                    json.put("msg","系统错误，注册失败");
                }

            }else{
                json.put("success",false);
                json.put("msg","验证码无效");
            }

        }else{
            json.put("success",false);
            json.put("msg","该手机号已被注册");
            //System.out.println("fail");
        }
        String mmp = JSONArray.toJSONString(json);
        System.out.println(mmp);
        return mmp;
    }


    public String SendVerCodeSMS(String cellphoneno) throws ClientException {
        JSONObject json=new JSONObject();
        System.out.println("SendVerCodeSMS cellphoneno="+cellphoneno);
        if(cellphoneno!=null&&!cellphoneno.equals("")){
            String verifyCode = String
                    .valueOf(new Random().nextInt(899999) + 100000);
            System.out.println("cellphoneno="+cellphoneno);
            System.out.println("verifyCode="+verifyCode);
            //此处发送手机短信

            int res=verificationCodeDao.CanResendVerificationCode(cellphoneno,new Date());
            if(res==2){
                //1分钟内存在已发送的验证码，本次不可发送
                json.put("success",false);
                json.put("msg","60秒内验证码不可重复发送");
            }else{
                int ct=verificationCodeDao.delVerificationCodeByCellPhoneNo(cellphoneno);
                SendSmsResponse smsresponse=AliyunSMS.sendVerificationCodeSms(cellphoneno,verifyCode);
                if(smsresponse.getCode().equals("OK")){
                    VerificationCode vc=new VerificationCode();
                    vc.setId(0);
                    vc.setCell_phone_no(cellphoneno);
                    vc.setVerification_code(verifyCode);
                    Date now = new Date();
                    Date expire_time = new Date(now.getTime() + 300000);
                    Date no_resend_until_time = new Date(now.getTime() + 30000);
                    vc.setExpire_time(expire_time);
                    vc.setNo_resend_until_time(no_resend_until_time);
                    int count=verificationCodeDao.addVerificationCode(vc);

                    json.put("success",true);
                    json.put("msg","验证码已发送至手机,有效时间5分钟");
                }else{
                    json.put("success",false);
                    json.put("msg","验证码发送错误"+smsresponse.getCode()+" "+smsresponse.getMessage());
                }
            }


        }else{
            json.put("success",false);
            json.put("msg","该手机号不存在");
            //System.out.println("fail");
        }
        String mmp = JSONArray.toJSONString(json);
        System.out.println(mmp);
        return mmp;
    }
}
