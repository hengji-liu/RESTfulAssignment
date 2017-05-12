package au.edu.unsw.soacourse.foundITCo.servlets;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import au.edu.unsw.soacourse.foundITCo.beans.Keys;
import au.edu.unsw.soacourse.foundITCo.beans.Posting;

@WebServlet("/manager")
public class ManagerController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// TODO change ip when deploy to docker
	private static final String JOB_URL = "http://localhost:8080/JobServices";
	private static final String POLL_URL = "http://localhost:8080/PollingServices";

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
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

	private void addKeys(WebClient client) {
		client.header(Keys.SECURITY_KEY, Keys.SECURITY_VAL);
		client.header(Keys.SHORT_KEY, Keys.SHORT_VAL_MANAGER);
	}

	private void gotoCreatePosting(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("manager/createPosting.jsp").forward(request, response);
	}

	private void gotoManagePosting(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO load posting list
		request.getRequestDispatcher("manager/createPosting.jsp").forward(request, response);
	}

	private void createPosting(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// get paras
		String companyName = request.getParameter("companyName");
		String salaryRate = request.getParameter("salaryRate");
		String positionType = request.getParameter("positionType");
		String location = request.getParameter("location");
		String descriptions = request.getParameter("descriptions");
		// assembly pojo
		Posting posting = new Posting();
		posting.setCompanyName(companyName);
		posting.setSalaryRate(salaryRate);
		posting.setPositionType(positionType);
		posting.setLocation(location);
		posting.setDescriptions(descriptions);
		// post to job services
		WebClient client = WebClient.create(JOB_URL, Arrays.asList(new JacksonJsonProvider()));
		client.path("/postings");
		client.type(MediaType.APPLICATION_JSON);
		 addKeys(client);
		client.post(posting);
		// deal with response
		Response clientResponse = client.getResponse();
		int httpStatus = clientResponse.getStatus();
		if (201 != httpStatus) {
			request.setAttribute("errorCode", httpStatus);
			request.getRequestDispatcher("manager/fail.jsp").forward(request, response);
		} else {
			String createdURL = clientResponse.getLocation().toString();
			String createdId = createdURL.substring(createdURL.lastIndexOf('/')+1, createdURL.length());
			System.out.println(createdId);
			//TODO get manager userid from session, insert into db
			request.getRequestDispatcher("manager/createPosting.jsp").forward(request, response);
		}
	}
}
