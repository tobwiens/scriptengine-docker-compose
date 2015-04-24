package jsr223.docker.compose;

import org.jetbrains.annotations.NotNull;
import processbuilder.SingletonProcessBuilderFactory;
import processbuilder.utils.ProcessBuilderUtilities;

import javax.script.*;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;

/**
 * Created on 4/21/2015.
 */
public class DockerComposeScriptEngine extends AbstractScriptEngine {

    private static final int SUCCESSFUL_EXECUTION = 0;

    // TODO: Implement
    // TODO: Test
    @Override
    public Object eval(String script, ScriptContext context) throws ScriptException {
        // Create docker compose command
        String[] dockerComposeCommand = DockerComposeCommandCreator.createDockerComposeExecutionCommand();

        // Create a process builder
        ProcessBuilder processBuilder = SingletonProcessBuilderFactory
                .getInstance().getProcessBuilder(dockerComposeCommand);

        // Add string bindings as environment variables
        addBindingToStringMap(context.getBindings(ScriptContext.ENGINE_SCOPE), processBuilder.environment());

        try {
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
            System.err.println("Filed to execute docker-compose.");
            e.printStackTrace();
        } catch (InterruptedException e) {
            // Thread was interrupted somehow, now we need to make sure that
            // the container are stopped even though several interrupts could
            // occur. Therefore start a thread which will remove all container.
            // TODO: Start thread which will stop and remove container in the background
        }


        return null;
    }
    // TODO: Implement
    // TODO: Test
    @Override
    public Object eval(Reader reader, ScriptContext context) throws ScriptException {

        return null;
    }

    /**
     * Adds all bindings which are from type @String to the environment map. All other bindings are printed
     * with toString() to std.err with an error message.
     * @param bindings Bindings which will be read and added to environment.
     * @param environment Map<String,String> which will get all Entry<String,String> from he @Bindings
     */
    private void addBindingToStringMap(@NotNull Bindings bindings, @NotNull Map<String, String> environment) {
        for(Map.Entry<String, Object> entry: bindings.entrySet()) {
            if (entry.getValue() instanceof String) {
                environment.put(entry.getKey(),(String) entry.getValue());
            } else {
                System.err.println("Ignored binding: "+entry.getValue().toString());
            }
        }
    }

    @Override
    public Bindings createBindings() {
        return new SimpleBindings();
    }
    // TODO: Implement
    // TODO: Test
    @Override
    public ScriptEngineFactory getFactory() {
        return null;
    }
}
