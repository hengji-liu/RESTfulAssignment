<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset==UTF-8">
<title>Job posting Details</title>
</head>
<body>
	<h1>Job Posting Details</h1>
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

	<c:choose>
		<c:when test="${posting.status == 'Created'}">
			<a
				href="manager?method=changeStatus&newStatus=open&pid=${posting.jobId }">Open
				this job posting</a>
			<br />
			<h1>Update posting</h1>
			<form action="manager?method=updatePosting&pid=${posting.jobId }"
				method="post">
				<table>
					<tr>
						<td>Company Name</td>
						<td><input type="text" name="companyName"
							value="${posting.companyName }"></td>
					</tr>
					<tr>
						<td>Salary Rate</td>
						<td><input type="text" name="salaryRate"
							value="${posting.salaryRate }"></td>
					</tr>
					<tr>
						<td>Position Type</td>
						<td><input type="text" name="positionType"
							value="${posting.positionType }"></td>
					</tr>
					<tr>
						<td>Location</td>
						<td><input type="text" name="location"
							value="${posting.location }"></td>
					</tr>
					<tr>
						<td>Descriptions</td>
						<td><input type="text" name="descriptions"
							value="${posting.descriptions }"></td>
					</tr>
					<tr>
						<td>Click to update</td>
						<td><input type="submit" value="Update"></td>
					</tr>
				</table>
			</form>
		</c:when>
		<c:when test="${posting.status == 'Open'}">
			<c:if test="${size == 0}">
				<h1>Update posting</h1>
				<form action="manager?method=updatePosting&pid=${posting.jobId }"
					method="post">
					<table>
						<tr>
							<td>Company Name</td>
							<td><input type="text" name="companyName"
								value="${posting.companyName }"></td>
						</tr>
						<tr>
							<td>Salary Rate</td>
							<td><input type="text" name="salaryRate"
								value="${posting.salaryRate }"></td>
						</tr>
						<tr>
							<td>Position Type</td>
							<td><input type="text" name="positionType"
								value="${posting.positionType }"></td>
						</tr>
						<tr>
							<td>Location</td>
							<td><input type="text" name="location"
								value="${posting.location }"></td>
						</tr>
						<tr>
							<td>Descriptions</td>
							<td><input type="text" name="descriptions"
								value="${posting.descriptions }"></td>
						</tr>
						<tr>
							<td>Click to update</td>
							<td><input type="submit" value="Update"></td>
						</tr>
					</table>
				</form>
			</c:if>
			<c:if test="${size>0 }">
				<h1>Applications for this job</h1>
				<table border="1">
					<tr>
						<th>Candidate details</th>
						<th>Cover letter</th>
						<th>Application Status</th>
					</tr>
					<c:forEach var="app" items="${applications}">
						<tr>
							<td>${app.candidateDetails }</td>
							<td>${app.coverLetter }</td>
							<td>${app.status }</td>
						</tr>
					</c:forEach>
				</table>
				<br />
				<h1>Assign reviewers</h1>
				<form
					action="manager?method=changeStatus&newStatus=in_review&pid=${posting.jobId }"
					method="post">
					<input type="submit" value="Assign">
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
			</c:if>
		</c:when>
		<c:when test="${posting.status == 'In review'}">
			<h1>Applications for this job</h1>
			<table border="1">
				<tr>
					<th>Candidate details</th>
					<th>Cover letter</th>
					<th>Application Status</th>
				</tr>
				<c:forEach var="app" items="${applications}">
					<tr>
						<td>${app.candidateDetails }</td>
						<td>${app.coverLetter }</td>
						<td>${app.status }</td>
					</tr>
				</c:forEach>
			</table>
		</c:when>
		<c:when test="${posting.status == 'Processed'}">
			<h1>Applications for this job</h1>
			<table border="1">
				<tr>
					<th>Candidate details</th>
					<th>Cover letter</th>
					<th>Application Status</th>
					<th>Reviewers' comments</th>
				</tr>
				<c:forEach var="app" items="${applications}">
					<tr>
						<td>${app.candidateDetails }</td>
						<td>${app.coverLetter }</td>
						<td>${app.status }</td>
						<td>${app.jobId }</td>
					</tr>
				</c:forEach>
			</table>
			<br />
			<h1>Set Interview time for short-listed candidates to choose</h1>
			<form
				action="manager?method=changeStatus&newStatus=sent_invitations&pid=${posting.jobId}"
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
		</c:when>
		<c:otherwise>
			<!-- sent invitation -->
			<h1>Short listed applications, their review and poll response</h1>
			<table border="1">
				<tr>
					<th>Candidate details</th>
					<th>Cover letter</th>
					<th>Application Status</th>
					<th>Reviewers' comments and poll response</th>
				</tr>
				<c:forEach var="app" items="${applications}">
					<tr>
						<td>${app.candidateDetails }</td>
						<td>${app.coverLetter }</td>
						<td>${app.status }</td>
						<td>${app.jobId }</td>
					</tr>
				</c:forEach>
			</table>

		</c:otherwise>
	</c:choose>


</body>
</html>