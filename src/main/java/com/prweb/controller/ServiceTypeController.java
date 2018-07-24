package com.prweb.controller;


import com.alibaba.fastjson.JSONArray;
import com.prweb.dao.ServiceTypeDao;
import com.prweb.entity.ServiceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
}
