/*
 *  *
 * ProActive Parallel Suite(TM): The Java(TM) library for
 *    Parallel, Distributed, Multi-Core Computing for
 *    Enterprise Grids & Clouds
 *
 * Copyright (C) 1997-2016 INRIA/University of
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
 *  Contributor(s): ActiveEon Team - http://www.activeeon.com
 *
 *  * $$ACTIVEEON_CONTRIBUTOR$$
 */
package jsr223.docker.compose.utils;

public class Log4jConfigurationLoader {

    private static final String LOG4J_CONFIGURATION_FILE = "config/log/scriptengines.properties";

    public void loadLog4jConfiguration() {
        // Catch all exceptions to not sacrifice functionality for logging.
        try {
            // configure the logger here, quick and dirty
            org.apache.log4j.PropertyConfigurator.configure(getClass()
                    .getClassLoader().getResourceAsStream(LOG4J_CONFIGURATION_FILE));
        } catch (NullPointerException e) { //NOSONAR
            warnAboutMissingConfigurationFileOnErrorStream();
        } catch (Exception e) { //NOSONAR
            warnAboutNotFunctioningLoggingOnErrorStream(e);
        }

    }

    private void warnAboutNotFunctioningLoggingOnErrorStream(Exception e) {
        System.err.println("Log4j initialization failed: " + LOG4J_CONFIGURATION_FILE + //NOSONAR
                ". Docker Compose script engine is functional but logging is disabled." + //NOSONAR
                "Stacktrace is: "); //NOSONAR
        e.printStackTrace(); //NOSONAR
    }

    private void warnAboutMissingConfigurationFileOnErrorStream() {
        System.err.println("Log4j configuration file not found: " + LOG4J_CONFIGURATION_FILE + //NOSONAR
                ". Any output for the Docker Compose script engine is disabled."); //NOSONAR
    }
}
