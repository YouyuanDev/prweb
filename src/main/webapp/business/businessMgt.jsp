<%--
  Created by IntelliJ IDEA.
  User: kurt
  Date: 7/17/18
  Time: 5:25 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>业务信息管理</title>
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
            $('#yyBusinessDialog').dialog({
                onClose:function () {
                    var type=$('#yyCancelBtn').attr('operationtype');
                    if(type!="add"){
                        clearFormLabel();
                    }
                }
            });
            $('.mini-buttonedit .mini-buttonedit-input').css('width','150px');
        });
        function addBusiness(){
            $('#yyCancelBtn').attr('operationtype','add');
            $('#yyBusinessDialog').dialog('open').dialog('setTitle','新增');
            $('#BusinessForm').form('clear');
            $("input[name='id']").val('0');
            url="/Business/saveBusiness.action";
        }
        function delBusiness() {
            var row = $('#BusinessDatagrids').datagrid('getSelections');
            if(row.length>0){
                var idArr=[];
                for (var i=0;i<row.length;i++){
                    idArr.push(row[i].id);
                }
                var idArrs=idArr.join(',');
                $.messager.confirm('系统提示',"您确定要删除这<font color=red>"+idArr.length+ "</font>条数据吗？",function (r) {
                    if(r){
                        $.post(
                            "/Business/delBusiness.action",
                            {hlparam:idArrs},function (data) {
                                if(data.success){
                                    $("#BusinessDatagrids").datagrid("reload");
                                }
                                yyAlertFour(data.message);
                            },"json");
                    }
                });
            }else{
                yyAlertOne();
            }
        }
        function editBusiness() {
            $('#yyCancelBtn').attr('operationtype','edit');
            var row = $('#BusinessDatagrids').datagrid('getSelected');
            if(row){
                $('#yyBusinessDialog').dialog('open').dialog('setTitle','修改');
                row.create_time=getDate1(row.create_time);
                $('#BusinessForm').form('load',row);
                url="/Business/saveBusiness.action";
            }else{
                yyAlertTwo();
            }
        }
        function searchBusiness() {
            $('#BusinessDatagrids').datagrid('load',{
                'business_no': $('#business_no').val(),
                'business_name': $('#business_name').val(),
                'business_type':$('#business_type').val()
            });
        }
        function BusinessFormSubmit() {
            $('#BusinessForm').form('submit',{
                url:url,
                onSubmit:function () {
                    if($("input[name='business_no']").val()==""){
                        yyAlertFour("请输入业务编号");
                        return false;
                    }else if($("input[name='business_name']").val()==""){
                        yyAlertFour("请输入业务名称");
                        return false;
                    }
                    else if($("input[name='create_time']").val()==""){
                        yyAlertFour("请输入创建日期");
                        return false;
                    }
                },
                success: function(result){
                    //alert(result);
                    var result = eval('('+result+')');
                    if (result.success){
                        $('#yyBusinessDialog').dialog('close');
                        $('#BusinessDatagrids').datagrid('reload');
                        clearFormLabel();
                    }
                    yyAlertFour(result.message);
                },
                error:function () {
                    yyAlertThree();
                }
            });
        }
        function BusinessCancelSubmit() {
            $('#yyBusinessDialog').dialog('close');
        }
        function  clearFormLabel() {
            $('#BusinessForm').form('clear');
        }
    </script>
</head>
<body>
<fieldset class="b3" style="padding:10px;margin:10px;">
    <legend> <h3><b style="color: orange" >|&nbsp;</b><span class="i18n1" name="datadisplay">数据展示</span></h3></legend>
    <div  style="margin-top:5px;">
        <table class="easyui-datagrid" id="BusinessDatagrids" url="/Business/getBusinessByLike.action" striped="true" loadMsg="正在加载中。。。" textField="text" pageSize="20" fitColumns="true" pagination="true" toolbar="#yyBusinessTb">
            <thead>
            <tr>
                <th data-options="field:'ck',checkbox:true"></th>
                <th field="id" align="center" width="100" class="i18n1" name="id"></th>
                <th field="business_no" align="center" width="100" class="i18n1" name="businessno"></th>
                <th field="business_name" align="center" width="100" class="i18n1" name="businessname"></th>
                <th field="business_type" align="center" width="100" class="i18n1" name="businesstype"></th>
                <th field="create_time" align="center" width="100" class="i18n1" name="createtime" data-options="formatter:formatterdate"></th>
                <th field="valid" align="center" width="100" class="i18n1" name="valid"></th>
            </tr>
            </thead>
        </table>
    </div>
