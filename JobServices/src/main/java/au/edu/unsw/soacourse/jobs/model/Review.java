package au.edu.unsw.soacourse.jobs.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Review {

	/*
	 * Reviews: _reviewId, _appId, reviewer details, comments, decision (e.g.,
	 * recommend, not recommend, etc).
	 */

	private String reviewId;
	private String appId;
	private String reviewerDetails;
	private String comments;
	private String recommend;
	private String notRecommend;

	public Review() {

	}

	public Review(String reviewId, String appId, String reviewerDetails, String comments, String recommend,
			String notRecommend) {
		super();
		this.reviewId = reviewId;
		this.appId = appId;
		this.reviewerDetails = reviewerDetails;
		this.comments = comments;
		this.recommend = recommend;
		this.notRecommend = notRecommend;
	}

	public String getReviewId() {
		return reviewId;
	}

	public void setReviewId(String reviewId) {
		this.reviewId = reviewId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getReviewerDetails() {
		return reviewerDetails;
	}

	public void setReviewerDetails(String reviewerDetails) {
		this.reviewerDetails = reviewerDetails;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getRecommend() {
		return recommend;
	}

	public void setRecommend(String recommend) {
		this.recommend = recommend;
	}

	public String getNotRecommend() {
		return notRecommend;
	}

	public void setNotRecommend(String notRecommend) {
		this.notRecommend = notRecommend;
	}

}
