package jsr223.docker.compose;

import org.junit.Assert;
import org.junit.Test;
import testing.utils.ReflectionUtilities;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created on 4/23/2015.
 */
public class DockerComposeCommandCreatorTest {

    private static String mockYamlFile = "mysqldb:\n  image: mysql:latest \n  environment:\n    MYSQL_DATABASE: sample";

    /**
     * Thist test case checks whether the yaml file given is contained in the command.
     * In order to create a proper yaml file each line of the given String must be contained exactly once.
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    @Test
    public void checkYamlFileContainedInCommand() throws NoSuchFieldException, IllegalAccessException {
        // Create command line
        List<String> result = Arrays.asList(DockerComposeCommandCreator
                .createDockerComposeFileCreationCommand(mockYamlFile));

        // List all yaml file lines
        List<String> containedLines = new LinkedList<>(Arrays.asList(mockYamlFile));

        for (String yamlLine : containedLines) {
            Assert.assertEquals("Line of yaml file must be contained exactly 1 time.",
                    1,
                    containedInArray(result, yamlLine));
        }
    }

    /**
     * The yaml file which is given to the docker-compose is created by a separate command, which is tested here.
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    @Test
    public void checkFileCreateStartAndEndContainedInCommand() throws NoSuchFieldException, IllegalAccessException {
        // Create command line
        List<String> result = Arrays.asList(DockerComposeCommandCreator
                .createDockerComposeFileCreationCommand(mockYamlFile));

        // Get private fields
        Field fileCreateStartField = ReflectionUtilities.makeFieldAccessible("bashCreateYamlFileCommandStart",
                DockerComposeCommandCreator.class);
        Field fileCreateEndField = ReflectionUtilities.makeFieldAccessible("bashCreateYamlFileCommandEnd",
                DockerComposeCommandCreator.class);

        // Extract strings
        String fileCreateStart = (String) fileCreateStartField.get(new DockerComposeCommandCreator());
        String fileCreateEnd = (String) fileCreateEndField.get(new DockerComposeCommandCreator());

        Assert.assertEquals("The file must be created using the static fields in the class.",
                1,
                containedInArray(result, fileCreateStart));

        Assert.assertEquals("The file must be created using the static fields in the class.",
                1,
                containedInArray(result, fileCreateEnd));
    }
    @Test
    public void testDockerExecutionCommandWithSudo() throws NoSuchFieldException, IllegalAccessException {
        Field useSudoField = ReflectionUtilities.makeFieldAccessible("useSudo",
                DockerComposeScriptEngineFactory.class);
        boolean oldValue = (boolean) useSudoField.get(new DockerComposeScriptEngineFactory());

        // Run test with sudo true
        useSudoField.set(new DockerComposeScriptEngineFactory(), true);
        testDockerComposeExecutionCommand();

        // Run test with sudo false
        useSudoField.set(new DockerComposeScriptEngineFactory(), false);
        testDockerComposeExecutionCommand();

        // Restore value from configuration file
        useSudoField.set(new DockerComposeScriptEngineFactory(), oldValue);
    }

    @Test
    public void testDockerComposeExecutionCommand() throws NoSuchFieldException, IllegalAccessException {
        String[] command = DockerComposeCommandCreator.createDockerComposeExecutionCommandBash();
        int index = 0;

        // Check for sudo command
        if (DockerComposeScriptEngineFactory.isUseSudo()) {
            Assert.assertEquals("Sudo command must be used when configured.",
                    DockerComposeScriptEngineFactory.getSudoCommand(),
                    command[index++]);
        }

        // Check for docker compose command as next command
        Assert.assertEquals("Docker compose command must be used as read from configuration.",
                DockerComposeScriptEngineFactory.getDockerComposeCommand(),
                command[index++]);

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

    /**
     * Counts how often one specific string is contained in a list of strings.
     * @param array List of strings.
     * @param containedString String to look for in the array.
     * @return In how many strings, of the given array, the search string can be found.
     */
    private int containedInArray(List<String> array, String containedString) {
        int result = 0;

        for (String element : array) {
            if (element.contains(containedString)) {
                result += 1;
            }
        }
        return result;
    }
}
