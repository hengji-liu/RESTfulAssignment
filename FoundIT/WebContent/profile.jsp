<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>													
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>User Profile</title>
</head>			
<body>
    <form method="POST" action='user?update' name="frmAddUser">
        <input type="hidden" name="id"
            value="<c:out value="${userProfile.id}" />" />
        Email : <input type="text" readonly="readonly" name="email"
            value="<c:out value="${loginedUser.email}" />" /> <br /> 
        First Name : <input
            type="text" name="firstName"
            value="<c:out value="${userProfile.firstName}" />" /> <br /> 
        Last Name : <input
            type="text" name="lastName"
            value="<c:out value="${userProfile.lastName}" />" /> <br /> 
        Current Position : <input
            type="text" name="currentPosition"
            value="<c:out value="${userProfile.currentPosition}" />" /> <br /> 
        Education : <input
            type="text" name="education"
            value="<c:out value="${userProfile.education}" />" /> <br /> 
        Experience : <input
            type="text" name="experience"
            value="<c:out value="${userProfile.experience}" />" /> <br /> 
        Professional Skill : <input
            type="text" name="professionalSkill"
            value="<c:out value="${userProfile.professionalSkill}" />" /> <br /> 
            
            <input
            type="submit" value="Update" />
    </form>
</body>
</html>