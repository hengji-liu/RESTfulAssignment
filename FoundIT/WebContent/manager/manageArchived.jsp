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
<ul>
	<li><a href="manager?method=gotoManagePosting&archived=0">Show
			active job posting</a></li>
	<li><a href="manager/home_manager.jsp">Go back to home</a></li>
</ul>
<body>
	<table border="1">
		<tr>
			<th>Job posting information</th>
			<th>Job posting status</th>
		</tr>
		<c:forEach var="posting" items="${list}">
            <tr>
				<td>${posting.companyName},${posting.positionType},
					${posting.location }, ${posting.descriptions }</td>
				<td>${posting.status}</td>
			</tr>
		</c:forEach>
	</table>
</body>
</html>