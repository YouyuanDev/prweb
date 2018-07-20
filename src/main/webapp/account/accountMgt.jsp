<%--
  Created by IntelliJ IDEA.
  User: kurt
  Date: 7/17/18
  Time: 5:24 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>人员账户管理</title>
    <link rel="stylesheet" type="text/css" href="../easyui/themes/bootstrap/easyui.css">
    <link rel="stylesheet" type="text/css" href="../easyui/themes/icon.css">
    <link href="../miniui/multiupload/multiupload.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" href="../css/common.css"/>
    <script src="../easyui/jquery.min.js" type="text/javascript"></script>
    <script src="../js/common.js" type="text/javascript"></script>
    <script src="../miniui/boot.js" type="text/javascript"></script>
    <script src="../miniui/fileupload/swfupload/swfupload.js" type="text/javascript"></script>
    <script src="../miniui/multiupload/multiupload.js" type="text/javascript"></script>
    <script  src="../js/lrscroll.js" type="text/javascript"></script>
    <script src="../js/jquery.i18n.properties-1.0.9.js" type="text/javascript"></script>
    <script src="../js/language.js" type="text/javascript"></script>
    <script type="text/javascript">
        var url;
        $(function () {
            $('#yyAccountDialog').dialog({
                onClose:function () {
                    var type=$('#yyCancelBtn').attr('operationtype');
                    if(type!="add"){
                        clearFormLabel();
                    }
                }
            });
            $('.mini-buttonedit .mini-buttonedit-input').css('width','150px');
        });
        function addAccount(){
            $('#yyCancelBtn').attr('operationtype','add');
            $('#yyAccountDialog').dialog('open').dialog('setTitle','新增');
            $('#accountForm').form('clear');
            $("input[name='id']").val('0');
            // $('#register_time').text('');
            // $('#last_login_time').text('');
            url="/AccountOperation/saveAccount.action";
            look2.deselectAll();
        }
        function delAccount() {
            var row = $('#accountDatagrids').datagrid('getSelections');
            if(row.length>0){
                var idArr=[];
                for (var i=0;i<row.length;i++){
                    idArr.push(row[i].id);
                }
                var idArrs=idArr.join(',');
                $.messager.confirm('系统提示',"您确定要删除这<font color=red>"+idArr.length+ "</font>条数据吗？",function (r) {
                    if(r){
                        $.post(
                            "/AccountOperation/delAccount.action",
                            {"hlparam":idArrs},function (data) {
                                if(data.success){
                                    $("#accountDatagrids").datagrid("reload");
                                }
                                yyAlertFour(data.message);
                            },"json");
                    }
                });
            }else{
                yyAlertOne();
            }
        }
        function editAccount() {
            $('#yyCancelBtn').attr('operationtype','edit');
            var row = $('#accountDatagrids').datagrid('getSelected');
            if(row){
                $('#yyAccountDialog').dialog('open').dialog('setTitle','修改');
                row.register_time=getDate1(row.register_time);
                row.last_login_time=getDate1(row.last_login_time);
                $('#accountForm').form('load',row);
                look2.setText(row.role_no_list);
                look2.setValue(row.role_no_list);
                url="/AccountOperation/saveAccount.action";
            }else{
                yyAlertTwo();
            }
        }
        function searchAccount() {
            $('#accountDatagrids').datagrid('load',{
                'username': $('#username').val(),
                'account_status': $('#account_status').val(),
                'account_type': $('#account_type').val()
            });
        }
        function accountFormSubmit() {
            $('#accountForm').form('submit',{
                url:url,
                onSubmit:function () {
                    if($("input[name='username']").val()==""){
                        yyAlertFour("请输入用户名");
                        return false;
                    }else if($("input[name='password']").val()==""){
                        yyAlertFour("请输入密码");
                        return false;
                    }
                    else if($("input[name='register_time']").val()==""){
                        yyAlertFour("请输入注册时间");
                        return false;
                    }else if($("input[name='last_login_time']").val()==""){
                        yyAlertFour("上次登录时间");
                        return false;
                    }
                },
                success: function(result){
                    //alert(result);
                    var result = eval('('+result+')');
                    if (result.success){
                        $('#yyAccountDialog').dialog('close');
                        $('#accountDatagrids').datagrid('reload');
                        clearFormLabel();
                    }
                    yyAlertFour(result.message);
                },
                error:function () {
                    yyAlertThree();
                }
            });
        }
        function accountCancelSubmit() {
            $('#yyAccountDialog').dialog('close');
        }
        function  clearFormLabel() {
            $('#accountForm').form('clear');
        }
    </script>
