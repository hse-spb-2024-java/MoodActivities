package org.hse.moodactivities.server;

import org.hse.moodactivities.services.AuthService;

import io.grpc.*;
import org.hse.moodactivities.utils.GptClientRequest;
import org.hse.moodactivities.utils.GptClientStream;
import org.hse.moodactivities.utils.GptMessages;
import org.hse.moodactivities.utils.GptResponse;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AppServer {

    private final static Logger LOGGER = Logger.getLogger(
            AppServer.class.getName());

    public static void main(String[] args) {
        GptMessages.GptMessage first = new GptMessages.GptMessage(GptMessages.GptMessage.Role.user, "what is an orange?");
        GptMessages.GptMessage second = new GptMessages.GptMessage(GptMessages.GptMessage.Role.user, "what colour is it?");
        GptClientStream stream = new GptClientStream();
        GptResponse response1 = stream.sendRequest(first);
        GptResponse response2 = stream.sendRequest(second);
        System.out.println(response1);
        System.out.println(response2);

        ExecutorService executor = Executors.newFixedThreadPool(10);

        Server server = ServerBuilder.forPort(12345)
                .executor(executor)
                .addService(new AuthService())
                .build();

        try {
            server.start();
            int tic = 0;
            while (tic != 1) {
                tic += 2;
            }
            server.awaitTermination();
        } catch (Exception e) {
            LOGGER.log(Level.ALL, e.getMessage());
        }
    }
}
