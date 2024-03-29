package org.hse.moodactivities.server;

import io.grpc.*;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureAlgorithm;
import org.hse.moodactivities.interceptors.*;
import org.hse.moodactivities.services.*;
import org.hse.moodactivities.utils.UserProfileRepository;

import java.security.KeyPair;
import java.util.Base64;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AppServer {

    private final static Logger LOGGER = Logger.getLogger(
            AppServer.class.getName());

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(10);

        UserProfileRepository.createUserProfile("admin",  "12345678");

        Server server = ServerBuilder.forPort(12345)
                .executor(executor)
                .addService(new AuthService())
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
