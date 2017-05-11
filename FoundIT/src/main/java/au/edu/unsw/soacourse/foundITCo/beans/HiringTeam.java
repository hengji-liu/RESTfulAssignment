package au.edu.unsw.soacourse.foundITCo.beans;

import com.j256.ormlite.field.DatabaseField;

public class HiringTeam {
	@DatabaseField(id =true, foreign = true)
	private User teamMember;
	@DatabaseField(id =true, foreign = true)
	private UserApplication userApplication;
	
	public HiringTeam() {
		
	}

	public User getTeamMember() {
		return teamMember;
	}

	public void setTeamMember(User teamMember) {
		this.teamMember = teamMember;
	}

	public UserApplication getUserApplication() {
		return userApplication;
	}

	public void setUserApplication(UserApplication userApplication) {
		this.userApplication = userApplication;
	}

}
