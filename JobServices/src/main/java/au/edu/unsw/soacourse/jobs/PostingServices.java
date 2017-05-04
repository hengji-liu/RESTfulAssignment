package au.edu.unsw.soacourse.jobs;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import au.edu.unsw.soacourse.jobs.dao.PostingsDAO;
import au.edu.unsw.soacourse.jobs.model.Posting;
import au.edu.unsw.soacourse.jobs.model.PostingStatus;

public class PostingServices {
	private PostingsDAO dao = new PostingsDAO();

	@GET
	@Path("/posting/{id}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response get(@HeaderParam("accept") String type, @PathParam("id") String id) {
		// validation, id should be an int
		try {
			Integer.parseInt(id);
		} catch (NumberFormatException e) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		// get item
		Posting p = dao.findById(id);
		if (null != p) {
			if (type.equals(MediaType.WILDCARD) || type.equals(MediaType.APPLICATION_JSON)) {
				return Response.ok(p, MediaType.APPLICATION_JSON).build();
			} else if (type.equals(MediaType.APPLICATION_XML)) {
				return Response.ok(p, MediaType.APPLICATION_XML).build();
			} else {// other type not supported
				System.out.println(type);
				return Response.status(Status.BAD_REQUEST).build();
			}
		} else {// item not found
			return Response.noContent().build();
		}
	}

	@POST
	@Path("/posting")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response post(Posting obj) {
		// validation, jobId must be null or empty
		if (!(null == obj.getJobId() || "".equals(obj.getJobId())))
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
		if (null != obj.getStatus()) {
			try {
				int status = Integer.parseInt(obj.getStatus());
				if (PostingStatus.OPEN < status)
					return Response.status(Status.BAD_REQUEST).build();
			} catch (NumberFormatException e) {
				return Response.status(Status.BAD_REQUEST).build();
			}
		} else { // default status is created
			obj.setStatus(String.valueOf(PostingStatus.CREATED));
		}
		// insert
		int insertedId = dao.insert(obj);
		if (0 != insertedId) {
			URI uri = null;
			try {
				uri = new URI("posting/" + Integer.toString(insertedId));
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			return Response.created(uri).build();
		}
		// insert fail
		return Response.status(Status.INTERNAL_SERVER_ERROR).build();
	}

	@DELETE
	@Path("/posting/{id}")
	@Consumes(MediaType.TEXT_PLAIN)
	public void del(@PathParam("id") String id) {
		// TODO Auto-generated method stub

	}

	@PUT
	@Path("/posting}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response put() {
		// TODO Auto-generated method stub
		return null;
	}

	// TODO
	public Response search() {
		return null;
	}

}
