package jsr223.docker.compose.utils;

import java.io.IOException;
import java.io.StringWriter;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import processbuilder.ProcessBuilderFactory;
import processbuilder.utils.ProcessBuilderUtilities;


@Log4j
@RequiredArgsConstructor
public class DockerComposeVersionGetter {

    @NonNull
    private ProcessBuilderUtilities processBuilderUtilities;

    /**
     * Retrieves the docker compose version.
     *
     * @return The currently installed version return by the docker compose command or an empty string
     * the version could not be determined.
     */
    public String getDockerComposeVersion(ProcessBuilderFactory factory) {
        if (factory == null) {
            return "";
        }

        String result = ""; // Empty string for empty result if version recovery fails

        ProcessBuilder processBuilder = factory.getProcessBuilder(DockerComposePropertyLoader
                        .getInstance()
                        .getDockerComposeCommand(),
                "--version");

        try {
            Process process = processBuilder.start();

            // Attach stream to std output of process
            StringWriter commandOutput = new StringWriter();
            processBuilderUtilities.attachStreamsToProcess(process, commandOutput, null, null);

            // Wait for process to exit
            process.waitFor();

            // Extract output
            result = commandOutput.toString();
        } catch (IOException | InterruptedException | IndexOutOfBoundsException e) {
            log.debug("Failed to retrieve docker compose version.", e);
        }
        return result;
    }

}
