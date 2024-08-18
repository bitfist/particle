package io.github.bitfist.particle.function.javascript;

import org.eclipse.swt.browser.Browser;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class JavaScriptInvocationHandler implements InvocationHandler {

    private final Browser browser;
    private final Map<Method, String> codeMap = new HashMap<>();

    public JavaScriptInvocationHandler(Browser browser) {
        this.browser = browser;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // Handle Object methods specially
        if (method.getDeclaringClass() == Object.class) {
            return switch (method.getName()) {
                case "equals" -> handleEquals(proxy, args[0]);
                case "hashCode" -> handleHashCode();
                case "toString" -> handleToString(proxy);
                case "getClass" -> method.getDeclaringClass();
                default -> throw new UnsupportedOperationException("Unsupported method: " + method);
            };
        }

        String code = getCode(method);

        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                code = code.replace("$" + i + "$", args[i].toString());
            }
        }

        final String codeToExecute = code;

        browser.getShell().getDisplay().asyncExec(() -> {
            if (!browser.execute(codeToExecute)) {
                throw new RuntimeException("JavaScript invocation failed");
            }
        });

        return null;
    }

    private boolean handleEquals(Object proxy, Object other) {
        if (proxy == other) {
            return true;
        }
        if (other == null || !Proxy.isProxyClass(other.getClass())) {
            return false;
        }
        var otherHandler = Proxy.getInvocationHandler(other);
        return this.equals(otherHandler);
    }

    private int handleHashCode() {
        return System.identityHashCode(this);
    }

    private String handleToString(Object proxy) {
        return "Proxy for interface: " + proxy.getClass().getInterfaces()[0].getName();
    }

    private String getCode(Method method) {
        if (!codeMap.containsKey(method)) {
            JavaScriptCode annotation = method.getAnnotation(JavaScriptCode.class);
            codeMap.put(method, annotation.value());
        }

        return codeMap.get(method);
    }
}
