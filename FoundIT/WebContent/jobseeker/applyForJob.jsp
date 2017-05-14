<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset==UTF-8">
<title>Apply for job</title>
</head>
<body>
	<form action="jobseeker?method=createApplication&id=${id }" method="post">
		<table>
			<tr>
				<td>Candidate Details</td>
				<td><input type="text" name="candidateDetails"></td>
			</tr>
			<tr>
				<td>Cover Letter</td>
				<td><input type="text" name="coverLetter"></td>
			</tr>
		</table>
		<input type="submit" value="Apply">
	</form>

</body>
</html>