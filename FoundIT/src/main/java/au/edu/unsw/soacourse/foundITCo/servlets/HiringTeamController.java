package au.edu.unsw.soacourse.foundITCo.servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
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
import au.edu.unsw.soacourse.foundITCo.PostingStatus;
import au.edu.unsw.soacourse.foundITCo.Utils;
import au.edu.unsw.soacourse.foundITCo.beans.Application;
import au.edu.unsw.soacourse.foundITCo.beans.ApplicationReviewer;
import au.edu.unsw.soacourse.foundITCo.beans.ApplicationStatus;
import au.edu.unsw.soacourse.foundITCo.beans.Keys;
import au.edu.unsw.soacourse.foundITCo.beans.Posting;
import au.edu.unsw.soacourse.foundITCo.beans.Review;
import au.edu.unsw.soacourse.foundITCo.beans.User;
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
		
		if (request.getQueryString().equalsIgnoreCase("applications")) {
			
			List<Application> applications = new ArrayList<Application>();
			PostingsDao postingsDao = new PostingsDao(Keys.SHORT_VAL_REVIEWER);
			
			try {
				
				List<ApplicationReviewer> applicationReviews = DBUtil.getApplicationReviewerDao().queryBuilder().where().eq(ApplicationReviewer.USER_ID, user.getEmail()).query();
				List<UserProfile> userProfiles = new ArrayList<>();
				List<Posting> postings = new ArrayList<Posting>();
				List<String> appIds = new ArrayList<String>();
	
				if (applicationReviews.size() > 0) {
					
					for (Iterator<ApplicationReviewer> applicationReview = applicationReviews.iterator(); 
							applicationReview.hasNext();) {
						ApplicationReviewer applicationReviewer = (ApplicationReviewer) applicationReview.next();
						appIds.add(applicationReviewer.getUserApplication().getApplicationId());
						
						List<UserProfile> userProfile = DBUtil.getUserProfileDao().queryBuilder().where().
								eq(UserProfile.USER_ID, applicationReviewer.getUserApplication().getUser().getEmail()).query();
						if (userProfile.size() > 0)
							userProfiles.add(userProfile.get(0));
						else {
							userProfiles.add(null);
						}
					}
					
					applications = ApplicationDao.findApplicationsById(baseUri, Keys.SHORT_VAL_REVIEWER, appIds);
					
					List<String> jobIds = new ArrayList<String>();
					for(int i = 0; i < applications.size(); i++) {
						jobIds.add(applications.get(i).getJobId());
					}
					
					postings = postingsDao.findPostingById(jobIds);
				}
				
				request.setAttribute("postings", postings);
				request.setAttribute("userProfiles", userProfiles);
				request.setAttribute("appIds", appIds);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
			}

			RequestDispatcher dispatcher = request.getRequestDispatcher("hiringteam/candidate_list.jsp");
			dispatcher.forward(request, response);
		} else if (!request.getParameter("review").isEmpty() && request.getParameter("review") != null) {
			List<UserProfile> userProfiles = new ArrayList<UserProfile>();
			try {
				userProfiles = DBUtil.getUserProfileDao().queryBuilder().where().eq(UserProfile.USER_ID, request.getParameter("review")).query();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (userProfiles.size() > 0)
				request.setAttribute("userProfile", userProfiles.get(0));
			
			List<String> appIds = new ArrayList<String>();
			appIds.add(request.getParameter("appId"));
			List<Application> applications = new ArrayList<Application>();
			applications = ApplicationDao.findApplicationsById(baseUri, Keys.SHORT_VAL_REVIEWER, appIds);
			
			request.setAttribute("application", applications.get(0));
			request.setAttribute("appId", request.getParameter("appId"));
			
			List<UserReview> userReviews = new ArrayList<UserReview>();
			
			try {
				userReviews = DBUtil.getUserReviewDao().queryBuilder().where().eq(UserReview.USER_ID, user).query();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			List<Review> reviews = new ArrayList<Review>();
			if (userReviews.size() > 0){
				List<String> ids = new ArrayList<String>();
				ids.add(userReviews.get(0).getReview_id());
				reviews = ReviewDao.findReviewById(baseUri, Keys.SHORT_VAL_REVIEWER, ids);
			}
			
			if (reviews.size() > 0)
				request.setAttribute("review", reviews.get(0));
			
			RequestDispatcher dispatcher = request.getRequestDispatcher("hiringteam/review.jsp");
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
		
		if (request.getQueryString().equalsIgnoreCase("review")) {
			Review review = new Review();
			review.setComments(request.getParameter("comments"));
			review.setDecision(request.getParameter("decision"));
			review.setReviewerDetails(request.getParameter("details"));
			review.setAppId(request.getParameter("appId"));
			// post to job services
			Response serviceResponse = ReviewDao.createReview(baseUri, Keys.SHORT_VAL_REVIEWER, review);
			// deal with response
			int httpStatus = serviceResponse.getStatus();
			if (201 != httpStatus) {
				request.setAttribute("errorCode", httpStatus);
				request.getRequestDispatcher("hiringteam/fail.jsp").forward(request, response);
			} else {
				
				String createdURL = serviceResponse.getLocation().toString();
				String createdId = createdURL.substring(createdURL.lastIndexOf('/') + 1, createdURL.length());
				
				serviceResponse = ApplicationDao.updateStatus(baseUri, createdId, review.getAppId(), String.valueOf(ApplicationStatus.IN_REVIEW));
					
				if (201 != httpStatus) {
					request.setAttribute("errorCode", httpStatus);
					request.getRequestDispatcher("hiringteam/fail.jsp").forward(request, response);
				}
				
				User user = Utils.getLoginedUser(request.getSession());
				String jobId = request.getParameter("jobId");
				
				List<Application> applications = ApplicationDao.findApplicationsByPostingId(baseUri, Keys.SHORT_VAL_REVIEWER, jobId);
			
				boolean changeStatus = true;
				for (Application application : applications) {
					if (!application.getStatus().equals("1")) {
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
				
				response.sendRedirect("hiringteam?applications");
			}
		}
	}

}
