package jsr223.docker.compose;

import mockit.Expectations;
import mockit.Mocked;
import mockit.VerificationsInOrder;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.script.ScriptContext;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import javax.script.SimpleScriptContext;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created on 4/21/2015.
 */
@RunWith(JMockit.class)
public class DockerComposeScriptEngineTest {

    String yamlFileWithVariables = "$name:\n"
            + "    image: $container\n"
            + "    command: echo \"$greetings\"";

    final String yamlFileExpected = "EchoUbuntu:\n"
            + "    image: dockerfile/ubuntu\n"
            + "    command: echo \"Hello World\"";


    /**
     * Test whether file is forced to disk. With a write flush and afterwards deleted.
     * @param configFileWriter Mocked file writer.
     * @throws IOException Should not be thrown.
     * @throws ScriptException Should not be thrown.
     */
    @Test
    public void testWhetherScriptIsForcedToDiskAndDeleted(@Mocked final FileWriter configFileWriter,
                                                          @Mocked final ProcessBuilder pb ) throws IOException, ScriptException, InterruptedException {
        ScriptContext context = new SimpleScriptContext();

        context.setErrorWriter(null);
        context.setReader(null);
        context.setErrorWriter(null);

        new Expectations() {
            {
                // Return an empty HashMap to prevent NullPointerException
                pb.environment(); result = new HashMap<>();
            }
        };

        DockerComposeScriptEngine dcse = new DockerComposeScriptEngine();
        dcse.eval(yamlFileExpected, context);

        new VerificationsInOrder() {
            {
                // Expect that script with substituted variables is written to disk
                configFileWriter.write(yamlFileExpected);
                // Check whether file is close, for docker-compose command to open it.
                configFileWriter.close();
                // After writing the file to disk and closing it the process builder is called
                pb.start().waitFor();
            }
        };


        org.junit.Assert.assertEquals("Configuration file must be deleted.",
                new File(DockerComposeCommandCreator.getYamlFileName()).exists(),
        false);
    }

    /**
     * Put variables into a HashMap<String,String> and test whether its variables are substituted
     * correctly.
     * @param configFileWriter Mock FileWriter object, to check if correct file is written.
     * @throws ScriptException Should not be thrown.
     * @throws IOException Should not be thrown.
     */
    @Test
    public void testStringStringMapsAsVariables(@Mocked final ProcessBuilder pb, @Mocked final FileWriter configFileWriter) throws ScriptException, IOException {
        ScriptContext context = new SimpleScriptContext();
        Map<String, String> variables = new HashMap<>();
        variables.put("name", "EchoUbuntu");
        variables.put("container", "dockerfile/ubuntu");
        variables.put("greetings", "Hello World");
        context.setAttribute("variables", variables, ScriptContext.ENGINE_SCOPE);


        context.setErrorWriter(null);
        context.setReader(null);
        context.setErrorWriter(null);

        new Expectations() {
            {
                pb.environment(); result = new HashMap<>();
                // Expect that script with substituted variables is written to disk
                configFileWriter.write(yamlFileExpected);
            }
        };

        DockerComposeScriptEngine dcse = new DockerComposeScriptEngine();
        dcse.eval(yamlFileWithVariables, context);
    }

    /**
     * Test whether variables are substituted correctly, when added as String,String to
     * the context and correctly written to disk.
     * @param configFileWriter Mocked FileWriter.
     * @throws ScriptException Should not be thrown
     * @throws IOException Should not be thrown
     */
    @Test
    public void testVariableSubstitution(@Mocked final FileWriter configFileWriter, @Mocked final ProcessBuilder pb ) throws ScriptException, IOException {
        ScriptContext context = new SimpleScriptContext();
        context.setAttribute("name", "EchoUbuntu", ScriptContext.ENGINE_SCOPE);
        context.setAttribute("container", "dockerfile/ubuntu", ScriptContext.ENGINE_SCOPE);
        context.setAttribute("greetings", "Hello World", ScriptContext.ENGINE_SCOPE);

        context.setErrorWriter(null);
        context.setReader(null);
        context.setErrorWriter(null);

        new Expectations() {
            {
                // Return an empty HashMap to prevent NullPointerException
                pb.environment(); result = new HashMap<>();

                // Expect that script with substituted variables is written to disk
                configFileWriter.write(yamlFileExpected);
            }
        };

        DockerComposeScriptEngine dcse = new DockerComposeScriptEngine();
        dcse.eval(yamlFileWithVariables, context);

    }

    /**
     * Check whether the process builder is correctly used and executed. If the environment is accessed
     * and if the input and output streams are accessed.
     * @param pb Mocked ProcessBuilder
     * @throws IOException Should not be thrown.
     * @throws InterruptedException Should not be thrown.
     * @throws ScriptException Should not be thrown.
     */
    @Test
    public void checkProcessBuilderExecution(@Mocked final ProcessBuilder pb) throws IOException, InterruptedException, ScriptException {
        new Expectations() {
            {
                // Expect a new creation of the process builder with the command given from the
                // DockerComposeCommandCreator
                new ProcessBuilder(DockerComposeCommandCreator.createDockerComposeExecutionCommand());

                // Expect the environment to be accessed to fill it with env variables
                pb.environment(); result = new HashMap<String, String>();

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
