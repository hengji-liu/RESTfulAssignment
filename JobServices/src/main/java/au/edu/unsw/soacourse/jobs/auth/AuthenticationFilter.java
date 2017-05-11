package au.edu.unsw.soacourse.jobs.auth;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import au.edu.unsw.soacourse.jobs.dao.AuthDao;

@SecuredByKey
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {
	private AuthDao authDao = new AuthDao();

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		if (!requestContext.getHeaders().containsKey("Security-Key")) {
			requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
		} else {
			// get Security-Key header from the request
			String secKey = requestContext.getHeaderString("Security-Key");
			// check key belongs to a partner
			int tokenExist = authDao.verifyToken(secKey);
			if (0 == tokenExist)
				requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
		}

	}
}