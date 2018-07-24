package com.prweb.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.prweb.dao.FunctionDao;
import com.prweb.dao.RoleDao;
import com.prweb.entity.Account;
import com.prweb.entity.Function;
import com.prweb.entity.Role;
//import com.prweb.util.APICloudPushService;
import com.prweb.util.ResponseUtil;
import com.prweb.dao.AccountDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Controller
@RequestMapping("/Login")
public class LoginController {

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private FunctionDao functionDao;

    //存放登录用户的session
    private Map<String,HttpSession> UserSessionMap=new HashMap<String,HttpSession>();

//    public static String md5(String pass){
//        String saltSource = "blog";
//        String hashAlgorithmName = "MD5";
//        Object salt = new Md5Hash(saltSource);
//        int hashIterations = 1024;
//        Object result = new SimpleHash(hashAlgorithmName, pass, salt, hashIterations);
//        String password = result.toString();
//        return password;
//    }


    private JSONObject getFunctionJson(String username,HttpServletRequest request){

        JSONObject json=new JSONObject();
        HttpSession session = request.getSession();
        //把用户数据保存在session域对象中
        session.setAttribute("userSession", username);
        //设置权限
        HashMap<String,Object> functionMap=new HashMap<String,Object>();
        //这里读取数据库设置所有权限
        String role_no_list=null;
        if(username!=null) {
            List<Account> lt=accountDao.getAccountByUserName(username);
            if(lt.size()>0) {
                Account account=lt.get(0);
                role_no_list=account.getRole_no_list();
                if(role_no_list!=null&&!role_no_list.equals("")){
                    role_no_list=role_no_list.replace(',',';');
                    String[] roles= role_no_list.split(";");
                    for(int i=0;i<roles.length;i++){
                        List<Role> rolelt=roleDao.getRoleByRoleNo(roles[i]);
                        //System.out.println("role ="+roles[i]);
                        if(rolelt.size()>0) {
                            Role role=rolelt.get(0);
                            String functionlist = role.getFunction_no_list();
                            if(functionlist!=null&&!functionlist.equals("")){
                                functionlist=functionlist.replace(',',';');
                                String[] func_no_s=functionlist.split(";");
                                for(int j=0;j<func_no_s.length;j++) {
                                    List<Function> funlst=functionDao.getFunctionByFunctionNo(func_no_s[j]);
                                    if(funlst.size()>0){
                                        //得到function
                                        Function f=funlst.get(0);
                                        String function_no=f.getFunction_no();
                                        String uris=f.getUri();
                                        functionMap.put(function_no,"1");
                                        String[] uriArray=uris.split(";");
                                        for(int n=0;n<uriArray.length;n++){
                                            functionMap.put(uriArray[n],"1");
                                            System.out.println("functionMap put="+function_no);
                                            System.out.println("uri put="+uriArray[n]);
                                        }

                                    }


                                }
                            }
                        }
                    }

                }


            }
        }
        session.setAttribute("userfunctionMap", functionMap);
        //functionMap.put("index","1");

        //查找是否存在其他用户登录该session
        HttpSession oldusersession=UserSessionMap.get(username);
        String msg="";
        if(oldusersession!=null&&oldusersession.getId()!=session.getId()){
            msg="（已踢出其他客户端）";
            System.out.println(msg);
            UserSessionMap.remove(username);
            try{
                oldusersession.invalidate();
            }catch (Exception e){
                e.printStackTrace();
            }

        }

        // 保存新用户session到公用UserSessionMap
        UserSessionMap.put(username,session);

        //跳转到用户主页
        json.put("success",true);
        json.put("msg","登录成功"+msg);
        json.put("roles",role_no_list);
        //System.out.println("登录验证 success");
//                String basePath = request.getSession().getServletContext().getRealPath("/");
//                System.out.println("登录验证 basePath="+basePath);
//                APICloudPushService.SendPushNotification(basePath,"title标题","内容内容内容内容","2","0","all","");
////
        return json;
    }


    //APP手机忘记密码
    @RequestMapping("/ForgotPassword")
    @ResponseBody
    public String ForgotPassword(HttpServletRequest request,HttpServletResponse response){
        JSONObject json=new JSONObject();
        String cellphoneno= request.getParameter("cellphoneno");
        try{
            int resTotal=0;
            System.out.println("cellphoneno="+cellphoneno);
            //System.out.println("ppassword="+ppassword);
            //if(personDao!=null)
            String password=null;
             Account account= accountDao.getPasswordByCellPhoneNo(cellphoneno);
            if(account!=null){
                password=account.getPassword();
                //此处发送手机短信

                json.put("success",true);
                json.put("msg","登录密码已发送至手机");
            }else{
                json.put("success",false);
                json.put("msg","手机号或密码错误");
                //System.out.println("fail");
            }
            ResponseUtil.write(response,json);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }



    //APP手机登录验证
    @RequestMapping("/LoginWithCellPhoneNo")
    @ResponseBody
    public String LoginWithCellPhoneNo(HttpServletRequest request,HttpServletResponse response){
        JSONObject json=new JSONObject();
        String cellphoneno= request.getParameter("cellphoneno");
        String password= request.getParameter("password");
        try{
            int resTotal=0;
            System.out.println("cellphoneno="+cellphoneno);
            //System.out.println("ppassword="+ppassword);
            //if(personDao!=null)
            List<Account> resultList= accountDao.VerifyCellphoneNoPassword(cellphoneno,password);
            if(resultList.size()>0){
                json=getFunctionJson(resultList.get(0).getUsername(),request);
            }else{
                json.put("success",false);
                json.put("msg","手机号或密码错误");
                //System.out.println("fail");
            }
            ResponseUtil.write(response,json);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }



    //登录验证
    @RequestMapping("/commitLogin")
    @ResponseBody
    public String commitLogin(HttpServletRequest request,HttpServletResponse response){
        JSONObject json=new JSONObject();
        String username= request.getParameter("username");
        String password= request.getParameter("password");
        try{
            int resTotal=0;
            System.out.println("username="+username);
            //System.out.println("ppassword="+ppassword);
            //if(personDao!=null)
            List<Account> resultList= accountDao.VerifyUserNamePassword(username,password);
            if(resultList.size()>0){
                json=getFunctionJson(username,request);
            }else{
                json.put("success",false);
                json.put("msg","用户名或密码错误");
                //System.out.println("fail");
            }
            ResponseUtil.write(response,json);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }




    //登录验证
    @RequestMapping("/Logout")
    @ResponseBody
    public String Logout(HttpServletRequest request,HttpServletResponse response){
        JSONObject json=new JSONObject();
        try{

                HttpSession session = request.getSession();
                //把用户数据保存在session域对象中
                //session.removeAttribute("userSession");
                //session.removeAttribute("userfunctionMap");
                UserSessionMap.remove(session.getAttribute("userSession"));
                session.invalidate();
                //跳转到登录页面
                json.put("success",true);
                json.put("msg","登出成功");

            ResponseUtil.write(response,json);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    //返回自己session，给APP使用
    @RequestMapping("/getMySession")
    @ResponseBody
    public String getMySession(HttpServletRequest request,HttpServletResponse response){
        JSONObject json=new JSONObject();
        try{

            HttpSession session = request.getSession();
            //把用户数据保存在session域对象中
            String username=(String)session.getAttribute("userSession");

            if(username!=null) {
                json.put("success",true);
                json.put("username", username);
                json.put("msg","获取username成功");
            }else{
                json.put("success",false);
                json.put("username", "");
                json.put("msg","不存在session，请先登录");
            }

            ResponseUtil.write(response,json);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


}
