<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Applicant Review</title>
</head>
<body>
	<h2>Applicant Review</h2>
	<h3>Profile</h3>
	<table>
		<tr>
			<td>First Name</td>
			<td>:</td>
			<td><input type="text" readonly="readonly" name="firstName"
				value="<c:out value="${userProfile.firstName}" />" /></td>
		</tr>
		<tr>
			<td>Last Name</td>
			<td>:</td>
			<td><input type="text" readonly="readonly" name="lastName"
				value="<c:out value="${userProfile.lastName}" />" /></td>
		</tr>
		<tr>
			<td>Current Position</td>
			<td>:</td>
			<td><input type="text" readonly="readonly"
				name="currentPosition"
				value="<c:out value="${userProfile.currentPosition}" />" /></td>
		</tr>
		<tr>
			<td>Education</td>
			<td>:</td>
			<td><input type="text" readonly="readonly" name="education"
				value="<c:out value="${userProfile.education}" />" /></td>
		</tr>
		<tr>
			<td>Experience</td>
			<td>:</td>
			<td><input type="text" readonly="readonly" name="experience"
				value="<c:out value="${userProfile.experience}" />" /></td>
		</tr>
		<tr>
			<td>Professional Skill</td>
			<td>:</td>
			<td><input type="text" readonly="readonly" name="experience"
				value="<c:out value="${userProfile.experience}" />" /></td>
		</tr>
	</table>

	<br />

	<h3>Application</h3>
	<table>
		<tr>
			<td>Detail</td>
			<td>:</td>
			<td><input type="text" readonly="readonly" name="candidateDetail"
				value="<c:out value="${application.candidateDetails}" />" /></td>
		</tr>
		<tr>
			<td>Cover Letter</td>
			<td>:</td>
			<td><textarea rows="6" cols="25"><c:out value="${application.coverLetter}"/></textarea></td>
		</tr>
	</table>

	<br />
	
	<h3>Review</h3>
	<form method="POST" action='hiringteam?review' name="frmAddUser">
		<input type="hidden" name="appId" value="<c:out value="${appId}" />" />
		<input type="hidden" name="jobId" value="<c:out value="${application.jobId}" />" />
		<input type="hidden" name="reviewId" value="<c:out value="${review.reviewId}" />" />
		<table>
			<tr>
				<td>Details</td>
				<td>:</td>
				<td><input type="text" name="details"
					value="<c:out value="${review.reviewerDetails}" />" /></td>
			</tr>
			<tr>
				<td>Comments</td>
				<td>:</td>
				<td><input type="text" name="comments"
					value="<c:out value="${review.comments}" />" /></td>
			</tr>
			<tr>
				<td>Decision</td>
				<td>:</td>
				<td><select name="decision">
						<option value="1">Recommend</option>
						<option value="0">Not Recommend</option>
				</select></td>
			</tr>
			<tr>
				<td></td>
				<td></td>
				<td><input type="submit" value="Review" /></td>
			</tr>
		</table>
	</form>
</body>
</html>