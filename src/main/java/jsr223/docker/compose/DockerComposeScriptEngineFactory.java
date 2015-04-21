package jsr223.docker.compose;


import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DockerComposeScriptEngineFactory implements ScriptEngineFactory {

    private static final String NAME = "docker-compose";
    private static final String ENGINE = "docker-compose yaml file";
    private static final String ENGINE_VERSION = ""; // TODO: Create versions
    private static final String LANGUAGE = "Bash"; // TODO: correct language
    private static final String LANGUAGE_VERSION = "not existing"; // TODO: Add correct version

    private static final Map<String, Object> parameters = new HashMap<String, Object>();

    static {
        parameters.put(ScriptEngine.NAME, NAME);
        parameters.put(ScriptEngine.ENGINE, ENGINE);
        parameters.put(ScriptEngine.ENGINE_VERSION, ENGINE_VERSION);
        parameters.put(ScriptEngine.LANGUAGE, LANGUAGE);
        parameters.put(ScriptEngine.LANGUAGE_VERSION, LANGUAGE_VERSION);
    }

    // TODO: Implement
    @Override
    public String getEngineName() {
        return null;
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
}
