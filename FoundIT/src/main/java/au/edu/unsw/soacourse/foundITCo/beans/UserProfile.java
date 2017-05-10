package au.edu.unsw.soacourse.foundITCo.beans;

import com.j256.ormlite.field.DatabaseField;

public class UserProfile {
	@DatabaseField(id = true, generatedId = true)
	private String id;
	@DatabaseField(foreign = true, canBeNull = false)
	private User user;
	
	@DatabaseField
	private String firstName;
	@DatabaseField
	private String lastName;
	@DatabaseField
	private String currentPosition;
	@DatabaseField
	private String education;
	@DatabaseField
	private String experience;
	@DatabaseField
	private String profesionalSkill;
	
	public UserProfile() {
		
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

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getCurrentPosition() {
		return currentPosition;
	}

	public void setCurrentPosition(String currentPosition) {
		this.currentPosition = currentPosition;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getExperience() {
		return experience;
	}

	public void setExperience(String experience) {
		this.experience = experience;
	}

	public String getProfesionalSkill() {
		return profesionalSkill;
	}

	public void setProfesionalSkill(String profesionalSkill) {
		this.profesionalSkill = profesionalSkill;
	}
	
	

}
