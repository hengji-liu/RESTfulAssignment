<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Hi There</title>
</head>
<body>

	<ul>
		<li><a href="signout">Sign out</a></li>
		<li><a href="jobseeker?method=gotoManageApplication&archived=0">Manage my applications</a>
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
						<option value="processed">Closed</option>
				</select></td>
			</tr>		
		</table>
		<input type="submit" value="Search">
	</form>

</body>
</html>