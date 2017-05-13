<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset==UTF-8">
<title>Manage current job postings</title>
</head>
<body>
	<form
		action="manager?method=changeStatus&newStatus=in_review&pid=${pid }"
		method="post">
		<input type="submit" value="Create">
		<table border="1">
			<tr>
				<th>Reviewer</th>
			</tr>
			<c:forEach var="reviewer" items="${reviewers}">
            <tr>
					<td><input type="checkbox" name="selectedReviewers"
						value="${reviewer.email }">${reviewer.email }<br></td>
				</tr>
			</c:forEach>
		</table>
	</form>
</body>
</html>