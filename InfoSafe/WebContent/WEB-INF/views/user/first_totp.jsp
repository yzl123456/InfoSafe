<%@page import="hznu.grade15x.entity.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<center>
		<%
		User user=(User)request.getAttribute("user");
		session.putValue("id",user.getId());
		%>
		<img src="${pageContext.request.contextPath}/QRCode/<%=session.getAttribute("id") %>.jpg"><br>
		请在您的谷歌APP上输入以下字符串作为密钥:${secretKey}<br>
		<form action="firstToTotp">
			<input type="hidden" name="id" value=${sessionScope.id }>
			<input type="submit" value="确定">
		</form>
	</center>
</body>
</html>