package io.github.bitfist.particle.function.java.mapping;

import io.github.bitfist.particle.function.java.converter.ArgumentsConverter;
import org.eclipse.swt.browser.Browser;

import java.lang.reflect.Method;

public class BrowserConsumer extends AbstractBrowserMapping {

    private final ArgumentsConverter argumentsConverter;

    public BrowserConsumer(
            ArgumentsConverter argumentsConverter,
            Object bean,
            Method method,
            Browser browser,
            String name) {
        super(bean, method, browser, name);
        this.argumentsConverter = argumentsConverter;
    }

    @Override
    protected Object execute(Object[] arguments) {
        try {
            Object[] converted = argumentsConverter.convert(arguments);
            method.invoke(bean, converted);
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
