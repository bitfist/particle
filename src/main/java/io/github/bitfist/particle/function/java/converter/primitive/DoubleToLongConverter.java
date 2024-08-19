package io.github.bitfist.particle.function.java.converter.primitive;

import io.github.bitfist.particle.function.java.converter.ValueConverter;

public class DoubleToLongConverter implements ValueConverter {

    @Override
    public Object convert(Object value) {
        if (value == null) {
            return null;
        }
        return ((Number) value).longValue();
    }
}
