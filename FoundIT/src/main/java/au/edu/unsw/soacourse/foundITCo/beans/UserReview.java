package au.edu.unsw.soacourse.foundITCo.beans;

import com.j256.ormlite.field.DatabaseField;

public class UserReview {
	@DatabaseField(id = true, generatedId = true)
	private String id;
	@DatabaseField(foreign = true, canBeNull = false)
	private User user;
	@DatabaseField
	private String review_id;
	
	public UserReview() {
		
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

	public String getReview_id() {
		return review_id;
	}

	public void setReview_id(String review_id) {
		this.review_id = review_id;
	}
	
}
