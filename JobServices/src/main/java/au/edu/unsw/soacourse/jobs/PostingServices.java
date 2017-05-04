package au.edu.unsw.soacourse.jobs;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import au.edu.unsw.soacourse.jobs.dao.PostingsDao;
import au.edu.unsw.soacourse.jobs.model.Posting;
import au.edu.unsw.soacourse.jobs.model.PostingStatus;

@Path("/posting")
public class PostingServices implements RESTfulAPI<Posting>{

	private PostingsDao dao = new PostingsDao();
	
	@Override
	public List<Posting> getMulti() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@GET
	@Path("/{id}")
	@Consumes("text/plain")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Posting getOne(@PathParam("id") String id) {
		return dao.getOne(id);
	}

	@Override
	public void post() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void del(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Response put() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
