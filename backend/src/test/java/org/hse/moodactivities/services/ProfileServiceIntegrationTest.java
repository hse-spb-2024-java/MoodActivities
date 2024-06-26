package org.hse.moodactivities.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.hse.moodactivities.common.proto.requests.profile.ChangeInfoRequest;
import org.hse.moodactivities.common.proto.requests.profile.CheckPasswordRequest;
import org.hse.moodactivities.common.proto.requests.profile.FeedbackRequest;
import org.hse.moodactivities.common.proto.requests.profile.GetInfoRequest;
import org.hse.moodactivities.common.proto.responses.profile.ChangeInfoResponse;
import org.hse.moodactivities.common.proto.responses.profile.CheckPasswordResponse;
import org.hse.moodactivities.common.proto.responses.profile.FeedbackResponse;
import org.hse.moodactivities.common.proto.responses.profile.GetInfoResponse;
import org.hse.moodactivities.common.proto.services.ProfileServiceGrpc;
import org.hse.moodactivities.data.entities.mongodb.User;
import org.hse.moodactivities.data.entities.postgres.UserProfile;
import org.hse.moodactivities.utils.JWTUtils.JWTUtils;
import org.hse.moodactivities.utils.UserProfileRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.Optional;

import io.grpc.ManagedChannel;
import io.grpc.Server;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;

public class ProfileServiceIntegrationTest {

    private Server server;
    private ManagedChannel channel;
    private ProfileServiceGrpc.ProfileServiceBlockingStub blockingStub;

    @BeforeEach
    public void setup() throws Exception {
        String serverName = InProcessServerBuilder.generateName();
        server = InProcessServerBuilder.forName(serverName)
                .directExecutor()
                .addService(new ProfileService())
                .build()
                .start();

        channel = InProcessChannelBuilder.forName(serverName)
                .directExecutor()
                .build();

        blockingStub = ProfileServiceGrpc.newBlockingStub(channel);
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
    public void testGetInfo() {
        String userId = "testUser";
        UserProfile testUserProfile = new UserProfile();
        testUserProfile.setName("Test Name");
        testUserProfile.setLogin("testLogin");
        testUserProfile.setEmail("test@example.com");
        testUserProfile.setDayOfBirth("2000-01-01");

        try (MockedStatic<JWTUtils> jwtUtilsMockedStatic = Mockito.mockStatic(JWTUtils.class);
             MockedStatic<UserProfileRepository> userProfileRepositoryMockedStatic = Mockito.mockStatic(UserProfileRepository.class)) {

            jwtUtilsMockedStatic.when(JWTUtils::getClientIdContextKey).thenReturn(userId);
            userProfileRepositoryMockedStatic.when(() -> UserProfileRepository.findById(userId)).thenReturn(Optional.of(testUserProfile));

            GetInfoRequest request = GetInfoRequest.newBuilder().build();
            GetInfoResponse response = blockingStub.getInfo(request);

            assertEquals("Test Name", response.getName());
            assertEquals("testLogin", response.getLogin());
            assertEquals("test@example.com", response.getEmail());
            assertEquals("2000-01-01", response.getDateOfBirth());
            assertFalse(response.getGoogleEnabled());
        }
    }

    @Test
    public void testChangeInfo() {
        String userId = "testUser";
        UserProfile testUserProfile = new UserProfile();
        testUserProfile.setName("Test Name");
        testUserProfile.setLogin("testLogin");
        testUserProfile.setEmail("test@example.com");
        testUserProfile.setDayOfBirth("2000-01-01");

        try (MockedStatic<JWTUtils> jwtUtilsMockedStatic = Mockito.mockStatic(JWTUtils.class);
             MockedStatic<UserProfileRepository> userProfileRepositoryMockedStatic = Mockito.mockStatic(UserProfileRepository.class)) {

            jwtUtilsMockedStatic.when(JWTUtils::getClientIdContextKey).thenReturn(userId);
            userProfileRepositoryMockedStatic.when(() -> UserProfileRepository.findById(userId)).thenReturn(Optional.of(testUserProfile));
            userProfileRepositoryMockedStatic.when(() -> UserProfileRepository.updateEntity(testUserProfile)).thenReturn(true);

            ChangeInfoRequest request = ChangeInfoRequest.newBuilder()
                    .setName("New Name")
                    .setDateOfBirth("1990-01-01")
                    .build();

            ChangeInfoResponse response = blockingStub.changeInfo(request);

            assertTrue(response.getCompleted());
            assertEquals("New Name", response.getNewInfo().getName());
            assertEquals("1990-01-01", response.getNewInfo().getDateOfBirth());
        }
    }

    @Test
    public void testGiveFeedback() {
        String userId = "testUser";
        User testUser = new User();

        try (MockedStatic<JWTUtils> jwtUtilsMockedStatic = Mockito.mockStatic(JWTUtils.class);
             MockedStatic<StatsService> statsServiceMockedStatic = Mockito.mockStatic(StatsService.class)) {

            jwtUtilsMockedStatic.when(JWTUtils::getClientIdContextKey).thenReturn(userId);
            statsServiceMockedStatic.when(() -> StatsService.getUser(userId)).thenReturn(testUser);

            FeedbackRequest request = FeedbackRequest.newBuilder()
                    .setNegative("Not satisfied")
                    .setPositive("Great!")
                    .build();

            FeedbackResponse response = blockingStub.giveFeedback(request);

            assertEquals("Not satisfied", testUser.getNegativeFeedback());
            assertEquals("Great!", testUser.getPositiveFeedback());
        }
    }

    @Test
    public void testCheckPassword() {
        String userId = "testUser";
        UserProfile testUserProfile = new UserProfile();
        testUserProfile.setHashedPassword(UserProfile.hashPassword("correctPassword"));

        try (MockedStatic<JWTUtils> jwtUtilsMockedStatic = Mockito.mockStatic(JWTUtils.class);
             MockedStatic<UserProfileRepository> userProfileRepositoryMockedStatic = Mockito.mockStatic(UserProfileRepository.class)) {

            jwtUtilsMockedStatic.when(JWTUtils::getClientIdContextKey).thenReturn(userId);
            userProfileRepositoryMockedStatic.when(() -> UserProfileRepository.findById(userId)).thenReturn(Optional.of(testUserProfile));

            CheckPasswordRequest request = CheckPasswordRequest.newBuilder()
                    .setPassword("correctPassword")
                    .build();

            CheckPasswordResponse response = blockingStub.checkPassword(request);

            assertTrue(response.getCorrect());

            request = CheckPasswordRequest.newBuilder()
                    .setPassword("wrongPassword")
                    .build();

            response = blockingStub.checkPassword(request);

            assertFalse(response.getCorrect());
        }
    }
}
