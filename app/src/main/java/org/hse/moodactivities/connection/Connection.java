package org.hse.moodactivities.connection;

import io.grpc.*;

import org.hse.moodactivities.common.proto.services.AuthServiceGrpc;
import org.hse.moodactivities.common.proto.requests.auth.*;
import org.hse.moodactivities.common.proto.responses.auth.*;

public class Connection {
    public static void main(String[] args) {
        // Create a new channel
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 12345)
                .usePlaintext()
                .build();

        AuthServiceGrpc.AuthServiceBlockingStub stub = AuthServiceGrpc.newBlockingStub(channel);

        RegistrationRequest request = RegistrationRequest.newBuilder().setUsername("World").build();

        // Call the sayHello method on the server
        RegistrationResponse response = stub.registration(request);

        System.out.println("Greeting: " + response.getMessage());

        channel.shutdown();
    }
}
