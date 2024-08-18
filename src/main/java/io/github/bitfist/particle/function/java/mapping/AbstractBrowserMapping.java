package io.github.bitfist.particle.function.java.mapping;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;

import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

abstract class AbstractBrowserMapping extends BrowserFunction {

    private static final Logger log = Logger.getLogger(AbstractBrowserMapping.class.getName());

    protected final Object bean;
    protected final Method method;

    public AbstractBrowserMapping(Object bean, Method method, Browser browser, String name) {
        super(browser, name);
        this.bean = bean;
        this.method = method;
    }

    @Override
    public Object function(Object[] arguments) {
        try {
            return execute(arguments);
        } catch (Throwable throwable) {
            log.log(Level.SEVERE, "Error in " + method.getName() + ": " + throwable.getMessage(), throwable);
            return null;
        }
    }

    protected abstract Object execute(Object[] arguments);
}
