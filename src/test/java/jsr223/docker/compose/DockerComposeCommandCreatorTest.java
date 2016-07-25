package jsr223.docker.compose;

import jsr223.docker.compose.utils.DockerComposePropertyLoader;
import org.junit.Assert;
import org.junit.Test;
import testing.utils.ReflectionUtilities;

import java.lang.reflect.Field;

public class DockerComposeCommandCreatorTest {

    private final DockerComposeCommandCreator dockerCommandCreator = new DockerComposeCommandCreator();

    @Test
    public void testDockerExecutionCommandWithSudo() throws NoSuchFieldException, IllegalAccessException {
        Field useSudoField = ReflectionUtilities.makeFieldAccessible("useSudo",
                DockerComposePropertyLoader.class);
        boolean oldValue = (boolean) useSudoField.get(DockerComposePropertyLoader.getInstance());

        // Run test with sudo true
        useSudoField.set(DockerComposePropertyLoader.getInstance(), true);
        testDockerComposeExecutionCommand();

        // Run test with sudo false
        useSudoField.set(DockerComposePropertyLoader.getInstance(), false);
        testDockerComposeExecutionCommand();

        // Restore value from configuration file
        useSudoField.set(DockerComposePropertyLoader.getInstance(), oldValue);
    }

    /**
     * Test whether the remove command has the right structure.
     *
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    @Test
    public void testDockerComposeDownCommand() throws NoSuchFieldException, IllegalAccessException {
        String[] command = dockerCommandCreator.createDockerComposeDownCommand();
        // Running index; which position of array will currently be checked.
        int index = 0;

        // Check if sudo is added correctly
        index = checkSudoAndComposeCommand(command, index);



        // Check if stop and remove (down) argument is next
        Assert.assertEquals("Down option must be used.",
                dockerCommandCreator.STOP_AND_REMOVE_CONTAINER_ARGUMENT,
                command[index++]);

        // Check if volume argument is next
        Assert.assertEquals("Volume option must be used after down.",
                dockerCommandCreator.VOLUMES_ARGUMENT,
                command[index++]);
    }

    /**
     * Check whether the execution command has the right structure.
     *
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    @Test
    public void testDockerComposeExecutionCommand() throws NoSuchFieldException, IllegalAccessException {
        String[] command = dockerCommandCreator.createDockerComposeExecutionCommand();
        int index = 0;

        // Check if sudo and compose command are added correctly
        index = checkSudoAndComposeCommand(command, index);


        // Check if file argument is used
        Assert.assertEquals("File option must be used.",
                DockerComposeCommandCreator.FILENAME_ARGUMENT,
                command[index++]);

        // Check if correct filename is used
        Assert.assertEquals("Correct filename must be used in command.",
                DockerComposeCommandCreator.YAML_FILE_NAME,
                command[index++]);

        // Check whether correct start command for yaml file is used
        Assert.assertEquals("Correct argument for compose command must be used.",
                DockerComposeCommandCreator.START_CONTAINER_ARGUMENT,
                command[index++]);
    }

    private int checkSudoAndComposeCommand(String[] command, int index) {
        // Check for sudo command
        if (DockerComposePropertyLoader.getInstance().isUseSudo()) {
            Assert.assertEquals("Sudo command must be used when configured.",
                    DockerComposePropertyLoader.getInstance().getSudoCommand(),
                    command[index++]);
        }

        // Check for docker compose command as next command
        Assert.assertEquals("Docker compose command must be used as read from configuration.",
                DockerComposePropertyLoader.getInstance().getDockerComposeCommand(),
                command[index++]);
        return index;
    }
}
