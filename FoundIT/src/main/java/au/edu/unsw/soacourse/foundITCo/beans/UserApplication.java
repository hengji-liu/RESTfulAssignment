package au.edu.unsw.soacourse.foundITCo.beans;

import com.j256.ormlite.field.DatabaseField;

public class UserApplication {
	@DatabaseField(id = true)
	private String id;
	@DatabaseField(foreign = true, canBeNull = false)
	private User user;
	@DatabaseField
	private String application_id;
	@DatabaseField
	private String posting_id;
	@DatabaseField
	private String poll_id; // poll to set interview time
	@DatabaseField
	private int archived; // 0 is not, 1 is archived

	public UserApplication() {
	}

	public UserApplication(String id, User user, String application_id, String posting_id, String poll_id,
			int archived) {
		super();
		this.id = id;
		this.user = user;
		this.application_id = application_id;
		this.posting_id = posting_id;
		this.poll_id = poll_id;
		this.archived = archived;
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

	public String getApplication_id() {
		return application_id;
	}

	public void setApplication_id(String application_id) {
		this.application_id = application_id;
	}

	public String getPosting_id() {
		return posting_id;
	}

	public void setPosting_id(String posting_id) {
		this.posting_id = posting_id;
	}

	public String getPoll_id() {
		return poll_id;
	}

	public void setPoll_id(String poll_id) {
		this.poll_id = poll_id;
	}

	public int getArchived() {
		return archived;
	}

	public void setArchived(int archived) {
		this.archived = archived;
	}

}
