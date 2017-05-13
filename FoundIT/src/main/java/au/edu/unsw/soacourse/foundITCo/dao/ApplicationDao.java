package au.edu.unsw.soacourse.foundITCo.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import au.edu.unsw.soacourse.foundITCo.Utils;
import au.edu.unsw.soacourse.foundITCo.beans.Application;
import au.edu.unsw.soacourse.foundITCo.beans.Keys;
import au.edu.unsw.soacourse.foundITCo.beans.Posting;

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
            client.path("/applications/" + id);
            addKeys(client, shortKey);
            try {
                Application a = client.get(Application.class);
                Utils.trasnfromApplicationStatus(a);
                applications.add(a);
            } catch (Exception e) {
                // TODO
                System.out.println(" this posting id is not in the db of jobservices");
            }
        }
        return applications;
    }

//    public Response createPosting(Posting posting){
//        WebClient client = WebClient.create(JOB_URL, Arrays.asList(new JacksonJsonProvider()));
//        client.path("/postings");
//        client.type(MediaType.APPLICATION_JSON);
//        addKeys(client);
//        client.post(posting);
//        Response serviceResponse = client.getResponse();
//        return serviceResponse;
//    }
//
//    public Response updateStatus(String pid, String newStatus){
//        WebClient client = WebClient.create(JOB_URL, Arrays.asList(new JacksonJsonProvider()));
//        client.path("/postings/" +newStatus+"/"+pid);
//        addKeys(client);
//        client.put(null);
//        Response serviceResponse = client.getResponse();
//        return serviceResponse;
//    }
}
