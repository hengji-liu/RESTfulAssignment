package au.edu.unsw.soacourse.foundITCo.beans;

import com.j256.ormlite.field.DatabaseField;

public class ApplicationReviewer {
	public static final String USER_ID = "user_id";
	public static final String APPLICATION_ID = "application_id";
	
	@DatabaseField(generatedId = true)
	private int id;
	
	@DatabaseField(foreign = true, columnName = USER_ID)
	private User reviewer;
	@DatabaseField(foreign = true, columnName = APPLICATION_ID)
	private UserApplication userApplication;
	
	public ApplicationReviewer() {
		
	}
	

	public int getId() {
		return id;
	}



	public void setId(int id) {
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
