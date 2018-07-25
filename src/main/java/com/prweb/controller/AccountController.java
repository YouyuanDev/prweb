package com.prweb.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.prweb.dao.AccountDao;


import com.prweb.entity.Account;
import com.prweb.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/AccountOperation")
public class AccountController {

    @Autowired
    private AccountDao accountDao;


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
        List<HashMap<String,Object>> list=accountDao.getAllByLike(username,account_status,account_type,start,Integer.parseInt(rows));
        int count=accountDao.getCountAllByLike(username,account_status,account_type);
        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",list);
        String mmp= JSONArray.toJSONString(maps);
        System.out.print("mmp:"+mmp);
        return mmp;

    }

    //保存Account
    @RequestMapping(value = "/saveAccount")
    @ResponseBody
    public String saveAccount(Account account, HttpServletResponse response){
        System.out.print("saveAccount");

        JSONObject json=new JSONObject();
        try{
            int resTotal=0;
            if(account.getRegister_time()==null){
                account.setRegister_time(new Date());
            }
            if(account.getLast_login_time()==null){
                account.setLast_login_time(new Date());
            }

            if(account.getId()==0){
                //添加
                resTotal=accountDao.addAccount(account);

            }else{
                //修改！

                resTotal=accountDao.updateAccount(account);
            }
            if(resTotal>0){
                json.put("success",true);
                json.put("message","保存成功");
            }else{
                json.put("success",false);
                json.put("message","保存失败");
            }

        }catch (Exception e){
            e.printStackTrace();
            json.put("success",false);
            json.put("message",e.getMessage());

        }finally {
            try {
                ResponseUtil.write(response, json);
            }catch  (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    //删除Account信息
    @RequestMapping("/delAccount")
    public String delAccount(@RequestParam(value = "hlparam")String hlparam,HttpServletResponse response)throws Exception{
        String[]idArr=hlparam.split(",");
        int resTotal=0;
        resTotal=accountDao.delAccount(idArr);
        JSONObject json=new JSONObject();
        StringBuilder sbmessage = new StringBuilder();
        sbmessage.append("总共");
        sbmessage.append(Integer.toString(resTotal));
        sbmessage.append("项角色信息删除成功\n");
        if(resTotal>0){
            //System.out.print("删除成功");
            json.put("success",true);
        }else{
            //System.out.print("删除失败");
            json.put("success",false);
        }
        json.put("message",sbmessage.toString());
        ResponseUtil.write(response,json);
        return null;
    }





}
