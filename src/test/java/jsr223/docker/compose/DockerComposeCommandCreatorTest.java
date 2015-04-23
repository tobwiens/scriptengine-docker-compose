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

    private static String[] mockYamlFile = {"mysqldb:","  image: mysql:latest",
    "  environment:", "    MYSQL_DATABASE: sample"};

    /**
     * Thist test case checks whether the yaml file given is contained in the command.
     * In order to succeed each line of the yaml file must be contained exactly one time.
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    @Test
    public void checkYamlFileContainedInCommand() throws NoSuchFieldException, IllegalAccessException {
        // Create command line
        List<String> result = Arrays.asList(DockerComposeCommandCreator
                .createDockerComposeExecutionCommandBash(mockYamlFile));

        // List all yaml file lines
        List<String> containedLines = new LinkedList<>(Arrays.asList(mockYamlFile));

        for (String yamlLine : containedLines) {
            Assert.assertEquals("Line of yaml file must be contained exactly 1 time.",
                    1,
                    containedInArray(result, yamlLine));
        }
    }

    /**
     * The yaml file which is given to the docker-compose command has a prefix and a postfix. This test
     * checks whether the prefix and postfix are contained in the command, as specified in the class.
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    @Test
    public void checkFileStartAndEndContainedInCommand() throws NoSuchFieldException, IllegalAccessException {
        // Create command line
        List<String> result = Arrays.asList(DockerComposeCommandCreator
                .createDockerComposeExecutionCommandBash(mockYamlFile));

        // Get private fields
        Field fileStartField = ReflectionUtilities.makeFieldAccessible("bashComposeFileStart",
                DockerComposeCommandCreator.class);
        Field fileEndField = ReflectionUtilities.makeFieldAccessible("bashComposeFileEnd",
                DockerComposeCommandCreator.class);

        // Extract strings
        String fileStart = (String) fileStartField.get(new DockerComposeCommandCreator());
        String fileEnd = (String) fileEndField.get(new DockerComposeCommandCreator());

        Assert.assertEquals("The file start operator must be exactly one time in command",
                1,
                containedInArray(result, fileStart));

        Assert.assertEquals("The file end operator must be exactly one time in command",
                1,
                containedInArray(result, fileEnd));
    }

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
