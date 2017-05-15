<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Register form</title>
</head>
<body>

	<h2>Sign up</h2>
	<form method="post" action="signup">
		<table>
			<tr>
				<td>Registered as</td>
				<td>:</td>
				<td><select name="userType">
						<option value="manager">Manager</option>
						<option value="hiringteam">Hiring team</option>
						<option value="jobseeker">Job seeker</option>
				</select></td>
			</tr>
			<tr>
				<td>Email</td>
				<td>:</td>
				<td><input type="text" name="email" /></td>
			</tr>
			<tr>
				<td>Password</td>
				<td>:</td>
				<td><input type="password" name="password" /></td>
			</tr>
			<tr>
				<td></td>
				<td></td>
				<td><input type="submit" value="register" /></td>
			</tr>
		</table>
	</form>
	
</body>
</html>