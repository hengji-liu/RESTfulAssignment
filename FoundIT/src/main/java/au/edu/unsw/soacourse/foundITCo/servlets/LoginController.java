package au.edu.unsw.soacourse.foundITCo.servlets;

import java.io.IOException;

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

/**
 * Servlet implementation class SignOnServlet
 */
@WebServlet({ "/signin", "/signout" })
public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		String requestPath = request.getRequestURI().substring(request.getContextPath().length());
		RequestDispatcher dipatcher;
		
		if (!requestPath.equals("/signout")) {
			dipatcher = getServletContext().getRequestDispatcher("/signin.jsp");
		} else {
			HttpSession session = request.getSession();
			User logUser = Utils.getLoginedUser(session);
			
			if (logUser != null) {
				Utils.deleteLoginedUser(session);
			}
			
			Utils.deleteUserCookie(response);
			
			dipatcher = getServletContext().getRequestDispatcher("/index.jsp");
		}
		
		dipatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {

			User user = new User();
			user.setEmail(request.getParameter("email"));
			user.setPassword(request.getParameter("password"));
			
			RequestDispatcher dispatcher;

			user = DBUtil.getUserDao().queryForSameId(user);


			if (user != null && user.isValid()) {
				HttpSession session = request.getSession(true);
				Utils.storeLoginedUser(session, user);
				Utils.storeUserCookie(response, user);
				if (user.getUserType().equals("manager"))
					dispatcher = getServletContext().getRequestDispatcher("/manager/home_manager.jsp");
				else if (user.getUserType().equals("hiringteam"))
					dispatcher = getServletContext().getRequestDispatcher("/hiringteam/home_hiringteam.jsp");
				else 
					dispatcher = getServletContext().getRequestDispatcher("/jobseeker/home_jobseeker.jsp");
			}
			else {
				dispatcher = getServletContext().getRequestDispatcher("/error.jsp");
			}
			
			dispatcher.forward(request, response);
		}

		catch (Throwable theException) {
			System.out.println(theException);
		}

		DBUtil.closeConnection();
	}

}
