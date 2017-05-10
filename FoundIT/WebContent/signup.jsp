<!DOCTYPE html>
<html>
    <head>
        <title>Register form</title>
    </head>
    <body>
        <form method="post" action="signup">
        Registered as:	<select name="userType">
							  <option value="manager">Manager</option>
							  <option value="hiringteam">Hiring team</option>
							  <option value="jobseeker">Job seeker</option>
						</select> <br/>
        Email:<input type="text" name="email" /><br/>
        Password:<input type="password" name="password" /><br/>
        <input type="submit" value="register" />
        </form>
    </body>
</html>