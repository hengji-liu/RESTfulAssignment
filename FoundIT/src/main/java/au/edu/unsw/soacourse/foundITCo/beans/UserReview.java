package au.edu.unsw.soacourse.foundITCo.beans;

import com.j256.ormlite.field.DatabaseField;

public class UserReview {
	public static final String USER_ID = "user_id";
	
	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField(foreign = true, canBeNull = false, columnName = USER_ID)
	private User user;
	@DatabaseField
	private String review_id;
	
	public UserReview() {
		
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

	public String getReview_id() {
		return review_id;
	}

	public void setReview_id(String review_id) {
		this.review_id = review_id;
	}
	
}
