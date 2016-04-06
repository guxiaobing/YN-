<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@include file="/WEB-INF/jsp/include/easyui_core.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'menuList.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script type="text/javascript" src="./gxb/menu.js"></script>
  </head>
  
  <body>
  						
                 
    				<table id="dataGrid">
    				</table>
    	<div id="dlg" class="easyui-dialog"
			style="width:500px;height:200px;padding:10px 20px" closed="true"
			buttons="#dlg-buttons">
			<form id="fm" method="post" novalidate>
				<div class="fitem">
					<label>菜单名称:</label> <input id="text"name="name" class="easyui-textbox" required="true">
				</div>
				<div class="fitem">
					<label>资源地址:</label> <input id="url"name="url" class="easyui-textbox" required="true">
				</div>
				<div class="fitem" id="rd">
					<label>是否隐藏:</label> 
					<input type="radio" value="0" name="enable" required="true"/>是
					<input type="radio" value="1" name="enable" required="true" />否
				</div>
				
				</div>
			</form>
		</div>
		<div id="addlg" class="easyui-dialog"
			style="width:500px;height:200px;padding:10px 20px" closed="true"
			buttons="#dlg-buttons1">
			<form id="fm1" method="post" novalidate>
				<div class="fitem">
					<label>菜单名称:</label> <input id="text1"name="name" class="easyui-textbox" required="true">
				</div>
				<div class="fitem">
					<label>资源地址:</label> <input id="url1"name="url" class="easyui-textbox" required="true">
				</div>
				<div class="fitem" id="rd1">
					<label>是否隐藏:</label> 
					<input type="radio" value="0" name="enable1" required="true"/>&nbsp;是&nbsp;
					<input type="radio" value="1" name="enable1" required="true" />&nbsp;否&nbsp;
				</div>
				<div>
					<label>父菜单:</label> <select id="parentMenu" name="parent" class="easyui-combobox" required="true" 
					 style="border:1px solid #95B8E7; width:124px; height: 21px; vertical-align:middle"
					 data-options="
					 	url:'./getParentMenu.do',
									method:'post',
									valueField:'id',
									textField:'name'
					 ">
					 
					</select>
				</div>
				</form>
		</div>
		<div id="dlg-buttons">
			<a href="javascript:void(0)" class="easyui-linkbutton c6"
				iconCls="icon-ok" onclick="onSave()" style="width:90px">保存</a> 
				<a href="javascript:void(0)" class="easyui-linkbutton"
				iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')"
				style="width:90px">取消</a>
		</div>
		<div id="dlg-buttons1">
			<a href="javascript:void(0)" class="easyui-linkbutton c6"
				iconCls="icon-ok" onclick="onSaveAdd()" style="width:90px">保存</a> 
				<a href="javascript:void(0)" class="easyui-linkbutton"
				iconCls="icon-cancel" onclick="javascript:$('#addlg').dialog('close')"
				style="width:90px">取消</a>
		</div>
  </body>
</html>
