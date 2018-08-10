package com.prweb.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.prweb.dao.FailureTypeDao;
import com.prweb.entity.FailureType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FailureTypeServiceImpl implements FailureTypeService{


    @Autowired
    FailureTypeDao failureTypeDao;


    public String getAllFailureType(){
        List<FailureType> list=failureTypeDao.getAllFailureType();
        String mmp= JSONArray.toJSONString(list);
        return mmp;
    }

    public String getFailureTypeByLike(String failure_type_name,int start, int rows){
        List<HashMap<String,Object>> list=failureTypeDao.getAllByLike(failure_type_name,start, rows);
        int count=failureTypeDao.getCountAllByLike(failure_type_name);
        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",list);
        String mmp= JSONArray.toJSONString(maps);
        System.out.print("mmp:"+mmp);
        return mmp;
    }

    public String saveFailureType(FailureType failureType){
        int resTotal=0;
        JSONObject json=new JSONObject();

        if(failureType.getId()==0){
            //添加
            resTotal=failureTypeDao.addFailureType(failureType);

        }else{
            //修改！

            resTotal=failureTypeDao.updateFailureType(failureType);
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

    public String delFailureType(String hlparam){
        String[]idArr=hlparam.split(",");
        int resTotal=0;
        resTotal=failureTypeDao.delFailureType(idArr);
        JSONObject json=new JSONObject();
        StringBuilder sbmessage = new StringBuilder();
        sbmessage.append("总共");
        sbmessage.append(Integer.toString(resTotal));
        sbmessage.append("项故障类型信息删除成功\n");
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
}
