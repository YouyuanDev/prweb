<%--
  Created by IntelliJ IDEA.
  User: kurt
  Date: 7/20/18
  Time: 2:38 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>角色管理</title>
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
            $('#yyRoleDialog').dialog({
                onClose:function () {
                    var type=$('#yyCancelBtn').attr('operationtype');
                    if(type!="add"){
                        clearFormLabel();
                    }
                }
            });
            $('.mini-buttonedit .mini-buttonedit-input').css('width','150px');
        });
        function addRole(){
            $('#yyCancelBtn').attr('operationtype','add');
            $('#yyRoleDialog').dialog('open').dialog('setTitle','新增');
            $('#RoleForm').form('clear');
            $("input[name='id']").val('0');
            url="/Role/saveRole.action";
            look2.deselectAll();
        }
        function delRole() {
            var row = $('#RoleDatagrids').datagrid('getSelections');
            if(row.length>0){
                var idArr=[];
                for (var i=0;i<row.length;i++){
                    idArr.push(row[i].id);
                }
                var idArrs=idArr.join(',');
                $.messager.confirm('系统提示',"您确定要删除这<font color=red>"+idArr.length+ "</font>条数据吗？",function (r) {
                    if(r){
                        $.post(
                            "/Role/delRole.action",
                            {"hlparam":idArrs},function (data) {
                                if(data.success){
                                    $("#RoleDatagrids").datagrid("reload");
                                }
                                yyAlertFour(data.message);
                            },"json");
                    }
                });
            }else{
                yyAlertOne();
            }
        }
        function editRole() {
            $('#yyCancelBtn').attr('operationtype','edit');
            var row = $('#RoleDatagrids').datagrid('getSelected');
            if(row){
                $('#yyRoleDialog').dialog('open').dialog('setTitle','修改');
                $('#RoleForm').form('load',row);
                combox1.setValue(row.push_event_list);
                look2.setText(row.function_no_list);
                look2.setValue(row.function_no_list);
                url="/Role/saveRole.action";
            }else{
                yyAlertTwo();
            }
        }
        function searchRole() {
            $('#RoleDatagrids').datagrid('load',{
                'role_no': $('#roleno').val(),
                'role_name': $('#rolename').val()
            });
        }
        function RoleFormSubmit() {
            $('#RoleForm').form('submit',{
                url:url,
                onSubmit:function () {
                    if($("input[name='role_no']").val()==""){

                        yyAlertFour("请输入角色编号");
                        return false;
                    }
                    else if($("input[name='role_name']").val()==""){

                        yyAlertFour("请输入角色名称");
                        return false;
                    }
                },
                success: function(result){
                    var result = eval('('+result+')');
                    if (result.success){
                        $('#yyRoleDialog').dialog('close');
                        $('#RoleDatagrids').datagrid('reload');
                        clearFormLabel();
                    }
                    yyAlertFour(result.message);
                },
                error:function () {
                    yyAlertThree();
                }
            });
        }
        function RoleCancelSubmit() {
            $('#yyRoleDialog').dialog('close');
        }
        function  clearFormLabel() {
            $('#RoleForm').form('clear');
            look2.setText('');
            look2.setValue('');
        }
    </script>
</head>

<body>

<fieldset class="b3" style="padding:10px;margin:10px;">
    <legend> <h3><b style="color: orange" >|&nbsp;</b><span class="i18n1" name="datadisplay">数据展示</span></h3></legend>
    <div  style="margin-top:5px;">
        <table class="easyui-datagrid" id="RoleDatagrids" url="/Role/getRoleByLike.action" striped="true" loadMsg="正在加载中。。。" textField="text" pageSize="20" fitColumns="true" pagination="true" toolbar="#yyRoleTb">
            <thead>
            <tr>
                <th data-options="field:'ck',checkbox:true"></th>
                <th field="id" align="center" width="100" class="i18n1" name="id">流水号</th>
                <th field="role_no" align="center" width="100" class="i18n1" name="roleno">角色编号</th>
                <th field="role_name" align="center" width="100" class="i18n1" name="rolename">角色名称</th>
                <th field="function_no_list" align="center" width="100" class="i18n1" name="functionnolist" >权限列表</th>

            </tr>
            </thead>
        </table>

    </div>
</fieldset>

<!--工具栏-->
<div id="yyRoleTb" style="padding:10px;">
    <span class="i18n1" name="roleno">角色编号</span>:
    <input id="roleno" name="roleno" style="line-height:22px;border:1px solid #ccc">
    <span class="i18n1" name="rolename">角色名称</span>:
    <input id="rolename" name="rolename" style="line-height:22px;border:1px solid #ccc">

    <a href="#" class="easyui-linkbutton" plain="true" data-options="iconCls:'icon-search'" onclick="searchRole()">Search</a>
    <div style="float:right">
        <a href="#" id="addRoleLinkBtn" class="easyui-linkbutton i18n1" name="add" data-options="iconCls:'icon-add',plain:true" onclick="addRole()">添加</a>
        <a href="#" id="editRoleLinkBtn" class="easyui-linkbutton i18n1" name="edit" data-options="iconCls:'icon-edit',plain:true" onclick="editRole()">修改</a>
        <a href="#" id="deltRoleLinkBtn" class="easyui-linkbutton i18n1" name="delete" data-options="iconCls:'icon-remove',plain:true" onclick="delRole()">删除</a>
    </div>
