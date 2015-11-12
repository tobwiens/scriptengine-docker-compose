package jsr223.docker.compose;

import jsr223.docker.compose.utils.DockerComposePropertyLoader;
import org.junit.Assert;
import org.junit.Test;
import testing.utils.ReflectionUtilities;

import java.lang.reflect.Field;
import java.util.List;

public class DockerComposeCommandCreatorTest {

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
     * Test whether the stop command has the right structure.
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    @Test
    public void testDockerComposeStopCommand() throws NoSuchFieldException, IllegalAccessException {
        String[] command = DockerComposeCommandCreator.createDockerComposeStopCommand();
        // Running index; which position of array will currently be checked.
        int index = 0;

        // Check if sudo is added correctly
        index = checkSudoAndComposeCommand(command, index);

        // Check if stop argument is next
        Assert.assertEquals("Stop option must be used.",
                ReflectionUtilities.makeFieldAccessible("stopContainerArgument",
                        DockerComposeCommandCreator.class).get(new DockerComposeCommandCreator()),
        command[index++]);
    }

    /**
     * Test whether the remove command has the right structure.
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    @Test
    public void testDockerComposeRemoveCommand() throws NoSuchFieldException, IllegalAccessException {
        String[] command = DockerComposeCommandCreator.createDockerComposeRemoveCommand();
        // Running index; which position of array will currently be checked.
        int index = 0;

        // Check if sudo is added correctly
        index = checkSudoAndComposeCommand(command, index);

        // Check if stop argument is next
        Assert.assertEquals("Stop option must be used.",
                ReflectionUtilities.makeFieldAccessible("removeContainerArgument",
                        DockerComposeCommandCreator.class).get(new DockerComposeCommandCreator()),
                command[index++]);

        // Check if stop argument is next
        Assert.assertEquals("Stop option must be use force argument.",
                ReflectionUtilities.makeFieldAccessible("removeWithForceContainerArgument",
                        DockerComposeCommandCreator.class).get(new DockerComposeCommandCreator()),
                command[index++]);
    }

    /**
     * Check whether the exeuction command has the right structure.
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    @Test
    public void testDockerComposeExecutionCommand() throws NoSuchFieldException, IllegalAccessException {
        String[] command = DockerComposeCommandCreator.createDockerComposeExecutionCommand();
        int index = 0;

        // Check if sudo and compose command are added correctly
        index = checkSudoAndComposeCommand(command, index);


        // Check if file argument is used
        Assert.assertEquals("File option must be used.",
                ReflectionUtilities.makeFieldAccessible("filenameArgument",
                        DockerComposeCommandCreator.class).get(new DockerComposeCommandCreator()),
                command[index++]);

        // Check if correct filename is used
        Assert.assertEquals("Correct filename must be used in command.",
                ReflectionUtilities.makeFieldAccessible("yamlFileName",
                        DockerComposeCommandCreator.class).get(new DockerComposeCommandCreator()),
                command[index++]);

        // Check whether correct start command for yaml file is used
        Assert.assertEquals("Correct argument for compose command must be used.",
                ReflectionUtilities.makeFieldAccessible("setupContainerArgument",
                        DockerComposeCommandCreator.class).get(new DockerComposeCommandCreator()),
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
