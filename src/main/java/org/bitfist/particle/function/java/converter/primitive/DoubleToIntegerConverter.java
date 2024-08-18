package org.bitfist.particle.function.java.converter.primitive;

import org.bitfist.particle.function.java.converter.ValueConverter;

public class DoubleToIntegerConverter implements ValueConverter {

    @Override
    public Object convert(Object value) {
        if (value == null) {
            return null;
        }
        return ((Number) value).intValue();
    }
}