</head>
<body>
<fieldset class="b3" style="padding:10px;margin:10px;">
    <legend> <h3><b style="color: orange" >|&nbsp;</b><span class="i18n1" name="datadisplay">数据展示</span></h3></legend>
    <div  style="margin-top:5px;">
        <table class="easyui-datagrid" id="accountDatagrids" url="/AccountOperation/getAccountByLike.action" striped="true" loadMsg="正在加载中。。。" textField="text" pageSize="20" fitColumns="true" pagination="true" toolbar="#yyaccountTb">
            <thead>
            <tr>
                <th data-options="field:'ck',checkbox:true"></th>
                <th field="id" align="center" width="100" class="i18n1" name="id"></th>
                <th field="username" align="center" width="100" class="i18n1" name="username"></th>
                <th field="password" align="center" width="100" class="i18n1" name="password"></th>
                <th field="cell_phone" align="center" width="100" class="i18n1" name="cellphone"></th>
                <th field="account_type" align="center" width="100" class="i18n1" name="accounttype"></th>
                <th field="person_user_no" align="center" width="100" class="i18n1" name="personuserno"></th>
                <th field="company_user_no" align="center" width="100" class="i18n1" name="companyuserno"></th>
                <th field="system_user_no" align="center" width="100" class="i18n1" name="systemuserno"></th>
                <th field="register_time" align="center" width="100" class="i18n1" name="registertime" data-options="formatter:formatterdate"></th>
                <th field="account_status" align="center" width="100" class="i18n1" name="accountstatus"></th>
                <th field="role_no_list" align="center" width="100" class="i18n1" name="rolenolist"></th>
                <th field="icon_url" align="center" width="100" class="i18n1" name="iconurl"></th>
            </tr>
            </thead>
        </table>
    </div>
</fieldset>
<!--工具栏-->
<div id="yyaccountTb" style="padding:10px;">
    <span class="i18n1" name="username"></span>:
    <input id="username"  style="line-height:22px;border:1px solid #ccc">
    <span class="i18n1" name="accountstatus"></span>:
    <input id="account_status"  style="line-height:22px;border:1px solid #ccc">
    <span class="i18n1" name="accounttype"></span>:
    <input id="account_type"  style="line-height:22px;border:1px solid #ccc">
    <a href="#" class="easyui-linkbutton" plain="true" data-options="iconCls:'icon-search'" onclick="searchAccount()">Search</a>
    <div style="float:right">
        <a href="#" id="addAccountLinkBtn" class="easyui-linkbutton i18n1" name="add" data-options="iconCls:'icon-add',plain:true" onclick="addAccount()">添加</a>
        <a href="#" id="editAccountLinkBtn" class="easyui-linkbutton i18n1" name="edit" data-options="iconCls:'icon-edit',plain:true" onclick="editAccount()">修改</a>
        <a href="#" id="deltAccountLinkBtn" class="easyui-linkbutton i18n1" name="delete" data-options="iconCls:'icon-remove',plain:true" onclick="delAccount()">删除</a>
    </div>
