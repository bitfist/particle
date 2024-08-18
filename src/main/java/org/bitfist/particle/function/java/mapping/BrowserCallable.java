package org.bitfist.particle.function.java.mapping;

import org.eclipse.swt.browser.Browser;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BrowserCallable extends AbstractBrowserMapping{

    public BrowserCallable(Object bean, Method method, Browser browser, String name) {
        super(bean, method, browser, name);
    }

    @Override
    protected Object execute(Object[] arguments) {
        try {
            method.invoke(bean);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
