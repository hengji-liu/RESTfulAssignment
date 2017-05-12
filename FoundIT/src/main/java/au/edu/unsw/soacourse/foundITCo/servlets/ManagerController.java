package au.edu.unsw.soacourse.foundITCo.servlets;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import com.j256.ormlite.dao.Dao;

import au.edu.unsw.soacourse.foundITCo.DBUtil;
import au.edu.unsw.soacourse.foundITCo.Utils;
import au.edu.unsw.soacourse.foundITCo.beans.Keys;
import au.edu.unsw.soacourse.foundITCo.beans.Posting;
import au.edu.unsw.soacourse.foundITCo.beans.User;
import au.edu.unsw.soacourse.foundITCo.beans.UserPosting;

@WebServlet("/manager")
public class ManagerController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// TODO change ip when deploy to docker
	private static final String JOB_URL = "http://localhost:8080/JobServices";
	private static final String POLL_URL = "http://localhost:8080/PollingServices";

	private Dao<UserPosting, String> userPostingDao = DBUtil.getUserPostingDao();

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
		User userInSession = Utils.getLoginedUser(request.getSession());
		String userId = userInSession.getEmail();
		List<UserPosting> ups = null;
		try {
			ups = userPostingDao.queryForEq("user_id", userInSession);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// get postings from jobservices
		List<Posting> list = new ArrayList<>();
		if (null != ups && 0 != ups.size()) {
			WebClient client = WebClient.create(JOB_URL, Arrays.asList(new JacksonJsonProvider()));
			for (Iterator iterator = ups.iterator(); iterator.hasNext();) {
				UserPosting up = (UserPosting) iterator.next();
				client.back(true);
				client.path("/postings/" + up.getPosting_id());
				addKeys(client);
				try {
					Posting p = client.get(Posting.class);
					Utils.trasnfromPostingStatus(p);
					list.add(p);
				} catch (Exception e) {
					// TODO
					System.out.println(" this posting id is not in the db of jobservices");
				}
			}
		}
		request.setAttribute("list", list);
		request.getRequestDispatcher("manager/managePosting.jsp").forward(request, response);
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
			String createdId = createdURL.substring(createdURL.lastIndexOf('/') + 1, createdURL.length());
			User userInSession = Utils.getLoginedUser(request.getSession());
			// insert into db
			UserPosting up = new UserPosting();
			up.setId(UUID.randomUUID().toString());
			up.setPosting_id(createdId);
			up.setUser(userInSession);
			try {
				userPostingDao.create(up);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			request.getRequestDispatcher("manager/createPosting.jsp").forward(request, response);
		}
	}
}
