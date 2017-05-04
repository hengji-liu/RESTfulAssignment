package au.edu.unsw.soacourse.jobs.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Application {

	/*
	 * Applications: _appId, _jobId, candidate's details, cover letter (text),
	 * status (received, in-review, accept/reject, etc.). In practice, an
	 * application is likely to include attachments (e.g., resume). To simplify,
	 * just deal with text here. Have a look at a seek.com.au's application
	 * page, to see the types of information relevant to applications.
	 */

	private String appId;
	private String jobId;
	private String candidateDetails;
	private String coverLetter;
	private String status; // received, inReview, accept/reject

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getCandidateDetails() {
		return candidateDetails;
	}

	public void setCandidateDetails(String candidateDetails) {
		this.candidateDetails = candidateDetails;
	}

	public String getCoverLetter() {
		return coverLetter;
	}

	public void setCoverLetter(String coverLetter) {
		this.coverLetter = coverLetter;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
