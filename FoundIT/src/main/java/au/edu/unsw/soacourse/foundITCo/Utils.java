package au.edu.unsw.soacourse.foundITCo;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import au.edu.unsw.soacourse.foundITCo.beans.Application;
import au.edu.unsw.soacourse.foundITCo.beans.Posting;
import au.edu.unsw.soacourse.foundITCo.beans.User;

public class Utils {
	public static final String ATT_NAME_CONNECTION = "ATTRIBUTE_FOR_CONNECTION";

	private static final String ATT_NAME_USER_NAME = "ATTRIBUTE_FOR_STORE_USER_NAME_IN_COOKIE";

	// Store user info in Session.
	public static void storeLoginedUser(HttpSession session, User loginedUser) {

		// On the JSP can access ${loginedUser}
		session.setAttribute("loginedUser", loginedUser);
	}

	// Get the user information stored in the session.
	public static User getLoginedUser(HttpSession session) {
		User loginedUser = (User) session.getAttribute("loginedUser");
		return loginedUser;
	}

	public static void deleteLoginedUser(HttpSession session) {
		session.removeAttribute("loginedUser");
	}

	// Store info in Cookie
	public static void storeUserCookie(HttpServletResponse response, User user) {
		System.out.println("Store user cookie");
		Cookie cookieUserName = new Cookie(ATT_NAME_USER_NAME, user.getEmail());

		// 1 day (Convert to seconds)
		cookieUserName.setMaxAge(24 * 60 * 60);
		response.addCookie(cookieUserName);
	}

	public static String getUserNameInCookie(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (ATT_NAME_USER_NAME.equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}

	// Delete cookie.
	public static void deleteUserCookie(HttpServletResponse response) {
		Cookie cookieUserName = new Cookie(ATT_NAME_USER_NAME, null);

		// 0 seconds (Expires immediately)
		cookieUserName.setMaxAge(0);
		response.addCookie(cookieUserName);
	}

	public static void trasnfromPostingStatus(Posting p) {
		switch (Integer.parseInt(p.getStatus())) {
		case PostingStatus.CREATED:
			p.setStatus("Created");
			break;
		case PostingStatus.OPEN:
			p.setStatus("Open");
			break;
		case PostingStatus.IN_REVIEW:
			p.setStatus("In review");
			break;
		case PostingStatus.PROCESSED:
			p.setStatus("Processed");
			break;
		case PostingStatus.SENT_INVITATIONS:
			p.setStatus("Sent Invitations");
			break;
		default:
			break;
		}
	}

	public static void trasnfromApplicationStatus(Application a) {
		switch (Integer.parseInt(a.getStatus())) {
		case ApplicationStatus.RECEIVED:
			a.setStatus("Received");
			break;
		case ApplicationStatus.IN_REVIEW:
			a.setStatus("In review");
			break;
		case ApplicationStatus.ACCEPTED:
			a.setStatus("Accepted");
			break;
		case ApplicationStatus.REJECTED:
			a.setStatus("Rejected");
			break;
		default:
			break;
		}
	}

}
