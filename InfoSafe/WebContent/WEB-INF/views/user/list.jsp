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
	<h4>this is user page!</h4>
	<c:if test="${page.numberOfElements==0||page==null }">
		没有任何记录
	</c:if>
	<c:if test="${page!=null }">
		<table border="1" cellpadding="10" cellspacing="0">
			<tr>
				<th>ID</th>
				<th>Username</th>
				<th>Password</th>
				<th>SecretKey</th>
				<th>Seed</th>
			</tr>
			<c:forEach items="${page.content }" var="user">
				<tr>
					<td>${user.id }</td>
					<td>${user.username }</td>
					<td>${user.password }</td>
					<td>${user.secretKey }</td>
					<td>${user.seed }</td>
					
				</tr>
			</c:forEach>
		</table>
	</c:if>
</body>
</html>