package org.hse.moodactivities.utils;

import org.hse.moodactivities.common.proto.requests.survey.*;

import java.nio.charset.StandardCharsets;

public class GptRequestFormatter {

    public static StringBuilder StringValidator(String input) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c == '"') {
                if (i > 0 && input.charAt(i - 1) != '\\') {
                    result.append("\\\"");
                } else if (i == 0) {
                    result.append("\\\"");
                } else {
                    result.append("\"");
                }
            } else {
                result.append(c);
            }
        }

        return result;
    }

    public static GptMessages.GptMessage surveyRequest(LongSurveyRequest request) {
        StringBuilder message = new StringBuilder();
        message.append("What can you say about user's mental state for the day and provide two responses: one succinct (exactly three words), the other expanded (no more than 200 characters), given the following activities: ");
        for (var activity : request.getActivitiesList()) {
            message.append(String.format("%s, ", activity));
        }
        message.append("they experienced the following emotions: ");
        for (var emotion : request.getEmotionsList()) {
            message.append(String.format("%s, ", emotion));
        }
        message.append(String.format("and in response to the question: \\\"%s\\\", they replied: \\\"%s\\\"", request.getQuestion(), request.getAnswer()));
        message = StringValidator(message.toString());
        return new GptMessages.GptMessage(GptMessages.GptMessage.Role.user, message.toString());
    }
}
