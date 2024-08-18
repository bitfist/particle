package org.bitfist.particle;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bitfist.particle.function.java.BrowserMappingProcessor;
import org.bitfist.particle.function.javascript.JavaScriptFileProxyFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Shell;

import java.util.Collection;
import java.util.List;

public class ParticleWindow {

    private final Shell shell;
    private Browser browser;

    private BrowserMappingProcessor browserMappingProcessor;
    private JavaScriptFileProxyFactory javaScriptFileProxyFactory;

    public ParticleWindow(Shell shell, ObjectMapper objectMapper) {
        this.shell = shell;
        initialize(objectMapper);
    }

    private void initialize(ObjectMapper objectMapper) {
        shell.setLayout(new FillLayout());

        int engine = SWT.WEBKIT;
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            engine = SWT.EDGE;
        }

        browser = new Browser(shell, engine);
        browserMappingProcessor = new BrowserMappingProcessor(browser, objectMapper);
        javaScriptFileProxyFactory = new JavaScriptFileProxyFactory(browser);
    }

    public void open(String url) {
        shell.open();
        browser.setUrl(url);
    }

    public void registerBrowserMappings(Object... browserMappings) {
        registerBrowserMappings(List.of(browserMappings));
    }

    public void registerBrowserMappings(Collection<Object> browserMappings) {
        browserMappingProcessor.processBrowserMappings(browserMappings);
    }

    public <T> T registerJavaScriptFile(Class<T> type) {
        return javaScriptFileProxyFactory.getProxy(type);
    }

//    private void startEventLoop() {
//        while (!shell.isDisposed()) {
//            if (!display.readAndDispatch()) {
//                display.sleep();
//            }
//        }
//        display.dispose();
//        System.exit(0);
//    }

}