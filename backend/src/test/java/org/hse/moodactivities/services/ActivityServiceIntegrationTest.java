package org.hse.moodactivities.services;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.hse.moodactivities.common.proto.requests.activity.GetActivityRequest;
import org.hse.moodactivities.common.proto.requests.activity.RecordActivityRequest;
import org.hse.moodactivities.common.proto.responses.activity.GetActivityResponse;
import org.hse.moodactivities.common.proto.responses.activity.RecordActivityResponse;
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
        String serverName = InProcessServerBuilder.generateName();
        server = InProcessServerBuilder.forName(serverName)
                .directExecutor()
                .addService(new ActivityService())
                .build()
                .start();

        channel = InProcessChannelBuilder.forName(serverName)
                .directExecutor()
                .build();

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
        GetActivityRequest request = GetActivityRequest.newBuilder()
                .setDate("2024-06-22")
                .build();

        GetActivityResponse response = blockingStub.getActivity(request);
        assertEquals(200, response.getStatusCode());
        assertFalse(response.getActivity().equals(""));
    }

    @Test
    public void testRecordActivity() {
        RecordActivityRequest request = RecordActivityRequest.newBuilder()
                .setDate("2024-06-22")
                .setRecord("Ok.")
                .build();

        RecordActivityResponse response = blockingStub.recordActivity(request);
        assertEquals(200, response.getStatusCode());
    }
}
