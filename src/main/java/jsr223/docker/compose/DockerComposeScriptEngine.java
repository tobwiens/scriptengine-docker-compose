package jsr223.docker.compose;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.FileAlreadyExistsException;
import java.util.Map;

import javax.script.AbstractScriptEngine;
import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

import jsr223.docker.compose.utils.DockerComposePropertyLoader;
import lombok.extern.log4j.Log4j;
import org.jetbrains.annotations.NotNull;
import processbuilder.SingletonProcessBuilderFactory;
import processbuilder.utils.ProcessBuilderUtilities;

@Log4j
public class DockerComposeScriptEngine extends AbstractScriptEngine {

    private static final String DOCKER_HOST_PROPERTY_NAME = "DOCKER_HOST";
    private static final String log4jConfigurationFile = "config/log/scriptengines.properties";


    public DockerComposeScriptEngine() {
        // This is the entrypoint of the script engine
        // configure the logger here, quick and dirty
        // Catch all exceptions to not sacrifice functionality for logging.
        try {
            org.apache.log4j.PropertyConfigurator.configure(getClass()
                    .getClassLoader().getResourceAsStream(log4jConfigurationFile));
        } catch (NullPointerException e) {
            System.err.println("Log4j configuration file not found: " + log4jConfigurationFile +
                    ". Any output for the Docker Compose script engine is disabled.");
        } catch (Exception e) {
            System.err.println("Log4j initialization failed: " + log4jConfigurationFile +
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
        addBindingToStringMap(context.getBindings(ScriptContext.ENGINE_SCOPE), variablesMap);

        // Add DOCKER_HOST variable to execution environment
        variablesMap.put(DOCKER_HOST_PROPERTY_NAME,
                DockerComposePropertyLoader.getInstance().getDockerHost());

        // Replace variables in configuration file
        script = replaceVariables(script, variablesMap);

        File composeYamlFile = new File(DockerComposeCommandCreator.getYamlFileName());
        try {

            // Create configuration file
            if (!composeYamlFile.createNewFile()) {
                throw new FileAlreadyExistsException("Configuration file already exists: "
                        + DockerComposeCommandCreator.getYamlFileName());
            }

            // Force configuration file to disk
            Writer configFileWriter = new FileWriter(composeYamlFile);
            configFileWriter.write(script);
            configFileWriter.close();

            // Start process
            Process process = processBuilder.start();

            // Attach streams
            ProcessBuilderUtilities.attachStreamsToProcess(process,
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
            // TODO: Start thread which will stop and remove container in the background
            log.warn("Execution was terminated and container might need to be terminated manually.");
        } finally {
            // Delete configuration file
            composeYamlFile.delete();
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

    private String replaceVariables(String script, Map<String, String> variables) {
        String result = script;
        // Replace all variables one by one
        for (Map.Entry<String, String> variable : variables.entrySet()) {
            if (variable.getValue() != null) {
                result = result.replace("$" + variable.getKey(), variable.getValue());
            }
        }
        return result;
    }

    /**
     * Adds all bindings which are from type @String to the environment map. All other bindings are printed
     * with toString() to log file.
     *
     * @param bindings    Bindings which will be read and added to environment.
     * @param environment Map<String,String> which will get all Entry<String,String> from the @Bindings
     */
    private void addBindingToStringMap(@NotNull Bindings bindings, @NotNull Map<String, String> environment) {
        for (Map.Entry<String, Object> entry : bindings.entrySet()) {
            if (entry.getValue() instanceof String) {
                addEntryToEnvironmentWhichIsAPureString(environment, entry);
            } else { // Go through maps and add String String values to the environment map.
                AddEntryToEnvironmentOtherThanPureStrings(environment, entry);
            }
        }
    }

    private void addEntryToEnvironmentWhichIsAPureString(@NotNull Map<String, String> environment,
            Map.Entry<String, Object> entry) {
        environment.put(entry.getKey(), (String) entry.getValue());
        log.debug("Added binding: " + entry.getKey() + ":" + entry.getValue().toString());
    }

    private void AddEntryToEnvironmentOtherThanPureStrings(@NotNull Map<String, String> environment,
            Map.Entry<String, Object> entry) {
        log.warn("Got Binding binding: " + entry.getKey() + entry.getValue());
        if (containsKeyAndValue(entry) && valueIsMapType(entry)) {
            addEntryToEnvironmentWhichIsAMapContainingStrings(environment, entry);
        } else {
            log.warn(
                    "Ignored binding: " + entry.getKey() + "(" + getClassName(
                            entry.getKey()) + "):" + entry.getValue() + "(" + getClassName(
                            entry.getValue()) + ")");

        }
    }

    private String getClassName(Object object) {
        return object != null ? object.getClass().getName() : null;
    }


    private boolean valueIsMapType(Map.Entry<String, Object> entry) {
        return entry.getValue() instanceof Map<?, ?>;
    }

    private boolean containsKeyAndValue(Map.Entry<String, Object> entry) {
        return entry.getKey() != null && entry.getValue() != null;
    }

    private void addEntryToEnvironmentWhichIsAMapContainingStrings(@NotNull Map<String, String> environment,
            Map.Entry<String, Object> entry) {
        for (Map.Entry<?, ?> mapEntry : ((Map<?, ?>) entry.getValue()).entrySet()) {
            if (mapEntry.getValue() instanceof String && mapEntry.getKey() instanceof String) {
                environment.put((String) mapEntry.getKey(), (String) mapEntry.getValue());
                log.debug("Added binding: " + mapEntry.getKey() + ":" + mapEntry.getValue().toString());
            }
        }
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
