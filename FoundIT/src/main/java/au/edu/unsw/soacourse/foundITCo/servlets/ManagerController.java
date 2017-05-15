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

import au.edu.unsw.soacourse.foundITCo.ApplicationStatus;
import au.edu.unsw.soacourse.foundITCo.DBUtil;
import au.edu.unsw.soacourse.foundITCo.Keys;
import au.edu.unsw.soacourse.foundITCo.Utils;
import au.edu.unsw.soacourse.foundITCo.beans.Application;
import au.edu.unsw.soacourse.foundITCo.beans.Poll;
import au.edu.unsw.soacourse.foundITCo.beans.Posting;
import au.edu.unsw.soacourse.foundITCo.beans.Review;
import au.edu.unsw.soacourse.foundITCo.beans.User;
import au.edu.unsw.soacourse.foundITCo.beans.UserApplication;
import au.edu.unsw.soacourse.foundITCo.beans.UserPosting;
import au.edu.unsw.soacourse.foundITCo.beans.Vote;
import au.edu.unsw.soacourse.foundITCo.dao.ApplicationsDao;
import au.edu.unsw.soacourse.foundITCo.dao.PollsDao;
import au.edu.unsw.soacourse.foundITCo.dao.PostingsDao;
import au.edu.unsw.soacourse.foundITCo.dao.ReviewsDao;

