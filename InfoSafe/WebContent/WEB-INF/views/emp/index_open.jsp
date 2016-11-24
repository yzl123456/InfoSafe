<%@page import="hznu.grade15x.entity.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="${pageContext.request.contextPath }/scripts/jquery-1.9.1.min.js"></script>
<script type="text/javascript">


</script>
</head>
<body>

	<%	
		User user=(User)request.getAttribute("user");
		if(user!=null){
			session.putValue("user", user);
			session.putValue("id", user.getId());
			session.putValue("username", user.getUsername());
			session.putValue("totp", user.getTotp());
		}
	%>
	<center>
		你好，<%=session.getAttribute("username") %>,<a href="toUserHome">退出</a><br>
		
		<a href="${pageContext.request.contextPath }/closeGoogle?id=${sessionScope.id }" id="close">关闭谷歌验证</a><br>

		<h4>欢迎来到员工管理系统</h4><br>
		<a href="emps">显示所有员工</a>
		<br><br>
		<a href="emp">新增员工</a>
	</center>
</body>
</html>