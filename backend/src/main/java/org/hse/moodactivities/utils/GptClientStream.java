package org.hse.moodactivities.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GptClientStream {
    ArrayList<GptPrompt> history;
    private static final Logger LOGGER = LoggerFactory.getLogger(GptClientStream.class);

    public static String sendRequest(String message) {
        try {
            GptPrompt prompt = new GptPrompt("user", message);
            HttpURLConnection connection = GptConnection.getHttpURLConnection(prompt);

            StringBuilder response = new StringBuilder();
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
            }
            connection.disconnect();
            return response.toString();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }
}
