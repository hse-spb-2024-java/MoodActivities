package org.hse.moodactivities.utils;

import java.util.Objects;

public class GptResponse {
    private final GptMessages.GptMessage message;
    private final Integer statusCode;
    private final String response;

    public GptResponse(GptMessages.GptMessage message, Integer statusCode, String response) {
        this.message = message;
        this.statusCode = statusCode;
        this.response = response;
    }

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

    public GptMessages.GptMessage message() {
        return message;
    }

    public Integer statusCode() {
        return statusCode;
    }

    public String response() {
        return response;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (GptResponse) obj;
        return Objects.equals(this.message, that.message) &&
                Objects.equals(this.statusCode, that.statusCode) &&
                Objects.equals(this.response, that.response);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, statusCode, response);
    }

    @Override
    public String toString() {
        return "GptResponse[" +
                "message=" + message + ", " +
                "statusCode=" + statusCode + ", " +
                "response=" + response + ']';
    }

}
