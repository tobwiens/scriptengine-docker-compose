package jsr223.docker.compose;

import jsr223.docker.compose.utils.DockerComposeUtilities;
import processbuilder.SingletonProcessBuilderFactory;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DockerComposeScriptEngineFactory implements ScriptEngineFactory {

    // Script engine parameters
    private static final String NAME = "docker-compose";
    private static final String ENGINE = "docker-compose";
    private static final String ENGINE_VERSION = "0.0.1";
    private static final String LANGUAGE = "yaml";

    private static final Map<String, Object> parameters = new HashMap<>();

    public DockerComposeScriptEngineFactory() {
        // This is the entrypoint of the script engine
        // configure the logger here, quick and dirty
        org.apache.log4j.PropertyConfigurator.configure(getClass()
        .getClassLoader().getResourceAsStream("config/log/scriptengines.properties"));

        parameters.put(ScriptEngine.NAME, this.NAME);
        parameters.put(ScriptEngine.ENGINE_VERSION, this.ENGINE_VERSION);
        parameters.put(ScriptEngine.LANGUAGE, this.LANGUAGE);
        parameters.put(ScriptEngine.ENGINE, this.ENGINE);
        parameters.put(ScriptEngine.LANGUAGE_VERSION,
                DockerComposeUtilities.
                getDockerComposeVersion(SingletonProcessBuilderFactory.getInstance()));

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
        return null;
    }

    @Override
    public String getOutputStatement(String toDisplay) {
        return null;
    }

    @Override
    public String getProgram(String... statements) {
        return null;
    }

    @Override
    public ScriptEngine getScriptEngine() {
        return new DockerComposeScriptEngine();
    }
}
