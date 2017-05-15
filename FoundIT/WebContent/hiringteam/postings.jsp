<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Postings</title>
</head>
<body>

	<h2>Postings</h2>
	
	<ul>
		<li><a href="hiringteam">Home</a></li>
	</ul>

	<table border="1">
		<tr>
			<th>Job Detail</th>
			<th>Status</th>
			<th>Actions</th>
		</tr>
		<c:forEach var="posting" items="${postings}" varStatus="loop">
			<c:choose>
				<c:when test="${posting.status == 'In review'}">
					<tr>
						<td>
							<table>
								<tr>
									<td>${posting.companyName}</td>
									<td></td>
									<td></td>
								</tr>
								<tr>
									<td>Locations</td>
									<td>:</td>
									<td>${posting.location }</td>
								</tr>
								<tr>
									<td>Type</td>
									<td>:</td>
									<td>${posting.positionType }</td>
								</tr>
								<tr>
									<td>Desc</td>
									<td>:</td>
									<td>${posting.descriptions }</td>
								</tr>

							</table>
						</td>
						<td>${posting.status}</td>
						<td><a href="hiringteam?applicants=${posting.jobId }">
								Candidate List</a></td>
					</tr>
				</c:when>
			</c:choose>
		</c:forEach>
	</table>


</body>
</html>