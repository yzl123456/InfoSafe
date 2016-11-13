<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<center>
		<a href="users">List ALL</a>
		<h4>This is the Home Page!</h4>
		${myError }<br>
		<form action="login" method="post">
		Username:<input type="text" name="username"><br>
		Password:<input type="password" name="password"><br>
		<input type="submit" value="login in">&nbsp;&nbsp;&nbsp;<a href="toRegisterPage">Register</a>
		</form>
	</center>
</body>
</html>