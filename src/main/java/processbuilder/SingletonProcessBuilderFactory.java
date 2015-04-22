package processbuilder;

/**
 * Created on 4/21/2015.
 */
public class SingletonProcessBuilderFactory implements ProcessBuilderFactory{

    private SingletonProcessBuilderFactory() {}

    /**
     *  Initializes ProcessBuilderFactory.
     *
     * ProcessBuilderFactoryHolder is loaded on the first execution of ProcessBuilderFactory.getInstance()
     * or the first access to ProcessBuilderFactoryHolder.INSTANCE, not before.
     */
    private static class ProcessBuilderFactoryHolder {
        private static final SingletonProcessBuilderFactory INSTANCE = new SingletonProcessBuilderFactory();
    }


    public static ProcessBuilderFactory getInstance() {
        return ProcessBuilderFactoryHolder.INSTANCE;
    }

    /**
     *  METHODS
     */

    @Override
    public ProcessBuilder getProcessBuilder(String... command) {
        return new ProcessBuilder(command);
    }
}

