package jsr223.docker.compose.bindings;

import org.junit.Assert;
import org.junit.Test;

import javax.script.ScriptException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.hasEntry;
import static org.junit.Assert.assertThat;

public class MapBindingsAdderTest {

    @Test
    public void testAddEntryToEnvironmentOtherThanPureStringsDoesntThrowNullpointer() throws Exception {
        MapBindingsAdder mapBindingsAdder = new MapBindingsAdder();
        Map<String, String> environment = new HashMap<>();

        // Construct Map which contains null values
        Map<String, Object> nullValues = new HashMap<>();
        nullValues.put("Test", null);
        nullValues.put(null, null);

        // Iterate through the null values and pass them to the add method.
        for (Map.Entry<String, Object> entry : nullValues.entrySet()) {
            mapBindingsAdder.addEntryToEnvironmentOtherThanPureStrings(environment, entry);
        }

        // Nothing added so the size will be 0
        Assert.assertEquals(environment.size(), 0);
    }

    @Test
    public void testSkipsIfEnvironmentIsNull() throws Exception {
        MapBindingsAdder mapBindingsAdder = new MapBindingsAdder();

        Map<String, Object> variableMap = new HashMap<>();
        variableMap.put("name", null);
        variableMap.put("container", "dockerfile/ubuntu");
        variableMap.put("greetings", "Hello World");

        // Iterate through the null values and pass them to the add method.
        for (Map.Entry<String, Object> entry : variableMap.entrySet()) {
            mapBindingsAdder.addEntryToEnvironmentOtherThanPureStrings(null, entry);
        }
    }

    @Test
    public void testSkipsIfEntryIsNull() throws Exception {
        MapBindingsAdder mapBindingsAdder = new MapBindingsAdder();
        Map<String, String> environment = new HashMap<>();

        mapBindingsAdder.addEntryToEnvironmentOtherThanPureStrings(environment, null);

        // Nothing added so the size will be 0
        Assert.assertEquals(environment.size(), 0);
    }

    @Test
    public void testMapInsideMap() throws ScriptException, IOException {
        MapBindingsAdder mapBindingsAdder = new MapBindingsAdder();
        Map<String, String> environment = new HashMap<>();
        Map<String, String> variableMap = new HashMap<>();
        Map<String, Object> outsideMap = new HashMap<>();
        variableMap.put("name", null);
        variableMap.put("container", "dockerfile/ubuntu");
        variableMap.put("greetings", "Hello World");
        outsideMap.put("variables", variableMap);

        // Add map which contains map
        for (Map.Entry<String, Object> entry : outsideMap.entrySet()) {
            mapBindingsAdder.addEntryToEnvironmentOtherThanPureStrings(environment, entry);
        }

        assertThat(environment, hasEntry("container", "dockerfile/ubuntu"));
        assertThat(environment, hasEntry("greetings", "Hello World"));
    }

}