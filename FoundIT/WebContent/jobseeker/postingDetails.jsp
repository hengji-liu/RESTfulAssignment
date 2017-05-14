<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset==UTF-8">
<title>Job postings Details</title>
</head>
<a href="jobseeker?method=gotoApply&id=${posting.jobId }">Apply for this job</a>
<body>
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
</body>
</html>