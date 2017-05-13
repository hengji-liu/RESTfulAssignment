package au.edu.unsw.soacourse.foundITCo.beans;

import com.j256.ormlite.field.DatabaseField;

public class UserApplication {
	@DatabaseField(id = true)
	private String id;
	@DatabaseField(foreign = true, canBeNull = false)
	private User user;
	@DatabaseField
	private String application_id;

	public UserApplication() {
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getApplication_id() {
		return application_id;
	}

	public void setApplication_id(String application_id) {
		this.application_id = application_id;
	}
	
	
}
