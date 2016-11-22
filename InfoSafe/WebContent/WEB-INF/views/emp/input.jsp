<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib  prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="${pageContext.request.contextPath }/scripts/jquery-1.9.1.min.js"></script>
<script type="text/javascript">
	$(function(){
		$("#name").change(function(){
			var val=$(this).val();
			val=$.trim(val);
			$(this).val(val);
			
			var oldName=$("#oldName").val();
			oldName=$.trim(oldName);
			if(oldName!=null&&oldName!=""&&val==oldName){
				alert("用户名可以使用");
				return ;
			}
			
			
			var url="${pageContext.request.contextPath }/ajaxValidateName";
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
	<%-- 
	${departments }
	<c:forEach items="${departments }" var="dept">
		${dept.departmentName }<br>
	</c:forEach>

	<c:if test="${employee.id!=null }">
		<input type="hidden" value="${employee.name }" id="oldName">
	</c:if>
		--%>
	<c:set value="${pageContext.request.contextPath }/emp" var="url"></c:set>
	<c:if test="${employee.id!=null }">
		<c:set value="${pageContext.request.contextPath }/emp/${employee.id}" var="url"></c:set>
	</c:if>
	
	<br><br>
	<form:form action="${url }" method="POST" modelAttribute="employee">
		
		<c:if test="${employee.id!=null }">
			<input type="hidden" value="${employee.name }" id="oldName">
			<form:hidden path="id" />
			<input type="hidden" name="_method" value="PUT">
		</c:if>
	
	
		Name:<form:input id="name" path="name"/><br>
		Email:<form:input path="email"/><br>
		Birth:<form:input path="birth"/>&nbsp;&nbsp;格式形如:yyyy-mm-dd<br>
		Department:
		<form:select path="department.id" items="${departments }" itemLabel="departmentName" itemValue="id"></form:select>
		<%-- 
		<form:select path="department.id" items="${departments}" 
		itemLabel="${departmentName}" itemValue="${id}"></form:select>
		--%>
		<br>
		<input type="submit" value="Submit">
	
	</form:form>
	
</body>
</html>