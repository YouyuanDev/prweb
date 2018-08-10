package com.prweb.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.prweb.dao.FunctionDao;
import com.prweb.entity.Function;
import com.prweb.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FunctionServiceImpl implements FunctionService {

    @Autowired
    private FunctionDao functionDao;

    public String getFunctionByLike(String function_no,String function_name,int start,int rows){
        List<HashMap<String,Object>> list=functionDao.getAllByLike(function_no,function_name,start,rows);
        int count=functionDao.getCountAllByLike(function_no,function_name);
        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",list);
        //System.out.println("rrrrrrrrrrrow="+count);
        String mmp= JSONArray.toJSONString(maps);
        //System.out.print("mmp:"+mmp);
        return mmp;
    }

    public String getFunctionByNoName(String function_no,String function_name){
        List<HashMap<String,Object>> list=functionDao.getAllByNoName(function_no,function_name);
        String mmp= JSONArray.toJSONString(list);
        System.out.println(mmp);
        return mmp;
    }

    public String saveFunction(Function function){
        int resTotal=0;
        JSONObject json=new JSONObject();

        if(function.getId()==0){
            //添加
            resTotal=functionDao.addFunction(function);

        }else{
            //修改！

            resTotal=functionDao.updateFunction(function);
        }
        if(resTotal>0){
            json.put("success",true);
            json.put("message","保存成功");
        }else{
            json.put("success",false);
            json.put("message","保存失败");
        }
        String mmp= JSONArray.toJSONString(json);
        System.out.println(mmp);
        return mmp;
    }

    public String delFunction(String hlparam){
        String[]idArr=hlparam.split(",");
        int resTotal=0;
        resTotal=functionDao.delFunction(idArr);
        JSONObject json=new JSONObject();
        StringBuilder sbmessage = new StringBuilder();
        sbmessage.append("总共");
        sbmessage.append(Integer.toString(resTotal));
        sbmessage.append("项权限信息删除成功\n");
        if(resTotal>0){
            //System.out.print("删除成功");
            json.put("success",true);
        }else{
            //System.out.print("删除失败");
            json.put("success",false);
        }
        json.put("message",sbmessage.toString());
        String mmp= JSONArray.toJSONString(json);
        System.out.println(mmp);
        return mmp;
    }
}
