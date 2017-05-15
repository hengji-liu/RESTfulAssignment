package au.edu.unsw.soacourse.foundITCo.dao;

import java.util.Arrays;

import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import au.edu.unsw.soacourse.foundITCo.Keys;
import au.edu.unsw.soacourse.foundITCo.beans.Poll;

public class PollsDao {

	// TODO change ip when deploy to docker
	private static final String POLL_URL = "http://localhost:8080/PollingServices";
	private String shortKey;

	public PollsDao(String shortKey) {
		super();
		this.shortKey = shortKey;
	}

	private void addKeys(WebClient client) {
		client.header(Keys.SECURITY_KEY, Keys.SECURITY_VAL);
		client.header(Keys.SHORT_KEY, shortKey);
	}

	public Poll findPollById(String id) {
		WebClient client = WebClient.create(POLL_URL, Arrays.asList(new JacksonJsonProvider()));
		client.back(true);
		client.path("/polls/" + id);
		addKeys(client);
		try {
			Poll p = client.get(Poll.class);
			return p;
		} catch (Exception e) {
			// TODO
			System.out.println(" this poll id is not in the db of jobservices");
		}
		return null;
	}

	public Response createPoll(String title, String description, String optionsType, String optionsSepBySemicolon,
			String comments, String finalChoice) {
		WebClient client = WebClient.create(POLL_URL, Arrays.asList(new JacksonJsonProvider()));
		client.path("/polls");
		addKeys(client);
		Form form = new Form();
		form.param("title", title);
		form.param("description", description);
		form.param("optionsType", optionsType);
		form.param("options", optionsSepBySemicolon);
		if (null != comments)
			form.param("comments", comments);
		if (null != finalChoice)
			form.param("finalChoice", finalChoice);
		client.type(MediaType.APPLICATION_FORM_URLENCODED);
		client.post(form);
		Response serviceResponse = client.getResponse();
		return serviceResponse;
	}

}
