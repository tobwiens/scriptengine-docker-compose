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
 *  Contributor(s):
 *
 *  * $$ACTIVEEON_INITIAL_DEV$$
 */
package jsr223.docker.compose.file.write;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.FileAlreadyExistsException;

public class ConfigurationFileWriter {

    public File forceFileToDisk(String fileContent, String filenameAndPath) throws IOException {
        File composeYamlFile = new File(filenameAndPath);
        // Create configuration file
        if (!composeYamlFile.createNewFile()) {
            composeYamlFile.delete();
            if (!composeYamlFile.createNewFile()) {
                throw new FileAlreadyExistsException("Configuration file was deleted but still exists: "
                        + filenameAndPath);
            }
        }

        // Force configuration file to disk
        Writer configFileWriter = new FileWriter(composeYamlFile);
        configFileWriter.write(fileContent);
        configFileWriter.close();

        return composeYamlFile;
    }
}
