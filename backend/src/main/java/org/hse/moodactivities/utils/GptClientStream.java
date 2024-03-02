package org.hse.moodactivities.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GptClientStream {
    GptMessages history = new GptMessages();
    private static final Logger LOGGER = LoggerFactory.getLogger(GptClientStream.class);

    public GptMessages.GptMessage sendRequest(GptMessages.GptMessage message) {
        try {
            history.addMessage(message);
            GptMessages.GptMessage response = GptClientRequest.sendRequest(history);
            if (response != null) {
                history.addMessage(response);
            }
            return response;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }
}
