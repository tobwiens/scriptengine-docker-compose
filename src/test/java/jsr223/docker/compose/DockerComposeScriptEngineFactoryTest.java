package jsr223.docker.compose;

import javax.script.ScriptEngine;

import jsr223.docker.compose.utils.DockerComposeVersionGetter;
import org.junit.Test;
import processbuilder.ProcessBuilderFactory;
import processbuilder.utils.ProcessBuilderUtilities;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.contains;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DockerComposeScriptEngineFactoryTest {

    private String testVersion = "someVersion 44";

    @Test(expected = NullPointerException.class)
    public void testThatVersionGetterCannotBeNull() {
        new DockerComposeScriptEngineFactory(null);
    }

    @Test
    public void testThatDockerComposeVersionGetterIsUsed() {
        DockerComposeVersionGetter dockerComposeVersionGetterMock = mock(DockerComposeVersionGetter.class);
        when(dockerComposeVersionGetterMock.getDockerComposeVersion(
                any(ProcessBuilderFactory.class))).thenReturn(testVersion);

        DockerComposeScriptEngineFactory dockerComposeScriptEngineFactory = new DockerComposeScriptEngineFactory(
                dockerComposeVersionGetterMock);

        assertThat(dockerComposeScriptEngineFactory.getLanguageVersion(), is(testVersion));
    }

    @Test
    public void testThatDockerComposeScriptEngineFactoryReturnsScriptEngine() {
        DockerComposeScriptEngineFactory dockerComposeScriptEngineFactory = new DockerComposeScriptEngineFactory(
                new DockerComposeVersionGetter(new ProcessBuilderUtilities()));

        assertThat(dockerComposeScriptEngineFactory.getScriptEngine() instanceof DockerComposeScriptEngine,
                is(true));
    }

    @Test
    public void testThatDockerComposeScriptEngineFactoryReturnsNonNullParameterName() {
        DockerComposeScriptEngineFactory dockerComposeScriptEngineFactory = new DockerComposeScriptEngineFactory(
                new DockerComposeVersionGetter(new ProcessBuilderUtilities()));

        assertThat(dockerComposeScriptEngineFactory.getParameter(ScriptEngine.NAME), is(notNullValue()));
    }

    @Test
    public void testThatDockerComposeScriptEngineFactoryReturnsNonNullParameterEngineVersion() {
        DockerComposeScriptEngineFactory dockerComposeScriptEngineFactory = new DockerComposeScriptEngineFactory(
                new DockerComposeVersionGetter(new ProcessBuilderUtilities()));

        assertThat(dockerComposeScriptEngineFactory.getParameter(ScriptEngine.ENGINE_VERSION),
                is(notNullValue()));
    }

    @Test
    public void testThatDockerComposeScriptEngineFactoryReturnsNonNullParameterLanguage() {
        DockerComposeScriptEngineFactory dockerComposeScriptEngineFactory = new DockerComposeScriptEngineFactory(
                new DockerComposeVersionGetter(new ProcessBuilderUtilities()));

        assertThat(dockerComposeScriptEngineFactory.getParameter(ScriptEngine.LANGUAGE), is(notNullValue()));
    }

    @Test
    public void testThatDockerComposeScriptEngineFactoryReturnsNonNullParameterEngine() {
        DockerComposeScriptEngineFactory dockerComposeScriptEngineFactory = new DockerComposeScriptEngineFactory(
                new DockerComposeVersionGetter(new ProcessBuilderUtilities()));

        assertThat(dockerComposeScriptEngineFactory.getParameter(ScriptEngine.ENGINE), is(notNullValue()));
    }

    @Test
    public void testThatDockerComposeScriptEngineFactoryReturnsNonNullLanguageName() {
        DockerComposeScriptEngineFactory dockerComposeScriptEngineFactory = new DockerComposeScriptEngineFactory(
                new DockerComposeVersionGetter(new ProcessBuilderUtilities()));

        assertThat(dockerComposeScriptEngineFactory.getEngineName(), is(notNullValue()));
    }

    @Test
    public void testThatDockerComposeScriptEngineFactoryNamesContainsDockerCompose() {
        DockerComposeScriptEngineFactory dockerComposeScriptEngineFactory = new DockerComposeScriptEngineFactory(
                new DockerComposeVersionGetter(new ProcessBuilderUtilities()));

        assertThat(dockerComposeScriptEngineFactory.getNames(), hasItem(containsString("docker-compose")));
    }

    @Test
    public void testThatDockerComposeScriptEngineFactoryMimesTypesContainsYaml() {
        DockerComposeScriptEngineFactory dockerComposeScriptEngineFactory = new DockerComposeScriptEngineFactory(
                new DockerComposeVersionGetter(new ProcessBuilderUtilities()));

        assertThat(dockerComposeScriptEngineFactory.getMimeTypes(), hasItem(containsString("yaml")));
    }

    @Test
    public void testThatDockerComposeScriptEngineFactoryExtensionContainsYaml() {
        DockerComposeScriptEngineFactory dockerComposeScriptEngineFactory = new DockerComposeScriptEngineFactory(
                new DockerComposeVersionGetter(new ProcessBuilderUtilities()));

        assertThat(dockerComposeScriptEngineFactory.getExtensions(), hasItem(containsString("yaml")));
    }

    @Test
    public void testThatDockerComposeScriptEngineFactoryExtensionContainsYml() {
        DockerComposeScriptEngineFactory dockerComposeScriptEngineFactory = new DockerComposeScriptEngineFactory(
                new DockerComposeVersionGetter(new ProcessBuilderUtilities()));

        assertThat(dockerComposeScriptEngineFactory.getExtensions(), hasItem(containsString("yml")));
    }

    @Test
    public void testThatDockerComposeScriptEngineFactoryEngineVersionIsNonNull() {
        DockerComposeScriptEngineFactory dockerComposeScriptEngineFactory = new DockerComposeScriptEngineFactory(
                new DockerComposeVersionGetter(new ProcessBuilderUtilities()));

        assertThat(dockerComposeScriptEngineFactory.getEngineVersion(), is(notNullValue()));
    }

    @Test
    public void testThatDockerComposeScriptEngineFactoryLanguageIsNonNull() {
        DockerComposeScriptEngineFactory dockerComposeScriptEngineFactory = new DockerComposeScriptEngineFactory(
                new DockerComposeVersionGetter(new ProcessBuilderUtilities()));

        assertThat(dockerComposeScriptEngineFactory.getLanguageName(), is(notNullValue()));
    }
}
