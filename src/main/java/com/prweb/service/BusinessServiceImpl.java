package com.prweb.service;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.prweb.dao.BusinessDao;
import com.prweb.entity.Business;
import com.prweb.util.ComboxItem;
import com.prweb.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BusinessServiceImpl implements BusinessService{

    @Autowired
    private BusinessDao businessDao;



    public String getBusinessByLike(String business_no,String business_name,String business_type,int start,int rows){

        List<HashMap<String,Object>> list=businessDao.getAllByLike(business_no,business_name,business_type,start,rows);
        int count=businessDao.getCountAllByLike(business_no,business_name,business_type);
        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",list);
        //System.out.println("rrrrrrrrrrrow="+count);
        String mmp= JSONArray.toJSONString(maps);
        //System.out.print("mmp:"+mmp);
        return mmp;
    }

    public String getBusinessByBusinessNo(String business_no){
        List<Business> list=businessDao.getBusinessByBusinessNo(business_no);
        String mmp= JSONArray.toJSONString(list);
        System.out.println(mmp);
        return mmp;
    }

    public String getAllBusiness(){
        List<Business>list=businessDao.getAllBusiness();
        List<ComboxItem> colist=new ArrayList<ComboxItem>();
        for(int i=0;i<list.size();i++){
            ComboxItem citem= new ComboxItem();
            Business mill=((Business)list.get(i));
            citem.id=mill.getBusiness_no();
            citem.text= mill.getBusiness_no()+"("+mill.getBusiness_name()+")";
            colist.add(citem);
        }
        String map= JSONObject.toJSONString(colist);
        return map;
    }


    public String saveBusiness(Business business){
        int resTotal=0;
        JSONObject json=new JSONObject();
        if(business.getCreate_time()==null){
            business.setCreate_time(new Date());
        }
        if(business.getId()==0){
            //添加
            resTotal=businessDao.addBusiness(business);
        }else{
            //修改！
            resTotal=businessDao.updateBusiness(business);
        }
        if(resTotal>0){
            json.put("success",true);
            json.put("message","保存成功");
        }else{
            json.put("success",false);
            json.put("message","保存失败");
        }
        String map= JSONObject.toJSONString(json);
        return map;
    }

    public String delBusiness(String hlparam){
        String[]idArr=hlparam.split(",");
        int resTotal=0;
        resTotal=businessDao.delBusiness(idArr);
        JSONObject json=new JSONObject();
        StringBuilder sbmessage = new StringBuilder();
        sbmessage.append("总共");
        sbmessage.append(Integer.toString(resTotal));
        sbmessage.append("项业务信息删除成功\n");
        if(resTotal>0){
            //System.out.print("删除成功");
            json.put("success",true);
        }else{
            //System.out.print("删除失败");
            json.put("success",false);
        }
        json.put("message",sbmessage.toString());
        String map= JSONObject.toJSONString(json);
        return map;
    }
}
