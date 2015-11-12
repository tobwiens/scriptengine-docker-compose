package jsr223.docker.compose.utils;

import mockit.Expectations;
import mockit.MockUp;
import mockit.Mocked;
import org.junit.Ignore;
import org.junit.Test;
import processbuilder.ProcessBuilderFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;


public class DockerComposeUtilitiesTest {
    String testString = "Test output";


    private class MockedProcess extends Process {

        @Override
        public OutputStream getOutputStream() {
            return null;
        }

        public InputStream getInputStream() {
           return new ByteArrayInputStream(testString.getBytes());
        }

        @Override
        public InputStream getErrorStream() {
            return null;
        }

        @Override
        public int waitFor() throws InterruptedException {
            Thread.sleep(1000);
            return 0;
        }

        @Override
        public int exitValue() {
            return 0;
        }

        @Override
        public void destroy() {

        }

    }


    @Test
    public void getDockerComposeVersionReturnOutputFromProcessStream(@Mocked final ProcessBuilderFactory factory, @Mocked final ProcessBuilder pb, @Mocked final Process process) throws IOException, InterruptedException {
        new Expectations() {
            {
                pb.start(); result = new MockedProcess();
            }
        };
        assertThat(DockerComposeUtilities.getDockerComposeVersion(factory), is(testString));
    }

    @Test
    public void addVersionParamaterToDockerComposeVersionCommand(@Mocked final ProcessBuilderFactory factory ) {
        new Expectations() {
            {
                // Return an empty HashMap to prevent NullPointerException
                factory.getProcessBuilder(anyString, withSubstring("--version"));
            }
        };
        DockerComposeUtilities.getDockerComposeVersion(factory);
    }

    @Test
    public void startProcessBuilderAndWaitForRunningProcessToFinish(@Mocked final ProcessBuilderFactory factory, @Mocked final ProcessBuilder pb, @Mocked final Process process) throws IOException, InterruptedException {
        new Expectations() {
            {
                // Start process builder
                pb.start(); result = process;

                // Wait for running process to finish
                process.waitFor();
            }
        };
        DockerComposeUtilities.getDockerComposeVersion(factory);
    }

}
