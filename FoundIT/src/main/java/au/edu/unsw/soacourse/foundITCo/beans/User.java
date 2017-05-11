package au.edu.unsw.soacourse.foundITCo.beans;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="users")
public class User {

	@DatabaseField(id = true)
    private String email;
	@DatabaseField
    private String password;
	@DatabaseField
    private String userType;
	@DatabaseField
    public boolean valid;
	
    public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

    public String getPassword() {
       return password;
	}

    public void setPassword(String newPassword) {
       password = newPassword;
	}
	
			
    public String getEmail() {
       return email;
			}

    public void setEmail(String newEmail) {
       email = newEmail;
			}

				
    public boolean isValid() {
       return valid;
	}

    public void setValid(boolean newValid) {
       valid = newValid;
	}	
}
