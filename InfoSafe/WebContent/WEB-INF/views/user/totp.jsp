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

	<%	
		User user=(User)request.getAttribute("user");
		if(user!=null){
			session.putValue("id", user.getId());
			session.putValue("username", user.getUsername());
		}
	%>
	<center>
		<form action="validateCode">
			<img src="${pageContext.request.contextPath}/QRCode/<%=session.getAttribute("id")%>.jpg"><br>
			${myError }<br>
			<input type="hidden" name="id" value="${sessionScope.id }">
			请输入您手机谷歌app上的动态密码<input type="text" name="code"><br>
			<input type="submit" value="验证">
		</form>
	
	</center>
</body>
</html>