<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset==UTF-8">
<title>Applicants</title>
</head>
<body>
	<h2>Applicants</h2>
	<ul>
		<li><a href="hiringteam?postings">Back</a></li>
	</ul>
	<table border="1">
		<tr>
			<th>Candidate Detail</th>
			<th>Status</th>
			<th>Actions</th>
		</tr>
		<c:forEach var="userProfile" items="${userProfiles}" varStatus="loop">
 			<c:choose>
				<c:when test="${applications[loop.index].status == 'In review'}"> 
					<tr>
						<td>${userProfile.firstName }${userProfile.lastName }<br />
							Cur Position: ${userProfile.currentPosition }
						</td>
						<td>${applications[loop.index].status}</td>
						<td><a
							href="hiringteam?review=${userProfile.user.email }&appId=${appIds[loop.index] }">
								Review application</a></td>
					</tr>
 				</c:when>
			</c:choose> 
		</c:forEach>
	</table>
</body>
</html>