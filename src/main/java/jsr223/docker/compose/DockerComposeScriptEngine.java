package jsr223.docker.compose;

import jsr223.docker.compose.bindings.MapBindingsAdder;
import jsr223.docker.compose.bindings.StringBindingsAdder;
import jsr223.docker.compose.file.write.ConfigurationFileWriter;
import jsr223.docker.compose.utils.DockerComposePropertyLoader;
import jsr223.docker.compose.utils.Log4jConfigurationLoader;
import jsr223.docker.compose.yaml.VariablesReplacer;
import lombok.extern.log4j.Log4j;
import processbuilder.SingletonProcessBuilderFactory;
import processbuilder.utils.ProcessBuilderUtilities;

import javax.script.*;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.util.Map;

@Log4j
public class DockerComposeScriptEngine extends AbstractScriptEngine {

    private static final String DOCKER_HOST_PROPERTY_NAME = "DOCKER_HOST";


    private ProcessBuilderUtilities processBuilderUtilities = new ProcessBuilderUtilities();
    private VariablesReplacer variablesReplacer = new VariablesReplacer();
    private ConfigurationFileWriter configurationFileWriter = new ConfigurationFileWriter();
    private StringBindingsAdder stringBindingsAdder = new StringBindingsAdder(
            new MapBindingsAdder());
    private DockerComposeCommandCreator dockerComposeCommandCreator = new DockerComposeCommandCreator();
    private Log4jConfigurationLoader log4jConfigurationLoader = new Log4jConfigurationLoader();


    public DockerComposeScriptEngine() {
        // This is the entry-point of the script engine
        log4jConfigurationLoader.loadLog4jConfiguration();
    }


    @Override
    public Object eval(String script, ScriptContext context) throws ScriptException {
        // Create docker compose command
        String[] dockerComposeCommand = dockerComposeCommandCreator
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
                    dockerComposeCommandCreator.YAML_FILE_NAME);

            // Start process
            Process process = processBuilder.start();

            // Attach streams
            processBuilderUtilities.attachStreamsToProcess(process,
                    context.getWriter(),
                    context.getErrorWriter(),
                    context.getReader());

            // Wait for process to exit
            return process.waitFor();
        } catch (IOException e) {
            log.warn("Failed to execute Docker Compose.", e);
        } catch (InterruptedException e) {
            log.info("Container execution interrupted. " + e.getMessage());
        } finally {
            try {
                // Reset thread's interrupt flag
                Thread.interrupted();
                stopAndRemoveContainers().waitFor();
            } catch (Exception e) {
                log.error("Container removal was interrupted: " + e.getMessage());
            }
            // Delete configuration file
            if (composeYamlFile != null) {
                boolean deleted = composeYamlFile.delete();
                if (!deleted) {
                    log.warn("File: " + composeYamlFile.getAbsolutePath() + " was not deleted.");
                }
            }
        }
        return null;
    }

    private Process stopAndRemoveContainers() throws IOException {
        return SingletonProcessBuilderFactory
                .getInstance()
                .getProcessBuilder(dockerComposeCommandCreator.createDockerComposeDownCommand())
                .start();
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
        return new DockerComposeScriptEngineFactory();
    }
}
