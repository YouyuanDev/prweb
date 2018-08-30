package com.prweb.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.prweb.dao.*;
import com.prweb.entity.*;
//import com.prweb.util.APICloudPushService;
import com.prweb.service.LoginService;
import com.prweb.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.*;

import com.prweb.util.AliyunSMS;



@Controller
@RequestMapping("/Login")
public class LoginController {


    @Autowired
    private LoginService loginService;


    //存放登录用户的session
    private Map<String,HttpSession> UserSessionMap=new HashMap<String,HttpSession>();

//    public static String md5(String pass){
//        String saltSource = "blog";
//        String hashAlgorithmName = "MD5";
//        Object salt = new Md5Hash(saltSource);
//        int hashIterations = 1024;
//        Object result = new SimpleHash(hashAlgorithmName, pass, salt, hashIterations);
//        String password = result.toString();
//        return password;
//    }




    public LoginController() {
    }




    //APP检测手机号是否可以注册
    @RequestMapping("/APPIsCellphoneNoValidForRegister")
    @ResponseBody
    public String APPIsCellphoneNoValidForRegister(HttpServletRequest request,HttpServletResponse response){
        //JSONObject json=new JSONObject();
        String cellphoneno= request.getParameter("cellphoneno");

//        try{
//            System.out.println("cellphoneno="+cellphoneno);
//
//            ResponseUtil.write(response,json);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        return loginService.APPIsCellphoneNoValidForRegister(cellphoneno);
    }

//    //获取账户的当前order
//    private Order getCurrentOrderbyUsername(String username,String accountType){
//        Order order = null;
//        if(username!=null&&!username.equals("")&&accountType!=null&&!accountType.equals("")){
//            List<Account> lt=accountDao.getAccountByUserName(username);
//            if(username!=null&&accountType!=null&&lt.size()>0) {
//                Account account = lt.get(0);
//                if (account != null) {
//                    if (accountType.equals("person_user")) {
//                        order = orderDao.getCurrentPersonUserOrderByUsername(username);
//                    } else if (accountType.equals("company_user")) {
//                        order = orderDao.getCurrentOrderCompanyUserByUsername(username);
//                    }
//                }
//            }
//        }
//        return order;
//    }







    //APP手机账户切换用户类型   个人用户 商户用户切换
    @RequestMapping("/APPSwitchAccoutType")
    @ResponseBody
    public String APPSwitchAccoutType(HttpServletRequest request,HttpServletResponse response) {

        String ToAccountType= request.getParameter("ToAccountType");
        JSONObject json = new JSONObject();
        //返回用户session数据
        HttpSession session = request.getSession();
        //把用户数据保存在session域对象中
        String username=(String)session.getAttribute("userSession");
        String accountType=(String)session.getAttribute("accountType");

        json=loginService.APPSwitchAccoutType(ToAccountType,username,accountType);

        if(json.getBoolean("success")){
            session.setAttribute("accountType",json.getString("accountType"));
        }
        String mmp = JSONArray.toJSONString(json);
        System.out.println(mmp);
        return mmp;

    }
    //APP修改登录密码
    @RequestMapping("/APPChangePassword")
    @ResponseBody
    public String APPChangePassword(HttpServletRequest request,HttpServletResponse response){
        String cellphoneno= request.getParameter("cellphoneno");
        String old_password= request.getParameter("old_password");
        String new_password= request.getParameter("new_password");
        return loginService.APPChangePassword(cellphoneno, old_password,new_password);
    }

    //APP修改登录手机号
    @RequestMapping("/APPChangeCellphone")
    @ResponseBody
    public String APPChangeCellphone(HttpServletRequest request,HttpServletResponse response){
        String old_cellphoneno= request.getParameter("old_cellphoneno");
        String new_cellphoneno= request.getParameter("new_cellphoneno");
        String verifycode= request.getParameter("verifycode");

        return loginService.APPChangePassword(old_cellphoneno, new_cellphoneno,verifycode);
    }


    //APP手机注册
    @RequestMapping("/APPRegister")
    @ResponseBody
    public String APPRegister(HttpServletRequest request,HttpServletResponse response){
        String cellphoneno= request.getParameter("cellphoneno");
        String password= request.getParameter("password");
        String verifycode= request.getParameter("verifycode");

        return loginService.APPRegister(cellphoneno, password,verifycode);

//        try{
//
//            ResponseUtil.write(response,json);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return null;
    }


    //发送验证码
    @RequestMapping("/SendVerCodeSMS")
    @ResponseBody
    public String SendVerCodeSMS(HttpServletRequest request,HttpServletResponse response){
        JSONObject json=new JSONObject();
        String cellphoneno= request.getParameter("cellphoneno");
        String mmp="";
        try{
            mmp=loginService.SendVerCodeSMS(cellphoneno);
            ResponseUtil.write(response,json);
        }catch (Exception e){
            e.printStackTrace();
            json.put("success",false);
            json.put("msg","SendVerCodeSMS系统错误");
            mmp = JSONArray.toJSONString(json);
            System.out.println(mmp);
        }finally {
            return mmp;
        }

    }



