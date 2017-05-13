package au.edu.unsw.soacourse.jobs.auth;

import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import au.edu.unsw.soacourse.jobs.dao.AuthDao;

@RolesAllowed
@Provider
@Priority(Priorities.AUTHORIZATION)
public class AuthorisationFilter implements ContainerRequestFilter {
	private AuthDao authDao = new AuthDao();
	@Context
	private ResourceInfo resourceInfo;

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		// Class<?> resourceClass = resourceInfo.getResourceClass();
		// List<Roles> classRoles = extractRoles(resourceClass);

		if (!requestContext.getHeaders().containsKey("Short-Key")) {
			requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).build());
		} else {
			// get short key
			String key = requestContext.getHeaderString("Short-Key");
			// get the parameters in class or method annotations
			Method resourceMethod = resourceInfo.getResourceMethod();
			List<String> methondRoles = extractRoles(resourceMethod);
			// verify
			String role = authDao.getPartnerUserRoleGroup(key);
			if (null == role || !methondRoles.contains(role))
				requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).build());
		}
	}

	// Extract the roles from the annotated element
	private List<String> extractRoles(AnnotatedElement annotatedElement) {
		if (annotatedElement == null)
			return new ArrayList<String>();
		RolesAllowed skc = annotatedElement.getAnnotation(RolesAllowed.class);
		if (skc == null)
			return new ArrayList<String>();
		Roles[] allowedRoles = skc.value();
		String[] strings = new String[allowedRoles.length];
		for (int i = 0; i < strings.length; i++) {
			strings[i] = allowedRoles[i].toString();
		}
		return Arrays.asList(strings);
	}
}