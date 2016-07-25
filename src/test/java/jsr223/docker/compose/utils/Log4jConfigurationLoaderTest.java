package jsr223.docker.compose.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class Log4jConfigurationLoaderTest {
    @Spy
    Log4jConfigurationLoader log4jConfigurationLoader;

    @Test
    public void loadLog4jConfigurationDOesntThrowExceptionIfFileIfFileIsNotPresent() throws Exception {
        log4jConfigurationLoader.loadLog4jConfiguration();
    }

}