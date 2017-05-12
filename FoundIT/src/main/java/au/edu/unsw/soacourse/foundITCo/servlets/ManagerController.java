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

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;

import com.j256.ormlite.dao.Dao;

import au.edu.unsw.soacourse.foundITCo.DBUtil;
import au.edu.unsw.soacourse.foundITCo.Utils;
import au.edu.unsw.soacourse.foundITCo.Dao.PostingsDao;
import au.edu.unsw.soacourse.foundITCo.beans.Keys;
import au.edu.unsw.soacourse.foundITCo.beans.Posting;
import au.edu.unsw.soacourse.foundITCo.beans.User;
import au.edu.unsw.soacourse.foundITCo.beans.UserPosting;

@WebServlet("/manager")
public class ManagerController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Dao<UserPosting, String> userPostingDao = DBUtil.getUserPostingDao();
	private Dao<User, String> userDao = DBUtil.getUserDao();
	private PostingsDao postingsDao = new PostingsDao(Keys.SHORT_VAL_MANAGER);

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

	private void gotoCreatePosting(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("manager/createPosting.jsp").forward(request, response);
	}

	private void gotoManagePosting(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		User userInSession = Utils.getLoginedUser(request.getSession());
		String archived = request.getParameter("archived");
		// get this user's active posting ids
		Map<String, Object> queryParas = new HashMap<>();
		queryParas.put("user_id", userInSession);
		queryParas.put("archived", archived);
		List<UserPosting> ups = null;
		try {
			ups = userPostingDao.queryForFieldValues(queryParas);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		List<Posting> list = new ArrayList<>();
		if (null != ups && 0 != ups.size()) {
			// gather ids
			List<String> ids = new ArrayList<>();
			for (Iterator iterator = ups.iterator(); iterator.hasNext();) {
				UserPosting up = (UserPosting) iterator.next();
				ids.add(up.getPosting_id());
			}
			// get postings from jobservices
			list = postingsDao.findPostingById(ids);
		}
		request.setAttribute("list", list);
		if ("0".equals(archived)) {
			request.getRequestDispatcher("manager/managePosting.jsp").forward(request, response);
		} else {
			request.getRequestDispatcher("manager/manageArchived.jsp").forward(request, response);
		}
	}

	private void gotoAssignReviewers(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		User userInSession = Utils.getLoginedUser(request.getSession());
		// get all reviewers
		List<User> reviewers = null;
		try {
			reviewers = userDao.queryForEq("userType", "hiringteam");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println(reviewers.size());
		request.setAttribute("reviewers", reviewers);
		request.getRequestDispatcher("manager/assignReviewers.jsp").forward(request, response);
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
		Response serviceResponse = postingsDao.createPosting(posting);
		// deal with response
		int httpStatus = serviceResponse.getStatus();
		if (201 != httpStatus) {
			request.setAttribute("errorCode", httpStatus);
			request.getRequestDispatcher("manager/fail.jsp").forward(request, response);
		} else {
			String createdURL = serviceResponse.getLocation().toString();
			String createdId = createdURL.substring(createdURL.lastIndexOf('/') + 1, createdURL.length());
			User userInSession = Utils.getLoginedUser(request.getSession());
			// insert into db
			UserPosting up = new UserPosting();
			up.setId(UUID.randomUUID().toString());
			up.setPosting_id(createdId);
			up.setUser(userInSession);
			up.setArchived(0);
			try {
				userPostingDao.create(up);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			request.getRequestDispatcher("manager?method=gotoManagePosting&archived=0").forward(request, response);
		}
	}

	private void changeStatus(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String pid = request.getParameter("pid");
		String newStatus = request.getParameter("newStatus");
		Response serviceResponse = postingsDao.updateStatus(pid, newStatus);
		int serviceStatus = serviceResponse.getStatus();
		if (204 != serviceStatus) {
			request.setAttribute("errorCode", serviceStatus);
			request.getRequestDispatcher("manager/fail.jsp").forward(request, response);
		} else {
			RequestDispatcher dispatcher = null;
			switch (newStatus) {
			case "in_review": // TODO
				dispatcher = request.getRequestDispatcher("manager?method=gotoAssignReviewers");
				break;
			case "sent_invitations": // TODO
				dispatcher = request.getRequestDispatcher("manager?method=gotoSetInterviewTime");
				break;
			default:
				dispatcher = request.getRequestDispatcher("manager?method=gotoManagePosting&archived=0");
				break;
			}
			dispatcher.forward(request, response);
		}
	}
}
