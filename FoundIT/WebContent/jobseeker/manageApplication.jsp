<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset==UTF-8">
<title>Manage my application</title>
</head>

<ul>
	<c:if test="${0 == archived}">
		<li><a href="jobseeker?method=gotoManageApplication&archived=1">Show
				archived job posting</a></li>
	</c:if>
	<c:if test="${1 == archived}">
		<li><a href="jobseeker?method=gotoManageApplication&archived=0">Show
				active job posting</a></li>
	</c:if>
	<li><a href="jobseeker/home_jobseeker.jsp">Go back to home</a></li>
</ul>
<body>
	<table border="1">
		<tr>
			<th>Jobs I applied for</th>
			<th>Application status</th>
			<c:if test="${0 == archived}">
				<th>Action</th>
			</c:if>
		</tr>
		<c:forEach var="application" items="${list}">
            <tr>
				<td><a
					href="jobseeker?method=gotoAppDetails&aid=${application.appId }">${application.jobId}</a></td>
				<td>${application.status}</td>
				<c:if test="${0 == archived}">
					<td><c:choose>
							<c:when test="${application.status == 'Received'}">
							Click on the job post to see application details or update.
						</c:when>
							<c:when test="${application.status == 'In review'}">
							Please wait until reviewing is finished.
					    </c:when>
							<c:when test="${application.status == 'Accepted'}">
							You can click on the job post to vote for you interview time.<br />
							or 	<a href="jobseeker?method=archive&aid=${application.appId}">
									click here to archive this application</a>
							</c:when>
							<c:otherwise>
								<a href="jobseeker?method=archive&aid=${application.appId}">
									Click to archive this application</a>
							</c:otherwise>
						</c:choose></td>
				</c:if>
			</tr>
		</c:forEach>
	</table>
</body>
</html>