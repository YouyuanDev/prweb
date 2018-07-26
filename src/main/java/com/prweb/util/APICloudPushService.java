package com.prweb.util;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;


public class APICloudPushService {


    // APP 来自于APICloud
    static String appid = "A6087851424740";
    static String appkey = "C8ACB3D1-A4DC-68CD-885A-E32C38113C68";
    static String APIURL = "https://p.apicloud.com/api/push/message";

    public static void main(String[] args) throws IOException {


//        @Autowired
//        private RoleDao roleDao;
//
//
//        //发送推送消息
//        public void SendEvent(String event, String title,String content){
//            List<HashMap<String,Object>>  lt=roleDao.getRolesByEvent(event);
//
//            for(int i=0;i<lt.size();i++){
//                String role=(String)lt.get(i).get("role_no");
//                //发消息
//                APICloudPushService.SendPushNotification("",title,content,"1","0",role,"");
//            }
//
//
//        }


        SendPushNotification("","10:59titile","内容内容内容内容","1","0","all","");

    }




    public static String SendPushNotification(String sha1bathPath,String str_title,String str_content,String str_type,String str_platform,String str_groupName,String str_userIds){

        try{
            HttpClient client = new HttpClient();

            // post请求
            PostMethod post = new UTF8PostMethod(APIURL);

            // 提交参数
            NameValuePair title = new NameValuePair("title",str_title);                 // 消息标题
            NameValuePair content = new NameValuePair("content",str_content);             // 消息内容
            NameValuePair type = new NameValuePair("type",str_type);                     // 消息类型，1:消息 2:通知
            NameValuePair platform = new NameValuePair("platform",str_platform);             // 0：全部平台，1：ios, 2：android
            // 推送组，推送用户(没有可不写)
            NameValuePair groupName = new NameValuePair("groupName",str_groupName);     // 推送组名，多个组用英文逗号隔开.默认:全部组
            NameValuePair userIds = new NameValuePair("userIds",str_userIds);         // 推送用户id, 多个用户用英文逗号分隔

            post.setRequestBody(new NameValuePair[]{title, content, type, platform, groupName, userIds});
            HttpMethod method = post;
            // 生成规则
            String    key = testInvokeScriptMethod(sha1bathPath);
            // 设置请求头部信息
            method.setRequestHeader("X-APICloud-AppId", appid);
            method.setRequestHeader("X-APICloud-AppKey", key);
            // 执行方法
            client.executeMethod(method);
            // 打印服务器返回的状态
            System.out.println(method.getStatusLine());
            // 打印结果页面
            String response = new String(method.getResponseBodyAsString().getBytes("8859_1"));
            // 打印返回的信息
            System.out.println(response);
            // 释放连接
            method.releaseConnection();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }




    /**
     * Java中调用脚本语言的方法，通过JDK平台给script的方法中的形参赋值
     *
     * @param
     * @return String
     * */
    private static String testInvokeScriptMethod(String bathPath) throws Exception {
        // 获取时间戳
        long now = new Date().getTime();

        String key = appid + "UZ" + appkey + "UZ" + now;

        ScriptEngineManager sem = new ScriptEngineManager();
        ScriptEngine engine = sem.getEngineByName("js");

        // sha1.js 路径 (可根据自己的需求改为项目存放的相对路径)

        String sha1 = "/Users/kurt/Documents/workspace/htcsweb/src/main/webapp/js/sha1.js";

        if(bathPath!=null&&!bathPath.equals("")){
            sha1=bathPath+"/js/sha1.js";
        }
        System.out.println("sha1="+sha1);
        // 调用js文件
        FileReader fr = new FileReader(sha1);
        engine.eval(fr); // 指定脚本

        Invocable inv = (Invocable) engine;
        // 调用js函数方法(SHA1)
        String res = (String) inv.invokeFunction ("SHA1", key);
        //System.out.println("sha1="+res);
        return res + "." + now;
    }

    private static class UTF8PostMethod extends PostMethod{
        public UTF8PostMethod(String url){
            super(url);
        }
        @Override
        public String getRequestCharSet() {
            return "UTF-8";
        }
    }
}
