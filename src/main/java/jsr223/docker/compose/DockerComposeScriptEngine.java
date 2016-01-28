package jsr223.docker.compose;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.util.Map;

import javax.script.AbstractScriptEngine;
import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

import jsr223.docker.compose.bindings.MapBindingsAdder;
import jsr223.docker.compose.bindings.StringBindingsAdder;
import jsr223.docker.compose.file.write.ConfigurationFileWriter;
import jsr223.docker.compose.utils.DockerComposePropertyLoader;
import jsr223.docker.compose.utils.DockerComposeVersionGetter;
import jsr223.docker.compose.yaml.VariablesReplacer;
import lombok.extern.log4j.Log4j;
import processbuilder.SingletonProcessBuilderFactory;
import processbuilder.utils.ProcessBuilderUtilities;

@Log4j
public class DockerComposeScriptEngine extends AbstractScriptEngine {

    private static final String DOCKER_HOST_PROPERTY_NAME = "DOCKER_HOST";
    private static final String LOG4J_CONFIGURATION_FILE = "config/log/scriptengines.properties";

    private static ProcessBuilderUtilities processBuilderUtilities = new ProcessBuilderUtilities();
    private static VariablesReplacer variablesReplacer = new VariablesReplacer();
    private static ConfigurationFileWriter configurationFileWriter = new ConfigurationFileWriter();
    private static StringBindingsAdder stringBindingsAdder = new StringBindingsAdder(
            new MapBindingsAdder());


    public DockerComposeScriptEngine() {
        // This is the entrypoint of the script engine
        // configure the logger here, quick and dirty
        // Catch all exceptions to not sacrifice functionality for logging.
        try {
            org.apache.log4j.PropertyConfigurator.configure(getClass()
                    .getClassLoader().getResourceAsStream(LOG4J_CONFIGURATION_FILE));
        } catch (NullPointerException e) {
            System.err.println("Log4j configuration file not found: " + LOG4J_CONFIGURATION_FILE +
                    ". Any output for the Docker Compose script engine is disabled.");
        } catch (Exception e) {
            System.err.println("Log4j initialization failed: " + LOG4J_CONFIGURATION_FILE +
                    ". Docker Compose script engine is functional but logging is disabled." +
                    "Stacktrace is: ");
            e.printStackTrace();
        }

    }


    @Override
    public Object eval(String script, ScriptContext context) throws ScriptException {
        // Create docker compose command
        String[] dockerComposeCommand = DockerComposeCommandCreator
                .createDockerComposeExecutionCommand();

        // Create a process builder
        ProcessBuilder processBuilder = SingletonProcessBuilderFactory
                .getInstance().getProcessBuilder(dockerComposeCommand);

        // Use process builder environment and fill it with environment variables
        Map<String, String> variablesMap = processBuilder.environment();

        // Add string bindings as environment variables
        stringBindingsAdder.addBindingToStringMap(context.getBindings(ScriptContext.ENGINE_SCOPE),
                variablesMap);

        // Add DOCKER_HOST variable to execution environment
        variablesMap.put(DOCKER_HOST_PROPERTY_NAME,
                DockerComposePropertyLoader.getInstance().getDockerHost());

        // Replace variables in configuration file
        String scriptReplacedVariables = variablesReplacer.replaceVariables(script, variablesMap);

        File composeYamlFile = null;

        try {
            composeYamlFile = configurationFileWriter.forceFileToDisk(scriptReplacedVariables,
                    DockerComposeCommandCreator.getYamlFileName());

            // Start process
            Process process = processBuilder.start();

            // Attach streams
            processBuilderUtilities.attachStreamsToProcess(process,
                    context.getWriter(),
                    context.getErrorWriter(),
                    context.getReader());

            // Wait for process to exit
            int returnValue = process.waitFor();

            // Stop containers
            SingletonProcessBuilderFactory
                    .getInstance()
                    .getProcessBuilder(DockerComposeCommandCreator.createDockerComposeStopCommand())
                    .start().waitFor();

            // Remove containers
            SingletonProcessBuilderFactory
                    .getInstance()
                    .getProcessBuilder(DockerComposeCommandCreator.createDockerComposeRemoveCommand())
                    .start().waitFor();

            return returnValue;

        } catch (IOException e) {
            log.warn("Failed to execute Docker Compose.", e);
        } catch (InterruptedException e) {
            // Thread was interrupted somehow, now we need to make sure that
            // the container are stopped even though several interrupts could
            // occur. Therefore start a thread which will remove all container.
            log.warn("Execution was terminated and container might need to be terminated manually.");
        } finally {
            // Delete configuration file
            if (composeYamlFile != null) {
                boolean deleted = composeYamlFile.delete();
                if (!deleted) {
                    log.warn("File: "+composeYamlFile.getAbsolutePath()+" was not deleted.");
                }
            }
        }
        return null;
    }


    // TODO: Test
    @Override
    public Object eval(Reader reader, ScriptContext context) throws ScriptException {

        StringWriter stringWriter = new StringWriter();

        try {
            ProcessBuilderUtilities.pipe(reader, stringWriter);
        } catch (IOException e) {
            log.warn(
                    "Filed to convert Reader into StringWriter. Not possible to execute Docker Compose script.");
            log.debug(
                    "Filed to convert Reader into StringWriter. Not possible to execute Docker Compose script.",
                    e);
        }

        return eval(stringWriter.toString(), context);
    }


    @Override
    public Bindings createBindings() {
        return new SimpleBindings();
    }

    @Override
    public ScriptEngineFactory getFactory() {
        return new DockerComposeScriptEngineFactory(new DockerComposeVersionGetter(processBuilderUtilities));
    }
}
