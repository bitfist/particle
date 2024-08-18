package io.github.bitfist.particle.function.javascript;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JavaScriptFileProxyTest {

    private final Display display = mock(Display.class);
    private final Shell shell = mock(Shell.class);
    private final Browser browser = mock(Browser.class);

    private final JavaScriptFileProxyFactory target = new JavaScriptFileProxyFactory(browser);

    @Test
    void testProxy() {
        when(browser.execute(any())).thenReturn(true);
        when(browser.getShell()).thenReturn(shell);
        when(shell.getDisplay()).thenReturn(display);

        var proxy = target.getProxy(SomeJavaScript.class);
        proxy.makeBodyRed();

        var runnableArgumentCaptor = ArgumentCaptor.forClass(Runnable.class);
        verify(display).asyncExec(runnableArgumentCaptor.capture());

        runnableArgumentCaptor.getValue().run();
        verify(browser).execute(any());

        assertTrue(proxy.equals(proxy));
        assertFalse(proxy.equals(this));
        assertNotNull(proxy.toString());
        assertNotEquals(0, proxy.hashCode());
    }

    @Test
    void expectIllegalArgumentExceptionWithClass() {
        assertThrows(IllegalArgumentException.class, () -> target.getProxy(JavaScriptFileProxyTest.class));
    }

}