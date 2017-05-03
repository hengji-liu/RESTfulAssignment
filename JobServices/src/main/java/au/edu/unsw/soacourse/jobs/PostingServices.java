package au.edu.unsw.soacourse.jobs;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import au.edu.unsw.soacourse.jobs.model.Posting;
import au.edu.unsw.soacourse.jobs.model.PostingStatus;

@Path("/posting")
public class PostingServices {

	@GET
	@Path("/{id}")
	@Consumes("text/plain")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<Posting> getJobs(@PathParam("id") String id) {
		ArrayList<Posting> list = new ArrayList<>();
		Posting posting = new Posting();
		posting.setStatus(PostingStatus.CREATED);
		posting.setJobId(id);
		list.add(posting);
		return list;
	}

    @GET
    @Path("/echo/{input}")
    @Produces("text/plain")
    public String ping(@PathParam("input") String input) {
        return input;
    }

	
	
	// @POST
	// @Produces("application/json")
	// @Consumes("application/json")
	// @Path("/jsonBean")
	// public Response modifyJson(JsonBean input) {
	// input.setVal2(input.getVal1());
	// return Response.ok().entity(input).build();
	// }
}
