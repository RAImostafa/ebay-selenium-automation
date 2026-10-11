package pages;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/* Reads test data from test-data.properties on the classpath. */
public class TestDataReader {

    private final Properties props = new Properties();

    public TestDataReader() {
        try (InputStream in = TestDataReader.class.getClassLoader()
                .getResourceAsStream("test-data.properties")) {
            if (in == null) {
                throw new IllegalStateException("test-data.properties not found on classpath");
            }
            props.load(in);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load test-data.properties", e);
        }
    }

    public String get(String key) {
        String value = props.getProperty(key);
        if (value == null) {
            throw new IllegalStateException("Missing key in test-data.properties: " + key);
        }
        return value;
    }
}


