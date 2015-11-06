package systemtests.helper;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

public class ProActiveRestGetLogs {

    private final WebTarget schedulerWebTarget = ClientBuilder.newClient().target("http://trydev.activeeon.com:8080/rest");
    private final String sessionIdFormParam = "sessionid";


    public String getLogsFromJob(String token, String jobid) {
        // Rest target to submit workflows
        WebTarget schedulerSubmitTarget = this.schedulerWebTarget.path("/scheduler/jobs/"+jobid+"/result/log/all");

        // Post a request with media type json plus session id and workflow - it expects a string back
        return schedulerSubmitTarget.request(MediaType.APPLICATION_JSON_TYPE)
                .header(sessionIdFormParam,token)
                .get(String.class);
    }
}
