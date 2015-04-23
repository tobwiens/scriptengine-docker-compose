package jsr223.docker.compose;

import org.junit.Assert;
import org.junit.Test;
import testing.utils.ReflectionUtilities;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created on 4/23/2015.
 */
public class DockerComposeCommandCreatorTest {

    private static String[] mockYamlFile = {"mysqldb:","  image: mysql:latest",
    "  environment:", "    MYSQL_DATABASE: sample"};

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


    private List<String> splitWithLineSeparator(List<String> stringArray) throws NoSuchFieldException, IllegalAccessException {
        ArrayList<String> result = new ArrayList<>();

        // Get line separator
        Field lineSeparatorField = ReflectionUtilities.makeFieldAccessible("lineSeparator",
                DockerComposeCommandCreator.class);
        String lineSeparator = (String) lineSeparatorField.get(new DockerComposeCommandCreator());
        for (String element : stringArray) {
            result.addAll(Arrays.asList(element.split(lineSeparator)));
        }
        return result;
    }
}
