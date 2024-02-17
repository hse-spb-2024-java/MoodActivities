package server;

import services.AuthService;

import io.grpc.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AppServer {

    private final static Logger LOGGER = Logger.getLogger(
            AppServer.class.getName());

    public static void main(String[] args) {
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
