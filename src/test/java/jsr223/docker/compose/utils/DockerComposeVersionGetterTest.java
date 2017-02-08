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
package jsr223.docker.compose.utils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.Reader;
import java.io.Writer;

import org.junit.Assume;
import org.junit.Test;
import org.mockito.Matchers;

import processbuilder.ProcessBuilderFactory;
import processbuilder.utils.ProcessBuilderUtilities;


public class DockerComposeVersionGetterTest {

    @Test
    public void getDockerComposeVersionCallsProcessBuilderFactory() throws Exception {
        ProcessBuilderFactory factory = mock(ProcessBuilderFactory.class);
        ProcessBuilderUtilities processBuilderUtilitiesMock = spy(ProcessBuilderUtilities.class);

        when(factory.getProcessBuilder(Matchers.<String[]> anyVararg())).thenReturn(new ProcessBuilder(""));

        DockerComposeVersionGetter dockerComposeVersionGetter = new DockerComposeVersionGetter(processBuilderUtilitiesMock);
        dockerComposeVersionGetter.getDockerComposeVersion(factory);

        verify(factory).getProcessBuilder(Matchers.<String[]> anyVararg());
    }

    @Test
    public void getDockerComposeVersionInvalidCommandReturnsEmptyString() throws Exception {
        ProcessBuilderFactory factory = mock(ProcessBuilderFactory.class);
        ProcessBuilderUtilities processBuilderUtilitiesMock = spy(ProcessBuilderUtilities.class);

        when(factory.getProcessBuilder(Matchers.<String[]> anyVararg())).thenReturn(new ProcessBuilder("...."));

        DockerComposeVersionGetter dockerComposeVersionGetter = new DockerComposeVersionGetter(processBuilderUtilitiesMock);

        assertThat(dockerComposeVersionGetter.getDockerComposeVersion(factory), is(""));
    }

    @Test
    public void getDockerComposeVersionCallsProcessBuilderUtilitiesWindows() throws Exception {
        Assume.assumeTrue(System.getProperty("os.name").toLowerCase().startsWith("win"));

        ProcessBuilderFactory factoryMock = mock(ProcessBuilderFactory.class);
        ProcessBuilderUtilities processBuilderUtilitiesMock = spy(ProcessBuilderUtilities.class);

        when(factoryMock.getProcessBuilder(Matchers.<String[]> anyVararg())).thenReturn(new ProcessBuilder("cmd",
                                                                                                           "/C",
                                                                                                           "dir"));

        DockerComposeVersionGetter dockerComposeVersionGetter = new DockerComposeVersionGetter(processBuilderUtilitiesMock);

        dockerComposeVersionGetter.getDockerComposeVersion(factoryMock);

        verify(processBuilderUtilitiesMock).attachStreamsToProcess(any(Process.class),
                                                                   any(Writer.class),
                                                                   any(Writer.class),
                                                                   any(Reader.class));
    }

    @Test
    public void getDockerComposeVersionCallsProcessBuilderUtilitiesLinux() throws Exception {
        Assume.assumeTrue(System.getProperty("os.name").toLowerCase().startsWith("lin"));

        ProcessBuilderFactory factoryMock = mock(ProcessBuilderFactory.class);
        ProcessBuilderUtilities processBuilderUtilitiesMock = spy(ProcessBuilderUtilities.class);

        when(factoryMock.getProcessBuilder(Matchers.<String[]> anyVararg())).thenReturn(new ProcessBuilder("ls"));

        DockerComposeVersionGetter dockerComposeVersionGetter = new DockerComposeVersionGetter(processBuilderUtilitiesMock);
        dockerComposeVersionGetter.getDockerComposeVersion(factoryMock);

        verify(processBuilderUtilitiesMock).attachStreamsToProcess(any(Process.class),
                                                                   any(Writer.class),
                                                                   any(Writer.class),
                                                                   any(Reader.class));
    }

    @Test
    public void getDockerComposeVersionNullFactoryReturnsEmptyString() throws Exception {
        DockerComposeVersionGetter dockerComposeVersionGetter = new DockerComposeVersionGetter(new ProcessBuilderUtilities());

        assertThat(dockerComposeVersionGetter.getDockerComposeVersion(null), is(""));
    }
}
