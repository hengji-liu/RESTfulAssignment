<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset==UTF-8">
<title>Create a new job posting</title>
</head>
<body>
	<form action="manager?method=createPosting" method="post">
		<table>
			<tr>
				<td>Company Name</td>
				<td><input type="text" name="companyName"></td>
			</tr>
			<tr>
				<td>Salary Rate</td>
				<td><input type="text" name="salaryRate"></td>
			</tr>
			<tr>
				<td>Position Type</td>
				<td><input type="text" name="positionType"></td>
			</tr>
			<tr>
				<td>Location</td>
				<td><input type="text" name="location"></td>
			</tr>
			<tr>
				<td>Descriptions</td>
				<td><input type="text" name="descriptions"></td>
			</tr>
			<tr>
				<td>Click to create</td>
				<td><input type="submit" value="Create"></td>
			</tr>
		</table>
	</form>

</body>
</html>