package tempest_double;

import jakarta.persistence.AttributeConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tempest_double.entity.JsonConverter;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
class JsonConverterTest {

    private JsonConverter jsonConverter;

    @BeforeEach
    void setUp() {
        jsonConverter = new JsonConverter();
    }

    @Test
    void testConvertToDatabaseColumnWithValidMap() {
        Map<String, Object> attribute = new HashMap<>();
        attribute.put("key1", "value1");
        attribute.put("key2", 42);

        String result = jsonConverter.convertToDatabaseColumn(attribute);

        assertNotNull(result, "Resulting JSON string should not be null.");
        assertTrue(result.contains("\"key1\":\"value1\""), "JSON string should contain key1 and its value.");
        assertTrue(result.contains("\"key2\":42"), "JSON string should contain key2 and its value.");
    }

    @Test
    void testConvertToDatabaseColumnWithNullMap() {
        String result = jsonConverter.convertToDatabaseColumn(null);

        assertNull(result, "Result should be null when the input Map is null.");
    }

    @Test
    void testConvertToDatabaseColumnWithEmptyMap() {
        Map<String, Object> attribute = new HashMap<>();

        String result = jsonConverter.convertToDatabaseColumn(attribute);

        assertEquals("{}", result, "Result should be an empty JSON object for an empty Map.");
    }

    @Test
    void testConvertToDatabaseColumnThrowsException() {
        AttributeConverter<Map<String, Object>, String> faultyConverter = new JsonConverter() {
            @Override
            public String convertToDatabaseColumn(Map<String, Object> attribute) {
                throw new IllegalArgumentException("Simulated error");
            }
        };

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                faultyConverter.convertToDatabaseColumn(Map.of("key", new Object()))
        );

        assertTrue(exception.getMessage().contains("Simulated error"), "Exception message should indicate the simulated error.");
    }

    @Test
    void testConvertToEntityAttributeWithValidJson() {
        String dbData = "{\"key1\":\"value1\",\"key2\":42}";

        Map<String, Object> result = jsonConverter.convertToEntityAttribute(dbData);

        assertNotNull(result, "Resulting Map should not be null.");
        assertEquals("value1", result.get("key1"), "Map should contain key1 with the correct value.");
        assertEquals(42, result.get("key2"), "Map should contain key2 with the correct value.");
    }

    @Test
    void testConvertToEntityAttributeWithNullJson() {
        Map<String, Object> result = jsonConverter.convertToEntityAttribute(null);

        assertNull(result, "Result should be null when the input JSON is null.");
    }

    @Test
    void testConvertToEntityAttributeWithEmptyJson() {
        String dbData = "{}";

        Map<String, Object> result = jsonConverter.convertToEntityAttribute(dbData);

        assertNotNull(result, "Resulting Map should not be null.");
        assertTrue(result.isEmpty(), "Resulting Map should be empty for an empty JSON string.");
    }

    @Test
    void testConvertToEntityAttributeThrowsException() {
        String invalidJson = "{invalid-json}";

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                jsonConverter.convertToEntityAttribute(invalidJson)
        );

        assertTrue(exception.getMessage().contains("Error converting JSON to Map"),
                "Exception message should indicate an error in JSON conversion.");
    }
}