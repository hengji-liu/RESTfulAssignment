package au.edu.unsw.soacourse.foundITCo.servlets;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;

import com.j256.ormlite.dao.Dao;

import au.edu.unsw.soacourse.foundITCo.DBUtil;
import au.edu.unsw.soacourse.foundITCo.Utils;
import au.edu.unsw.soacourse.foundITCo.Dao.ApplicationsDao;
import au.edu.unsw.soacourse.foundITCo.Dao.PostingsDao;
import au.edu.unsw.soacourse.foundITCo.beans.Application;
import au.edu.unsw.soacourse.foundITCo.beans.Keys;
import au.edu.unsw.soacourse.foundITCo.beans.Posting;
import au.edu.unsw.soacourse.foundITCo.beans.User;
import au.edu.unsw.soacourse.foundITCo.beans.UserApplication;
import au.edu.unsw.soacourse.foundITCo.beans.UserPosting;

@WebServlet("/jobseeker")
public class JobseekerController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Dao<UserPosting, String> userPostingDao = DBUtil.getUserPostingDao();
	private Dao<User, String> userDao = DBUtil.getUserDao();
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
			if (list.size()>0) {
				request.getRequestDispatcher("jobseeker/home_jobseeker.jsp").forward(request, response);
			}else{
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
				request.getRequestDispatcher("jobseeker?method=gotoManageApplication").forward(request, response);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	private void gotoManageApplication(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO
		request.getRequestDispatcher("jobseeker/manageApplication.jsp").forward(request, response);
	}

}
