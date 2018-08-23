<%--
  Created by IntelliJ IDEA.
  User: kurt
  Date: 8/22/18
  Time: 4:33 PM
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
        var basePath ="<%=basePath%>"+"/upload/pictures/";
        $(function () {
            $('#yyCommentDialog').dialog({
                onClose:function () {
                    var type=$('#yyCancelBtn').attr('operationtype');
                    if(type!="add"){
                        clearFormLabel();
                    }
                }
            });
            $('.mini-buttonedit .mini-buttonedit-input').css('width','150px');
        });
        // function addComment(){
        //     $('#yyCancelBtn').attr('operationtype','add');
        //     $('#yyCommentDialog').dialog('open').dialog('setTitle','新增');
        //     $('#CommentForm').form('clear');
        //     $("input[name='id']").val('0');
        //     $('#order_time').text('');
        //     $('#finish_time').text('');
        //     url="/Comment/saveComment.action";
        // }
        function delComment() {
            var row = $('#CommentDatagrids').datagrid('getSelections');
            if(row.length>0){
                var idArr=[];
                for (var i=0;i<row.length;i++){
                    idArr.push(row[i].id);
                }
                var idArrs=idArr.join(',');
                $.messager.confirm('系统提示',"您确定要删除这<font color=red>"+idArr.length+ "</font>条数据吗？",function (r) {
                    if(r){
                        $.post(
                            "/Comment/delComment.action",
                            {hlparam:idArrs},function (data) {
                                if(data.success){
                                    $("#CommentDatagrids").datagrid("reload");
                                }
                                yyAlertFour(data.message);
                            },"json");
                    }
                });
            }else{
                yyAlertOne();
            }
        }
        function editComment() {
            $('#yyCancelBtn').attr('operationtype','edit');
            var row = $('#CommentDatagrids').datagrid('getSelected');
            if(row){
                $('#yyCommentDialog').dialog('open').dialog('setTitle','修改');
                if(row.comment_time!=undefined) {
                    row.comment_time = getDate1(row.comment_time);
                }
                $('#CommentForm').form('load',row);
                url="/Comment/saveComment.action";
            }else{
                yyAlertTwo();
            }
        }
        function searchComment() {
            $('#CommentDatagrids').datagrid('load',{
                'comment_from_person_user_no': $('#commentfrompersonuserno').val(),
                'comment_to_comany_no': $('#commenttocomanyno').val()
            });
        }
        function CommentFormSubmit() {
            // $('#OrderForm').form('submit',{
            //     url:url,
            //     onSubmit:function () {
            //         setParams($("input[name='service_fee']"));
            //
            //         if($("input[name='business_no']").val()==""){
            //             yyAlertFour("请输入业务编号");
            //             return false;
            //         }else if($("input[name='order_time']").val()==""){
            //             yyAlertFour("请输入下单时间");
            //             return false;
            //         }else if($("input[name='service_fee']").val()==""){
            //             yyAlertFour("请输入服务费用");
            //             return false;
            //         }
            //         // else if($("input[name='finsh_time']").val()==""){
            //         //     yyAlertFour("请输入完成时间");
            //         //     return false;
            //         // }
            //         else if($("input[name='order_status']").val()==""){
            //             yyAlertFour("请输入订单状态");
            //             return false;
            //         }
            //     },
            //     success: function(result){
            //         //alert(result);
            //         var result = eval('('+result+')');
            //         if (result.success){
            //             $('#yyOrderDialog').dialog('close');
            //             $('#OrderDatagrids').datagrid('reload');
            //             clearFormLabel();
            //         }
            //         yyAlertFour(result.message);
            //     },
            //     error:function () {
            //         yyAlertThree();
            //     }
            // });
        }
        function CommentCancelSubmit() {
            $('#yyCommentDialog').dialog('close');
        }
        function  clearFormLabel() {
            $('#CommentForm').form('clear');
            $('#hl-gallery-con').empty();
        }

    </script>
</head>
<body>
<fieldset class="b3" style="padding:10px;margin:10px;">
    <legend> <h3><b style="color: orange" >|&nbsp;</b><span class="i18n1" name="datadisplay">数据展示</span></h3></legend>
    <div  style="margin-top:5px;">
        <table class="easyui-datagrid" id="CommentDatagrids" url="/Comment/getCommentByLike.action" striped="true" loadMsg="正在加载中。。。" textField="text" pageSize="20" fitColumns="true" pagination="true" toolbar="#yyCommentTb">
            <thead>
            <tr>
                <th data-options="field:'ck',checkbox:true"></th>
                <th field="id" align="center" width="100" class="i18n1" name="id"></th>
                <th field="comment_no" align="center" width="100" class="i18n1" name="commentno"></th>
                <th field="rating" align="center" width="100" class="i18n1" name="rating"></th>
                <th field="options" align="center" width="100" class="i18n1" name="options"></th>
                <th field="remark" align="center" width="100" class="i18n1" name="remark"></th>
                <th field="comment_from_person_user_no" align="center" width="100" class="i18n1" name="commentfrompersonuserno"></th>
                <th field="anonymous" align="center" width="100" class="i18n1" name="anonymous"></th>
                <th field="comment_to_comany_no" align="center" width="100" class="i18n1" name="commenttocomanyno"></th>
                <th field="comment_time" align="center" width="100" class="i18n1" name="commenttime" data-options="formatter:formatterdate"></th>
            </tr>
            </thead>
        </table>
    </div>
