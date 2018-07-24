package com.prweb.controller;

import com.alibaba.fastjson.JSONArray;
import com.prweb.dao.FailureTypeDao;
import com.prweb.entity.FailureType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;



@Controller
@RequestMapping("/FailureType")
public class FailureTypeController {


    @Autowired
    FailureTypeDao failureTypeDao;

    //得到所有的FailureType
    @RequestMapping(value ="/getAllFailureType",produces = "text/plain;charset=utf-8")
    @ResponseBody
    public String getAllFailureType(HttpServletRequest request){
        List<FailureType> list=failureTypeDao.getAllFailureType();
        String mmp= JSONArray.toJSONString(list);
        return mmp;
    }
}