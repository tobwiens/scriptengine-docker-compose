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

    /**
     * This method creates a bash command which starts docker-compose with a given yaml file.
     * @param yamlFile String representation of the yaml file to execute.
     * @return A @String array which contains the command as a separate @String and each
     * argument as a separate @String.
     */
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

    /**
     * This methods creates an escape map. All future escapes necessary to run a compose script properly
     * will be added.
     * @return A map which contains a key (string to be replaced) and a value (replaced).
     */
    private static Map<String,String> getEscapeMap() {
        Map<String, String> replaceThat = new HashMap<>();

        // Add everything which should be escaped and how
        // Replace " with \"
        replaceThat.put("\"", "\\\"");

        return replaceThat;
    }

    /**
     * Escape an array of @Strings.
     * @param stringsToEscape Array where each string will be escaped.
     * @return Array which contains all
     */
    private static String[] escapeArrayOfStringsForBash(@NotNull String... stringsToEscape) {
        ArrayList<String> result = new ArrayList<>();
        Map<String, String> replaceThat = getEscapeMap();

        for (String line : stringsToEscape) {
            result.add(replaceAllOccurrences(line, replaceThat));
        }
        return result.toArray(new String[result.size()]);
    }

    /**
     * Replaces all occurrences in a String given via a Map<StringToReplace,Replaced>.
     * @param string String which will have replaced all occurrences.
     * @param replaceMap Map<StringToReplace,Replaced>.
     * @return String where all occurrences are replaced.
     */
    private static String replaceAllOccurrences(@NotNull String string, @NotNull Map<String, String> replaceMap) {
        for (String replaceEntry : replaceMap.keySet()) {
            string = string.replace(replaceEntry, replaceMap.get(replaceEntry));
        }
        return string;
    }
}