</div>
<!--添加、修改框-->
<div id="yyAccountDialog" class="easyui-dialog" data-options="title:'添加',modal:true"  closed="true" buttons="#dlg-buttons" style="display: none;padding:5px;width:950px;height:auto;">
    <form id="accountForm" method="post">
        <fieldset style="width:900px;border:solid 1px #aaa;margin-top:8px;position:relative;">
            <legend>账户信息</legend>
            <table class="ht-table">
                <tr>
                    <td class="i18n1" name="id"></td>
                    <td colspan="5"><input class="easyui-textbox" type="text" name="id" readonly="true" value=""/></td>
                </tr>
                <tr>
                    <td width="16%" class="i18n1" name="username"></td>
                    <td>
                        <input class="easyui-textbox" type="text" value="" name="username" />
                    </td>
                    <td></td>
                    <td width="16%" class="i18n1" name="password"></td>
                    <td>
                        <input class="easyui-textbox"  name="password" type="password" style="width:150px;height:22px;padding:12px" data-options="prompt:'登录密码',iconCls:'icon-lock',iconWidth:38">
                    </td>
                    <td></td>
                </tr>
                <tr>
                    <td width="16%"  class="i18n1" name="cellphone"></td>
                    <td><input class="easyui-textbox" type="text" name="cell_phone" value=""/></td>
                    <td></td>
                    <td width="16%"  class="i18n1" name="accounttype"></td>
                    <td>
                        <select class="easyui-combobox" name="account_type" data-options="editable:false" style="width:200px;">
                            <option value="个人用户">个人用户</option>
                            <option value="企业商户">企业商户</option>
                            <option value="后台管理人员">后台管理人员</option>
                        </select>
                    </td>
                    <td></td>
                </tr>
                <tr>
                    <td class="i18n1" name="personuserno"></td>
                    <td><input class="easyui-textbox" type="text" name="person_user_no" value=""/></td>
                    <td></td>
                    <td class="i18n1" name="companyuserno"></td>
                    <td><input class="easyui-textbox"  type="text" name="company_user_no" value=""/></td>
                    <td></td>
                </tr>
                <tr>
                    <td class="i18n1" name="systemuserno"></td>
                    <td><input class="easyui-textbox"  type="text"  name="system_user_no" value=""/></td>
                    <td></td>
                    <td width="16%" class="i18n1" name="accountstatus"></td>
                    <td><select id="cc" class="easyui-combobox" name="account_status" data-options="editable:false" style="width:200px;">
                        <option value="0">在用</option>
                        <option value="1">停用</option>
                    </select></td>
                    <td></td>
                </tr>
                <tr>
                    <td width="16%" class="i18n1" name="registertime"></td>
                    <td><input class="easyui-datetimebox" type="text"  name="register_time" value="" data-options="formatter:myformatter2,parser:myparser2"/>
                    </td>
                    <td></td>
                    <td width="16%" class="i18n1" name="lastlogintime"></td>
                    <td><input class="easyui-datetimebox" type="text" name="last_login_time" value="" data-options="formatter:myformatter2,parser:myparser2"/>
                    </td>
                    <td></td>
                </tr>
                <tr>
                    <td class="i18n1" name="iconurl"></td>
                    <td colspan="5"><input class="easyui-textbox" type="text"  name="icon_url" value=""/>
                    </td>
                </tr>
                <tr>
                    <td>角色列表</td>
                    <td colspan="5">
                        <input id="lookup2" name="role_no_list" class="mini-lookup hl-mini-input" style="width:400px;"
                               textField="role_no" valueField="role_no" popupWidth="auto"
                               popup="#gridPanel" grid="#datagrid1" multiSelect="true"
                        />
                        <div id="gridPanel" class="mini-panel" title="header" iconCls="icon-add" style="width:450px;height:250px;"
                             showToolbar="true" showCloseButton="true" showHeader="false" bodyStyle="padding:0" borderStyle="border:0"
                        >
                            <div property="toolbar" style="padding:5px;padding-left:8px;text-align:center;">
                                <div style="float:left;padding-bottom:2px;">
                                    <span>角色编号或名称：</span>
                                    <input id="keyText" class="mini-textbox" style="width:160px;" onenter="onSearchClick"/>
                                    <a class="mini-button" onclick="onSearchClick">查询</a>
                                    <a class="mini-button" onclick="onClearClick">清除</a>
                                </div>
                                <div style="float:right;padding-bottom:2px;">
                                    <a class="mini-button" onclick="onCloseClick">关闭</a>
                                </div>
                                <div style="clear:both;"></div>
                            </div>
                            <div id="datagrid1" class="mini-datagrid" style="width:100%;height:100%;"
                                 borderStyle="border:0" showPageSize="false" showPageIndex="false"
                                 url="/Role/getAllRoleByLike.action"
                            >
                                <div property="columns">
                                    <div type="checkcolumn" ></div>
                                    <div field="id" width="120" headerAlign="center" allowSort="true" class="i18n1" name="id">流水号</div>
                                    <div field="role_no" width="120" headerAlign="center" allowSort="true" class="i18n1" name="roleno">角色编号</div>
                                    <div field="role_name" width="120" headerAlign="center" allowSort="true" class="i18n1" name="rolename">角色名称</div>
                                </div>
                            </div>
                        </div>
                    </td>
                </tr>
            </table>
        </fieldset>
    </form>
</div>
<div id="dlg-buttons" align="center" style="width:900px;">
    <a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="accountFormSubmit()">Save</a>
    <a href="#" class="easyui-linkbutton" id="yyCancelBtn" operationtype="add" iconCls="icon-cancel" onclick="accountCancelSubmit()">Cancel</a>
</div>
<script type="text/javascript" src="../easyui/jquery.easyui.min.js"></script>
</body>
</html>
<script type="text/javascript">
    mini.parse();
    var grid = mini.get("datagrid1");
    var keyText = mini.get("keyText");
    var look2= mini.get("lookup2");
    //grid.load();
    // look2.on('valuechanged',function () {
    //    var rows = grid.getSelected();
    //    $("input[name='function_no_list']").val(rows.function_no);
    // });
    function onSearchClick(e) {
        grid.load({
            role_no: keyText.value,
            role_name:keyText.value
        });
    }
    function onCloseClick(e) {
        look2.hidePopup();
    }
    function onClearClick(e) {
        look2.deselectAll();
    }
    look2.on("showpopup",function(e){
        $('.mini-shadow').css('z-index','99999');
        $('.mini-popup').css('z-index','100000');
        $('.mini-panel').css('z-index','100000');
        $('#searchBar2').css('display','block');
        grid.load({
            role_no: keyText.value,
            role_name:keyText.value
        });
    });
    $(function () {
        $('.hl-mini-input .mini-buttonedit-border .mini-buttonedit-input').css("width","360px");
    });
    hlLanguage("../i18n/");
</script>

