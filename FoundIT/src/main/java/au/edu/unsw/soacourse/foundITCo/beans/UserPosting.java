package au.edu.unsw.soacourse.foundITCo.beans;

import com.j256.ormlite.field.DatabaseField;

public class UserPosting {
	@DatabaseField(id = true, generatedId = true)
	private String id;
	@DatabaseField
	private User user;
	@DatabaseField(foreign = true, canBeNull = false)
	private String posting_id;
	
	public UserPosting() {
		
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
	public String getPosting_id() {
		return posting_id;
	}
	public void setPosting_id(String posting_id) {
		this.posting_id = posting_id;
	}

	
}
