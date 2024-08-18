package io.github.bitfist.particle.function.java.converter;

import java.util.List;

public class ArgumentsConverter {

    private final List<ValueConverter> converters;

    public ArgumentsConverter(List<ValueConverter> converters) {
        this.converters = converters;
    }

    public Object[] convert(Object[] arguments) {
        Object[] converted = new Object[arguments.length];

        for (int i = 0; i < arguments.length; i++) {
            converted[i] = converters.get(i).convert(arguments[i]);
        }

        return converted;
    }

}
