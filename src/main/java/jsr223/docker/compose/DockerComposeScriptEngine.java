package jsr223.docker.compose;

import processbuilder.SingletonProcessBuilderFactory;

import javax.script.*;
import java.io.Reader;

/**
 * Created on 4/21/2015.
 */
public class DockerComposeScriptEngine extends AbstractScriptEngine {
    // TODO: Implement
    // TODO: Test
    @Override
    public Object eval(String script, ScriptContext context) throws ScriptException {
        return null;
    }
    // TODO: Implement
    // TODO: Test
    @Override
    public Object eval(Reader reader, ScriptContext context) throws ScriptException {

        ProcessBuilder processBuilder = SingletonProcessBuilderFactory.getInstance().getProcessBuilder();

        Bindings scriptBindings = context.getBindings(ScriptContext.ENGINE_SCOPE);

        return null;
    }

    @Override
    public Bindings createBindings() {

        return new SimpleBindings();
    }
    // TODO: Implement
    // TODO: Test
    @Override
    public ScriptEngineFactory getFactory() {
        return null;
    }
}
