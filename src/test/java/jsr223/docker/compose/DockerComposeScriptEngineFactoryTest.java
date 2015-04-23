package jsr223.docker.compose;

import org.junit.Assert;
import org.junit.Test;
import testing.utils.ReflectionUtilities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

/**
 * Created on 4/21/2015.
 */
public class DockerComposeScriptEngineFactoryTest {

    /**
     * Tests the dockerComposeScriptEngineFactory static block initialization. By invoking
     * two methods which are used for initializations. The first one loads properties from
     * a file and the second one assigns those properties to variables which are accessible
     * through getters. Default values are provided and if no configuration file was found
     * those will be used. That default value behavior is tested in this class.
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
    @Test
    public void testDefaultConfiguration() throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        String configurationFilePath = "";
        String expectedComposeCommand = DockerComposeScriptEngineFactory.getDockerComposeCommand();
        String expectedSudoCommand = DockerComposeScriptEngineFactory.getSudoCommand();
        String expectedUsageOfSudo = DockerComposeScriptEngineFactory.isUseSudo()?"true":"false";
        checkAssignmentOfConfigFile(configurationFilePath, expectedComposeCommand, expectedSudoCommand, expectedUsageOfSudo);
    }

    /**
     * Test whether values are reset. The default value test might be executed before the own value test,
     * in that case it is not tested whether the memory is really reset. That is because the default values
     * are used when the properties are empty, when the properties are then filled with own values both test
     * might succeed. Therefore those tests are executed several times to check whether the properties
     * are really reset.
     * @throws NoSuchMethodException
     * @throws NoSuchFieldException
     * @throws IOException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    @Test
    public void testResetOfConfigurationFile() throws NoSuchMethodException, NoSuchFieldException, IOException, IllegalAccessException, InvocationTargetException {
        testOwnConfiguration();
        testDefaultConfiguration();
        testOwnConfiguration();
        testDefaultConfiguration();
    }

    @Test
    public void testOwnConfiguration() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, NoSuchFieldException, IOException {
        String composeCommandValue = "TestCommand-001";
        String sudoCommandValue = "TestCommand-002";
        String usageSudo = "ThisShouldBeABoolean";
        String ownConfigFile = "freakyOptions.properties";

        Properties ownProperties = new Properties();
        Field composeCommandField = ReflectionUtilities.makeFieldAccessible("PROP_DOCKER_COMPOSE_COMMAND",
                DockerComposeScriptEngineFactory.class);
        Field sudoCommandField = ReflectionUtilities.makeFieldAccessible("PROP_SUDO_COMMAND",
                DockerComposeScriptEngineFactory.class);
        Field useSudoField = ReflectionUtilities.makeFieldAccessible("PROP_DOCKER_COMPOSE_USE_SUDO",
                DockerComposeScriptEngineFactory.class);

        ownProperties.setProperty((String) composeCommandField.get(new DockerComposeScriptEngineFactory()),
                composeCommandValue);
        ownProperties.setProperty((String) sudoCommandField.get(new DockerComposeScriptEngineFactory()),
                sudoCommandValue);
        ownProperties.setProperty((String) useSudoField.get(new DockerComposeScriptEngineFactory()),
                usageSudo);

        // Open a file stream and write properties to it
        File configFile = new File(ownConfigFile);
        if (configFile.exists()) {
            //noinspection ResultOfMethodCallIgnored
            configFile.delete();
        }
        //noinspection ResultOfMethodCallIgnored
        configFile.createNewFile();
        OutputStream output = new FileOutputStream(configFile);

        // Write configuration file
        ownProperties.store(output, "Testing Configuration");

        checkAssignmentOfConfigFile(ownConfigFile, composeCommandValue,
                sudoCommandValue, usageSudo);

        // Clean up
        output.close();
        //noinspection ResultOfMethodCallIgnored
        configFile.delete();
    }

    /**
     * This method checks whether given a configuration file the properties are read correctly
     * and assigned to static variables which can then be accessed through getters.
     * @param configurationFilePath File path to configuration file.
     * @param expectedComposeCommand Docker compose command.
     * @param expectedSudoCommand Sudo command.
     * @param expectedUsageOfSudo If sudo should be used.
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
    private void checkAssignmentOfConfigFile(String configurationFilePath, String expectedComposeCommand, String expectedSudoCommand, String expectedUsageOfSudo) throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Field configPath = DockerComposeScriptEngineFactory.class
                .getDeclaredField("DOCKER_COMPOSE_CONFIGURATION_FILE_PATH");
        configPath.setAccessible(true);

        // Save old value and set new vale
        String oldConfigPath = (String) configPath.get(new DockerComposeScriptEngine());
        configPath.set(new DockerComposeScriptEngine(), configurationFilePath);

        // Call method to reset and reload properties
        @SuppressWarnings("NullArgumentToVariableArgMethod")
        Method reloadProperties = DockerComposeScriptEngineFactory.class
                .getDeclaredMethod("loadConfigFileIntoProperties", null);
        reloadProperties.setAccessible(true);
        //noinspection NullArgumentToVariableArgMethod
        reloadProperties.invoke(new DockerComposeScriptEngineFactory());

        // Call static method to load from configuration file
        @SuppressWarnings("NullArgumentToVariableArgMethod")
        Method resetConstants = DockerComposeScriptEngineFactory.class
                .getDeclaredMethod("assignDockerComposeConstants", null);
        resetConstants.setAccessible(true);
        resetConstants.invoke(new DockerComposeScriptEngineFactory());

        // Check whether all values are set to the expected values
        Assert.assertEquals("Value must be as expected.",
                expectedComposeCommand,
                DockerComposeScriptEngineFactory.getDockerComposeCommand());
        Assert.assertEquals("Value must be as expected.",
                expectedSudoCommand,
                DockerComposeScriptEngineFactory.getSudoCommand());
        Assert.assertEquals("Value must be as expected.",
                Boolean.parseBoolean(expectedUsageOfSudo),
                DockerComposeScriptEngineFactory.isUseSudo());


        // Restore old values
        configPath.set(new DockerComposeScriptEngineFactory(), oldConfigPath);
        //noinspection NullArgumentToVariableArgMethod
        reloadProperties.invoke(new DockerComposeScriptEngine(), null);
    }

}
