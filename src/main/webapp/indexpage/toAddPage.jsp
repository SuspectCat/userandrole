<%@page import="entity.RoleInfo"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Insert message page</title>
</head>
<body>

	<%
		List<RoleInfo> roleInfomation = (List<RoleInfo>) request.getAttribute("findAllRole");
	%>
	
	<form action="AddUserInfoServlet" method="post">
		<table>
			<tr>
				<td>
					用户名：<input type="text" name="username">
					姓名:   <input type="text" name="realName">
					性别: 
					<select name="gender">
						<option value="">请选择</option>
						<option value="1">男</option>
						<option value="0">女</option>
					</select>
					职位:
					
					<%
						for (RoleInfo roleInfo : roleInfomation) {
					%>
							<input type="checkbox" name="role" value="<%= roleInfo.getId() %>"><%= roleInfo.getRole_Name() %>
					<%
						}
					%>
					
					<button type="submit">提交</button>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>