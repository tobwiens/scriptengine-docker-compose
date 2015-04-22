package jsr223.docker.compose;


import jsr223.docker.compose.utils.DockerComposeUtilities;
import processbuilder.SingletonProcessBuilderFactory;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class DockerComposeScriptEngineFactory implements ScriptEngineFactory {

    // External configuration
    private static final String DOCKER_COMPOSE_CONFIGURATION_FILE_PATH = "config/docker-compose.properties";

    private static Properties properties = new Properties();
    private static InputStream input = null;

    // Configuration keys
    private static final String PROP_DOCKER_COMPOSE_COMMAND = "docker.compose.command";
    private static final String PROP_DOCKER_COMPOSE_COMMAND_DEFAULT = "/usr/local/bin/docker-compose";
    private static final String PROP_SUDO_COMMAND = "sudo.command";
    private static final String PROP_SUDO_COMMAND_DEFAULT = "/usr/bin/sudo";
    private static final String PROP_DOCKER_COMPOSE_USE_SUDO = "docker.compose.use.sudo";
    private static final String PROP_DOCKER_COMPOSE_USE_SUDO_DEFAULT = "false";


    // Docker compose configuration - are initialized when loaded into memory; static { }.
    private static String dockerComposeCommand;
    private static String sudoCommand;
    private static boolean useSudo;

    // Script engine parameters
    private static final String NAME = "docker-compose";
    private static final String ENGINE = "docker-compose yaml file";
    private static final String ENGINE_VERSION = ""; // TODO: Create versions
    private static final String LANGUAGE = "yaml"; // TODO: correct language

    private static final Map<String, Object> parameters = new HashMap<String, Object>();

    // TODO: Read command from config file
    static {
        
        // Load config file and save into props
        loadConfigFileIntoProperties();

        // Read from properties and assign it to public cons
        DockerComposeScriptEngineFactory.assignDockerComposeConstants();

        // Write script engine parameters
        parameters.put(ScriptEngine.NAME, NAME);
        parameters.put(ScriptEngine.ENGINE, ENGINE);
        parameters.put(ScriptEngine.ENGINE_VERSION, ENGINE_VERSION);
        parameters.put(ScriptEngine.LANGUAGE, LANGUAGE);
        parameters.put(ScriptEngine.LANGUAGE_VERSION, DockerComposeUtilities.
                    getDockerComposeVersion(DockerComposeScriptEngineFactory.dockerComposeCommand,
                            SingletonProcessBuilderFactory.getInstance()));
    }

    /**
     * Resets properties and loads them from configuration a configuration file
     */
    private static void loadConfigFileIntoProperties() {
        try {
            // Open configuration file
            input = new FileInputStream(DOCKER_COMPOSE_CONFIGURATION_FILE_PATH);
            // Load fields
            properties = new Properties();
            properties.load(input);


        } catch (java.io.IOException e) {
            // No configuration file
            System.err.println("No configuration file for docker-compose script engine.");
            System.err.println("Use standard values, execution might not work.");
            e.printStackTrace();
        }
    }

    /**
     * Assigns docker compose constant by reading them from properties or using
     * a default value.
     */
    private static void assignDockerComposeConstants() {
        DockerComposeScriptEngineFactory.dockerComposeCommand =
                properties.getProperty(PROP_DOCKER_COMPOSE_COMMAND,
                        PROP_DOCKER_COMPOSE_COMMAND_DEFAULT);
        DockerComposeScriptEngineFactory.sudoCommand =
                properties.getProperty(PROP_SUDO_COMMAND,
                        PROP_SUDO_COMMAND_DEFAULT);
        DockerComposeScriptEngineFactory.useSudo =
                Boolean.parseBoolean(properties.getProperty(PROP_DOCKER_COMPOSE_USE_SUDO,
                        PROP_DOCKER_COMPOSE_USE_SUDO_DEFAULT));
    }


    @Override
    public String getEngineName() {
        return (String) parameters.get(ScriptEngine.NAME);
    }
    // TODO: Implement
    @Override
    public String getEngineVersion() {
        return null;
    }
    // TODO: Implement
    @Override
    public List<String> getExtensions() {
        return null;
    }
    // TODO: Implement
    @Override
    public List<String> getMimeTypes() {
        return null;
    }
    // TODO: Implement
    @Override
    public List<String> getNames() {
        return null;
    }
    // TODO: Implement
    @Override
    public String getLanguageName() {
        return null;
    }
    // TODO: Implement
    @Override
    public String getLanguageVersion() {
        return null;
    }
    // TODO: Implement
    @Override
    public Object getParameter(String key) {
        return null;
    }
    // TODO: Implement
    // TODO: Test
    @Override
    public String getMethodCallSyntax(String obj, String m, String... args) {
        return null;
    }
    // TODO: Implement
    @Override
    public String getOutputStatement(String toDisplay) {
        return null;
    }
    // TODO: Implement
    @Override
    public String getProgram(String... statements) {
        return null;
    }
    // TODO: Implement
    @Override
    public ScriptEngine getScriptEngine() {
        return null;
    }

    /**
     * GETTERS FOR CONSTANTS HOW TO USE THE DOCKER COMPOSE COMMAND
     */
    public static String getDockerComposeCommand() {
        return dockerComposeCommand;
    }

    public static String getSudoCommand() {
        return sudoCommand;
    }

    public static boolean isUseSudo() {
        return useSudo;
    }
}