    //APP手机忘记密码
    @RequestMapping("/ForgotPassword")
    @ResponseBody
    public String ForgotPassword(HttpServletRequest request,HttpServletResponse response){
        JSONObject json=new JSONObject();
        String cellphoneno= request.getParameter("cellphoneno");
        String mmp="";
        try{
            System.out.println("cellphoneno="+cellphoneno);
            mmp=loginService.ForgotPassword(cellphoneno);
//            ResponseUtil.write(response,json);
        }catch (Exception e){
            e.printStackTrace();
            json.put("success",false);
            json.put("msg","系统错误，无法发送手机密码到手机");
            mmp= JSONArray.toJSONString(json);
        }finally {
            return mmp;
        }
    }



    //APP手机登录验证
    @RequestMapping("/LoginWithCellPhoneNo")
    @ResponseBody
    public String LoginWithCellPhoneNo(HttpServletRequest request,HttpServletResponse response){
        JSONObject json=new JSONObject();
        String cellphoneno= request.getParameter("cellphoneno");
        String password= request.getParameter("password");
        HttpSession session = request.getSession();

        try{
            int resTotal=0;
            System.out.println("cellphoneno="+cellphoneno);

            //String username=loginService.LoginWithCellPhoneNo(cellphoneno,password);
            Account account=loginService.LoginWithCellPhoneNo(cellphoneno,password);
            if(account!=null){
                String username=account.getUsername();
                json=loginService.getFunctionJson(username);
                json.put("account",account);
                if(json.getBoolean("success")){
                    setSessionInfo(session,username,json.getObject("userfunctionMap",HashMap.class),json.getString("accountType"));
                }
            }else{
                json.put("success",false);
                json.put("msg","手机号或密码错误");
            }
//            if(username!=null&&!username.equals("")){
//                json=loginService.getFunctionJson(username);
//                if(json.getBoolean("success")){
//                    setSessionInfo(session,username,json.getObject("userfunctionMap",HashMap.class),json.getString("accountType"));
//                }
//
//            }else{
//                json.put("success",false);
//                json.put("msg","手机号或密码错误");
//            }



            ResponseUtil.write(response,json);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private void setSessionInfo(HttpSession session,String username,HashMap<String,Object> functionMap,String accountType){
        //把用户数据保存在session域对象中
        session.setAttribute("userSession", username);
        session.setAttribute("userfunctionMap", functionMap);
        session.setAttribute("accountType", accountType);
        //查找是否存在其他用户登录该session
        HttpSession oldusersession=UserSessionMap.get(username);
        String msg="";
        if(oldusersession!=null&&oldusersession.getId()!=session.getId()){
            msg="（已踢出其他客户端）";
            System.out.println(msg);
            UserSessionMap.remove(username);
            try{
                oldusersession.invalidate();
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        // 保存新用户session到公用UserSessionMap
        UserSessionMap.put(username,session);
    }


    //登录验证
    @RequestMapping("/commitLogin")
    @ResponseBody
    public String commitLogin(HttpServletRequest request,HttpServletResponse response){
        JSONObject json=new JSONObject();
        String username= request.getParameter("username");
        String password= request.getParameter("password");
        HttpSession session = request.getSession();
        try{
            int resTotal=0;
            System.out.println("username="+username);
            //System.out.println("ppassword="+ppassword);
            //if(personDao!=null)
            boolean verifed=loginService.commitLogin(username,password);
            if(verifed){
                json=loginService.getFunctionJson(username);
                setSessionInfo(session,username,json.getObject("userfunctionMap",HashMap.class),json.getString("accountType"));
            }else{
                json.put("success",false);
                json.put("msg","用户名或密码错误");
                //System.out.println("fail");
            }
            ResponseUtil.write(response,json);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }




    //登录验证
    @RequestMapping("/Logout")
    @ResponseBody
    public String Logout(HttpServletRequest request,HttpServletResponse response){
        JSONObject json=new JSONObject();
        try{

                HttpSession session = request.getSession();
                //把用户数据保存在session域对象中
                //session.removeAttribute("userSession");
                //session.removeAttribute("userfunctionMap");
                UserSessionMap.remove(session.getAttribute("userSession"));
                session.invalidate();
                //跳转到登录页面
                json.put("success",true);
                json.put("msg","登出成功");

            ResponseUtil.write(response,json);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    //返回自己session，给APP使用
    @RequestMapping("/getMySession")
    @ResponseBody
    public String getMySession(HttpServletRequest request,HttpServletResponse response){
        JSONObject json=new JSONObject();
        try{

            HttpSession session = request.getSession(false);
            //把用户数据保存在session域对象中

            if(session==null){
                json.put("success",false);
                json.put("msg","不存在session");
            }else{
                String username=(String)session.getAttribute("userSession");
                String accountType=(String)session.getAttribute("accountType");
                if(username!=null) {
                    json.put("success",true);
                    json.put("username", username);
                    json.put("accountType", accountType);
                    json.put("msg","获取username成功");
                }else{
                    json.put("success",false);
                    json.put("username", "");
                    json.put("msg","不存在session，请先登录");
                }
            }
            System.out.println("getMySession:"+json.getString("msg"));
            ResponseUtil.write(response,json);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


}
