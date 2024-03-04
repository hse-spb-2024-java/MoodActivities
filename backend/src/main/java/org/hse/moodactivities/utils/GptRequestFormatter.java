package org.hse.moodactivities.utils;

import org.hse.moodactivities.common.proto.requests.survey.*;

public class GptRequestFormatter {
    public static GptMessages.GptMessage surveyRequest(LongSurveyRequest request) {
        StringBuilder message = new StringBuilder();
        message.append("Summarize the user's mental state for the day and provide two responses: one succinct (exactly three words), the other expanded (no more than 200 characters), given the following activities: ");
        for (var activity : request.getActivitiesList()) {
            message.append(String.format("%s, ", activity));
        }
        message.append("they experienced the following emotions: ");
        for (var emotion : request.getEmotionsList()) {
            message.append(String.format("%s, ", emotion));
        }
        message.append(String.format("and in response to the question: \"%s\", they replied: \"%s\"", request.getQuestion(), request.getAnswer()));
        return new GptMessages.GptMessage(GptMessages.GptMessage.Role.user, message.toString());
    }
}
