<%--
  Created by IntelliJ IDEA.
  User: kurt
  Date: 7/20/18
  Time: 2:39 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>权限管理</title>
    <link rel="stylesheet" type="text/css" href="../easyui/themes/bootstrap/easyui.css">
    <link rel="stylesheet" type="text/css" href="../easyui/themes/icon.css">
    <link href="../miniui/multiupload/multiupload.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" href="../css/common.css"/>
    <script src="../easyui/jquery.min.js" type="text/javascript"></script>
    <script src="../js/common.js" type="text/javascript"></script>
    <script src="../miniui/boot.js" type="text/javascript"></script>
    <script  src="../miniui/js/miniui.js" type="text/javascript"></script>
    <script src="../miniui/fileupload/swfupload/swfupload.js" type="text/javascript"></script>
    <script src="../miniui/multiupload/multiupload.js" type="text/javascript"></script>
    <script  src="../js/lrscroll.js" type="text/javascript"></script>
    <script src="../js/jquery.i18n.properties-1.0.9.js" type="text/javascript"></script>
    <script src="../js/language.js" type="text/javascript"></script>
    <script type="text/javascript">
        var url;
        $(function () {
            $('#yyFunctionDialog').dialog({
                onClose:function () {
                    var type=$('#yyCancelBtn').attr('operationtype');
                    if(type!="add"){
                        clearFormLabel();
                    }
                }
            });
            $('.mini-buttonedit .mini-buttonedit-input').css('width','150px');
        });
        function addFunction(){
            $('#yyCancelBtn').attr('operationtype','add');
            $('#yyFunctionDialog').dialog('open').dialog('setTitle','新增');
            $('#FunctionForm').form('clear');
            $("input[name='id']").val('0');
            url="/Function/saveFunction.action";
        }
        function delFunction() {
            var row = $('#FunctionDatagrids').datagrid('getSelections');
            if(row.length>0){
                var idArr=[];
                for (var i=0;i<row.length;i++){
                    idArr.push(row[i].id);
                }
                var idArrs=idArr.join(',');
                $.messager.confirm('系统提示',"您确定要删除这<font color=red>"+idArr.length+ "</font>条数据吗？",function (r) {
                    if(r){
                        $.post(
                            "/Function/delFunction.action",
                            {"hlparam":idArrs},function (data) {
                                if(data.success){
                                    $("#FunctionDatagrids").datagrid("reload");
                                }
                                yyAlertFour(data.message);
                            },"json");
                    }
                });
            }else{
                yyAlertOne();
            }
        }
        function editFunction() {
            $('#yyCancelBtn').attr('operationtype','edit');
            var row = $('#FunctionDatagrids').datagrid('getSelected');
            if(row){
                $('#yyFunctionDialog').dialog('open').dialog('setTitle','修改');
                $('#FunctionForm').form('load',row);
                url="/Function/saveFunction.action";
            }else{
                yyAlertTwo();
            }
        }
        function searchFunction() {
            $('#FunctionDatagrids').datagrid('load',{
                'function_no': $('#functionno').val(),
                'function_name': $('#functionname').val()
            });
        }
        function FunctionFormSubmit() {
            $('#FunctionForm').form('submit',{
                url:url,
                onSubmit:function () {
                    if($("input[name='function_no']").val()==""){
                        yyAlertFour("请输入权限编号");
                        return false;
                    }
                    else if($("input[name='function_name']").val()==""){
                        yyAlertFour("请输入权限名称");
                        return false;
                    }
                },
                success: function(result){
                    var result = eval('('+result+')');
                    if (result.success){
                        $('#yyFunctionDialog').dialog('close');
                        $('#FunctionDatagrids').datagrid('reload');
                        clearFormLabel();
                    }
                    yyAlertFour(result.message);
                },
                error:function () {
                    yyAlertThree();
                }
            });
        }
        function FunctionCancelSubmit() {
            $('#yyFunctionDialog').dialog('close');
        }

        function  clearFormLabel() {
            $('#FunctionForm').form('clear');

        }
    </script>
</head>
<body>

