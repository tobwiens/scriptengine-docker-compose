/*
 *  *
 * ProActive Parallel Suite(TM): The Java(TM) library for
 *    Parallel, Distributed, Multi-Core Computing for
 *    Enterprise Grids & Clouds
 *
 * Copyright (C) 1997-2015 INRIA/University of
 *                 Nice-Sophia Antipolis/ActiveEon
 * Contact: proactive@ow2.org or contact@activeeon.com
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; version 3 of
 * the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 *
 * If needed, contact us to obtain a release under GPL Version 2 or 3
 * or a different license than the AGPL.
 *
 *  Initial developer(s):               The ProActive Team
 *                        http://proactive.inria.fr/team_members.htm
 *  Contributor(s):
 *
 *  * $$ACTIVEEON_INITIAL_DEV$$
 */
package jsr223.docker.compose.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j;

import java.io.IOException;
import java.util.Properties;


@Log4j
public class DockerComposePropertyLoader {

    @lombok.Getter
    public String dockerHost;

    @lombok.Getter
    public String dockerComposeCommand;

    @lombok.Getter
    public String sudoCommand;

    @lombok.Getter
    public boolean useSudo;

    protected Properties properties;

    protected String configurationFile = "config/scriptengines/docker-compose.properties";

    private DockerComposePropertyLoader()  {
        properties = new Properties();
        try {
            log.debug("Load properties from configuration file: "+configurationFile);
            properties.load(getClass().getClassLoader().getResourceAsStream(configurationFile));
        } catch (IOException | NullPointerException e) {
            log.info("Configuration file "+configurationFile+" not found. Standard values will be used.");
            log.debug("Configuration file "+configurationFile+" not found. Standard values will be used.", e);
        }

        // Get property, specify default value
        this.dockerComposeCommand = properties.getProperty("docker.compose.command",
                "/usr/local/bin/docker-compose");
        // Get property, specify default value
        this.sudoCommand = properties.getProperty("docker.compose.sudo.command",
                "/usr/bin/sudo");
        // Get property, specify default value
        this.useSudo = Boolean.parseBoolean(properties.getProperty("docker.compose.use.sudo",
                "false"));
        this.dockerHost = properties.getProperty("docker.host", "");
    }




     /**    Initializes DockerComposePropertyLoader.

      DockerComposePropertyLoaderHolder is loaded on the first execution of DockerComposePropertyLoader.getInstance()
      or the first access to DockerComposePropertyLoaderHolder.INSTANCE, not before.
    **/
     @NoArgsConstructor(access = AccessLevel.PRIVATE)
    private static class DockerComposePropertyLoaderHolder {
        private static final DockerComposePropertyLoader INSTANCE = new DockerComposePropertyLoader();
    }

    public static DockerComposePropertyLoader getInstance() {
        return DockerComposePropertyLoaderHolder.INSTANCE;
    }
}
