package org.hse.moodactivities.utils;

import org.hse.moodactivities.data.entities.mongodb.Question;
import org.hse.moodactivities.data.promts.PromptsStorage;

import java.net.HttpURLConnection;
import java.time.LocalDate;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class StringGenerationService {
    private static String lastGeneratedString;

    public static void generateString() {
        StringBuilder requestString = new StringBuilder(PromptsStorage.getString("dailyQuestion.request"));
        GptResponse response = GptClientRequest.sendRequest(new GptMessages(GptMessages.GptMessage.Role.user, requestString.toString()));
        if (response.statusCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
            lastGeneratedString = response.message().getContent();
            MongoDBSingleton instance = MongoDBSingleton.getInstance();
            String id = String.valueOf(instance.getQuestionsConnection().count(new Question()));
            Question question = new Question(id, lastGeneratedString, LocalDate.now());
            instance.getQuestionsConnection().saveEntity(question);
        }
    }

    public static String getRandomQuestion() {
        StringBuilder requestString = new StringBuilder(PromptsStorage.getString("dailyQuestion.request"));
        GptResponse response = GptClientRequest.sendRequest(new GptMessages(GptMessages.GptMessage.Role.user, requestString.toString()));
        if (response.statusCode() != null && response.statusCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
            return response.message().getContent();
        }
        return response.response();
    }

    public static String getLastGeneratedString() {
        synchronized (StringGenerationService.class) {
            if (lastGeneratedString == null) {
                generateString();
            }
            return lastGeneratedString;
        }
    }

    public static void startScheduledGeneration() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            generateString();
        }, 0, 1, TimeUnit.DAYS);
    }
}
