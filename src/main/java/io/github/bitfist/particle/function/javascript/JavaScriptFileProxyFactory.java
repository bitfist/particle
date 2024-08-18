package io.github.bitfist.particle.function.javascript;

import org.eclipse.swt.browser.Browser;

import java.lang.reflect.Proxy;

public class JavaScriptFileProxyFactory {

    private final JavaScriptInvocationHandler invocationHandler;

    public JavaScriptFileProxyFactory(Browser browser) {
        invocationHandler = new JavaScriptInvocationHandler(browser);
    }

    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> type) {
        if (!type.isInterface()) {
            throw new IllegalArgumentException("Type must be an interface");
        }

        Object proxyInstance = Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, invocationHandler);

        return (T) proxyInstance;
    }
}
