package processbuilder;


public interface ProcessBuilderFactory {
    ProcessBuilder getProcessBuilder(String... command);
}
