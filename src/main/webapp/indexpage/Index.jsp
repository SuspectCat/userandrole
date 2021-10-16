<%@page import="entity.UserInfo"%>
<%@page import="vo.UserInfoVo"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Insert title here</title>
</head>
<!-- 引入jquery文件 -->
<script src="https://cdn.staticfile.org/jquery/1.10.2/jquery.min.js"></script>
<body>
	<div>
		<%
			UserInfo users = (UserInfo)request.getAttribute("user");
		%>
	
		<form action="UserInfoServlet" method="post">
			用户名：<input type="text" name="username" value="<%= users.getUsername()%>"> 姓名: <input type="text" name="realName" value="<%= users.getRealName() %>"> 
			性别：<select name="gender">
				<option value="">请选择</option>
				<option value="1">男</option>
				<option value="0">女</option>
			</select>
			<button type="submit">搜索</button>
			<button type="button" onclick="location.href='ToAddPage'">添加</button>
		</form>
	</div>

	<table>
		<tr>
			<td>用户名</td>
			<td>姓名</td>
			<td>性别</td>
			<td>角色</td>
		</tr>

		<%
			if (null != request.getAttribute("userInfo")) {
				List<UserInfoVo> userList = (List<UserInfoVo>) request.getAttribute("userInfo");
				
				for (UserInfoVo user : userList) {
		%>
		<tr>
			<td><%= user.getUsername() %></td>
			<td><%= user.getRealName() %></td>
			<td><%= user.getGender() %></td>
			<td><%= user.getRoleNames() %></td>
		</tr>
		<%
				}
			}
		%>

	</table>

</body>
<script type="text/javascript">
	var gender = <%= users.getGender() %>
	$("select[name='gender'] option[value=" + gender + "]").attr("selected", true);
</script>
</html>