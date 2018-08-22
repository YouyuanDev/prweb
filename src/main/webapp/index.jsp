<%@ page import="java.util.ResourceBundle" %>
<%@ page import="java.util.HashMap" %><%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2017/11/13 0013
  Time: 19:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String bPath =request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>熊猫救援后台管理系统 主页</title>
    <link rel="stylesheet" type="text/css" href="easyui/themes/bootstrap/easyui.css">
    <link rel="stylesheet" type="text/css" href="easyui/themes/icon.css">
    <link rel="stylesheet" href="css/common.css"/>
    <script type="text/javascript" src="easyui/jquery.min.js"></script>
    <script src="js/jquery.i18n.properties-1.0.9.js" type="text/javascript"></script>
    <script src="js/language.js" type="text/javascript"></script>
    <script src="js/common.js" type="text/javascript"></script>
    <script src="../js/jquery.form.js" type="text/javascript"></script>
    <script type="text/javascript">
        var url;
        $(function(){
            var uriArr=[
                "businessMgt","ServiceTypeMgt","FailureTypeMgt","commentMgr","FundTransferRecordMgr",
                "orderMgt",
                "accountMgt","role","function"
            ];
            var businessArr=uriArr.slice(0,5);
            var orderArr=uriArr.slice(5,6);
            var accountArr=uriArr.slice(6,9);
            var hsMapList="<%=session.getAttribute("userfunctionMap")%>";
            alert(hsMapList);
            var funArr;
            if(hsMapList!=null&&hsMapList!=""&&hsMapList.length>0){
                var reg=new RegExp('=1',"g")
                hsMapList=hsMapList.replace(reg,"");
                funArr=hsMapList.substring(1,hsMapList.length-1).split(',');
            }
            var tempNameArr=new Array();//得到的是比对uri中新的权限数组
            for(var i=0;i<funArr.length;i++){
                if($.inArray(funArr[i].trim(),uriArr)!=-1){
                    tempNameArr.push(funArr[i].trim());
                }
            }
            var finalNameArr=new Array();
            $.each(uriArr,function (index,element) {
                if($.inArray(element,tempNameArr)!=-1){
                    finalNameArr.push(element);
                }
            });
            if(finalNameArr.length>0){
                var businessDiv='<div title=\"业务管理\" class=\"i18n\" name=\"businessmanagement\"  style=\"padding:10px;\"><ul id=\"yy-business\">';
                var businessDivSon="";
                var orderDiv='<div title=\"订单管理\" class=\"i18n\" name=\"ordermanagement\"  style=\"padding:10px;\"><ul id=\"yy-order\">';
                var orderDivSon="";
                var accountDiv='<div title=\"账户管理\" class=\"i18n\" name=\"accountmanagement\"  style=\"padding:10px;\"><ul id=\"yy-account\">';
                var accountDivSon="";
                var endDiv="</ul></div>";
                //外喷砂
                $.each(finalNameArr,function (index,element) {
                    if($.inArray(element,businessArr)!=-1){
                        businessDivSon+=MakeMenus(element);
                        return true;
                    }
                    if($.inArray(element,orderArr)!=-1){
                        orderDivSon+=MakeMenus(element);
                        return true;
                    }
                    if($.inArray(element,accountArr)!=-1){
                        accountDivSon+=MakeMenus(element);
                        return true;
                    }
                });
                if(accountDivSon!=""&&accountDivSon.length>0){
                    accountDiv+=accountDivSon;
                    accountDiv+=endDiv;
                    $('#aa').append(accountDiv);
                }
                if(businessDivSon!=""&&businessDivSon.length>0){
                    businessDiv+=businessDivSon;
                    businessDiv+=endDiv;
                    $('#aa').append(businessDiv);
                }
                if(orderDivSon!=""&&orderDivSon.length>0){
                    orderDiv+=orderDivSon;
                    orderDiv+=endDiv;
                    $('#aa').append(orderDiv);
                }
            }
            hlLanguage("i18n/");
            //业务管理
            $("#yy-business").tree({
                onClick:function (node) {
                    var tab=$('#hlTab').tabs('getTab',node.text);
                    var nodeTxt=node.text;
                    if(tab){
                        $('#hlTab').tabs('select',node.text);
                    }else{
                        if("业务管理"==nodeTxt||"Business Management"==nodeTxt){
                            $('#hlTab').tabs('add',{
                                title:node.text,
                                content:"<iframe scrolling='auto' frameborder='0'  src='business/businessMgt.jsp' style='width:100%;height:100%;'></iframe>",
                                closable:true
                            });
                            hlLanguage();
                        }
                        else if("服务类型管理"==nodeTxt||"Service Type Management"==nodeTxt){
                            $('#hlTab').tabs('add',{
                                title:node.text,
                                content:"<iframe scrolling='auto' frameborder='0'  src='basic/ServiceTypeMgt.jsp' style='width:100%;height:100%;'></iframe>",
                                closable:true
                            });
                            hlLanguage();
                        }
                        else if("故障类型管理"==nodeTxt||"Failure Type Management"==nodeTxt){
                            $('#hlTab').tabs('add',{
                                title:node.text,
                                content:"<iframe scrolling='auto' frameborder='0'  src='basic/FailureTypeMgt.jsp' style='width:100%;height:100%;'></iframe>",
                                closable:true
                            });
                            hlLanguage();
                        }else if("评论管理"==nodeTxt||"Comment Management"==nodeTxt){
                            $('#hlTab').tabs('add',{
                                title:node.text,
                                content:"<iframe scrolling='auto' frameborder='0'  src='comment/commentMgr.jsp' style='width:100%;height:100%;'></iframe>",
                                closable:true
                            });
                            hlLanguage();
                        }else if("提现记录管理"==nodeTxt||"Withdrawal Record Management"==nodeTxt){
                            $('#hlTab').tabs('add',{
                                title:node.text,
                                content:"<iframe scrolling='auto' frameborder='0'  src='fund/FundTransferRecordMgr.jsp' style='width:100%;height:100%;'></iframe>",
                                closable:true
                            });
                            hlLanguage();
                        }
                    }
                }
            });
            //订单管理
            $("#yy-order").tree({
                onClick:function (node) {
                    var tab=$('#hlTab').tabs('getTab',node.text);
                    var nodeTxt=node.text;
                    if(tab){
                        $('#hlTab').tabs('select',node.text);
                    }else{
                        if("订单管理"==nodeTxt||"Order Management"==nodeTxt){
                            $('#hlTab').tabs('add',{
                                title:node.text,
                                content:"<iframe scrolling='auto' frameborder='0'  src='order/orderMgt.jsp' style='width:100%;height:100%;'></iframe>",
                                closable:true
                            });
                            hlLanguage();
                        }
                    }
                }
            });
            //账户管理
            $("#yy-account").tree({
                onClick:function (node) {
                    var tab=$('#hlTab').tabs('getTab',node.text);
                    var nodeTxt=node.text;
                    if(tab){
                        $('#hlTab').tabs('select',node.text);
                    }else{
                        if("账户管理"==nodeTxt||"Account Management"==nodeTxt){
                            $('#hlTab').tabs('add',{
                                title:node.text,
                                content:"<iframe scrolling='auto' frameborder='0'  src='account/accountMgt.jsp' style='width:100%;height:100%;'></iframe>",
                                closable:true
                            });
                            hlLanguage();
                        }
                        else if("角色管理"==nodeTxt||"Role Management"==nodeTxt){
                            $('#hlTab').tabs('add',{
                                title:node.text,
                                content:"<iframe scrolling='auto' frameborder='0'  src='account/role.jsp' style='width:100%;height:100%;'></iframe>",
                                closable:true
                            });
                            hlLanguage();
                        }
                        else if("权限管理"==nodeTxt||"Function Management"==nodeTxt){
                            $('#hlTab').tabs('add',{
                                title:node.text,
                                content:"<iframe scrolling='auto' frameborder='0'  src='account/function.jsp' style='width:100%;height:100%;'></iframe>",
                                closable:true
                            });
                            hlLanguage();
                        }
                    }
                }
            });
            //退出登录
            $('.btnLogout').click(function () {
                var form = $("<form>");//定义一个form表单
                form.attr("style", "display:none");
                form.attr("target", "");
                form.attr("method", "post");//请求类型
                form.attr("action","/Login/logout.action");//请求地址
                $("body").append(form);//将表单放置在web中
                var options={
                    type:'POST',
                    url:'/Login/Logout.action',
                    dataType:'json',
                    beforeSubmit:function () {
                        ajaxLoading();
                    },
                    success:function (data) {
                        ajaxLoadEnd();
                        //alert(data.msg);
                        if(data.success){
                            window.location.href="<%=bPath%>"+"login/login.jsp";
                        }
                    },error:function () {
                        ajaxLoadEnd();
                    }
                };
                form.ajaxSubmit(options);
                return false;
            });
            function ajaxLoadEnd() {
                $(".datagrid-mask").remove();
                $(".datagrid-mask-msg").remove();
            }
            function ajaxLoading() {
                $("<div class=\"datagrid-mask\"></div>").css({
                    display: "block",
                    width: "100%",
                    height: $(window).height()
                }).appendTo("body");
                $("<div class=\"datagrid-mask-msg\"></div>").html("正在退出，请稍候。。。").appendTo("body").css({
                    display: "block",
                    left: ($(document.body).outerWidth(true) - 190) / 2,
                    top: ($(window).height() - 45) / 2
                });
            }
        });
        function  MakeMenus(name) {
            var res='<li class=\"i18n1\" name=\"'+name+'\" ></li>';
            return res;
        }
    </script>
