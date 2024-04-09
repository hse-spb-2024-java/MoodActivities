package org.hse.moodactivities.data.promts;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PromptsStorage {
    private static Properties properties;
    private static String PROMPTS = "prompts.properties";

    static {
        properties = new Properties();
        try (InputStream inputStream = PromptsStorage.class.getClassLoader().getResourceAsStream(PROMPTS);) {
            properties.load(inputStream);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getString(String key) {
        return properties.getProperty(key);
    }
}
