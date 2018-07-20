package com.prweb.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.prweb.dao.FunctionDao;
import com.prweb.dao.RoleDao;
import com.prweb.entity.Function;
import com.prweb.entity.Role;
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
@RequestMapping("/Function")
public class FunctionController {
    @Autowired
    private FunctionDao functionDao;


    //搜索
    @RequestMapping(value = "getFunctionByLike",produces = "text/plain;charset=utf-8")
    @ResponseBody
    public String getFunctionByLike(@RequestParam(value = "function_no",required = false)String function_no, @RequestParam(value = "function_name",required = false)String function_name, HttpServletRequest request){
        String page= request.getParameter("page");
        String rows= request.getParameter("rows");
        if(page==null){
            page="1";
        }
        if(rows==null){
            rows="20";
        }
        int start=(Integer.parseInt(page)-1)*Integer.parseInt(rows);
        List<HashMap<String,Object>> list=functionDao.getAllByLike(function_no,function_name,start,Integer.parseInt(rows));
        int count=functionDao.getCountAllByLike(function_no,function_name);
        Map<String,Object> maps=new HashMap<String,Object>();
        maps.put("total",count);
        maps.put("rows",list);
        //System.out.println("rrrrrrrrrrrow="+count);
        String mmp= JSONArray.toJSONString(maps);
        //System.out.print("mmp:"+mmp);
        return mmp;

    }
    @RequestMapping(value = "getFunctionByNoName",produces = "text/plain;charset=utf-8")
    @ResponseBody
    public String getFunctionByNoName(@RequestParam(value = "function_no",required = false)String function_no, @RequestParam(value = "function_name",required = false)String function_name, HttpServletRequest request){
        List<HashMap<String,Object>> list=functionDao.getAllByNoName(function_no,function_name);
        String mmp= JSONArray.toJSONString(list);
        System.out.println(mmp);
        return mmp;
    }

    //保存function
    @RequestMapping(value = "/saveFunction")
    @ResponseBody
    public String saveFunction(Function function, HttpServletResponse response){
        System.out.print("saveFunction");

        JSONObject json=new JSONObject();
        try{
            int resTotal=0;


            if(function.getId()==0){
                //添加
                resTotal=functionDao.addFunction(function);

            }else{
                //修改！

                resTotal=functionDao.updateFunction(function);
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


    //删除Function信息
    @RequestMapping("/delFunction")
    public String delFunction(@RequestParam(value = "hlparam")String hlparam,HttpServletResponse response)throws Exception{
        String[]idArr=hlparam.split(",");
        int resTotal=0;
        resTotal=functionDao.delFunction(idArr);
        JSONObject json=new JSONObject();
        StringBuilder sbmessage = new StringBuilder();
        sbmessage.append("总共");
        sbmessage.append(Integer.toString(resTotal));
        sbmessage.append("项权限信息删除成功\n");
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
