package jsr223.docker.compose.yaml;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.script.ScriptException;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;

public class VariablesReplacerTest {

    final static String yamlFileWithVariables = "$name:\n"
            + "    image: $container\n"
            + "    command: echo \"$greetings\"";

    final static String yamlFileExpected = "EchoUbuntu:\n"
            + "    image: dockerfile/ubuntu\n"
            + "    command: echo \"Hello World\"";


    @Test
    public void testVariableSubstitution() throws ScriptException, IOException {
        VariablesReplacer variablesReplacer = new VariablesReplacer();
        Map<String, String> variableMap = new HashMap<>();
        variableMap.put("name", "EchoUbuntu");
        variableMap.put("container", "dockerfile/ubuntu");
        variableMap.put("greetings", "Hello World");

        String stringVariablesReplaced = variablesReplacer.replaceVariables(yamlFileWithVariables,
                variableMap);

        assertThat(stringVariablesReplaced, is(yamlFileExpected));

    }

    @Test
    public void testScriptIsNull() throws ScriptException, IOException {
        VariablesReplacer variablesReplacer = new VariablesReplacer();
        Map<String, String> variableMap = new HashMap<>();
        variableMap.put("name", "EchoUbuntu");
        variableMap.put("container", "dockerfile/ubuntu");
        variableMap.put("greetings", "Hello World");

        String stringVariablesReplaced = variablesReplacer.replaceVariables(null, variableMap);

        assertThat(stringVariablesReplaced == null, is(true));
    }

    @Test
    public void testVariablesIsNull() throws ScriptException, IOException {
        VariablesReplacer variablesReplacer = new VariablesReplacer();

        String stringVariablesReplaced = variablesReplacer.replaceVariables(yamlFileWithVariables, null);

        assertThat(stringVariablesReplaced, is(yamlFileWithVariables));
    }

    @Test
    public void testScriptAndVariablesIsNull() throws ScriptException, IOException {
        VariablesReplacer variablesReplacer = new VariablesReplacer();

        String stringVariablesReplaced = variablesReplacer.replaceVariables(null, null);

        assertThat(stringVariablesReplaced == null, is(true));
    }

    @Test
    public void testKeyIsNull() throws ScriptException, IOException {
        VariablesReplacer variablesReplacer = new VariablesReplacer();
        Map<String, String> variableMap = new HashMap<>();
        variableMap.put(null, "EchoUbuntu");
        variableMap.put("container", "dockerfile/ubuntu");
        variableMap.put("greetings", "Hello World");

        String stringVariablesReplaced = variablesReplacer.replaceVariables(yamlFileWithVariables,
                variableMap);

        // Name will not be replaced
        assertThat(stringVariablesReplaced, containsString("$name:"));
    }

    @Test
    public void testValueIsNull() throws ScriptException, IOException {
        VariablesReplacer variablesReplacer = new VariablesReplacer();
        Map<String, String> variableMap = new HashMap<>();
        variableMap.put("name", null);
        variableMap.put("container", "dockerfile/ubuntu");
        variableMap.put("greetings", "Hello World");

        String stringVariablesReplaced = variablesReplacer.replaceVariables(yamlFileWithVariables,
                variableMap);

        // Name will not be replaced
        assertThat(stringVariablesReplaced, containsString("$name:"));
    }
}