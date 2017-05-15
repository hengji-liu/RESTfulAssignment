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
	<c:if test="${0 == archived}">
		<li><a href="manager?method=gotoManagePosting&archived=1">Show
				archived job posting</a></li>
	</c:if>
	<c:if test="${1 == archived}">
		<li><a href="manager?method=gotoManagePosting&archived=0">Show
				active job posting</a></li>
	</c:if>

	<li><a href="manager/home_manager.jsp">Go back to home</a></li>
</ul>
<body>
	<table border="1">
		<tr>
			<th>Job posting information</th>
			<th>Job posting status</th>
			<c:if test="${0 == archived}">
				<th>Action</th>
			</c:if>
		</tr>
		<c:forEach var="posting" items="${list}">
            <tr>
				<td><a
					href="manager?method=gotoPostingDetails&pid=${posting.jobId }">
						${posting.companyName},${posting.positionType}, ${posting.location },
						${posting.descriptions }</a></td>
				<td>${posting.status}</td>
				<c:if test="${0 == archived}">
					<td><c:choose>
							<c:when test="${posting.status == 'Created'}">
									&lt;-- To see details or open this posting
							</c:when>
							<c:when test="${posting.status == 'Open'}">
									&lt;-- To collect candidate and assign reviewers for this posting
							</c:when>
							<c:when test="${posting.status == 'In review'}">
							Please wait until reviewing is finished.
					  </c:when>
							<c:when test="${posting.status == 'Processed'}">
								&lt;-- To create an interview time poll for short-listed candidates
							</c:when>
							<c:otherwise>
								<a href="manager?method=archive&pid=${posting.jobId}">
									Archive this posting</a>
							</c:otherwise>
						</c:choose></td>
				</c:if>
			</tr>
		</c:forEach>
	</table>



</body>
</html>