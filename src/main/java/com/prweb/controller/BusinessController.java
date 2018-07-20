package com.prweb.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.prweb.dao.BusinessDao;


import com.prweb.entity.Business;
import com.prweb.util.ComboxItem;
import com.prweb.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
@RequestMapping("/Business")
public class BusinessController {
    @Autowired
    private BusinessDao businessDao;


    //搜索
    @RequestMapping(value = "getBusinessByLike",produces = "text/plain;charset=utf-8")
    @ResponseBody
    public String getBusinessByLike(@RequestParam(value = "business_no",required = false)String business_no, @RequestParam(value = "business_name",required = false)String business_name, @RequestParam(value = "business_type",required = false)String business_type,  HttpServletRequest request){
        String page= request.getParameter("page");
        String rows= request.getParameter("rows");
        if(page==null){
            page="1";
        }
        if(rows==null){
            rows="20";
        }
        int start=(Integer.parseInt(page)-1)*Integer.parseInt(rows);
        List<HashMap<String,Object>> list=businessDao.getAllByLike(business_no,business_name,business_type,start,Integer.parseInt(rows));
        int count=businessDao.getCountAllByLike(business_no,business_name,business_type);
        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",list);
        //System.out.println("rrrrrrrrrrrow="+count);
        String mmp= JSONArray.toJSONString(maps);
        //System.out.print("mmp:"+mmp);
        return mmp;

    }
    @RequestMapping(value = "getBusinessByBusinessNo",produces = "text/plain;charset=utf-8")
    @ResponseBody
    public String getBusinessByBusinessNo(@RequestParam(value = "business_no",required = false)String business_no, HttpServletRequest request){
        List<Business> list=businessDao.getBusinessByBusinessNo(business_no);
        String mmp= JSONArray.toJSONString(list);
        System.out.println(mmp);
        return mmp;
    }
    //用于
    @RequestMapping("/getAllBusiness")
    @ResponseBody
    public String getAllBusiness(HttpServletRequest request){
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



    //保存Business
    @RequestMapping(value = "/saveBusiness")
    @ResponseBody
    public String saveBusiness(Business business, HttpServletResponse response)throws Exception{
        System.out.print("saveBusiness");
        JSONObject json=new JSONObject();
        try{
            int resTotal=0;
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


    //删除Business信息
    @RequestMapping("/delBusiness")
    public String delBusiness(@RequestParam(value = "hlparam")String hlparam,HttpServletResponse response)throws Exception{
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
        ResponseUtil.write(response,json);
        return null;
    }

}
