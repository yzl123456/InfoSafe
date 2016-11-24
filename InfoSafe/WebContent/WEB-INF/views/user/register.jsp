<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="${pageContext.request.contextPath }/scripts/jquery-1.9.1.min.js"></script>
<script type="text/javascript">
	$(function(){
		$("#submit").click(function(){
			var nameVal=$("#name").val();
			var passVal=$("#password").val();
			if(nameVal!=null&&nameVal!=""&&passVal!=""&&passVal!=null)
				alert("注册成功!")
		});
	
		
		$("#name").change(function(){
			var val=$(this).val();
			val=$.trim(val);
			$(this).val(val);
			
			
			var url="${pageContext.request.contextPath }/ajaxUserValidateName";
			var args={"name":val,"data":new Date()};
			$.post(url,args,function(data){
				if(data=="0")
					alert("用户名可以使用");
				else alert("用户名不能使用")
			});
			
		});
		
	})
		
</script>
</head>
<body>
	<center>
		<h4>This is the register Page!</h4>
		<form action="user" method="post">
			${myError }<br>
			Username:<input id="name" type="text" name="username"><br>
			Password:<input id="password" type="password" name="password"><br>
			<input id="submit" type="submit" value="register">
		</form>
	</center>
</body>
</html>