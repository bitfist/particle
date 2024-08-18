package io.github.bitfist.particle.function.java.mapping;

import io.github.bitfist.particle.function.java.converter.ValueConverter;
import org.eclipse.swt.browser.Browser;

import java.lang.reflect.Method;

public class BrowserProducer extends AbstractBrowserMapping {

    private final ValueConverter returnValueConverter;

    public BrowserProducer(
            ValueConverter returnValueConverter,
            Object bean,
            Method method,
            Browser browser,
            String name
    ) {
        super(bean, method, browser, name);
        this.returnValueConverter = returnValueConverter;
    }

    @Override
    protected Object execute(Object[] arguments) {
        try {
            Object result = method.invoke(bean);
            return returnValueConverter.convert(result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
