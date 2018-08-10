package com.prweb.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.prweb.dao.ServiceTypeDao;
import com.prweb.entity.Role;
import com.prweb.entity.ServiceType;
import com.prweb.service.ServiceTypeService;
import com.prweb.util.ResponseUtil;
import org.omg.PortableInterceptor.INACTIVE;
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
    private ServiceTypeService serviceTypeService;

    //得到所有的ServiceType
    @RequestMapping(value ="/getAllServiceType",produces = "text/plain;charset=utf-8")
    @ResponseBody
    public String getAllServiceType(HttpServletRequest request){
        return serviceTypeService.getAllServiceType();
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
        return serviceTypeService.getServiceTypeByLike(service_type_name,start, Integer.parseInt(rows));

    }

    //保存ServiceType
    @RequestMapping(value = "/saveServiceType")
    @ResponseBody
    public String saveServiceType(ServiceType serviceType, HttpServletResponse response){
        System.out.print("saveServiceType");

        return serviceTypeService.saveServiceType(serviceType);
    }


    //删除serviceType信息
    @RequestMapping("/delServiceType")
    public String delServiceType(@RequestParam(value = "hlparam")String hlparam,HttpServletResponse response)throws Exception{
        return serviceTypeService.delServiceType(hlparam);
    }


}
