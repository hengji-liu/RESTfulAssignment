package au.edu.unsw.soacourse.foundITCo.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import au.edu.unsw.soacourse.foundITCo.Utils;
import au.edu.unsw.soacourse.foundITCo.beans.Application;
import au.edu.unsw.soacourse.foundITCo.Keys;
import au.edu.unsw.soacourse.foundITCo.beans.Review;

public class ReviewDao {
	private static WebClient client;

    private static void addKeys(WebClient client, String shortKey) {
        client.header(Keys.SECURITY_KEY, Keys.SECURITY_VAL);
        client.header(Keys.SHORT_KEY, shortKey);
    }

    public static List<Review> findReviewsById(String baseUri, String shortKey, List<String> ids) {
        List<Review> reviews = new ArrayList<>();
        client = WebClient.create(baseUri, Arrays.asList(new JacksonJsonProvider()));
        for (Iterator<String> iterator = ids.iterator(); iterator.hasNext();) {
            String id = (String) iterator.next();
            client.back(true);
            client.path("/JobServices/reviews/" + id);
            addKeys(client, shortKey);
            try {
            	Review r = client.get(Review.class);
                Utils.trasnfromReviewDecision(r);
                reviews.add(r);
            } catch (Exception e) {
                // TODO
            	e.printStackTrace();
                System.out.println(" this posting id is not in the db of jobservices");
            }
        }
        client.close();
        return reviews;
    }
    
	public static Review findReviewById(String baseUri, String shortKey, String id) {
		WebClient client = WebClient.create(baseUri, Arrays.asList(new JacksonJsonProvider()));
		client.back(true);
		client.path("/JobServices/reviews/" + id);
        addKeys(client, shortKey);
		try {
			Review r = client.get(Review.class);
			Utils.trasnfromReviewDecision(r);
			client.close();
			return r;
		} catch (Exception e) {
			// TODO
			System.out.println(" this application id is not in the db of jobservices");
		}
		client.close();
		return null;
	}
	
    public static List<Review> findReviewsByAppId(String baseUri, String shortKey, String appId) {
        List<Review> reviews = new ArrayList<>();
        client = WebClient.create(baseUri, Arrays.asList(new JacksonJsonProvider()));
//        for (Iterator<String> iterator = ids.iterator(); iterator.hasNext();) {
//            String id = (String) iterator.next();
            client.back(true);
            client.path("/JobServices/applications/" + appId + "/reviews");
            addKeys(client, shortKey);
            try {
                reviews = client.get(new GenericType<List<Review>>() {});
            } catch (Exception e) {
                // TODO
            	e.printStackTrace();
                System.out.println(" this posting id is not in the db of jobservices");
            }
//        }
        client.close();
        return reviews;
    }

    public static Response createReview(String baseUri, String shortKey, Review review){
        WebClient client = WebClient.create(baseUri, Arrays.asList(new JacksonJsonProvider()));
        client.path("/JobServices/reviews");
        client.type(MediaType.APPLICATION_JSON);
        addKeys(client, shortKey);
        client.post(review);
        Response serviceResponse = client.getResponse();
        return serviceResponse;
    }
    
	public static Response updateReview(String baseUri, String shortKey, String id, Review review) {
		WebClient client = WebClient.create(baseUri, Arrays.asList(new JacksonJsonProvider()));
		client.path("/JobServices/reviews/" + id);
		client.type(MediaType.APPLICATION_JSON);
		addKeys(client, shortKey);
		client.put(review);
		Response serviceResponse = client.getResponse();
		return serviceResponse;
	}
}
