package processbuilder;

/**
 * Created on 4/21/2015.
 */
public interface ProcessBuilderFactory {

    public ProcessBuilder getProcessBuilder(String... command);


}
