package au.edu.unsw.soacourse.foundITCo.servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import au.edu.unsw.soacourse.foundITCo.DBUtil;
import au.edu.unsw.soacourse.foundITCo.Utils;
import au.edu.unsw.soacourse.foundITCo.beans.Application;
import au.edu.unsw.soacourse.foundITCo.beans.ApplicationReviewer;
import au.edu.unsw.soacourse.foundITCo.beans.Posting;
import au.edu.unsw.soacourse.foundITCo.beans.User;
import au.edu.unsw.soacourse.foundITCo.beans.UserProfile;
import au.edu.unsw.soacourse.foundITCo.dao.ApplicationDao;

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
		if (request.getQueryString().equalsIgnoreCase("applications")) {
			HttpSession session = request.getSession();
			User user = Utils.getLoginedUser(session);
			
			if (user == null) {
				try {
					user = DBUtil.getUserDao().queryForId(Utils.getUserNameInCookie(request));
					Utils.storeLoginedUser(session, user);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			List<Application> applications = null;
			request.setAttribute("applications", null);
			
			String baseUri = request.getScheme() + "://" +   // "http" + "://
		             request.getServerName() +       // "myhost"
		             ":" +                           // ":"
		             request.getServerPort();
			
			try {
				List<ApplicationReviewer> applicationReviews = DBUtil.getApplicationReviewerDao().queryBuilder().where().eq(ApplicationReviewer.USER_ID, user.getEmail()).query();
				if (applicationReviews.size() > 0) {
					List<String> ids = new ArrayList<String>();
					for (Iterator<ApplicationReviewer> applicationReview = applicationReviews.iterator(); applicationReview
							.hasNext();) {
						ApplicationReviewer applicationReviewer = (ApplicationReviewer) applicationReview
								.next();
						ids.add(applicationReviewer.getUserApplication().getApplication_id());
					}
					applications = ApplicationDao.findApplicationsById(baseUri, "app-reviewer", ids);
				}
				request.setAttribute("applications", applications);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
			}
			
			DBUtil.closeConnection();

			RequestDispatcher dispatcher = request.getRequestDispatcher("/profile.jsp");
			dispatcher.forward(request, response);
		}
	}
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
