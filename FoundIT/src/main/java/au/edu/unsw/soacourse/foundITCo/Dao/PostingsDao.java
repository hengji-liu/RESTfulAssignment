package au.edu.unsw.soacourse.foundITCo.Dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import au.edu.unsw.soacourse.foundITCo.Utils;
import au.edu.unsw.soacourse.foundITCo.beans.Keys;
import au.edu.unsw.soacourse.foundITCo.beans.Posting;

public class PostingsDao {

	// TODO change ip when deploy to docker
	private static final String JOB_URL = "http://localhost:8080/JobServices";

	private void addKeys(WebClient client, String shortKey) {
		client.header(Keys.SECURITY_KEY, Keys.SECURITY_VAL);
		client.header(Keys.SHORT_KEY, shortKey);
	}

	public List<Posting> findPostingById(List<String> ids, String shortKey) {
		List<Posting> list = new ArrayList<>();
		WebClient client = WebClient.create(JOB_URL, Arrays.asList(new JacksonJsonProvider()));
		for (Iterator<String> iterator = ids.iterator(); iterator.hasNext();) {
			String id = (String) iterator.next();
			client.back(true);
			client.path("/postings/" + id);
			addKeys(client, shortKey);
			try {
				Posting p = client.get(Posting.class);
				Utils.trasnfromPostingStatus(p);
				list.add(p);
			} catch (Exception e) {
				// TODO
				System.out.println(" this posting id is not in the db of jobservices");
			}
		}
		return list;
	}

	public Response createPosting(Posting posting, String shortKey){
		WebClient client = WebClient.create(JOB_URL, Arrays.asList(new JacksonJsonProvider()));
		client.path("/postings");
		client.type(MediaType.APPLICATION_JSON);
		addKeys(client, shortKey);
		client.post(posting);
		Response clientResponse = client.getResponse();
		return clientResponse;
	}
}
