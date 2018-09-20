<%--
  Created by IntelliJ IDEA.
  User: kurt
  Date: 7/17/18
  Time: 5:25 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
    String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
%>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>新闻管理</title>
    <link rel="stylesheet" type="text/css" href="../easyui/themes/bootstrap/easyui.css">
    <link rel="stylesheet" type="text/css" href="../easyui/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="../css/common.css"/>
    <link rel="stylesheet" type="text/css" href="../css/bootstrap.css">
    <script src="../easyui/jquery.min.js" type="text/javascript"></script>
    <script src="../js/common.js" type="text/javascript"></script>
    <script src="../miniui/boot.js" type="text/javascript"></script>
    <script  src="../js/lrscroll.js" type="text/javascript"></script>
    <script src="../js/jquery.i18n.properties-1.0.9.js" type="text/javascript"></script>
    <script src="../js/language.js" type="text/javascript"></script>
    <script src="../ckeditor4/ckeditor.js" type="text/javascript"></script>
    <style type="text/css">
        .news-label{
            display: block;
            padding-top:10px;
            padding-bottom:10px;
        }
    </style>
    <script type="text/javascript">
        var url,ckEditor;
        var ctx="<%=basePath%>";
        var basePath ="<%=basePath%>"+"/upload/pictures/";
        $(function () {
            loadCkEditor();
            $('#yyNewsDialog').css('top','5px');
            $('#yyNewsDialog').dialog({
                onClose:function () {
                    var type=$('#yyCancelBtn').attr('operationtype');
                    if(type!="add"){
                        clearFormLabel();
                    }
                }
            });
        });
        function addNews(){
            $('#yyCancelBtn').attr('operationtype','add');
            $('#yyNewsDialog').dialog('open').dialog('setTitle','新增');
            $('#NewsForm').form('clear');
            $("input[name='id']").val('0');
            $('#publish_time').text('');
            url="/News/saveNews.action";
        }
        function delNews() {
            var row = $('#NewsDatagrids').datagrid('getSelections');
            if(row.length>0){
                var idArr=[];
                for (var i=0;i<row.length;i++){
                    idArr.push(row[i].id);
                }
                var idArrs=idArr.join(',');
                $.messager.confirm('系统提示',"您确定要删除这<font color=red>"+idArr.length+ "</font>条数据吗？",function (r) {
                    if(r){
                        $.post(
                            "/News/delNews.action",
                            {hlparam:idArrs},function (data) {
                                if(data.success){
                                    $("#NewsDatagrids").datagrid("reload");
                                }
                                yyAlertFour(data.message);
                            },"json");
                    }
                });
            }else{
                yyAlertOne();
            }
        }
        function editNews() {
            $('#yyCancelBtn').attr('operationtype','edit');
            var row = $('#NewsDatagrids').datagrid('getSelected');
            if(row){
                $('#yyNewsDialog').dialog('open').dialog('setTitle','修改');
                $("input[name='id']").val(row.id);
                if(row.publish_time!=undefined) {
                    row.publish_time = getDate1(row.publish_time);
                }
                $('#title').textbox("setValue",row.title);
                setCkEditorData(row.content);
                $('#publish_time').datetimebox("setValue",row.publish_time);
                url="/News/saveNews.action";
            }else{
                yyAlertTwo();
            }
        }
        function searchNews() {
            $('#NewsDatagrids').datagrid('load',{
                'username': $('#news_username').val(),
                'title': $('#news_title').val()
            });
        }
        function NewsFormSubmit() {
            var id=$("input[name='id']").val();
            var title=$('#title').textbox("getValue");
            var content=getCkEditorData();
            var publish_time=$('#publish_time').datetimebox("getValue");
            if(title==""){
                yyAlertFour("请输入标题");
                return false;
            }else if(content==""){
                yyAlertFour("请输入内容");
                return false;
            }else if(publish_time==""){
                yyAlertFour("请输入发布时间");
                return false;
            }
            $.ajax({
                type: "post",
                url: url,
                dataType:'json',
                data:{
                   id:id,
                    title:title,
                    content:content,
                    publish_time:publish_time
                },
                success:function (data) {
                    if (data.success){
                        $('#yyNewsDialog').dialog('close');
                        $('#NewsDatagrids').datagrid('reload');
                        clearFormLabel();
                    }
                    yyAlertFour(result.message);
                },error:function () {
                    yyAlertThree();
                }
            })
        }
        function NewsCancelSubmit() {
            $('#yyNewsDialog').dialog('close');
        }
        function  clearFormLabel() {
            setCkEditorData("");
            $('#NewsForm').form('clear');
        }
        //加载富文本编辑器
        function  loadCkEditor() {
            alert(ctx+'/UploadFile/uploadPicture.action');
            try{
                ckEditor=CKEDITOR.replace('editor1', {
                    language: 'zh-cn',
                    height:350,
                    filebrowserImageUploadUrl :ctx+'/UploadFile/uploadPicture.action',
                    filebrowserUploadUrl:ctx+'/UploadFile/uploadFile.action',
                    removePlugins: 'about,maximize'
                });
            }catch(e){
                yyAlertFour("加载富文本编辑器时出错!");
            }

        }
        //获取富文本编辑器内容
        function getCkEditorData(){
            try{
                var content= ckEditor.getData();
                return content;
            }catch(e){
                yyAlertFour("获取富文本编辑器内容时出错!");
                return "";
            }
        }
        //设置富文本编辑器内容
        function setCkEditorData(content){
            try{
                ckEditor.setData(content);
            }catch(e){
                yyAlertFour("设置富文本编辑器内容时出错!");
            }

        }
    </script>