</fieldset>
<!--工具栏-->
<div id="yyCommentTb" style="padding:10px;">
    <span class="i18n1" name="commentfrompersonuserno"></span>:
    <input id="commentfrompersonuserno"  style="line-height:22px;border:1px solid #ccc">
    <span class="i18n1" name="commenttocomanyno"></span>:
    <input id="commenttocomanyno"  style="line-height:22px;border:1px solid #ccc">
    <a href="#" class="easyui-linkbutton" plain="true" data-options="iconCls:'icon-search'" onclick="searchComment()">Search</a>
    <div style="float:right">
        <%--<a href="#" id="addOrderLinkBtn" class="easyui-linkbutton i18n1" name="add" data-options="iconCls:'icon-add',plain:true" onclick="addOrder()">添加</a>--%>
        <a href="#" id="editCommentLinkBtn" class="easyui-linkbutton i18n1" name="search" data-options="iconCls:'icon-edit',plain:true" onclick="editComment()">查看</a>
        <a href="#" id="deltCommentLinkBtn" class="easyui-linkbutton i18n1" name="delete" data-options="iconCls:'icon-remove',plain:true" onclick="delComment()">删除</a>
    </div>
</div>
<!--添加、修改框-->
<div id="yyCommentDialog" class="easyui-dialog" data-options="title:'添加',modal:true"  closed="true" buttons="#dlg-buttons" style="display: none;padding:5px;width:950px;height:auto;">
    <form id="CommentForm" method="post">
        <fieldset style="width:900px;border:solid 1px #aaa;margin-top:8px;position:relative;">
            <legend>评论信息</legend>
            <table class="ht-table">
                <tr>
                    <td class="i18n1" name="id"></td>
                    <td colspan="5"><input class="easyui-textbox" type="text" name="id" readonly="true" value="0"/></td>
                </tr>
                <tr>
                    <td width="16%"  class="i18n1" name="commentno"></td>
                    <td >
                        <input class="easyui-textbox" type="text" value="" name="comment_no" readonly="true" />
                    </td>
                    <td ></td>
                    <td width="16%"  class="i18n1" name="rating"></td>
                    <td>
                        <input class="easyui-textbox" type="text" value="" name="rating" readonly="true" />
                    </td>
                    <td></td>
                </tr>

                <tr>
                    <td width="16%"  class="i18n1" name="options"></td>
                    <td><input class="easyui-textbox" type="text" name="options" value=""/></td>
                    <td></td>
                    <td width="16%"  class="i18n1" name="commentfrompersonuserno"></td>
                    <td><input class="easyui-textbox" type="text" name="comment_from_person_user_no" value=""/></td>
                    <td></td>
                </tr>
                <tr>
                    <td width="16%"  class="i18n1" name="anonymous"></td>
                    <td><input class="easyui-textbox"  type="text" name="anonymous" value=""/></td>
                    <td></td>
                    <td width="16%"  class="i18n1" name="commenttocomanyno"></td>
                    <td><input class="easyui-textbox" type="text" name="comment_to_comany_no" value=""/></td>
                    <td></td>
                </tr>
                <tr>
                    <td width="16%" class="i18n1" name="commenttime"></td>
                    <td><input class="easyui-datetimebox" type="text" id="order_time" name="comment_time" value="" data-options="formatter:myformatter2,parser:myparser2"/>
                    </td>
                    <td></td>
                </tr>
                <tr>
                    <td class="i18n1" name="remark" width="20%">备注</td>
                    <td colspan="3"><input class="easyui-textbox" type="text" value="" name="remark" data-options="multiline:true" style="height:60px;width:100%;"/></td>
                </tr>
            </table>
        </fieldset>
    </form>
</div>
<div id="dlg-buttons" align="center" style="width:900px;">
    <%--<a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="CommentFormSubmit()">Save</a>--%>
    <a href="#" class="easyui-linkbutton" id="yyCancelBtn" operationtype="add" iconCls="icon-cancel" onclick="CommentCancelSubmit()">Cancel</a>
</div>
<script type="text/javascript" src="../easyui/jquery.easyui.min.js"></script>
</body>
</html>
<script type="text/javascript">
    mini.parse();
    var grid= mini.get("multiupload1");
    var keyText = mini.get("keyText");
    function onSearchClick(e) {
        grid.load({
            role_no: keyText.value,
            role_name:keyText.value
        });
    }
    $(function () {
        $('.hl-mini-input .mini-buttonedit-border .mini-buttonedit-input').css("width","360px");
    });
    hlLanguage("../i18n/");
</script>


