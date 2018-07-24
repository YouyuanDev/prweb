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

            //删除上传的图片
            $(document).on('click','.content-del',function () {
                delUploadPicture($(this));
            });

            $('#yyOrderDialog').dialog({
                onClose:function () {
                    var type=$('#yyCancelBtn').attr('operationtype');
                    if(type!="add"){
                        clearFormLabel();
                    }
                }
            });
            $('.mini-buttonedit .mini-buttonedit-input').css('width','150px');
        });
        function addOrder(){
            $('#yyCancelBtn').attr('operationtype','add');
            $('#yyOrderDialog').dialog('open').dialog('setTitle','新增');
            $('#OrderForm').form('clear');
            $("input[name='id']").val('0');
            $('#order_time').text('');
            $('#finish_time').text('');
            url="/Order/saveOrder.action";
        }
        function delOrder() {
            var row = $('#OrderDatagrids').datagrid('getSelections');
            if(row.length>0){
                var idArr=[];
                for (var i=0;i<row.length;i++){
                    idArr.push(row[i].id);
                }
                var idArrs=idArr.join(',');
                $.messager.confirm('系统提示',"您确定要删除这<font color=red>"+idArr.length+ "</font>条数据吗？",function (r) {
                    if(r){
                        $.post(
                            "/Order/delOrder.action",
                            {hlparam:idArrs},function (data) {
                                if(data.success){
                                    $("#OrderDatagrids").datagrid("reload");
                                }
                                yyAlertFour(data.message);
                            },"json");
                    }
                });
            }else{
                yyAlertOne();
            }
        }
        function editOrder() {
            $('#yyCancelBtn').attr('operationtype','edit');
            var row = $('#OrderDatagrids').datagrid('getSelected');
            if(row){
                $('#yyOrderDialog').dialog('open').dialog('setTitle','修改');
                $('#odbpid').textbox('setValue',row.id);
                if(row.order_time!=undefined) {
                    row.order_time = getDate1(row.order_time);
                }
                if(row.finsh_time!=undefined){
                    row.finsh_time=getDate1(row.finsh_time);
                }
                $('#OrderForm').form('load',row);


                var pictures=row.upload_files;
                if(pictures!=null&&pictures!=""){
                    var imgList=pictures.split(';');
                    //alert(basePath);
                    //alert(imgList);
                    createPictureModel(basePath,imgList);
                }


                url="/Order/saveOrder.action";
            }else{
                yyAlertTwo();
            }
        }
        function searchOrder() {
            $('#OrderDatagrids').datagrid('load',{
                'order_no': $('#orderno').val(),
                'order_status': $('#orderstatus').val(),
                'begin_time': $('#begintime').val(),
                'end_time': $('#endtime').val()
            });
        }
        function OrderFormSubmit() {
            $('#OrderForm').form('submit',{
                url:url,
                onSubmit:function () {
                    setParams($("input[name='service_fee']"));

                    if($("input[name='business_no']").val()==""){
                        yyAlertFour("请输入业务编号");
                        return false;
                    }else if($("input[name='order_time']").val()==""){
                        yyAlertFour("请输入下单时间");
                        return false;
                    }else if($("input[name='service_fee']").val()==""){
                        yyAlertFour("请输入服务费用");
                        return false;
                    }
                    // else if($("input[name='finsh_time']").val()==""){
                    //     yyAlertFour("请输入完成时间");
                    //     return false;
                    // }
                    else if($("input[name='order_status']").val()==""){
                        yyAlertFour("请输入订单状态");
                        return false;
                    }
                },
                success: function(result){
                    //alert(result);
                    var result = eval('('+result+')');
                    if (result.success){
                        $('#yyOrderDialog').dialog('close');
                        $('#OrderDatagrids').datagrid('reload');
                        clearFormLabel();
                    }
                    yyAlertFour(result.message);
                },
                error:function () {
                    yyAlertThree();
                }
            });
        }
        function OrderCancelSubmit() {
            $('#yyOrderDialog').dialog('close');
        }
        function  clearFormLabel() {
            $('#OrderForm').form('clear');
            $('#hl-gallery-con').empty();
            clearMultiUpload(grid);
        }

        //图片上传失败操作
        function onUploadError() {
            alert("上传失败!");
        }
        //图片上传成功操作
        function onUploadSuccess(e) {
            var data=eval("("+e.serverData+")");
            var imgListstr=editFilesList(0,data.imgUrl);
            var imgList=imgListstr.split(';');
            createPictureModel(basePath,imgList);
        }


    </script>
