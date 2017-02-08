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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.script.ScriptEngine;

import org.junit.Test;

import jsr223.docker.compose.utils.DockerComposeVersionGetter;
import processbuilder.ProcessBuilderFactory;
import processbuilder.utils.ProcessBuilderUtilities;


public class DockerComposeScriptEngineFactoryTest {

    @Test(expected = NullPointerException.class)
    public void testThatVersionGetterCannotBeNull() {
        new DockerComposeScriptEngineFactory(new ProcessBuilderUtilities(), null);
    }

    @Test(expected = NullPointerException.class)
    public void testThatProcessBuilderCannotBeNull() {
        new DockerComposeScriptEngineFactory(null, new DockerComposeVersionGetter(new ProcessBuilderUtilities()));
    }

    @Test(expected = NullPointerException.class)
    public void testThatVersionGetterAndProcessBuilderCannotBeNull() {
        new DockerComposeScriptEngineFactory(null, null);
    }

    @Test
    public void testThatDockerComposeVersionGetterIsUsed() {
        DockerComposeVersionGetter dockerComposeVersionGetterMock = mock(DockerComposeVersionGetter.class);
        String testVersion = "someVersion 44";
        when(dockerComposeVersionGetterMock.getDockerComposeVersion(any(ProcessBuilderFactory.class))).thenReturn(testVersion);

        DockerComposeScriptEngineFactory dockerComposeScriptEngineFactory = new DockerComposeScriptEngineFactory(new ProcessBuilderUtilities(),
                                                                                                                 dockerComposeVersionGetterMock);

        assertThat(dockerComposeScriptEngineFactory.getLanguageVersion(), is(testVersion));
    }

    @Test
    public void testThatDockerComposeScriptEngineFactoryReturnsScriptEngine() {
        DockerComposeScriptEngineFactory dockerComposeScriptEngineFactory = new DockerComposeScriptEngineFactory(new ProcessBuilderUtilities(),
                                                                                                                 new DockerComposeVersionGetter(new ProcessBuilderUtilities()));

        assertThat(dockerComposeScriptEngineFactory.getScriptEngine() instanceof DockerComposeScriptEngine, is(true));
    }

    @Test
    public void testThatDockerComposeScriptEngineFactoryReturnsNonNullParameterName() {
        DockerComposeScriptEngineFactory dockerComposeScriptEngineFactory = new DockerComposeScriptEngineFactory(new ProcessBuilderUtilities(),
                                                                                                                 new DockerComposeVersionGetter(new ProcessBuilderUtilities()));

        assertThat(dockerComposeScriptEngineFactory.getParameter(ScriptEngine.NAME), is(notNullValue()));
    }

    @Test
    public void testThatDockerComposeScriptEngineFactoryReturnsNonNullParameterEngineVersion() {
        DockerComposeScriptEngineFactory dockerComposeScriptEngineFactory = new DockerComposeScriptEngineFactory(new ProcessBuilderUtilities(),
                                                                                                                 new DockerComposeVersionGetter(new ProcessBuilderUtilities()));

        assertThat(dockerComposeScriptEngineFactory.getParameter(ScriptEngine.ENGINE_VERSION), is(notNullValue()));
    }

    @Test
    public void testThatDockerComposeScriptEngineFactoryReturnsNonNullParameterLanguage() {
        DockerComposeScriptEngineFactory dockerComposeScriptEngineFactory = new DockerComposeScriptEngineFactory(new ProcessBuilderUtilities(),
                                                                                                                 new DockerComposeVersionGetter(new ProcessBuilderUtilities()));

        assertThat(dockerComposeScriptEngineFactory.getParameter(ScriptEngine.LANGUAGE), is(notNullValue()));
    }

    @Test
    public void testThatDockerComposeScriptEngineFactoryReturnsNonNullParameterEngine() {
        DockerComposeScriptEngineFactory dockerComposeScriptEngineFactory = new DockerComposeScriptEngineFactory(new ProcessBuilderUtilities(),
                                                                                                                 new DockerComposeVersionGetter(new ProcessBuilderUtilities()));

        assertThat(dockerComposeScriptEngineFactory.getParameter(ScriptEngine.ENGINE), is(notNullValue()));
    }

    @Test
    public void testThatDockerComposeScriptEngineFactoryReturnsNonNullLanguageName() {
        DockerComposeScriptEngineFactory dockerComposeScriptEngineFactory = new DockerComposeScriptEngineFactory(new ProcessBuilderUtilities(),
                                                                                                                 new DockerComposeVersionGetter(new ProcessBuilderUtilities()));

        assertThat(dockerComposeScriptEngineFactory.getEngineName(), is(notNullValue()));
    }

    @Test
    public void testThatDockerComposeScriptEngineFactoryNamesContainsDockerCompose() {
        DockerComposeScriptEngineFactory dockerComposeScriptEngineFactory = new DockerComposeScriptEngineFactory(new ProcessBuilderUtilities(),
                                                                                                                 new DockerComposeVersionGetter(new ProcessBuilderUtilities()));

        assertThat(dockerComposeScriptEngineFactory.getNames(), hasItem(containsString("docker-compose")));
    }

    @Test
    public void testThatDockerComposeScriptEngineFactoryMimesTypesContainsYaml() {
        DockerComposeScriptEngineFactory dockerComposeScriptEngineFactory = new DockerComposeScriptEngineFactory(new ProcessBuilderUtilities(),
                                                                                                                 new DockerComposeVersionGetter(new ProcessBuilderUtilities()));

        assertThat(dockerComposeScriptEngineFactory.getMimeTypes(), hasItem(containsString("yaml")));
    }

    @Test
    public void testThatDockerComposeScriptEngineFactoryExtensionContainsYaml() {
        DockerComposeScriptEngineFactory dockerComposeScriptEngineFactory = new DockerComposeScriptEngineFactory(new ProcessBuilderUtilities(),
                                                                                                                 new DockerComposeVersionGetter(new ProcessBuilderUtilities()));

        assertThat(dockerComposeScriptEngineFactory.getExtensions(), hasItem(containsString("yaml")));
    }

    @Test
    public void testThatDockerComposeScriptEngineFactoryExtensionContainsYml() {
        DockerComposeScriptEngineFactory dockerComposeScriptEngineFactory = new DockerComposeScriptEngineFactory(new ProcessBuilderUtilities(),
                                                                                                                 new DockerComposeVersionGetter(new ProcessBuilderUtilities()));

        assertThat(dockerComposeScriptEngineFactory.getExtensions(), hasItem(containsString("yml")));
    }

    @Test
    public void testThatDockerComposeScriptEngineFactoryEngineVersionIsNonNull() {
        DockerComposeScriptEngineFactory dockerComposeScriptEngineFactory = new DockerComposeScriptEngineFactory(new ProcessBuilderUtilities(),
                                                                                                                 new DockerComposeVersionGetter(new ProcessBuilderUtilities()));

        assertThat(dockerComposeScriptEngineFactory.getEngineVersion(), is(notNullValue()));
    }

    @Test
    public void testThatDockerComposeScriptEngineFactoryLanguageIsNonNull() {
        DockerComposeScriptEngineFactory dockerComposeScriptEngineFactory = new DockerComposeScriptEngineFactory(new ProcessBuilderUtilities(),
                                                                                                                 new DockerComposeVersionGetter(new ProcessBuilderUtilities()));

        assertThat(dockerComposeScriptEngineFactory.getLanguageName(), is(notNullValue()));
    }
}
