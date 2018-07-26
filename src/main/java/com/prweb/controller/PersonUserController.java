package com.prweb.controller;

import com.alibaba.fastjson.JSONObject;
import com.prweb.dao.CompanyDao;
import com.prweb.entity.Business;
import com.prweb.entity.CompanyUser;
import com.prweb.util.ComboxItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/PersonUser")
public class PersonUserController {

    @Autowired
    CompanyDao companyDao;


    //用于
    @RequestMapping(value = "getNearByCompany",produces = "text/plain;charset=utf-8")
    @ResponseBody
    public String getNearByCompany(HttpServletRequest request){

        String lon= request.getParameter("lon");
        String lat= request.getParameter("lat");


        System.out.println("center lon="+lon);
        System.out.println("center lat="+lat);

        if(lon==null||lon.equals("")){
            lon="121.480242";//人民广场的坐标
        }
        if(lat==null||lat.equals("")){
            lat="31.238269";
        }



        List<HashMap<String,Object>> list=companyDao.getNearByCompany(lon,lat);
        String map= JSONObject.toJSONString(list);
        return map;
    }

}
