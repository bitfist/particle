package io.github.bitfist.particle.function.java.mapping;

import io.github.bitfist.particle.function.java.converter.ArgumentsConverter;
import io.github.bitfist.particle.function.java.converter.ValueConverter;
import org.eclipse.swt.browser.Browser;

import java.lang.reflect.Method;

public class BrowserFunction extends AbstractBrowserMapping{

    private final ArgumentsConverter argumentsConverter;
    private final ValueConverter returnValueConverter;

    public BrowserFunction(
            ArgumentsConverter argumentsConverter,
            ValueConverter returnValueConverter,
            Object bean,
            Method method,
            Browser browser,
            String name
    ) {
        super(bean, method, browser, name);
        this.argumentsConverter = argumentsConverter;
        this.returnValueConverter = returnValueConverter;
    }

    @Override
    protected Object execute(Object[] arguments) {
        try {
            Object[] converted = argumentsConverter.convert(arguments);
            Object result = method.invoke(bean, converted);
            return returnValueConverter.convert(result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