</head>
<body>
<fieldset class="b3" style="padding:10px;margin:10px;">
    <legend> <h3><b style="color: orange" >|&nbsp;</b><span class="i18n1" name="datadisplay">数据展示</span></h3></legend>
    <div  style="margin-top:5px;">
        <table class="easyui-datagrid" id="NewsDatagrids" url="/News/getNewsByLike.action" striped="true" loadMsg="正在加载中。。。" textField="text" pageSize="20" fitColumns="true" pagination="true" toolbar="#yyNewsTb">
            <thead>
            <tr>
                <th data-options="field:'ck',checkbox:true"></th>
                <th field="id" align="center" width="100" class="i18n1" name="id"></th>
                <th field="username" align="center" width="100" class="i18n1" name="username"></th>
                <th field="title" align="center" width="100" class="i18n1" name="title" data-options=""></th>
                <th field="publish_time" align="center" width="100" class="i18n1" name="publishtime" data-options="formatter:formatterdate"></th>
            </tr>
            </thead>
        </table>
    </div>
</fieldset>
<!--工具栏-->
<div id="yyNewsTb" style="padding:10px;">
    <span class="i18n1" name="username"></span>:
    <input id="news_username"  style="line-height:22px;border:1px solid #ccc">
    <span class="i18n1" name="title"></span>:
    <input id="news_title"  style="line-height:22px;border:1px solid #ccc">
    <a href="#" class="easyui-linkbutton" plain="true" data-options="iconCls:'icon-search'" onclick="searchNews()">Search</a>
    <div style="float:right">
        <a href="#" id="addNewsLinkBtn" class="easyui-linkbutton i18n1" name="add" data-options="iconCls:'icon-add',plain:true" onclick="addNews()">添加</a>
        <a href="#" id="editNewsLinkBtn" class="easyui-linkbutton i18n1" name="edit" data-options="iconCls:'icon-edit',plain:true" onclick="editNews()">修改</a>
        <a href="#" id="deltNewsLinkBtn" class="easyui-linkbutton i18n1" name="delete" data-options="iconCls:'icon-remove',plain:true" onclick="delNews()">删除</a>
    </div>
</div>
<!--添加、修改框-->
<div id="yyNewsDialog" class="easyui-dialog" data-options="title:'添加',modal:true"  closed="true" buttons="#dlg-buttons" style="display: none;padding:5px;width:950px;height:auto;">
    <form id="NewsForm" method="post">
            <input  type="hidden" name="id"  value="0"/>
            <div class="form-group">
                <label for="title" >标题</label>
                <input class="easyui-textbox" type="text" class="form-control" id="title" placeholder="标题" style="width:100%;">
            </div>
            <div class="form-group">
                <label for="editor1" >发布内容</label>
                <textarea name="editor1" id="editor1">

                </textarea>
            </div>
            <div class="form-group">
                <label for="publish_time" >发布时间</label>
                <input class="easyui-datetimebox"  type="text" id="publish_time" value="" data-options="formatter:myformatter2,parser:myparser2,editable:false" style="width:100%;"/>
            </div>
    </form>
</div>
<div id="dlg-buttons" align="center" style="width:900px;">
    <a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="NewsFormSubmit()">保存</a>
    <a href="#" class="easyui-linkbutton" id="yyCancelBtn" operationtype="add" iconCls="icon-cancel" onclick="NewsCancelSubmit()">取消</a>
</div>
<script type="text/javascript" src="../easyui/jquery.easyui.min.js"></script>
</body>
</html>
<script type="text/javascript">
    hlLanguage("../i18n/");
</script>


