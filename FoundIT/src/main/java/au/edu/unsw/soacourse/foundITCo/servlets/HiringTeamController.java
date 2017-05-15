package au.edu.unsw.soacourse.foundITCo.servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;

import au.edu.unsw.soacourse.foundITCo.DBUtil;
import au.edu.unsw.soacourse.foundITCo.Utils;
import au.edu.unsw.soacourse.foundITCo.beans.Application;
import au.edu.unsw.soacourse.foundITCo.Keys;
import au.edu.unsw.soacourse.foundITCo.beans.Posting;
import au.edu.unsw.soacourse.foundITCo.beans.Review;
import au.edu.unsw.soacourse.foundITCo.beans.User;
import au.edu.unsw.soacourse.foundITCo.beans.UserApplication;
import au.edu.unsw.soacourse.foundITCo.beans.UserPosting;
import au.edu.unsw.soacourse.foundITCo.beans.UserProfile;
import au.edu.unsw.soacourse.foundITCo.beans.UserReview;
import au.edu.unsw.soacourse.foundITCo.dao.ApplicationDao;
import au.edu.unsw.soacourse.foundITCo.dao.PostingsDao;
import au.edu.unsw.soacourse.foundITCo.dao.ReviewDao;

/**
 * Servlet implementation class HiringTeamController
 */
