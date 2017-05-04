package au.edu.unsw.soacourse.polling;

import java.sql.SQLException;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import au.edu.unsw.soacourse.polling.beans.Poll;
import au.edu.unsw.soacourse.polling.beans.Vote;

@Path("/polling")
public class Polling {
	private final static String DATABASE_URL = "jdbc:sqlite:polling.db";
	
	private Dao<Poll, String> pollDao;
	private Dao<Vote, String> voteDao;
	protected ConnectionSource connectionSource;

    public Dao<Poll, String> getPollDao() {
    	if (pollDao == null) {
    		try {
				connectionSource = new JdbcConnectionSource(DATABASE_URL);
				pollDao = DaoManager.createDao(connectionSource, Poll.class);
				if (!pollDao.isTableExists()) {
					TableUtils.createTable(connectionSource, Poll.class);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
		return pollDao;
	}

	public Dao<Vote, String> getVoteDao() {
    	if (voteDao == null) {
    		try {
				connectionSource = new JdbcConnectionSource(DATABASE_URL);
				voteDao = DaoManager.createDao(connectionSource, Vote.class);
				if (!voteDao.isTableExists()) {
					TableUtils.createTable(connectionSource, Vote.class);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
		return voteDao;
	}

    @GET
    @Path("/poll/{input}")
    @Produces("text/plain")
    public String getPoll(@PathParam("input") String input) {
        try {
			return String.valueOf(getPollDao().countOf());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return input;
    }
    
    @GET
    @Path("/polls/")
    @Produces("text/plain")
    public String getPolls(@PathParam("input") String input) {
        return input;
    }
    
    @POST
    @Produces("application/json")
    @Consumes("application/json")
    @Path("/addNewVote")
    public Response addNewVote(Vote inputVote) {
    	String voteId = UUID.randomUUID().toString();
    	Vote outputVote = new Vote(voteId, inputVote.getParticipantName(), inputVote.getChosenOption());
        return Response.ok().entity(outputVote).build();
    }
}