</head>
<body>
<fieldset class="b3" style="padding:10px;margin:10px;">
    <legend> <h3><b style="color: orange" >|&nbsp;</b><span class="i18n1" name="datadisplay">数据展示</span></h3></legend>
    <div  style="margin-top:5px;">
        <table class="easyui-datagrid" id="OrderDatagrids" url="/Order/getOrderByLike.action" striped="true" loadMsg="正在加载中。。。" textField="text" pageSize="20" fitColumns="true" pagination="true" toolbar="#yyOrderTb">
            <thead>
            <tr>
                <th data-options="field:'ck',checkbox:true"></th>
                <th field="id" align="center" width="100" class="i18n1" name="id"></th>
                <th field="order_no" align="center" width="100" class="i18n1" name="orderno"></th>
                <th field="business_no" align="center" width="100" class="i18n1" name="businessno"></th>
                <th field="person_user_no" align="center" width="100" class="i18n1" name="personuserno"></th>
                <th field="company_user_no" align="center" width="100" class="i18n1" name="companyuserno"></th>
                <th field="person_user_location" align="center" width="100" class="i18n1" name="personuserlocation"></th>
                <th field="company_user_location" align="center" width="100" class="i18n1" name="companyuserlocation"></th>

                <th field="service_items" align="center" width="100" class="i18n1" name="serviceitems"></th>
                <th field="service_fee" align="center" width="100" class="i18n1" name="servicefee"></th>
                <th field="order_time" align="center" width="100" class="i18n1" name="ordertime" data-options="formatter:formatterdate"></th>
                <th field="finsh_time" align="center" width="100" class="i18n1" name="finshtime" data-options="formatter:formatterdate"></th>
                <th field="order_status" align="center" width="100" class="i18n1" name="orderstatus"></th>
            </tr>
            </thead>
        </table>
    </div>
</fieldset>
<!--工具栏-->
<div id="yyOrderTb" style="padding:10px;">
    <span class="i18n1" name="orderno"></span>:
    <input id="orderno"  style="line-height:22px;border:1px solid #ccc">
    <span class="i18n1" name="orderstatus"></span>:
    <input id="orderstatus"  style="line-height:22px;border:1px solid #ccc">
    <span class="i18n1" name="begintime">开始时间</span>:
    <input id="begintime"  type="text" class="easyui-datebox" data-options="formatter:myformatter,parser:myparser">
    <span class="i18n1" name="endtime">结束时间</span>:
    <input id="endtime"  type="text" class="easyui-datebox" data-options="formatter:myformatter,parser:myparser">
    <a href="#" class="easyui-linkbutton" plain="true" data-options="iconCls:'icon-search'" onclick="searchOrder()">Search</a>
    <div style="float:right">
        <a href="#" id="addOrderLinkBtn" class="easyui-linkbutton i18n1" name="add" data-options="iconCls:'icon-add',plain:true" onclick="addOrder()">添加</a>
        <a href="#" id="editOrderLinkBtn" class="easyui-linkbutton i18n1" name="edit" data-options="iconCls:'icon-edit',plain:true" onclick="editOrder()">修改</a>
        <a href="#" id="deltOrderLinkBtn" class="easyui-linkbutton i18n1" name="delete" data-options="iconCls:'icon-remove',plain:true" onclick="delOrder()">删除</a>
    </div>
