package au.edu.unsw.soacourse.foundITCo.beans;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "userPostings")
public class UserPosting {
	@DatabaseField(id = true)
	private String id;
	@DatabaseField(foreign = true, canBeNull = false)
	private User user;
	@DatabaseField
	private String posting_id;
	@DatabaseField
	private int archived; // 0 is not, 1 is archived

	public UserPosting() {

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

	public String getPosting_id() {
		return posting_id;
	}

	public void setPosting_id(String posting_id) {
		this.posting_id = posting_id;
	}

	public int getArchived() {
		return archived;
	}

	public void setArchived(int archived) {
		this.archived = archived;
	}

}
