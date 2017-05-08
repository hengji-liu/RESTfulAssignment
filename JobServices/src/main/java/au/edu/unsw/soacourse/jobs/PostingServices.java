package au.edu.unsw.soacourse.jobs;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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
import au.edu.unsw.soacourse.jobs.model.Posting;
import au.edu.unsw.soacourse.jobs.model.PostingStatus;

public class PostingServices {
	private PostingsDao pDao = new PostingsDao();
	private ApplicationsDao aDao = new ApplicationsDao();

	@GET
	@Path("/posting/{id}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response get(@HeaderParam("accept") String type, @PathParam("id") String id) {
		// validation, id should be an int
		try {
			Integer.parseInt(id);
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
		Posting p = pDao.findById(id);
		if (null != p) {
			if (type.equals(MediaType.WILDCARD))
				type = MediaType.APPLICATION_JSON; // default json
			return Response.status(Status.OK).entity(p).type(type).build();
		} else {// item not found
			return Response.status(Status.NOT_FOUND).build();
		}
	}

	@POST
	@Path("/posting")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response post(Posting obj) {
		// validation, jobId must be null or empty
		String jobId = obj.getJobId();
		if (null != jobId && !"".equals(jobId))
			return Response.status(Status.BAD_REQUEST).build();
		// validation, other fields must not be null
		if (null == obj.getCompanyName()//
				|| null == obj.getDescriptions()//
				|| null == obj.getLocation()//
				|| null == obj.getPositionType()//
				|| null == obj.getSalaryRate()//
		) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		// validation, status can only be empty or number 0(created)/1(open)
		String status = obj.getStatus();
		if (null == status || "".equals(status)) { // default status is created
			obj.setStatus(String.valueOf(PostingStatus.CREATED));
		} else {
			try {
				int statusInt = Integer.parseInt(status);
				if (statusInt >= PostingStatus.IN_REVIEW)
					return Response.status(Status.BAD_REQUEST).build();
			} catch (NumberFormatException e) {
				return Response.status(Status.BAD_REQUEST).build();
			}
		}
		// insert
		int insertedId = pDao.insert(obj);
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

	@DELETE
	@Path("/posting/{id}")
	public Response del(@PathParam("id") String id) {
		// validation, id should be an int
		try {
			Integer.parseInt(id);
		} catch (NumberFormatException e) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		// check item exists
		Posting p = pDao.findById(id);
		if (null == p)
			return Response.status(Status.NOT_FOUND).build();
		// check no application is associated with this posting
		int count = aDao.countByJobId(id);
		if (count > 0) {
			return Response.status(Status.FORBIDDEN).build();
		} else if (count < 0) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
		// delete item
		int affectedRowCount = pDao.delete(id);
		if (0 == affectedRowCount) { // delete fail
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		} else {
			return Response.status(Status.NO_CONTENT).build();
		}
	}

	@PUT
	@Path("/posting/{id}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response put(@PathParam("id") String id, Posting obj) {
		// validation, id param should be an int
		try {
			Integer.parseInt(id);
		} catch (NumberFormatException e) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		// validation, jobId in payload must be null or empty
		String jobId = obj.getJobId();
		if (null != jobId && !"".equals(jobId))
			return Response.status(Status.BAD_REQUEST).build();
		// validation, has something to update
		boolean hasUpdate = false;
		hasUpdate |= (null != obj.getCompanyName());
		hasUpdate |= (null != obj.getDescriptions());
		hasUpdate |= (null != obj.getLocation());
		hasUpdate |= (null != obj.getPositionType());
		hasUpdate |= (null != obj.getSalaryRate());
		hasUpdate |= (null != obj.getStatus());
		if (!hasUpdate)
			return Response.status(Status.BAD_REQUEST).build();
		// check item exists
		Posting p = pDao.findById(id);
		if (null == p)
			return Response.status(Status.NOT_FOUND).build();
		// check no application is associated with this posting
		int count = aDao.countByJobId(id);
		if (count > 0) {
			return Response.status(Status.FORBIDDEN).build();
		} else if (count < 0) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
		// update
		obj.setJobId(id);
		int affectedRowCount = pDao.update(obj);
		if (0 == affectedRowCount) { // update fail
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		} else {
			return Response.status(Status.NO_CONTENT).build();
		}
	}

	@GET
	@Path("/postings") // postings?keyword=yo&status=0
	@Produces(MediaType.APPLICATION_JSON)
	public Response search(@HeaderParam("accept") String type, @QueryParam("keyword") String keyword,
			@QueryParam("status") String status) {
		// validation media type
		if (!type.equals(MediaType.WILDCARD) && !type.equals(MediaType.APPLICATION_JSON))
			return Response.status(Status.BAD_REQUEST).build();

		// if no query param
		if ((null == keyword || "".equals(keyword)) && (null == status || "".equals(status))) {
			List<Posting> list = pDao.findAll();
			if (null != list) {
				return Response.status(Status.OK).entity(list).type(MediaType.APPLICATION_JSON).build();
			} else {
				return Response.status(Status.INTERNAL_SERVER_ERROR).build();
			}
		}
		// validation, status is an valid int
		if (null != status && !"".equals(status)) {
			try {
				int statusInt = Integer.parseInt(status);
				if (statusInt < PostingStatus.CREATED && statusInt > PostingStatus.PROCESSED)
					return Response.status(Status.BAD_REQUEST).build();
			} catch (NumberFormatException e) {
				return Response.status(Status.BAD_REQUEST).build();
			}
		}

		// search on at least one param
		List<Posting> list = pDao.search(keyword, status);
		if (null != list) {
			return Response.status(Status.OK).entity(list).type(MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PUT
	@Path("/postings/{status}/{id}") // status is rejected or accept
	public Response updateStatus(@PathParam("status") String status, @PathParam("id") String id) {
		// validation, id should be an int
		try {
			Integer.parseInt(id);
		} catch (NumberFormatException e) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		// rejected or accepted
		if ("open".equals(status) //
				|| "in_review".equals(status) //
				|| "processed".equals(status) //
				|| "sent_invitations".equals(status) //
		) {
			Posting p = pDao.findById(id);
			// check item exist
			if (null == p)
				return Response.status(Status.NOT_FOUND).build();
			// check status and update
			int oldStatus = Integer.parseInt(p.getStatus());
			int newStatus;
			switch (status) {
			case "open":
				newStatus = PostingStatus.OPEN;
				break;
			case "in_review":
				newStatus = PostingStatus.IN_REVIEW;
				break;
			case "processed":
				newStatus = PostingStatus.PROCESSED;
				break;
			default: // sent_invitations
				newStatus = PostingStatus.SENT_INVITATIONS;
			}
			// status have a total order, can only move forward
			if (newStatus < oldStatus)
				return Response.status(Status.FORBIDDEN).build();
			// update status
			int affectedRowCount = pDao.update(p);
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