<fieldset class="b3" style="padding:10px;margin:10px;">
    <legend> <h3><b style="color: orange" >|&nbsp;</b><span class="i18n1" name="datadisplay">数据展示</span></h3></legend>
    <div  style="margin-top:5px;">
        <table class="easyui-datagrid" id="FunctionDatagrids" url="/Function/getFunctionByLike.action" striped="true" loadMsg="正在加载中。。。" textField="text" pageSize="20" fitColumns="true" pagination="true" toolbar="#yyFunctionTb">
            <thead>
            <tr>
                <th data-options="field:'ck',checkbox:true"></th>
                <th field="id" align="center" width="100" class="i18n1" name="id"></th>
                <th field="module_name" align="center" width="100" class="i18n1" name="modulename"></th>
                <th field="function_no" align="center" width="100" class="i18n1" name="functionno"></th>
                <th field="function_name" align="center" width="100" class="i18n1" name="functionname"></th>
                <th field="operation_type" align="center" width="100" class="i18n1" name="operationtype"></th>
                <th field="uri" align="center" width="100" class="i18n1" name="uri"></th>
            </tr>
            </thead>
        </table>
    </div>
</fieldset>

<!--工具栏-->
<div id="yyFunctionTb" style="padding:10px;">
    <span class="i18n1" name="functionno">权限编号</span>:
    <input id="functionno" name="functionno" style="line-height:22px;border:1px solid #ccc">
    <span class="i18n1" name="functionname">权限名称</span>:
    <input id="functionname" name="functionname" style="line-height:22px;border:1px solid #ccc">

    <a href="#" class="easyui-linkbutton" plain="true" data-options="iconCls:'icon-search'" onclick="searchFunction()">Search</a>
    <div style="float:right">
        <a href="#" id="addFunctionLinkBtn" class="easyui-linkbutton i18n1" name="add" data-options="iconCls:'icon-add',plain:true" onclick="addFunction()">添加</a>
        <a href="#" id="editFunctionLinkBtn" class="easyui-linkbutton i18n1" name="edit" data-options="iconCls:'icon-edit',plain:true" onclick="editFunction()">修改</a>
        <a href="#" id="deltFunctionLinkBtn" class="easyui-linkbutton i18n1" name="delete" data-options="iconCls:'icon-remove',plain:true" onclick="delFunction()">删除</a>
    </div>
</div>


<!--添加、修改框-->
<div id="yyFunctionDialog" class="easyui-dialog" data-options="title:'添加',modal:true"  closed="true" buttons="#dlg-buttons" style="display: none;padding:5px;width:950px;height:auto;">
    <form id="FunctionForm" method="post">


        <fieldset style="width:900px;border:solid 1px #aaa;margin-top:8px;position:relative;">
            <legend>权限信息</legend>

            <table class="ht-table">
                <tr>
                    <td class="i18n1" name="id">流水号</td>
                    <td colspan="1"><input class="easyui-textbox" type="text" name="id" readonly="true" value="0"/></td>
                    <td></td>
                    <td class="i18n1" name="modulename">模块名称</td>
                    <td colspan="1">
                        <input class="easyui-textbox" name="module_name" type="text" style="width:150px;height:22px;padding:12px" >
                    </td>
                    <td></td>
                </tr>
                <tr>
                    <td class="i18n1" name="functionno">权限编号</td>
                    <td colspan="1" >
                        <input class="easyui-textbox" type="text" value="" name="function_no" />
                    </td>
                    <td></td>
                    <td class="i18n1" name="functionname">权限名称</td>
                    <td colspan="1">
                        <input class="easyui-textbox" name="function_name" type="text" style="width:150px;height:22px;padding:12px" >
                    </td>
                    <td></td>

                </tr>
                <tr>
                    <td class="i18n1" name="operationtype">操作类型</td>
                    <td colspan="1" >
                        <select class="easyui-combobox" name="operation_type"  data-options="editable:false" style="width:200px;">
                            <option value="r">查看</option>
                            <option value="w">编辑</option>
                            <option value="d">删除</option>
                        </select>
                    </td>
                    <td></td>
                    <td class="i18n1" name="uri">URI名称</td>
                    <td colspan="1">
                        <input class="easyui-textbox" name="uri" type="text" style="width:150px;height:22px;padding:12px" >
                    </td>
                    <td></td>
                </tr>
            </table>



        </fieldset>
    </form>


</div>
<div id="dlg-buttons" align="center" style="width:900px;">
    <a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="FunctionFormSubmit()">Save</a>
    <a href="#" class="easyui-linkbutton" id="yyCancelBtn" operationtype="add" iconCls="icon-cancel" onclick="FunctionCancelSubmit()">Cancel</a>
</div>


<script type="text/javascript" src="../easyui/jquery.easyui.min.js"></script>
</body>
</html>
<script type="text/javascript">
    mini.parse();

    hlLanguage("../i18n/");
</script>

