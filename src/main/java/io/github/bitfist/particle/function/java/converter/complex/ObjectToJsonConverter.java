package io.github.bitfist.particle.function.java.converter.complex;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bitfist.particle.function.java.converter.ValueConverter;

public class ObjectToJsonConverter implements ValueConverter {

    private final ObjectMapper objectMapper;

    public ObjectToJsonConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Object convert(Object value) {
        if (value == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(value);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert object to JSON", e);
        }
    }
}

