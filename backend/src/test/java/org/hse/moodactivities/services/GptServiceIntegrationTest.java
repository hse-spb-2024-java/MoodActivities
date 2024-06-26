package org.hse.moodactivities.services;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.hse.moodactivities.common.proto.requests.gpt.GptSessionRequest;
import org.hse.moodactivities.common.proto.requests.gpt.MetaToGptRequest;
import org.hse.moodactivities.common.proto.responses.gpt.GptSessionResponse;
import org.hse.moodactivities.common.proto.responses.gpt.MetaToGptResponse;
import org.hse.moodactivities.common.proto.services.GptServiceGrpc;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.grpc.ManagedChannel;
import io.grpc.Server;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.stub.StreamObserver;

public class GptServiceIntegrationTest {

    private Server server;
    private ManagedChannel channel;
    private GptServiceGrpc.GptServiceBlockingStub blockingStub;
    private GptServiceGrpc.GptServiceStub asyncStub;

    @BeforeEach
    public void setup() throws Exception {
        String serverName = InProcessServerBuilder.generateName();
        server = InProcessServerBuilder.forName(serverName)
                .directExecutor()
                .addService(new GptService())
                .build()
                .start();

        channel = InProcessChannelBuilder.forName(serverName)
                .directExecutor()
                .build();

        blockingStub = GptServiceGrpc.newBlockingStub(channel);
        asyncStub = GptServiceGrpc.newStub(channel);
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
    public void testGptSession() throws Exception {
        StreamObserver<GptSessionResponse> responseObserver = new StreamObserver<GptSessionResponse>() {
            @Override
            public void onNext(GptSessionResponse value) {
                assertEquals("Test response", value.getMessage());
                assertEquals(200, value.getResponseCode());
            }

            @Override
            public void onError(Throwable t) {
                throw new RuntimeException(t);
            }

            @Override
            public void onCompleted() {
                // Test completed successfully
            }
        };

        StreamObserver<GptSessionRequest> requestObserver = asyncStub.gptSession(responseObserver);

        GptSessionRequest request = GptSessionRequest.newBuilder()
                .setMessage("Test message")
                .build();

        requestObserver.onNext(request);
        requestObserver.onNext(request);
        requestObserver.onCompleted();
    }

    @Test
    public void testAskWithMeta() {
        MetaToGptRequest request = MetaToGptRequest.newBuilder()
                .setMeta("Test meta")
                .setMessage("Test message")
                .build();

        MetaToGptResponse response = blockingStub.askWithMeta(request);

        assertFalse(response.getMessage().equals(""));
        assertEquals(200, response.getResponseCode());
    }
}
