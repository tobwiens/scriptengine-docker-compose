package jsr223.docker.compose.utils;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.Bindings;
import javax.script.SimpleBindings;

import org.junit.Test;

/**
 * @author ActiveEon Team
 * @since 01/02/2018
 */
public class CommandlineOptionsFromBindingsExtractorTest {

    @Test
    public void testThatGenericInformationBindingsIsExtractedAndMapContentIsReturnedSplit() {
        CommandlineOptionsFromBindingsExtractor commandlineOptionsFromBindingsExtractor = new CommandlineOptionsFromBindingsExtractor();

        Bindings bindings = new SimpleBindings();
        Map<String,String> genericInformation = new HashMap<>();
        genericInformation.put(CommandlineOptionsFromBindingsExtractor.DOCKER_COMPOSE_UP_COMMANDLINE_OPTIONS_KEY,"--option1 --option2");
        bindings.put(CommandlineOptionsFromBindingsExtractor.GENERIC_INFORMATION_KEY, genericInformation);

        List<String> options = commandlineOptionsFromBindingsExtractor.getDockerComposeCommandOptions(bindings);

        assertTrue(options.contains("--option1"));
        assertTrue(options.contains("--option2"));
        assertTrue(options.size() == 2);
    }

    @Test
    public void testThatGenericInformationBindingsIsExtractedAndMapContentIsReturnedSplitWithRegex() {
        CommandlineOptionsFromBindingsExtractor commandlineOptionsFromBindingsExtractor = new CommandlineOptionsFromBindingsExtractor();

        Bindings bindings = new SimpleBindings();
        Map<String,String> genericInformation = new HashMap<>();
        genericInformation.put(CommandlineOptionsFromBindingsExtractor.DOCKER_COMPOSE_UP_COMMANDLINE_OPTIONS_KEY,"--option1!SPLIT!--option2");
        genericInformation.put(CommandlineOptionsFromBindingsExtractor.DOCKER_COMPOSE_COMMANDLINE_OPTIONS_SPLIT_REGEX_KEY, "!SPLIT!");
        bindings.put(CommandlineOptionsFromBindingsExtractor.GENERIC_INFORMATION_KEY, genericInformation);

        List<String> options = commandlineOptionsFromBindingsExtractor.getDockerComposeCommandOptions(bindings);

        assertTrue(options.contains("--option1"));
        assertTrue(options.contains("--option2"));
        assertTrue(options.size() == 2);
    }

}