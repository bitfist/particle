package io.github.bitfist.particle.function.java.converter.primitive;

import io.github.bitfist.particle.function.java.converter.ValueConverter;

public class PassThroughConverter implements ValueConverter {

    @Override
    public Object convert(Object value) {
        return value;
    }
}
