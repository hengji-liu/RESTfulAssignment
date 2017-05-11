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

import au.edu.unsw.soacourse.jobs.dao.ApplicationsDao;
import au.edu.unsw.soacourse.jobs.dao.PostingsDao;
import au.edu.unsw.soacourse.jobs.model.Application;
import au.edu.unsw.soacourse.jobs.model.ApplicationStatus;
import au.edu.unsw.soacourse.jobs.model.Posting;
import au.edu.unsw.soacourse.jobs.model.PostingStatus;

public class ApplicationServices {
	private ApplicationsDao aDao = new ApplicationsDao();
	private PostingsDao pDao = new PostingsDao();

	@GET
	@Path("/applications/{appId}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response get(@HeaderParam("accept") String type, @PathParam("appId") String appId) {
		// validation, appId should be an int
		try {
			Integer.parseInt(appId);
		} catch (NumberFormatException e) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		// validation media type
		if (!type.equals(MediaType.WILDCARD) //
				&& !type.equals(MediaType.APPLICATION_JSON) //
				&& !type.equals(MediaType.APPLICATION_XML)) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		// get item
		Application p = aDao.findById(appId);
		if (null != p) {
			if (type.equals(MediaType.WILDCARD))
				type = MediaType.APPLICATION_JSON; // default json
			return Response.status(Status.OK).entity(p).type(type).build();
		} else {// item not found
			return Response.status(Status.NOT_FOUND).build();
		}
	}

	@GET
	@Path("/applications")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllApps(@HeaderParam("accept") String type) {
		// validation media type
		if (!type.equals(MediaType.WILDCARD) //
				&& !type.equals(MediaType.APPLICATION_JSON)) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		List<Application> list = aDao.findAll();
		if (null != list) {
			return Response.status(Status.OK).entity(list).type(MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GET
	@Path("/postings/{jobId}/applications")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAppByJob(@HeaderParam("accept") String type, @QueryParam("jobId") String jobId) {
		// validation media type
		if (!type.equals(MediaType.WILDCARD) //
				&& !type.equals(MediaType.APPLICATION_JSON)) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		// validation, jobId is an valid int
		if (null != jobId && !"".equals(jobId)) {
			try {
				Integer.parseInt(jobId);
			} catch (NumberFormatException e) {
				return Response.status(Status.BAD_REQUEST).build();
			}
		}
		// find applications by jobId
		List<Application> list = aDao.findByJobId(jobId);
		if (null != list) {
			return Response.status(Status.OK).entity(list).type(MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@POST
	@Path("/applications")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response post(Application obj) {
		// validation, appId must be null or empty
		String appId = obj.getAppId();
		if (null != appId && !"".equals(appId))
			return Response.status(Status.BAD_REQUEST).build();
		// validation, status must be null or empty
		String status = obj.getStatus();
		if (null != status && !"".equals(status))
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
		// check, posting status is open
		Posting p = pDao.findById(obj.getJobId());
		if (PostingStatus.OPEN != Integer.parseInt(p.getStatus()))
			return Response.status(Status.FORBIDDEN).build();
		// insert
		obj.setStatus(String.valueOf(ApplicationStatus.RECEIVED));
		int insertedId = aDao.insert(obj);
		if (0 != insertedId) {
			URI uri = null;
			try {
				uri = new URI("application/" + Integer.toString(insertedId));
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			return Response.status(Status.CREATED).location(uri).build();
		}
		// insert fail
		return Response.status(Status.INTERNAL_SERVER_ERROR).build();
	}

	@PUT
	@Path("/applications/{appId}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response put(@PathParam("appId") String appId, Application obj) {
		// validation, appId param should be an int
		try {
			Integer.parseInt(appId);
		} catch (NumberFormatException e) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		// validation, jobId in payload should be an int
		if (null != obj.getJobId()) {
			try {
				Integer.parseInt(obj.getJobId());
			} catch (NumberFormatException e) {
				return Response.status(Status.BAD_REQUEST).build();
			}
		}
		// validation, status in payload must be null or empty
		String statusPayload = obj.getStatus();
		if (null != statusPayload && !"".equals(statusPayload))
			return Response.status(Status.BAD_REQUEST).build();
		// validation, appId in payload must be null or empty
		String appIdPayload = obj.getAppId();
		if (null != appIdPayload && !"".equals(appIdPayload))
			return Response.status(Status.BAD_REQUEST).build();
		// validation, has something to update
		boolean hasUpdate = false;
		hasUpdate |= (null != obj.getJobId());
		hasUpdate |= (null != obj.getCandidateDetails());
		hasUpdate |= (null != obj.getCoverLetter());
		hasUpdate |= (null != obj.getStatus());
		if (!hasUpdate)
			return Response.status(Status.BAD_REQUEST).build();
		// check item exists
		Application p = aDao.findById(appId);
		if (null == p)
			return Response.status(Status.NOT_FOUND).build();
		// cannot update application if already in-review
		int status = Integer.parseInt(p.getStatus());
		if (status >= ApplicationStatus.IN_REVIEW)
			return Response.status(Status.FORBIDDEN).build();
		// update
		obj.setAppId(appId);
		int affectedRowCount = aDao.update(obj);
		if (0 == affectedRowCount) { // update fail
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		} else {
			return Response.status(Status.NO_CONTENT).build();
		}
	}

	@PUT
	@Path("/applications/{status}/{id}") // status is rejected or accept or
											// in_review
	public Response updateStatus(@PathParam("status") String status, @PathParam("id") String id) {
		// validation, id should be an int
		try {
			Integer.parseInt(id);
		} catch (NumberFormatException e) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		// rejected or accepted
		if ("in_review".equals(status) || "rejected".equals(status) || "accepted".equals(status)) {
			Application a = aDao.findById(id);
			// check item exist
			if (null == a)
				return Response.status(Status.NOT_FOUND).build();
			// check old status
			// can set to in_review at any time
			// but set to r/a only after in_review
			if (!status.equals("in_review")// rejected or accepted
					&& Integer.parseInt(a.getStatus()) < ApplicationStatus.IN_REVIEW) {
				return Response.status(Status.FORBIDDEN).build();
			}
			// update
			switch (status) {
			case "in_review":
				a.setStatus(String.valueOf(ApplicationStatus.IN_REVIEW));
				break;
			case "rejected":
				a.setStatus(String.valueOf(ApplicationStatus.REJECTED));
				break;
			default: // accepted
				a.setStatus(String.valueOf(ApplicationStatus.ACCEPTED));
				break;
			}
			int affectedRowCount = aDao.update(a);
			if (0 == affectedRowCount) { // update fail
				return Response.status(Status.INTERNAL_SERVER_ERROR).build();
			} else {
				return Response.status(Status.NO_CONTENT).build();
			}
		} else {
			return Response.status(Status.METHOD_NOT_ALLOWED).build();
		}
	}
}
