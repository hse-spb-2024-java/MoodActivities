package org.hse.moodactivities.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.hse.moodactivities.common.proto.requests.activity.GetActivityRequest;
import org.hse.moodactivities.common.proto.responses.activity.GetActivityResponse;
import org.hse.moodactivities.common.proto.services.ActivityServiceGrpc;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.grpc.ManagedChannel;
import io.grpc.Server;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;

public class ActivityServiceIntegrationTest {

    private Server server;
    private ManagedChannel channel;
    private ActivityServiceGrpc.ActivityServiceBlockingStub blockingStub;

    @BeforeEach
    public void setup() throws Exception {
        // Initialize and start the in-process server
        String serverName = InProcessServerBuilder.generateName();
        server = InProcessServerBuilder.forName(serverName)
                .directExecutor()
                .addService(new ActivityService())
                .build()
                .start();

        // Initialize the in-process channel
        channel = InProcessChannelBuilder.forName(serverName)
                .directExecutor()
                .build();

        // Create a blocking stub
        blockingStub = ActivityServiceGrpc.newBlockingStub(channel);
    }

    @AfterEach
    public void teardown() {
        if (channel != null) {
            channel.shutdownNow();
        }
        if (server != null) {
            server.shutdownNow();
        }
    }

    @Test
    public void testGetActivity() {
        // Create a GetActivityRequest
        GetActivityRequest request = GetActivityRequest.newBuilder()
                .setDate("2024-06-22")
                .build();

        // Call the service
        GetActivityResponse response = blockingStub.getActivity(request);

        // Validate the response
        assertEquals(200, response.getStatusCode());
        // Add additional assertions based on the expected response
    }

    // Add more test methods for other RPC methods
}
