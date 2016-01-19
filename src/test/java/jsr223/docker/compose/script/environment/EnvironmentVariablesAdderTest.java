package jsr223.docker.compose.script.environment;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class EnvironmentVariablesAdderTest {

    @Test
    public void testAddEntryToEnvironmentOtherThanPureStringsDoesntThrowNullpointer() throws Exception {
        EnvironmentVariablesAdder environmentVariablesAdder = new EnvironmentVariablesAdder();
        Map<String, String> environment = new HashMap<>();

        // Construct Map which contains null values
        Map<String, Object> nullValues = new HashMap<>();
        nullValues.put("Test", null);
        nullValues.put(null, null);

        // Iterate through the null values and don't add them to the environment map
        for ( Map.Entry<String, Object> entry : nullValues.entrySet()) {
            environmentVariablesAdder.AddEntryToEnvironmentOtherThanPureStrings(environment, entry);
        }

        // Nothing added so the size will be 0
        Assert.assertEquals(environment.size(), 0);
    }

}