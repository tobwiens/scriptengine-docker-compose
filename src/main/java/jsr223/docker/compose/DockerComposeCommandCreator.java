/*
 * ProActive Parallel Suite(TM):
 * The Open Source library for parallel and distributed
 * Workflows & Scheduling, Orchestration, Cloud Automation
 * and Big Data Analysis on Enterprise Grids & Clouds.
 *
 * Copyright (c) 2007 - 2017 ActiveEon
 * Contact: contact@activeeon.com
 *
 * This library is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation: version 3 of
 * the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * If needed, contact us to obtain a release under GPL Version 2 or 3
 * or a different license than the AGPL.
 */
package jsr223.docker.compose;

import java.util.ArrayList;
import java.util.List;

import jsr223.docker.compose.utils.DockerComposePropertyLoader;
import lombok.NoArgsConstructor;


@NoArgsConstructor
public class DockerComposeCommandCreator {

    // Constants
    public static final String YAML_FILE_NAME = "docker-compose.yml";

    public static final String FILENAME_ARGUMENT = "-f";

    public static final String START_CONTAINER_ARGUMENT = "up";

    public static final String STOP_AND_REMOVE_CONTAINER_ARGUMENT = "down";

    public static final String VOLUMES_ARGUMENT = "--volumes";

    /**
     * Construct docker compose down command.
     *
     * @return String array representing a command.
     */
    public String[] createDockerComposeDownCommand() {
        List<String> command = new ArrayList<>();
        addSudoAndDockerComposeCommand(command);

        // Stop and remove containers
        command.add(STOP_AND_REMOVE_CONTAINER_ARGUMENT);
        // Remove volumes with containers
        command.add(VOLUMES_ARGUMENT);
        return command.toArray(new String[command.size()]);
    }

    /**
     * This method creates a bash command which starts docker-compose with a given yaml file.
     *
     * @return A String array which contains the command as a separate @String and each
     * argument as a separate String.
     */
    public String[] createDockerComposeExecutionCommand() {
        List<String> command = new ArrayList<>();
        addSudoAndDockerComposeCommand(command);

        // Add filename argument
        command.add(FILENAME_ARGUMENT);

        // Add filename
        command.add(YAML_FILE_NAME);

        // Start container with argument
        command.add(START_CONTAINER_ARGUMENT);
        return command.toArray(new String[command.size()]);
    }

    /**
     * Adds sudo and docker compose command to the given list. Sudo is only added when
     * it is configured to do that.
     *
     * @param command List which gets the command(s) added.
     */
    private void addSudoAndDockerComposeCommand(List<String> command) {
        // Add sudo if necessary
        if (DockerComposePropertyLoader.getInstance().isUseSudo()) {
            command.add(DockerComposePropertyLoader.getInstance().getSudoCommand());
        }

        // Add docker compose command
        command.add(DockerComposePropertyLoader.getInstance().getDockerComposeCommand());
    }
}
