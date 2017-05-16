<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String path = request.getContextPath(); // absolute path
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<html>
<head>
<base href=" <%=basePath%>">
<title>Home</title>
</head>
<body>

	<h2>Home</h2>
	<ul>
        <li><a href="user?profile">Profile</a></li>
		<li><a
			href="jobseeker?method=gotoManageApplication&amp;archived=0">Manage
				my applications</a></li>
		<li><a href="signout">Sign out</a></li>
	</ul>

	<form action="jobseeker?method=search" method="post">
		<table>
			<tr>
				<td>Keyword</td>
				<td><input type="text" name="keyword"></td>
			</tr>
			<tr>
				<td>Status</td>
				<td><select name="status">
						<option value="1">Open</option>
						<option value="5">Closed</option>
				</select></td>
			</tr>
		</table>
		<input type="submit" value="Search">
	</form>

</body>
</html>