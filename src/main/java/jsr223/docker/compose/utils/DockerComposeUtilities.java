package jsr223.docker.compose.utils;

import processbuilder.ProcessBuilderFactory;

import java.io.IOException;

/**
 * Created on 4/21/2015.
 */
public class DockerComposeUtilities {

    // TODO: Test
    /**
     * Retrieves the docker compose version.
     * @param command The docker compose command.
     * @return The currently installed version return by the docker compose command.
     */
    public static String getDockerComposeVersion(String command, ProcessBuilderFactory factory) {
        String result = ""; // Empty string for empty result if version recovery fails

        ProcessBuilder pb = factory.getInstance().getProcessBuilder(new String[] {command, "--version"});

        try {
            Process process = pb.start();
            process.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }
}
