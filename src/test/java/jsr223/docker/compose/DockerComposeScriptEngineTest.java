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

import java.io.IOException;

import javax.script.ScriptException;


public class DockerComposeScriptEngineTest {

    /**
     * Check whether the process builder is correctly used and executed. If the environment is accessed
     * and if the input and output streams are accessed.
     *
     * @param pb Mocked ProcessBuilder
     * @throws IOException          Should not be thrown.
     * @throws InterruptedException Should not be thrown.
     * @throws ScriptException      Should not be thrown.
     */
    /*
     * @Test
     * public void checkProcessBuilderExecution(
     * 
     * @Mocked final ProcessBuilder pb) throws IOException, InterruptedException, ScriptException {
     * new Expectations() {
     * {
     * // Expect a new creation of the process builder with the command given from the
     * // DockerComposeCommandCreator
     * new ProcessBuilder(DockerComposeCommandCreator.createDockerComposeExecutionCommand());
     * 
     * // Expect the environment to be accessed to fill it with env variables
     * pb.environment();
     * result = new HashMap<String, String>();
     * 
     * // Expect to start the process
     * Process process = pb.start();
     * // Expect to get all streams and attach them
     * process.getOutputStream();
     * process.getErrorStream();
     * process.getInputStream();
     * // Expect to wait for finishing of execution
     * process.waitFor();
     * result = 0;
     * 
     * // Expect to finish and stop containers
     * new ProcessBuilder(DockerComposeCommandCreator.createDockerComposeStopCommand());
     * 
     * // Expect to remove container
     * new ProcessBuilder(DockerComposeCommandCreator.createDockerComposeRemoveCommand());
     * }
     * };
     * 
     * DockerComposeScriptEngine scriptEngine = new DockerComposeScriptEngine();
     * org.junit.Assert.assertEquals("Return value must be as expected.",
     * 0,
     * scriptEngine.eval("Mock", new SimpleBindings()));
     * }
     */
}
