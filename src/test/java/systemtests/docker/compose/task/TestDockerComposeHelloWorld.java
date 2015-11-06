package systemtests.docker.compose.task;

import org.json.JSONObject;
import org.junit.Test;
import systemtests.helper.ProActiveRestGetLogs;
import systemtests.helper.ProActiveRestLogin;
import systemtests.helper.ProActiveRestSubmitWorkflow;
import systemtests.helper.ProActiveRestWaitForJobFinished;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.text.StringContains.containsString;
import static org.junit.Assert.assertThat;

public class TestDockerComposeHelloWorld {

    private final ProActiveRestLogin proactiveRestLogin = new ProActiveRestLogin();
    private final ProActiveRestSubmitWorkflow proactiveRestSubmitWorkflow = new ProActiveRestSubmitWorkflow();
    private final ProActiveRestGetLogs proactiveRestGetLos = new ProActiveRestGetLogs();
    private final ProActiveRestWaitForJobFinished proactiveRestWaitForJobFinished = new ProActiveRestWaitForJobFinished();

    private final String username = "admin";
    private final String password = "admin";

    private final String helloWorldBusyboxWorkflowPath =
            "systemtests/docker/compose/task/DockerComposeBusyBoxHelloWorldWorkflow.xml";


    @Test
    public void helloWorldBusyboxContainerTest() throws IOException {
        String token = this.proactiveRestLogin.login(this.username, this.password);

        // Submit workflow with token and file
        String submissionAnswer = proactiveRestSubmitWorkflow.submitWorkflow(token,
               new File(getClass()
                       .getClassLoader()
                       .getResource(helloWorldBusyboxWorkflowPath)
                       .getPath()));


        final JSONObject jsonObject = new JSONObject(submissionAnswer);
        String jobId = Integer.toString(jsonObject.getInt("id"));

        this.proactiveRestWaitForJobFinished.waitForJobFinished(token,
                jobId);

        String logs = this.proactiveRestGetLos.getLogsFromJob(token,
                jobId );

        assertThat(logs, containsString("Hello ProActive"));
    }
}
