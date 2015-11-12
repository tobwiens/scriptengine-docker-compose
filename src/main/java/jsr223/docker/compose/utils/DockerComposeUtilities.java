package jsr223.docker.compose.utils;

import jsr223.docker.compose.DockerComposeCommandCreator;
import org.jetbrains.annotations.NotNull;
import processbuilder.ProcessBuilderFactory;
import processbuilder.utils.ProcessBuilderUtilities;

import java.io.IOException;
import java.io.StringWriter;



public class DockerComposeUtilities {
    // TODO: Add logger
    /**
     * Retrieves the docker compose version.
     * @return The currently installed version return by the docker compose command or an empty string
     * the version could not be determined.
     */
    public static String getDockerComposeVersion(@NotNull ProcessBuilderFactory factory) {
        String result = ""; // Empty string for empty result if version recovery fails

        ProcessBuilder pb = factory.getProcessBuilder(DockerComposePropertyLoader
                .getInstance()
                .getDockerComposeCommand(),
                "--version");

        try {
            Process process = pb.start();

            // Attach stream to std output of process
            StringWriter commandOutput = new StringWriter();
            ProcessBuilderUtilities.attachStreamsToProcess(process,commandOutput, null, null);

            // Wait for process to exit
            process.waitFor();

            // Extract output
            result = commandOutput.toString();
        } catch (IOException|InterruptedException|IndexOutOfBoundsException  e){
            System.err.println("Failed to retrieve docker compose version.");
            e.printStackTrace();
        }
        return result;
    }

}
