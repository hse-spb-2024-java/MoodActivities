package org.hse.moodactivities.utils;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;

import org.hse.moodactivities.data.promts.PromptsStorage;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

import io.github.cdimascio.dotenv.Dotenv;

public class WeatherApp {
    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherApp.class);

    private static Dotenv dotenv = Dotenv.load();
    private static final String API_KEY = dotenv.get("OPEN_WEATHER_MAP_KEY");
    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather";

    private static Optional<String> retranslateWeather(String weather) {
        String prompt = String.format(PromptsStorage.getString("weatherApp.retranslateWeather"), weather);
        GptResponse response = GptClientRequest.sendRequest(new GptMessages(GptMessages.GptMessage.Role.user, prompt));
        if (response.statusCode() < HTTP_BAD_REQUEST) {
            return Optional.of(response.message().getContent());
        }
        return Optional.empty();
    }

    public static Optional<String> getWeather(double latitude, double longitude) {
        String url = String.format("%s?lat=%f&lon=%f&appid=%s&units=metric", BASE_URL, latitude, longitude, API_KEY);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JSONObject jsonResponse = new JSONObject(response.body());
                System.out.println(jsonResponse);
                String weatherDescription = jsonResponse.getJSONArray("weather").getJSONObject(0).getString("description");
                return retranslateWeather(weatherDescription);
            } else {
                LOGGER.error("Error: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            LOGGER.error(e.getMessage());
        }
        return Optional.empty();
    }
}
