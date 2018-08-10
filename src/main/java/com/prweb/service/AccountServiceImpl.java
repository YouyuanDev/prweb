package com.prweb.service;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.prweb.dao.AccountDao;
import com.prweb.entity.Account;
import com.prweb.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AccountServiceImpl implements AccountService{

    @Autowired
    private AccountDao accountDao;

    public String getAccountByLike(String username,String account_status,String account_type,int start,int rows){
        List<HashMap<String,Object>> list=accountDao.getAllByLike(username,account_status,account_type,start,rows);
        int count=accountDao.getCountAllByLike(username,account_status,account_type);
        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",list);
        String mmp= JSONArray.toJSONString(maps);
        System.out.print("mmp:"+mmp);
        return mmp;
    }

    public String saveAccount(Account account){
        int resTotal=0;
        JSONObject json=new JSONObject();
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
        String mmp= JSONArray.toJSONString(json);
        System.out.print("mmp:"+mmp);
        return mmp;
    }

    public String delAccount(String hlparam){
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
        String mmp= JSONArray.toJSONString(json);
        System.out.print("mmp:"+mmp);
        return mmp;
    }


    public String getAccountInfo(String username){
        JSONObject json=new JSONObject();
        List<Account> list=accountDao.getAccountByUserName(username);
        if(list.size()>0){
            Account account=list.get(0);
            json.put("success",true);
            json.put("account",account);
            json.put("message","获取账户信息成功");

        }else{
            json.put("success",false);
            json.put("message","获取账户信息失败");

        }
        String mmp= JSONArray.toJSONString(json);
        System.out.print("mmp:"+mmp);
        return mmp;

    }

}