</div>


<!--添加、修改框-->
<div id="yyRoleDialog" class="easyui-dialog" data-options="title:'添加',modal:true"  closed="true" buttons="#dlg-buttons" style="display: none;padding:5px;width:950px;height:auto;">
    <form id="RoleForm" method="post">
        <fieldset style="width:900px;border:solid 1px #aaa;margin-top:8px;position:relative;">
            <legend>角色信息</legend>

            <table class="ht-table">
                <tr>
                    <td class="i18n1" name="id">流水号</td>
                    <td colspan="5"><input class="easyui-textbox" type="text" name="id" readonly="true" value="0"/></td>
                </tr>
                <tr>
                    <td class="i18n1" name="roleno">角色编号</td>
                    <td colspan="2" >
                        <input class="easyui-textbox" type="text" value="" name="role_no" />
                    </td>
                    <td class="i18n1" name="rolename">角色名称</td>
                    <td colspan="2">
                        <input class="easyui-textbox" name="role_name" type="text" style="width:150px;height:22px;padding:12px" >
                    </td>

                </tr>
                <tr>
                    <td  class="i18n1" name="pushevent">推送事件</td>
                    <td colspan="5">
                        <div id="combobox1" class="mini-combobox hl-mini-input" style="width:400px;"  popupWidth="185" textField="push_event_name" valueField="push_event"
                             url="/Role/getAllPushEventRule.action" name="push_event_list" multiSelect="true"  showClose="true" oncloseclick="onComboxCloseClick" >
                            <div property="columns">
                                <div header="事件" field="push_event_name"></div>
                            </div>
                        </div>

                    </td>
                </tr>
                <tr>
                    <td class="i18n1" name="functionlist">权限列表</td>
                    <td colspan="5">
                        <input id="lookup2" name="function_no_list" class="mini-lookup hl-mini-input" style="width:400px;"
                               textField="function_no" valueField="function_no" popupWidth="auto"
                               popup="#gridPanel" grid="#datagrid1" multiSelect="true"
                        />

                        <div id="gridPanel" class="mini-panel" title="header" iconCls="icon-add" style="width:450px;height:250px;"
                             showToolbar="true" showCloseButton="true" showHeader="false" bodyStyle="padding:0" borderStyle="border:0"
                        >
                            <div property="toolbar" style="padding:5px;padding-left:8px;text-align:center;">
                                <div style="float:left;padding-bottom:2px;">
                                    <span>功能编号或名称：</span>
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
                                 url="/Function/getFunctionByNoName.action"
                            >
                                <div property="columns">
                                    <div type="checkcolumn" ></div>
                                    <div field="module_name" width="120" headerAlign="center" allowSort="true" class="i18n1" name="modulename">功能模块</div>
                                    <div field="function_no" width="120" headerAlign="center" allowSort="true" class="i18n1" name="functionno">功能编号</div>
                                    <div field="function_name" width="120" headerAlign="center" allowSort="true" class="i18n1" name="functionname">功能名称</div>
                                    <%--<div field="module_function_name" width="120" headerAlign="center" allowSort="true" class="i18n1" name="modulefunctionname">模块功能名称</div>--%>
                                    <div field="uri" width="120" headerAlign="center" allowSort="true" class="i18n1" name="uri">uri</div>
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
    <a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="RoleFormSubmit()">Save</a>
    <a href="#" class="easyui-linkbutton" id="yyCancelBtn" operationtype="add" iconCls="icon-cancel" onclick="RoleCancelSubmit()">Cancel</a>
</div>
<script type="text/javascript" src="../easyui/jquery.easyui.min.js"></script>
</body>
</html>
<script type="text/javascript">
    mini.parse();
    var grid = mini.get("datagrid1");
    var keyText = mini.get("keyText");
    var look2= mini.get("lookup2");
    var combox1=mini.get("combobox1");

    function onSearchClick(e) {
        grid.load({
            function_no: keyText.value,
            function_name:keyText.value
        });
    }
    function onCloseClick(e) {
        var lookup2 = mini.get("lookup2");
        lookup2.hidePopup();
    }
    function onClearClick(e) {
        var lookup2 = mini.get("lookup2");
        lookup2.deselectAll();
    }
    look2.on("showpopup",function(e){
        $('.mini-shadow').css('z-index','99999');
        $('.mini-popup').css('z-index','100000');
        $('.mini-panel').css('z-index','100000');
        $('#searchBar2').css('display','block');
        grid.load({
            function_no: keyText.value,
            function_name:keyText.value
        });
        //$('.mini-buttonedit .mini-buttonedit-input').css('width','150px');
    });
    combox1.on("showpopup",function () {
        $('.mini-shadow').css('z-index','99999');
        $('.mini-popup').css('z-index','100000');
        $('.mini-panel').css('z-index','100000');
    });
    $(function () {
        $('.hl-mini-input .mini-buttonedit-border .mini-buttonedit-input').css("width","360px");
    });
    hlLanguage("../i18n/");
</script>
