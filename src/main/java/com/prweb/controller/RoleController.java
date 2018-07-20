package com.prweb.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.prweb.dao.PushEventRuleDao;
import com.prweb.dao.RoleDao;
import com.prweb.entity.PushEventRule;
import com.prweb.entity.Role;
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
        List<HashMap<String,Object>> list=roleDao.getAllByLike(role_no,role_name,start,Integer.parseInt(rows));
        int count=roleDao.getCountAllByLike(role_no,role_name);
        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",list);
        String mmp= JSONArray.toJSONString(maps);
        System.out.print("mmp:"+mmp);
        return mmp;

    }

    //搜索
    @RequestMapping(value ="/getAllRoleByLike",produces = "text/plain;charset=utf-8")
    @ResponseBody
    public String getAllRoleByLike(@RequestParam(value = "role_no",required = false)String role_no, @RequestParam(value = "role_name",required = false)String role_name, HttpServletRequest request){
        List<HashMap<String,Object>> list=roleDao.getAllRoleByLike(role_no,role_name);
        String mmp= JSONArray.toJSONString(list);
        return mmp;
    }

    //得到所有的推送事件
    @RequestMapping(value ="/getAllPushEventRule",produces = "text/plain;charset=utf-8")
    @ResponseBody
    public String getAllPushEventRule(HttpServletRequest request){
        List<PushEventRule> list=pushEventRuleDao.getAllPushEventRule();
        String mmp= JSONArray.toJSONString(list);
        return mmp;
    }


    //保存Role
    @RequestMapping(value = "/saveRole")
    @ResponseBody
    public String saveRole(Role role, HttpServletResponse response){
        System.out.print("saveRole");

        JSONObject json=new JSONObject();
        try{
            int resTotal=0;


            if(role.getId()==0){
                //添加
                resTotal=roleDao.addRole(role);

            }else{
                //修改！

                resTotal=roleDao.updateRole(role);
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


    //删除Role信息
    @RequestMapping("/delRole")
    public String delRole(@RequestParam(value = "hlparam")String hlparam,HttpServletResponse response)throws Exception{
        String[]idArr=hlparam.split(",");
        int resTotal=0;
        resTotal=roleDao.delRole(idArr);
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