</head>
<body class="easyui-layout">
<div data-options="region:'south',split:true" style="height:55px;">
    <div style="text-align: center"><h3> &copy;2018 熊猫救援 版权所有</h3></div>
</div>
<div data-options="region:'north',split:true">
    <div style="float: right;padding:10px">
        <select id="language">
            <option value="zh-CN">中文</option>
            <option value="en">ENGLISH</option>
        </select>
        <button class="btnLogout">退出</button>
    </div>
</div>
<div data-options="region:'west'" title="导航菜单" class="i18n" name="navigation" style="width:200px;">
    <div id="aa" class="easyui-accordion">

    </div>
</div>

<div data-options="region:'center'," style="padding:5px;">
    <div id="hlTab" class="easyui-tabs" data-options="fit:true">
        <div title="首页" class="i18n" name="home" style="padding:5px;display:none;" data-options="iconCls:'icon-lightbulb'">
        </div>
    </div>
</div>
<div id="hlTb">
    <a href="#" id="addLinkBtn" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">Add</a>
    <a href="#" id="editLinkBtn" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">Edit</a>
    <a href="#" id="deltLinkBtn" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">Delete</a>
</div>
<script type="text/javascript" src="easyui/jquery.easyui.min.js"></script>
</body>
</html>
<script type="text/javascript">
    hlLanguage("i18n/");
</script>