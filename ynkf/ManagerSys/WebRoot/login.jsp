<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'index.jsp' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<title>云南省水资源管理系统</title>

	<link rel="stylesheet" type="text/css" href="./css/login.css">

	<script src="./js/easyui/jquery.min.js" type="text/javascript"></script> 
  </head>
  <script type="text/javascript">
  		function login(){
  			$('#loginForm').submit();
  		}
  </script>
 
  	<div style="width:100%;height:90%;position: absolute;top:50%;left:50%;margin-top:-320px;margin-left:-50%;">
  		<div class="header">欢迎使用</div>
  		<div class="center">
  			<div class="login_right">
  				<div style="width:100%;height:auto;margin-top: 150px">
  					<form action="./login.do" method="post" name="loginForm" id="loginForm" class="loginForm">
  						<div class="login_info">
							<label>用户名：</label>
							<input type="text" name="loginname" id="loginname" class="login_input" value="${loginname }"/>
							&nbsp;<span id="nameerr" class="errInfo"></span>
						</div>
						<div class="login_info">
							<label>密　码：</label>
							<input type="password" name="password" id="password" class="login_input" value="${password }"/>
							&nbsp;<span id="pwderr" class="errInfo"></span>
						</div>
						<div class="login_info">
							<input type="button" name="loginBtn" id="loginBtn" value="登录" class="btn" onclick="login()"/>
							<label>　　　</label>
							<input type="reset" name="cancelBtn" id="cancelBtn" value="取消" class="btn"/>
						</div>
  					</form>
  				</div>
  			</div>
  			<div class="login_left">
				<div style="width:100%;height:auto;margin-top:150px;">
					<div class="logo"></div>
					<div class="left_txt">
				
					</div>
				</div>
			</div>
  		</div>
  		<div class="bottom">
			技术支持 &copy;  东华软件股份公司
		</div>
  	</div>
  </body>
</html>
