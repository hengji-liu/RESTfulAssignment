package foundITCo.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import foundITCo.bean.Posting;

public class PostingServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// TODO change ip when deploy to docker
	private static final String REST_URI = "http://localhost:8080/JobServices";

	public void service(HttpServletRequest request, HttpServletResponse response) {
		String methodName = request.getParameter("method");
		if (methodName == null || methodName.isEmpty())
			throw new RuntimeException("no method");
		// reflection
		Method method = null;
		try {
			method = this.getClass().getDeclaredMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
			method.setAccessible(true);
		} catch (Exception e) {
			throw new RuntimeException(methodName + " does not exist");
		}
		// call method
		try {
			method.invoke(this, request, response);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	private void post(HttpServletRequest request, HttpServletResponse response) {
		WebClient client = WebClient.create(REST_URI, Arrays.asList(new JacksonJsonProvider()));
		client.path("/postings");

		Posting p = new Posting();
		p.setCompanyName("comp");
		p.setDescriptions("desc");
		p.setLocation("syd");
		p.setPositionType("ft");
		p.setSalaryRate("high");

		client.type(MediaType.APPLICATION_JSON);
		client.post(p);

		// **form example**
		// Form form = new Form();
		// form.param("id", "4");
		// form.param("title", "Programming Collective Intelligence");
		// form.param("detail", "Details of Programming Collective
		// Intelligence");
		//
		// bookClient.path("/books").type(MediaType.APPLICATION_FORM_URLENCODED);
		// bookClient.post(form);

	}

	private void update(HttpServletRequest request, HttpServletResponse response) {
		WebClient client = WebClient.create(REST_URI, Arrays.asList(new JacksonJsonProvider()));
		client.path("/postings/{id}");

		Posting p = new Posting();
		p.setCompanyName("comp");
		p.setDescriptions("desc");
		p.setLocation("syd");
		p.setPositionType("ft");
		p.setSalaryRate("high");

		client.type(MediaType.APPLICATION_JSON);
		client.put(p);
	}

	private void get(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		WebClient client = WebClient.create(REST_URI, Arrays.asList(new JacksonJsonProvider()));
		client.path("/postings/" + id);
		client.accept(MediaType.APPLICATION_JSON);
		Posting p = client.get(Posting.class);
		// to display page
		request.setAttribute("posting", p);
		request.getRequestDispatcher("test2.jsp").forward(request, response);
	}

	private void find(HttpServletRequest request, HttpServletResponse response) {

	}

	private void delete(HttpServletRequest request, HttpServletResponse response) {

	}

}
