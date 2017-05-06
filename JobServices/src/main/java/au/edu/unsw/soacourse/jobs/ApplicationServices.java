package au.edu.unsw.soacourse.jobs;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import au.edu.unsw.soacourse.jobs.dao.ApplicationDao;
import au.edu.unsw.soacourse.jobs.model.Application;
import au.edu.unsw.soacourse.jobs.model.ApplicationStatus;

public class ApplicationServices {
	private ApplicationDao dao = new ApplicationDao();

	@GET
	@Path("/application/{appId}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response get(@HeaderParam("accept") String type, @PathParam("appId") String appId) {
		// validation, appId should be an int
		try {
			Integer.parseInt(appId);
		} catch (NumberFormatException e) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		// validation media type
		if (type.equals(MediaType.WILDCARD) //
				|| type.equals(MediaType.APPLICATION_JSON) //
				|| type.equals(MediaType.APPLICATION_XML)) {
			// do nothing
		} else {// other type not supported
			return Response.status(Status.BAD_REQUEST).build();
		}
		// get item
		Application p = dao.findById(appId);
		if (null != p) {
			if (type.equals(MediaType.WILDCARD) || type.equals(MediaType.APPLICATION_JSON)) {
				return Response.status(Status.OK).entity(p).type(MediaType.APPLICATION_JSON).build();
			} else {
				return Response.status(Status.OK).entity(p).type(MediaType.APPLICATION_XML).build();
			}
		} else {// item not found
			return Response.status(Status.NOT_FOUND).build();
		}
	}

	@GET
	@Path("/applications") // applications?jobId=x
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAppByJob(@HeaderParam("accept") String type, @QueryParam("jobId") String jobId) {
		// validation media type
		if (type.equals(MediaType.WILDCARD) //
				|| type.equals(MediaType.APPLICATION_JSON)) {
			// do nothing
		} else {// other type not supported
			return Response.status(Status.BAD_REQUEST).build();
		}
		// if no query param, do find all
		if (null == jobId || "".equals(jobId)) {
			List<Application> list = dao.findAll();
			if (null != list) {
				return Response.status(Status.OK).entity(list).type(MediaType.APPLICATION_JSON).build();
			} else {
				return Response.status(Status.INTERNAL_SERVER_ERROR).build();
			}
		}
		// validation, jobId is an valid int
		if (null != jobId && !"".equals(jobId)) {
			try {
				Integer.parseInt(jobId);
			} catch (NumberFormatException e) {
				return Response.status(Status.BAD_REQUEST).build();
			}
		}
		// search on at least one param
		List<Application> list = dao.findByJobId(jobId);
		if (null != list) {
			return Response.status(Status.OK).entity(list).type(MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@POST
	@Path("/application")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response post(Application obj) {
		// validation, appId must be null or empty
		if (!(null == obj.getAppId() || "".equals(obj.getAppId())))
			return Response.status(Status.BAD_REQUEST).build();
		// validation, status must be null or empty
		if (!(null == obj.getStatus() || "".equals(obj.getStatus())))
			return Response.status(Status.BAD_REQUEST).build();
		// validation, jobId must not be null and be an int
		if (null == obj.getJobId())
			return Response.status(Status.BAD_REQUEST).build();
		try {
			Integer.parseInt(obj.getJobId());
		} catch (NumberFormatException e) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		// validation, other fields must not be null
		if (null == obj.getCandidateDetails()//
				|| null == obj.getCoverLetter()//
		) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		// insert
		obj.setStatus(String.valueOf(ApplicationStatus.RECEIVED));
		int insertedId = dao.insert(obj);
		if (0 != insertedId) {
			URI uri = null;
			try {
				uri = new URI("posting/" + Integer.toString(insertedId));
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			return Response.status(Status.CREATED).location(uri).build();
		}
		// insert fail
		return Response.status(Status.INTERNAL_SERVER_ERROR).build();
	}

	// TODO
	// @PUT
	// @Path("/application/{appId}")
	// @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	// public Response put(@PathParam("id") String id, Application obj) {
	// // validation, id should be an int
	// try {
	// Integer.parseInt(id);
	// } catch (NumberFormatException e) {
	// return Response.status(Status.BAD_REQUEST).build();
	// }
	// // validation, jobId must be null or empty
	// if (!(null == obj.getJobId() || "".equals(obj.getJobId())))
	// return Response.status(Status.BAD_REQUEST).build();
	// // validation, has something to update
	// boolean hasUpdate = false;
	// hasUpdate |= (null != obj.getCompanyName());
	// hasUpdate |= (null != obj.getDescriptions());
	// hasUpdate |= (null != obj.getLocation());
	// hasUpdate |= (null != obj.getPositionType());
	// hasUpdate |= (null != obj.getSalaryRate());
	// hasUpdate |= (null != obj.getStatus());
	// if (!hasUpdate)
	// return Response.status(Status.BAD_REQUEST).build();
	// // check item exists
	// Application p = dao.findById(id);
	// if (null == p)
	// return Response.status(Status.NOT_FOUND).build();
	// // check new status, > current && < max
	// int newStatus = Integer.parseInt(obj.getStatus());
	// int oldStatus = Integer.parseInt(p.getStatus());
	// if (newStatus < oldStatus || newStatus > PostingStatus.SENT_INVITATIONS)
	// return Response.status(Status.FORBIDDEN).build();
	// // TODO check no application is associated with this posting, FORBIDDEN
	//
	// // update
	// obj.setJobId(id);
	// int affectedRowCount = dao.update(obj);
	// if (0 == affectedRowCount) { // update fail
	// return Response.status(Status.INTERNAL_SERVER_ERROR).build();
	// } else {
	// obj = dao.findById(id);
	// return Response.status(Status.NO_CONTENT).build();
	// }
	// }
}
