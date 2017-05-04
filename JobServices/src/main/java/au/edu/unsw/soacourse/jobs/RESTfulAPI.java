package au.edu.unsw.soacourse.jobs;

import java.util.List;

import javax.ws.rs.core.Response;

public interface RESTfulAPI<T> {

	T getOne(String id);

	void del(String id);

	List<T> getMulti();// TODO

	void post();// TODO

	Response put(); // TODO
}