@WebServlet("/manager")
public class ManagerController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Dao<UserPosting, String> userPostingDao = DBUtil.getUserPostingDao();
	private Dao<User, String> userDao = DBUtil.getUserDao();
	private Dao<UserApplication, String> userApplicationDao = DBUtil.getUserApplicationDao();
	private PostingsDao postingsDao = new PostingsDao(Keys.SHORT_VAL_MANAGER);
	private ApplicationsDao applicationsDao = new ApplicationsDao(Keys.SHORT_VAL_MANAGER);
	private ReviewsDao reviewsDao = new ReviewsDao(Keys.SHORT_VAL_MANAGER);
	private PollsDao pollsDao = new PollsDao(Keys.SHORT_VAL_MANAGER);

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
			for (Iterator iterator = ups.iterator(); iterator.hasNext();) {
				UserPosting up = (UserPosting) iterator.next();
				Posting p = postingsDao.findPostingById(up.getPosting_id());
				list.add(p);
			}
		}
		request.setAttribute("list", list);
		request.setAttribute("archived", archived);
		request.getRequestDispatcher("manager/managePosting.jsp").forward(request, response);
	}

	private void gotoPostingDetails(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String pid = request.getParameter("pid");
		Posting posting = postingsDao.findPostingById(pid);
		List<Application> applications = applicationsDao.findApplicationByPostingId(pid);
		for (Iterator iterator = applications.iterator(); iterator.hasNext();) {
			Application application = (Application) iterator.next();
			Utils.trasnfromApplicationStatus(application);
		}
		switch (posting.getStatus()) {
		case "Open":
			// list reviewers for assginment
			List<User> reviewers = null;
			try {
				reviewers = userDao.queryForEq("userType", "hiringteam");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			request.setAttribute("reviewers", reviewers);
			break;
		case "Processed":
			// change jobid to meaningful review info
			for (Iterator<Application> iterator = applications.iterator(); iterator.hasNext();) {
				Application application = (Application) iterator.next();
				String aid = application.getAppId();
				List<Review> reviews = reviewsDao.findReviewByAppId(aid);
				Utils.trasnfromReviewDecision(reviews.get(0));
				Utils.trasnfromReviewDecision(reviews.get(1));
				application.setJobId(reviews.get(1).getDecision() + ": " + reviews.get(0).getComments() + "<br/>"
						+ reviews.get(1).getDecision() + ": " + reviews.get(1).getComments());
			}
			break;
		case "Sent Invitations":
			// get change jobid to meaningful chosen interview time
			for (Iterator<Application> iterator = applications.iterator(); iterator.hasNext();) {
				Application application = (Application) iterator.next();
				String aid = application.getAppId();
				List<Review> reviews = reviewsDao.findReviewByAppId(aid);
				Utils.trasnfromReviewDecision(reviews.get(0));
				Utils.trasnfromReviewDecision(reviews.get(1));
				String reviewStr = reviews.get(1).getDecision() + ": " + reviews.get(0).getComments() + "<br/>"
						+ reviews.get(1).getDecision() + ": " + reviews.get(1).getComments();
				if ("Accepted".equals(application.getStatus())) {
					try {
						List<UserApplication> ua = userApplicationDao.queryForEq("application_id",
								application.getAppId());
						String pollId = ua.get(0).getPoll_id();
						Poll poll = pollsDao.findPollById(pollId);
						List<Vote> votes = poll.getVotes();
						if (votes.size() > 0) { // if voted
							reviewStr += "<br/>" + votes.get(0).getChosenOption()
									+ " is the applicant's chosen interview time.";
						} else {
							reviewStr += "<br/>This applicant has not responded to the interview time poll.";
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				application.setJobId(reviewStr);
			}
		default:
			break;
		}
		int size = applications.size();
		request.setAttribute("size", size); // decides if can be updated

		request.setAttribute("applications", applications);
		request.setAttribute("posting", posting);
		request.getRequestDispatcher("manager/postingDetails.jsp").forward(request, response);
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
			request.getRequestDispatcher("fail.jsp").forward(request, response);
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

	private void updatePosting(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// get paras
		String pid = request.getParameter("pid");
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
		Response serviceResponse = postingsDao.updatePosting(pid, posting);
		// deal with response
		int httpStatus = serviceResponse.getStatus();
		if (204 != httpStatus) {
			request.setAttribute("errorCode", httpStatus);
			request.getRequestDispatcher("fail.jsp").forward(request, response);
		} else {
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
			request.getRequestDispatcher("fail.jsp").forward(request, response);
		} else {
			switch (newStatus) {
			case "in_review":
				// save userPosting relationship for reviewers
				String[] selectedReviewers = request.getParameterValues("selectedReviewers");
				for (int j = 0; j < selectedReviewers.length; j++) {
					UserPosting up = new UserPosting();
					up.setId(UUID.randomUUID().toString());
					try {
						User reviewer = userDao.queryForId(selectedReviewers[j]);
						up.setUser(reviewer);
					} catch (SQLException e) {
						e.printStackTrace();
					}
					up.setPosting_id(pid);
					try {
						userPostingDao.create(up);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				// change application status to in_review
				List<Application> applications = applicationsDao.findApplicationByPostingId(pid);
				for (Iterator iterator = applications.iterator(); iterator.hasNext();) {
					Application application = (Application) iterator.next();
					applicationsDao.updateStatus(application.getAppId(), newStatus);
				}
				break;
			case "sent_invitations":
				String option1 = (String) request.getParameter("option1");
				String option2 = (String) request.getParameter("option2");
				String option3 = (String) request.getParameter("option3");
				String optionsSepBySemicolon = option1 + ";" + option2 + ";" + option3;
				List<Application> apps = applicationsDao.findApplicationByPostingId(pid);
				for (Iterator iterator = apps.iterator(); iterator.hasNext();) {
					Application application = (Application) iterator.next();
					if (application.getStatus().equals(String.valueOf(ApplicationStatus.ACCEPTED))) {
						// post to poll services
						Response pollResponse = pollsDao.createPoll("interview time", "choose one time suits you",
								"DATE", optionsSepBySemicolon, null, null);
						String createdURL = pollResponse.getLocation().getPath();
						String createdId = createdURL.substring(createdURL.lastIndexOf('/') + 1, createdURL.length());
						// link poll_id with its application
						try {
							UserApplication ua = userApplicationDao.queryForEq("application_id", application.getAppId())
									.get(0);
							ua.setPoll_id(createdId);
							userApplicationDao.update(ua);
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}
			default:
				break;
			}
			request.getRequestDispatcher("manager?method=gotoManagePosting&archived=0").forward(request, response);
		}
	}

	private void archive(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String pid = request.getParameter("pid");
		User userInSession = Utils.getLoginedUser(request.getSession());
		UserPosting up = new UserPosting();
		up.setUser(userInSession);
		up.setPosting_id(pid);
		try {
			List<UserPosting> result = userPostingDao.queryForMatching(up);
			UserPosting resultUp = result.get(0);
			resultUp.setArchived(1);
			userPostingDao.update(resultUp);
			request.getRequestDispatcher("manager?method=gotoManagePosting&archived=1").forward(request, response);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
