package com.prweb.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.prweb.dao.ServiceTypeDao;
import com.prweb.entity.Role;
import com.prweb.entity.ServiceType;
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
@RequestMapping("/ServiceType")
public class ServiceTypeController {


    @Autowired
    ServiceTypeDao serviceTypeDao;

    //得到所有的ServiceType
    @RequestMapping(value ="/getAllServiceType",produces = "text/plain;charset=utf-8")
    @ResponseBody
    public String getAllServiceType(HttpServletRequest request){
        List<ServiceType> list=serviceTypeDao.getAllServiceType();
        String mmp= JSONArray.toJSONString(list);
        return mmp;
    }



    //获取所有ServiceType列表
    @RequestMapping("getServiceTypeByLike")
    @ResponseBody
    public String getServiceTypeByLike(@RequestParam(value = "service_type_name",required = false)String service_type_name, HttpServletRequest request){
        String page= request.getParameter("page");
        String rows= request.getParameter("rows");
        if(page==null){
            page="1";
        }
        if(rows==null){
            rows="20";
        }
        int start=(Integer.parseInt(page)-1)*Integer.parseInt(rows);
        List<HashMap<String,Object>> list=serviceTypeDao.getAllByLike(service_type_name,start,Integer.parseInt(rows));
        int count=serviceTypeDao.getCountAllByLike(service_type_name);
        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",list);
        String mmp= JSONArray.toJSONString(maps);
        System.out.print("mmp:"+mmp);
        return mmp;

    }

    //保存ServiceType
    @RequestMapping(value = "/saveServiceType")
    @ResponseBody
    public String saveServiceType(ServiceType serviceType, HttpServletResponse response){
        System.out.print("saveServiceType");

        JSONObject json=new JSONObject();
        try{
            int resTotal=0;


            if(serviceType.getId()==0){
                //添加
                resTotal=serviceTypeDao.addServiceType(serviceType);

            }else{
                //修改！

                resTotal=serviceTypeDao.updateServiceType(serviceType);
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


    //删除serviceType信息
    @RequestMapping("/delServiceType")
    public String delServiceType(@RequestParam(value = "hlparam")String hlparam,HttpServletResponse response)throws Exception{
        String[]idArr=hlparam.split(",");
        int resTotal=0;
        resTotal=serviceTypeDao.delServiceType(idArr);
        JSONObject json=new JSONObject();
        StringBuilder sbmessage = new StringBuilder();
        sbmessage.append("总共");
        sbmessage.append(Integer.toString(resTotal));
        sbmessage.append("项服务类型信息删除成功\n");
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
