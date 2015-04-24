package jsr223.docker.compose.utils;

import org.junit.Assert;
import org.junit.Test;
import processbuilder.ProcessBuilderFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * Created on 4/21/2015.
 */
public class DockerComposeUtilitiesTest {

    public final static String[] expectedInput = {"docker-compose", "--version"};
    public final static String firstPartOutput = "docker-compose";
    public final static String secondPartOutput = "1.1.0";
    public final static String output = firstPartOutput+" "+secondPartOutput;

    private class VersionProcessBuilderFactory implements ProcessBuilderFactory {




        @Override
        public ProcessBuilder getProcessBuilder(String... command) {

            int index = 0;
            for (String element: expectedInput) {
                Assert.assertTrue("Command must match "+element, element.equals(command[index++]));
            }

            return new ProcessBuilder();
        }
    }

    /**
     * This test is checking whether the right command is given to the process builder.
     * The ProcessBuilder class is final and cannot be mocked therefore the output will
     * be tested differently and an Exception is excepted here.
     */
    @Test
    public void dockerComposeVersionTest(){
        try {
            System.out.println("The ArrayIndexOutOfBoundsException was expected to be thrown....");
            DockerComposeUtilities.getDockerComposeVersion(expectedInput[0], new VersionProcessBuilderFactory());
        } catch( Exception e) {
            // Exceptions will be thrown for sure!!! The only part tested here is if the command
            // constructed correctly and given to the ProcessBuilderFactory
        }
    }


    /**
     * This test mocks the output of the compose --version command and checks whether it is extracted
     * correctly using the extractMethod and the regex.
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
    @Test
    public void dockerComposeVersionRegexTest() throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        // Check if the regex and index work when given the expected output
        Field regexString = DockerComposeUtilities.class.getDeclaredField("versionOutputRegex");
        Field positionInt = DockerComposeUtilities.class.getDeclaredField("versionOutputPosition");

        // Make accessible
        regexString.setAccessible(true);
        positionInt.setAccessible(true);

        // Get extraction method
        Method extractWithRegex = DockerComposeUtilities.class.getDeclaredMethod("extractVersionFromOutput", String.class);
        extractWithRegex.setAccessible(true);

        // Call method which checks with regex
        Assert.assertTrue("Version number must match: "+secondPartOutput,
                extractWithRegex.invoke(new DockerComposeUtilitiesTest(),output).equals(secondPartOutput));
    }
}
