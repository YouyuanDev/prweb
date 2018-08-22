package com.prweb.controller;


import com.alibaba.fastjson.JSONObject;
import com.prweb.entity.Comment;
import com.prweb.entity.FundTransferRecord;
import com.prweb.service.CommentService;
import com.prweb.service.FundTransferRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/FundTransfer")
public class FundTransferRecordController {

    @Autowired
    private FundTransferRecordService fundTransferRecordService;


    //搜索
    @RequestMapping(value = "getFundTransferRecordByLike",produces = "text/plain;charset=utf-8")
    @ResponseBody
    public String getFundTransferRecordByLike(@RequestParam(value = "transfer_no",required = false)String transfer_no, @RequestParam(value = "company_no",required = false)String company_no, HttpServletRequest request){
        String page= request.getParameter("page");
        String rows= request.getParameter("rows");
        if(page==null){
            page="1";
        }
        if(rows==null){
            rows="20";
        }
        int start=(Integer.parseInt(page)-1)*Integer.parseInt(rows);

        return fundTransferRecordService.getFundTransferRecordByLike(transfer_no,company_no,start,Integer.parseInt(rows));

    }



}
