package au.edu.unsw.soacourse.foundITCo.servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import au.edu.unsw.soacourse.foundITCo.DBUtil;
import au.edu.unsw.soacourse.foundITCo.Utils;
import au.edu.unsw.soacourse.foundITCo.beans.User;
import au.edu.unsw.soacourse.foundITCo.beans.UserProfile;

/**
 * Servlet implementation class UserController
 */
@WebServlet("/user")
public class UserController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		if (request.getQueryString().equalsIgnoreCase("profile")){
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
			
			List<UserProfile> userProfile;
			request.setAttribute("userProfile", null);
			
			try {
				userProfile = DBUtil.getUserProfileDao().queryBuilder().where().eq(UserProfile.USER_ID, user.getEmail()).query();
				if (userProfile.size() > 0) {
					request.setAttribute("userProfile", userProfile.get(0));
				}
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
		if (request.getQueryString().equalsIgnoreCase("update")){
			HttpSession session = request.getSession();
			User user = Utils.getLoginedUser(session);
			
			if (user == null) {
				try {
					user = DBUtil.getUserDao().queryForId(Utils.getUserNameInCookie(request));
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			UserProfile userProfile = new UserProfile();
			if (!request.getParameter("id").isEmpty())
				userProfile.setId(Integer.parseInt(request.getParameter("id")));
			userProfile.setFirstName(request.getParameter("firstName"));
			userProfile.setLastName(request.getParameter("lastName"));
			userProfile.setCurrentPosition(request.getParameter("currentPosition"));
			userProfile.setEducation(request.getParameter("education"));
			userProfile.setExperience(request.getParameter("experience"));
			userProfile.setProfessionalSkill(request.getParameter("professionalSkill"));
			userProfile.setUser(user);
			
			try {
				if (DBUtil.getUserProfileDao().createOrUpdate(userProfile) != null) {
					response.sendRedirect("user?profile");
				} 
			} catch (SQLException e) {
					RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
					dispatcher.forward(request, response);
				// TODO Auto-generated catch block
//				e.printStackTrace();
			}
			
			DBUtil.closeConnection();
			
		}
	}

}
