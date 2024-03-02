package org.hse.moodactivities.utils;

public record GptResponse (GptMessages.GptMessage message, Integer statusCode, String response){
    public GptResponse(GptMessages.GptMessage message, int code) {
        this(message, code, null);
    }
    public GptResponse(int statusCode) {
        this(null, statusCode, null);
    }

    public GptResponse(int statusCode, String response) {
        this(null, statusCode, response);
    }

    public GptResponse(String response) {
        this(null, null, response);
    }
}
