package au.edu.unsw.soacourse.foundITCo.filters;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import au.edu.unsw.soacourse.foundITCo.DBUtil;
import au.edu.unsw.soacourse.foundITCo.Utils;
import au.edu.unsw.soacourse.foundITCo.beans.User;

/**
 * Servlet Filter implementation class SessionFilter
 */
@WebFilter(urlPatterns = { "/manager", "/jobseeker", "/hiringteam" }, filterName = "sessionFilter")
public class SessionFilter implements Filter {
	private static final String HIRING_TEAM_REQUEST_URI = "/FoundITCo/hiringteam";
	private static final String JOBSEEKER_REQUEST_URI = "/FoundITCo/jobseeker";
	private static final String MANAGER_REQUEST_URI = "/FoundITCo/manager";

    /**
     * Default constructor. 
     */
    public SessionFilter() {
        // TODO Auto-generated constructor stub
     	 	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		HttpSession session = req.getSession();
		
		User userInSession = Utils.getLoginedUser(session);
		
		if (userInSession == null) {
			try {
				userInSession = DBUtil.getUserDao().queryForId(Utils.getUserNameInCookie(req));
				Utils.storeLoginedUser(session, userInSession);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
//	       String checked = (String) session.getAttribute("COOKIE_CHECKED");
//	       if (checked == null && conn != null) {
//	           String userName = MyUtils.getUserNameInCookie(req);
//	           try {
//	               UserAccount user = DBUtils.findUser(conn, userName);
//	               MyUtils.storeLoginedUser(session, user);
//	           } catch (SQLException e) {
//	               e.printStackTrace();
//	           }
//	    
//	           // Mark checked.
//	           session.setAttribute("COOKIE_CHECKED", "CHECKED");
//	       }

		if (userInSession != null) {
			session.setAttribute("COOKIE_CHECKED", "CHECKED");
			if (req.getRequestURI().equalsIgnoreCase(HIRING_TEAM_REQUEST_URI)) {
				if (userInSession.getUserType().equals("hiringteam")) {
					chain.doFilter(request, response);
				} else {
					res.sendError(HttpServletResponse.SC_NOT_FOUND);
				}
			} if (req.getRequestURI().equalsIgnoreCase(JOBSEEKER_REQUEST_URI)) {
				if (userInSession.getUserType().equals("jobseeker")) {
					chain.doFilter(request, response);
				} else {
					res.sendError(HttpServletResponse.SC_NOT_FOUND);
				}
			} else {
				chain.doFilter(request, response);
			}
			return;
		} 
		else {
			res.sendRedirect("signin.jsp");
			return;   
		}
	 
	}
	
    //basic validation of pages that do not require authentication
    private boolean needsAuthentication(String url) {
        String[] validNonAuthenticationUrls =
            { "Login.jsp", "Register.jsp" };
        for(String validUrl : validNonAuthenticationUrls) {
            if (url.endsWith(validUrl)) {
                return false;
            }
        }
        return true;
    }

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
