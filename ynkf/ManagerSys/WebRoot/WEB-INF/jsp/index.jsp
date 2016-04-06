<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@include file="/WEB-INF/jsp/include/easyui_core.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
  <head>
  <STYLE>
.main_header{width:100%;height:64px;margin:0px;background:url("${path}/img/login_bg.jpg") repeat-x;overflow: hidden;}
.header_left{float:left;width:262px;height:100%;background:url("${path}/img/logo2.png");}
</STYLE>
    <base href="<%=basePath%>">
    <title>云南省水资源后台管理系统</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
  </head>
  <script type="text/javascript" src="./gxb/first.js"></script>
  
   <body class="easyui-layout">  
        <!-- 正上方panel -->  
        <div data-options="region:'north',border:false" style="height:60px;background:#46B43B;padding:0px">
        <!-- <div class="main_header" region="north" style="height:70px;padding:5px;" > -->
			<div class="header_left"></div>
			
	        <div id="sessionInfoDiv" style="position: absolute;right: 5px;top:10px;">
	            <strong><%=user.getUsername() %></SPAN></strong>&nbsp;欢迎你！
	            <SPAN id=clock style="color: blue;"></SPAN>
				<SCRIPT type=text/javascript> 
				    var clock = new Clock();
				    clock.display(document.getElementById("clock"));
				</SCRIPT>
	        </div>
	        <div style="position: absolute; right: 0px; bottom: 0px; ">
	            <a href="javascript:void(0);" class="easyui-menubutton" data-options="menu:'#layout_north_pfMenu',iconCls:'icon-ok'">更换皮肤</a> <a href="javascript:void(0);" class="easyui-menubutton" data-options="menu:'#layout_north_kzmbMenu',iconCls:'icon-help'">控制面板</a> <a href="javascript:void(0);" class="easyui-menubutton" data-options="menu:'#layout_north_zxMenu',iconCls:'icon-back'">注销</a>
	        </div>
				
	        <div id="layout_north_pfMenu" style="width: 120px; display: none;">
	            <div onclick="changeTheme('default');">默认</div>
	        </div>
	        <div id="layout_north_kzmbMenu" style="width: 100px; display: none;">
	            <div onclick="userInfoFun();">个人信息</div>
	        </div>
	        <div id="layout_north_zxMenu" style="width: 100px; display: none;">
	            <div onclick="logout();">重新登录</div>
	            <div class="menu-sep"></div>
	            <div onclick="logoutClose(true);">退出系统</div>
	        </div>
	        
        </div>  
	    	
		<!-- 左侧菜单 -->
        <div data-options="region:'west',href:''" title="导航菜单" style="width: 200px; padding: 0px;">
			<ul id="mainMenu"></ul>
			
		</div>
		
        <!-- 正中间panel -->  
        <!-- <div region="center" title="功能区" >   -->
        <div region="center">  
            <div class="easyui-tabs" id="mainTabs" fit="true" border="false">  
                <div title="首页" style="padding:20px;">  
                        <table id="dataGrid">
                        </table>
                </div>  
            </div>  
        </div>  
        
        <!-- 正下方panel -->  
        <div region="south" style="height:24px;line-height:22px;" align="center">  
            <label style="font-size:11px;">技术支持&nbsp;&nbsp;&nbsp;&nbsp;东华软件股份公司&nbsp;</label>  
        </div>   
    </body>
</html>
