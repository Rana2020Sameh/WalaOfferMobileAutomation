package utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
public class PropertyReader {
    private static Properties properties;
    private static final String CONFIG_FILE_PATH = "src/test/resources/config.properties";

    private PropertyReader() {
        // Private constructor to prevent instantiation
    }

    public static void loadProperties() {
        if (properties == null) {
            properties = new Properties();
            try (InputStream input = Files.newInputStream(Paths.get(CONFIG_FILE_PATH))) {
                properties.load(input);
            } catch (IOException ex) {
                ex.printStackTrace();
                throw new RuntimeException("Could not load config.properties file. " + ex.getMessage());
            }
        }
    }

    public static String getProperty(String key) {
        if (properties == null) {
            loadProperties();
        }
        String value = properties.getProperty(key);
        if (value == null) {
            System.err.println("Warning: Property '" + key + "' not found in " + CONFIG_FILE_PATH);
        }
        return value;
    }

    public static int getIntProperty(String key) {
        return Integer.parseInt(getProperty(key));
    }

    public static boolean getBooleanProperty(String key) {
        return Boolean.parseBoolean(getProperty(key));
    }
}