package au.edu.unsw.soacourse.foundITCo.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import au.edu.unsw.soacourse.foundITCo.Utils;
import au.edu.unsw.soacourse.foundITCo.beans.Application;
import au.edu.unsw.soacourse.foundITCo.Keys;

public class ApplicationDao {

	private static WebClient client;

    private static void addKeys(WebClient client, String shortKey) {
        client.header(Keys.SECURITY_KEY, Keys.SECURITY_VAL);
        client.header(Keys.SHORT_KEY, shortKey);
    }

    public static List<Application> findApplicationsById(String baseUri, String shortKey, List<String> ids) {
        List<Application> applications = new ArrayList<>();
        client = WebClient.create(baseUri, Arrays.asList(new JacksonJsonProvider()));
        for (Iterator<String> iterator = ids.iterator(); iterator.hasNext();) {
            String id = (String) iterator.next();
            client.back(true);
            client.path("/JobServices/applications/" + id);
            addKeys(client, shortKey);
            try {
                Application a = client.get(Application.class);
                Utils.trasnfromApplicationStatus(a);
                applications.add(a);
            } catch (Exception e) {
                // TODO
            	e.printStackTrace();
                System.out.println(" this posting id is not in the db of jobservices");
            }
        }
        client.close();
        return applications;
    }
    
	public static Application findApplicationById(String baseUri, String shortKey, String id) {
		WebClient client = WebClient.create(baseUri, Arrays.asList(new JacksonJsonProvider()));
		client.back(true);
		client.path("/JobServices/applications/" + id);
        addKeys(client, shortKey);
		try {
			Application a = client.get(Application.class);
			Utils.trasnfromApplicationStatus(a);
			client.close();
			return a;
		} catch (Exception e) {
			// TODO
			System.out.println(" this application id is not in the db of jobservices");
		}
		client.close();
		return null;
	}
    
    public static List<Application> findApplicationsByPostingId(String baseUri, String shortKey, String pId) {
        List<Application> applications = new ArrayList<>();
        client = WebClient.create(baseUri, Arrays.asList(new JacksonJsonProvider()));
//        for (Iterator<String> iterator = ids.iterator(); iterator.hasNext();) {
//            String id = (String) iterator.next();
            client.back(true);
            client.path("/JobServices/postings/" + pId + "/applications");
            addKeys(client, shortKey);
            try {
                applications = client.get(new GenericType<List<Application>>() {});
//                Utils.trasnfromApplicationStatus(a);
            } catch (Exception e) {
                // TODO
            	e.printStackTrace();
                System.out.println(" this posting id is not in the db of jobservices");
            }
//        }
        client.close();
        return applications;
    }
    
    public static Response updateStatus(String baseUri, String shortKey, String appId, String newStatus){
		WebClient client = WebClient.create(baseUri, Arrays.asList(new JacksonJsonProvider()));
		client.path("/JobServices/applications/" + newStatus + "/" + appId);
		addKeys(client, shortKey);
		client.put(null);
		Response serviceResponse = client.getResponse();
		client.close();
		return serviceResponse;
	}
}
