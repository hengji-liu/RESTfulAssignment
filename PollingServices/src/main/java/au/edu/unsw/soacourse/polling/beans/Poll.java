package au.edu.unsw.soacourse.polling.beans;

import java.util.List;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "polls")
public class Poll {
	@DatabaseField(id = true)
	private String pID;
	@DatabaseField
	private String title;
	@DatabaseField
	private String description;
	@DatabaseField
	private String optionType;
	private List<String> options;
	@DatabaseField
	private String comments;
	@DatabaseField
	private String finalChoice;
	
	public Poll() {
		
	}
	
	public String getpID() {
		return pID;
	}
	public void setpID(String pID) {
		this.pID = pID;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getOptionType() {
		return optionType;
	}
	public void setOptionType(String optionType) {
		this.optionType = optionType;
	}
	public List<String> getOptions() {
		return options;
	}
	public void setOptions(List<String> options) {
		this.options = options;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getFinalChoice() {
		return finalChoice;
	}
	public void setFinalChoice(String finalChoice) {
		this.finalChoice = finalChoice;
	}

}
