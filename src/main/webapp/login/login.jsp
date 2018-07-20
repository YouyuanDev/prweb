<%--
  Created by IntelliJ IDEA.
  User: kurt
  Date: 2/8/18
  Time: 4:45 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>熊猫救援后台管理系统</title>
    <link rel="stylesheet" type="text/css" href="../easyui/themes/bootstrap/easyui.css">
    <link rel="stylesheet" type="text/css" href="../easyui/themes/bootstrap/window.css">
    <link rel="stylesheet" type="text/css" href="../easyui/themes/icon.css">
    <link rel="stylesheet" href="../css/common.css"/>
    <script type="text/javascript" src="../easyui/jquery.min.js"></script>
    <script type="text/javascript" src="../easyui/jquery.easyui.min.js"></script>
    <script src="../js/common.js" type="text/javascript"></script>
    <script src="../js/jquery.i18n.properties-1.0.9.js" type="text/javascript"></script>
    <script src="../js/language.js" type="text/javascript"></script>
    <script>
        $(function() {
            var ckusername=getCookie("UserName");
            var ckpass=getCookie("Password");
            if(ckusername!=null&&ckpass!=null&&ckusername!=""&&ckpass!=""){
                $('#username').textbox('setValue',ckusername);
                $('#password').textbox('setValue',ckpass);
                $("#logrem").attr("checked",true);
            }
            //如果存在框架，则调用login.jsp
            if(window.parent.location.href.indexOf("/login/login.jsp")==-1){
                window.parent.location.href="/login/login.jsp";
            }
        });
        //重置用户名密码
        function onResetClick(e) {
            $('#username').textbox('setValue','');
            $('#password').textbox('setValue','');
        }
        //提交登录验证
        function submitForm() {
            $("#frmLogin").form('submit',{
                url: "/Login/commitLogin.action",
                onSubmit:function() {
                    return $(this).form('validate');
                },
                success:function(result) {
                    var data = eval('(' + result + ')');
                    if (data.success == true) {
                        if ($("#logrem").attr("checked")) {//记住密码
                            //alert("记住密码!");
                            var u=$("#username").val();
                            var p=$("#password").val();
                            getCookie("UserName",u);
                            getCookie("Password",p);
                        }
                        $.messager.alert("登录成功",data.msg);
                        window.location.href = "../index.jsp";
                    } else {
                        $.messager.alert("错误提示",data.msg);
                    }
                }
            });
        }
    </script>
</head>
<body class="box" >
<div id="w" class="easyui-window" title="熊猫救援后台管理系统 登录" data-options="modal:true,closed:false,iconCls:'Lockgo',closable:false,minimizable:false" style="width:400px;padding:20px 70px 20px 70px;">
    <form id="frmLogin" method="post">
        <div style="margin-bottom:10px">
            <input class="easyui-textbox" id="username" name="username"  style="width:100%;height:30px;padding:12px" data-options="prompt:'员工工号',iconCls:'icon-man',iconWidth:38">
        </div>
        <div style="margin-bottom:20px">
            <input class="easyui-textbox" id="password" name="password" type="password" style="width:100%;height:30px;padding:12px" data-options="prompt:'登录密码',iconCls:'icon-lock',iconWidth:38">
        </div>
        <div style="margin-bottom:20px">
            <input type="checkbox" checked="true" id="logrem">
            <span class="i18n1" name="rememberme">记住账号</span>
        </div>
        <div style="width:100%">
            <a href="javascript:;" onclick="submitForm();" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" style="padding:5px 0px;width:48%;">
                <span class="i18n1" name="login">登录</span>
            </a>
            <a href="javascript:;" onclick="onResetClick();" class="easyui-linkbutton" data-options="iconCls:'icon-clear'" style="padding:5px 0px;width:48%;">
                <span class="i18n1" name="reset">重置</span>
            </a>
        </div>
    </form>
</div>

</body>
</html>
<script type="text/javascript">
    hlLanguage("../i18n/");
</script>