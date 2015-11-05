package systemtests.helper;

import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.IOException;

public class ProActiveRestSubmitWorkflow {

    private final WebTarget schedulerWebTarget = ClientBuilder.newClient()
            .register(MultiPartFeature.class)
            .target("http://trydev.activeeon.com:8080/rest");

    private final String sessionIdFormParam = "sessionid";


    /**
     * Takes workflow file to submit it to the scheduler.
     * @param token session id.
     * @param file Workflow to be submitted to the scheduler.
     * @return Json response from the scheduler.
     * @throws IOException If anything goes wrong with writing the file into a string for submittion.
     */
    public String submitWorkflow(String token, File file) throws IOException {

        // Rest target to submit workflows
        WebTarget schedulerSubmitTarget = this.schedulerWebTarget.path("/scheduler/submit");

       // Add a octet stream of a file to the post
        FormDataMultiPart multiPart = new FormDataMultiPart();
        multiPart.setMediaType(MediaType.MULTIPART_FORM_DATA_TYPE);
        multiPart.bodyPart(
                new FileDataBodyPart("file", file, MediaType.APPLICATION_XML_TYPE));

        // Post a request with media type json plus session id and workflow - it expects a string back
        String jobId =  schedulerSubmitTarget.request(MediaType.APPLICATION_JSON_TYPE)
                .header(sessionIdFormParam,token)
                .post(Entity.entity(multiPart,multiPart.getMediaType()),String.class);

        return jobId;
    }
}