@WebServlet("/hiringteam")
public class HiringTeamController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HiringTeamController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		User user = Utils.getLoginedUser(session);
		
		String baseUri = request.getScheme() + "://" +   // "http" + "://
	             request.getServerName() +       // "myhost"
	             ":" +                           // ":"
	             request.getServerPort();
		
		if (user == null) {
			try {
				user = DBUtil.getUserDao().queryForId(Utils.getUserNameInCookie(request));
				Utils.storeLoginedUser(session, user);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if (request.getQueryString().split("=")[0].equalsIgnoreCase("applicants")) {
			
			List<Application> applications = new ArrayList<Application>();
			String jobId = request.getParameter("applicants");
			applications = ApplicationDao.findApplicationsByPostingId(baseUri, Keys.SHORT_VAL_REVIEWER, jobId);
			
			try {
				List<UserProfile> userProfiles = new ArrayList<>();
				List<String> appIds = new ArrayList<String>();
				for (Application application: applications) {
					List<UserApplication> userApplication = DBUtil.getUserApplicationDao().queryForEq("application_id", application.getAppId());
					List<UserProfile> userProfile = DBUtil.getUserProfileDao().queryBuilder().where().
							eq(UserProfile.USER_ID, userApplication.get(0).getUser().getEmail()).query();
					if (userProfile.size() > 0)
						userProfiles.add(userProfile.get(0));
					else {
						userProfiles.add(null);
					}
					appIds.add(application.getAppId());
				}
				
				for(Application application: applications) {
					Utils.trasnfromApplicationStatus(application);
				}
				
				request.setAttribute("applications", applications);
				request.setAttribute("userProfiles", userProfiles);
				request.setAttribute("appIds", appIds);
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
			}

			RequestDispatcher dispatcher = request.getRequestDispatcher("hiringteam/applicants.jsp");
			dispatcher.forward(request, response);
			
		} else if (request.getQueryString().split("=")[0].equalsIgnoreCase("review")) {
			List<UserProfile> userProfiles = new ArrayList<UserProfile>();
			try {
				userProfiles = DBUtil.getUserProfileDao().queryBuilder().where().eq(UserProfile.USER_ID, request.getParameter("review")).query();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (userProfiles.size() > 0)
				request.setAttribute("userProfile", userProfiles.get(0));
			
			Application application = ApplicationDao.findApplicationById(baseUri, Keys.SHORT_VAL_REVIEWER, request.getParameter("appId"));
			
			request.setAttribute("application", application);
			request.setAttribute("appId", request.getParameter("appId"));
			
			List<UserReview> userReviews = new ArrayList<UserReview>();
			
			try {
				userReviews = DBUtil.getUserReviewDao().queryBuilder().where().eq(UserReview.USER_ID, user).query();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Review review = new Review();
			if (userReviews.size() > 0){
				review = ReviewDao.findReviewById(baseUri, Keys.SHORT_VAL_REVIEWER, userReviews.get(0).getReview_id());
			}
			
			request.setAttribute("review", review);
			
			RequestDispatcher dispatcher = request.getRequestDispatcher("hiringteam/review.jsp");
			dispatcher.forward(request, response);
		} else if (request.getQueryString().equalsIgnoreCase("postings")) {
			List<UserPosting> userPostings = new ArrayList<UserPosting>();
			try {
				userPostings = DBUtil.getUserPostingDao().queryBuilder().where().eq(UserPosting.USER_ID, user.getEmail()).query();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			PostingsDao postingsDao = new PostingsDao(Keys.SHORT_VAL_REVIEWER);
			List<Posting> postings = new ArrayList<Posting>();
			for (UserPosting userPosting: userPostings) {
				postings.add(postingsDao.findPostingById(userPosting.getPosting_id()));
			}
			
			request.setAttribute("postings", postings);

			RequestDispatcher dispatcher = request.getRequestDispatcher("hiringteam/postings.jsp");
			dispatcher.forward(request, response);
		}
		
		DBUtil.closeConnection();
		
	}
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String baseUri = request.getScheme() + "://" +   // "http" + "://
	             request.getServerName() +       // "myhost"
	             ":" +                           // ":"
	             request.getServerPort();

		String jobId = request.getParameter("jobId");
		String appId = request.getParameter("appId");
		
		if (request.getQueryString().equalsIgnoreCase("review")) {
			Review review = new Review();
			review.setComments(request.getParameter("comments"));
			review.setDecision(request.getParameter("decision"));
			review.setReviewerDetails(request.getParameter("details"));
			review.setAppId(appId);
			// post to job services
			Response serviceResponse = null;
			if (request.getParameter("reviewId") == null || request.getParameter("reviewId").isEmpty())
				serviceResponse = ReviewDao.createReview(baseUri, Keys.SHORT_VAL_REVIEWER, review);
			else 
				serviceResponse = ReviewDao.updateReview(baseUri, Keys.SHORT_VAL_REVIEWER, 
						request.getParameter("reviewId"), review);
			// deal with response
			int httpStatus = serviceResponse.getStatus();
			if (201 == httpStatus) {
				String createdURL = serviceResponse.getLocation().toString();
				String createdId = createdURL.substring(createdURL.lastIndexOf('/') + 1, createdURL.length());
				
				serviceResponse = ApplicationDao.updateStatus(baseUri, createdId, review.getAppId(), "in_review");
//					
//				if (201 != httpStatus) {
//					request.setAttribute("errorCode", httpStatus);
//					request.getRequestDispatcher("hiringteam/fail.jsp").forward(request, response);
//				}
				
				List<UserPosting> userPostings = new ArrayList<UserPosting>();
				try {
					userPostings = DBUtil.getUserPostingDao().queryBuilder().where().eq(UserPosting.POSTING_id, jobId).query();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				int reviewersCount = userPostings.size() - 1;
				
				List<Review> reviews = ReviewDao.findReviewsByAppId(baseUri, Keys.SHORT_VAL_REVIEWER, appId);
				
				if (reviews.size() == reviewersCount) {
					boolean recommend = true;
					for (Review rev: reviews) {
						if (rev.getDecision().equals("0")) {
							recommend = false;
							break;
						}
					}
					
					if (recommend)
						ApplicationDao.updateStatus(baseUri, Keys.SHORT_VAL_REVIEWER, appId, "accepted");
					else
						ApplicationDao.updateStatus(baseUri, Keys.SHORT_VAL_REVIEWER, appId, "rejected");
				}
					
				User user = Utils.getLoginedUser(request.getSession());
				
				List<Application> applications = ApplicationDao.findApplicationsByPostingId(baseUri, Keys.SHORT_VAL_REVIEWER, jobId);
			
				boolean changeStatus = true;
				for (Application application : applications) {
					if (Integer.parseInt(application.getStatus()) < 2) {
						changeStatus = false;
						break;
					}
	
				}
				
				if (changeStatus) {
					PostingsDao postingsDao = new PostingsDao(Keys.SHORT_VAL_REVIEWER);
					postingsDao.updateStatus(jobId, "processed");
				}
				
				UserReview userReview = new UserReview();
				userReview.setUser(user);
				userReview.setReview_id(createdId);
				
				try {
					DBUtil.getUserReviewDao().create(userReview);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				response.sendRedirect("hiringteam?applicants=" + request.getParameter("jobId"));
			} else if (204 == httpStatus) {
				List<UserPosting> userPostings = new ArrayList<UserPosting>();
				try {
					userPostings = DBUtil.getUserPostingDao().queryBuilder().where().eq(UserPosting.POSTING_id, jobId).query();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				int reviewersCount = userPostings.size() - 1;
				
				List<Review> reviews = ReviewDao.findReviewsByAppId(baseUri, Keys.SHORT_VAL_REVIEWER, appId);
				
				if (reviews.size() == reviewersCount) {
					boolean recommend = true;
					for (Review rev: reviews) {
						if (rev.getDecision().equals("0")) {
							recommend = false;
							break;
						}
					}
					
					if (recommend)
						ApplicationDao.updateStatus(baseUri, Keys.SHORT_VAL_REVIEWER, appId, "accpeted");
					else
						ApplicationDao.updateStatus(baseUri, Keys.SHORT_VAL_REVIEWER, appId, "rejected");
				}
				
				List<Application> applications = ApplicationDao.findApplicationsByPostingId(baseUri, Keys.SHORT_VAL_REVIEWER, jobId);
			
				boolean changeStatus = true;
				for (Application application : applications) {
					if (Integer.parseInt(application.getStatus()) < 2) {
						changeStatus = false;
						break;
					}
	
				}
				
				if (changeStatus) {
					PostingsDao postingsDao = new PostingsDao(Keys.SHORT_VAL_REVIEWER);
					postingsDao.updateStatus(jobId, "processed");
				}
				
				response.sendRedirect("hiringteam?applicants=" + request.getParameter("jobId"));
			}else {
				request.setAttribute("errorCode", httpStatus);
				request.getRequestDispatcher("hiringteam/fail.jsp").forward(request, response);
			}
		}
	}

}
