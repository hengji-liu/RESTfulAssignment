package au.edu.unsw.soacourse.foundITCo.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import au.edu.unsw.soacourse.foundITCo.Utils;
import au.edu.unsw.soacourse.foundITCo.beans.Application;
import au.edu.unsw.soacourse.foundITCo.beans.Keys;
import au.edu.unsw.soacourse.foundITCo.beans.Review;

public class ReviewDao {
	private static WebClient client;

    private static void addKeys(WebClient client, String shortKey) {
        client.header(Keys.SECURITY_KEY, Keys.SECURITY_VAL);
        client.header(Keys.SHORT_KEY, shortKey);
    }

    public static List<Review> findReviewById(String baseUri, String shortKey, List<String> ids) {
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

    public static Response createReview(String baseUri, String shortKey, Review review){
        WebClient client = WebClient.create(baseUri, Arrays.asList(new JacksonJsonProvider()));
        client.path("/JobServices/reviews");
        client.type(MediaType.APPLICATION_JSON);
        addKeys(client, shortKey);
        client.post(review);
        Response serviceResponse = client.getResponse();
        return serviceResponse;
    }
//
//    public Response updateStatus(String pid, String newStatus){
//        WebClient client = WebClient.create(JOB_URL, Arrays.asList(new JacksonJsonProvider()));
//        client.path("/postings/" +newStatus+"/"+pid);
//        addKeys(client);
//        client.put(null);
//        Response serviceResponse = client.getResponse();
//        return serviceResponse;
//    }
}
