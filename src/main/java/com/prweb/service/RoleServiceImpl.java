package com.prweb.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.prweb.dao.PushEventRuleDao;
import com.prweb.dao.RoleDao;
import com.prweb.entity.PushEventRule;
import com.prweb.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PushEventRuleDao pushEventRuleDao;


    public String getRoleByLike(String role_no,String role_name,int start, int rows){
        List<HashMap<String,Object>> list=roleDao.getAllByLike(role_no,role_name,start,rows);
        int count=roleDao.getCountAllByLike(role_no,role_name);
        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",list);
        String mmp= JSONArray.toJSONString(maps);
        System.out.print("mmp:"+mmp);
        return mmp;
    }

    public String getAllRoleByLike(String role_no,String role_name){
        List<HashMap<String,Object>> list=roleDao.getAllRoleByLike(role_no,role_name);
        String mmp= JSONArray.toJSONString(list);
        return mmp;
    }

    public String getAllPushEventRule(){
        List<PushEventRule> list=pushEventRuleDao.getAllPushEventRule();
        String mmp= JSONArray.toJSONString(list);
        return mmp;
    }

    public String saveRole(Role role){
        int resTotal=0;
        JSONObject json = new JSONObject();

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
        String mmp= JSONArray.toJSONString(json);
        return mmp;
    }

    public String delRole(String hlparam){
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
        String mmp= JSONArray.toJSONString(json);
        return mmp;

    }
}
