package au.edu.unsw.soacourse.foundITCo.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.cxf.jaxrs.client.WebClient;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import au.edu.unsw.soacourse.foundITCo.Keys;
import au.edu.unsw.soacourse.foundITCo.beans.Application;
import au.edu.unsw.soacourse.foundITCo.beans.Review;

public class ReviewsDao {

	// TODO change ip when deploy to docker
	private static final String JOB_URL = "http://localhost:8080/JobServices";
	private String shortKey;

	public ReviewsDao(String shortKey) {
		super();
		this.shortKey = shortKey;
	}

	private void addKeys(WebClient client) {
		client.header(Keys.SECURITY_KEY, Keys.SECURITY_VAL);
		client.header(Keys.SHORT_KEY, shortKey);
	}

	public List<Review> findReviewByAppId(String aid) {
		List<Review> list = new ArrayList<>();
		WebClient client = WebClient.create(JOB_URL, Arrays.asList(new JacksonJsonProvider()));
		client.path("/applications/" + aid + "/reviews");
		addKeys(client);
		list.addAll(client.getCollection(Review.class));
		return list;
	}
}
