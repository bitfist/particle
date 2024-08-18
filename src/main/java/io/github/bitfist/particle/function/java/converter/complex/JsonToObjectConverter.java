package io.github.bitfist.particle.function.java.converter.complex;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bitfist.particle.function.java.converter.ValueConverter;

public class JsonToObjectConverter implements ValueConverter {

    private final ObjectMapper objectMapper;
    private final Class<?> type;

    public JsonToObjectConverter(ObjectMapper objectMapper, Class<?> type) {
        this.objectMapper = objectMapper;
        this.type = type;
    }

    @Override
    public Object convert(Object value) {
        if (value == null) {
            return null;
        }
        try {
            return objectMapper.readValue((String) value, type);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert value", e);
        }
    }
}
