package au.edu.unsw.soacourse.polling.beans;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@JsonIgnoreProperties(value = {"Poll"}, ignoreUnknown=true)
@DatabaseTable(tableName = "votes")
@XmlRootElement
public class Vote {
	@DatabaseField(id = true)
	@XmlAttribute
	private String voteId;
	
	@DatabaseField(foreign = true, canBeNull = false)
	@XmlElement
	private Poll poll;
	
	@DatabaseField
	@XmlElement(name = "participant-name")
	private String participantName;
	
	@DatabaseField
	@XmlElement
	private String chosenOption;
	
	public Vote() {
		
	}
	
	public Vote(String voteId, String participantName, String chosenOption, Poll poll) {
		super();
		this.voteId = voteId;
		this.participantName = participantName;
		this.chosenOption = chosenOption;
		this.poll = poll;
	}

	public Poll getPoll() {
		return poll;
	}

	public void setPoll(Poll poll) {
		this.poll = poll;
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
	
	@Override
	public int hashCode() {
		return voteId.hashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == null || other.getClass() != getClass()) {
			return false;
		}
		return voteId.equals(((Vote) other).voteId);
	}

	@Override
	public String toString() {
		return "Vote [voteId=" + voteId + ", participantName="
				+ participantName + ", chosenOption=" + chosenOption + "]";
	}
}
