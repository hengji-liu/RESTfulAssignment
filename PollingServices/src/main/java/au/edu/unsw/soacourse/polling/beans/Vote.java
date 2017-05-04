package au.edu.unsw.soacourse.polling.beans;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "votes")
public class Vote {
	@DatabaseField
	private String voteId;
	@DatabaseField
	private String participantName;
	@DatabaseField
	private String chosenOption;
	
	public Vote() {
		
	}
	
	public Vote(String voteId, String participantName, String chosenOption) {
		super();
		this.voteId = voteId;
		this.participantName = participantName;
		this.chosenOption = chosenOption;
	}

	public String getVoteId() {
		return voteId;
	}
	public void setVoteId(String voteId) {
		this.voteId = voteId;
	}
	public String getParticipantName() {
		return participantName;
	}
	public void setParticipantName(String participantName) {
		this.participantName = participantName;
	}
	public String getChosenOption() {
		return chosenOption;
	}
	public void setChosenOption(String chosenOption) {
		this.chosenOption = chosenOption;
	}
}
