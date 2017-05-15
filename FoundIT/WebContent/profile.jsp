<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Profile</title>
</head>
<body>

	<h2>Profile</h2>
	<form method="POST" action='user?update' name="frmAddUser">
		<input type="hidden" name="id"
			value="<c:out value="${userProfile.id}" />" />
		<table>
			<tr>
				<td>Email</td>
				<td>:</td>
				<td><input type="text" readonly="readonly" name="email"
					value="<c:out value="${loginedUser.email}" />" /></td>
			</tr>
			<tr>
				<td>First Name</td>
				<td>:</td>
				<td><input type="text" name="firstName"
					value="<c:out value="${userProfile.firstName}" />" /></td>
			</tr>
			<tr>
				<td>Last Name</td>
				<td>:</td>
				<td><input type="text" name="lastName"
					value="<c:out value="${userProfile.lastName}" />" /></td>
			</tr>
			<tr>
				<td>Current Position</td>
				<td>:</td>
				<td><input type="text" name="currentPosition"
					value="<c:out value="${userProfile.currentPosition}" />" /></td>
			</tr>
			<tr>
				<td>Education</td>
				<td>:</td>
				<td><input type="text" name="education"
					value="<c:out value="${userProfile.education}" />" /></td>
			</tr>
			<tr>
				<td>Experience</td>
				<td>:</td>
				<td><input type="text" name="experience"
					value="<c:out value="${userProfile.experience}" />" /></td>
			</tr>
			<tr>
				<td>Professional Skill</td>
				<td>:</td>
				<td><input type="text" name="professionalSkill"
					value="<c:out value="${userProfile.professionalSkill}" />" /></td>
			</tr>
			<tr>
				<td></td>
				<td></td>
				<td><input type="submit" value="Update" /></td>
			</tr>
		</table>
	</form>

</body>
</html>