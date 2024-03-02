package org.hse.moodactivities.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import io.github.cdimascio.dotenv.Dotenv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GptClientRequest {
    private static final Logger LOGGER = LoggerFactory.getLogger(GptClientRequest.class);
    private static Dotenv dotenv = Dotenv.load();
    private static final String apiKey = dotenv.get("GPT_KEY");

    private static String bodyGeneration(String model, GptMessages messages, Double temperature) {
        return "{ \"model\": \"" + model + "\", \"messages\": " + messages + ", \"temperature\": " + temperature + "}";
    }

    public static GptMessages.GptMessage sendRequest(GptMessages messages) {
        try {
            String url = "https://api.openai.com/v1/chat/completions";
            String apiKey = ""; // TODO: READ FROM CONFIG
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(bodyGeneration("gpt-3.5-turbo", messages, 0.7)))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                JSONParser parser = new JSONParser();
                JSONObject body = (JSONObject) parser.parse(response.body());

                JSONArray choices = (JSONArray) body.getOrDefault("choices", null);
                if (choices != null) {
                    JSONObject firstChoice = (JSONObject) choices.get(0);
                    JSONObject messageJson = (JSONObject) firstChoice.get("message");
                    if (messageJson != null) {
                        String messageRole = (String) messageJson.get("role");
                        String messageContent = (String) messageJson.get("content");
                        return new GptMessages.GptMessage(GptMessages.GptMessage.Role.valueOf(messageRole), messageContent);
                    }
                }
            }
            // TODO handle net problems
            return null;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }
}
