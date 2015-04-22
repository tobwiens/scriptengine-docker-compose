package jsr223.docker.compose;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Created on 4/21/2015.
 */
public class DockerComposeScriptEngineFactoryTest {

    @Test
    public void testDefaultConfiguration() throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        // Set an invalid configuration file path
        Field configPath = DockerComposeScriptEngineFactory.class
                .getDeclaredField("DOCKER_COMPOSE_CONFIGURATION_FILE_PATH");
        configPath.setAccessible(true);
        // Remove final modifier
        Field configPathModifier = Field.class.getDeclaredField("modifiers");
        configPathModifier.setAccessible(true);
        configPathModifier.set(configPath, configPath.getModifiers() & ~Modifier.FINAL);
        // Save old value and set new vale
        String oldConfigPath = (String) configPath.get(new DockerComposeScriptEngine());
        configPath.set(new DockerComposeScriptEngine(), "");

        // Call method to reset and reload properties
        Method reloadProperties = DockerComposeScriptEngineFactory.class
                .getDeclaredMethod("loadConfigFileIntoProperties", null);
        reloadProperties.setAccessible(true);
        reloadProperties.invoke(new DockerComposeScriptEngine(), null);

        // Call static method to load from configuration file
        Method resetConstants = DockerComposeScriptEngineFactory.class
                .getDeclaredMethod("assignDockerComposeConstants", null);

        // Check whether all values are set to default
        checkExpectedValueOfProperty("PROP_DOCKER_COMPOSE_COMMAND_DEFAULT",
                DockerComposeScriptEngineFactory.getDockerComposeCommand());
        checkExpectedValueOfProperty("PROP_SUDO_COMMAND_DEFAULT",
                DockerComposeScriptEngineFactory.getSudoCommand());
        checkExpectedValueOfProperty("PROP_DOCKER_COMPOSE_USE_SUDO_DEFAULT",
                DockerComposeScriptEngineFactory.isUseSudo()?"true":"false"); // Convert to String

        // Restore old values
        configPath.set(new DockerComposeScriptEngine(), oldConfigPath);
        reloadProperties.invoke(new DockerComposeScriptEngine(), null);

    }

    private void checkExpectedValueOfProperty(String propertyName, Object expectedValueComposeCommand) throws NoSuchFieldException, IllegalAccessException {
        Field commandDefault = DockerComposeScriptEngineFactory.
                class.getDeclaredField(propertyName);
        commandDefault.setAccessible(true);
        Assert.assertEquals("Default configuration must be applied when no config " +
                        "file was found.", expectedValueComposeCommand,
                commandDefault.get(new DockerComposeScriptEngineFactory()));
    }


}
