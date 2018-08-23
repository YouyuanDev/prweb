<%--
  Created by IntelliJ IDEA.
  User: kurt
  Date: 8/22/18
  Time: 4:32 PM
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
            $('#yyFundDialog').dialog({
                onClose:function () {
                    var type=$('#yyCancelBtn').attr('operationtype');
                    if(type!="add"){
                        clearFormLabel();
                    }
                }
            });
            $('.mini-buttonedit .mini-buttonedit-input').css('width','150px');
        });
        function editFund() {
            $('#yyCancelBtn').attr('operationtype','edit');
            var row = $('#FundDatagrids').datagrid('getSelected');
            if(row){
                $('#yyFundDialog').dialog('open').dialog('setTitle','修改');
                if(row.transfer_date!=undefined) {
                    row.transfer_date = getDate1(row.transfer_date);
                }
                $('#FundForm').form('load',row);
                url="/Fund/saveFund.action";
            }else{
                yyAlertTwo();
            }
        }
        function searchFund() {
            $('#FundDatagrids').datagrid('load',{
                'transfer_no': $('#transferno').val(),
                'company_no': $('#comanyno').val()
            });
        }
        function FundCancelSubmit() {
            $('#yyFundDialog').dialog('close');
        }
        function  clearFormLabel() {
            $('#FundForm').form('clear');
            $('#hl-gallery-con').empty();
        }

    </script>
</head>
<body>
<fieldset class="b3" style="padding:10px;margin:10px;">
    <legend> <h3><b style="color: orange" >|&nbsp;</b><span class="i18n1" name="datadisplay">数据展示</span></h3></legend>
    <div  style="margin-top:5px;">
        <table class="easyui-datagrid" id="FundDatagrids" url="/FundTransfer/getFundTransferRecordByLike.action" striped="true" loadMsg="正在加载中。。。" textField="text" pageSize="20" fitColumns="true" pagination="true" toolbar="#yyFundTb">
            <thead>
            <tr>
                <th data-options="field:'ck',checkbox:true"></th>
                <th field="id" align="center" width="100" class="i18n1" name="id"></th>
                <th field="transfer_no" align="center" width="100" class="i18n1" name="transferno"></th>
                <th field="company_no" align="center" width="100" class="i18n1" name="companyno"></th>
                <th field="fund_transfer_method" align="center" width="100" class="i18n1" name="fundtransfermethod"></th>
                <th field="transfer_amount" align="center" width="100" class="i18n1" name="transferamount"></th>
                <th field="payee_account" align="center" width="100" class="i18n1" name="payeeaccount"></th>
                <th field="payee_real_name" align="center" width="100" class="i18n1" name="payeerealname"></th>
                <th field="transfer_status" align="center" width="100" class="i18n1" name="transferstatus"></th>
                <th field="sub_msg" align="center" width="100" class="i18n1" name="submsg"></th>
                <th field="alipay_out_biz_no" align="center" width="100" class="i18n1" name="alipayoutbizno"></th>
                <th field="alipay_fund_order_id" align="center" width="100" class="i18n1" name="alipayfundorderid"></th>
                <th field="transfer_date" align="center" width="100" class="i18n1" name="transferdate" data-options="formatter:formatterdate"></th>
            </tr>
            </thead>
        </table>
    </div>
</fieldset>
<!--工具栏-->
<div id="yyFundTb" style="padding:10px;">
    <span class="i18n1" name="transferno"></span>:
    <input id="transferno"  style="line-height:22px;border:1px solid #ccc">
    <span class="i18n1" name="companyno"></span>:
    <input id="companyno"  style="line-height:22px;border:1px solid #ccc">
    <a href="#" class="easyui-linkbutton" plain="true" data-options="iconCls:'icon-search'" onclick="searchFund()">Search</a>
    <div style="float:right">
        <a href="#" id="editFundLinkBtn" class="easyui-linkbutton i18n1" name="search" data-options="iconCls:'icon-edit',plain:true" onclick="editFund()">查看</a>
    </div>
</div>
<!--添加、修改框-->
<div id="yyFundDialog" class="easyui-dialog" data-options="title:'添加',modal:true"  closed="true" buttons="#dlg-buttons" style="display: none;padding:5px;width:950px;height:auto;">
    <form id="FundForm" method="post">
        <fieldset style="width:900px;border:solid 1px #aaa;margin-top:8px;position:relative;">
            <legend>转账记录</legend>
            <table class="ht-table">
                <tr>
                    <td class="i18n1" name="id"></td>
                    <td colspan="5"><input class="easyui-textbox" type="text" name="id" readonly="true" value="0"/></td>
                </tr>
                <tr>
                    <td width="16%"  class="i18n1" name="transferno"></td>
                    <td >
                        <input class="easyui-textbox" type="text" value="" name="transfer_no" readonly="true" />
                    </td>
                    <td ></td>
                    <td width="16%"  class="i18n1" name="companyno"></td>
                    <td>
                        <input class="easyui-textbox" type="text" value="" name="company_no" readonly="true" />
                    </td>
                    <td></td>
                </tr>
                <tr>
                    <td width="16%"  class="i18n1" name="fundtransfermethod"></td>
                    <td><input class="easyui-textbox" type="text" name="fund_transfer_method" value=""/></td>
                    <td></td>
                    <td width="16%"  class="i18n1" name="transferamount"></td>
                    <td><input class="easyui-textbox" type="text" name="transfer_amount" value=""/></td>
                    <td></td>
                </tr>
                <tr>
                    <td width="16%"  class="i18n1" name="payeeaccount"></td>
                    <td><input class="easyui-textbox"  type="text" name="payee_account" value=""/></td>
                    <td></td>
                    <td width="16%"  class="i18n1" name="payeerealname"></td>
                    <td><input class="easyui-textbox" type="text" name="payee_real_name" value=""/></td>
                    <td></td>
                </tr>
                <tr>
                    <td width="16%"  class="i18n1" name="transferstatus"></td>
                    <td><input class="easyui-textbox"  type="text" name="transfer_status" value=""/></td>
                    <td></td>
                    <td width="16%"  class="i18n1" name="alipayoutbizno"></td>
                    <td><input class="easyui-textbox" type="text" name="alipay_out_biz_no" value=""/></td>
                    <td></td>
                </tr>
                <tr>
                    <td width="16%"  class="i18n1" name="alipayfundorderid"></td>
                    <td><input class="easyui-textbox" type="text" name="alipay_fund_order_id" value=""/></td>
                    <td></td>
                    <td width="16%" class="i18n1" name="transferdate"></td>
                    <td><input class="easyui-datetimebox" type="text" id="transfer_date" name="transfer_date" value="" data-options="formatter:myformatter2,parser:myparser2"/>
                    </td>
                    <td></td>
                </tr>
                <tr>
                    <td class="i18n1" name="submsg" width="20%"></td>
                    <td colspan="3"><input class="easyui-textbox" type="text" value="" name="sub_msg" data-options="multiline:true" style="height:60px;width:100%;"/></td>
                </tr>
            </table>
        </fieldset>
    </form>
</div>
<div id="dlg-buttons" align="center" style="width:900px;">
    <%--<a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="FundFormSubmit()">Save</a>--%>
    <a href="#" class="easyui-linkbutton" id="yyCancelBtn" operationtype="add" iconCls="icon-cancel" onclick="FundCancelSubmit()">Cancel</a>
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


