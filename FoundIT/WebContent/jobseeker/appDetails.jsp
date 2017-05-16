<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset==UTF-8">
<title>Application Details</title>
</head>
<body>


	<h1>Application Details</h1>
	<table border="1">
		<tr>
			<td>Candidate Details</td>
			<td>${application.candidateDetails }</td>
		</tr>
		<tr>
			<td>Cover Letter</td>
			<td>${application.coverLetter }</td>
		</tr>
	</table>
	<br />

	<c:if test="${application.status == 'Received'}">
		<h1>Posting Details</h1>
		<table border="1">
			<tr>
				<td>Company name</td>
				<td>${posting.companyName}</td>
			</tr>
			<tr>
				<td>Salary rate</td>
				<td>${posting.salaryRate }</td>
			</tr>
			<tr>
				<td>Position type</td>
				<td>${posting.positionType}</td>
			</tr>
			<tr>
				<td>Location</td>
				<td>${posting.location }</td>
			</tr>
			<tr>
				<td>Descriptions</td>
				<td>${posting.descriptions }</td>
			</tr>
		</table>
		<br />
		<h1>Update Application</h1>
		<form
			action="jobseeker?method=updateApplication&id=${application.appId }"
			method="post">
			<table>
				<tr>
					<td>Candidate Details</td>
					<td><input type="text" name="candidateDetails"
						value="${application.candidateDetails }"></td>
				</tr>
				<tr>
					<td>Cover Letter</td>
					<td><input type="text" name="coverLetter"
						value="${application.coverLetter }"></td>
				</tr>
			</table>
			<input type="submit" value="Update">
		</form>
	</c:if>

	<c:if test="${application.status == 'Accepted'}">
		<c:choose>
			<c:when test="${not empty pollNotYet }">Please wait until the manager sets an interview time for you.</c:when>
			<c:when test="${not empty chosenOption}">Your chosen interview time is ${chosenOption }.</c:when>
			<c:otherwise>
				<h1>Vote For Interview Time</h1>
				<form action="jobseeker?method=voteForPoll&pid=${poll.pollId }"
					method="post">
					<table border="1">
						<tr>
							<th>${poll.title }</th>
						</tr>
						<tr>
							<th>${poll.description }</th>
						</tr>
						<tr>
							<td><c:forEach var="opt" items="${options}">
									<input type="radio" name="selectedTime" value="${opt }">${opt }<br />
								</c:forEach></td>
						</tr>
						<tr>
							<td><input type="submit" value="Vote for the selected time"></td>
						</tr>
					</table>
				</form>
			</c:otherwise>
		</c:choose>
	</c:if>

</body>
</html>