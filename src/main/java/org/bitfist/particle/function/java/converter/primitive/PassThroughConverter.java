package org.bitfist.particle.function.java.converter.primitive;

import org.bitfist.particle.function.java.converter.ValueConverter;

public class PassThroughConverter implements ValueConverter {

    @Override
    public Object convert(Object value) {
        return value;
    }
}
