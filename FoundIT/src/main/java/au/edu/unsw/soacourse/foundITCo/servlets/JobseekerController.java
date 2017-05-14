package au.edu.unsw.soacourse.foundITCo.servlets;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;

import com.j256.ormlite.dao.Dao;

import au.edu.unsw.soacourse.foundITCo.DBUtil;
import au.edu.unsw.soacourse.foundITCo.Keys;
import au.edu.unsw.soacourse.foundITCo.Utils;
import au.edu.unsw.soacourse.foundITCo.beans.Application;
import au.edu.unsw.soacourse.foundITCo.beans.Posting;
import au.edu.unsw.soacourse.foundITCo.beans.User;
import au.edu.unsw.soacourse.foundITCo.beans.UserApplication;
import au.edu.unsw.soacourse.foundITCo.beans.UserPosting;
import au.edu.unsw.soacourse.foundITCo.dao.ApplicationsDao;
import au.edu.unsw.soacourse.foundITCo.dao.PostingsDao;

@WebServlet("/jobseeker")
public class JobseekerController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Dao<UserPosting, String> userPostingDao = DBUtil.getUserPostingDao();
	private Dao<UserApplication, String> userApplicationDao = DBUtil.getUserApplicationDao();
	private PostingsDao postingsDao = new PostingsDao(Keys.SHORT_VAL_CANDIDATE);
	private ApplicationsDao applicationsDao = new ApplicationsDao(Keys.SHORT_VAL_CANDIDATE);

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

	private void search(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String keyword = request.getParameter("keyword");
		String status = request.getParameter("status");
		List<Posting> results = postingsDao.search(keyword, status);
		request.setAttribute("list", results);
		request.getRequestDispatcher("jobseeker/displaySearchResult.jsp").forward(request, response);
	}

	private void gotoPostingDetails(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String pid = request.getParameter("id");
		Posting p = postingsDao.findPostingById(pid);
		request.setAttribute("posting", p);
		request.getRequestDispatcher("jobseeker/postingDetails.jsp").forward(request, response);
	}

	private void gotoApply(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String id = request.getParameter("id");
		User userInSession = Utils.getLoginedUser(request.getSession());
		// check no duplicate applying
		UserPosting up = new UserPosting();
		up.setPosting_id(id);
		up.setUser(userInSession);
		try {
			List<UserPosting> list = userPostingDao.queryForMatching(up);
			if (list.size() > 0) {
				request.getRequestDispatcher("jobseeker/home_jobseeker.jsp").forward(request, response);
			} else {
				request.setAttribute("id", id);
				request.getRequestDispatcher("jobseeker/applyForJob.jsp").forward(request, response);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void createApplication(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String pid = request.getParameter("id");
		String candidateDetails = request.getParameter("candidateDetails");
		String coverLetter = request.getParameter("coverLetter");
		User userInSession = Utils.getLoginedUser(request.getSession());
		try {
			// assembly applications
			Application application = new Application();
			application.setCandidateDetails(candidateDetails);
			application.setCoverLetter(coverLetter);
			application.setJobId(pid);
			// pass to service
			Response serviceResponse = applicationsDao.createApplication(application);
			int httpStatus = serviceResponse.getStatus();
			if (201 != httpStatus) {
				request.setAttribute("errorCode", httpStatus);
				request.getRequestDispatcher("fail.jsp").forward(request, response);
			} else {
				String createdURL = serviceResponse.getLocation().toString();
				String createdId = createdURL.substring(createdURL.lastIndexOf('/') + 1, createdURL.length());
				// insert user-application into local db
				UserApplication ua = new UserApplication();
				ua.setId(UUID.randomUUID().toString());
				ua.setApplication_id(createdId);
				ua.setUser(userInSession);
				userApplicationDao.create(ua);
				// insert user-posting into local db
				UserPosting up = new UserPosting();
				up.setId(UUID.randomUUID().toString());
				up.setPosting_id(pid);
				up.setUser(userInSession);
				userPostingDao.create(up);
				request.getRequestDispatcher("jobseeker?method=gotoManageApplication&archived=0").forward(request,
						response);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	private void gotoManageApplication(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		User userInSession = Utils.getLoginedUser(request.getSession());
		String archived = request.getParameter("archived");
		try {
			// get this user's active/archived application ids
			Map<String, Object> queryParas = new HashMap<>();
			queryParas.put("user_id", userInSession);
			queryParas.put("archived", archived);
			List<UserApplication> userAppList = userApplicationDao.queryForFieldValues(queryParas);
			// get applications from job services
			List<Application> applications = new ArrayList<>();
			for (Iterator iterator = userAppList.iterator(); iterator.hasNext();) {
				UserApplication userApplication = (UserApplication) iterator.next();
				String aid = userApplication.getApplication_id();
				Application a = applicationsDao.findApplicationById(aid);
				applications.add(a);
			}
			// change jobid to meaningful posting info
			for (Iterator<Application> iterator = applications.iterator(); iterator.hasNext();) {
				Application application = (Application) iterator.next();
				String pid = application.getJobId();
				Posting p = postingsDao.findPostingById(pid);
				application.setJobId(p.getCompanyName() + "," + p.getPositionType() + "," + p.getLocation() + ","
						+ p.getDescriptions());
			}
			request.setAttribute("list", applications);
			if ("0".equals(archived)) {
				request.getRequestDispatcher("jobseeker/manageApplication.jsp").forward(request, response);
			} else {
				request.getRequestDispatcher("jobseeker/manageArchived.jsp").forward(request, response);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void gotoUpdateApplication(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String aid = request.getParameter("aid");
		Application app = applicationsDao.findApplicationById(aid);
		request.setAttribute("application", app);
		request.getRequestDispatcher("jobseeker/updateApplication.jsp").forward(request, response);
	}

	private void updateApplication(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String aid = request.getParameter("id");
		String candidateDetails = request.getParameter("candidateDetails");
		String coverLetter = request.getParameter("coverLetter");
		// assembly applications
		Application application = new Application();
		application.setCandidateDetails(candidateDetails);
		application.setCoverLetter(coverLetter);
		// pass to service
		Response serviceResponse = applicationsDao.updateApplication(aid, application);
		int httpStatus = serviceResponse.getStatus();
		if (204 != httpStatus) {
			request.setAttribute("errorCode", httpStatus);
			request.getRequestDispatcher("fail.jsp").forward(request, response);
		} else {
			request.getRequestDispatcher("jobseeker?method=gotoManageApplication&archived=0").forward(request,
					response);
		}

	}

	private void archive(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String aid = request.getParameter("id");
		User userInSession = Utils.getLoginedUser(request.getSession());
		UserApplication ua = new UserApplication();
		ua.setUser(userInSession);
		ua.setApplication_id(aid);
		try {
			List<UserApplication> result = userApplicationDao.queryForMatching(ua);
			UserApplication resultUa = result.get(0);
			resultUa.setArchived(1);
			userApplicationDao.update(resultUa);
			request.getRequestDispatcher("jobseeker?method=gotoManageApplication&archived=1").forward(request,
					response);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
