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
<a href="jobseeker?method=gotoManageApplication&archived=1">Show
	archived job posting</a>

<body>
	<table border="1">
		<tr>
			<th>Jobs I applied for</th>
			<th>Application status</th>
			<th>Action</th>
		</tr>
		<c:forEach var="application" items="${list}">
            <tr>
				<td>${application.jobId}</td>
				<td>${application.status}</td>
				<td><c:choose>
						<c:when test="${application.status == 'Received'}">
							<a
								href="jobseeker?method=gotoUpdateApplication&aid=${application.appId }">
								Click to update application</a>
						</c:when>
						<c:when test="${application.status == 'In review'}">
							Please wait until reviewing is finished.
					  </c:when>
						<c:when test="${posting.status == 'Accepted'}">
							<a
								href="jobseeker?method=gotoInterviewPoll&aid=${application.appId}">
								Click to choose an interview time</a>
						</c:when>
						<c:otherwise>
							<a href="jobseeker?method=archive&aid=${application.appId}">
								Click to archive this application</a>
						</c:otherwise>
					</c:choose></td>
			</tr>
		</c:forEach>
	</table>
</body>
</html>