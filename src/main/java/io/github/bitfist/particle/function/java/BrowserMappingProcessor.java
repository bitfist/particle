package io.github.bitfist.particle.function.java;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bitfist.particle.function.java.converter.ArgumentsConverter;
import io.github.bitfist.particle.function.java.converter.ValueConverter;
import io.github.bitfist.particle.function.java.converter.complex.JsonToObjectConverter;
import io.github.bitfist.particle.function.java.converter.complex.ObjectToJsonConverter;
import io.github.bitfist.particle.function.java.converter.primitive.DoubleToByteConverter;
import io.github.bitfist.particle.function.java.converter.primitive.DoubleToFloatConverter;
import io.github.bitfist.particle.function.java.converter.primitive.DoubleToIntegerConverter;
import io.github.bitfist.particle.function.java.converter.primitive.DoubleToLongConverter;
import io.github.bitfist.particle.function.java.converter.primitive.DoubleToShortConverter;
import io.github.bitfist.particle.function.java.converter.primitive.PassThroughConverter;
import io.github.bitfist.particle.function.java.mapping.BrowserCallable;
import io.github.bitfist.particle.function.java.mapping.BrowserConsumer;
import io.github.bitfist.particle.function.java.mapping.BrowserFunction;
import io.github.bitfist.particle.function.java.mapping.BrowserProducer;
import org.eclipse.swt.browser.Browser;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

/**
 * The {@link BrowserMappingProcessor} processes the {@link #browserMappings}, searches for methods annotated with
 * [BrowserMapping] and creates {@link org.eclipse.swt.browser.BrowserFunction}s around them. Primitive objects are
 * directly supported, but complex objects are converted to and from JSON. Keep this in mind when calling the functions
 * from the browser (use `JSON.stringify` and `JSON.parse`).
 */
public class BrowserMappingProcessor {

    private static final Logger log = Logger.getLogger(BrowserMappingProcessor.class.getName());

    private final Browser browser;
    private final ObjectMapper objectMapper;
    private final List<String> numberTypes = List.of("java.lang.Byte", "java.lang.Short", "java.lang.Integer", "java.lang.Long", "java.lang.Float", "java.lang.Double");
    private final List<String> primitiveTypes = new ArrayList<>(numberTypes);

    public BrowserMappingProcessor(Browser browser, ObjectMapper objectMapper) {
        this.browser = browser;
        this.objectMapper = objectMapper;

        primitiveTypes.add("char");
        primitiveTypes.add("java.lang.String");
    }

    public void processBrowserMappings(Collection<Object> browserMappings) {
        browserMappings.forEach(this::processBrowserMappings);
    }

    private void processBrowserMappings(Object browserMappingObject) {
        BrowserMapping classAnnotation = browserMappingObject.getClass().getDeclaredAnnotation(BrowserMapping.class);
        String classPrefix = classAnnotation.prefix();

        Arrays.stream(browserMappingObject.getClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(BrowserMapping.class))
                .forEach(method -> processBrowserMapping(classPrefix, browserMappingObject, method));
    }

    private void processBrowserMapping(String classPrefix, Object bean, Method method) {
        BrowserMapping methodAnnotation = method.getDeclaredAnnotation(BrowserMapping.class);
        String methodPrefix = methodAnnotation.prefix();
        Class<?> returnType = method.getReturnType();
        boolean hasReturnType = !returnType.getName().equals("void");
        boolean hasParameters = method.getParameters().length > 0;
        String browserMappingName = createBrowserMappingName(classPrefix, methodPrefix, method.getName());

        log.info("Mapping method " + method.getName() + " to " + browserMappingName);

        if (!hasParameters && !hasReturnType) {
            new BrowserCallable(bean, method, browser, browserMappingName);
        } else if (hasParameters && !hasReturnType) {
            new BrowserConsumer(createArgumentsConverter(method), bean, method, browser, browserMappingName);
        } else if (!hasParameters) {
            new BrowserProducer(createReturnValueConverter(returnType), bean, method, browser, browserMappingName);
        } else {
            new BrowserFunction(
                    createArgumentsConverter(method),
                    createReturnValueConverter(returnType),
                    bean,
                    method,
                    browser,
                    browserMappingName
            );
        }
    }

    /**
     * Creates a name for a browser mapping: `prefixFromClass_prefixFromMethod_methodName`
     *
     * @param classPrefix  the prefix provided through the class annotation; can be empty
     * @param methodPrefix the prefix provided through the method annotation; can be empty
     * @param methodName   the name of the annotated method;
     * @return the browser mapping name containing the prefixes
     */
    private String createBrowserMappingName(String classPrefix, String methodPrefix, String methodName) {
        String name = "";

        if (!classPrefix.trim().isEmpty()) {
            name = classPrefix + "_";
        }
        if (!methodPrefix.trim().isEmpty()) {
            name = name + methodPrefix + "_";
        }

        name += methodName;

        return name;
    }

    /**
     * Creates an {@link ArgumentsConverter} to convert all arguments. Note that numbers are exclusively double in
     * JavaScript.
     */
    private ArgumentsConverter createArgumentsConverter(Method method) {
        List<ValueConverter> valueConverters = Arrays
                .stream(method.getParameters())
                .map(this::createArgumentConverter)
                .toList();
        return new ArgumentsConverter(valueConverters);
    }

    private ValueConverter createArgumentConverter(Parameter parameter) {
        Class<?> type = parameter.getType();
        if (isNumberType(type)) {
            String numberType = type.getName();
            return switch (numberType) {
                case "java.lang.Byte" -> new DoubleToByteConverter();
                case "java.lang.Short" -> new DoubleToShortConverter();
                case "java.lang.Integer" -> new DoubleToIntegerConverter();
                case "java.lang.Long" -> new DoubleToLongConverter();
                case "java.lang.Float" -> new DoubleToFloatConverter();
                default -> new PassThroughConverter();
            };
        } else if (isPrimitiveType(type)) {
            return new PassThroughConverter();
        } else {
            return new JsonToObjectConverter(objectMapper, type);
        }
    }

    private ValueConverter createReturnValueConverter(Class<?> returnType) {
        if (isPrimitiveType(returnType)) {
            return new PassThroughConverter();
        }
        return new ObjectToJsonConverter(objectMapper);
    }

    private boolean isNumberType(Class<?> type) {
        return numberTypes.contains(type.getName());
    }

    private boolean isPrimitiveType(Class<?> type) {
        return primitiveTypes.contains(type.getName());
    }
}
