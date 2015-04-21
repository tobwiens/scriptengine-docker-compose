package jsr223.docker.compose;

import javax.script.*;
import java.io.Reader;

/**
 * Created on 4/21/2015.
 */
public class DockerComposeScriptEngine extends AbstractScriptEngine {

    @Override
    public Object eval(String script, ScriptContext context) throws ScriptException {
        return null;
    }

    @Override
    public Object eval(Reader reader, ScriptContext context) throws ScriptException {
        return null;
    }

    @Override
    public Bindings createBindings() {
        return null;
    }

    @Override
    public ScriptEngineFactory getFactory() {
        return null;
    }
}