</fieldset>
<!--工具栏-->
<div id="yyBusinessTb" style="padding:10px;">
    <span class="i18n1" name="businessno"></span>:
    <input id="business_no"  style="line-height:22px;border:1px solid #ccc">
    <span class="i18n1" name="businessname"></span>:
    <input id="business_name"  style="line-height:22px;border:1px solid #ccc">
    <span class="i18n1" name="businesstype"></span>:
    <input id="business_type"  style="line-height:22px;border:1px solid #ccc">
    <a href="#" class="easyui-linkbutton" plain="true" data-options="iconCls:'icon-search'" onclick="searchBusiness()">Search</a>
    <div style="float:right">
        <a href="#" id="addBusinessLinkBtn" class="easyui-linkbutton i18n1" name="add" data-options="iconCls:'icon-add',plain:true" onclick="addBusiness()">添加</a>
        <a href="#" id="editBusinessLinkBtn" class="easyui-linkbutton i18n1" name="edit" data-options="iconCls:'icon-edit',plain:true" onclick="editBusiness()">修改</a>
        <a href="#" id="deltBusinessLinkBtn" class="easyui-linkbutton i18n1" name="delete" data-options="iconCls:'icon-remove',plain:true" onclick="delBusiness()">删除</a>
    </div>
</div>
<!--添加、修改框-->
<div id="yyBusinessDialog" class="easyui-dialog" data-options="title:'添加',modal:true"  closed="true" buttons="#dlg-buttons" style="display: none;padding:5px;width:950px;height:auto;">
    <form id="BusinessForm" method="post">
        <fieldset style="width:900px;border:solid 1px #aaa;margin-top:8px;position:relative;">
            <legend>业务信息</legend>
            <table class="ht-table">
                <tr>
                    <td class="i18n1" name="id"></td>
                    <td colspan="5"><input class="easyui-textbox" type="text" name="id" readonly="true" value="0"/></td>
                </tr>
                <tr>
                    <td class="i18n1" name="businessno"></td>
                    <td>
                        <input class="easyui-textbox" type="text" value="" name="business_no" />
                    </td>
                    <td></td>
                    <td class="i18n1" name="businessname"></td>
                    <td>
                        <input class="easyui-textbox" type="text" value="" name="business_name" />
                    </td>
                    <td></td>
                </tr>
                <tr>
                    <td  class="i18n1" name="businesstype"></td>
                    <td><input class="easyui-textbox" type="text" name="business_type" value=""/></td>
                    <td></td>
                    <td class="i18n1" name="valid"></td>
                    <td><select id="cc" class="easyui-combobox" name="valid" data-options="editable:false" style="width:200px;">
                        <option value="1">在用</option>
                        <option value="0">停用</option>
                    </select></td>
                    <td></td>
                </tr>
                <tr>
                    <td class="i18n1" name="createtime"></td>
                    <td><input class="easyui-datetimebox" type="text"  name="create_time" value="" data-options="formatter:myformatter2,parser:myparser2"/>
                    </td>
                    <td></td>
                </tr>
            </table>
        </fieldset>
    </form>
</div>
<div id="dlg-buttons" align="center" style="width:900px;">
    <a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="BusinessFormSubmit()">Save</a>
    <a href="#" class="easyui-linkbutton" id="yyCancelBtn" operationtype="add" iconCls="icon-cancel" onclick="BusinessCancelSubmit()">Cancel</a>
</div>
<script type="text/javascript" src="../easyui/jquery.easyui.min.js"></script>
</body>
</html>
<script type="text/javascript">
    mini.parse();


    hlLanguage("../i18n/");
</script>


