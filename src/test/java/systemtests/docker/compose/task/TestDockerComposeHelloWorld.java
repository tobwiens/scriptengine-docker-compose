package systemtests.docker.compose.task;

import org.junit.Test;
import systemtests.helper.ProActiveRestLogin;
import systemtests.helper.ProActiveRestSubmitWorkflow;

import java.io.File;
import java.io.IOException;

public class TestDockerComposeHelloWorld {

    private ProActiveRestLogin proactiveRestLogin = new ProActiveRestLogin();
    private ProActiveRestSubmitWorkflow proactiveRestSubmitWorkflow = new ProActiveRestSubmitWorkflow();

    private String username = "admin";
    private String password = "admin";

    private String helloWorldBusyboxWorkflowPath =
            "systemtests/docker/compose/task/DockerComposeBusyBoxHelloWorldWorkflow.xml";


    @Test
    public void helloWorldBusyboxContainerTest() throws IOException {
        String token = this.proactiveRestLogin.login(this.username, this.password);

        // Submit workflow with token and file
        String jobId = proactiveRestSubmitWorkflow.submitWorkflow(token,
               new File(getClass()
                       .getClassLoader()
                       .getResource(helloWorldBusyboxWorkflowPath)
                       .getPath()));

        System.out.println(jobId);
    }
}
