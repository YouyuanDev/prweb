package com.prweb.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.prweb.dao.AccountDao;


import com.prweb.entity.Account;
import com.prweb.service.AccountService;
import com.prweb.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/AccountOperation")
public class AccountController {

    @Autowired
    private AccountService accountService;


    //获取所有role列表
    @RequestMapping("getAccountByLike")
    @ResponseBody
    public String getAccountByLike(@RequestParam(value = "username",required = false)String username, @RequestParam(value = "account_status",required = false)String account_status, @RequestParam(value = "account_type",required = false)String account_type, HttpServletRequest request){
        String page= request.getParameter("page");
        String rows= request.getParameter("rows");
        if(page==null){
            page="1";
        }
        if(rows==null){
            rows="20";
        }
        int start=(Integer.parseInt(page)-1)*Integer.parseInt(rows);
        return accountService.getAccountByLike(username,account_status,account_type,start,Integer.parseInt(rows));

    }

    //保存Account
    @RequestMapping(value = "/saveAccount")
    @ResponseBody
    public String saveAccount(Account account, HttpServletResponse response){
        System.out.print("saveAccount");

        JSONObject json=new JSONObject();
        String mmp="";
        try{
            mmp=accountService.saveAccount(account);
        }catch (Exception e){
            e.printStackTrace();
            json.put("success",false);
            json.put("message",e.getMessage());
            mmp= JSONObject.toJSONString(json);
        }finally {
//            try {
//                ResponseUtil.write(response, json);
//            }catch  (Exception e) {
//                e.printStackTrace();
//            }
            return mmp;
        }
    }


    //删除Account信息
    @RequestMapping("/delAccount")
    public String delAccount(@RequestParam(value = "hlparam")String hlparam,HttpServletResponse response)throws Exception{
        String mmp="";
        try{
            mmp=accountService.delAccount(hlparam);
        }catch (Exception e){
            JSONObject json=new JSONObject();
            e.printStackTrace();
            json.put("success",false);
            json.put("message",e.getMessage());
            mmp= JSONObject.toJSONString(json);
        }finally {
            return mmp;
        }
    }



    //获取accountInfo
    @RequestMapping(value = "getAccountInfo",produces = "text/plain;charset=utf-8")
    @ResponseBody
    public String getAccountInfo( HttpServletRequest request){

        System.out.print("getAccountInfo");

        JSONObject json = new JSONObject();
        //返回用户session数据
        HttpSession session = request.getSession();
        //把用户数据保存在session域对象中
        String username = (String) session.getAttribute("userSession");
        //String accountType = (String) session.getAttribute("accountType");

        return accountService.getAccountInfo(username);

    }

}
