package au.edu.unsw.soacourse.foundITCo.Dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import au.edu.unsw.soacourse.foundITCo.Utils;
import au.edu.unsw.soacourse.foundITCo.beans.Application;
import au.edu.unsw.soacourse.foundITCo.beans.Keys;
import au.edu.unsw.soacourse.foundITCo.beans.Posting;

public class ApplicationsDao {

	// TODO change ip when deploy to docker
	private static final String JOB_URL = "http://localhost:8080/JobServices";
	private String shortKey;

	public ApplicationsDao(String shortKey) {
		super();
		this.shortKey = shortKey;
	}

	private void addKeys(WebClient client) {
		client.header(Keys.SECURITY_KEY, Keys.SECURITY_VAL);
		client.header(Keys.SHORT_KEY, shortKey);
	}

	public List<Application> findApplicationByPostingId(String id) {
		List<Application> list = new ArrayList<>();
		WebClient client = WebClient.create(JOB_URL, Arrays.asList(new JacksonJsonProvider()));
		client.path("/postings/" + id + "/applications");
		addKeys(client);
		list = (List<Application>) client.getCollection(Application.class);
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Application application = (Application) iterator.next();
			System.out.println(application.getAppId());
		}
		return list;
	}

	// public List<Posting> findPostingById(List<String> ids) {
	// List<Posting> list = new ArrayList<>();
	// WebClient client = WebClient.create(JOB_URL, Arrays.asList(new
	// JacksonJsonProvider()));
	// for (Iterator<String> iterator = ids.iterator(); iterator.hasNext();) {
	// String id = (String) iterator.next();
	// client.back(true);
	// client.path("/postings/" + id);
	// addKeys(client);
	// try {
	// Posting p = client.get(Posting.class);
	// Utils.trasnfromPostingStatus(p);
	// list.add(p);
	// } catch (Exception e) {
	// // TODO
	// System.out.println(" this posting id is not in the db of jobservices");
	// }
	// }
	// return list;
	// }

	// public Response createPosting(Posting posting){
	// WebClient client = WebClient.create(JOB_URL, Arrays.asList(new
	// JacksonJsonProvider()));
	// client.path("/postings");
	// client.type(MediaType.APPLICATION_JSON);
	// addKeys(client);
	// client.post(posting);
	// Response serviceResponse = client.getResponse();
	// return serviceResponse;
	// }

	public Response updateStatus(String aid, String newStatus) {
		WebClient client = WebClient.create(JOB_URL, Arrays.asList(new JacksonJsonProvider()));
		client.path("/applications/" + newStatus + "/" + aid);
		addKeys(client);
		client.put(null);
		Response serviceResponse = client.getResponse();
		return serviceResponse;
	}
}
