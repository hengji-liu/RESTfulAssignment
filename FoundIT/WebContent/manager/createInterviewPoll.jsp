<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset==UTF-8">
<title>Set interview time</title>
</head>
<body>
	<form
		action="manager?method=changeStatus&newStatus=sent_invitations&pid=${pid}"
		method="post">
		<input type="submit" value="Send invitations">
		<table border="1">
			<tr>
				<th></th>
				<th>Possible dates for applicants to choose from</th>
			</tr>
			<tr>
				<td>Option 1</td>
				<td><input type="text" name="option1"></td>
			</tr>
			<tr>
				<td>Option 2</td>
				<td><input type="text" name="option2"></td>
			</tr>
			<tr>
				<td>Option 3</td>
				<td><input type="text" name="option3"></td>
			</tr>
		</table>
	</form>
</body>
</html>