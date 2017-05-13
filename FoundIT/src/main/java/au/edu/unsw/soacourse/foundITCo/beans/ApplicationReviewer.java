package au.edu.unsw.soacourse.foundITCo.beans;

import com.j256.ormlite.field.DatabaseField;

public class ApplicationReviewer {
	public static final String USER_ID = "user_id";
	
	@DatabaseField(generatedId = true)
	private String id;
	
	@DatabaseField(id =true, foreign = true, columnName = USER_ID)
	private User reviewer;
	@DatabaseField(id =true, foreign = true)
	private UserApplication userApplication;
	
	public ApplicationReviewer() {
		
	}
	

	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public User getReviewer() {
		return reviewer;
	}

	public void setReviewer(User reviewer) {
		this.reviewer = reviewer;
	}

	public UserApplication getUserApplication() {
		return userApplication;
	}

	public void setUserApplication(UserApplication userApplication) {
		this.userApplication = userApplication;
	}

}
