package systemtests.helper;


import org.json.JSONObject;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

public class ProActiveRestWaitForJobFinished {

    private final WebTarget schedulerWebTarget = ClientBuilder.newClient().target("http://trydev.activeeon.com:8080/rest");
    private final String sessionIdFormParam = "sessionid";

    public void waitForJobFinished(String token, String jobid) {
        // Rest target to submit workflows
        WebTarget schedulerSubmitTarget = this.schedulerWebTarget.path("/scheduler/jobs/" + jobid);


        String jobStatus = "nothing";
        // Query REST Api until jobStatus is FINISHED
        while (!"FINISHED".equals(jobStatus)) {
            // Post a request with media type json plus session id and workflow - it expects a string back
            String answer = schedulerSubmitTarget.request(MediaType.APPLICATION_JSON_TYPE)
                    .header(sessionIdFormParam, token)
                    .get(String.class);
            final JSONObject jsonObject = new JSONObject(answer);
            jobStatus = jsonObject.getJSONObject("jobInfo").getString("status");
        }
    }
}
