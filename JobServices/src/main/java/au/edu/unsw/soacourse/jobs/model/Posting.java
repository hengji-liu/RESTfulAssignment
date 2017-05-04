package au.edu.unsw.soacourse.jobs.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Posting {

	/*
	 * Job Postings: _jobId, company name, salary rate, position type, location,
	 * details/job descriptions, status (created, open, in-review, processed,
	 * sent-invitations, etc.). Have a look at a job site like seek.com.au or
	 * LinkedIn to see the types of information relevant to a job posting.
	 */

	private String jobId;
	private String companyName;
	private String salaryRate;
	private String positionType;
	private String location;
	private String descriptions;
	private String status; // created, open, inReview, processed,
	// sentInvitations
	
	public String getJobId() {
		return jobId;
	}
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getSalaryRate() {
		return salaryRate;
	}
	public void setSalaryRate(String salaryRate) {
		this.salaryRate = salaryRate;
	}
	public String getPositionType() {
		return positionType;
	}
	public void setPositionType(String positionType) {
		this.positionType = positionType;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getDescriptions() {
		return descriptions;
	}
	public void setDescriptions(String descriptions) {
		this.descriptions = descriptions;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

}
