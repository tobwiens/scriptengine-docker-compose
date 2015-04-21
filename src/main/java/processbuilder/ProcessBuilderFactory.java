package processbuilder;

/**
 * Created on 4/21/2015.
 */
public class ProcessBuilderFactory {

    private ProcessBuilderFactory() {}

    /**
     *  Initializes ProcessBuilderFactory.
     *
     * ProcessBuilderFactoryHolder is loaded on the first execution of ProcessBuilderFactory.getInstance()
     * or the first access to ProcessBuilderFactoryHolder.INSTANCE, not before.
     */
    private static class ProcessBuilderFactoryHolder {
        private static final ProcessBuilderFactory INSTANCE = new ProcessBuilderFactory();
    }

    public static ProcessBuilderFactory getInstance() {
        return ProcessBuilderFactoryHolder.INSTANCE;
    }

    /**
     *  METHODS
     */

    public ProcessBuilder getProcessBuilder(String... command) {
        return new ProcessBuilder(command);
    }
}

