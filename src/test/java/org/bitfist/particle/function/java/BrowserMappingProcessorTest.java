package org.bitfist.particle.function.java;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

import javax.swing.SwingUtilities;

@EnabledIf("isBrowserTestEnabled")
public class BrowserMappingProcessorTest {

    public static boolean isBrowserTestEnabled() {
        return Boolean.parseBoolean(System.getenv("BROWSER_TEST_ENABLED"));
    }

    @Test
    public void simpleBrowserTest() throws Exception {
        // given
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setSize(1280, 800);
        shell.setLayout(new FillLayout());
        shell.setText("Test Browser");

        Browser browser = new Browser(shell, SWT.NONE);
        TestBrowserMapping testBrowserMapping = new TestBrowserMapping();

        // when
        var processor = new BrowserMappingProcessor(browser, new ObjectMapper());
        processor.processBrowserMappings(java.util.Collections.singletonList(testBrowserMapping));
        browser.execute("window.test_aCallable();");
        browser.execute("window.test_aConsumer('test1337');");
        browser.execute("window.test_aProducer();");
        browser.execute("window.test_aFunction('test1338');");
        browser.execute("window.test_aByteConsumer(12);");
        browser.execute("window.test_aShortConsumer(12);");
        browser.execute("window.test_anIntegerConsumer(1337);");
        browser.execute("window.test_aLongConsumer(133700);");
        browser.execute("window.test_aFloatConsumer(13.37);");
        browser.execute("window.test_aDoubleConsumer(13.37);");
        browser.execute("window.test_anObjectConsumer(null);");
        browser.execute("window.test_anObjectConsumer(\"{ \\\"id\\\": 1, \\\"name\\\": \\\"greetings from javascript\\\" }\");");

        // then
        Assertions.assertEquals(1, testBrowserMapping.getCallableCalled());
        Assertions.assertEquals(1, testBrowserMapping.getConsumerCalled());
        Assertions.assertEquals(1, testBrowserMapping.getProducerCalled());
        Assertions.assertEquals(1, testBrowserMapping.getFunctionCalled());

        Assertions.assertEquals("test1337", testBrowserMapping.getConsumed());
        Assertions.assertEquals((Byte) (byte) 12, testBrowserMapping.getByteValue());
        Assertions.assertEquals((Short) (short) 12, testBrowserMapping.getShortValue());
        Assertions.assertEquals((Integer) 1337, testBrowserMapping.getIntegerValue());
        Assertions.assertEquals((Long) 133700L, testBrowserMapping.getLongValue());
        Assertions.assertEquals((Float) 13.37f, testBrowserMapping.getFloatValue());
        Assertions.assertEquals((Double) 13.37, testBrowserMapping.getDoubleValue());
        Assertions.assertEquals(1, testBrowserMapping.getTestObject().getId());
        Assertions.assertEquals("greetings from javascript", testBrowserMapping.getTestObject().getName());

        display.dispose();
    }
}
