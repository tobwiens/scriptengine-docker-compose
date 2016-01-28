package jsr223.docker.compose;

import java.io.IOException;

import javax.script.ScriptException;


public class DockerComposeScriptEngineTest {


    /**
     * Check whether the process builder is correctly used and executed. If the environment is accessed
     * and if the input and output streams are accessed.
     *
     * @param pb Mocked ProcessBuilder
     * @throws IOException          Should not be thrown.
     * @throws InterruptedException Should not be thrown.
     * @throws ScriptException      Should not be thrown.
     */
   /* @Test
    public void checkProcessBuilderExecution(
            @Mocked final ProcessBuilder pb) throws IOException, InterruptedException, ScriptException {
        new Expectations() {
            {
                // Expect a new creation of the process builder with the command given from the
                // DockerComposeCommandCreator
                new ProcessBuilder(DockerComposeCommandCreator.createDockerComposeExecutionCommand());

                // Expect the environment to be accessed to fill it with env variables
                pb.environment();
                result = new HashMap<String, String>();

                // Expect to start the process
                Process process = pb.start();
                // Expect to get all streams and attach them
                process.getOutputStream();
                process.getErrorStream();
                process.getInputStream();
                // Expect to wait for finishing of execution
                process.waitFor();
                result = 0;

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
    }*/
}
