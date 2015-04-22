package jsr223.docker.compose.utils;

import processbuilder.ProcessBuilderFactory;
import processbuilder.utils.ProcessBuilderUtilities;

import java.io.IOException;
import java.io.StringWriter;


/**
 * Created on 4/21/2015.
 */
public class DockerComposeUtilities {

    private final static String versionOutputRegex = "^docker-compose ";
    private final static int versionOutputPosition = 1;

    // TODO: Add logger

    // TODO: Test
    /**
     * Retrieves the docker compose version.
     * @param composeCommand The docker compose command. Example "/bin/sudo/docker-compose"
     * @return The currently installed version return by the docker compose command or an empty string
     * the version could not be determined.
     */
    public static String getDockerComposeVersion(String composeCommand, ProcessBuilderFactory factory) {
        String result = ""; // Empty string for empty result if version recovery fails

        ProcessBuilder pb = factory.getProcessBuilder(new String[]{composeCommand, "--version"});

        try {
            Process process = pb.start();

            // Attach stream to std output of process
            StringWriter commandOutput = new StringWriter();
            ProcessBuilderUtilities.attachStreamsToProcess(process,commandOutput, null, null);

            // Wait for process to exit
            process.waitFor();

            // Extract output
            result = DockerComposeUtilities.extractVersionFromOutput(commandOutput.toString());
        } catch (IOException|InterruptedException|IndexOutOfBoundsException  e){
            System.err.println("Failed to retrieve docker compose version.");
            e.printStackTrace();
        }
        return result;
    }


    private static String extractVersionFromOutput(String versionOutput) {
        // Read the command output, the output should look like
        // docker-compose [VERSION]
        String result = versionOutput.split(versionOutputRegex)[versionOutputPosition];
        return result;
    }
}
