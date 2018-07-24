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
    <title>服务类型管理</title>
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
            $('#yyServiceDialog').dialog({
                onClose:function () {
                    var type=$('#yyCancelBtn').attr('operationtype');
                    if(type!="add"){
                        clearFormLabel();
                    }
                }
            });
            $('.mini-buttonedit .mini-buttonedit-input').css('width','150px');
        });
        function addService(){
            $('#yyCancelBtn').attr('operationtype','add');
            $('#yyServiceDialog').dialog('open').dialog('setTitle','新增');
            $('#ServiceForm').form('clear');
            $("input[name='id']").val('0');
            url="/ServiceType/saveServiceType.action";
        }
        function delService() {
            var row = $('#ServiceDatagrids').datagrid('getSelections');
            if(row.length>0){
                var idArr=[];
                for (var i=0;i<row.length;i++){
                    idArr.push(row[i].id);
                }
                var idArrs=idArr.join(',');
                $.messager.confirm('系统提示',"您确定要删除这<font color=red>"+idArr.length+ "</font>条数据吗？",function (r) {
                    if(r){
                        $.post(
                            "/ServiceType/delServiceType.action",
                            {"hlparam":idArrs},function (data) {
                                if(data.success){
                                    $("#ServiceDatagrids").datagrid("reload");
                                }
                                yyAlertFour(data.message);
                            },"json");
                    }
                });
            }else{
                yyAlertOne();
            }
        }
        function editService() {
            $('#yyCancelBtn').attr('operationtype','edit');
            var row = $('#ServiceDatagrids').datagrid('getSelected');
            if(row){
                $('#yyServiceDialog').dialog('open').dialog('setTitle','修改');
                $('#ServiceForm').form('load',row);
                url="/ServiceType/saveServiceType.action";
            }else{
                yyAlertTwo();
            }
        }
        function searchService() {
            $('#ServiceDatagrids').datagrid('load',{
                'service_type_name':$('#servicetypename').val()
            });
        }
        function ServiceFormSubmit() {
            $('#ServiceForm').form('submit',{
                url:url,
                onSubmit:function () {
                    if($("input[name='service_type_code']").val()==""){
                        yyAlertFour("请输入服务类型编号");
                        return false;
                    }else if($("input[name='service_type_name']").val()==""){
                        yyAlertFour("请输入服务类型名称");
                        return false;
                    }
                    else if($("input[name='service_type_name_en']").val()==""){
                        yyAlertFour("请输入服务类型名称（英文）");
                        return false;
                    }
                },
                success: function(result){
                    //alert(result);
                    var result = eval('('+result+')');
                    if (result.success){
                        $('#yyServiceDialog').dialog('close');
                        $('#ServiceDatagrids').datagrid('reload');
                        clearFormLabel();
                    }
                    yyAlertFour(result.message);
                },
                error:function () {
                    yyAlertThree();
                }
            });
        }
        function ServiceCancelSubmit() {
            $('#yyServiceDialog').dialog('close');
        }
        function  clearFormLabel() {
            $('#ServiceForm').form('clear');
        }
    </script>
</head>
<body>
<fieldset class="b3" style="padding:10px;margin:10px;">
    <legend> <h3><b style="color: orange" >|&nbsp;</b><span class="i18n1" name="datadisplay">数据展示</span></h3></legend>
    <div  style="margin-top:5px;">
        <table class="easyui-datagrid" id="ServiceDatagrids" url="/ServiceType/getServiceTypeByLike.action" striped="true" loadMsg="正在加载中。。。" textField="text" pageSize="20" fitColumns="true" pagination="true" toolbar="#yyServiceTb">
            <thead>
            <tr>
                <th data-options="field:'ck',checkbox:true"></th>
                <th field="id" align="center" width="100" class="i18n1" name="id"></th>
                <th field="service_type_code" align="center" width="100" class="i18n1" name="servicetypecode"></th>
                <th field="service_type_name" align="center" width="100" class="i18n1" name="servicetypename"></th>
                <th field="service_type_name_en" align="center" width="100" class="i18n1" name="servicetypenameen"></th>
            </tr>
            </thead>
        </table>
    </div>
</fieldset>
<!--工具栏-->
<div id="yyServiceTb" style="padding:10px;">
    <span class="i18n1" name="servicetypename"></span>:
    <input id="servicetypename"  style="line-height:22px;border:1px solid #ccc">
    <a href="#" class="easyui-linkbutton" plain="true" data-options="iconCls:'icon-search'" onclick="searchService()">Search</a>
    <div style="float:right">
        <a href="#" id="addServiceLinkBtn" class="easyui-linkbutton i18n1" name="add" data-options="iconCls:'icon-add',plain:true" onclick="addService()">添加</a>
        <a href="#" id="editServiceLinkBtn" class="easyui-linkbutton i18n1" name="edit" data-options="iconCls:'icon-edit',plain:true" onclick="editService()">修改</a>
        <a href="#" id="deltServiceLinkBtn" class="easyui-linkbutton i18n1" name="delete" data-options="iconCls:'icon-remove',plain:true" onclick="delService()">删除</a>
    </div>
</div>
<!--添加、修改框-->
<div id="yyServiceDialog" class="easyui-dialog" data-options="title:'添加',modal:true"  closed="true" buttons="#dlg-buttons" style="display: none;padding:5px;width:950px;height:auto;">
    <form id="ServiceForm" method="post">
        <fieldset style="width:900px;border:solid 1px #aaa;margin-top:8px;position:relative;">
            <legend>服务信息</legend>
            <table class="ht-table">
                <tr>
                    <td class="i18n1" name="id"></td>
                    <td colspan="5"><input class="easyui-textbox" type="text" name="id" readonly="true" value="0"/></td>
                </tr>
                <tr>
                    <td class="i18n1" name="servicetypecode"></td>
                    <td>
                        <input class="easyui-textbox" type="text" value="" name="service_type_code" />
                    </td>
                    <td></td>
                    <td class="i18n1" name="servicetypename"></td>
                    <td>
                        <input class="easyui-textbox" type="text" value="" name="service_type_name" />
                    </td>
                    <td></td>
                </tr>
                <tr>
                    <td class="i18n1" name="servicetypenameen"></td>
                    <td>
                        <input class="easyui-textbox" type="text" value="" name="service_type_name_en" />
                    </td>
                    <td></td>
                </tr>

            </table>
        </fieldset>
    </form>
</div>
<div id="dlg-buttons" align="center" style="width:900px;">
    <a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="ServiceFormSubmit()">Save</a>
    <a href="#" class="easyui-linkbutton" id="yyCancelBtn" operationtype="add" iconCls="icon-cancel" onclick="ServiceCancelSubmit()">Cancel</a>
</div>
<script type="text/javascript" src="../easyui/jquery.easyui.min.js"></script>
</body>
</html>
<script type="text/javascript">
    mini.parse();


    hlLanguage("../i18n/");
</script>



