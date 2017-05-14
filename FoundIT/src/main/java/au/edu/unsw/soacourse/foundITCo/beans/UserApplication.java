package au.edu.unsw.soacourse.foundITCo.beans;

import com.j256.ormlite.field.DatabaseField;

public class UserApplication {
	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField(foreign = true, canBeNull = false, foreignAutoRefresh = true)
	private User user;
	@DatabaseField
	private String applicationId;

	public UserApplication() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}
	
	
}
