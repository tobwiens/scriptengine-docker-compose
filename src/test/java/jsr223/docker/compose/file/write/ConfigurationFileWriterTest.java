package jsr223.docker.compose.file.write;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ConfigurationFileWriterTest {

    private final String fileContent = "Test File content";
    private final String fileName = "forcedToDiskFile.txt";

    @Test
    public void testComposeFileIsWrittenToDisk() throws IOException {
        ConfigurationFileWriter configurationFileWriter = new ConfigurationFileWriter();

        File fileOnDisk = configurationFileWriter.forceFileToDisk(fileContent, fileName);
        fileOnDisk.deleteOnExit();

        assertThat(new File(fileOnDisk.getAbsolutePath()).exists(), is(true));
        assertThat(Files.readAllBytes(Paths.get(fileOnDisk.getAbsolutePath())), is(fileContent.getBytes()));
    }

    @Test
    public void testComposeFileIsOverwritten() throws IOException {
        ConfigurationFileWriter configurationFileWriter = new ConfigurationFileWriter();

        // Create file with same name -- must be overwritten
        new File(fileName).createNewFile();
        File fileOnDisk = configurationFileWriter.forceFileToDisk(fileContent, fileName);
        fileOnDisk.deleteOnExit();

        assertThat(new File(fileOnDisk.getAbsolutePath()).exists(), is(true));
        assertThat(Files.readAllBytes(Paths.get(fileOnDisk.getAbsolutePath())), is(fileContent.getBytes()));
    }
}