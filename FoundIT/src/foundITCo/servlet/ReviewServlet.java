package foundITCo.servlet;

import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ReviewServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void service(HttpServletRequest request, HttpServletResponse response) {
		String methodName = request.getParameter("method");
		if (methodName == null || methodName.isEmpty()) {
			throw new RuntimeException("no method");
		}

		// reflection
		Class<? extends ReviewServlet> c = this.getClass();
		Method method = null;
		try {
			method = c.getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
		} catch (Exception e) {
			throw new RuntimeException(methodName + "does not exist");
		}

		// call method
		try {
			method.invoke(this, request, response);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