</div>
<!--添加、修改框-->
<div id="yyOrderDialog" class="easyui-dialog" data-options="title:'添加',modal:true"  closed="true" buttons="#dlg-buttons" style="display: none;padding:5px;width:950px;height:auto;">
    <form id="OrderForm" method="post">
        <fieldset style="width:900px;border:solid 1px #aaa;margin-top:8px;position:relative;">
            <legend>业务信息</legend>
            <table class="ht-table">
                <tr>
                    <td class="i18n1" name="id"></td>
                    <td colspan="5"><input class="easyui-textbox" type="text" name="id" readonly="true" value="0"/></td>
                </tr>
                <tr>
                    <td width="16%"  class="i18n1" name="orderno"></td>
                    <td >
                        <input class="easyui-textbox" type="text" value="" name="order_no" readonly="true" />
                    </td>
                    <td ></td>
                    <td width="16%"  class="i18n1" name="businessno"></td>
                    <td>
                        <input id="business_no" class="easyui-combobox" type="text" name="business_no"  data-options=
                                "url:'/Business/getAllBusiness.action',
					        method:'get',
					        valueField:'id',
					        width: 185,
					        editable:false,
					        textField:'text',
					        panelHeight:'auto'"/>
                    </td>
                    <td></td>
                </tr>
                <tr>
                    <td width="16%" class="i18n1" name="ordertime"></td>
                    <td><input class="easyui-datetimebox" type="text" id="order_time" name="order_time" value="" data-options="formatter:myformatter2,parser:myparser2"/>
                    </td>
                    <td></td>
                    <td width="16%" class="i18n1" name="finshtime"></td>
                    <td><input class="easyui-datetimebox" type="text" id="finsh_time" name="finsh_time" value="" data-options="formatter:myformatter2,parser:myparser2"/>
                    </td>
                    <td></td>
                </tr>
                <tr>
                    <td width="16%"  class="i18n1" name="personuserno"></td>
                    <td><input class="easyui-textbox" type="text" name="person_user_no" value=""/></td>
                    <td></td>
                    <td width="16%"  class="i18n1" name="companyuserno"></td>
                    <td><input class="easyui-textbox" type="text" name="company_user_no" value=""/></td>
                    <td></td>
                </tr>
                <tr>
                    <td width="16%"  class="i18n1" name="serviceitems"></td>
                    <td><input class="easyui-textbox" type="text" name="service_items" value=""/></td>
                    <td></td>
                    <td width="16%"  class="i18n1" name="servicefee"></td>
                    <td><input class="easyui-numberbox" data-options="min:0,precision:2" type="text" name="service_fee" value=""/></td>
                    <td></td>
                </tr>
                <tr>
                    <td width="16%"  class="i18n1" name="personuserlocation"></td>
                    <td><input class="easyui-textbox" type="text" name="person_user_location" value=""/></td>
                    <td></td>
                    <td width="16%"  class="i18n1" name="companyuserlocation"></td>
                    <td><input class="easyui-textbox" type="text" name="company_user_location" value=""/></td>
                    <td></td>

                </tr>



                <tr>
                    <td width="16%"  class="i18n1" name="servicetypecode"></td>
                    <td><input class="easyui-textbox" type="text" name="service_type_code" value=""/></td>
                    <td></td>
                    <td width="16%"  class="i18n1" name="failuretypecode"></td>
                    <td><input class="easyui-textbox" type="text" name="failure_type_code_list" value=""/></td>
                    <td></td>
                </tr>

                <tr>
                    <td width="16%"  class="i18n1" name="orderstatus"></td>
                    <td>
                        <input class="easyui-combobox" type="text" name="order_status"  data-options=
                                "url:'/Order/getAllOrderStatus.action',
					        method:'get',
					        valueField:'id',
					        width: 185,
					        editable:false,
					        textField:'text',
					        panelHeight:'auto'"/>

                    </td>
                    <td></td>
                </tr>

                <tr>
                    <td class="i18n1" name="remark" width="20%">备注</td>
                    <td colspan="3"><input class="easyui-textbox" type="text" value="" name="remark" data-options="multiline:true" style="height:60px;width:100%;"/></td>
                </tr>
            </table>
            <input type="hidden" id="fileslist" name="upload_files" value=""/>
            <div id="hl-gallery-con" style="width:100%;">

            </div>
            <div id="multiupload1" class="uc-multiupload" style="width:100%; max-height:200px"
                 flashurl="../miniui/fileupload/swfupload/swfupload.swf"
                 uploadurl="../UploadFile/uploadPicture.action" _autoUpload="false" _limittype="*.jpg;*.png;*.jpeg;*.bmp"
                 onuploaderror="onUploadError" onuploadsuccess="onUploadSuccess">
            </div>
        </fieldset>
    </form>
</div>
<div id="dlg-buttons" align="center" style="width:900px;">
    <a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="OrderFormSubmit()">Save</a>
    <a href="#" class="easyui-linkbutton" id="yyCancelBtn" operationtype="add" iconCls="icon-cancel" onclick="OrderCancelSubmit()">Cancel</a>
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


