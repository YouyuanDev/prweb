package com.prweb.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.prweb.dao.PushEventRuleDao;
import com.prweb.dao.RoleDao;
import com.prweb.entity.PushEventRule;
import com.prweb.entity.Role;
import com.prweb.service.RoleService;
import com.prweb.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/Role")
public class RoleController {

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PushEventRuleDao pushEventRuleDao;


    @Autowired
    private RoleService roleService;

    //获取所有role列表
    @RequestMapping("getRoleByLike")
    @ResponseBody
    public String getRoleByLike(@RequestParam(value = "role_no",required = false)String role_no, @RequestParam(value = "role_name",required = false)String role_name, HttpServletRequest request){
        String page= request.getParameter("page");
        String rows= request.getParameter("rows");
        if(page==null){
            page="1";
        }
        if(rows==null){
            rows="20";
        }
        int start=(Integer.parseInt(page)-1)*Integer.parseInt(rows);
        return roleService.getRoleByLike(role_no,role_name,start,Integer.parseInt(rows));

    }

    //搜索
    @RequestMapping(value ="/getAllRoleByLike",produces = "text/plain;charset=utf-8")
    @ResponseBody
    public String getAllRoleByLike(@RequestParam(value = "role_no",required = false)String role_no, @RequestParam(value = "role_name",required = false)String role_name, HttpServletRequest request){
        return roleService.getAllRoleByLike(role_no,role_name);
    }

    //得到所有的推送事件
    @RequestMapping(value ="/getAllPushEventRule",produces = "text/plain;charset=utf-8")
    @ResponseBody
    public String getAllPushEventRule(HttpServletRequest request){
        return roleService.getAllPushEventRule();
    }


    //保存Role
    @RequestMapping(value = "/saveRole")
    @ResponseBody
    public String saveRole(Role role, HttpServletResponse response){
        System.out.print("saveRole");
        JSONObject json=new JSONObject();
        String mmp="";
        try{
            mmp=roleService.saveRole(role);
        }catch (Exception e){
            e.printStackTrace();
            json.put("success",false);
            json.put("message",e.getMessage());
            mmp= JSONObject.toJSONString(json);
        }finally {
//            try {
////                ResponseUtil.write(response, json);
////            }catch  (Exception e) {
////                e.printStackTrace();
////            }
            return mmp;
        }

    }


    //删除Role信息
    @RequestMapping("/delRole")
    public String delRole(@RequestParam(value = "hlparam")String hlparam,HttpServletResponse response)throws Exception{
        System.out.print("delRole");
        JSONObject json=new JSONObject();
        String mmp="";
        try{
            mmp=roleService.delRole(hlparam);
        }catch (Exception e){
            e.printStackTrace();
            json.put("success",false);
            json.put("message",e.getMessage());
            mmp= JSONObject.toJSONString(json);
        }finally {
            return mmp;
        }
    }

}
