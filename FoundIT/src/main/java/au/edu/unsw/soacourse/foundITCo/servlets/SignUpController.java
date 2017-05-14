package au.edu.unsw.soacourse.foundITCo.servlets;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import au.edu.unsw.soacourse.foundITCo.DBUtil;
import au.edu.unsw.soacourse.foundITCo.beans.User;

/**
 * Servlet implementation class SignUpServlet
 */
@WebServlet("/signup")
public class SignUpController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SignUpController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		RequestDispatcher dipatcher = getServletContext().getRequestDispatcher("/signup.jsp");
		dipatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html;charset=UTF-8");

		User newUser = new User();
		newUser.setEmail(request.getParameter("email"));
		newUser.setPassword(request.getParameter("password"));
		newUser.setUserType(request.getParameter("userType"));
		newUser.setValid(true);

		try {
			int success = DBUtil.getUserDao().create(newUser);
			
			if (success > 0) {
				response.sendRedirect("success.jsp");
			} else {
				RequestDispatcher dispatcher = request.getRequestDispatcher("error_signup.jsp");
				dispatcher.forward(request, response);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			RequestDispatcher dispatcher = request.getRequestDispatcher("error_signup.jsp");
			dispatcher.forward(request, response);
		}

		DBUtil.closeConnection();
	}

}
