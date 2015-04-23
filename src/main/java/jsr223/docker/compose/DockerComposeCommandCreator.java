package jsr223.docker.compose;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 4/23/2015.
 */
public class DockerComposeCommandCreator {

    // Constants
    private static String bashComposeFileStart = "<(\"";
    private static String bashComposeFileEnd = "\")";
    private static String lineSeparator = "\n";

    public static String[] createDockerComposeExecutionCommandBash(@NotNull String... yamlFile) {
        List<String> command = new ArrayList<>();

        // Add sudo if necessary
        if (DockerComposeScriptEngineFactory.isUseSudo()) {
            command.add(DockerComposeScriptEngineFactory.getSudoCommand());
        }

        // Add docker compose command
        command.add(DockerComposeScriptEngineFactory.getDockerComposeCommand());

        // Add a temporary file
        String yamlInput = bashComposeFileStart;
        for (String line : escapeArrayOfStringsForBash(yamlFile)) {
            yamlInput += line+lineSeparator;
        }
        yamlInput += bashComposeFileEnd;
        command.add(yamlInput);

        return command.toArray(new String[command.size()]);
    }

    private static Map<String,String> getEscapeMap() {
        Map<String, String> replaceThat = new HashMap<>();

        // Add everything which should be escaped and how
        // Replace " with \"
        replaceThat.put("\"", "\\\"");

        return replaceThat;
    }

    private static String[] escapeArrayOfStringsForBash(@NotNull String... stringsToEscape) {
        ArrayList<String> result = new ArrayList<>();
        Map<String, String> replaceThat = getEscapeMap();

        for (String line : stringsToEscape) {
            result.add(replaceAllOccurrences(line, replaceThat));
        }
        return result.toArray(new String[result.size()]);
    }

    private static String replaceAllOccurrences(@NotNull String string, @NotNull Map<String, String> replaceMap) {
        for (String replaceEntry : replaceMap.keySet()) {
            string = string.replace(replaceEntry, replaceMap.get(replaceEntry));
        }
        return string;
    }
}
