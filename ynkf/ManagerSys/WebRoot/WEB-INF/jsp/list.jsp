<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>用户管理</title>
<%@include file="/WEB-INF/jsp/include/easyui_core.jsp"%>
</head>
<script type="text/javascript" src="./gxb/wiu.js"></script>
<body>
						<div id="tool">
                        	<a href="javasript:void(0)" class="easyui-linkbutton"
                        		iconCls="icon-reload" plain="true" >刷新</a>
                        		<a href="javasript:void(0)" class="easyui-linkbutton"
                        		iconCls="icon-add" plain="true" >新增</a>
                        		<a href="javasript:void(0)" class="easyui-linkbutton"
                        		iconCls="icon-edit" plain="true" >编辑</a>
                        		<span>取用水户：</span><input type="text" class="easyui-textbox"name="search_name" id="search_name" size="20px" />
                        		<a href="javascript:FindData()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a> 
                        </div> 
                        
                        <table id="dataGrid">
						</table>
</body>
</html>
