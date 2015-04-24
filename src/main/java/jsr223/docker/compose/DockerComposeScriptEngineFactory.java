package jsr223.docker.compose;


import jsr223.docker.compose.utils.DockerComposeUtilities;
import processbuilder.SingletonProcessBuilderFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import java.io.*;
import java.util.*;

public class DockerComposeScriptEngineFactory implements ScriptEngineFactory {

    // External configuration
    protected static String DOCKER_COMPOSE_CONFIGURATION_FILENAME = "docker-compose.properties";
    protected static String DOCKER_COMPOSE_CONFIGURATION_PATH = ClassLoader
            .getSystemClassLoader().getResource(".").getPath()
            +"config/";

    private static Properties properties = new Properties();

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
    private static final String ENGINE = "docker-compose";
    private static final String ENGINE_VERSION = "0.1";
    private static final String LANGUAGE = "yaml";

    private static final Map<String, Object> parameters = new HashMap<>();

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
            InputStream input = new FileInputStream(
                    DOCKER_COMPOSE_CONFIGURATION_PATH+DOCKER_COMPOSE_CONFIGURATION_FILENAME);
            // Load fields
            properties = new Properties();
            properties.load(input);

            input.close();
        } catch (java.io.IOException e) {
            // No configuration file
            System.err.println("No configuration file for docker-compose script engine found: "
                   + DOCKER_COMPOSE_CONFIGURATION_PATH + DOCKER_COMPOSE_CONFIGURATION_FILENAME);
            System.err.println(e.getMessage());
            System.err.println("Use standard values, execution might not work.");
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

    @Override
    public String getEngineVersion() {
        return (String) parameters.get(ScriptEngine.ENGINE_VERSION);
    }

    @Override
    public List<String> getExtensions() {
        return Arrays.asList("yml", "yaml");
    }

    @Override
    public List<String> getMimeTypes() {
        return Arrays.asList("text/yaml");
    }

    @Override
    public List<String> getNames() {
        return Arrays.asList("docker-compose", "fig");
    }

    @Override
    public String getLanguageName() {
        return (String) parameters.get(ScriptEngine.LANGUAGE);
    }

    @Override
    public String getLanguageVersion() {
        return (String) parameters.get(ScriptEngine.LANGUAGE_VERSION);
    }

    @Override
    public Object getParameter(String key) {
        return parameters.get(key);
    }

    @Override
    public String getMethodCallSyntax(String obj, String m, String... args) {
        throw new NotImplementedException();
    }

    @Override
    public String getOutputStatement(String toDisplay) {
        throw new NotImplementedException();
    }

    @Override
    public String getProgram(String... statements) {
        throw new NotImplementedException();
    }

    @Override
    public ScriptEngine getScriptEngine() {
        return new DockerComposeScriptEngine();
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
