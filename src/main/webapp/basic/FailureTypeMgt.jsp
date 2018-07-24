<%--
  Created by IntelliJ IDEA.
  User: kurt
  Date: 7/24/18
  Time: 12:25 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>故障类型管理</title>
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
            $('#yyFailureDialog').dialog({
                onClose:function () {
                    var type=$('#yyCancelBtn').attr('operationtype');
                    if(type!="add"){
                        clearFormLabel();
                    }
                }
            });
            $('.mini-buttonedit .mini-buttonedit-input').css('width','150px');
        });
        function addFailure(){
            $('#yyCancelBtn').attr('operationtype','add');
            $('#yyFailureDialog').dialog('open').dialog('setTitle','新增');
            $('#FailureForm').form('clear');
            $("input[name='id']").val('0');
            url="/FailureType/saveFailureType.action";
        }
        function delFailure() {
            var row = $('#FailureDatagrids').datagrid('getSelections');
            if(row.length>0){
                var idArr=[];
                for (var i=0;i<row.length;i++){
                    idArr.push(row[i].id);
                }
                var idArrs=idArr.join(',');
                $.messager.confirm('系统提示',"您确定要删除这<font color=red>"+idArr.length+ "</font>条数据吗？",function (r) {
                    if(r){
                        $.post(
                            "/FailureType/delFailureType.action",
                            {"hlparam":idArrs},function (data) {
                                if(data.success){
                                    $("#FailureDatagrids").datagrid("reload");
                                }
                                yyAlertFour(data.message);
                            },"json");
                    }
                });
            }else{
                yyAlertOne();
            }
        }
        function editFailure() {
            $('#yyCancelBtn').attr('operationtype','edit');
            var row = $('#FailureDatagrids').datagrid('getSelected');
            if(row){
                $('#yyFailureDialog').dialog('open').dialog('setTitle','修改');
                $('#FailureForm').form('load',row);
                url="/FailureType/saveFailureType.action";
            }else{
                yyAlertTwo();
            }
        }
        function searchFailure() {
            $('#FailureDatagrids').datagrid('load',{
                'failure_type_name':$('#failuretypename').val()
            });
        }
        function FailureFormSubmit() {
            $('#FailureForm').form('submit',{
                url:url,
                onSubmit:function () {
                    if($("input[name='failure_type_code']").val()==""){
                        yyAlertFour("请输入故障类型编号");
                        return false;
                    }else if($("input[name='failure_type_name']").val()==""){
                        yyAlertFour("请输入故障类型名称");
                        return false;
                    }
                    else if($("input[name='failure_type_name_en']").val()==""){
                        yyAlertFour("请输入故障类型名称（英文）");
                        return false;
                    }
                },
                success: function(result){
                    //alert(result);
                    var result = eval('('+result+')');
                    if (result.success){
                        $('#yyFailureDialog').dialog('close');
                        $('#FailureDatagrids').datagrid('reload');
                        clearFormLabel();
                    }
                    yyAlertFour(result.message);
                },
                error:function () {
                    yyAlertThree();
                }
            });
        }
        function FailureCancelSubmit() {
            $('#yyFailureDialog').dialog('close');
        }
        function  clearFormLabel() {
            $('#FailureForm').form('clear');
        }
    </script>
</head>
<body>
<fieldset class="b3" style="padding:10px;margin:10px;">
    <legend> <h3><b style="color: orange" >|&nbsp;</b><span class="i18n1" name="datadisplay">数据展示</span></h3></legend>
    <div  style="margin-top:5px;">
        <table class="easyui-datagrid" id="FailureDatagrids" url="/FailureType/getFailureTypeByLike.action" striped="true" loadMsg="正在加载中。。。" textField="text" pageSize="20" fitColumns="true" pagination="true" toolbar="#yyFailureTb">
            <thead>
            <tr>
                <th data-options="field:'ck',checkbox:true"></th>
                <th field="id" align="center" width="100" class="i18n1" name="id"></th>
                <th field="failure_type_code" align="center" width="100" class="i18n1" name="failuretypecode"></th>
                <th field="failure_type_name" align="center" width="100" class="i18n1" name="failuretypename"></th>
                <th field="failure_type_name_en" align="center" width="100" class="i18n1" name="failuretypenameen"></th>
            </tr>
            </thead>
        </table>
    </div>
</fieldset>
<!--工具栏-->
<div id="yyFailureTb" style="padding:10px;">
    <span class="i18n1" name="failuretypename"></span>:
    <input id="failuretypename"  style="line-height:22px;border:1px solid #ccc">
    <a href="#" class="easyui-linkbutton" plain="true" data-options="iconCls:'icon-search'" onclick="searchFailure()">Search</a>
    <div style="float:right">
        <a href="#" id="addFailureLinkBtn" class="easyui-linkbutton i18n1" name="add" data-options="iconCls:'icon-add',plain:true" onclick="addFailure()">添加</a>
        <a href="#" id="editFailureLinkBtn" class="easyui-linkbutton i18n1" name="edit" data-options="iconCls:'icon-edit',plain:true" onclick="editFailure()">修改</a>
        <a href="#" id="deltFailureLinkBtn" class="easyui-linkbutton i18n1" name="delete" data-options="iconCls:'icon-remove',plain:true" onclick="delFailure()">删除</a>
    </div>
</div>
<!--添加、修改框-->
<div id="yyFailureDialog" class="easyui-dialog" data-options="title:'添加',modal:true"  closed="true" buttons="#dlg-buttons" style="display: none;padding:5px;width:950px;height:auto;">
    <form id="FailureForm" method="post">
        <fieldset style="width:900px;border:solid 1px #aaa;margin-top:8px;position:relative;">
            <legend>故障信息</legend>
            <table class="ht-table">
                <tr>
                    <td class="i18n1" name="id"></td>
                    <td colspan="5"><input class="easyui-textbox" type="text" name="id" readonly="true" value="0"/></td>
                </tr>
                <tr>
                    <td class="i18n1" name="failuretypecode"></td>
                    <td>
                        <input class="easyui-textbox" type="text" value="" name="failure_type_code" />
                    </td>
                    <td></td>
                    <td class="i18n1" name="failuretypename"></td>
                    <td>
                        <input class="easyui-textbox" type="text" value="" name="failure_type_name" />
                    </td>
                    <td></td>
                </tr>
                <tr>
                    <td class="i18n1" name="failuretypenameen"></td>
                    <td>
                        <input class="easyui-textbox" type="text" value="" name="failure_type_name_en" />
                    </td>
                    <td></td>
                </tr>

            </table>
        </fieldset>
    </form>
</div>
<div id="dlg-buttons" align="center" style="width:900px;">
    <a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="FailureFormSubmit()">Save</a>
    <a href="#" class="easyui-linkbutton" id="yyCancelBtn" operationtype="add" iconCls="icon-cancel" onclick="FailureCancelSubmit()">Cancel</a>
</div>
<script type="text/javascript" src="../easyui/jquery.easyui.min.js"></script>
</body>
</html>
<script type="text/javascript">
    mini.parse();


    hlLanguage("../i18n/");
</script>



