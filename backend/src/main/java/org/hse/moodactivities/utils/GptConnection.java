package org.hse.moodactivities.utils;

import org.jetbrains.annotations.NotNull;

import java.io.DataOutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GptConnection {
    // TODO read from env
    private static final String API_KEY = "YOUR_API_KEY_HERE";
    private static final Logger LOGGER = LoggerFactory.getLogger(GptConnection.class);

    @NotNull
    static HttpURLConnection getHttpURLConnection(GptPrompt prompt) {
        try {
            URL url = new URL("https://api.openai.com/v1/engines/text-davinci-003/completions");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + GptConnection.API_KEY);
            connection.setDoOutput(true);

            String payload = "{\"prompt\": \"" + prompt + "\", \"max_tokens\": 50}";

            try (DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {
                byte[] input = payload.getBytes(StandardCharsets.UTF_8);
                outputStream.write(input, 0, input.length);
            }
            return connection;
        }
        catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }
}
