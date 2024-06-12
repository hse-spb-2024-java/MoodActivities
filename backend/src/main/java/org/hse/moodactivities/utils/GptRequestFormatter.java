package org.hse.moodactivities.utils;

import org.hse.moodactivities.common.proto.requests.survey.LongSurveyRequest;
import org.hse.moodactivities.data.promts.PromptsStorage;

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
        message.append(PromptsStorage.getString("surveyPrompt.basePromptWithActivities"));
        for (var activity : request.getActivitiesList()) {
            message.append(String.format("%s, ", activity));
        }
        message.append(PromptsStorage.getString("surveyPrompt.addEmotions"));
        for (var emotion : request.getEmotionsList()) {
            message.append(String.format("%s, ", emotion));
        }
        message.append(String.format(PromptsStorage.getString("surveyPrompt.addQuestion"), request.getQuestion(), request.getAnswer()));
        message.append(PromptsStorage.getString("surveyPrompt.personalAdvice"));
        message = StringValidator(message.toString());
        return new GptMessages.GptMessage(GptMessages.GptMessage.Role.user, message.toString());
    }
}
