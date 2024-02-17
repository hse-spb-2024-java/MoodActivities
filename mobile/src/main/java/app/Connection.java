package main.java.app;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import org.hse.moodactivities.common.proto.services.AuthServiceGrpc;
import org.hse.moodactivities.common.utils.proto.requests.auth.*;
import org.hse.moodactivities.common.utils.proto.responses.auth.*;

public class Connection {
    public static void main(String[] args) {
        // Create a new channel
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 12345)
                .usePlaintext()
                .build();

        AuthServiceGrpc.AuthServiceBlockingStub stub = AuthServiceGrpc.newBlockingStub(channel);

        RegistrRequest request = RegistrRequest.newBuilder().setNickname("World").build();

        // Call the sayHello method on the server
        RegistrResponse response = stub.register(request);

        System.out.println("Greeting: " + response.getMessage());

        channel.shutdown();
    }
}
