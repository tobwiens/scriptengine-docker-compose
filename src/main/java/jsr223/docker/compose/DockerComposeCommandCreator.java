package jsr223.docker.compose;

import jsr223.docker.compose.utils.DockerComposePropertyLoader;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class DockerComposeCommandCreator {

    // Constants
    @lombok.Getter
    private static String yamlFileName = "docker-compose.yml";
    private static String filenameArgument = "-f";
    private static String stopContainerArgument = "stop";
    private static String removeContainerArgument = "rm";
    private static String removeWithForceContainerArgument = "--force";
    private static String setupContainerArgument = "up";


    /**
     * Construct the docker compose stop command.
     * @return String array representing a command.
     */
    @NotNull
    public static String[] createDockerComposeStopCommand() {
        List<String> command = new ArrayList<>();
        addSudoAndDockerComposeCommand(command);

        command.add(stopContainerArgument);
        return command.toArray(new String[command.size()]);
    }

    /**
     * Construct docker compose remove command.
     * @return String array representing a command.
     */
    @NotNull
    public static String[] createDockerComposeRemoveCommand() {
        List<String> command = new ArrayList<>();
        addSudoAndDockerComposeCommand(command);

        // Remove container with force; otherwise user input is expected.
        command.add(removeContainerArgument);
        command.add(removeWithForceContainerArgument);
        return command.toArray(new String[command.size()]);
    }


    /**
     * This method creates a bash command which starts docker-compose with a given yaml file.
     * @return A String array which contains the command as a separate @String and each
     * argument as a separate String.
     */
    @NotNull
    public static String[] createDockerComposeExecutionCommand() {
        List<String> command = new ArrayList<>();
        addSudoAndDockerComposeCommand(command);


        // Add filename argument
        command.add(filenameArgument);

        // Add filename
        command.add(yamlFileName);

        // Start container with argument
        command.add(setupContainerArgument);
        return command.toArray(new String[command.size()]);
    }

    /**
     * Adds sudo and docker compose command to the given list. Sudo is only added when
     * it is configured to do that.
     * @param command List which gets the command(s) added.
     */
    private static void addSudoAndDockerComposeCommand(@NotNull List<String> command) {
        // Add sudo if necessary
        if (DockerComposePropertyLoader.getInstance().isUseSudo()) {
            command.add(DockerComposePropertyLoader.getInstance().getSudoCommand());
        }

        // Add docker compose command
        command.add(DockerComposePropertyLoader.getInstance().getDockerComposeCommand());
    }
}
