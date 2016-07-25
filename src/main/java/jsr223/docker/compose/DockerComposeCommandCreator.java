package jsr223.docker.compose;

import jsr223.docker.compose.utils.DockerComposePropertyLoader;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class DockerComposeCommandCreator {

    // Constants
    public static final String YAML_FILE_NAME = "docker-compose.yml";
    public static final String FILENAME_ARGUMENT = "-f";
    public static final String START_CONTAINER_ARGUMENT = "up";
    public static final String STOP_AND_REMOVE_CONTAINER_ARGUMENT = "down";
    public static final String VOLUMES_ARGUMENT = "--volumes";


    /**
     * Construct docker compose down command.
     *
     * @return String array representing a command.
     */
    public String[] createDockerComposeDownCommand() {
        List<String> command = new ArrayList<>();
        addSudoAndDockerComposeCommand(command);

        // Stop and remove containers
        command.add(STOP_AND_REMOVE_CONTAINER_ARGUMENT);
        // Remove volumes with containers
        command.add(VOLUMES_ARGUMENT);
        return command.toArray(new String[command.size()]);
    }


    /**
     * This method creates a bash command which starts docker-compose with a given yaml file.
     *
     * @return A String array which contains the command as a separate @String and each
     * argument as a separate String.
     */
    public String[] createDockerComposeExecutionCommand() {
        List<String> command = new ArrayList<>();
        addSudoAndDockerComposeCommand(command);


        // Add filename argument
        command.add(FILENAME_ARGUMENT);

        // Add filename
        command.add(YAML_FILE_NAME);

        // Start container with argument
        command.add(START_CONTAINER_ARGUMENT);
        return command.toArray(new String[command.size()]);
    }

    /**
     * Adds sudo and docker compose command to the given list. Sudo is only added when
     * it is configured to do that.
     *
     * @param command List which gets the command(s) added.
     */
    private void addSudoAndDockerComposeCommand(List<String> command) {
        // Add sudo if necessary
        if (DockerComposePropertyLoader.getInstance().isUseSudo()) {
            command.add(DockerComposePropertyLoader.getInstance().getSudoCommand());
        }

        // Add docker compose command
        command.add(DockerComposePropertyLoader.getInstance().getDockerComposeCommand());
    }
}
