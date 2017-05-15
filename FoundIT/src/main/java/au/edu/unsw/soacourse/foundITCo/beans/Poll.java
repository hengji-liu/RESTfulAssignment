package au.edu.unsw.soacourse.foundITCo.beans;

import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "polls")
public class Poll {
	@DatabaseField(id = true)
	private String pollId;
	@DatabaseField
	private String title;
	@DatabaseField
	private String description;
	@DatabaseField
	private String optionType;
	@DatabaseField
	private String options;
	@DatabaseField
	private String comments;
	@DatabaseField
	private String finalChoice;
	private List<Vote> votes;
	
	public List<Vote> getVotes() {
		return votes;
	}
	
	public void addVote(Vote vote) {
		votes.add(vote);
	}

	public void setVotes(List<Vote> votes) {
		this.votes = votes;
	}

	public Poll() {
		
	}
	
	public Poll(String pollId, String title, String description,
			String optionType, String options, String comments) {
		super();
		this.pollId = pollId;
		this.title = title;
		this.description = description;
		this.optionType = optionType;
		this.options = options;
		this.comments = comments;
		this.votes = new ArrayList<Vote>();
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
	
	public String getPollId() {
		return pollId;
	}

	public void setPollId(String pollId) {
		this.pollId = pollId;
	}

	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
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
	
//	public List<Vote> getVotes(){
//		return new ArrayList<Vote>(votes);
//	}
	
	@Override
	public int hashCode() {
		return pollId.hashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == null || other.getClass() != getClass()) {
			return false;
		}
		return pollId.equals(((Poll) other).pollId);
	}

	@Override
	public String toString() {
		return "Poll [pollId=" + pollId + ", title=" + title + ", description="
				+ description + ", optionType=" + optionType + ", options="
				+ options + ", comments=" + comments + ", finalChoice="
				+ finalChoice + "]";
	}

}
