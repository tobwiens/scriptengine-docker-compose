package processbuilder.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;

/**
 * Created on 4/21/2015.
 */
public class ProcessBuilderUtilities {

    // TODO: Test
    /**
     * Attaches standard and error writer as well as input to a process.
     * @param process Process which to attach Output, Error and Input to.
     * @param processOutput A data sink for the process standard output. If null nothing will ne attached.
     * @param processError A data sink for the process' error output. If null nothing will be attached.
     * @param processInput A data source to be streamed to the process. If null nothing will be attached.
     */
    public static void attachStreamsToProcess(@NotNull Process process, @Nullable Writer processOutput, Writer processError,
                                       Reader processInput) {
      if (processOutput != null ) {
          // Attach to std output
          attachToInputStream(new InputStreamReader(process.getInputStream()), processOutput);
      }

        if (processError != null) {
            // Attach error output
            attachToInputStream(new InputStreamReader(process.getErrorStream()), processError);
        }

        if (processInput != null) {
            // Attach process input
            attachToInputStream(processInput,new OutputStreamWriter(process.getOutputStream()));
        }
    }
    // TODO: Test
    /**
     * Creates a thread which will constantly pipe data, only active when new data is available, from a source to an attached sink. After
     * reaching the end of the source stream the @Thread will silently be destroyed.
     * @param source Data source.
     * @param attachedSink Data sink.
     */
    public static void attachToInputStream(@NotNull final Reader source, @NotNull final Writer attachedSink) {
        new Thread() {
            public void run() {
                try {
                    pipe(source, attachedSink);
                } catch (IOException e) {
                    //Done
                }
            }
        }.start();
    }
    // TODO: Test
    /**
     * Pipes all data from a reader (source) to a writer (sink) until an I/O execution occurs or the
     * end of the source is reached.
     * @param from Source of data.
     * @param to Sink of data.
     * @throws IOException
     */
    public static void pipe(@NotNull Reader from, @NotNull Writer to) throws IOException {
        char[] buff = new char[1024];
        int n = from.read(buff);
        while (n != -1) {
            to.write(buff, 0, n);
            to.flush();
            n = from.read(buff);
        }
        from.close();
    }
}
