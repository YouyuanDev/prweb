package com.prweb.controller;

import com.alibaba.fastjson.JSONObject;
import com.prweb.dao.CompanyUserDao;
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
    CompanyUserDao companyUserDao;


    //用于
    @RequestMapping("/getNearByCompanyUser")
    @ResponseBody
    public String getNearByCompanyUser(HttpServletRequest request){
        List<HashMap<String,Object>> list=companyUserDao.getNearByCompanyUser("121.480242","31.238269");
        String map= JSONObject.toJSONString(list);
        return map;
    }

}
