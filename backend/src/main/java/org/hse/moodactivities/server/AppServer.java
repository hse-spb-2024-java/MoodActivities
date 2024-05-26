package org.hse.moodactivities.server;

import org.hse.moodactivities.interceptors.JWTAuthServerInterceptor;
import org.hse.moodactivities.services.ActivityService;
import org.hse.moodactivities.services.AuthService;
import org.hse.moodactivities.services.GptService;
import org.hse.moodactivities.services.QuestionService;
import org.hse.moodactivities.services.StatsService;
import org.hse.moodactivities.services.SurveyService;
import org.hse.moodactivities.utils.StringGenerationService;
import org.hse.moodactivities.utils.UserProfileRepository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class AppServer {

    private final static Logger LOGGER = Logger.getLogger(
            AppServer.class.getName());

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        StringGenerationService.startScheduledGeneration();

        UserProfileRepository.createPlainUserProfile("admin", "", "12345678");

        Server server = ServerBuilder.forPort(12345)
                .executor(executor)
                .addService(new ActivityService())
                .addService(new AuthService())
                .addService(new GptService())
                .addService(new QuestionService())
                .addService(new StatsService())
                .addService(new SurveyService())
                .intercept(new JWTAuthServerInterceptor())
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
