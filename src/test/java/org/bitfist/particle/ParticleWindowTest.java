package org.bitfist.particle;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bitfist.particle.function.java.TestBrowserMapping;
import org.bitfist.particle.function.javascript.SomeJavaScript;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@EnabledIf("isBrowserTestEnabled")
class ParticleWindowTest {

    public static boolean isBrowserTestEnabled() {
        return Boolean.parseBoolean(System.getenv("BROWSER_TEST_ENABLED"));
    }

    private final Display display = Display.getDefault();
    private final Shell shell = new Shell(display);

    @Test
    void testWindow() {
        var particleWindow = new ParticleWindow(shell, new ObjectMapper());
        particleWindow.registerBrowserMappings(new TestBrowserMapping());
        var proxy = particleWindow.registerJavaScriptFile(SomeJavaScript.class);

        particleWindow.open("https://www.google.at");

        assertNotNull(proxy);

        display.dispose();
    }

}