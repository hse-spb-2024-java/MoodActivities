package org.hse.moodactivities.services;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.hse.moodactivities.common.proto.requests.survey.LongSurveyRequest;
import org.hse.moodactivities.common.proto.responses.survey.LongSurveyResponse;
import org.hse.moodactivities.common.proto.services.SurveyServiceGrpc;
import org.hse.moodactivities.utils.GptClientRequest;
import org.hse.moodactivities.utils.GptMessages;
import org.hse.moodactivities.utils.GptResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.grpc.ManagedChannel;
import io.grpc.Server;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;

public class SurveyServiceIntegrationTest {

    private Server server;
    private ManagedChannel channel;
    private SurveyServiceGrpc.SurveyServiceBlockingStub blockingStub;
    private SurveyService surveyService;

    @BeforeEach
    public void setup() throws Exception {
        surveyService = new SurveyService();
        String serverName = InProcessServerBuilder.generateName();
        server = InProcessServerBuilder.forName(serverName)
                .directExecutor()
                .addService(surveyService)
                .build()
                .start();

        channel = InProcessChannelBuilder.forName(serverName)
                .directExecutor()
                .build();

        blockingStub = SurveyServiceGrpc.newBlockingStub(channel);
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
    public void testLongSurvey() {
        GptResponse mockResponse = mock(GptResponse.class);
        when(mockResponse.statusCode()).thenReturn(200);
        when(mockResponse.message()).thenReturn(new GptMessages.GptMessage(GptMessages.GptMessage.Role.user, "This is a test response."));
        GptClientRequest.sendRequest(new GptMessages(GptMessages.GptMessage.Role.user, "test request"));

        LongSurveyRequest request = LongSurveyRequest.newBuilder()
                .setLat(55.7558)
                .setLon(37.6173)
                .setMoodRating(5)
                .build();

        LongSurveyResponse response = blockingStub.longSurvey(request);

        assertNotNull(response);
        assertNotEquals(response.getShortSummary(), "");
        assertNotEquals(response.getFullSummary(), "");
    }
}
