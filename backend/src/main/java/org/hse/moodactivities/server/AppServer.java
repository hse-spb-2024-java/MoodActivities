package org.hse.moodactivities.server;

import org.hse.moodactivities.interceptors.JWTAuthServerInterceptor;
import org.hse.moodactivities.services.ActivityService;
import org.hse.moodactivities.services.AuthService;
import org.hse.moodactivities.services.GptService;
import org.hse.moodactivities.services.ProfileService;
import org.hse.moodactivities.services.QuestionService;
import org.hse.moodactivities.services.StatsService;
import org.hse.moodactivities.services.SurveyService;
import org.hse.moodactivities.services.HealthService;
import org.hse.moodactivities.tools.Filler;
import org.hse.moodactivities.utils.StringGenerationService;
import org.hse.moodactivities.utils.UserProfileRepository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.github.cdimascio.dotenv.Dotenv;
import io.grpc.Server;
import io.grpc.ServerBuilder;

public class AppServer {

    private final static Logger LOGGER = Logger.getLogger(
            AppServer.class.getName());

    private static final Dotenv dotenv = Dotenv.load();

    public static void main(String[] args) {
        // Filler.runFiller("3");
        ExecutorService executor = Executors.newFixedThreadPool(10);
        StringGenerationService.startScheduledGeneration();

        UserProfileRepository.createPlainUserProfile(dotenv.get("ADMIN"), dotenv.get("EMAIL"), dotenv.get("PASSWORD"));

        Server server = ServerBuilder.forPort(12345)
                .executor(executor)
                .addService(new ActivityService())
                .addService(new AuthService())
                .addService(new GptService())
                .addService(new QuestionService())
                .addService(new ProfileService())
                .addService(new StatsService())
                .addService(new SurveyService())
                .addService(new HealthService())
                .intercept(new JWTAuthServerInterceptor())
                .build();

        try {
            server.start();
            server.awaitTermination();
        } catch (Exception e) {
            LOGGER.log(Level.ALL, e.getMessage());
        }
    }
}
