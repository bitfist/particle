package io.github.bitfist.particle.function.java.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bitfist.particle.function.java.TestObject;
import io.github.bitfist.particle.function.java.converter.complex.JsonToObjectConverter;
import io.github.bitfist.particle.function.java.converter.complex.ObjectToJsonConverter;
import io.github.bitfist.particle.function.java.converter.primitive.DoubleToByteConverter;
import io.github.bitfist.particle.function.java.converter.primitive.DoubleToFloatConverter;
import io.github.bitfist.particle.function.java.converter.primitive.DoubleToIntegerConverter;
import io.github.bitfist.particle.function.java.converter.primitive.DoubleToLongConverter;
import io.github.bitfist.particle.function.java.converter.primitive.DoubleToShortConverter;
import io.github.bitfist.particle.function.java.converter.primitive.PassThroughConverter;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ArgumentsConverterTest {

    private final ArgumentsConverter target = new ArgumentsConverter(
            Arrays.asList(
                    new DoubleToByteConverter(),
                    new DoubleToShortConverter(),
                    new DoubleToFloatConverter(),
                    new DoubleToIntegerConverter(),
                    new DoubleToLongConverter(),
                    new PassThroughConverter(),
                    new ObjectToJsonConverter(new ObjectMapper()),
                    new JsonToObjectConverter(new ObjectMapper(), TestObject.class)
            )
    );

    @Test
    void convertArguments() {
        Object[] values = target.convert(new Object[]{12.0, 999, 999.7, 100_000.0, 100_000.0, "test", new TestObject(6, "hello"), "{\"id\":7,\"name\":\"blah\"}"});

        assertEquals((byte) 12, values[0]);
        assertEquals((short) 999, values[1]);
        assertEquals(999.7f, values[2]);
        assertEquals(100_000, values[3]);
        assertEquals(100_000L, values[4]);
        assertEquals("test", values[5]);
        assertEquals("{\"id\":6,\"name\":\"hello\"}", values[6]);
        TestObject value7 = (TestObject) values[7];
        assertEquals(7, value7.getId());
        assertEquals("blah", value7.getName());
    }

    @Test
    void convertNull() {
        Object[] values = target.convert(new Object[] { null, null, null, null, null, null, null, null});

        for (Object value : values) {
            assertNull(value);
        }
    }
}
