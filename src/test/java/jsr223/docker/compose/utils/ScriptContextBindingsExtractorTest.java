package jsr223.docker.compose.utils;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.script.ScriptContext;
import javax.script.SimpleBindings;

import org.junit.Test;
import org.mockito.Mockito;

/**
 * @author ActiveEon Team
 * @since 01/02/2018
 */
public class ScriptContextBindingsExtractorTest {

    @Test
    public void testThatBindingsAreExtractedWithScopeConstant() {
        ScriptContextBindingsExtractor scriptContextBindingsExtractor = new ScriptContextBindingsExtractor();
        ScriptContext scriptContextMock = mock(ScriptContext.class);
        when(scriptContextMock.getBindings(Mockito.anyInt())).thenReturn(new SimpleBindings());

        scriptContextBindingsExtractor.extractFrom(scriptContextMock);

        verify(scriptContextMock).getBindings(ScriptContextBindingsExtractor.BINDINGS_SCOPE);
    }

}