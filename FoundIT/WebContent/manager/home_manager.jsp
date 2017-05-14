<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<% 
String path = request.getContextPath(); // absolute path
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/"; 
%> 
<html>
<head>
<base href=" <%=basePath%>"> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Hi There</title>
</head>
<body>

    <ul>
        <li><a href="signout">Sign out</a></li>
        <li><a href="manager?method=gotoCreatePosting">Create a new job posting</a></li>
        <li><a href="manager?method=gotoManagePosting&archived=0">Manager my job postings</a></li>
     </ul>

	
</body>
</html>