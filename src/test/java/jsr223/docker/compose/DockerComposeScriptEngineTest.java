package jsr223.docker.compose;

import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.script.ScriptException;
import javax.script.SimpleBindings;
import java.io.IOException;

/**
 * Created on 4/21/2015.
 */
@RunWith(JMockit.class)
public class DockerComposeScriptEngineTest {


    // TODO: Test if configuration file is written to disk
    // TODO: Test if configuration file is deleted after usage

    @Test
    public void checkProcessBuilderExecution(@Mocked final ProcessBuilder pb) throws IOException, InterruptedException, ScriptException {
        new Expectations() {
            {
                // Expect a new creation of the process builder with the command given from the
                // DockerComposeCommandCreator
                new ProcessBuilder(DockerComposeCommandCreator.createDockerComposeExecutionCommand());

                // Expect the environment to be accessed to fill it with env variables
                pb.environment();
                // Expect to start the process
                Process process = pb.start();
                // Expect to get all streams and attach them
                process.getOutputStream();
                process.getErrorStream();
                process.getInputStream();
                // Expect to wait for finishing of execution
                process.waitFor(); result = 0;

                // Expect to finish and stop containers
                new ProcessBuilder(DockerComposeCommandCreator.createDockerComposeStopCommand());

                // Expect to remove container
                new ProcessBuilder(DockerComposeCommandCreator.createDockerComposeRemoveCommand());
            }
        };

        DockerComposeScriptEngine scriptEngine = new DockerComposeScriptEngine();
        org.junit.Assert.assertEquals("Return value must be as expected.",
                0,
                scriptEngine.eval("Mock", new SimpleBindings()));
    }
}
